package org.camunda.bpm.getstarted.service;

import java.io.BufferedReader;
import java.net.*;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class WebServiceCall4 implements JavaDelegate {
	
	public void execute(DelegateExecution arg0) throws Exception {
	

//		for(Map.Entry<String, Object> entry : arg0.getVariables().entrySet()) {
//			System.out.println(entry.getKey()+" -> "+entry.getValue());
//		}
		
		// Anfrage URL
		String parm=String.valueOf(arg0.getVariable("result"));
		String parm2=String.valueOf(arg0.getVariable("enrich"));
	
		String requestURL ="http://extraktor.next-lvl-service.de/save.php?arg0="+ URLEncoder.encode(parm, "UTF-8") + "&save="+ URLEncoder.encode(parm2, "UTF-8") ;
		
		// GET/POST Methodenaufruf URL + Speichern
		String result = getStringFromUrl(requestURL);
	
		
		// Ausgabe
		
		System.out.println();
 		System.out.println("Result Storing: ");
 		System.out.println("Storing: "+ result);
 		//System.out.println("Result: "+requestURL);


 		

	}
	
	// GET/POST Methode
	private String getStringFromUrl(String urlToReadFrom) throws Exception {
		
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        } };
       
        final SSLContext sc = SSLContext.getInstance("SSL"); 
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        
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
