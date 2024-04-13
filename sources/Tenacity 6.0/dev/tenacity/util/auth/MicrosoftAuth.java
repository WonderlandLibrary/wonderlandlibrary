package dev.tenacity.util.auth;

import com.google.gson.*;
import com.sun.net.httpserver.HttpServer;
import net.minecraft.util.Session;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Utility methods for authenticating via Microsoft.
 * Source: https://github.com/axieum/authme
 *
 * <p>For more information refer to: https://wiki.vg/Microsoft_Authentication_Scheme
 */
public final class MicrosoftAuth {
    // A reusable Apache HTTP request config
    // NB: We use Apache's HTTP implementation as the native HTTP client does
    //     not appear to free its resources after use!
    public static final RequestConfig REQUEST_CONFIG = RequestConfig
            .custom()
            .setConnectionRequestTimeout(30_000)
            .setConnectTimeout(30_000)
            .setSocketTimeout(30_000)
            .build();
    // Account Manager
    public static final String CLIENT_ID = "42a60a84-599d-44b2-a7c6-b00cdef1d6a2";
    // 25565 + 10
    public static final int PORT = 25575;

    /**
     * Navigates to the Microsoft login, and listens for a successful login
     * callback.
     *
     * <p>NB: You must manually interrupt the executor thread if the
     * completable future is cancelled!
     *
     * @param executor executor to run the login task on
     * @return completable future for the Microsoft auth token
     * @see #acquireMSAuthCode(Consumer, Executor)
     */
    public static CompletableFuture<String> acquireMSAuthCode(
            final Executor executor
    ) {
        return acquireMSAuthCode(MicrosoftAuth::openWebLink, executor);
    }

    /**
     * Generates a Microsoft login link, triggers the given browser action, and
     * listens for a successful login callback.
     *
     * <p>NB: You must manually interrupt the executor thread if the
     * completable future is cancelled!
     *
     * @param browserAction consumer that opens the generated login url
     * @param executor      executor to run the login task on
     * @return completable future for the Microsoft auth token
     */
    public static CompletableFuture<String> acquireMSAuthCode(
            final Consumer<URI> browserAction,
            final Executor executor
    ) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Generate a random "state" to be included in the request that will in turn be returned with the token
                final String state = RandomStringUtils.randomAlphanumeric(8);

                // Prepare a temporary HTTP server we can listen for the OAuth2 callback on
                final HttpServer server = HttpServer.create(
                        new InetSocketAddress(PORT), 0
                );

                // Track when a request has been handled
                final CountDownLatch latch = new CountDownLatch(1);
                final AtomicReference<String> authCode = new AtomicReference<>(null),
                        errorMsg = new AtomicReference<>(null);

                server.createContext("/callback", exchange -> {
                    // Parse the query parameters
                    final Map<String, String> query = URLEncodedUtils
                            .parse(
                                    exchange.getRequestURI().toString().replaceAll("/callback\\?", ""),
                                    StandardCharsets.UTF_8
                            )
                            .stream()
                            .collect(Collectors.toMap(NameValuePair::getName, NameValuePair::getValue));

                    // Check the returned parameter values
                    if (!state.equals(query.get("state"))) {
                        // The "state" does not match what we sent
                        errorMsg.set(
                                String.format("State mismatch! Expected '%s' but got '%s'.", state, query.get("state"))
                        );
                    } else if (query.containsKey("code")) {
                        // Successfully matched the auth code
                        authCode.set(query.get("code"));
                    } else if (query.containsKey("error")) {
                        // Otherwise, try to find an error description
                        errorMsg.set(String.format("%s: %s", query.get("error"), query.get("error_description")));
                    }

                    // Send a response informing that the browser may now be closed
                    final InputStream stream = MicrosoftAuth.class.getResourceAsStream("/callback.html");
                    final byte[] response = stream != null ? IOUtils.toByteArray(stream) : new byte[0];
                    exchange.getResponseHeaders().add("Content-Type", "text/html");
                    exchange.sendResponseHeaders(200, response.length);
                    exchange.getResponseBody().write(response);
                    exchange.getResponseBody().close();

                    // Let the caller thread know that the request has been handled
                    latch.countDown();
                });

