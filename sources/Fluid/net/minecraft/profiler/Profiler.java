// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.profiler;

import java.util.Iterator;
import java.util.Collections;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.src.Config;
import net.optifine.Lagometer;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import java.util.Map;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class Profiler
{
    private static final Logger logger;
    private final List<String> sectionList;
    private final List<Long> timestampList;
    public boolean profilingEnabled;
    private String profilingSection;
    private final Map<String, Long> profilingMap;
    public boolean profilerGlobalEnabled;
    private boolean profilerLocalEnabled;
    private static final String SCHEDULED_EXECUTABLES = "scheduledExecutables";
    private static final String TICK = "tick";
    private static final String PRE_RENDER_ERRORS = "preRenderErrors";
    private static final String RENDER = "render";
    private static final String DISPLAY = "display";
    private static final int HASH_SCHEDULED_EXECUTABLES;
    private static final int HASH_TICK;
    private static final int HASH_PRE_RENDER_ERRORS;
    private static final int HASH_RENDER;
    private static final int HASH_DISPLAY;
    
    static {
        logger = LogManager.getLogger();
        HASH_SCHEDULED_EXECUTABLES = "scheduledExecutables".hashCode();
        HASH_TICK = "tick".hashCode();
        HASH_PRE_RENDER_ERRORS = "preRenderErrors".hashCode();
        HASH_RENDER = "render".hashCode();
        HASH_DISPLAY = "display".hashCode();
    }
    
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
            final int i = name.hashCode();
            if (i == Profiler.HASH_SCHEDULED_EXECUTABLES && name.equals("scheduledExecutables")) {
                Lagometer.timerScheduledExecutables.start();
            }
            else if (i == Profiler.HASH_TICK && name.equals("tick") && Config.isMinecraftThread()) {
                Lagometer.timerScheduledExecutables.end();
                Lagometer.timerTick.start();
            }
            else if (i == Profiler.HASH_PRE_RENDER_ERRORS && name.equals("preRenderErrors")) {
                Lagometer.timerTick.end();
            }
        }
        if (Config.isFastRender()) {
            final int j = name.hashCode();
            if (j == Profiler.HASH_RENDER && name.equals("render")) {
                GlStateManager.clearEnabled = false;
            }
            else if (j == Profiler.HASH_DISPLAY && name.equals("display")) {
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
            final long i = System.nanoTime();
            final long j = this.timestampList.remove(this.timestampList.size() - 1);
            this.sectionList.remove(this.sectionList.size() - 1);
            final long k = i - j;
            if (this.profilingMap.containsKey(this.profilingSection)) {
                this.profilingMap.put(this.profilingSection, this.profilingMap.get(this.profilingSection) + k);
            }
            else {
                this.profilingMap.put(this.profilingSection, k);
            }
            if (k > 100000000L) {
                Profiler.logger.warn("Something's taking too long! '" + this.profilingSection + "' took aprox " + k / 1000000.0 + " ms");
            }
            this.profilingSection = (this.sectionList.isEmpty() ? "" : this.sectionList.get(this.sectionList.size() - 1));
        }
    }
    
    public List<Result> getProfilingData(String profilerName) {
        if (!this.profilingEnabled) {
            return null;
        }
        long i = this.profilingMap.containsKey("root") ? this.profilingMap.get("root") : 0L;
        final long j = this.profilingMap.containsKey(profilerName) ? this.profilingMap.get(profilerName) : -1L;
        final List<Result> list = Lists.newArrayList();
        if (profilerName.length() > 0) {
            profilerName = String.valueOf(profilerName) + ".";
        }
        long k = 0L;
        for (final String s : this.profilingMap.keySet()) {
            if (s.length() > profilerName.length() && s.startsWith(profilerName) && s.indexOf(".", profilerName.length() + 1) < 0) {
                k += this.profilingMap.get(s);
            }
        }
        final float f = (float)k;
        if (k < j) {
            k = j;
        }
        if (i < k) {
            i = k;
        }
        for (final String s2 : this.profilingMap.keySet()) {
            if (s2.length() > profilerName.length() && s2.startsWith(profilerName) && s2.indexOf(".", profilerName.length() + 1) < 0) {
                final long l = this.profilingMap.get(s2);
                final double d0 = l * 100.0 / k;
                final double d2 = l * 100.0 / i;
                final String s3 = s2.substring(profilerName.length());
                list.add(new Result(s3, d0, d2));
            }
        }
        for (final String s4 : this.profilingMap.keySet()) {
            this.profilingMap.put(s4, this.profilingMap.get(s4) * 950L / 1000L);
        }
        if (k > f) {
            list.add(new Result("unspecified", (k - f) * 100.0 / k, (k - f) * 100.0 / i));
        }
        Collections.sort(list);
        list.add(0, new Result(profilerName, 100.0, k * 100.0 / i));
        return list;
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
    
    public void startSection(final Class<?> p_startSection_1_) {
        if (this.profilingEnabled) {
            this.startSection(p_startSection_1_.getSimpleName());
        }
    }
    
    public static final class Result implements Comparable<Result>
    {
        public double field_76332_a;
        public double field_76330_b;
        public String field_76331_c;
        
        public Result(final String profilerName, final double usePercentage, final double totalUsePercentage) {
            this.field_76331_c = profilerName;
            this.field_76332_a = usePercentage;
            this.field_76330_b = totalUsePercentage;
        }
        
        @Override
        public int compareTo(final Result p_compareTo_1_) {
            return (p_compareTo_1_.field_76332_a < this.field_76332_a) ? -1 : ((p_compareTo_1_.field_76332_a > this.field_76332_a) ? 1 : p_compareTo_1_.field_76331_c.compareTo(this.field_76331_c));
        }
        
        public int getColor() {
            return (this.field_76331_c.hashCode() & 0xAAAAAA) + 4473924;
        }
    }
}
