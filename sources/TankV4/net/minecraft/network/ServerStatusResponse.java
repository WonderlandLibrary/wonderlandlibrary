package net.minecraft.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.authlib.GameProfile;
import java.lang.reflect.Type;
import java.util.UUID;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.JsonUtils;

public class ServerStatusResponse {
   private IChatComponent serverMotd;
   private String favicon;
   private ServerStatusResponse.PlayerCountData playerCount;
   private ServerStatusResponse.MinecraftProtocolVersionIdentifier protocolVersion;

   public String getFavicon() {
      return this.favicon;
   }

   public IChatComponent getServerDescription() {
      return this.serverMotd;
   }

   public ServerStatusResponse.MinecraftProtocolVersionIdentifier getProtocolVersionInfo() {
      return this.protocolVersion;
   }

   public ServerStatusResponse.PlayerCountData getPlayerCountData() {
      return this.playerCount;
   }

   public void setServerDescription(IChatComponent var1) {
      this.serverMotd = var1;
   }

   public void setFavicon(String var1) {
      this.favicon = var1;
   }

   public void setPlayerCountData(ServerStatusResponse.PlayerCountData var1) {
      this.playerCount = var1;
   }

   public void setProtocolVersionInfo(ServerStatusResponse.MinecraftProtocolVersionIdentifier var1) {
      this.protocolVersion = var1;
   }

   public static class PlayerCountData {
      private final int onlinePlayerCount;
      private GameProfile[] players;
      private final int maxPlayers;

      public int getOnlinePlayerCount() {
         return this.onlinePlayerCount;
      }

      public PlayerCountData(int var1, int var2) {
         this.maxPlayers = var1;
         this.onlinePlayerCount = var2;
      }

      public GameProfile[] getPlayers() {
         return this.players;
      }

      public void setPlayers(GameProfile[] var1) {
         this.players = var1;
      }

      public int getMaxPlayers() {
         return this.maxPlayers;
      }

      public static class Serializer implements JsonSerializer, JsonDeserializer {
         public JsonElement serialize(ServerStatusResponse.PlayerCountData var1, Type var2, JsonSerializationContext var3) {
            JsonObject var4 = new JsonObject();
            var4.addProperty("max", (Number)var1.getMaxPlayers());
            var4.addProperty("online", (Number)var1.getOnlinePlayerCount());
            if (var1.getPlayers() != null && var1.getPlayers().length > 0) {
               JsonArray var5 = new JsonArray();

               for(int var6 = 0; var6 < var1.getPlayers().length; ++var6) {
                  JsonObject var7 = new JsonObject();
                  UUID var8 = var1.getPlayers()[var6].getId();
                  var7.addProperty("id", var8 == null ? "" : var8.toString());
                  var7.addProperty("name", var1.getPlayers()[var6].getName());
                  var5.add(var7);
               }

               var4.add("sample", var5);
            }

            return var4;
         }

         public Object deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
            return this.deserialize(var1, var2, var3);
         }

         public JsonElement serialize(Object var1, Type var2, JsonSerializationContext var3) {
            return this.serialize((ServerStatusResponse.PlayerCountData)var1, var2, var3);
         }

         public ServerStatusResponse.PlayerCountData deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
            JsonObject var4 = JsonUtils.getJsonObject(var1, "players");
            ServerStatusResponse.PlayerCountData var5 = new ServerStatusResponse.PlayerCountData(JsonUtils.getInt(var4, "max"), JsonUtils.getInt(var4, "online"));
            if (JsonUtils.isJsonArray(var4, "sample")) {
               JsonArray var6 = JsonUtils.getJsonArray(var4, "sample");
               if (var6.size() > 0) {
                  GameProfile[] var7 = new GameProfile[var6.size()];

                  for(int var8 = 0; var8 < var7.length; ++var8) {
                     JsonObject var9 = JsonUtils.getJsonObject(var6.get(var8), "player[" + var8 + "]");
                     String var10 = JsonUtils.getString(var9, "id");
                     var7[var8] = new GameProfile(UUID.fromString(var10), JsonUtils.getString(var9, "name"));
                  }

                  var5.setPlayers(var7);
               }
            }

            return var5;
         }
      }
   }

   public static class MinecraftProtocolVersionIdentifier {
      private final String name;
      private final int protocol;

      public String getName() {
         return this.name;
      }

      public MinecraftProtocolVersionIdentifier(String var1, int var2) {
         this.name = var1;
         this.protocol = var2;
      }

      public int getProtocol() {
         return this.protocol;
      }

      public static class Serializer implements JsonDeserializer, JsonSerializer {
         public Object deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
            return this.deserialize(var1, var2, var3);
         }

         public JsonElement serialize(ServerStatusResponse.MinecraftProtocolVersionIdentifier var1, Type var2, JsonSerializationContext var3) {
            JsonObject var4 = new JsonObject();
            var4.addProperty("name", var1.getName());
            var4.addProperty("protocol", (Number)var1.getProtocol());
            return var4;
         }

         public JsonElement serialize(Object var1, Type var2, JsonSerializationContext var3) {
            return this.serialize((ServerStatusResponse.MinecraftProtocolVersionIdentifier)var1, var2, var3);
         }

         public ServerStatusResponse.MinecraftProtocolVersionIdentifier deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
            JsonObject var4 = JsonUtils.getJsonObject(var1, "version");
            return new ServerStatusResponse.MinecraftProtocolVersionIdentifier(JsonUtils.getString(var4, "name"), JsonUtils.getInt(var4, "protocol"));
         }
      }
   }

   public static class Serializer implements JsonDeserializer, JsonSerializer {
      public Object deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         return this.deserialize(var1, var2, var3);
      }

      public JsonElement serialize(Object var1, Type var2, JsonSerializationContext var3) {
         return this.serialize((ServerStatusResponse)var1, var2, var3);
      }

      public ServerStatusResponse deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         JsonObject var4 = JsonUtils.getJsonObject(var1, "status");
         ServerStatusResponse var5 = new ServerStatusResponse();
         if (var4.has("description")) {
            var5.setServerDescription((IChatComponent)var3.deserialize(var4.get("description"), IChatComponent.class));
         }

         if (var4.has("players")) {
            var5.setPlayerCountData((ServerStatusResponse.PlayerCountData)var3.deserialize(var4.get("players"), ServerStatusResponse.PlayerCountData.class));
         }

         if (var4.has("version")) {
            var5.setProtocolVersionInfo((ServerStatusResponse.MinecraftProtocolVersionIdentifier)var3.deserialize(var4.get("version"), ServerStatusResponse.MinecraftProtocolVersionIdentifier.class));
         }

         if (var4.has("favicon")) {
            var5.setFavicon(JsonUtils.getString(var4, "favicon"));
         }

         return var5;
      }

      public JsonElement serialize(ServerStatusResponse var1, Type var2, JsonSerializationContext var3) {
         JsonObject var4 = new JsonObject();
         if (var1.getServerDescription() != null) {
            var4.add("description", var3.serialize(var1.getServerDescription()));
         }

         if (var1.getPlayerCountData() != null) {
            var4.add("players", var3.serialize(var1.getPlayerCountData()));
         }

         if (var1.getProtocolVersionInfo() != null) {
            var4.add("version", var3.serialize(var1.getProtocolVersionInfo()));
         }

         if (var1.getFavicon() != null) {
            var4.addProperty("favicon", var1.getFavicon());
         }

         return var4;
      }
   }
}
