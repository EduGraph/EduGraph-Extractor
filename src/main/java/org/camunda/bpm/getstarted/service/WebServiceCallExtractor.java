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

public class WebServiceCallExtractor implements JavaDelegate {
	
	public void execute(DelegateExecution execution)  throws Exception {
		
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		RuntimeService runtimeService = processEngine.getRuntimeService();
		
		// Anfrage URL
		String requestURL ="http://extraktor.next-lvl-service.de/extrakt.php";
		String result;
		
		try {
			
			// GET/POST Methodenaufruf URL
			result = getStringFromUrl(requestURL);
			
			// Request speichern 
			execution.setVariable("result", result);
		}
		catch (Exception e){
			result ="error";
		}		
		runtimeService.setVariable(execution.getId(), "result", result);
		
		// Ausgabe
		System.out.println();
		System.out.println("Prozessname: "+ execution.getVariable("eingabe"));
		System.out.println(requestURL);
		System.out.println("Result Extraction: "+result);
 		

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
