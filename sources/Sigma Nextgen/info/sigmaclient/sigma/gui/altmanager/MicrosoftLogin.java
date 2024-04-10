package info.sigmaclient.sigma.gui.altmanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpServer;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

@Getter
@Setter
public class MicrosoftLogin implements Closeable{
    private final String CLIENT_ID = "67b74668-ef33-49c3-a75c-18cbb2481e0c";
    private final String REDIRECT_URI = "http://localhost:4689/sad";
    private final String SCOPE = "XboxLive.signin%20offline_access";
    private final Gson gson = new GsonBuilder().create();
    private final JsonParser parser = new JsonParser();

    private final Logger logger = LogManager.getLogger("Microsoft Login");

    private HttpServer httpServer = null;
    private boolean logged = false;
    private String uuid;
    private String userName;
    private String accessToken;

    public boolean suss = false;

    public MicrosoftLogin() {
        logger.info("Try to create http server");
        if(httpServer != null){
            httpServer.stop(0);
            httpServer = null;
        }
        try {
            httpServer = HttpServer.create(new InetSocketAddress("localhost", 4689), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        httpServer.createContext("/sad", exchange -> {
            String query = exchange.getRequestURI().getQuery();

            if (query.contains("code")) {
                String code = query.split("code=")[1];

                String[] microsoftTokenAndRefreshToken = getMicrosoftTokenAndRefreshToken(code);
                String xBoxLiveToken = getXBoxLiveToken(microsoftTokenAndRefreshToken[0]);

                String[] xstsTokenAndHash = getXSTSTokenAndUserHash(xBoxLiveToken);
                String accessToken = getAccessToken(xstsTokenAndHash[0], xstsTokenAndHash[1]);


                JsonObject jsonObject = parser.parse(
                        get(
                                "https://api.minecraftservices.com/minecraft/profile",
                                Map.of("Authorization", "Bearer " + accessToken)
                        )
                ).getAsJsonObject();

                this.uuid = jsonObject.get("id").getAsString();
                this.userName = jsonObject.get("name").getAsString();
                this.accessToken = accessToken;
                this.logged = true;

                suss = true;
            }
        });
        httpServer.start();

        logger.info("Start http server");
        logger.info("Opening browser");
        try {
            String URL = "https://login.live.com/oauth20_authorize.srf?client_id=<client_id>&redirect_uri=<redirect_uri>&response_type=code&display=touch&scope=<scope>"
                    .replace("<client_id>", CLIENT_ID)
                    .replace("<redirect_uri>", REDIRECT_URI)
                    .replace("<scope>", SCOPE);
            Util.getOSType().openURI(new URI(URL));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private String[] getMicrosoftTokenAndRefreshToken(String code) {
        JsonObject jsonObject;
        try {
            jsonObject = parser.parse(
                    post(
                            "https://login.live.com/oauth20_token.srf",
                            "client_id=" + CLIENT_ID + "&code=" + code + "&grant_type=authorization_code&redirect_uri=" + REDIRECT_URI + "&scope=" + SCOPE,
                            Map.of("Content-Type", "application/x-www-form-urlencoded")
                    )
            ).getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new String[]{
                jsonObject.get("access_token").getAsString(),
                jsonObject.get("refresh_token").getAsString()
        };
    }

    @SuppressWarnings("HttpUrlsUsage")
    private String getXBoxLiveToken(String microsoftToken) {
        JsonObject paramObj = new JsonObject();
        JsonObject propertiesObj = new JsonObject();

        propertiesObj.addProperty("AuthMethod", "RPS");
        propertiesObj.addProperty("SiteName", "user.auth.xboxlive.com");
        propertiesObj.addProperty("RpsTicket", "d=" + microsoftToken);
        paramObj.add("Properties", propertiesObj);
        paramObj.addProperty("RelyingParty", "http://auth.xboxlive.com");
        paramObj.addProperty("TokenType", "JWT");

        JsonObject jsonObject;
        try {
            jsonObject = parser.parse(
                    post(
                            "https://user.auth.xboxlive.com/user/authenticate",
                            gson.toJson(paramObj),
                            Map.of(
                                    "Content-Type", "application/json",
                                    "Accept", "application/json"
                            )
                    )
            ).getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return jsonObject.get("Token").getAsString();
    }

    private String[] getXSTSTokenAndUserHash(String xboxLiveToken) {
        JsonObject paramObj = new JsonObject();
        JsonObject propertiesObj = new JsonObject();

        propertiesObj.addProperty("SandboxId", "RETAIL");
        propertiesObj.add("UserTokens", parser.parse(gson.toJson(Collections.singletonList(xboxLiveToken))));
        paramObj.add("Properties", propertiesObj);
        paramObj.addProperty("RelyingParty", "rp://api.minecraftservices.com/");
        paramObj.addProperty("TokenType", "JWT");

        JsonObject jsonObject;
        try {
            jsonObject = parser.parse(
                    post(
                            "https://xsts.auth.xboxlive.com/xsts/authorize",
                            gson.toJson(paramObj),
                            Map.of("Content-Type", "application/json")
                    )
            ).getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new String[]{
                jsonObject.get("Token").getAsString(),
                jsonObject.get("DisplayClaims").getAsJsonObject().get("xui").getAsJsonArray().get(0).getAsJsonObject().get("uhs").getAsString()
        };
    }

    @SuppressWarnings("SpellCheckingInspection")
    private String getAccessToken(String xstsToken, String uhs) {
        JsonObject paramObj = new JsonObject();
        paramObj.addProperty("identityToken", "XBL3.0 x=" + uhs + ";" + xstsToken);

        JsonObject jsonObject;
        try {
            jsonObject = parser.parse(
                    post(
                            "https://api.minecraftservices.com/authentication/login_with_xbox",
                            gson.toJson(paramObj),
                            Map.of(
                                    "Content-Type", "application/json",
                                    "Accept", "application/json"
                            )
                    )
            ).getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return jsonObject.get("access_token").getAsString();
    }

    private String post(String urlString, String param, Map<String, String> requestProperty) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setConnectTimeout(20000);
        connection.setReadTimeout(20000);
        connection.setRequestMethod("POST");

        for (Map.Entry<String, String> entry : requestProperty.entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(param.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();

        StringBuilder readText = new StringBuilder();
        InputStream inputStream = connection.getInputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            readText.append(new String(buffer, 0, len, StandardCharsets.UTF_8));
        }
        inputStream.close();

        connection.disconnect();

        return readText.toString();
    }

    @SuppressWarnings("SameParameterValue")
    private String get(String urlString, Map<String, String> requestProperty) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setDoInput(true);
        connection.setRequestMethod("GET");

        connection.setConnectTimeout(20000);
        connection.setReadTimeout(20000);

        for (Map.Entry<String, String> entry : requestProperty.entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }

        StringBuilder readText = new StringBuilder();
        InputStream inputStream = connection.getInputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            readText.append(new String(buffer, 0, len, StandardCharsets.UTF_8));
        }
        inputStream.close();

        connection.disconnect();

        return readText.toString();
    }

    @Override
    public void close() {
        if (httpServer != null) {
            httpServer.stop(0);
        }
    }
}
