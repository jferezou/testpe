package com.poleemploi;

import java.io.IOException;
import java.util.Date;
import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.poleemploi.exception.FichierInvalideException;
import com.poleemploi.service.ReaderFileService;
import com.poleemploi.service.ReaderFileServiceImpl;

/**
 * Classe principale
 * @author jferezou
 *
 */
public class Javanais {

	private static final Logger LOGGER = LoggerFactory.getLogger(Javanais.class);
	public static void main(String[] args) {
		Date debut = new Date();
		LOGGER.info("Debut traitement");
		try {
			//chargement contexte spring
			ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
			ReaderFileService reader = applicationContext.getBean(ReaderFileServiceImpl.class);
			reader.readAndLaunch();
		}
		catch(FichierInvalideException | TikaException | IOException e) {
			LOGGER.error("Erreur lors du traitement : ", e);
		}
		Date fin = new Date();
		LOGGER.info("Fin traitement en : {} ms", fin.getTime() - debut.getTime() );
	}

}