package org.camunda.bpm.getstarted.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class WebServiceCallStoring implements JavaDelegate {
	
	public void execute(DelegateExecution execution) throws Exception {
	
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		RuntimeService runtimeService = processEngine.getRuntimeService();
		
		// Anfrage URL
		String parm=String.valueOf(execution.getVariable("result"));
	
		String requestURL ="http://extraktor.next-lvl-service.de/save.php?arg0="+ URLEncoder.encode(parm, "UTF-8");
		
		// GET/POST Methodenaufruf URL + Speichern
		String result; 
				
		try {
			getStringFromUrl(requestURL);
			result = "erfolg";
		} catch (Exception e) {
			result ="error";
		}
		runtimeService.setVariable(execution.getId(), "result", result);			
	
		
		// Ausgabe
		System.out.println();
 		System.out.println("Result Storing: ");
 		System.out.println("Storing: "+ result);		

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
