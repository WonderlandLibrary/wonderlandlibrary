package net.minecraft.server.management;

import com.google.common.base.Charsets;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.ProfileLookupCallback;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.io.IOUtils;

public class PlayerProfileCache {
   private final MinecraftServer mcServer;
   private final File usercacheFile;
   private final Map usernameToProfileEntryMap = Maps.newHashMap();
   protected final Gson gson;
   private static final ParameterizedType TYPE = new ParameterizedType() {
      public Type getRawType() {
         return List.class;
      }

      public Type getOwnerType() {
         return null;
      }

      public Type[] getActualTypeArguments() {
         return new Type[]{PlayerProfileCache.ProfileEntry.class};
      }
   };
   private final Map uuidToProfileEntryMap = Maps.newHashMap();
   private final LinkedList gameProfiles = Lists.newLinkedList();
   public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

   public void load() {
      BufferedReader var1 = null;

      try {
         var1 = Files.newReader(this.usercacheFile, Charsets.UTF_8);
         List var2 = (List)this.gson.fromJson((Reader)var1, (Type)TYPE);
         this.usernameToProfileEntryMap.clear();
         this.uuidToProfileEntryMap.clear();
         this.gameProfiles.clear();
         Iterator var4 = Lists.reverse(var2).iterator();

         while(var4.hasNext()) {
            PlayerProfileCache.ProfileEntry var3 = (PlayerProfileCache.ProfileEntry)var4.next();
            if (var3 != null) {
               this.addEntry(var3.getGameProfile(), var3.getExpirationDate());
            }
         }
      } catch (FileNotFoundException var7) {
         IOUtils.closeQuietly((Reader)var1);
         return;
      } catch (JsonParseException var8) {
         IOUtils.closeQuietly((Reader)var1);
         return;
      }

      IOUtils.closeQuietly((Reader)var1);
   }

   private void addEntry(GameProfile var1, Date var2) {
      UUID var3 = var1.getId();
      if (var2 == null) {
         Calendar var4 = Calendar.getInstance();
         var4.setTime(new Date());
         var4.add(2, 1);
         var2 = var4.getTime();
      }

      String var7 = var1.getName().toLowerCase(Locale.ROOT);
      PlayerProfileCache.ProfileEntry var5 = new PlayerProfileCache.ProfileEntry(this, var1, var2, (PlayerProfileCache.ProfileEntry)null);
      if (this.uuidToProfileEntryMap.containsKey(var3)) {
         PlayerProfileCache.ProfileEntry var6 = (PlayerProfileCache.ProfileEntry)this.uuidToProfileEntryMap.get(var3);
         this.usernameToProfileEntryMap.remove(var6.getGameProfile().getName().toLowerCase(Locale.ROOT));
         this.gameProfiles.remove(var1);
      }

      this.usernameToProfileEntryMap.put(var1.getName().toLowerCase(Locale.ROOT), var5);
      this.uuidToProfileEntryMap.put(var3, var5);
      this.gameProfiles.addFirst(var1);
      this.save();
   }

   public void addEntry(GameProfile var1) {
      this.addEntry(var1, (Date)null);
   }

   private PlayerProfileCache.ProfileEntry getByUUID(UUID var1) {
      PlayerProfileCache.ProfileEntry var2 = (PlayerProfileCache.ProfileEntry)this.uuidToProfileEntryMap.get(var1);
      if (var2 != null) {
         GameProfile var3 = var2.getGameProfile();
         this.gameProfiles.remove(var3);
         this.gameProfiles.addFirst(var3);
      }

      return var2;
   }

   public GameProfile getProfileByUUID(UUID var1) {
      PlayerProfileCache.ProfileEntry var2 = (PlayerProfileCache.ProfileEntry)this.uuidToProfileEntryMap.get(var1);
      return var2 == null ? null : var2.getGameProfile();
   }

   public void save() {
      String var1 = this.gson.toJson((Object)this.getEntriesWithLimit(1000));
      BufferedWriter var2 = null;

      try {
         var2 = Files.newWriter(this.usercacheFile, Charsets.UTF_8);
         var2.write(var1);
      } catch (FileNotFoundException var6) {
         IOUtils.closeQuietly((Writer)var2);
         return;
      } catch (IOException var7) {
         IOUtils.closeQuietly((Writer)var2);
         return;
      }

      IOUtils.closeQuietly((Writer)var2);
   }

   private static GameProfile getGameProfile(MinecraftServer var0, String var1) {
      GameProfile[] var2 = new GameProfile[1];
      ProfileLookupCallback var3 = new ProfileLookupCallback(var2) {
         private final GameProfile[] val$agameprofile;

         public void onProfileLookupSucceeded(GameProfile var1) {
            this.val$agameprofile[0] = var1;
         }

         public void onProfileLookupFailed(GameProfile var1, Exception var2) {
            this.val$agameprofile[0] = null;
         }

         {
            this.val$agameprofile = var1;
         }
      };
      var0.getGameProfileRepository().findProfilesByNames(new String[]{var1}, Agent.MINECRAFT, var3);
      if (!var0.isServerInOnlineMode() && var2[0] == null) {
         UUID var4 = EntityPlayer.getUUID(new GameProfile((UUID)null, var1));
         GameProfile var5 = new GameProfile(var4, var1);
         var3.onProfileLookupSucceeded(var5);
      }

      return var2[0];
   }

