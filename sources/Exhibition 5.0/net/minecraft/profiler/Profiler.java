// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.profiler;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.optifine.Config;
import net.minecraft.optifine.Lagometer;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import java.util.Map;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class Profiler
{
    private static final Logger logger;
    private final List sectionList;
    private final List timestampList;
    public boolean profilingEnabled;
    private String profilingSection;
    private final Map profilingMap;
    private static final String __OBFID = "CL_00001497";
    public boolean profilerGlobalEnabled;
    private boolean profilerLocalEnabled;
    private static final String SCHEDULED_EXECUTABLES = "scheduledExecutables";
    private static final String TICK = "tick";
    private static final String PRE_RENDER_ERRORS = "preRenderErrors";
    private static final int HASH_SCHEDULED_EXECUTABLES;
    private static final int HASH_TICK;
    private static final int HASH_PRE_RENDER_ERRORS;
    
    public Profiler() {
        this.sectionList = Lists.newArrayList();
        this.timestampList = Lists.newArrayList();
        this.profilingSection = "";
        this.profilingMap = Maps.newHashMap();
        this.profilerGlobalEnabled = true;
        this.profilerLocalEnabled = this.profilerGlobalEnabled;
    }
    
    public void clearProfiling() {
        this.profilingMap.clear();
        this.profilingSection = "";
        this.sectionList.clear();
        this.profilerLocalEnabled = this.profilerGlobalEnabled;
    }
    
    public void startSection(final String name) {
        if (Lagometer.isActive()) {
            final int hashName = name.hashCode();
            if (hashName == Profiler.HASH_SCHEDULED_EXECUTABLES && name.equals("scheduledExecutables")) {
                Lagometer.timerScheduledExecutables.start();
            }
            else if (hashName == Profiler.HASH_TICK && name.equals("tick") && Config.isMinecraftThread()) {
                Lagometer.timerScheduledExecutables.end();
                Lagometer.timerTick.start();
            }
            else if (hashName == Profiler.HASH_PRE_RENDER_ERRORS && name.equals("preRenderErrors")) {
                Lagometer.timerTick.end();
            }
        }
        if (this.profilerLocalEnabled && this.profilingEnabled) {
            if (this.profilingSection.length() > 0) {
                this.profilingSection += ".";
            }
            this.profilingSection += name;
            this.sectionList.add(this.profilingSection);
            this.timestampList.add(System.nanoTime());
        }
    }
    
    public void endSection() {
        if (this.profilerLocalEnabled && this.profilingEnabled) {
            final long var1 = System.nanoTime();
            final long var2 = this.timestampList.remove(this.timestampList.size() - 1);
            this.sectionList.remove(this.sectionList.size() - 1);
            final long var3 = var1 - var2;
            if (this.profilingMap.containsKey(this.profilingSection)) {
                this.profilingMap.put(this.profilingSection, this.profilingMap.get(this.profilingSection) + var3);
            }
            else {
                this.profilingMap.put(this.profilingSection, var3);
            }
            if (var3 > 100000000L) {
                Profiler.logger.warn("Something's taking too long! '" + this.profilingSection + "' took aprox " + var3 / 1000000.0 + " ms");
            }
            this.profilingSection = (this.sectionList.isEmpty() ? "" : this.sectionList.get(this.sectionList.size() - 1));
        }
    }
    
    public List getProfilingData(String p_76321_1_) {
        if (!(this.profilerLocalEnabled = this.profilerGlobalEnabled)) {
            return new ArrayList(Arrays.asList(new Result("root", 0.0, 0.0)));
        }
        if (!this.profilingEnabled) {
            return null;
        }
        long var3 = this.profilingMap.containsKey("root") ? this.profilingMap.get("root") : 0L;
        final long var4 = this.profilingMap.containsKey(p_76321_1_) ? this.profilingMap.get(p_76321_1_) : -1L;
        final ArrayList var5 = Lists.newArrayList();
        if (p_76321_1_.length() > 0) {
            p_76321_1_ += ".";
        }
        long var6 = 0L;
        for (final String var8 : this.profilingMap.keySet()) {
            if (var8.length() > p_76321_1_.length() && var8.startsWith(p_76321_1_) && var8.indexOf(".", p_76321_1_.length() + 1) < 0) {
                var6 += this.profilingMap.get(var8);
            }
        }
        final float var9 = var6;
        if (var6 < var4) {
            var6 = var4;
        }
        if (var3 < var6) {
            var3 = var6;
        }
        for (final String var11 : this.profilingMap.keySet()) {
            if (var11.length() > p_76321_1_.length() && var11.startsWith(p_76321_1_) && var11.indexOf(".", p_76321_1_.length() + 1) < 0) {
                final long var12 = this.profilingMap.get(var11);
                final double var13 = var12 * 100.0 / var6;
                final double var14 = var12 * 100.0 / var3;
                final String var15 = var11.substring(p_76321_1_.length());
                var5.add(new Result(var15, var13, var14));
            }
        }
        for (final String var11 : this.profilingMap.keySet()) {
            this.profilingMap.put(var11, this.profilingMap.get(var11) * 950L / 1000L);
        }
        if (var6 > var9) {
            var5.add(new Result("unspecified", (var6 - var9) * 100.0 / var6, (var6 - var9) * 100.0 / var3));
        }
        Collections.sort((List<Comparable>)var5);
        var5.add(0, new Result(p_76321_1_, 100.0, var6 * 100.0 / var3));
        return var5;
    }
    
    public void endStartSection(final String name) {
        if (this.profilerLocalEnabled) {
            this.endSection();
            this.startSection(name);
        }
    }
    
    public String getNameOfLastSection() {
        return (this.sectionList.size() == 0) ? "[UNKNOWN]" : this.sectionList.get(this.sectionList.size() - 1);
    }
    
    static {
        logger = LogManager.getLogger();
        HASH_SCHEDULED_EXECUTABLES = "scheduledExecutables".hashCode();
        HASH_TICK = "tick".hashCode();
        HASH_PRE_RENDER_ERRORS = "preRenderErrors".hashCode();
    }
    
    public static final class Result implements Comparable
    {
        public double field_76332_a;
        public double field_76330_b;
        public String field_76331_c;
        private static final String __OBFID = "CL_00001498";
        
        public Result(final String p_i1554_1_, final double p_i1554_2_, final double p_i1554_4_) {
            this.field_76331_c = p_i1554_1_;
            this.field_76332_a = p_i1554_2_;
            this.field_76330_b = p_i1554_4_;
        }
        
        public int compareTo(final Result p_compareTo_1_) {
            return (p_compareTo_1_.field_76332_a < this.field_76332_a) ? -1 : ((p_compareTo_1_.field_76332_a > this.field_76332_a) ? 1 : p_compareTo_1_.field_76331_c.compareTo(this.field_76331_c));
        }
        
        public int func_76329_a() {
            return (this.field_76331_c.hashCode() & 0xAAAAAA) + 4473924;
        }
        
        @Override
        public int compareTo(final Object p_compareTo_1_) {
            return this.compareTo((Result)p_compareTo_1_);
        }
    }
}
