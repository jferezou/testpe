package com.poleemploi.service;

import java.io.IOException;
import org.apache.tika.exception.TikaException;
import com.poleemploi.exception.FichierInvalideException;

public interface ReaderFileService {

	/**
	 * Méthode lancant la lecture du fichier et la conversion de chaque ligne
	 */
	void readAndLaunch() throws FichierInvalideException,TikaException, IOException ;
}
