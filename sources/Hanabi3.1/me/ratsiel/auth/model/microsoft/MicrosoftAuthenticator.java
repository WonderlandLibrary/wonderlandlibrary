package me.ratsiel.auth.model.microsoft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import me.ratsiel.auth.abstracts.Authenticator;
import me.ratsiel.auth.abstracts.exception.AuthenticationException;
import me.ratsiel.json.abstracts.JsonValue;
import me.ratsiel.json.model.JsonArray;
import me.ratsiel.json.model.JsonObject;
import me.ratsiel.json.model.JsonString;

public class MicrosoftAuthenticator extends Authenticator {
   protected final String clientId = "00000000402b5328";
   protected final String scopeUrl = "service::user.auth.xboxlive.com::MBI_SSL";
   protected String loginUrl;
   protected String loginCookie;
   protected String loginPPFT;

   public XboxToken login(String email, String password) {
      MicrosoftToken microsoftToken = this.generateTokenPair(this.generateLoginCode(email, password));
      XboxLiveToken xboxLiveToken = this.generateXboxTokenPair(microsoftToken);
      return this.generateXboxTokenPair(xboxLiveToken);
   }

   private String generateLoginCode(String email, String password) {
      try {
         URL url = new URL("https://login.live.com/oauth20_authorize.srf?redirect_uri=https://login.live.com/oauth20_desktop.srf&scope=service::user.auth.xboxlive.com::MBI_SSL&display=touch&response_type=code&locale=en&client_id=00000000402b5328");
         HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
         InputStream inputStream = httpURLConnection.getResponseCode() == 200 ? httpURLConnection.getInputStream() : httpURLConnection.getErrorStream();
         this.loginCookie = httpURLConnection.getHeaderField("set-cookie");
         String responseData = (String)(new BufferedReader(new InputStreamReader(inputStream))).lines().collect(Collectors.joining());
         Matcher bodyMatcher = Pattern.compile("sFTTag:[ ]?'.*value=\"(.*)\"/>'").matcher(responseData);
         if (bodyMatcher.find()) {
            this.loginPPFT = bodyMatcher.group(1);
            bodyMatcher = Pattern.compile("urlPost:[ ]?'(.+?(?='))").matcher(responseData);
            if (!bodyMatcher.find()) {
               throw new AuthenticationException("Authentication error. Could not find 'LOGIN-URL' tag from response!");
            } else {
               this.loginUrl = bodyMatcher.group(1);
               if (this.loginCookie != null && this.loginPPFT != null && this.loginUrl != null) {
                  return this.sendCodeData(email, password);
               } else {
                  throw new AuthenticationException("Authentication error. Error in authentication process!");
               }
            }
         } else {
            throw new AuthenticationException("Authentication error. Could not find 'LOGIN-PFTT' tag from response!");
         }
      } catch (IOException var8) {
         throw new AuthenticationException(String.format("Authentication error. Request could not be made! Cause: '%s'", var8.getMessage()));
      }
   }

   private String sendCodeData(String email, String password) {
      Map requestData = new HashMap();
      requestData.put("login", email);
      requestData.put("loginfmt", email);
      requestData.put("passwd", password);
      requestData.put("PPFT", this.loginPPFT);
      String postData = this.encodeURL((Map)requestData);

      String authToken;
      try {
         byte[] data = postData.getBytes(StandardCharsets.UTF_8);
         HttpURLConnection connection = (HttpURLConnection)(new URL(this.loginUrl)).openConnection();
         connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
         connection.setRequestProperty("Content-Length", String.valueOf(data.length));
         connection.setRequestProperty("Cookie", this.loginCookie);
         connection.setDoInput(true);
         connection.setDoOutput(true);
         OutputStream outputStream = connection.getOutputStream();
         Throwable var9 = null;

         try {
            outputStream.write(data);
         } catch (Throwable var19) {
            var9 = var19;
            throw var19;
         } finally {
            if (outputStream != null) {
               if (var9 != null) {
                  try {
                     outputStream.close();
                  } catch (Throwable var18) {
                     var9.addSuppressed(var18);
                  }
               } else {
                  outputStream.close();
               }
            }

         }

         if (connection.getResponseCode() != 200 || connection.getURL().toString().equals(this.loginUrl)) {
            throw new AuthenticationException("Authentication error. Username or password is not valid.");
         }

         Pattern pattern = Pattern.compile("[?|&]code=([\\w.-]+)");
         Matcher tokenMatcher = pattern.matcher(URLDecoder.decode(connection.getURL().toString(), StandardCharsets.UTF_8.name()));
         if (!tokenMatcher.find()) {
            throw new AuthenticationException("Authentication error. Could not handle data from response.");
         }

         authToken = tokenMatcher.group(1);
      } catch (IOException var21) {
         throw new AuthenticationException(String.format("Authentication error. Request could not be made! Cause: '%s'", var21.getMessage()));
      }

      this.loginUrl = null;
      this.loginCookie = null;
      this.loginPPFT = null;
      return authToken;
   }

