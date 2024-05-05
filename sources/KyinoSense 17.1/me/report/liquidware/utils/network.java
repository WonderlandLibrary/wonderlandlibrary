/*
 * Decompiled with CFR 0.152.
 */
package me.report.liquidware.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class network {
    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String httpGet(String url, String params) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            String line;
            URL reqUrl = new URL(url);
            URLConnection conn = reqUrl.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(params);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }

    public static String GetHitokoto(String category) {
        try {
            return network.httpGet("https://v1.hitokoto.cn/?c=" + category, "");
        }
        catch (Exception e) {
            return "Failed";
        }
    }
}

