package me.ratsiel.auth.model.mojang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import me.ratsiel.auth.abstracts.Authenticator;
import me.ratsiel.auth.abstracts.exception.AuthenticationException;
import me.ratsiel.auth.model.microsoft.MicrosoftAuthenticator;
import me.ratsiel.auth.model.microsoft.XboxToken;
import me.ratsiel.auth.model.mojang.profile.MinecraftCape;
import me.ratsiel.auth.model.mojang.profile.MinecraftProfile;
import me.ratsiel.auth.model.mojang.profile.MinecraftSkin;
import me.ratsiel.json.abstracts.JsonValue;
import me.ratsiel.json.model.JsonArray;
import me.ratsiel.json.model.JsonBoolean;
import me.ratsiel.json.model.JsonNumber;
import me.ratsiel.json.model.JsonObject;
import me.ratsiel.json.model.JsonString;

public class MinecraftAuthenticator extends Authenticator {
   protected final MicrosoftAuthenticator microsoftAuthenticator = new MicrosoftAuthenticator();

   public MinecraftToken login(String email, String password) {
      try {
         URL url = new URL("https://authserver.mojang.com/authenticate");
         URLConnection urlConnection = url.openConnection();
         HttpURLConnection httpURLConnection = (HttpURLConnection)urlConnection;
         httpURLConnection.setRequestMethod("POST");
         httpURLConnection.setDoOutput(true);
         JsonObject request = new JsonObject();
         JsonObject agent = new JsonObject();
         agent.add((JsonValue)(new JsonString("name", "Minecraft")));
         agent.add((JsonValue)(new JsonNumber("version", "1")));
         request.add("agent", agent);
         request.add((JsonValue)(new JsonString("username", email)));
         request.add((JsonValue)(new JsonString("password", password)));
         request.add((JsonValue)(new JsonBoolean("requestUser", false)));
         String requestBody = request.toString();
         httpURLConnection.setFixedLengthStreamingMode(requestBody.length());
         httpURLConnection.setRequestProperty("Content-Type", "application/json");
         httpURLConnection.setRequestProperty("Host", "authserver.mojang.com");
         httpURLConnection.connect();
         OutputStream outputStream = httpURLConnection.getOutputStream();
         Throwable var10 = null;

         try {
            outputStream.write(requestBody.getBytes(StandardCharsets.US_ASCII));
         } catch (Throwable var20) {
            var10 = var20;
            throw var20;
         } finally {
            if (outputStream != null) {
               if (var10 != null) {
                  try {
                     outputStream.close();
                  } catch (Throwable var19) {
                     var10.addSuppressed(var19);
                  }
               } else {
                  outputStream.close();
               }
            }

         }

         JsonObject jsonObject = this.parseResponseData(httpURLConnection);
         return new MinecraftToken(((JsonString)jsonObject.get("accessToken", JsonString.class)).getValue(), ((JsonString)((JsonObject)jsonObject.get("selectedProfile")).get("name", JsonString.class)).getValue());
      } catch (IOException var22) {
         throw new AuthenticationException(String.format("Authentication error. Request could not be made! Cause: '%s'", var22.getMessage()));
      }
   }

   public MinecraftToken loginWithXbox(String email, String password) {
      XboxToken xboxToken = this.microsoftAuthenticator.login(email, password);

      try {
         URL url = new URL("https://api.minecraftservices.com/authentication/login_with_xbox");
         URLConnection urlConnection = url.openConnection();
         HttpURLConnection httpURLConnection = (HttpURLConnection)urlConnection;
         httpURLConnection.setRequestMethod("POST");
         httpURLConnection.setDoOutput(true);
         JsonObject request = new JsonObject();
         request.add("identityToken", new JsonString("XBL3.0 x=" + xboxToken.getUhs() + ";" + xboxToken.getToken()));
         String requestBody = request.toString();
         httpURLConnection.setFixedLengthStreamingMode(requestBody.length());
         httpURLConnection.setRequestProperty("Content-Type", "application/json");
         httpURLConnection.setRequestProperty("Host", "api.minecraftservices.com");
         httpURLConnection.connect();
         OutputStream outputStream = httpURLConnection.getOutputStream();
         Throwable var10 = null;

         try {
            outputStream.write(requestBody.getBytes(StandardCharsets.US_ASCII));
         } catch (Throwable var20) {
            var10 = var20;
            throw var20;
         } finally {
            if (outputStream != null) {
               if (var10 != null) {
                  try {
                     outputStream.close();
                  } catch (Throwable var19) {
                     var10.addSuppressed(var19);
                  }
               } else {
                  outputStream.close();
               }
            }

         }

         JsonObject jsonObject = this.microsoftAuthenticator.parseResponseData(httpURLConnection);
         return new MinecraftToken(((JsonString)jsonObject.get("access_token", JsonString.class)).getValue(), ((JsonString)jsonObject.get("username", JsonString.class)).getValue());
      } catch (IOException var22) {
         throw new AuthenticationException(String.format("Authentication error. Request could not be made! Cause: '%s'", var22.getMessage()));
      }
   }

   public MinecraftProfile checkOwnership(MinecraftToken minecraftToken) {
      try {
         URL url = new URL("https://api.minecraftservices.com/minecraft/profile");
         URLConnection urlConnection = url.openConnection();
         HttpURLConnection httpURLConnection = (HttpURLConnection)urlConnection;
         httpURLConnection.setRequestMethod("GET");
         httpURLConnection.setRequestProperty("Authorization", "Bearer " + minecraftToken.getAccessToken());
         httpURLConnection.setRequestProperty("Host", "api.minecraftservices.com");
         httpURLConnection.connect();
         JsonObject jsonObject = this.parseResponseData(httpURLConnection);
         UUID uuid = this.generateUUID(((JsonString)jsonObject.get("id", JsonString.class)).getValue());
         String name = ((JsonString)jsonObject.get("name", JsonString.class)).getValue();
         List minecraftSkins = this.json.fromJson(jsonObject.get("skins", JsonArray.class), List.class, MinecraftSkin.class);
         List minecraftCapes = this.json.fromJson(jsonObject.get("capes", JsonArray.class), List.class, MinecraftCape.class);
         return new MinecraftProfile(uuid, name, minecraftSkins, minecraftCapes);
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
         throw new AuthenticationException(String.format("Could not find profile!. Error: '%s'", ((JsonString)jsonObject.get("errorMessage", JsonString.class)).getValue()));
      } else {
         return jsonObject;
      }
   }

   public UUID generateUUID(String trimmedUUID) throws IllegalArgumentException {
      if (trimmedUUID == null) {
         throw new IllegalArgumentException();
      } else {
         StringBuilder builder = new StringBuilder(trimmedUUID.trim());

         try {
            builder.insert(20, "-");
            builder.insert(16, "-");
            builder.insert(12, "-");
            builder.insert(8, "-");
            return UUID.fromString(builder.toString());
         } catch (StringIndexOutOfBoundsException var4) {
            return null;
         }
      }
   }
}