                // Build a Microsoft login url
                final URIBuilder uriBuilder = new URIBuilder("https://login.live.com/oauth20_authorize.srf")
                        .addParameter("client_id", CLIENT_ID)
                        .addParameter("response_type", "code")
                        .addParameter("redirect_uri", String.format("http://localhost:%d/callback", server.getAddress().getPort()))
                        .addParameter("scope", "XboxLive.signin XboxLive.offline_access")
                        .addParameter("state", state)
                        .addParameter("prompt", "select_account");
                final URI uri = uriBuilder.build();

                // Navigate to the Microsoft login in browser
                browserAction.accept(uri);

                try {
                    // Start the HTTP server (http://localhost:25575/callback)
                    server.start();

                    // Wait for the server to stop and return the auth code
                    latch.await();

                    // If present, return
                    return Optional.ofNullable(authCode.get())
                            .filter(code -> !StringUtils.isBlank(code))
                            // Otherwise, throw an exception with the error description (if present)
                            .orElseThrow(() -> new Exception(
                                    Optional.ofNullable(errorMsg.get())
                                            .orElse("There was no auth code or error description present.")
                            ));
                } finally {
                    // Always release the server
                    server.stop(2);
                }
            } catch (InterruptedException e) {
                throw new CancellationException("Microsoft auth code acquisition was cancelled!");
            } catch (Exception e) {
                throw new CompletionException("Unable to acquire Microsoft auth code!", e);
            }
        }, executor);
    }

    /**
     * Exchanges a Microsoft auth code for an access token.
     *
     * <p>NB: You must manually interrupt the executor thread if the
     * completable future is cancelled!
     *
     * @param authCode Microsoft auth code
     * @param executor executor to run the login task on
     * @return completable future for the Microsoft access token
     */
    public static CompletableFuture<String> acquireMSAccessToken(
            final String authCode,
            final Executor executor
    ) {
        return CompletableFuture.supplyAsync(() -> {
            try (CloseableHttpClient client = HttpClients.createMinimal()) {
                // Build a new HTTP request
                final HttpPost request = new HttpPost(URI.create("https://login.live.com/oauth20_token.srf"));
                request.setConfig(REQUEST_CONFIG);
                request.setHeader("Content-Type", "application/x-www-form-urlencoded");
                request.setEntity(new UrlEncodedFormEntity(
                        Arrays.asList(
                                new BasicNameValuePair("client_id", CLIENT_ID),
                                new BasicNameValuePair("grant_type", "authorization_code"),
                                new BasicNameValuePair("code", authCode),
                                // We must provide the exact redirect URI that was used to obtain the auth code
                                new BasicNameValuePair(
                                        "redirect_uri", String.format("http://localhost:%d/callback", PORT)
                                )
                        ),
                        "UTF-8"
                ));

                // Send the request on the HTTP client
                final org.apache.http.HttpResponse res = client.execute(request);

                // Attempt to parse the response body as JSON and extract the access token
                final JsonObject json = new JsonParser().parse(EntityUtils.toString(res.getEntity())).getAsJsonObject();
                // If present, return
                return Optional.ofNullable(json.get("access_token"))
                        .map(JsonElement::getAsString)
                        .filter(token -> !StringUtils.isBlank(token))
                        // Otherwise, throw an exception with the error description (if present)
                        .orElseThrow(() -> new Exception(
                                json.has("error") ? String.format(
                                        "%s: %s",
                                        json.get("error").getAsString(),
                                        json.get("error_description").getAsString()
                                ) : "There was no access token or error description present."
                        ));
            } catch (InterruptedException e) {
                throw new CancellationException("Microsoft access token acquisition was cancelled!");
            } catch (Exception e) {
                throw new CompletionException("Unable to acquire Microsoft access token!", e);
            }
        }, executor);
    }

    /**
     * Exchanges a Microsoft access token for an Xbox Live access token.
     *
     * <p>NB: You must manually interrupt the executor thread if the
     * completable future is cancelled!
     *
     * @param accessToken Microsoft access token
     * @param executor    executor to run the login task on
     * @return completable future for the Xbox Live access token
     */
    public static CompletableFuture<String> acquireXboxAccessToken(
            final String accessToken,
            final Executor executor
    ) {
        return CompletableFuture.supplyAsync(() -> {
            try (CloseableHttpClient client = HttpClients.createMinimal()) {
                // Build a new HTTP request
                final HttpPost request = new HttpPost(URI.create("https://user.auth.xboxlive.com/user/authenticate"));
                final JsonObject entity = new JsonObject();
                final JsonObject properties = new JsonObject();
                properties.addProperty("AuthMethod", "RPS");
                properties.addProperty("SiteName", "user.auth.xboxlive.com");
                properties.addProperty("RpsTicket", String.format("d=%s", accessToken));
                entity.add("Properties", properties);
                entity.addProperty("RelyingParty", "http://auth.xboxlive.com");
                entity.addProperty("TokenType", "JWT");
                request.setConfig(REQUEST_CONFIG);
                request.setHeader("Content-Type", "application/json");
                request.setEntity(new StringEntity(entity.toString()));

                // Send the request on the HTTP client
                final org.apache.http.HttpResponse res = client.execute(request);

                // Attempt to parse the response body as JSON and extract the access token
                // NB: No response body is sent if the response is not valid
                final JsonObject json = res.getStatusLine().getStatusCode() == 200
                        ? new JsonParser().parse(EntityUtils.toString(res.getEntity())).getAsJsonObject()
                        : new JsonObject();
                // If present, return
                return Optional.ofNullable(json.get("Token"))
                        .map(JsonElement::getAsString)
                        .filter(token -> !StringUtils.isBlank(token))
                        // Otherwise, throw an exception with the error description (if present)
                        .orElseThrow(() -> new Exception(
                                json.has("XErr") ? String.format(
                                        "%s: %s", json.get("XErr").getAsString(), json.get("Message").getAsString()
                                ) : "There was no access token or error description present."
                        ));
            } catch (InterruptedException e) {
                throw new CancellationException("Xbox Live access token acquisition was cancelled!");
            } catch (Exception e) {
                throw new CompletionException("Unable to acquire Xbox Live access token!", e);
            }
        }, executor);
    }

    /**
     * Exchanges an Xbox Live access token for an Xbox Live XSTS (security
     * token service) token.
     *
     * <p>NB: You must manually interrupt the executor thread if the
     * completable future is cancelled!
     *
     * @param accessToken Xbox Live access token
     * @param executor    executor to run the login task on
     * @return completable future for a mapping of Xbox Live XSTS token ("Token") and user hash ("uhs")
     */
    public static CompletableFuture<Map<String, String>> acquireXboxXstsToken(
            final String accessToken,
            final Executor executor
    ) {
        return CompletableFuture.supplyAsync(() -> {
            try (CloseableHttpClient client = HttpClients.createMinimal()) {
                // Build a new HTTP request
                final HttpPost request = new HttpPost("https://xsts.auth.xboxlive.com/xsts/authorize");
                final JsonObject entity = new JsonObject();
                final JsonObject properties = new JsonObject();
                final JsonArray userTokens = new JsonArray();
                userTokens.add(new JsonPrimitive(accessToken));
                properties.addProperty("SandboxId", "RETAIL");
                properties.add("UserTokens", userTokens);
                entity.add("Properties", properties);
                entity.addProperty("RelyingParty", "rp://api.minecraftservices.com/");
                entity.addProperty("TokenType", "JWT");
                request.setConfig(REQUEST_CONFIG);
                request.setHeader("Content-Type", "application/json");
                request.setEntity(new StringEntity(entity.toString()));

                // Send the request on the HTTP client
                final org.apache.http.HttpResponse res = client.execute(request);

                // Attempt to parse the response body as JSON and extract the access token and user hash
                // NB: No response body is sent if the response is not valid
                final JsonObject json = res.getStatusLine().getStatusCode() == 200
                        ? new JsonParser().parse(EntityUtils.toString(res.getEntity())).getAsJsonObject()
                        : new JsonObject();
                return Optional.ofNullable(json.get("Token"))
                        .map(JsonElement::getAsString)
                        .filter(token -> !StringUtils.isBlank(token))
                        // If present, extract the user hash and return
                        .map(token -> {
                            // Extract the user hash
                            final String uhs = json.get("DisplayClaims").getAsJsonObject()
                                    .get("xui").getAsJsonArray()
                                    .get(0).getAsJsonObject()
                                    .get("uhs").getAsString();

                            // Return an immutable mapping of the token and user hash
                            Map<String, String> result = new HashMap<>();
                            result.put("Token", token);
                            result.put("uhs", uhs);
                            return result;
                        })
                        // Otherwise, throw an exception with the error description (if present)
                        .orElseThrow(() -> new Exception(
                                json.has("XErr") ? String.format(
                                        "%s: %s", json.get("XErr").getAsString(), json.get("Message").getAsString()
                                ) : "There was no access token or error description present."
                        ));
            } catch (InterruptedException e) {
                throw new CancellationException("Xbox Live XSTS token acquisition was cancelled!");
            } catch (Exception e) {
                throw new CompletionException("Unable to acquire Xbox Live XSTS token!", e);
            }
        }, executor);
    }

    /**
     * Exchanges an Xbox Live XSTS token for a Minecraft access token.
     *
     * <p>NB: You must manually interrupt the executor thread if the
     * completable future is cancelled!
     *
     * @param xstsToken Xbox Live XSTS token
     * @param userHash  Xbox Live user hash
     * @param executor  executor to run the login task on
     * @return completable future for the Minecraft access token
     */
    public static CompletableFuture<String> acquireMCAccessToken(
            final String xstsToken,
            final String userHash,
            final Executor executor
    ) {
        return CompletableFuture.supplyAsync(() -> {
            try (CloseableHttpClient client = HttpClients.createMinimal()) {
                // Build a new HTTP request
                final HttpPost request = new HttpPost(URI.create("https://api.minecraftservices.com/authentication/login_with_xbox"));
                request.setConfig(REQUEST_CONFIG);
                request.setHeader("Content-Type", "application/json");
                request.setEntity(new StringEntity(
                        String.format("{\"identityToken\": \"XBL3.0 x=%s;%s\"}", userHash, xstsToken)
                ));

                // Send the request on the HTTP client
                final org.apache.http.HttpResponse res = client.execute(request);

                // Attempt to parse the response body as JSON and extract the access token
                final JsonObject json = new JsonParser().parse(EntityUtils.toString(res.getEntity())).getAsJsonObject();

                // If present, return
                return Optional.ofNullable(json.get("access_token"))
                        .map(JsonElement::getAsString)
                        .filter(token -> !StringUtils.isBlank(token))
                        // Otherwise, throw an exception with the error description (if present)
                        .orElseThrow(() -> new Exception(
                                json.has("error") ? String.format(
                                        "%s: %s", json.get("error").getAsString(), json.get("errorMessage").getAsString()
                                ) : "There was no access token or error description present."
                        ));
            } catch (InterruptedException e) {
                throw new CancellationException("Minecraft access token acquisition was cancelled!");
            } catch (Exception e) {
                throw new CompletionException("Unable to acquire Minecraft access token!", e);
            }
        }, executor);
    }

    /**
     * Fetches the Minecraft profile for the given access token, and returns a
     * new Minecraft session.
     *
     * <p>NB: You must manually interrupt the executor thread if the
     * completable future is cancelled!
     *
     * @param mcToken  Minecraft access token
     * @param executor executor to run the login task on
     * @return completable future for the new Minecraft session
     */
    public static CompletableFuture<Session> login(
            final String mcToken,
            final Executor executor
    ) {
        return CompletableFuture.supplyAsync(() -> {
            try (CloseableHttpClient client = HttpClients.createMinimal()) {
                // Build a new HTTP request
                final HttpGet request = new HttpGet(URI.create("https://api.minecraftservices.com/minecraft/profile"));
                request.setConfig(REQUEST_CONFIG);
                request.setHeader("Authorization", "Bearer " + mcToken);

                // Send the request on the HTTP client
                final org.apache.http.HttpResponse res = client.execute(request);

                // Attempt to parse the response body as JSON and extract the profile
                final JsonObject json = new JsonParser().parse(EntityUtils.toString(res.getEntity())).getAsJsonObject();
                return Optional.ofNullable(json.get("id"))
                        .map(JsonElement::getAsString)
                        .filter(uuid -> !StringUtils.isBlank(uuid))
                        // If present, build a new session and return
                        .map(uuid -> new Session(
                                json.get("name").getAsString(),
                                uuid,
                                mcToken,
                                Session.Type.MOJANG.toString()
                        ))
                        // Otherwise, throw an exception with the error description (if present)
                        .orElseThrow(() -> new Exception(
                                json.has("error") ? String.format(
                                        "%s: %s", json.get("error").getAsString(), json.get("errorMessage").getAsString()
                                ) : "There was no profile or error description present."
                        ));
            } catch (InterruptedException e) {
                throw new CancellationException("Minecraft profile fetching was cancelled!");
            } catch (Exception e) {
                throw new CompletionException("Unable to fetch Minecraft profile!", e);
            }
        }, executor);
    }
    public static void openWebLink(URI uri) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}