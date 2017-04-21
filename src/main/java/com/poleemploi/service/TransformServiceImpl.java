package com.poleemploi.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("transformService")
@Transactional(propagation=Propagation.REQUIRES_NEW, isolation=Isolation.READ_COMMITTED, readOnly=false)
public class TransformServiceImpl implements TransformService{
	private static final Logger LOGGER = LoggerFactory.getLogger(TransformServiceImpl.class);
	
	@Value("${javanais}")
	private String javanaisValue;
	
	@Value("#{'${listeVoyelles}'.split(';')}") 
	 private List<Character> voyelles;
	
	@Value("#{'${listeConsonnes}'.split(';')}") 
	 private List<Character> consonnes;
		
	@Override
	/**
	 * Permet de convertir du texte vers le javanais
	 */
	public String convertToJavanais(final String line) {
		LOGGER.debug("Début conversion vers javanais");
		LOGGER.debug("Mot a ajouter : {}",this.javanaisValue);
		LOGGER.debug("Liste voyelles : {}",this.voyelles);
		LOGGER.debug("Liste consonnes : {}",this.consonnes);
		LOGGER.debug("Ligne à traiter : {}", line);
		StringBuilder strB = new StringBuilder();
		// si la ligne est vide, il n'y a rien à faire !
		if(!line.isEmpty()) {
			// on traite le cas particulier d'un mot qui commence par une voyelle en début de ligne
			if(this.voyelles.contains(line.charAt(0))) {
				strB.append(this.javanaisValue);
			}
			
			
			for (int i = 0 ; i < line.length() - 1;i++){
				boolean consonneOuEspace = false;
				char caractereEnCours = line.charAt(i);
				char caractereSuivant = line.charAt(i + 1);
				
				// on ajoute le caractère en cours
				strB.append(caractereEnCours);
				
				// le caractère est une consonne ou un espace
				if(this.consonnes.contains(caractereEnCours)) {
					consonneOuEspace = true;
				}
				// si le caractère suivant est une consonne ou espace et le suivant une voyelle, on ajoute le mot
				boolean isVoyelle = this.voyelles.contains(caractereSuivant);
				if(consonneOuEspace && isVoyelle) {
					strB.append(this.javanaisValue);
					consonneOuEspace = false;
				}	
			}
			// on ajoute le dernier caractère
			strB.append(line.substring(line.length() - 1));
		}
		String lineResult = strB.toString();
		LOGGER.debug("Fin conversion vers javanais : {}", lineResult);
		return lineResult;
	}

	@Override
	@Transactional
	/**
	 * Permet de convertir du texte du javanais vers la langue normale
	 */
	public String convertFromJavanais(final String line) {
		LOGGER.debug("Début conversion du javanais");
		LOGGER.debug("Mot a ajouter : {}",this.javanaisValue);
		LOGGER.debug("Liste voyelles : {}",this.voyelles);
		LOGGER.debug("Liste consonnes : {}",this.consonnes);
		LOGGER.debug("Ligne à traiter : {}", line);

		StringBuilder strB = new StringBuilder();
		// si la ligne est vide, il n'y a rien à faire !
		if(!line.isEmpty()) {
			String[] lineTab = line.split(this.javanaisValue);
			for(int i =0; i< lineTab.length - 1; i++) {
				String debut = lineTab[i];
				String fin = lineTab[i+1];
				if(!debut.isEmpty() && !fin.isEmpty()) {
					boolean dernierEstConsonneOuEspace = this.consonnes.contains(debut.charAt(debut.length()-1));
					boolean permierEstVoyelle = this.voyelles.contains(fin.charAt(0));
	
					strB.append(debut);
					if(!(dernierEstConsonneOuEspace && permierEstVoyelle)) {
						strB.append(this.javanaisValue);
					}
				}
			}
			
			// on ajoute toujours le dernier élément
			strB.append(lineTab[lineTab.length - 1]);
		}
		String lineResult = strB.toString();
		LOGGER.debug("Fin conversion du javanais : {}", lineResult);
		return lineResult;
	}

	

}
