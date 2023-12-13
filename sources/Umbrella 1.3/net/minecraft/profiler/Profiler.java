/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.profiler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.minecraft.client.renderer.GlStateManager;
import optifine.Config;
import optifine.Lagometer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Profiler {
    private static final Logger logger = LogManager.getLogger();
    private final List sectionList = Lists.newArrayList();
    private final List timestampList = Lists.newArrayList();
    public boolean profilingEnabled;
    private String profilingSection = "";
    private final Map profilingMap = Maps.newHashMap();
    private static final String __OBFID = "CL_00001497";
    public boolean profilerGlobalEnabled;
    private boolean profilerLocalEnabled = this.profilerGlobalEnabled = true;
    private static final String SCHEDULED_EXECUTABLES = "scheduledExecutables";
    private static final String TICK = "tick";
    private static final String PRE_RENDER_ERRORS = "preRenderErrors";
    private static final String RENDER = "render";
    private static final String DISPLAY = "display";
    private static final int HASH_SCHEDULED_EXECUTABLES = "scheduledExecutables".hashCode();
    private static final int HASH_TICK = "tick".hashCode();
    private static final int HASH_PRE_RENDER_ERRORS = "preRenderErrors".hashCode();
    private static final int HASH_RENDER = "render".hashCode();
    private static final int HASH_DISPLAY = "display".hashCode();

    public void clearProfiling() {
        this.profilingMap.clear();
        this.profilingSection = "";
        this.sectionList.clear();
        this.profilerLocalEnabled = this.profilerGlobalEnabled;
    }

    public void startSection(String name) {
        int hashName;
        if (Lagometer.isActive()) {
            hashName = name.hashCode();
            if (hashName == HASH_SCHEDULED_EXECUTABLES && name.equals(SCHEDULED_EXECUTABLES)) {
                Lagometer.timerScheduledExecutables.start();
            } else if (hashName == HASH_TICK && name.equals(TICK) && Config.isMinecraftThread()) {
                Lagometer.timerScheduledExecutables.end();
                Lagometer.timerTick.start();
            } else if (hashName == HASH_PRE_RENDER_ERRORS && name.equals(PRE_RENDER_ERRORS)) {
                Lagometer.timerTick.end();
            }
        }
        if (Config.isFastRender()) {
            hashName = name.hashCode();
            if (hashName == HASH_RENDER && name.equals(RENDER)) {
                GlStateManager.clearEnabled = false;
            } else if (hashName == HASH_DISPLAY && name.equals(DISPLAY)) {
                GlStateManager.clearEnabled = true;
            }
        }
        if (this.profilerLocalEnabled && this.profilingEnabled) {
            if (this.profilingSection.length() > 0) {
                this.profilingSection = String.valueOf(this.profilingSection) + ".";
            }
            this.profilingSection = String.valueOf(this.profilingSection) + name;
            this.sectionList.add(this.profilingSection);
            this.timestampList.add(System.nanoTime());
        }
    }

    public void endSection() {
        if (this.profilerLocalEnabled && this.profilingEnabled) {
            long var1 = System.nanoTime();
            long var3 = (Long)this.timestampList.remove(this.timestampList.size() - 1);
            this.sectionList.remove(this.sectionList.size() - 1);
            long var5 = var1 - var3;
            if (this.profilingMap.containsKey(this.profilingSection)) {
                this.profilingMap.put(this.profilingSection, (Long)this.profilingMap.get(this.profilingSection) + var5);
            } else {
                this.profilingMap.put(this.profilingSection, var5);
            }
            if (var5 > 100000000L) {
                logger.warn("Something's taking too long! '" + this.profilingSection + "' took aprox " + (double)var5 / 1000000.0 + " ms");
            }
            this.profilingSection = !this.sectionList.isEmpty() ? (String)this.sectionList.get(this.sectionList.size() - 1) : "";
        }
    }

    public List getProfilingData(String p_76321_1_) {
        this.profilerLocalEnabled = this.profilerGlobalEnabled;
        if (!this.profilerLocalEnabled) {
            return new ArrayList<Result>(Arrays.asList(new Result("root", 0.0, 0.0)));
        }
        if (!this.profilingEnabled) {
            return null;
        }
        long var3 = this.profilingMap.containsKey("root") ? (Long)this.profilingMap.get("root") : 0L;
        long var5 = this.profilingMap.containsKey(p_76321_1_) ? (Long)this.profilingMap.get(p_76321_1_) : -1L;
        ArrayList var7 = Lists.newArrayList();
        if (p_76321_1_.length() > 0) {
            p_76321_1_ = String.valueOf(p_76321_1_) + ".";
        }
        long var8 = 0L;
        for (String var20 : this.profilingMap.keySet()) {
            if (var20.length() <= p_76321_1_.length() || !var20.startsWith(p_76321_1_) || var20.indexOf(".", p_76321_1_.length() + 1) >= 0) continue;
            var8 += ((Long)this.profilingMap.get(var20)).longValue();
        }
        float var201 = var8;
        if (var8 < var5) {
            var8 = var5;
        }
        if (var3 < var8) {
            var3 = var8;
        }
        for (String var12 : this.profilingMap.keySet()) {
            if (var12.length() <= p_76321_1_.length() || !var12.startsWith(p_76321_1_) || var12.indexOf(".", p_76321_1_.length() + 1) >= 0) continue;
            long var13 = (Long)this.profilingMap.get(var12);
            double var15 = (double)var13 * 100.0 / (double)var8;
            double var17 = (double)var13 * 100.0 / (double)var3;
            String var19 = var12.substring(p_76321_1_.length());
            var7.add(new Result(var19, var15, var17));
        }
        for (String var12 : this.profilingMap.keySet()) {
            this.profilingMap.put(var12, (Long)this.profilingMap.get(var12) * 950L / 1000L);
        }
        if ((float)var8 > var201) {
            var7.add(new Result("unspecified", (double)((float)var8 - var201) * 100.0 / (double)var8, (double)((float)var8 - var201) * 100.0 / (double)var3));
        }
        Collections.sort(var7);
        var7.add(0, new Result(p_76321_1_, 100.0, (double)var8 * 100.0 / (double)var3));
        return var7;
    }

    public void endStartSection(String name) {
        if (this.profilerLocalEnabled) {
            this.endSection();
            this.startSection(name);
        }
    }

    public String getNameOfLastSection() {
        return this.sectionList.size() == 0 ? "[UNKNOWN]" : (String)this.sectionList.get(this.sectionList.size() - 1);
    }

    public static final class Result
    implements Comparable {
        public double field_76332_a;
        public double field_76330_b;
        public String field_76331_c;

        public Result(String p_i1554_1_, double p_i1554_2_, double p_i1554_4_) {
            this.field_76331_c = p_i1554_1_;
            this.field_76332_a = p_i1554_2_;
            this.field_76330_b = p_i1554_4_;
        }

        public int compareTo(Result p_compareTo_1_) {
            return p_compareTo_1_.field_76332_a < this.field_76332_a ? -1 : (p_compareTo_1_.field_76332_a > this.field_76332_a ? 1 : p_compareTo_1_.field_76331_c.compareTo(this.field_76331_c));
        }

        public int func_76329_a() {
            return (this.field_76331_c.hashCode() & 0xAAAAAA) + 0x444444;
        }

        public int compareTo(Object p_compareTo_1_) {
            return this.compareTo((Result)p_compareTo_1_);
        }
    }
}

