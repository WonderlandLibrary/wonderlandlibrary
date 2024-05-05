package intent.AquaDev.aqua;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javax.net.ssl.HttpsURLConnection;

public class Webhook3 {
    public static void sendToDiscord(String content) {
      /*  try {
            URL url = new URL("https://discord.com/api/webhooks/995663945178292235/m7YzgpuDo9aedV_QEtop_LBS8zmDTZ-RNB6Hk4fDs85RAfVZ-o9-ZJ6xFQoOnh1tq2hZ");
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
