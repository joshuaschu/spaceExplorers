package de.joshuaschulz.connection;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class APIRequest {
    private static final String API_KEY = "?api_key=6v9qvvdHbEckYYAizpz8AYPozpJJdiOgJC1yJz9s";
    private final URL requestURL;
    //TODO: Handle Exceptions
   public APIRequest(URL apiURL, Map<String,String> parameters) throws Exception{
        requestURL=new URL(new URL(apiURL,API_KEY),ParameterStringBuilder.getParamsString(parameters));
   }
   public APIRequest(String apiURL)throws Exception{
       requestURL=new URL(apiURL+API_KEY);
   }

   public String performAPICall() throws Exception{
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