   public PlayerProfileCache(MinecraftServer var1, File var2) {
      this.mcServer = var1;
      this.usercacheFile = var2;
      GsonBuilder var3 = new GsonBuilder();
      var3.registerTypeHierarchyAdapter(PlayerProfileCache.ProfileEntry.class, new PlayerProfileCache.Serializer(this, (PlayerProfileCache.Serializer)null));
      this.gson = var3.create();
      this.load();
   }

   private List getEntriesWithLimit(int var1) {
      ArrayList var2 = Lists.newArrayList();
      Iterator var4 = Lists.newArrayList(Iterators.limit(this.gameProfiles.iterator(), var1)).iterator();

      while(var4.hasNext()) {
         GameProfile var3 = (GameProfile)var4.next();
         PlayerProfileCache.ProfileEntry var5 = this.getByUUID(var3.getId());
         if (var5 != null) {
            var2.add(var5);
         }
      }

      return var2;
   }

   public GameProfile getGameProfileForUsername(String var1) {
      String var2 = var1.toLowerCase(Locale.ROOT);
      PlayerProfileCache.ProfileEntry var3 = (PlayerProfileCache.ProfileEntry)this.usernameToProfileEntryMap.get(var2);
      if (var3 != null && (new Date()).getTime() >= PlayerProfileCache.ProfileEntry.access$1(var3).getTime()) {
         this.uuidToProfileEntryMap.remove(var3.getGameProfile().getId());
         this.usernameToProfileEntryMap.remove(var3.getGameProfile().getName().toLowerCase(Locale.ROOT));
         this.gameProfiles.remove(var3.getGameProfile());
         var3 = null;
      }

      GameProfile var4;
      if (var3 != null) {
         var4 = var3.getGameProfile();
         this.gameProfiles.remove(var4);
         this.gameProfiles.addFirst(var4);
      } else {
         var4 = getGameProfile(this.mcServer, var2);
         if (var4 != null) {
            this.addEntry(var4);
            var3 = (PlayerProfileCache.ProfileEntry)this.usernameToProfileEntryMap.get(var2);
         }
      }

      this.save();
      return var3 == null ? null : var3.getGameProfile();
   }

   public String[] getUsernames() {
      ArrayList var1 = Lists.newArrayList((Iterable)this.usernameToProfileEntryMap.keySet());
      return (String[])var1.toArray(new String[var1.size()]);
   }

   class Serializer implements JsonSerializer, JsonDeserializer {
      final PlayerProfileCache this$0;

      public JsonElement serialize(Object var1, Type var2, JsonSerializationContext var3) {
         return this.serialize((PlayerProfileCache.ProfileEntry)var1, var2, var3);
      }

      public JsonElement serialize(PlayerProfileCache.ProfileEntry var1, Type var2, JsonSerializationContext var3) {
         JsonObject var4 = new JsonObject();
         var4.addProperty("name", var1.getGameProfile().getName());
         UUID var5 = var1.getGameProfile().getId();
         var4.addProperty("uuid", var5 == null ? "" : var5.toString());
         var4.addProperty("expiresOn", PlayerProfileCache.dateFormat.format(var1.getExpirationDate()));
         return var4;
      }

      private Serializer(PlayerProfileCache var1) {
         this.this$0 = var1;
      }

      public PlayerProfileCache.ProfileEntry deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         if (var1.isJsonObject()) {
            JsonObject var4 = var1.getAsJsonObject();
            JsonElement var5 = var4.get("name");
            JsonElement var6 = var4.get("uuid");
            JsonElement var7 = var4.get("expiresOn");
            if (var5 != null && var6 != null) {
               String var8 = var6.getAsString();
               String var9 = var5.getAsString();
               Date var10 = null;
               if (var7 != null) {
                  try {
                     var10 = PlayerProfileCache.dateFormat.parse(var7.getAsString());
                  } catch (ParseException var14) {
                     var10 = null;
                  }
               }

               if (var9 != null && var8 != null) {
                  UUID var11;
                  try {
                     var11 = UUID.fromString(var8);
                  } catch (Throwable var13) {
                     return null;
                  }

                  PlayerProfileCache.ProfileEntry var12 = this.this$0.new ProfileEntry(this.this$0, new GameProfile(var11, var9), var10, (PlayerProfileCache.ProfileEntry)null);
                  return var12;
               } else {
                  return null;
               }
            } else {
               return null;
            }
         } else {
            return null;
         }
      }

      public Object deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         return this.deserialize(var1, var2, var3);
      }

      Serializer(PlayerProfileCache var1, PlayerProfileCache.Serializer var2) {
         this(var1);
      }
   }

   class ProfileEntry {
      private final GameProfile gameProfile;
      private final Date expirationDate;
      final PlayerProfileCache this$0;

      public GameProfile getGameProfile() {
         return this.gameProfile;
      }

      ProfileEntry(PlayerProfileCache var1, GameProfile var2, Date var3, PlayerProfileCache.ProfileEntry var4) {
         this(var1, var2, var3);
      }

      static Date access$1(PlayerProfileCache.ProfileEntry var0) {
         return var0.expirationDate;
      }

      public Date getExpirationDate() {
         return this.expirationDate;
      }

      private ProfileEntry(PlayerProfileCache var1, GameProfile var2, Date var3) {
         this.this$0 = var1;
         this.gameProfile = var2;
         this.expirationDate = var3;
      }
   }
}
