package net.minecraft.server.management;

import com.google.common.base.Charsets;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
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
import net.minecraft.server.management.PlayerProfileCache;
import org.apache.commons.io.IOUtils;

/*
 * Exception performing whole class analysis ignored.
 */
public class PlayerProfileCache {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    private final Map<String, ProfileEntry> usernameToProfileEntryMap = Maps.newHashMap();
    private final Map<UUID, ProfileEntry> uuidToProfileEntryMap = Maps.newHashMap();
    private final LinkedList<GameProfile> gameProfiles = Lists.newLinkedList();
    private final MinecraftServer mcServer;
    protected final Gson gson;
    private final File usercacheFile;
    private static final ParameterizedType TYPE = new /* Unavailable Anonymous Inner Class!! */;

    public PlayerProfileCache(MinecraftServer server, File cacheFile) {
        this.mcServer = server;
        this.usercacheFile = cacheFile;
        GsonBuilder gsonbuilder = new GsonBuilder();
        gsonbuilder.registerTypeHierarchyAdapter(ProfileEntry.class, (Object)new Serializer(this, null));
        this.gson = gsonbuilder.create();
        this.load();
    }

    private static GameProfile getGameProfile(MinecraftServer server, String username) {
        GameProfile[] agameprofile = new GameProfile[1];
        2 profilelookupcallback = new /* Unavailable Anonymous Inner Class!! */;
        server.getGameProfileRepository().findProfilesByNames(new String[]{username}, Agent.MINECRAFT, (ProfileLookupCallback)profilelookupcallback);
        if (!server.isServerInOnlineMode() && agameprofile[0] == null) {
            UUID uuid = EntityPlayer.getUUID((GameProfile)new GameProfile((UUID)null, username));
            GameProfile gameprofile = new GameProfile(uuid, username);
            profilelookupcallback.onProfileLookupSucceeded(gameprofile);
        }
        return agameprofile[0];
    }

    public void addEntry(GameProfile gameProfile) {
        this.addEntry(gameProfile, null);
    }

    private void addEntry(GameProfile gameProfile, Date expirationDate) {
        UUID uuid = gameProfile.getId();
        if (expirationDate == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(2, 1);
            expirationDate = calendar.getTime();
        }
        String s = gameProfile.getName().toLowerCase(Locale.ROOT);
        ProfileEntry playerprofilecache$profileentry = new ProfileEntry(this, gameProfile, expirationDate, null);
        if (this.uuidToProfileEntryMap.containsKey((Object)uuid)) {
            ProfileEntry playerprofilecache$profileentry1 = (ProfileEntry)this.uuidToProfileEntryMap.get((Object)uuid);
            this.usernameToProfileEntryMap.remove((Object)playerprofilecache$profileentry1.getGameProfile().getName().toLowerCase(Locale.ROOT));
            this.gameProfiles.remove((Object)gameProfile);
        }
        this.usernameToProfileEntryMap.put((Object)gameProfile.getName().toLowerCase(Locale.ROOT), (Object)playerprofilecache$profileentry);
        this.uuidToProfileEntryMap.put((Object)uuid, (Object)playerprofilecache$profileentry);
        this.gameProfiles.addFirst((Object)gameProfile);
        this.save();
    }

    public GameProfile getGameProfileForUsername(String username) {
        String s = username.toLowerCase(Locale.ROOT);
        ProfileEntry playerprofilecache$profileentry = (ProfileEntry)this.usernameToProfileEntryMap.get((Object)s);
        if (playerprofilecache$profileentry != null && new Date().getTime() >= ProfileEntry.access$200((ProfileEntry)playerprofilecache$profileentry).getTime()) {
            this.uuidToProfileEntryMap.remove((Object)playerprofilecache$profileentry.getGameProfile().getId());
            this.usernameToProfileEntryMap.remove((Object)playerprofilecache$profileentry.getGameProfile().getName().toLowerCase(Locale.ROOT));
            this.gameProfiles.remove((Object)playerprofilecache$profileentry.getGameProfile());
            playerprofilecache$profileentry = null;
        }
        if (playerprofilecache$profileentry != null) {
            GameProfile gameprofile = playerprofilecache$profileentry.getGameProfile();
            this.gameProfiles.remove((Object)gameprofile);
            this.gameProfiles.addFirst((Object)gameprofile);
        } else {
            GameProfile gameprofile1 = PlayerProfileCache.getGameProfile(this.mcServer, s);
            if (gameprofile1 != null) {
                this.addEntry(gameprofile1);
                playerprofilecache$profileentry = (ProfileEntry)this.usernameToProfileEntryMap.get((Object)s);
            }
        }
        this.save();
        return playerprofilecache$profileentry == null ? null : playerprofilecache$profileentry.getGameProfile();
    }

