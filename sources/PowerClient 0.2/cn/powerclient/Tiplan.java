/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import me.AveReborn.Client;
import org.lwjgl.opengl.Display;

public class Tiplan {
    public String tiplan = this.sendGet("https://sslapi.hitokoto.cn/?charset=gbk", null);

    public Tiplan() {
        String hitokoto = "null";
        String from = "";
        boolean success = false;
        int failed = 0;
        while (!success) {
            try {
                if (failed > 5) break;
                JsonParser parser = new JsonParser();
                JsonObject object = (JsonObject)parser.parse(this.tiplan);
                hitokoto = object.get("hitokoto").getAsString();
                from = object.get("from").getAsString();
                success = true;
            }
            catch (Exception parser) {
                // empty catch block
            }
        }
        if (hitokoto.equalsIgnoreCase("null")) {
            Display.setTitle(String.valueOf(Client.CLIENT_NAME) + " v" + Client.CLIENT_VER + " | ERROR:Request Tiplan Failed");
        } else {
            Display.setTitle(String.valueOf(Client.CLIENT_NAME) + " v" + Client.CLIENT_VER + " | " + hitokoto + "  ---" + from);
        }
    }

    public String sendGet(String url, String param) {
        String result;
        result = "";
        BufferedReader in2 = null;
        try {
            try {
                String line;
                String urlNameString = url;
                URL realUrl = new URL(urlNameString);
                URLConnection connection = realUrl.openConnection();
                connection.setDoOutput(true);
                connection.setReadTimeout(99781);
                connection.setRequestProperty("accept", "*/*");
                connection.setRequestProperty("connection", "Keep-Alive");
                connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1) ZiMinClient;Chrome 69");
                connection.connect();
                Map<String, List<String>> map = connection.getHeaderFields();
                for (String string : map.keySet()) {
                }
                in2 = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = in2.readLine()) != null) {
                    result = String.valueOf(result) + line + "\n";
                }
            }
            catch (Exception urlNameString) {
                try {
                    if (in2 != null) {
                        in2.close();
                    }
                }
                catch (Exception exception) {}
            }
        }
        finally {
            try {
                if (in2 != null) {
                    in2.close();
                }
            }
            catch (Exception exception) {}
        }
        return result;
    }
}

