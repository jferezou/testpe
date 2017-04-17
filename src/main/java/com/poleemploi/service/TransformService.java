package com.poleemploi.service;

public interface TransformService {

	/**
	 * Permet de convertir du texte vers le javanais
	 */
	String convertToJavanais(final String line);
	
	/**
	 * Permet de convertir du texte du javanais vers la langue normale
	 */
	String convertFromJavanais(final String line);
}
