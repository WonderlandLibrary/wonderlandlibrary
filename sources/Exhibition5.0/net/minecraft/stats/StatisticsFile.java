// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.stats;

import org.apache.logging.log4j.LogManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S37PacketStatistics;
import net.minecraft.entity.player.EntityPlayerMP;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.HashMap;
import com.google.gson.JsonObject;
import net.minecraft.util.IJsonSerializable;
import com.google.gson.JsonElement;
import net.minecraft.util.TupleIntJsonSerializable;
import com.google.common.collect.Maps;
import com.google.gson.JsonParser;
import java.util.Map;
import java.util.HashSet;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.entity.player.EntityPlayer;
import com.google.gson.JsonParseException;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import com.google.common.collect.Sets;
import java.util.Set;
import java.io.File;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;

public class StatisticsFile extends StatFileWriter
{
    private static final Logger logger;
    private final MinecraftServer field_150890_c;
    private final File field_150887_d;
    private final Set field_150888_e;
    private int field_150885_f;
    private boolean field_150886_g;
    private static final String __OBFID = "CL_00001471";
    
    public StatisticsFile(final MinecraftServer p_i45306_1_, final File p_i45306_2_) {
        this.field_150888_e = Sets.newHashSet();
        this.field_150885_f = -300;
        this.field_150886_g = false;
        this.field_150890_c = p_i45306_1_;
        this.field_150887_d = p_i45306_2_;
    }
    
    public void func_150882_a() {
        if (this.field_150887_d.isFile()) {
            try {
                this.field_150875_a.clear();
                this.field_150875_a.putAll(this.func_150881_a(FileUtils.readFileToString(this.field_150887_d)));
            }
            catch (IOException var2) {
                StatisticsFile.logger.error("Couldn't read statistics file " + this.field_150887_d, (Throwable)var2);
            }
            catch (JsonParseException var3) {
                StatisticsFile.logger.error("Couldn't parse statistics file " + this.field_150887_d, (Throwable)var3);
            }
        }
    }
    
    public void func_150883_b() {
        try {
            FileUtils.writeStringToFile(this.field_150887_d, func_150880_a(this.field_150875_a));
        }
        catch (IOException var2) {
            StatisticsFile.logger.error("Couldn't save stats", (Throwable)var2);
        }
    }
    
    @Override
    public void func_150873_a(final EntityPlayer p_150873_1_, final StatBase p_150873_2_, final int p_150873_3_) {
        final int var4 = p_150873_2_.isAchievement() ? this.writeStat(p_150873_2_) : 0;
        super.func_150873_a(p_150873_1_, p_150873_2_, p_150873_3_);
        this.field_150888_e.add(p_150873_2_);
        if (p_150873_2_.isAchievement() && var4 == 0 && p_150873_3_ > 0) {
            this.field_150886_g = true;
            if (this.field_150890_c.isAnnouncingPlayerAchievements()) {
                this.field_150890_c.getConfigurationManager().sendChatMsg(new ChatComponentTranslation("chat.type.achievement", new Object[] { p_150873_1_.getDisplayName(), p_150873_2_.func_150955_j() }));
            }
        }
        if (p_150873_2_.isAchievement() && var4 > 0 && p_150873_3_ == 0) {
            this.field_150886_g = true;
            if (this.field_150890_c.isAnnouncingPlayerAchievements()) {
                this.field_150890_c.getConfigurationManager().sendChatMsg(new ChatComponentTranslation("chat.type.achievement.taken", new Object[] { p_150873_1_.getDisplayName(), p_150873_2_.func_150955_j() }));
            }
        }
    }
    
    public Set func_150878_c() {
        final HashSet var1 = Sets.newHashSet((Iterable)this.field_150888_e);
        this.field_150888_e.clear();
        this.field_150886_g = false;
        return var1;
    }
    
