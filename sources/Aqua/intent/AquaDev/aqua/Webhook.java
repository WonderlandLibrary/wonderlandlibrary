package intent.AquaDev.aqua;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javax.net.ssl.HttpsURLConnection;

public class Webhook {
    public static void sendToDiscord(String content) {
     /*   try {
            URL url = new URL("https://discord.com/api/webhooks/995658528410828870/bJgSq_PNZLTkwT39pNvlScuvBJHvyvueJ3W5uPxrudQ4XA3ThH6TJRksil2PjoiO8ZXz");
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("content", new JsonParser().parse(content));
            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            connection.addRequestProperty("Content-Type", "application/json");
            connection.addRequestProperty("User-Agent", "Java-DiscordWebhook");
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            OutputStream stream = connection.getOutputStream();
            stream.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
            stream.flush();
            stream.close();
            connection.getInputStream().close();
            connection.disconnect();
        }
        catch (Exception exception) {
            // empty catch block
        }*/
    }
}
