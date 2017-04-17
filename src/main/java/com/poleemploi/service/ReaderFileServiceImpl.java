package com.poleemploi.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.poleemploi.exception.FichierInvalideException;

@Service("ReaderFileService")
public class ReaderFileServiceImpl implements ReaderFileService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReaderFileServiceImpl.class);

	@Value("${fichier}")
	private String filePath;
	@Value("${fichierResultat}")
	private String fichierResultat;

	@Value("${convertirVersJavanais}")
	private boolean convertirVersJavanais;

	@Value("${monoThread}")
	private boolean monoThreaded;
	@Value("${treadNumber}")
	private int treadNumber;

	@Autowired
	TransformService transformService;

	@Override
	public void readAndLaunch() throws FichierInvalideException, TikaException, IOException {
		LOGGER.info("Début du traitement");
		// Vérifie que le fichier existe
		File file = new File(this.filePath);
		String fileName = file.getName();
		if (!file.exists()) {
			throw new FichierInvalideException("Ce fichier n'existe pas : " + this.filePath);
		}
		// Vérifie que ce n'est pas un répertoire
		else if (file.isDirectory()) {
			throw new FichierInvalideException("C'est un répertoire : " + this.filePath);
		}
		// verifie que ce soit bien un txt
		else {
			InputStream stream = new FileInputStream(file);
			InputStream bufferedInputstream = new BufferedInputStream(stream);
			MediaType fileInfo = this.getFileInfo(fileName, bufferedInputstream);
			if (!MediaType.TEXT_PLAIN.equals(fileInfo.getBaseType())) {
				throw new FichierInvalideException("Le fichier doit être au format " + MediaType.TEXT_PLAIN + " : " + this.filePath);
			}
			// si c'est ok on continue
			else {
				// on créé notre fichier de sortie
				this.multiThreaded(file);
			}
		}

	}

	/**
	 * Traitement multithread à l'aide des Future
	 * 
	 * @param file
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void multiThreaded(File file) throws IOException {

		try (final FileWriter fw = new FileWriter(this.fichierResultat); final BufferedWriter bw = new BufferedWriter(fw);) {

			ExecutorService service = Executors.newFixedThreadPool(this.treadNumber);
			// traite plusieurs lignes en parallèle, on utilise une liste pour les réécrire dans le même ordre
			List<CompletableFuture<String>> cptList = new ArrayList<>();
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				for (String line; (line = br.readLine()) != null;) {

					final String myLine = line;
					if (this.convertirVersJavanais) {
						CompletableFuture<String> cpt = CompletableFuture.supplyAsync(() -> this.transformService.convertToJavanais(myLine), service);
						cptList.add(cpt);

					} else {
						CompletableFuture<String> cpt = CompletableFuture.supplyAsync(() -> this.transformService.convertFromJavanais(myLine), service);
						cptList.add(cpt);
					}
				}

				// on écrit les résultats dans le fichier
				for (CompletableFuture<String> cpt : cptList) {
					bw.append(cpt.get());
					bw.newLine();
				}
			} catch (InterruptedException e) {
				LOGGER.error("Erreur lors de la fermeture du writer", e);
				Thread.currentThread().interrupt();
			} catch (ExecutionException e) {
				LOGGER.error("Erreur lors de la fermeture du writer", e);
			} finally {
				try {
					// on attend 1min la fin des threads
					service.shutdown();
					service.awaitTermination(1, TimeUnit.MINUTES);
				} catch (InterruptedException e) {
					LOGGER.error("Erreur lors du shutdown du threadPool", e);
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	/**
	 * Méthode utilisant apache Tika pour récupérer les infos du fichier :
	 * mimetype, charset ....
	 * 
	 * @param fileName
	 * @param stream
	 * @return
	 * @throws TikaException
	 * @throws IOException
	 */
	private MediaType getFileInfo(final String fileName, final InputStream stream) throws TikaException, IOException {
		TikaConfig tika = new TikaConfig();
		Metadata metadata = new Metadata();
		metadata.set(Metadata.RESOURCE_NAME_KEY, fileName);
		MediaType mimetype = tika.getDetector().detect(stream, metadata);
		return mimetype;
	}

}