   private void sendXboxRequest(HttpURLConnection httpURLConnection, JsonObject request, JsonObject properties) throws IOException {
      request.add("Properties", properties);
      String requestBody = request.toString();
      httpURLConnection.setFixedLengthStreamingMode(requestBody.length());
      httpURLConnection.setRequestProperty("Content-Type", "application/json");
      httpURLConnection.setRequestProperty("Accept", "application/json");
      httpURLConnection.connect();
      OutputStream outputStream = httpURLConnection.getOutputStream();
      Throwable var6 = null;

      try {
         outputStream.write(requestBody.getBytes(StandardCharsets.US_ASCII));
      } catch (Throwable var15) {
         var6 = var15;
         throw var15;
      } finally {
         if (outputStream != null) {
            if (var6 != null) {
               try {
                  outputStream.close();
               } catch (Throwable var14) {
                  var6.addSuppressed(var14);
               }
            } else {
               outputStream.close();
            }
         }

      }

   }

   private MicrosoftToken generateTokenPair(String authToken) {
      try {
         Map arguments = new HashMap();
         arguments.put("client_id", "00000000402b5328");
         arguments.put("code", authToken);
         arguments.put("grant_type", "authorization_code");
         arguments.put("redirect_uri", "https://login.live.com/oauth20_desktop.srf");
         arguments.put("scope", "service::user.auth.xboxlive.com::MBI_SSL");
         StringJoiner argumentBuilder = new StringJoiner("&");
         Iterator var4 = arguments.entrySet().iterator();

         while(var4.hasNext()) {
            Map.Entry entry = (Map.Entry)var4.next();
            argumentBuilder.add(this.encodeURL((String)entry.getKey()) + "=" + this.encodeURL((String)entry.getValue()));
         }

         byte[] data = argumentBuilder.toString().getBytes(StandardCharsets.UTF_8);
         URL url = new URL("https://login.live.com/oauth20_token.srf");
         URLConnection urlConnection = url.openConnection();
         HttpURLConnection httpURLConnection = (HttpURLConnection)urlConnection;
         httpURLConnection.setRequestMethod("POST");
         httpURLConnection.setDoOutput(true);
         httpURLConnection.setFixedLengthStreamingMode(data.length);
         httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
         httpURLConnection.connect();
         OutputStream outputStream = httpURLConnection.getOutputStream();
         Throwable var9 = null;

         try {
            outputStream.write(data);
         } catch (Throwable var19) {
            var9 = var19;
            throw var19;
         } finally {
            if (outputStream != null) {
               if (var9 != null) {
                  try {
                     outputStream.close();
                  } catch (Throwable var18) {
                     var9.addSuppressed(var18);
                  }
               } else {
                  outputStream.close();
               }
            }

         }

         JsonObject jsonObject = this.parseResponseData(httpURLConnection);
         return new MicrosoftToken(((JsonString)jsonObject.get("access_token", JsonString.class)).getValue(), ((JsonString)jsonObject.get("refresh_token", JsonString.class)).getValue());
      } catch (IOException var21) {
         throw new AuthenticationException(String.format("Authentication error. Request could not be made! Cause: '%s'", var21.getMessage()));
      }
   }

