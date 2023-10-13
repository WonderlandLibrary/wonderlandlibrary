/*
 * Decompiled with CFR 0.150.
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
    private final MinecraftServer field_150890_c;
    private final File field_150887_d;
    private final Set field_150888_e = Sets.newHashSet();
    private int field_150885_f = -300;
    private boolean field_150886_g = false;
    private static final String __OBFID = "CL_00001471";

    public StatisticsFile(MinecraftServer p_i45306_1_, File p_i45306_2_) {
        this.field_150890_c = p_i45306_1_;
        this.field_150887_d = p_i45306_2_;
    }

    public void func_150882_a() {
        if (this.field_150887_d.isFile()) {
            try {
                this.field_150875_a.clear();
                this.field_150875_a.putAll(this.func_150881_a(FileUtils.readFileToString((File)this.field_150887_d)));
            }
            catch (IOException var2) {
                logger.error("Couldn't read statistics file " + this.field_150887_d, (Throwable)var2);
            }
            catch (JsonParseException var3) {
                logger.error("Couldn't parse statistics file " + this.field_150887_d, (Throwable)var3);
            }
        }
    }

    public void func_150883_b() {
        try {
            FileUtils.writeStringToFile((File)this.field_150887_d, (String)StatisticsFile.func_150880_a(this.field_150875_a));
        }
        catch (IOException var2) {
            logger.error("Couldn't save stats", (Throwable)var2);
        }
    }

    @Override
    public void func_150873_a(EntityPlayer p_150873_1_, StatBase p_150873_2_, int p_150873_3_) {
        int var4 = p_150873_2_.isAchievement() ? this.writeStat(p_150873_2_) : 0;
        super.func_150873_a(p_150873_1_, p_150873_2_, p_150873_3_);
        this.field_150888_e.add(p_150873_2_);
        if (p_150873_2_.isAchievement() && var4 == 0 && p_150873_3_ > 0) {
            this.field_150886_g = true;
            if (this.field_150890_c.isAnnouncingPlayerAchievements()) {
                this.field_150890_c.getConfigurationManager().sendChatMsg(new ChatComponentTranslation("chat.type.achievement", p_150873_1_.getDisplayName(), p_150873_2_.func_150955_j()));
            }
        }
        if (p_150873_2_.isAchievement() && var4 > 0 && p_150873_3_ == 0) {
            this.field_150886_g = true;
            if (this.field_150890_c.isAnnouncingPlayerAchievements()) {
                this.field_150890_c.getConfigurationManager().sendChatMsg(new ChatComponentTranslation("chat.type.achievement.taken", p_150873_1_.getDisplayName(), p_150873_2_.func_150955_j()));
            }
        }
    }

    public Set func_150878_c() {
        HashSet var1 = Sets.newHashSet((Iterable)this.field_150888_e);
        this.field_150888_e.clear();
        this.field_150886_g = false;
        return var1;
    }

    public Map func_150881_a(String p_150881_1_) {
        JsonElement var2 = new JsonParser().parse(p_150881_1_);
        if (!var2.isJsonObject()) {
            return Maps.newHashMap();
        }
        JsonObject var3 = var2.getAsJsonObject();
        HashMap var4 = Maps.newHashMap();
        for (Map.Entry var6 : var3.entrySet()) {
            StatBase var7 = StatList.getOneShotStat((String)var6.getKey());
            if (var7 != null) {
                TupleIntJsonSerializable var8 = new TupleIntJsonSerializable();
                if (((JsonElement)var6.getValue()).isJsonPrimitive() && ((JsonElement)var6.getValue()).getAsJsonPrimitive().isNumber()) {
                    var8.setIntegerValue(((JsonElement)var6.getValue()).getAsInt());
                } else if (((JsonElement)var6.getValue()).isJsonObject()) {
                    JsonObject var9 = ((JsonElement)var6.getValue()).getAsJsonObject();
                    if (var9.has("value") && var9.get("value").isJsonPrimitive() && var9.get("value").getAsJsonPrimitive().isNumber()) {
                        var8.setIntegerValue(var9.getAsJsonPrimitive("value").getAsInt());
                    }
                    if (var9.has("progress") && var7.func_150954_l() != null) {
                        try {
                            Constructor var10 = var7.func_150954_l().getConstructor(new Class[0]);
                            IJsonSerializable var11 = (IJsonSerializable)var10.newInstance(new Object[0]);
                            var11.func_152753_a(var9.get("progress"));
                            var8.setJsonSerializableValue(var11);
                        }
                        catch (Throwable var12) {
                            logger.warn("Invalid statistic progress in " + this.field_150887_d, var12);
                        }
                    }
                }
                var4.put(var7, var8);
                continue;
            }
            logger.warn("Invalid statistic in " + this.field_150887_d + ": Don't know what " + (String)var6.getKey() + " is");
        }
        return var4;
    }

    public static String func_150880_a(Map p_150880_0_) {
        JsonObject var1 = new JsonObject();
        for (Map.Entry var3 : p_150880_0_.entrySet()) {
            if (((TupleIntJsonSerializable)var3.getValue()).getJsonSerializableValue() != null) {
                JsonObject var4 = new JsonObject();
                var4.addProperty("value", (Number)((TupleIntJsonSerializable)var3.getValue()).getIntegerValue());
                try {
                    var4.add("progress", ((TupleIntJsonSerializable)var3.getValue()).getJsonSerializableValue().getSerializableElement());
                }
                catch (Throwable var6) {
                    logger.warn("Couldn't save statistic " + ((StatBase)var3.getKey()).getStatName() + ": error serializing progress", var6);
                }
                var1.add(((StatBase)var3.getKey()).statId, (JsonElement)var4);
                continue;
            }
            var1.addProperty(((StatBase)var3.getKey()).statId, (Number)((TupleIntJsonSerializable)var3.getValue()).getIntegerValue());
        }
        return var1.toString();
    }

    public void func_150877_d() {
        for (StatBase var2 : this.field_150875_a.keySet()) {
            this.field_150888_e.add(var2);
        }
    }

    public void func_150876_a(EntityPlayerMP p_150876_1_) {
        int var2 = this.field_150890_c.getTickCounter();
        HashMap var3 = Maps.newHashMap();
        if (this.field_150886_g || var2 - this.field_150885_f > 300) {
            this.field_150885_f = var2;
            for (StatBase var5 : this.func_150878_c()) {
                var3.put(var5, this.writeStat(var5));
            }
        }
        p_150876_1_.playerNetServerHandler.sendPacket(new S37PacketStatistics(var3));
    }

    public void func_150884_b(EntityPlayerMP p_150884_1_) {
        HashMap var2 = Maps.newHashMap();
        for (Achievement var4 : AchievementList.achievementList) {
            if (!this.hasAchievementUnlocked(var4)) continue;
            var2.put(var4, this.writeStat(var4));
            this.field_150888_e.remove(var4);
        }
        p_150884_1_.playerNetServerHandler.sendPacket(new S37PacketStatistics(var2));
    }

    public boolean func_150879_e() {
        return this.field_150886_g;
    }
}