    public String[] getUsernames() {
        ArrayList list = Lists.newArrayList((Iterable)this.usernameToProfileEntryMap.keySet());
        return (String[])list.toArray((Object[])new String[list.size()]);
    }

    public GameProfile getProfileByUUID(UUID uuid) {
        ProfileEntry playerprofilecache$profileentry = (ProfileEntry)this.uuidToProfileEntryMap.get((Object)uuid);
        return playerprofilecache$profileentry == null ? null : playerprofilecache$profileentry.getGameProfile();
    }

    private ProfileEntry getByUUID(UUID uuid) {
        ProfileEntry playerprofilecache$profileentry = (ProfileEntry)this.uuidToProfileEntryMap.get((Object)uuid);
        if (playerprofilecache$profileentry != null) {
            GameProfile gameprofile = playerprofilecache$profileentry.getGameProfile();
            this.gameProfiles.remove((Object)gameprofile);
            this.gameProfiles.addFirst((Object)gameprofile);
        }
        return playerprofilecache$profileentry;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void load() {
        BufferedReader bufferedreader = null;
        try {
            bufferedreader = Files.newReader((File)this.usercacheFile, (Charset)Charsets.UTF_8);
            List list = (List)this.gson.fromJson((Reader)bufferedreader, (Type)TYPE);
            this.usernameToProfileEntryMap.clear();
            this.uuidToProfileEntryMap.clear();
            this.gameProfiles.clear();
            for (ProfileEntry playerprofilecache$profileentry : Lists.reverse((List)list)) {
                if (playerprofilecache$profileentry == null) continue;
                this.addEntry(playerprofilecache$profileentry.getGameProfile(), playerprofilecache$profileentry.getExpirationDate());
            }
        }
        catch (FileNotFoundException fileNotFoundException) {
            IOUtils.closeQuietly(bufferedreader);
        }
        catch (JsonParseException jsonParseException) {
            IOUtils.closeQuietly(bufferedreader);
        }
        catch (Throwable throwable) {
            IOUtils.closeQuietly(bufferedreader);
            throw throwable;
        }
        IOUtils.closeQuietly((Reader)bufferedreader);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void save() {
        String s = this.gson.toJson(this.getEntriesWithLimit(1000));
        BufferedWriter bufferedwriter = null;
        try {
            bufferedwriter = Files.newWriter((File)this.usercacheFile, (Charset)Charsets.UTF_8);
            bufferedwriter.write(s);
        }
        catch (FileNotFoundException fileNotFoundException) {
            IOUtils.closeQuietly(bufferedwriter);
        }
        catch (IOException var9) {
            IOUtils.closeQuietly(bufferedwriter);
            return;
        }
        catch (Throwable throwable) {
            IOUtils.closeQuietly(bufferedwriter);
            throw throwable;
        }
        IOUtils.closeQuietly((Writer)bufferedwriter);
        return;
    }

    private List<ProfileEntry> getEntriesWithLimit(int limitSize) {
        ArrayList arraylist = Lists.newArrayList();
        for (GameProfile gameprofile : Lists.newArrayList((Iterator)Iterators.limit((Iterator)this.gameProfiles.iterator(), (int)limitSize))) {
            ProfileEntry playerprofilecache$profileentry = this.getByUUID(gameprofile.getId());
            if (playerprofilecache$profileentry == null) continue;
            arraylist.add((Object)playerprofilecache$profileentry);
        }
        return arraylist;
    }
}
