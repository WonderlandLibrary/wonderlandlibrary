/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Charsets
 *  com.google.common.collect.Iterators
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.io.Files
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonDeserializationContext
 *  com.google.gson.JsonDeserializer
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParseException
 *  com.google.gson.JsonSerializationContext
 *  com.google.gson.JsonSerializer
 *  com.mojang.authlib.Agent
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.ProfileLookupCallback
 *  org.apache.commons.io.IOUtils
 */
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
import java.nio.charset.Charset;
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
    private final LinkedList<GameProfile> gameProfiles;
    private final File usercacheFile;
    private final Map<String, ProfileEntry> usernameToProfileEntryMap = Maps.newHashMap();
    protected final Gson gson;
    private static final ParameterizedType TYPE;
    private final MinecraftServer mcServer;
    private final Map<UUID, ProfileEntry> uuidToProfileEntryMap = Maps.newHashMap();
    public static final SimpleDateFormat dateFormat;

    private void addEntry(GameProfile gameProfile, Date date) {
        Object object;
        UUID uUID = gameProfile.getId();
        if (date == null) {
            object = Calendar.getInstance();
            ((Calendar)object).setTime(new Date());
            ((Calendar)object).add(2, 1);
            date = ((Calendar)object).getTime();
        }
        object = gameProfile.getName().toLowerCase(Locale.ROOT);
        ProfileEntry profileEntry = new ProfileEntry(gameProfile, date);
        if (this.uuidToProfileEntryMap.containsKey(uUID)) {
            ProfileEntry profileEntry2 = this.uuidToProfileEntryMap.get(uUID);
            this.usernameToProfileEntryMap.remove(profileEntry2.getGameProfile().getName().toLowerCase(Locale.ROOT));
            this.gameProfiles.remove(gameProfile);
        }
        this.usernameToProfileEntryMap.put(gameProfile.getName().toLowerCase(Locale.ROOT), profileEntry);
        this.uuidToProfileEntryMap.put(uUID, profileEntry);
        this.gameProfiles.addFirst(gameProfile);
        this.save();
    }

    static {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        TYPE = new ParameterizedType(){

            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{ProfileEntry.class};
            }

            @Override
            public Type getOwnerType() {
                return null;
            }

            @Override
            public Type getRawType() {
                return List.class;
            }
        };
    }

    public GameProfile getProfileByUUID(UUID uUID) {
        ProfileEntry profileEntry = this.uuidToProfileEntryMap.get(uUID);
        return profileEntry == null ? null : profileEntry.getGameProfile();
    }

    public PlayerProfileCache(MinecraftServer minecraftServer, File file) {
        this.gameProfiles = Lists.newLinkedList();
        this.mcServer = minecraftServer;
        this.usercacheFile = file;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeHierarchyAdapter(ProfileEntry.class, (Object)new Serializer());
        this.gson = gsonBuilder.create();
        this.load();
    }

    public void addEntry(GameProfile gameProfile) {
        this.addEntry(gameProfile, null);
    }

    private List<ProfileEntry> getEntriesWithLimit(int n) {
        ArrayList arrayList = Lists.newArrayList();
        for (GameProfile gameProfile : Lists.newArrayList((Iterator)Iterators.limit(this.gameProfiles.iterator(), (int)n))) {
            ProfileEntry profileEntry = this.getByUUID(gameProfile.getId());
            if (profileEntry == null) continue;
            arrayList.add(profileEntry);
        }
        return arrayList;
    }

    private ProfileEntry getByUUID(UUID uUID) {
        ProfileEntry profileEntry = this.uuidToProfileEntryMap.get(uUID);
        if (profileEntry != null) {
            GameProfile gameProfile = profileEntry.getGameProfile();
            this.gameProfiles.remove(gameProfile);
            this.gameProfiles.addFirst(gameProfile);
        }
        return profileEntry;
    }

    private static GameProfile getGameProfile(MinecraftServer minecraftServer, String string) {
        final GameProfile[] gameProfileArray = new GameProfile[1];
        ProfileLookupCallback profileLookupCallback = new ProfileLookupCallback(){

            public void onProfileLookupSucceeded(GameProfile gameProfile) {
                gameProfileArray[0] = gameProfile;
            }

            public void onProfileLookupFailed(GameProfile gameProfile, Exception exception) {
                gameProfileArray[0] = null;
            }
        };
        minecraftServer.getGameProfileRepository().findProfilesByNames(new String[]{string}, Agent.MINECRAFT, profileLookupCallback);
        if (!minecraftServer.isServerInOnlineMode() && gameProfileArray[0] == null) {
            UUID uUID = EntityPlayer.getUUID(new GameProfile(null, string));
            GameProfile gameProfile = new GameProfile(uUID, string);
            profileLookupCallback.onProfileLookupSucceeded(gameProfile);
        }
        return gameProfileArray[0];
    }

    public void save() {
        String string = this.gson.toJson(this.getEntriesWithLimit(1000));
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = Files.newWriter((File)this.usercacheFile, (Charset)Charsets.UTF_8);
            bufferedWriter.write(string);
        }
        catch (FileNotFoundException fileNotFoundException) {
            IOUtils.closeQuietly(bufferedWriter);
        }
        catch (IOException iOException) {
            IOUtils.closeQuietly(bufferedWriter);
            return;
        }
        IOUtils.closeQuietly((Writer)bufferedWriter);
        return;
    }

    public GameProfile getGameProfileForUsername(String string) {
        String string2 = string.toLowerCase(Locale.ROOT);
        ProfileEntry profileEntry = this.usernameToProfileEntryMap.get(string2);
        if (profileEntry != null && new Date().getTime() >= profileEntry.expirationDate.getTime()) {
            this.uuidToProfileEntryMap.remove(profileEntry.getGameProfile().getId());
            this.usernameToProfileEntryMap.remove(profileEntry.getGameProfile().getName().toLowerCase(Locale.ROOT));
            this.gameProfiles.remove(profileEntry.getGameProfile());
            profileEntry = null;
        }
        if (profileEntry != null) {
            GameProfile gameProfile = profileEntry.getGameProfile();
            this.gameProfiles.remove(gameProfile);
            this.gameProfiles.addFirst(gameProfile);
        } else {
            GameProfile gameProfile = PlayerProfileCache.getGameProfile(this.mcServer, string2);
            if (gameProfile != null) {
                this.addEntry(gameProfile);
                profileEntry = this.usernameToProfileEntryMap.get(string2);
            }
        }
        this.save();
        return profileEntry == null ? null : profileEntry.getGameProfile();
    }

    public String[] getUsernames() {
        ArrayList arrayList = Lists.newArrayList(this.usernameToProfileEntryMap.keySet());
        return arrayList.toArray(new String[arrayList.size()]);
    }

    public void load() {
        block4: {
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = Files.newReader((File)this.usercacheFile, (Charset)Charsets.UTF_8);
                List list = (List)this.gson.fromJson((Reader)bufferedReader, (Type)TYPE);
                this.usernameToProfileEntryMap.clear();
                this.uuidToProfileEntryMap.clear();
                this.gameProfiles.clear();
                for (ProfileEntry profileEntry : Lists.reverse((List)list)) {
                    if (profileEntry == null) continue;
                    this.addEntry(profileEntry.getGameProfile(), profileEntry.getExpirationDate());
                }
            }
            catch (FileNotFoundException fileNotFoundException) {
                IOUtils.closeQuietly(bufferedReader);
                break block4;
            }
            catch (JsonParseException jsonParseException) {
                IOUtils.closeQuietly(bufferedReader);
                break block4;
            }
            IOUtils.closeQuietly((Reader)bufferedReader);
        }
    }

    class Serializer
    implements JsonDeserializer<ProfileEntry>,
    JsonSerializer<ProfileEntry> {
        public ProfileEntry deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonElement jsonElement2 = jsonObject.get("name");
                JsonElement jsonElement3 = jsonObject.get("uuid");
                JsonElement jsonElement4 = jsonObject.get("expiresOn");
                if (jsonElement2 != null && jsonElement3 != null) {
                    String string = jsonElement3.getAsString();
                    String string2 = jsonElement2.getAsString();
                    Date date = null;
                    if (jsonElement4 != null) {
                        try {
                            date = dateFormat.parse(jsonElement4.getAsString());
                        }
                        catch (ParseException parseException) {
                            date = null;
                        }
                    }
                    if (string2 != null && string != null) {
                        UUID uUID;
                        try {
                            uUID = UUID.fromString(string);
                        }
                        catch (Throwable throwable) {
                            return null;
                        }
                        PlayerProfileCache playerProfileCache = PlayerProfileCache.this;
                        playerProfileCache.getClass();
                        ProfileEntry profileEntry = playerProfileCache.new ProfileEntry(new GameProfile(uUID, string2), date);
                        return profileEntry;
                    }
                    return null;
                }
                return null;
            }
            return null;
        }

        private Serializer() {
        }

        public JsonElement serialize(ProfileEntry profileEntry, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("name", profileEntry.getGameProfile().getName());
            UUID uUID = profileEntry.getGameProfile().getId();
            jsonObject.addProperty("uuid", uUID == null ? "" : uUID.toString());
            jsonObject.addProperty("expiresOn", dateFormat.format(profileEntry.getExpirationDate()));
            return jsonObject;
        }
    }

    class ProfileEntry {
        private final Date expirationDate;
        private final GameProfile gameProfile;

        private ProfileEntry(GameProfile gameProfile, Date date) {
            this.gameProfile = gameProfile;
            this.expirationDate = date;
        }

        public GameProfile getGameProfile() {
            return this.gameProfile;
        }

        public Date getExpirationDate() {
            return this.expirationDate;
        }
    }
}

