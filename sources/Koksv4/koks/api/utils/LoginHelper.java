package koks.api.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import koks.Koks;
import koks.api.Methods;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.util.Session;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class LoginHelper implements Methods {
    public static final LoginHelper INSTANCE = new LoginHelper();
    private Service currentService = Service.MOJANG;
    private final Class<YggdrasilMinecraftSessionService> yggdrasilMinecraftSessionServiceClass = YggdrasilMinecraftSessionService.class;
    private final Class<YggdrasilUserAuthentication> yggdrasilUserAuthenticationClass = YggdrasilUserAuthentication.class;
    private final HostnameVerifier originalHostVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
    private final SSLSocketFactory originalFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
    private final HostnameVerifier alteningHostVerifier = (hostname, sslSession) -> hostname.equals("authserver.thealtening.com") || hostname.equals("sessionserver.thealtening.com");
    private final SSLSocketFactory alteningFactory;

    public LoginHelper() {
        // note: this only works if the agent loaded correctly!
        try {
            System.out.println("Updating whitelisted domains");
            updateField(yggdrasilMinecraftSessionServiceClass, "WHITELISTED_DOMAINS", new String[]{".minecraft.net", ".mojang.com", ".thealtening.com"});
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            }, new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        assert sc != null;
        alteningFactory = sc.getSocketFactory();
    }

    /**
     * Switches the auth and session server to the required ip addresses and logs you into an account
     *
     * @param name     The username of the cracked user or an email address
     * @param password The password of the user leave it empty for a cracked or altening account
     * @return The session of the account or null if the login wasn't possible
     */
    public Session login(String name, String password) {
        if (password == null || password.equals("") && !name.contains("@alt.com")) {
            return new Session(name.substring(0, Math.min(15, name.length())), "-", "-", "mojang");
        } else {
            YggdrasilAuthenticationService yggdrasilAuthenticationService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
            switchService(name.contains("@alt.com") ? Service.THEALTENING : Service.MOJANG);

            YggdrasilUserAuthentication yggdrasilUserAuthentication = new YggdrasilUserAuthentication(yggdrasilAuthenticationService, Agent.MINECRAFT);
            yggdrasilUserAuthentication.setUsername(name);
            yggdrasilUserAuthentication.setPassword(name.contains("@alt.com") ? "DirtyMod" : password); // use dirtymod as altening password
            try {
                yggdrasilUserAuthentication.logIn();
                System.out.println("Login successful!");
                return new Session(yggdrasilUserAuthentication.getSelectedProfile().getName(), yggdrasilUserAuthentication.getSelectedProfile().getId().toString(), yggdrasilUserAuthentication.getAuthenticatedToken(), "mojang");
            } catch (Exception e) {
                System.out.println("Login failed!");
                return new Session(name, "-", "-", "mojang");
            }
        }
    }

    private final Gson parser = new GsonBuilder().create();

    public Session generateAlt(String apiKey) throws IOException {
        switchService(Service.THEALTENING);
        URL url = new URL("https://api.thealtening.com/v2/generate?key=" + apiKey);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        InputStream inputStream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder builder = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null) {
            builder.append(line).append("\n");
        }
        JsonObject json = parser.fromJson(builder.toString(), JsonObject.class);
        String token = json.get("token").getAsString();
        connection.disconnect();
        return login(token, Koks.name);
    }

    public void switchService(Service service) {
        if (currentService != service) {
            currentService = service;
            try {
                System.out.println("Switching to " + service.getName());
                // ssl
                switch (service) {
                    case MOJANG -> {
                        HttpsURLConnection.setDefaultSSLSocketFactory(originalFactory);
                        HttpsURLConnection.setDefaultHostnameVerifier(originalHostVerifier);
                    }
                    case THEALTENING -> {
                        HttpsURLConnection.setDefaultSSLSocketFactory(alteningFactory);
                        HttpsURLConnection.setDefaultHostnameVerifier(alteningHostVerifier);
                    }
                }
                // updating fields
                updateField(yggdrasilMinecraftSessionServiceClass, "BASE_URL", service.getSessionServer() + "session/minecraft/");
                updateField(yggdrasilMinecraftSessionServiceClass, "JOIN_URL", new URL(service.getSessionServer() + "session/minecraft/join"));
                updateField(yggdrasilMinecraftSessionServiceClass, "CHECK_URL", new URL(service.getSessionServer() + "session/minecraft/hasJoined"));

                updateField(yggdrasilUserAuthenticationClass, "BASE_URL", service.getAuthServer());
                updateField(yggdrasilUserAuthenticationClass, "ROUTE_AUTHENTICATE", new URL(service.getAuthServer() + "authenticate"));
                updateField(yggdrasilUserAuthenticationClass, "ROUTE_INVALIDATE", new URL(service.getAuthServer() + "invalidate"));
                updateField(yggdrasilUserAuthenticationClass, "ROUTE_REFRESH", new URL(service.getAuthServer() + "refresh"));
                updateField(yggdrasilUserAuthenticationClass, "ROUTE_VALIDATE", new URL(service.getAuthServer() + "validate"));
                updateField(yggdrasilUserAuthenticationClass, "ROUTE_SIGNOUT", new URL(service.getAuthServer() + "signout"));
            } catch (NoSuchFieldException | IllegalAccessException | MalformedURLException e) {
                System.out.println("Cannot switch service!");
                e.printStackTrace();
            }
        }
    }

    private void updateField(Class<?> clazz, String name, Object obj) throws NoSuchFieldException, IllegalAccessException {
        clazz.getDeclaredField(name).set(null, obj);
    }

    @Getter
    @AllArgsConstructor
    public enum Service {
        MOJANG("Mojang", "https://authserver.mojang.com/", "https://sessionserver.mojang.com/"),
        THEALTENING("Altening", "http://authserver.thealtening.com/", "http://sessionserver.thealtening.com/");

        private final String name, authServer, sessionServer;
    }
}
