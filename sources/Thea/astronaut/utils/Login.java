package astronaut.utils;


import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.authlib.exceptions.AuthenticationException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import net.minecraft.util.Session;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Login {
    /**
     * Based on https://github.com/Ratsiiel/minecraft-auth-library
     */
    private static final String clientId = "00000000402b5328";
    private static final String scopeUrl = "service::user.auth.xboxlive.com::MBI_SSL";

    private static final String servicesApi = "https://api.minecraftservices.com";

    private static String loginUrl = null;
    private static String loginCookie = null;
    private static String loginPPFT = null;

    public static Session logIn(String email, String password) throws IOException, URISyntaxException, AuthenticationException {
        final MicrosoftToken microsoftToken = generateTokenPair(generateLoginCode(email, password));
        final XboxLiveToken xboxLiveToken = generateXboxTokenPair(microsoftToken);
        final XboxToken xboxToken = generateXboxTokenPair(xboxLiveToken);

        final URL url = new URL(servicesApi + "/authentication/login_with_xbox");
        final URLConnection urlConnection = url.openConnection();
        final HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);
        final JsonObject request = new JsonObject();
        request.add("identityToken", new JsonPrimitive("XBL3.0 x=" + xboxToken.uhs + ";" + xboxToken.token));
        final String requestBody = request.toString();
        httpURLConnection.setFixedLengthStreamingMode(requestBody.length());
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("Host", new URI(servicesApi).getPath());
        httpURLConnection.connect();
        httpURLConnection.getOutputStream().write(requestBody.getBytes(StandardCharsets.US_ASCII));
        final JsonObject jsonObject = parseResponseData(httpURLConnection);

        final MinecraftProfile minecraftProfile = checkOwnership(jsonObject.get("access_token").getAsString());

        return new Session(
                minecraftProfile.username,
                minecraftProfile.uuid.toString(),
                jsonObject.get("access_token").getAsString(),
                "LEGACY"
        );
    }

    public static MinecraftProfile checkOwnership(String minecraftToken) throws AuthenticationException {
        try {
            final URL url = new URL("https://api.minecraftservices.com/minecraft/profile");
            final URLConnection urlConnection = url.openConnection();
            final HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + minecraftToken);
            httpURLConnection.setRequestProperty("Host", new URI(servicesApi).getPath());
            httpURLConnection.connect();
            final JsonObject jsonObject = parseResponseData(httpURLConnection);
            final UUID uuid = generateUUID(jsonObject.get("id").getAsString());
            final String name = jsonObject.get("name").getAsString();
            return new MinecraftProfile(uuid, name);
        } catch (IOException exception) {
            throw new AuthenticationException(String.format("Authentication error. Request could not be made! Cause: '%s'", exception.getMessage()));
        } catch (AuthenticationException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static UUID generateUUID(String trimmedUUID) {
        final StringBuilder builder = new StringBuilder(trimmedUUID.trim());
        builder.insert(20, "-");
        builder.insert(16, "-");
        builder.insert(12, "-");
        builder.insert(8, "-");
        return UUID.fromString(builder.toString());
    }

    public static String generateLoginCode(String email, String password) throws AuthenticationException {
        try {
            final URL url = new URL("https://login.live.com/oauth20_authorize.srf?redirect_uri=https://login.live.com/oauth20_desktop.srf&scope=" + scopeUrl + "&display=touch&response_type=code&locale=en&client_id=" + clientId);
            final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            final InputStream inputStream = httpURLConnection.getResponseCode() == 200 ? httpURLConnection.getInputStream() : httpURLConnection.getErrorStream();
            loginCookie = httpURLConnection.getHeaderField("set-cookie");
            final String responseData = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining());
            Matcher bodyMatcher = Pattern.compile("sFTTag:[ ]?'.*value=\"(.*)\"/>'").matcher(responseData);
            if (bodyMatcher.find())
                loginPPFT = bodyMatcher.group(1);
            else
                throw new AuthenticationException("Authentication error. Could not find 'LOGIN-PFTT' tag from response!");
            bodyMatcher = Pattern.compile("urlPost:[ ]?'(.+?(?='))").matcher(responseData);
            if (bodyMatcher.find())
                loginUrl = bodyMatcher.group(1);
            else
                throw new AuthenticationException("Authentication error. Could not find 'LOGIN-URL' tag from response!");
            if (loginCookie == null || loginPPFT == null || loginUrl == null)
                throw new AuthenticationException("Authentication error. Error in authentication process!");
        } catch (IOException exception) {
            throw new AuthenticationException(String.format("Authentication error. Request could not be made! Cause: '%s'", exception.getMessage()));
        }
        return sendCodeData(email, password);
    }

    private static String sendCodeData(String email, String password) throws AuthenticationException {
        final String authToken;
        final HashMap<String, String> requestData = new HashMap<>();
        requestData.put("login", email);
        requestData.put("loginfmt", email);
        requestData.put("passwd", password);
        requestData.put("PPFT", loginPPFT);
        final String postData = encodeURL(requestData);
        try {
            final byte[] data = postData.getBytes(StandardCharsets.UTF_8);
            final HttpURLConnection connection = (HttpURLConnection) new URL(loginUrl).openConnection();
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            connection.setRequestProperty("Cookie", loginCookie);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.getOutputStream().write(data);
            if (connection.getResponseCode() != 200 || connection.getURL().toString().equalsIgnoreCase(loginUrl)) {
                throw new AuthenticationException("Authentication error. Username or password is not valid.");
            }
            final Pattern pattern = Pattern.compile("[?|&]code=([\\w.-]+)");
            final Matcher tokenMatcher = pattern.matcher(URLDecoder.decode(connection.getURL().toString(), StandardCharsets.UTF_8.name()));
            if (tokenMatcher.find()) {
                authToken = tokenMatcher.group(1);
            } else {
                throw new AuthenticationException("Authentication error. Could not handle data from response.");
            }
        } catch (IOException exception){
            throw new AuthenticationException(String.format("Authentication error. Request could not be made! Cause: '%s'", exception.getMessage()));
        }
        loginUrl = null;
        loginCookie = null;
        loginPPFT = null;
        return authToken;
    }

    private static void sendXboxRequest(HttpURLConnection httpURLConnection, JsonObject request, JsonObject properties) throws IOException {
        request.add("Properties", properties);
        final String requestBody = request.toString();
        httpURLConnection.setFixedLengthStreamingMode(requestBody.length());
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("Accept", "application/json");
        httpURLConnection.connect();
        httpURLConnection.getOutputStream().write(requestBody.getBytes(StandardCharsets.US_ASCII));
    }

    public static MicrosoftToken generateTokenPair(String authToken) throws AuthenticationException {
        try {
            final HashMap<String, String> arguments = new HashMap<>();
            arguments.put("client_id", clientId);
            arguments.put("code", authToken);
            arguments.put("grant_type", "authorization_code");
            arguments.put("redirect_uri", "https://login.live.com/oauth20_desktop.srf");
            arguments.put("scope", scopeUrl);

            final StringJoiner argumentBuilder = new StringJoiner("&");
            arguments.forEach((key, value) -> {
                argumentBuilder.add(encodeURL(key) + "=" + encodeURL(value));
            });
            final byte[] data = argumentBuilder.toString().getBytes(StandardCharsets.UTF_8);
            final URL url = new URL("https://login.live.com/oauth20_token.srf");
            final URLConnection urlConnection = url.openConnection();
            final HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setFixedLengthStreamingMode(data.length);
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.connect();
            httpURLConnection.getOutputStream().write(data);
            final JsonObject jsonObject = parseResponseData(httpURLConnection);
            return new MicrosoftToken(jsonObject.get("access_token").getAsString(), jsonObject.get("refresh_token").getAsString());
        } catch (IOException exception) {
            throw new AuthenticationException(String.format("Authentication error. Request could not be made! Cause: '%s'", exception.getMessage()));
        }
    }

    public static XboxLiveToken generateXboxTokenPair(MicrosoftToken microsoftToken) throws AuthenticationException {
        try {
            final URL url = new URL("https://user.auth.xboxlive.com/user/authenticate");
            final URLConnection urlConnection = url.openConnection();
            final HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setDoOutput(true);
            final JsonObject request = new JsonObject();
            request.add("RelyingParty", new JsonPrimitive("http://auth.xboxlive.com"));
            request.add("TokenType", new JsonPrimitive("JWT"));
            final JsonObject properties = new JsonObject();
            properties.add("AuthMethod", new JsonPrimitive("RPS"));
            ;
            properties.add("SiteName", new JsonPrimitive("user.auth.xboxlive.com"));
            properties.add("RpsTicket", new JsonPrimitive(microsoftToken.token));
            sendXboxRequest(httpURLConnection, request, properties);
            final JsonObject jsonObject = parseResponseData(httpURLConnection);
            final String uhs = (jsonObject.get("DisplayClaims").getAsJsonObject().getAsJsonArray("xui").get(0).getAsJsonObject()).get("uhs").getAsString();
            return new XboxLiveToken(jsonObject.get("Token").getAsString(), uhs);
        } catch (IOException exception) {
            throw new AuthenticationException(String.format("Authentication error. Request could not be made! Cause: '%s'", exception.getMessage()));
        }
    }

    @SneakyThrows
    public static XboxToken generateXboxTokenPair(XboxLiveToken xboxLiveToken) {
        try {
            final URL url = new URL("https://xsts.auth.xboxlive.com/xsts/authorize");
            final URLConnection urlConnection = url.openConnection();
            final HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            final JsonObject request = new JsonObject();
            request.add("RelyingParty", new JsonPrimitive("rp://api.minecraftservices.com/"));
            request.add("TokenType", new JsonPrimitive("JWT"));
            final JsonObject properties = new JsonObject();
            properties.add("SandboxId", new JsonPrimitive("RETAIL"));
            final JsonArray userTokens = new JsonArray();
            userTokens.add(new JsonPrimitive(xboxLiveToken.token));
            properties.add("UserTokens", userTokens);
            sendXboxRequest(httpURLConnection, request, properties);
            if (httpURLConnection.getResponseCode() == 401) {
                throw new AuthenticationException("No xbox account was found!");
            }
            final JsonObject jsonObject = parseResponseData(httpURLConnection);
            final String uhs = (jsonObject.get("DisplayClaims").getAsJsonObject().get("xui").getAsJsonArray().get(0).getAsJsonObject()).get("uhs").getAsString();
            return new XboxToken(jsonObject.get("Token").getAsString(), uhs);
        } catch (IOException exception) {
            throw new AuthenticationException(String.format("Authentication error. Request could not be made! Cause: '%s'", exception.getMessage()));
        }
    }

    private static JsonObject parseResponseData(HttpURLConnection httpURLConnection) throws IOException, AuthenticationException {
        final BufferedReader bufferedReader = httpURLConnection.getResponseCode() != 200 ? new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream())) : new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

        final String lines = bufferedReader.lines().collect(Collectors.joining());
        final JsonObject jsonObject = new GsonBuilder().create().fromJson(lines, JsonObject.class);
        if (jsonObject.has("error")) {
            throw new AuthenticationException(jsonObject.get("error").toString() + ": " + jsonObject.get("error_description"));
        }
        return jsonObject;
    }

    private static String encodeURL(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException exception) {
            throw new UnsupportedOperationException(exception);
        }
    }

    private static String encodeURL(HashMap<String, String> map) {
        final StringBuilder sb = new StringBuilder();
        map.forEach((key, value) -> {
            if (sb.length() != 0)
                sb.append("&");
            sb.append(String.format("%s=%s", encodeURL(key), encodeURL(value)));
        });
        return sb.toString();
    }

    @AllArgsConstructor
    public static class MicrosoftToken {
        public final String token, refreshToken;
    }

    @AllArgsConstructor
    public static class XboxLiveToken {
        public final String token /* xuid */, uhs;
    }

    @AllArgsConstructor
    public static class XboxToken {
        public final String token, uhs;
    }

    @AllArgsConstructor
    public static class MinecraftProfile {
        public final UUID uuid;
        public final String username;
    }
}
