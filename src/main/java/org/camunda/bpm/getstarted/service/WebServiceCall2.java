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

public class WebServiceCall2 implements JavaDelegate {
	
	public void execute(DelegateExecution arg0) throws Exception {
	

//		for(Map.Entry<String, Object> entry : arg0.getVariables().entrySet()) {
//			System.out.println(entry.getKey()+" -> "+entry.getValue());
//		}
		
		// Anfrage URL
		String parm=String.valueOf(arg0.getVariable("result"));
		
	
		String requestURL ="http://extraktor.next-lvl-service.de/extrakt.php?arg0="+ URLEncoder.encode(parm, "UTF-8");
		
		// GET/POST Methodenaufruf URL
		String result = getStringFromUrl(requestURL);
		String update = "false"; 
		if(result.equals ("null"))
			{
			arg0.setVariable("update", update);
			}
		else
		{
			update="true";
			arg0.setVariable("update", update);
			// Request speichern 
			String save = result;
			arg0.setVariable("result", save);
		}
		
		// Ausgabe
		
		System.out.println();
		System.out.println(requestURL);
 		System.out.println("Direktes Result-2: "+result);
 		System.out.println("arg0-2: "+ String.valueOf(arg0.getVariable("result3")));
 		System.out.println("Update: "+ update);
 		

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
