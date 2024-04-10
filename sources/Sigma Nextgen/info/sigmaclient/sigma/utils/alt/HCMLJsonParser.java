package info.sigmaclient.sigma.utils.alt;


import com.alibaba.fastjson.JSONObject;
import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.config.alts.Alt;
import info.sigmaclient.sigma.config.alts.AltConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static info.sigmaclient.sigma.minimap.minimap.Minimap.mc;
import static info.sigmaclient.sigma.music.youtubedl.YoutubeVideoHelper.visitSite2;

public class HCMLJsonParser {
    public static String sendGetRequest(String url, String userAgent) throws IOException {
        URL requestUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", userAgent);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return response.toString();
        } else {
            throw new IOException("Request failed with response code: " + responseCode);
        }
    }
    public boolean parserCheck() {
            try {
                String clipboard = mc.keyboardListener.getClipboardString();
                if (clipboard.isEmpty()) return false;
                String copy = clipboard;
                if (copy.startsWith("https://pan.nyaproxy.xyz/")) {
                    // https://pan.nyaproxy.xyz/get/ujF0QxYOhU/NameSpider93708.txt
                    try {
                        String l = sendGetRequest(copy.replace("https://pan.nyaproxy.xyz/", "https://pan.nyaproxy.xyz/get/"), "Sigma NextGen");
                        String n = copy.split("/")[copy.split("/").length - 1].replace(".txt", "");
                        String uuid = visitSite2("https://playerdb.co/api/player/minecraft/" + n);
                        System.out.println(uuid);
                        JSONObject jsonUUID = JSONObject.parseObject(uuid);
                        uuid = jsonUUID.getJSONObject("data").getJSONObject("player").getString("raw_id");
                        System.out.println(uuid);
                        mc.session = new Session(n, uuid, l, "Legacy");
                        AltConfig.Instance.alts.add(new Alt(mc.session.getUsername(), mc.session.getPlayerID(), mc.session.getToken()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
//                System.out.println(copy);
                String uuid = null, name = null, accessToken = null, type = null;
                while (copy.contains("\"")) {
                    int index = copy.indexOf("\"");
                    copy = copy.replaceFirst("\"", "");
                    if (copy.contains("\"")) {
                        int index2 = copy.indexOf("\"");
                        copy = copy.replaceFirst("\"", "");
                        if (copy.contains(": ")) {
                            int index3 = copy.indexOf(": ");
                            copy = copy.replaceFirst(": \"?", "");
                            if (copy.contains(",\n")) {
                                int index4 = copy.indexOf(",\n");
                                if (copy.charAt(index4 - 1) == '"') {
                                    index -= 1;
                                }
                                copy = copy.replaceFirst("\"?,\n", "");
                                String parse = copy.substring(index3, index4);
                                String PP = copy.substring(index, index2);
                                System.out.printf("key: %s, value: %s", PP, parse);
                                switch (PP) {
                                    case " uuid":
                                        uuid = parse.trim();
                                        break;
                                    case " accessToken":
                                        accessToken = parse.trim();
                                        break;
                                    case " displayName":
                                        name = parse.trim();
                                        break;
                                }
                            }
                        }
                    }
                }
                if (uuid != null && accessToken != null && name != null) {
                    Minecraft.getInstance().session = new Session(
                            name, uuid, accessToken, "Legacy"
                    );
                    AltConfig.Instance.alts.add(new Alt(
                            Minecraft.getInstance().session.getUsername(),
                            Minecraft.getInstance().session.getPlayerID(),
                            Minecraft.getInstance().session.getToken()));
                    SigmaNG.getSigmaNG().configManager.saveDefaultConfig();
                    return true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        return false;
    }
}