    public Map func_150881_a(final String p_150881_1_) {
        final JsonElement var2 = new JsonParser().parse(p_150881_1_);
        if (!var2.isJsonObject()) {
            return Maps.newHashMap();
        }
        final JsonObject var3 = var2.getAsJsonObject();
        final HashMap var4 = Maps.newHashMap();
        for (final Map.Entry var6 : var3.entrySet()) {
            final StatBase var7 = StatList.getOneShotStat(var6.getKey());
            if (var7 != null) {
                final TupleIntJsonSerializable var8 = new TupleIntJsonSerializable();
                if (var6.getValue().isJsonPrimitive() && var6.getValue().getAsJsonPrimitive().isNumber()) {
                    var8.setIntegerValue(var6.getValue().getAsInt());
                }
                else if (var6.getValue().isJsonObject()) {
                    final JsonObject var9 = var6.getValue().getAsJsonObject();
                    if (var9.has("value") && var9.get("value").isJsonPrimitive() && var9.get("value").getAsJsonPrimitive().isNumber()) {
                        var8.setIntegerValue(var9.getAsJsonPrimitive("value").getAsInt());
                    }
                    if (var9.has("progress") && var7.func_150954_l() != null) {
                        try {
                            final Constructor var10 = var7.func_150954_l().getConstructor((Class[])new Class[0]);
                            final IJsonSerializable var11 = var10.newInstance(new Object[0]);
                            var11.func_152753_a(var9.get("progress"));
                            var8.setJsonSerializableValue(var11);
                        }
                        catch (Throwable var12) {
                            StatisticsFile.logger.warn("Invalid statistic progress in " + this.field_150887_d, var12);
                        }
                    }
                }
                var4.put(var7, var8);
            }
            else {
                StatisticsFile.logger.warn("Invalid statistic in " + this.field_150887_d + ": Don't know what " + var6.getKey() + " is");
            }
        }
        return var4;
    }
    
    public static String func_150880_a(final Map p_150880_0_) {
        final JsonObject var1 = new JsonObject();
        for (final Map.Entry var3 : p_150880_0_.entrySet()) {
            if (var3.getValue().getJsonSerializableValue() != null) {
                final JsonObject var4 = new JsonObject();
                var4.addProperty("value", (Number)var3.getValue().getIntegerValue());
                try {
                    var4.add("progress", var3.getValue().getJsonSerializableValue().getSerializableElement());
                }
                catch (Throwable var5) {
                    StatisticsFile.logger.warn("Couldn't save statistic " + var3.getKey().getStatName() + ": error serializing progress", var5);
                }
                var1.add(var3.getKey().statId, (JsonElement)var4);
            }
            else {
                var1.addProperty(var3.getKey().statId, (Number)var3.getValue().getIntegerValue());
            }
        }
        return var1.toString();
    }
    
    public void func_150877_d() {
        for (final StatBase var2 : this.field_150875_a.keySet()) {
            this.field_150888_e.add(var2);
        }
    }
    
    public void func_150876_a(final EntityPlayerMP p_150876_1_) {
        final int var2 = this.field_150890_c.getTickCounter();
        final HashMap var3 = Maps.newHashMap();
        if (this.field_150886_g || var2 - this.field_150885_f > 300) {
            this.field_150885_f = var2;
            for (final StatBase var5 : this.func_150878_c()) {
                var3.put(var5, this.writeStat(var5));
            }
        }
        p_150876_1_.playerNetServerHandler.sendPacket(new S37PacketStatistics(var3));
    }
    
    public void func_150884_b(final EntityPlayerMP p_150884_1_) {
        final HashMap var2 = Maps.newHashMap();
        for (final Achievement var4 : AchievementList.achievementList) {
            if (this.hasAchievementUnlocked(var4)) {
                var2.put(var4, this.writeStat(var4));
                this.field_150888_e.remove(var4);
            }
        }
        p_150884_1_.playerNetServerHandler.sendPacket(new S37PacketStatistics(var2));
    }
    
    public boolean func_150879_e() {
        return this.field_150886_g;
    }
    
    static {
        logger = LogManager.getLogger();
    }
}
