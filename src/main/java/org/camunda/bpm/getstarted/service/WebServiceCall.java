package org.camunda.bpm.getstarted.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
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

public class WebServiceCall implements JavaDelegate {
	
	public void execute(DelegateExecution arg0) throws Exception {
	

//		for(Map.Entry<String, Object> entry : arg0.getVariables().entrySet()) {
//			System.out.println(entry.getKey()+" -> "+entry.getValue());
//		}
		
		// Anfrage URL
		String requestURL ="http://extraktor.next-lvl-service.de/extrakt.php";
		
		// GET/POST Methodenaufruf URL
		String result = getStringFromUrl(requestURL);
		
		// Request speichern 
		arg0.setVariable("result", result);
		
		// Ausgabe
		System.out.println();
		System.out.println("Prozessname: "+ arg0.getVariable("eingabe"));
		System.out.println(requestURL);
		System.out.println("Result Extraction: "+result);
 		

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
