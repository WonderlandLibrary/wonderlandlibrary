package intent.AquaDev.aqua.alt.design;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.authlib.exceptions.AuthenticationException;
import intent.AquaDev.aqua.alt.design.Login;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.minecraft.util.Session;

public class Login {
    private static final String clientId = "00000000402b5328";
    private static final String scopeUrl = "service::user.auth.xboxlive.com::MBI_SSL";
    private static final String servicesApi = "https://api.minecraftservices.com";
    private static String loginUrl = null;
    private static String loginCookie = null;
    private static String loginPPFT = null;

    public static Session logIn(String email, String password) throws IOException, URISyntaxException, AuthenticationException {
        MicrosoftToken microsoftToken = Login.generateTokenPair(Login.generateLoginCode(email, password));
        XboxLiveToken xboxLiveToken = Login.generateXboxTokenPair(microsoftToken);
        XboxToken xboxToken = Login.generateXboxTokenPair(xboxLiveToken);
        URL url = new URL("https://api.minecraftservices.com/authentication/login_with_xbox");
        URLConnection urlConnection = url.openConnection();
        HttpURLConnection httpURLConnection = (HttpURLConnection)urlConnection;
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);
        JsonObject request = new JsonObject();
        request.add("identityToken", (JsonElement)new JsonPrimitive("XBL3.0 x=" + xboxToken.uhs + ";" + xboxToken.token));
        String requestBody = request.toString();
        httpURLConnection.setFixedLengthStreamingMode(requestBody.length());
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("Host", new URI(servicesApi).getPath());
        httpURLConnection.connect();
        httpURLConnection.getOutputStream().write(requestBody.getBytes(StandardCharsets.US_ASCII));
        JsonObject jsonObject = Login.parseResponseData(httpURLConnection);
        MinecraftProfile minecraftProfile = Login.checkOwnership(jsonObject.get("access_token").getAsString());
        return new Session(minecraftProfile.username, minecraftProfile.uuid.toString(), jsonObject.get("access_token").getAsString(), "LEGACY");
    }

    public static MinecraftProfile checkOwnership(String minecraftToken) throws AuthenticationException {
        try {
            URL url = new URL("https://api.minecraftservices.com/minecraft/profile");
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection)urlConnection;
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + minecraftToken);
            httpURLConnection.setRequestProperty("Host", new URI(servicesApi).getPath());
            httpURLConnection.connect();
            JsonObject jsonObject = Login.parseResponseData(httpURLConnection);
            UUID uuid = Login.generateUUID(jsonObject.get("id").getAsString());
            String name = jsonObject.get("name").getAsString();
            return new MinecraftProfile(uuid, name);
        }
        catch (IOException exception) {
            throw new AuthenticationException(String.format((String)"Authentication error. Request could not be made! Cause: '%s'", (Object[])new Object[]{exception.getMessage()}));
        }
        catch (AuthenticationException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static UUID generateUUID(String trimmedUUID) {
        StringBuilder builder = new StringBuilder(trimmedUUID.trim());
        builder.insert(20, "-");
        builder.insert(16, "-");
        builder.insert(12, "-");
        builder.insert(8, "-");
        return UUID.fromString((String)builder.toString());
    }

    public static String generateLoginCode(String email, String password) throws AuthenticationException {
        try {
            URL url = new URL("https://login.live.com/oauth20_authorize.srf?redirect_uri=https://login.live.com/oauth20_desktop.srf&scope=service::user.auth.xboxlive.com::MBI_SSL&display=touch&response_type=code&locale=en&client_id=00000000402b5328");
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            InputStream inputStream = httpURLConnection.getResponseCode() == 200 ? httpURLConnection.getInputStream() : httpURLConnection.getErrorStream();
            loginCookie = httpURLConnection.getHeaderField("set-cookie");
            String responseData = (String)new BufferedReader((Reader)new InputStreamReader(inputStream)).lines().collect(Collectors.joining());
            Matcher bodyMatcher = Pattern.compile((String)"sFTTag:[ ]?'.*value=\"(.*)\"/>'").matcher((CharSequence)responseData);
            if (!bodyMatcher.find()) {
                throw new AuthenticationException("Authentication error. Could not find 'LOGIN-PFTT' tag from response!");
            }
            loginPPFT = bodyMatcher.group(1);
            bodyMatcher = Pattern.compile((String)"urlPost:[ ]?'(.+?(?='))").matcher((CharSequence)responseData);
            if (!bodyMatcher.find()) {
                throw new AuthenticationException("Authentication error. Could not find 'LOGIN-URL' tag from response!");
            }
            loginUrl = bodyMatcher.group(1);
            if (loginCookie == null || loginPPFT == null || loginUrl == null) {
                throw new AuthenticationException("Authentication error. Error in authentication process!");
            }
        }
        catch (IOException exception) {
            throw new AuthenticationException(String.format((String)"Authentication error. Request could not be made! Cause: '%s'", (Object[])new Object[]{exception.getMessage()}));
        }
        return Login.sendCodeData(email, password);
    }

    private static String sendCodeData(String email, String password) throws AuthenticationException {
        String authToken;
        HashMap requestData = new HashMap();
        requestData.put((Object)"login", (Object)email);
        requestData.put((Object)"loginfmt", (Object)email);
        requestData.put((Object)"passwd", (Object)password);
        requestData.put((Object)"PPFT", (Object)loginPPFT);
        String postData = Login.encodeURL((HashMap<String, String>)requestData);
        try {
            byte[] data = postData.getBytes(StandardCharsets.UTF_8);
            HttpURLConnection connection = (HttpURLConnection)new URL(loginUrl).openConnection();
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            connection.setRequestProperty("Content-Length", String.valueOf((int)data.length));
            connection.setRequestProperty("Cookie", loginCookie);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.getOutputStream().write(data);
            if (connection.getResponseCode() != 200 || connection.getURL().toString().equalsIgnoreCase(loginUrl)) {
                throw new AuthenticationException("Authentication error. Username or password is not valid.");
            }
            Pattern pattern = Pattern.compile((String)"[?|&]code=([\\w.-]+)");
            Matcher tokenMatcher = pattern.matcher((CharSequence)URLDecoder.decode((String)connection.getURL().toString(), (String)StandardCharsets.UTF_8.name()));
            if (!tokenMatcher.find()) {
                throw new AuthenticationException("Authentication error. Could not handle data from response.");
            }
            authToken = tokenMatcher.group(1);
        }
        catch (IOException exception) {
            throw new AuthenticationException(String.format((String)"Authentication error. Request could not be made! Cause: '%s'", (Object[])new Object[]{exception.getMessage()}));
        }
        loginUrl = null;
        loginCookie = null;
        loginPPFT = null;
        return authToken;
    }

    private static void sendXboxRequest(HttpURLConnection httpURLConnection, JsonObject request, JsonObject properties) throws IOException {
        request.add("Properties", (JsonElement)properties);
        String requestBody = request.toString();
        httpURLConnection.setFixedLengthStreamingMode(requestBody.length());
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("Accept", "application/json");
        httpURLConnection.connect();
        httpURLConnection.getOutputStream().write(requestBody.getBytes(StandardCharsets.US_ASCII));
    }

    public static MicrosoftToken generateTokenPair(String authToken) throws AuthenticationException {
        try {
            HashMap arguments = new HashMap();
            arguments.put((Object)"client_id", (Object)clientId);
            arguments.put((Object)"code", (Object)authToken);
            arguments.put((Object)"grant_type", (Object)"authorization_code");
            arguments.put((Object)"redirect_uri", (Object)"https://login.live.com/oauth20_desktop.srf");
            arguments.put((Object)"scope", (Object)scopeUrl);
            StringJoiner argumentBuilder = new StringJoiner((CharSequence)"&");
            arguments.forEach((key, value) -> argumentBuilder.add((CharSequence)(Login.encodeURL(key) + "=" + Login.encodeURL(value))));
            byte[] data = argumentBuilder.toString().getBytes(StandardCharsets.UTF_8);
            URL url = new URL("https://login.live.com/oauth20_token.srf");
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection)urlConnection;
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setFixedLengthStreamingMode(data.length);
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.connect();
            httpURLConnection.getOutputStream().write(data);
            JsonObject jsonObject = Login.parseResponseData(httpURLConnection);
            return new MicrosoftToken(jsonObject.get("access_token").getAsString(), jsonObject.get("refresh_token").getAsString());
        }
        catch (IOException exception) {
            throw new AuthenticationException(String.format((String)"Authentication error. Request could not be made! Cause: '%s'", (Object[])new Object[]{exception.getMessage()}));
        }
    }

    public static XboxLiveToken generateXboxTokenPair(MicrosoftToken microsoftToken) throws AuthenticationException {
        try {
            URL url = new URL("https://user.auth.xboxlive.com/user/authenticate");
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection)urlConnection;
            httpURLConnection.setDoOutput(true);
            JsonObject request = new JsonObject();
            request.add("RelyingParty", (JsonElement)new JsonPrimitive("http://auth.xboxlive.com"));
            request.add("TokenType", (JsonElement)new JsonPrimitive("JWT"));
            JsonObject properties = new JsonObject();
            properties.add("AuthMethod", (JsonElement)new JsonPrimitive("RPS"));
            properties.add("SiteName", (JsonElement)new JsonPrimitive("user.auth.xboxlive.com"));
            properties.add("RpsTicket", (JsonElement)new JsonPrimitive(microsoftToken.token));
            Login.sendXboxRequest(httpURLConnection, request, properties);
            JsonObject jsonObject = Login.parseResponseData(httpURLConnection);
            String uhs = jsonObject.get("DisplayClaims").getAsJsonObject().getAsJsonArray("xui").get(0).getAsJsonObject().get("uhs").getAsString();
            return new XboxLiveToken(jsonObject.get("Token").getAsString(), uhs);
        }
        catch (IOException exception) {
            throw new AuthenticationException(String.format((String)"Authentication error. Request could not be made! Cause: '%s'", (Object[])new Object[]{exception.getMessage()}));
        }
    }

    public static XboxToken generateXboxTokenPair(XboxLiveToken xboxLiveToken) {
        try {
            URL url = new URL("https://xsts.auth.xboxlive.com/xsts/authorize");
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection)urlConnection;
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            JsonObject request = new JsonObject();
            request.add("RelyingParty", (JsonElement)new JsonPrimitive("rp://api.minecraftservices.com/"));
            request.add("TokenType", (JsonElement)new JsonPrimitive("JWT"));
            JsonObject properties = new JsonObject();
            properties.add("SandboxId", (JsonElement)new JsonPrimitive("RETAIL"));
            JsonArray userTokens = new JsonArray();
            userTokens.add((JsonElement)new JsonPrimitive(xboxLiveToken.token));
            properties.add("UserTokens", (JsonElement)userTokens);
            Login.sendXboxRequest(httpURLConnection, request, properties);
            if (httpURLConnection.getResponseCode() == 401) {
                throw new AuthenticationException("No xbox account was found!");
            }
            JsonObject jsonObject = Login.parseResponseData(httpURLConnection);
            String uhs = jsonObject.get("DisplayClaims").getAsJsonObject().get("xui").getAsJsonArray().get(0).getAsJsonObject().get("uhs").getAsString();
            return new XboxToken(jsonObject.get("Token").getAsString(), uhs);
        }
        catch (IOException exception) {
            throw new AuthenticationException(String.format((String)"Authentication error. Request could not be made! Cause: '%s'", (Object[])new Object[]{exception.getMessage()}));
        }
    }

    private static JsonObject parseResponseData(HttpURLConnection httpURLConnection) throws IOException, AuthenticationException {
        BufferedReader bufferedReader = httpURLConnection.getResponseCode() != 200 ? new BufferedReader((Reader)new InputStreamReader(httpURLConnection.getErrorStream())) : new BufferedReader((Reader)new InputStreamReader(httpURLConnection.getInputStream()));
        String lines = (String)bufferedReader.lines().collect(Collectors.joining());
        JsonObject jsonObject = (JsonObject)new GsonBuilder().create().fromJson(lines, JsonObject.class);
        if (jsonObject.has("error")) {
            throw new AuthenticationException(jsonObject.get("error").toString() + ": " + jsonObject.get("error_description"));
        }
        return jsonObject;
    }

    private static String encodeURL(String url) {
        try {
            return URLEncoder.encode((String)url, (String)"UTF-8");
        }
        catch (UnsupportedEncodingException exception) {
            throw new UnsupportedOperationException((Throwable)exception);
        }
    }

    private static String encodeURL(HashMap<String, String> map) {
        StringBuilder sb = new StringBuilder();
        map.forEach((key, value) -> {
            if (sb.length() != 0) {
                sb.append("&");
            }
            sb.append(String.format((String)"%s=%s", (Object[])new Object[]{Login.encodeURL(key), Login.encodeURL(value)}));
        });
        return sb.toString();
    }
}
