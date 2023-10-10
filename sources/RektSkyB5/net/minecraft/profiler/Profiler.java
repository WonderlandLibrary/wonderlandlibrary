/*
 * Decompiled with CFR 0.152.
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
import optfine.Config;
import optfine.Lagometer;
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
        if (Lagometer.isActive()) {
            int i2 = name.hashCode();
            if (i2 == HASH_SCHEDULED_EXECUTABLES && name.equals(SCHEDULED_EXECUTABLES)) {
                Lagometer.timerScheduledExecutables.start();
            } else if (i2 == HASH_TICK && name.equals(TICK) && Config.isMinecraftThread()) {
                Lagometer.timerScheduledExecutables.end();
                Lagometer.timerTick.start();
            } else if (i2 == HASH_PRE_RENDER_ERRORS && name.equals(PRE_RENDER_ERRORS)) {
                Lagometer.timerTick.end();
            }
        }
        if (Config.isFastRender()) {
            int j2 = name.hashCode();
            if (j2 == HASH_RENDER && name.equals(RENDER)) {
                GlStateManager.clearEnabled = false;
            } else if (j2 == HASH_DISPLAY && name.equals(DISPLAY)) {
                GlStateManager.clearEnabled = true;
            }
        }
        if (this.profilerLocalEnabled && this.profilingEnabled) {
            if (this.profilingSection.length() > 0) {
                this.profilingSection = this.profilingSection + ".";
            }
            this.profilingSection = this.profilingSection + name;
            this.sectionList.add(this.profilingSection);
            this.timestampList.add(System.nanoTime());
        }
    }

    public void endSection() {
        if (this.profilerLocalEnabled && this.profilingEnabled) {
            long i2 = System.nanoTime();
            long j2 = (Long)this.timestampList.remove(this.timestampList.size() - 1);
            this.sectionList.remove(this.sectionList.size() - 1);
            long k2 = i2 - j2;
            if (this.profilingMap.containsKey(this.profilingSection)) {
                this.profilingMap.put(this.profilingSection, (Long)this.profilingMap.get(this.profilingSection) + k2);
            } else {
                this.profilingMap.put(this.profilingSection, k2);
            }
            if (k2 > 100000000L) {
                logger.warn("Something's taking too long! '" + this.profilingSection + "' took aprox " + (double)k2 / 1000000.0 + " ms");
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
        long i2 = this.profilingMap.containsKey("root") ? (Long)this.profilingMap.get("root") : 0L;
        long j2 = this.profilingMap.containsKey(p_76321_1_) ? (Long)this.profilingMap.get(p_76321_1_) : -1L;
        ArrayList<Result> arraylist = Lists.newArrayList();
        if (p_76321_1_.length() > 0) {
            p_76321_1_ = p_76321_1_ + ".";
        }
        long k2 = 0L;
        for (Object s2 : this.profilingMap.keySet()) {
            if (((String)s2).length() <= p_76321_1_.length() || !((String)s2).startsWith(p_76321_1_) || ((String)s2).indexOf(".", p_76321_1_.length() + 1) >= 0) continue;
            k2 += ((Long)this.profilingMap.get(s2)).longValue();
        }
        float f2 = k2;
        if (k2 < j2) {
            k2 = j2;
        }
        if (i2 < k2) {
            i2 = k2;
        }
        for (Object s10 : this.profilingMap.keySet()) {
            String s1 = (String)s10;
            if (s1.length() <= p_76321_1_.length() || !s1.startsWith(p_76321_1_) || s1.indexOf(".", p_76321_1_.length() + 1) >= 0) continue;
            long l2 = (Long)this.profilingMap.get(s1);
            double d0 = (double)l2 * 100.0 / (double)k2;
            double d1 = (double)l2 * 100.0 / (double)i2;
            String s2 = s1.substring(p_76321_1_.length());
            arraylist.add(new Result(s2, d0, d1));
        }
        for (Object s3 : this.profilingMap.keySet()) {
            this.profilingMap.put(s3, (Long)this.profilingMap.get(s3) * 950L / 1000L);
        }
        if ((float)k2 > f2) {
            arraylist.add(new Result("unspecified", (double)((float)k2 - f2) * 100.0 / (double)k2, (double)((float)k2 - f2) * 100.0 / (double)i2));
        }
        Collections.sort(arraylist);
        arraylist.add(0, new Result(p_76321_1_, 100.0, (double)k2 * 100.0 / (double)i2));
        return arraylist;
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
        private static final String __OBFID = "CL_00001498";

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

