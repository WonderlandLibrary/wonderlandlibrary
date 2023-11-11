package net.minecraft.profiler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.renderer.GlStateManager;
import optifine.Config;
import optifine.Lagometer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Profiler {
    private static final Logger logger = LogManager.getLogger();

    /**
     * List of parent sections
     */
    private final List<String> sectionList = Lists.newArrayList();

    /**
     * List of timestamps (System.nanoTime)
     */
    private final List<Long> timestampList = Lists.newArrayList();

    /**
     * Flag profiling enabled
     */
    public boolean profilingEnabled;

    /**
     * Current profiling section
     */
    private String profilingSection = "";

    /**
     * Profiling map
     */
    private final Map<String, Long> profilingMap = Maps.newHashMap();
    public boolean profilerGlobalEnabled = true;
    private boolean profilerLocalEnabled;
    private static final String SCHEDULED_EXECUTABLES = "scheduledExecutables";
    private static final String TICK = "tick";
    private static final String PRE_RENDER_ERRORS = "preRenderErrors";
    private static final String RENDER = "render";
    private static final String DISPLAY = "display";
    private static final int HASH_SCHEDULED_EXECUTABLES = SCHEDULED_EXECUTABLES.hashCode();
    private static final int HASH_TICK = TICK.hashCode();
    private static final int HASH_PRE_RENDER_ERRORS = PRE_RENDER_ERRORS.hashCode();
    private static final int HASH_RENDER = RENDER.hashCode();
    private static final int HASH_DISPLAY = DISPLAY.hashCode();

    public Profiler() {
        this.profilerLocalEnabled = true;
    }

    /**
     * Clear profiling.
     */
    public void clearProfiling() {
        this.profilingMap.clear();
        this.profilingSection = "";
        this.sectionList.clear();
        this.profilerLocalEnabled = this.profilerGlobalEnabled;
    }

    /**
     * Start section
     */
    public void startSection(String name) {
        if (Lagometer.isActive()) {
            int i = name.hashCode();

            if (i == HASH_SCHEDULED_EXECUTABLES && name.equals(SCHEDULED_EXECUTABLES)) {
                Lagometer.timerScheduledExecutables.start();
            } else if (i == HASH_TICK && name.equals(TICK) && Config.isMinecraftThread()) {
                Lagometer.timerScheduledExecutables.end();
                Lagometer.timerTick.start();
            } else if (i == HASH_PRE_RENDER_ERRORS && name.equals(PRE_RENDER_ERRORS)) {
                Lagometer.timerTick.end();
            }
        }

        if (Config.isFastRender()) {
            int j = name.hashCode();

            if (j == HASH_RENDER && name.equals(RENDER)) {
                GlStateManager.clearEnabled = false;
            } else if (j == HASH_DISPLAY && name.equals(DISPLAY)) {
                GlStateManager.clearEnabled = true;
            }
        }

        if (!profilerLocalEnabled || !profilingEnabled)
            return;

        if (profilingSection.length() > 0)
            profilingSection = profilingSection + ".";

        profilingSection = profilingSection + name;
        sectionList.add(profilingSection);
        timestampList.add(System.nanoTime());
    }

    /**
     * End section
     */
    public void endSection() {
        if (!profilerLocalEnabled || !profilingEnabled)
            return;

        long now = System.nanoTime();
        long timestamp = timestampList.remove(timestampList.size() - 1);
        sectionList.remove(sectionList.size() - 1);
        long time = now - timestamp;

        if (profilingMap.containsKey(profilingSection))
            profilingMap.put(profilingSection, profilingMap.get(profilingSection) + time);
        else
            profilingMap.put(profilingSection, time);

        if (time > 100000000L)
            logger.warn("Something's taking too long! '" + profilingSection + "' took aprox " + (double) time / 1000000.0D + " ms");

        profilingSection = !sectionList.isEmpty() ? sectionList.get(sectionList.size() - 1) : "";
    }

    /**
     * Get profiling data
     */
    public List<Profiler.Result> getProfilingData(String p_76321_1_) {
        profilerLocalEnabled = profilerGlobalEnabled;

        if (!profilerLocalEnabled)
            return new ArrayList<>(Collections.singletonList(new Result("root", 0.0D, 0.0D)));
        else if (!profilingEnabled)
            return null;
        else {
            long i = profilingMap.getOrDefault("root", 0L);
            long j = profilingMap.getOrDefault(p_76321_1_, -1L);
            ArrayList<Profiler.Result> arraylist = Lists.newArrayList();

            if (p_76321_1_.length() > 0) p_76321_1_ = p_76321_1_ + ".";

            long k = 0L;

            for (String s : profilingMap.keySet()) {
                if (s.length() > p_76321_1_.length() && s.startsWith(p_76321_1_) && s.indexOf(".", p_76321_1_.length() + 1) < 0)
                    k += profilingMap.get(s);
            }

            float f = (float) k;

            if (k < j) k = j;
            if (i < k) i = k;

            for (String s : this.profilingMap.keySet()) {
                if (s.length() > p_76321_1_.length() && s.startsWith(p_76321_1_) && s.indexOf(".", p_76321_1_.length() + 1) < 0) {
                    long l = this.profilingMap.get(s);
                    double d0 = (double) l * 100.0D / (double) k;
                    double d1 = (double) l * 100.0D / (double) i;
                    String s2 = s.substring(p_76321_1_.length());
                    arraylist.add(new Profiler.Result(s2, d0, d1));
                }
            }

            profilingMap.replaceAll((s, v) -> profilingMap.get(s) * 950L / 1000L);

            if ((float) k > f)
                arraylist.add(new Profiler.Result("unspecified", (double) ((float) k - f) * 100.0D / (double) k, (double) ((float) k - f) * 100.0D / (double) i));

            Collections.sort(arraylist);
            arraylist.add(0, new Profiler.Result(p_76321_1_, 100.0D, (double) k * 100.0D / (double) i));
            return arraylist;
        }
    }

    /**
     * End current section and start a new section
     */
    public void endStartSection(String name) {
        if (!profilerLocalEnabled)
            return;
        endSection();
        startSection(name);
    }

    public String getNameOfLastSection() {
        return sectionList.size() == 0 ? "[UNKNOWN]" : sectionList.get(sectionList.size() - 1);
    }

    public static final class Result implements Comparable<Profiler.Result> {
        public double field_76332_a;
        public double field_76330_b;
        public String field_76331_c;

        public Result(String p_i1554_1_, double p_i1554_2_, double p_i1554_4_) {
            this.field_76331_c = p_i1554_1_;
            this.field_76332_a = p_i1554_2_;
            this.field_76330_b = p_i1554_4_;
        }

        public int compareTo(Profiler.Result result) {
            return result.field_76332_a < field_76332_a ? -1 : (field_76332_a > field_76332_a ? 1 : result.field_76331_c.compareTo(field_76331_c));
        }

        public int func_76329_a() {
            return (field_76331_c.hashCode() & 11184810) + 4473924;
        }
    }
}