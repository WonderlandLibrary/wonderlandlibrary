// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.microsoft;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import java.nio.charset.StandardCharsets;
import com.sun.net.httpserver.HttpExchange;
import java.util.concurrent.Executors;
import java.util.concurrent.Executor;
import com.sun.net.httpserver.HttpHandler;
import java.net.InetSocketAddress;
import dev.tenacity.intent.api.web.Browser;
import java.net.URISyntaxException;
import java.io.IOException;
import java.net.URI;
import java.awt.Desktop;
import com.google.gson.Gson;
import java.util.function.Consumer;
import com.sun.net.httpserver.HttpServer;
import java.util.concurrent.ExecutorService;

public class MicrosoftLogin
{
    static ExecutorService executor;
    private static final String CLIENT_ID = "9fbc7315-7200-4b2b-a655-bb38c865da17";
    private static final String CLIENT_SECRET = "Bzn8Q~YryydJsydgnnxHgJq.NM3Oo4.AEEohLbBb";
    private static final int PORT = 8247;
    private static HttpServer server;
    private static Consumer<String> callback;
    static Gson gson;
    
    static void browse(final String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        }
        catch (IOException | URISyntaxException ex2) {
            final Exception ex;
            final Exception e = ex;
            e.printStackTrace();
        }
    }
    
    public static void getRefreshToken(final Consumer<String> callback) {
        MicrosoftLogin.callback = callback;
        startServer();
        browse("https://login.live.com/oauth20_authorize.srf?client_id=9fbc7315-7200-4b2b-a655-bb38c865da17&client_secret=Bzn8Q~YryydJsydgnnxHgJq.NM3Oo4.AEEohLbBb&response_type=code&redirect_uri=http://localhost:8247&scope=XboxLive.signin%20offline_access");
    }
    
    public static LoginData login(String refreshToken) {
        final AuthTokenResponse res = (AuthTokenResponse)MicrosoftLogin.gson.fromJson(Browser.postExternal("https://login.live.com/oauth20_token.srf", "client_id=9fbc7315-7200-4b2b-a655-bb38c865da17&client_secret=Bzn8Q~YryydJsydgnnxHgJq.NM3Oo4.AEEohLbBb&refresh_token=" + refreshToken + "&grant_type=refresh_token&redirect_uri=http://localhost:" + 8247, false), (Class)AuthTokenResponse.class);
        if (res == null) {
            return new LoginData();
        }
        final String accessToken = res.access_token;
        refreshToken = res.refresh_token;
        final XblXstsResponse xblRes = (XblXstsResponse)MicrosoftLogin.gson.fromJson(Browser.postExternal("https://user.auth.xboxlive.com/user/authenticate", "{\"Properties\":{\"AuthMethod\":\"RPS\",\"SiteName\":\"user.auth.xboxlive.com\",\"RpsTicket\":\"d=" + accessToken + "\"},\"RelyingParty\":\"http://auth.xboxlive.com\",\"TokenType\":\"JWT\"}", true), (Class)XblXstsResponse.class);
        if (xblRes == null) {
            return new LoginData();
        }
        final XblXstsResponse xstsRes = (XblXstsResponse)MicrosoftLogin.gson.fromJson(Browser.postExternal("https://xsts.auth.xboxlive.com/xsts/authorize", "{\"Properties\":{\"SandboxId\":\"RETAIL\",\"UserTokens\":[\"" + xblRes.Token + "\"]},\"RelyingParty\":\"rp://api.minecraftservices.com/\",\"TokenType\":\"JWT\"}", true), (Class)XblXstsResponse.class);
        if (xstsRes == null) {
            return new LoginData();
        }
        final McResponse mcRes = (McResponse)MicrosoftLogin.gson.fromJson(Browser.postExternal("https://api.minecraftservices.com/authentication/login_with_xbox", "{\"identityToken\":\"XBL3.0 x=" + xblRes.DisplayClaims.xui[0].uhs + ";" + xstsRes.Token + "\"}", true), (Class)McResponse.class);
        if (mcRes == null) {
            return new LoginData();
        }
        final GameOwnershipResponse gameOwnershipRes = (GameOwnershipResponse)MicrosoftLogin.gson.fromJson(Browser.getBearerResponse("https://api.minecraftservices.com/entitlements/mcstore", mcRes.access_token), (Class)GameOwnershipResponse.class);
        if (gameOwnershipRes == null || !gameOwnershipRes.hasGameOwnership()) {
            return new LoginData();
        }
        final ProfileResponse profileRes = (ProfileResponse)MicrosoftLogin.gson.fromJson(Browser.getBearerResponse("https://api.minecraftservices.com/minecraft/profile", mcRes.access_token), (Class)ProfileResponse.class);
        if (profileRes == null) {
            return new LoginData();
        }
        return new LoginData(mcRes.access_token, refreshToken, profileRes.id, profileRes.name);
    }
    
    private static void startServer() {
        if (MicrosoftLogin.server != null) {
            return;
        }
        try {
            (MicrosoftLogin.server = HttpServer.create(new InetSocketAddress("localhost", 8247), 0)).createContext("/", new Handler());
            MicrosoftLogin.server.setExecutor(MicrosoftLogin.executor);
            MicrosoftLogin.server.start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void stopServer() {
        if (MicrosoftLogin.server == null) {
            return;
        }
        MicrosoftLogin.server.stop(0);
        MicrosoftLogin.server = null;
        MicrosoftLogin.callback = null;
    }
    
    static {
        MicrosoftLogin.executor = Executors.newSingleThreadExecutor();
        MicrosoftLogin.gson = new Gson();
    }
    
    public static class LoginData
    {
        public String mcToken;
        public String newRefreshToken;
        public String uuid;
        public String username;
        
        public LoginData() {
        }
        
        public LoginData(final String mcToken, final String newRefreshToken, final String uuid, final String username) {
            this.mcToken = mcToken;
            this.newRefreshToken = newRefreshToken;
            this.uuid = uuid;
            this.username = username;
        }
        
        public boolean isGood() {
            return this.mcToken != null;
        }
    }
    
    private static class Handler implements HttpHandler
    {
        @Override
        public void handle(final HttpExchange req) throws IOException {
            if (req.getRequestMethod().equals("GET")) {
                final List<NameValuePair> query = (List<NameValuePair>)URLEncodedUtils.parse(req.getRequestURI(), StandardCharsets.UTF_8.name());
                boolean ok = false;
                for (final NameValuePair pair : query) {
                    if (pair.getName().equals("code")) {
                        this.handleCode(pair.getValue());
                        ok = true;
                        break;
                    }
                }
                if (!ok) {
                    this.writeText(req, "Cannot authenticate.");
                }
                else {
                    this.writeText(req, "<html>You may now close this page.<script>close()</script></html>");
                }
            }
            stopServer();
        }
        
        private void handleCode(final String code) {
            final String response = Browser.postExternal("https://login.live.com/oauth20_token.srf", "client_id=9fbc7315-7200-4b2b-a655-bb38c865da17&code=" + code + "&client_secret=" + "Bzn8Q~YryydJsydgnnxHgJq.NM3Oo4.AEEohLbBb" + "&grant_type=authorization_code&redirect_uri=http://localhost:" + 8247, false);
            final AuthTokenResponse res = (AuthTokenResponse)MicrosoftLogin.gson.fromJson(response, (Class)AuthTokenResponse.class);
            if (res == null) {
                MicrosoftLogin.callback.accept(null);
            }
            else {
                MicrosoftLogin.callback.accept(res.refresh_token);
            }
        }
        
        private void writeText(final HttpExchange req, final String text) throws IOException {
            final OutputStream out = req.getResponseBody();
            req.getResponseHeaders().add("Content-Type", "text/html; charset=utf-8");
            req.sendResponseHeaders(200, text.length());
            out.write(text.getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();
        }
    }
    
    private static class AuthTokenResponse
    {
        @Expose
        @SerializedName("access_token")
        public String access_token;
        @Expose
        @SerializedName("refresh_token")
        public String refresh_token;
    }
    
    private static class XblXstsResponse
    {
        @Expose
        @SerializedName("Token")
        public String Token;
        @Expose
        @SerializedName("DisplayClaims")
        public DisplayClaims DisplayClaims;
        
        private static class DisplayClaims
        {
            @Expose
            @SerializedName("xui")
            private Claim[] xui;
            
            private static class Claim
            {
                @Expose
                @SerializedName("uhs")
                private String uhs;
            }
        }
    }
    
    private static class McResponse
    {
        @Expose
        @SerializedName("access_token")
        public String access_token;
    }
    
    private static class GameOwnershipResponse
    {
        @Expose
        @SerializedName("items")
        private Item[] items;
        
        private boolean hasGameOwnership() {
            boolean hasProduct = false;
            boolean hasGame = false;
            for (final Item item : this.items) {
                if (item.name.equals("product_minecraft")) {
                    hasProduct = true;
                }
                else if (item.name.equals("game_minecraft")) {
                    hasGame = true;
                }
            }
            return hasProduct && hasGame;
        }
        
        private static class Item
        {
            @Expose
            @SerializedName("name")
            private String name;
        }
    }
    
    private static class ProfileResponse
    {
        @Expose
        @SerializedName("id")
        public String id;
        @Expose
        @SerializedName("name")
        public String name;
    }
}
