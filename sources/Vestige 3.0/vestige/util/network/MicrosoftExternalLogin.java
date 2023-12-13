package vestige.util.network;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.HttpServer;

import net.minecraft.util.Session;
import vestige.ui.menu.AltLoginScreen;
import vestige.util.IMinecraft;

public class MicrosoftExternalLogin implements IMinecraft {

    private AltLoginScreen screen;

    public MicrosoftExternalLogin(AltLoginScreen screen) {
        this.screen = screen;
    }

    public void start() throws Exception {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(11999), 0);
        String authUrl = "https://login.live.com/oauth20_authorize.srf?client_id=18b632af-d7fb-43bc-aabd-6e4f3e5920bd&response_type=code&redirect_uri=http://localhost:11999/&scope=XboxLive.signin%20offline_access";

        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(authUrl);
        clip.setContents(stringSelection, stringSelection);

        screen.setStatus("Login URL copied to your clipboard.");
        
        httpServer.createContext("/", exchange -> {
            String code = exchange.getRequestURI().toString();

            try {
            	String message = "Currently logging into account, you can close this window.";
            	
            	exchange.sendResponseHeaders(200, message.length());
                OutputStream responseBody = exchange.getResponseBody();
                responseBody.write(message.getBytes(StandardCharsets.UTF_8));
                responseBody.close();

                screen.setStatus("Logging in...");

                URL url = new URL("http://vestige-client.net:11999" + code);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("GET");

                con.setDoOutput(true);

                OutputStream os = con.getOutputStream();

                os.close();

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String response = in.readLine();

                if(response.equals("failed")) {
                    screen.setStatus("Login failed !");
                } else {
                    String[] responseParts = response.split(":");

                    if(responseParts.length == 3) {
                        mc.setSession(new Session(responseParts[0], responseParts[1], responseParts[2], "mojang"));

                        screen.setStatus("Successfully logged into " + mc.getSession().getUsername());
                    } else {
                        screen.setStatus("Login failed : server-side issue.");
                    }
                }

                httpServer.stop(0);
            } catch (Exception e) {
            	e.printStackTrace();

                httpServer.stop(0);

                screen.setStatus("Login failed !");
            }
        });
        
        httpServer.setExecutor(null);
        httpServer.start();
    }
	
}