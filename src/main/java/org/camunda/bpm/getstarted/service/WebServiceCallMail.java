package org.camunda.bpm.getstarted.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class WebServiceCallMail implements JavaDelegate {
	
	public void execute(DelegateExecution execution) throws Exception {
		
		// Webservice URL und Datentyp zum speichern GET/POST Abfrage/Antwort
		String requestURL ="http://fbwsemantic.fh-brandenburg.de:9000/projects/extractor/mail.php?error=";
		String result = "";
		
		// Datentypen zum Aufruf der gespeicherten 
		String extract = "";
		String analyse = "";
		String enrich = "";
		String store = "";
		
		//Aufruf der gespeicherten "Error"-Statements, zu den einzelnen Webservices
		extract = (String) execution.getVariable("extract");
		analyse = (String) execution.getVariable("analyse");
		enrich = (String) execution.getVariable("enrich");
		store = (String) execution.getVariable("store");
		
		// Abfrage welcher Service antwortet mit dem entsprechendem Methodenaufruf und
		// Speicherung der Tomcat Consolen-Ausgabe
		if(extract=="error"){
			sendStringToUrl(requestURL+"extractFailed");
			result = "Der Extraktionsservice antwortet nicht";
		} 
		if (analyse=="error") {
			sendStringToUrl(requestURL+"analyseFailed");
			result = "Der Analyseservice antwortet nicht";
		}
		if (enrich=="error"){
			sendStringToUrl(requestURL+"enrichFailed");
			result = "Der Anreicherungsservice antwortet nicht";
		}
		if (store=="error"){
			sendStringToUrl(requestURL+"storeFailed");
			result = "Der Speicherungsservice antwortet nicht";
		}

		// Ausgabe auf der Tomcat-Console 
		System.out.println();
 		System.out.println("Benachrichtungsemail wurde versendet");
 		System.out.println("Status: "+ result);		

	}
	
	// GET/POST Methode
	private String sendStringToUrl(String urlToReadFrom) throws Exception {
        
        URL url = new URL(urlToReadFrom);
    
        URLConnection con = url.openConnection();
        final Reader reader = new InputStreamReader(con.getInputStream());
        final BufferedReader br = new BufferedReader(reader);        
        String line = "";
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {

        	sb.append(line);
        }        
        br.close();
        return sb.toString();
	}
}
