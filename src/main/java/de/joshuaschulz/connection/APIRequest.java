package de.joshuaschulz.connection;

import com.google.gson.Gson;
import com.sun.org.apache.bcel.internal.generic.GETFIELD;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class APIRequest {
    private static final String API_KEY = "?api_key=6v9qvvdHbEckYYAizpz8AYPozpJJdiOgJC1yJz9s";
    private final URL requestURL;
    //TODO: Handle Exceptions
   public APIRequest(URL apiURL, Map<String,String> parameters) throws UnsupportedEncodingException, MalformedURLException {
        requestURL=new URL(new URL(apiURL,API_KEY),ParameterStringBuilder.getParamsString(parameters));
   }
   public APIRequest(String apiURL)throws Exception{
       requestURL=new URL(apiURL+API_KEY);
   }

   public String performAPICall() throws IOException{
       HttpURLConnection con = (HttpURLConnection) requestURL.openConnection();
       con.setRequestMethod("GET");
       int responseCode = con.getResponseCode();
       BufferedReader in = new BufferedReader(
               new InputStreamReader(con.getInputStream()));
       String inputLine;
       StringBuffer content = new StringBuffer();
       while ((inputLine = in.readLine()) != null) {
           content.append(inputLine);
       }
       in.close();
       con.disconnect();

       return content.toString();
   }
}
