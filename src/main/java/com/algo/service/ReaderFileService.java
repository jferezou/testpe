package com.algo.service;

import java.io.IOException;
import org.apache.tika.exception.TikaException;
import com.algo.exception.FichierInvalideException;

@FunctionalInterface
public interface ReaderFileService {

	/**
	 * Méthode lancant la lecture du fichier et la conversion de chaque ligne
	 */
	void readAndLaunch() throws FichierInvalideException,TikaException, IOException ;
}
