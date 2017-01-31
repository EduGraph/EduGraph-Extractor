package org.camunda.bpm.getstarted.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class WebServiceCallAnalysis implements JavaDelegate {
	
	public void execute(DelegateExecution execution) throws Exception {
	
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		RuntimeService runtimeService = processEngine.getRuntimeService();
		
		// Webservice URL und Datentyp zum speichern GET/POST Abfrage/Antwort 
		String result;
		String requestURL ="http://fbwsemantic.fh-brandenburg.de:9000/projects/extractor/analyse.php";
		// Standard Erwartung ist false (keine Triple Veränderung)
		boolean error=false;
		String update="false";
		// Standard Tomcat-Consolen Ausgabe
		String msg="Keine Aktualisierung - Keine Veränderung festgestellt";
		
		// Abfrage ob der Service antwortet, wenn nicht greift das Exception Handling
		try {
			result = getStringFromUrl(requestURL);
		} catch (Exception e) {
			result = "error";
			error=true;
			update="error";
			msg="Fehler - Service nicht erreichbar (Zeitüberschreitung)";
		}
		
		// Abfrage: Wenn kein Error vorliegt und die Anwort true ist (Triple haben sich verändert)
		// wenn keine Veränderung vorliegt wird Standard Tomcat-Consolen Ausgabe genommen
		if(!error){
			 
			if(!result.equals ("false"))
				{
				update="true";
				msg="Aktualisierung durchgeführt";
				}

		}
		// Speichern der Antwort des Webservices (processEngine)
		runtimeService.setVariable(execution.getId(), "analyse", update);
		
		// Ausgabe auf der Tomcat-Console 
		System.out.println();
 		System.out.println("Ergebnis Analyse: " + msg);		

	}
	
	// GET/POST Methode
	private String getStringFromUrl(String urlToReadFrom) throws Exception {
        
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
