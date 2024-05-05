/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
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
import java.util.Iterator;
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
    private final MinecraftServer mcServer;
    private final File statsFile;
    private final Set<StatBase> field_150888_e = Sets.newHashSet();
    private int field_150885_f = -300;
    private boolean field_150886_g = false;

    public StatisticsFile(MinecraftServer serverIn, File statsFileIn) {
        this.mcServer = serverIn;
        this.statsFile = statsFileIn;
    }

    public void readStatFile() {
        if (!this.statsFile.isFile()) return;
        try {
            this.statsData.clear();
            this.statsData.putAll(this.parseJson(FileUtils.readFileToString(this.statsFile)));
            return;
        }
        catch (IOException ioexception) {
            logger.error("Couldn't read statistics file " + this.statsFile, (Throwable)ioexception);
            return;
        }
        catch (JsonParseException jsonparseexception) {
            logger.error("Couldn't parse statistics file " + this.statsFile, (Throwable)jsonparseexception);
        }
    }

    public void saveStatFile() {
        try {
            FileUtils.writeStringToFile(this.statsFile, StatisticsFile.dumpJson(this.statsData));
            return;
        }
        catch (IOException ioexception) {
            logger.error("Couldn't save stats", (Throwable)ioexception);
        }
    }

    @Override
    public void unlockAchievement(EntityPlayer playerIn, StatBase statIn, int p_150873_3_) {
        int i = statIn.isAchievement() ? this.readStat(statIn) : 0;
        super.unlockAchievement(playerIn, statIn, p_150873_3_);
        this.field_150888_e.add(statIn);
        if (statIn.isAchievement() && i == 0 && p_150873_3_ > 0) {
            this.field_150886_g = true;
            if (this.mcServer.isAnnouncingPlayerAchievements()) {
                this.mcServer.getConfigurationManager().sendChatMsg(new ChatComponentTranslation("chat.type.achievement", playerIn.getDisplayName(), statIn.func_150955_j()));
            }
        }
        if (!statIn.isAchievement()) return;
        if (i <= 0) return;
        if (p_150873_3_ != 0) return;
        this.field_150886_g = true;
        if (!this.mcServer.isAnnouncingPlayerAchievements()) return;
        this.mcServer.getConfigurationManager().sendChatMsg(new ChatComponentTranslation("chat.type.achievement.taken", playerIn.getDisplayName(), statIn.func_150955_j()));
    }

    public Set<StatBase> func_150878_c() {
        HashSet<StatBase> set = Sets.newHashSet(this.field_150888_e);
        this.field_150888_e.clear();
        this.field_150886_g = false;
        return set;
    }

    public Map<StatBase, TupleIntJsonSerializable> parseJson(String p_150881_1_) {
        JsonElement jsonelement = new JsonParser().parse(p_150881_1_);
        if (!jsonelement.isJsonObject()) {
            return Maps.newHashMap();
        }
        JsonObject jsonobject = jsonelement.getAsJsonObject();
        HashMap<StatBase, TupleIntJsonSerializable> map = Maps.newHashMap();
        Iterator<Map.Entry<String, JsonElement>> iterator = jsonobject.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonElement> entry = iterator.next();
            StatBase statbase = StatList.getOneShotStat(entry.getKey());
            if (statbase != null) {
                TupleIntJsonSerializable tupleintjsonserializable = new TupleIntJsonSerializable();
                if (entry.getValue().isJsonPrimitive() && entry.getValue().getAsJsonPrimitive().isNumber()) {
                    tupleintjsonserializable.setIntegerValue(entry.getValue().getAsInt());
                } else if (entry.getValue().isJsonObject()) {
                    JsonObject jsonobject1 = entry.getValue().getAsJsonObject();
                    if (jsonobject1.has("value") && jsonobject1.get("value").isJsonPrimitive() && jsonobject1.get("value").getAsJsonPrimitive().isNumber()) {
                        tupleintjsonserializable.setIntegerValue(jsonobject1.getAsJsonPrimitive("value").getAsInt());
                    }
                    if (jsonobject1.has("progress") && statbase.func_150954_l() != null) {
                        try {
                            Constructor<? extends IJsonSerializable> constructor = statbase.func_150954_l().getConstructor(new Class[0]);
                            IJsonSerializable ijsonserializable = constructor.newInstance(new Object[0]);
                            ijsonserializable.fromJson(jsonobject1.get("progress"));
                            tupleintjsonserializable.setJsonSerializableValue(ijsonserializable);
                        }
                        catch (Throwable throwable) {
                            logger.warn("Invalid statistic progress in " + this.statsFile, throwable);
                        }
                    }
                }
                map.put(statbase, tupleintjsonserializable);
                continue;
            }
            logger.warn("Invalid statistic in " + this.statsFile + ": Don't know what " + entry.getKey() + " is");
        }
        return map;
    }

    public static String dumpJson(Map<StatBase, TupleIntJsonSerializable> p_150880_0_) {
        JsonObject jsonobject = new JsonObject();
        Iterator<Map.Entry<StatBase, TupleIntJsonSerializable>> iterator = p_150880_0_.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<StatBase, TupleIntJsonSerializable> entry = iterator.next();
            if (entry.getValue().getJsonSerializableValue() != null) {
                JsonObject jsonobject1 = new JsonObject();
                jsonobject1.addProperty("value", entry.getValue().getIntegerValue());
                try {
                    jsonobject1.add("progress", entry.getValue().getJsonSerializableValue().getSerializableElement());
                }
                catch (Throwable throwable) {
                    logger.warn("Couldn't save statistic " + entry.getKey().getStatName() + ": error serializing progress", throwable);
                }
                jsonobject.add(entry.getKey().statId, jsonobject1);
                continue;
            }
            jsonobject.addProperty(entry.getKey().statId, entry.getValue().getIntegerValue());
        }
        return jsonobject.toString();
    }

    public void func_150877_d() {
        Iterator iterator = this.statsData.keySet().iterator();
        while (iterator.hasNext()) {
            StatBase statbase = (StatBase)iterator.next();
            this.field_150888_e.add(statbase);
        }
    }

    public void func_150876_a(EntityPlayerMP p_150876_1_) {
        int i = this.mcServer.getTickCounter();
        HashMap<StatBase, Integer> map = Maps.newHashMap();
        if (this.field_150886_g || i - this.field_150885_f > 300) {
            this.field_150885_f = i;
            for (StatBase statbase : this.func_150878_c()) {
                map.put(statbase, this.readStat(statbase));
            }
        }
        p_150876_1_.playerNetServerHandler.sendPacket(new S37PacketStatistics(map));
    }

    public void sendAchievements(EntityPlayerMP player) {
        HashMap<StatBase, Integer> map = Maps.newHashMap();
        Iterator<Achievement> iterator = AchievementList.achievementList.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                player.playerNetServerHandler.sendPacket(new S37PacketStatistics(map));
                return;
            }
            Achievement achievement = iterator.next();
            if (!this.hasAchievementUnlocked(achievement)) continue;
            map.put(achievement, this.readStat(achievement));
            this.field_150888_e.remove(achievement);
        }
    }

    public boolean func_150879_e() {
        return this.field_150886_g;
    }
}

