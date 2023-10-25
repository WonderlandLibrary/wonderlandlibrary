/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParseException
 *  com.google.gson.JsonParser
 *  org.apache.commons.io.FileUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.stats;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S37PacketStatistics;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.TupleIntJsonSerializable;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StatisticsFile
extends StatFileWriter {
    private static final Logger logger = LogManager.getLogger();
    private int field_150885_f = -300;
    private final MinecraftServer mcServer;
    private boolean field_150886_g = false;
    private final File statsFile;
    private final Set<StatBase> field_150888_e = Sets.newHashSet();

    public boolean func_150879_e() {
        return this.field_150886_g;
    }

    public void sendAchievements(EntityPlayerMP entityPlayerMP) {
        HashMap hashMap = Maps.newHashMap();
        for (Achievement achievement : AchievementList.achievementList) {
            if (!this.hasAchievementUnlocked(achievement)) continue;
            hashMap.put(achievement, this.readStat(achievement));
            this.field_150888_e.remove(achievement);
        }
        entityPlayerMP.playerNetServerHandler.sendPacket(new S37PacketStatistics(hashMap));
    }

    public StatisticsFile(MinecraftServer minecraftServer, File file) {
        this.mcServer = minecraftServer;
        this.statsFile = file;
    }

    public void func_150876_a(EntityPlayerMP entityPlayerMP) {
        int n = this.mcServer.getTickCounter();
        HashMap hashMap = Maps.newHashMap();
        if (this.field_150886_g || n - this.field_150885_f > 300) {
            this.field_150885_f = n;
            for (StatBase statBase : this.func_150878_c()) {
                hashMap.put(statBase, this.readStat(statBase));
            }
        }
        entityPlayerMP.playerNetServerHandler.sendPacket(new S37PacketStatistics(hashMap));
    }

    public Map<StatBase, TupleIntJsonSerializable> parseJson(String string) {
        JsonElement jsonElement = new JsonParser().parse(string);
        if (!jsonElement.isJsonObject()) {
            return Maps.newHashMap();
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        HashMap hashMap = Maps.newHashMap();
        for (Map.Entry entry : jsonObject.entrySet()) {
            StatBase statBase = StatList.getOneShotStat((String)entry.getKey());
            if (statBase != null) {
                TupleIntJsonSerializable tupleIntJsonSerializable = new TupleIntJsonSerializable();
                if (((JsonElement)entry.getValue()).isJsonPrimitive() && ((JsonElement)entry.getValue()).getAsJsonPrimitive().isNumber()) {
                    tupleIntJsonSerializable.setIntegerValue(((JsonElement)entry.getValue()).getAsInt());
                } else if (((JsonElement)entry.getValue()).isJsonObject()) {
                    JsonObject jsonObject2 = ((JsonElement)entry.getValue()).getAsJsonObject();
                    if (jsonObject2.has("value") && jsonObject2.get("value").isJsonPrimitive() && jsonObject2.get("value").getAsJsonPrimitive().isNumber()) {
                        tupleIntJsonSerializable.setIntegerValue(jsonObject2.getAsJsonPrimitive("value").getAsInt());
                    }
                    if (jsonObject2.has("progress") && statBase.func_150954_l() != null) {
                        try {
                            Constructor<? extends IJsonSerializable> constructor = statBase.func_150954_l().getConstructor(new Class[0]);
                            IJsonSerializable iJsonSerializable = constructor.newInstance(new Object[0]);
                            iJsonSerializable.fromJson(jsonObject2.get("progress"));
                            tupleIntJsonSerializable.setJsonSerializableValue(iJsonSerializable);
                        }
                        catch (Throwable throwable) {
                            logger.warn("Invalid statistic progress in " + this.statsFile, throwable);
                        }
                    }
                }
                hashMap.put(statBase, tupleIntJsonSerializable);
                continue;
            }
            logger.warn("Invalid statistic in " + this.statsFile + ": Don't know what " + (String)entry.getKey() + " is");
        }
        return hashMap;
    }

    public Set<StatBase> func_150878_c() {
        HashSet hashSet = Sets.newHashSet(this.field_150888_e);
        this.field_150888_e.clear();
        this.field_150886_g = false;
        return hashSet;
    }

    public void readStatFile() {
        if (this.statsFile.isFile()) {
            try {
                this.statsData.clear();
                this.statsData.putAll(this.parseJson(FileUtils.readFileToString((File)this.statsFile)));
            }
            catch (IOException iOException) {
                logger.error("Couldn't read statistics file " + this.statsFile, (Throwable)iOException);
            }
            catch (JsonParseException jsonParseException) {
                logger.error("Couldn't parse statistics file " + this.statsFile, (Throwable)jsonParseException);
            }
        }
    }

    public void func_150877_d() {
        for (StatBase statBase : this.statsData.keySet()) {
            this.field_150888_e.add(statBase);
        }
    }

    public static String dumpJson(Map<StatBase, TupleIntJsonSerializable> map) {
        JsonObject jsonObject = new JsonObject();
        for (Map.Entry<StatBase, TupleIntJsonSerializable> entry : map.entrySet()) {
            if (entry.getValue().getJsonSerializableValue() != null) {
                JsonObject jsonObject2 = new JsonObject();
                jsonObject2.addProperty("value", (Number)entry.getValue().getIntegerValue());
                try {
                    jsonObject2.add("progress", entry.getValue().getJsonSerializableValue().getSerializableElement());
                }
                catch (Throwable throwable) {
                    logger.warn("Couldn't save statistic " + entry.getKey().getStatName() + ": error serializing progress", throwable);
                }
                jsonObject.add(entry.getKey().statId, (JsonElement)jsonObject2);
                continue;
            }
            jsonObject.addProperty(entry.getKey().statId, (Number)entry.getValue().getIntegerValue());
        }
        return jsonObject.toString();
    }

    public void saveStatFile() {
        try {
            FileUtils.writeStringToFile((File)this.statsFile, (String)StatisticsFile.dumpJson(this.statsData));
        }
        catch (IOException iOException) {
            logger.error("Couldn't save stats", (Throwable)iOException);
        }
    }

    @Override
    public void unlockAchievement(EntityPlayer entityPlayer, StatBase statBase, int n) {
        int n2 = statBase.isAchievement() ? this.readStat(statBase) : 0;
        super.unlockAchievement(entityPlayer, statBase, n);
        this.field_150888_e.add(statBase);
        if (statBase.isAchievement() && n2 == 0 && n > 0) {
            this.field_150886_g = true;
            if (this.mcServer.isAnnouncingPlayerAchievements()) {
                this.mcServer.getConfigurationManager().sendChatMsg(new ChatComponentTranslation("chat.type.achievement", entityPlayer.getDisplayName(), statBase.func_150955_j()));
            }
        }
        if (statBase.isAchievement() && n2 > 0 && n == 0) {
            this.field_150886_g = true;
            if (this.mcServer.isAnnouncingPlayerAchievements()) {
                this.mcServer.getConfigurationManager().sendChatMsg(new ChatComponentTranslation("chat.type.achievement.taken", entityPlayer.getDisplayName(), statBase.func_150955_j()));
            }
        }
    }
}