   public XboxLiveToken generateXboxTokenPair(MicrosoftToken microsoftToken) {
      try {
         URL url = new URL("https://user.auth.xboxlive.com/user/authenticate");
         URLConnection urlConnection = url.openConnection();
         HttpURLConnection httpURLConnection = (HttpURLConnection)urlConnection;
         httpURLConnection.setDoOutput(true);
         JsonObject request = new JsonObject();
         request.add("RelyingParty", new JsonString("http://auth.xboxlive.com"));
         request.add("TokenType", new JsonString("JWT"));
         JsonObject properties = new JsonObject();
         properties.add("AuthMethod", new JsonString("RPS"));
         properties.add("SiteName", new JsonString("user.auth.xboxlive.com"));
         properties.add("RpsTicket", new JsonString(microsoftToken.getToken()));
         this.sendXboxRequest(httpURLConnection, request, properties);
         JsonObject jsonObject = this.parseResponseData(httpURLConnection);
         String uhs = ((JsonString)((JsonObject)((JsonArray)((JsonObject)jsonObject.get("DisplayClaims", JsonObject.class)).get("xui", JsonArray.class)).get(0)).get("uhs", JsonString.class)).getValue();
         return new XboxLiveToken(((JsonString)jsonObject.get("Token", JsonString.class)).getValue(), uhs);
      } catch (IOException var9) {
         throw new AuthenticationException(String.format("Authentication error. Request could not be made! Cause: '%s'", var9.getMessage()));
      }
   }

   public XboxToken generateXboxTokenPair(XboxLiveToken xboxLiveToken) {
      try {
         URL url = new URL("https://xsts.auth.xboxlive.com/xsts/authorize");
         URLConnection urlConnection = url.openConnection();
         HttpURLConnection httpURLConnection = (HttpURLConnection)urlConnection;
         httpURLConnection.setRequestMethod("POST");
         httpURLConnection.setDoOutput(true);
         JsonObject request = new JsonObject();
         request.add("RelyingParty", new JsonString("rp://api.minecraftservices.com/"));
         request.add("TokenType", new JsonString("JWT"));
         JsonObject properties = new JsonObject();
         properties.add("SandboxId", new JsonString("RETAIL"));
         JsonArray userTokens = new JsonArray();
         userTokens.add((JsonValue)(new JsonString(xboxLiveToken.getToken())));
         properties.add("UserTokens", userTokens);
         this.sendXboxRequest(httpURLConnection, request, properties);
         if (httpURLConnection.getResponseCode() == 401) {
            throw new AuthenticationException("No xbox account was found!");
         } else {
            JsonObject jsonObject = this.parseResponseData(httpURLConnection);
            String uhs = ((JsonString)((JsonObject)((JsonArray)((JsonObject)jsonObject.get("DisplayClaims", JsonObject.class)).get("xui", JsonArray.class)).get(0)).get("uhs", JsonString.class)).getValue();
            return new XboxToken(((JsonString)jsonObject.get("Token", JsonString.class)).getValue(), uhs);
         }
      } catch (IOException var10) {
         throw new AuthenticationException(String.format("Authentication error. Request could not be made! Cause: '%s'", var10.getMessage()));
      }
   }

   public JsonObject parseResponseData(HttpURLConnection httpURLConnection) throws IOException {
      BufferedReader bufferedReader;
      if (httpURLConnection.getResponseCode() != 200) {
         bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
      } else {
         bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
      }

      String lines = (String)bufferedReader.lines().collect(Collectors.joining());
      JsonObject jsonObject = (JsonObject)this.json.fromJsonString(lines, JsonObject.class);
      if (jsonObject.has("error")) {
         throw new AuthenticationException(jsonObject.get("error") + ": " + jsonObject.get("error_description"));
      } else {
         return jsonObject;
      }
   }

   private String encodeURL(String url) {
      try {
         return URLEncoder.encode(url, "UTF-8");
      } catch (UnsupportedEncodingException var3) {
         throw new UnsupportedOperationException(var3);
      }
   }

   private String encodeURL(Map map) {
      StringBuilder sb = new StringBuilder();

      Map.Entry entry;
      for(Iterator var3 = map.entrySet().iterator(); var3.hasNext(); sb.append(String.format("%s=%s", this.encodeURL(((String)entry.getKey()).toString()), this.encodeURL(((String)entry.getValue()).toString())))) {
         entry = (Map.Entry)var3.next();
         if (sb.length() > 0) {
            sb.append("&");
         }
      }

      return sb.toString();
   }
}
