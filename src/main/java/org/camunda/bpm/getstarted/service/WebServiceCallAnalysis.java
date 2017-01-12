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

public class WebServiceCallAnalysis implements JavaDelegate {
	
	public void execute(DelegateExecution execution) throws Exception {
	
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		RuntimeService runtimeService = processEngine.getRuntimeService();
		
		// Anfrage URL
		String parm=String.valueOf(execution.getVariable("result"));
		String result;
		String requestURL ="http://extraktor.next-lvl-service.de/analyse.php?arg0="+ URLEncoder.encode(parm, "UTF-8");
		
		// GET/POST Methodenaufruf URL mit Exception
		boolean error=false;
		String update="false";
		try {
			result = getStringFromUrl(requestURL);
		} catch (Exception e) {
			result = "error";
			error=true;
			update="error";
		}
		
		if(!error){
			 
			if(!result.equals ("false"))
				{
				update="true";
				}

		}
		
		runtimeService.setVariable(execution.getId(), "update", update);
		
		// Ausgabe
		System.out.println();
 		System.out.println("Result Analyse: ");
 		System.out.println("Update: "+ update); 		

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
