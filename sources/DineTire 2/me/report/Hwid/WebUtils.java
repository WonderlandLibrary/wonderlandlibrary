package me.report.Hwid;

import java.io.*;
import java.net.*;

public class WebUtils {
    public static String get(String url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while((inputLine = in.readLine())!= null) {
            response.append(inputLine);
            response.append("\n");
        }

        in.close();

        return response.toString();
    }


}
