package net.minecraft.profiler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.renderer.GlStateManager;
import net.optifine.Config;
import net.optifine.Lagometer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class Profiler {

    private static final Logger LOGGER = LogManager.getLogger();

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
    private static final int HASH_SCHEDULED_EXECUTABLES = "scheduledExecutables".hashCode();
    private static final int HASH_TICK = "tick".hashCode();
    private static final int HASH_PRE_RENDER_ERRORS = "preRenderErrors".hashCode();
    private static final int HASH_RENDER = "render".hashCode();
    private static final int HASH_DISPLAY = "display".hashCode();

    public Profiler() {
        this.profilerLocalEnabled = this.profilerGlobalEnabled;
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
            final int i = name.hashCode();

            if (i == HASH_SCHEDULED_EXECUTABLES && name.equals("scheduledExecutables")) {
                Lagometer.timerScheduledExecutables.start();
            } else if (i == HASH_TICK && name.equals("tick") && Config.isMinecraftThread()) {
                Lagometer.timerScheduledExecutables.end();
                Lagometer.timerTick.start();
            } else if (i == HASH_PRE_RENDER_ERRORS && name.equals("preRenderErrors")) {
                Lagometer.timerTick.end();
            }
        }

        if (Config.isFastRender()) {
            final int j = name.hashCode();

            if (j == HASH_RENDER && name.equals("render")) {
                GlStateManager.clearEnabled = false;
            } else if (j == HASH_DISPLAY && name.equals("display")) {
                GlStateManager.clearEnabled = true;
            }
        }

        if (this.profilerLocalEnabled) {
            if (this.profilingEnabled) {
                if (!this.profilingSection.isEmpty()) {
                    this.profilingSection = this.profilingSection + ".";
                }

                this.profilingSection = this.profilingSection + name;
                this.sectionList.add(this.profilingSection);
                this.timestampList.add(System.nanoTime());
            }
        }
    }

    /**
     * End section
     */
    public void endSection() {
        if (this.profilerLocalEnabled) {
            if (this.profilingEnabled) {
                final long i = System.nanoTime();
                final long j = this.timestampList.remove(this.timestampList.size() - 1);
                this.sectionList.remove(this.sectionList.size() - 1);
                final long k = i - j;

                if (this.profilingMap.containsKey(this.profilingSection)) {
                    this.profilingMap.put(this.profilingSection, this.profilingMap.get(this.profilingSection).longValue() + k);
                } else {
                    this.profilingMap.put(this.profilingSection, k);
                }

                if (k > 100000000L) {
                    LOGGER.warn("Something's taking too long! '" + this.profilingSection + "' took aprox " + (double) k / 1000000.0D + " ms");
                }

                this.profilingSection = !this.sectionList.isEmpty() ? this.sectionList.get(this.sectionList.size() - 1) : "";
            }
        }
    }

    /**
     * Get profiling data
     */
    public List getProfilingData(String p_76321_1_) {
        this.profilerLocalEnabled = this.profilerGlobalEnabled;

        if (!this.profilerLocalEnabled) {
            return new ArrayList(Arrays.asList(new Result("root", 0.0D, 0.0D)));
        } else if (!this.profilingEnabled) {
            return null;
        } else {
            long i = this.profilingMap.containsKey("root") ? this.profilingMap.get("root") : 0L;
            final long j = this.profilingMap.containsKey(p_76321_1_) ? this.profilingMap.get(p_76321_1_) : -1L;
            final ArrayList arraylist = Lists.newArrayList();

            if (!p_76321_1_.isEmpty()) {
                p_76321_1_ = p_76321_1_ + ".";
            }

            long k = 0L;

            for (Object s : this.profilingMap.keySet()) {
                if (((String) s).length() > p_76321_1_.length() && ((String) s).startsWith(p_76321_1_) && ((String) s).indexOf(".", p_76321_1_.length() + 1) < 0) {
                    k += this.profilingMap.get(s);
                }
            }

            final float f = (float) k;

            if (k < j) {
                k = j;
            }

            if (i < k) {
                i = k;
            }

            for (Object s10 : this.profilingMap.keySet()) {
                final String s1 = (String) s10;

                if (s1.length() > p_76321_1_.length() && s1.startsWith(p_76321_1_) && s1.indexOf(".", p_76321_1_.length() + 1) < 0) {
                    final long l = this.profilingMap.get(s1);
                    final double d0 = (double) l * 100.0D / (double) k;
                    final double d1 = (double) l * 100.0D / (double) i;
                    final String s2 = s1.substring(p_76321_1_.length());
                    arraylist.add(new Profiler.Result(s2, d0, d1));
                }
            }

            this.profilingMap.replaceAll((k1, v) -> this.profilingMap.get(k1) * 950L / 1000L);

            if ((float) k > f) {
                arraylist.add(new Profiler.Result("unspecified", (double) ((float) k - f) * 100.0D / (double) k, (double) ((float) k - f) * 100.0D / (double) i));
            }

            Collections.sort(arraylist);

            arraylist.add(0, new Profiler.Result(p_76321_1_, 100.0D, (double) k * 100.0D / (double) i));
            return arraylist;
        }
    }

    /**
     * End current section and start a new section
     */
    public void endStartSection(String name) {
        if (this.profilerLocalEnabled) {
            this.endSection();
            this.startSection(name);
        }
    }

    public String getNameOfLastSection() {
        return this.sectionList.isEmpty() ? "[UNKNOWN]" : this.sectionList.get(this.sectionList.size() - 1);
    }

    public static final class Result implements Comparable {

        public double field_76332_a;
        public double field_76330_b;
        public String field_76331_c;
        private static final String __OBFID = "CL_00001498";

        public Result(String p_i1554_1_, double p_i1554_2_, double p_i1554_4_) {
            this.field_76331_c = p_i1554_1_;
            this.field_76332_a = p_i1554_2_;
            this.field_76330_b = p_i1554_4_;
        }

        public int compareTo(Profiler.Result p_compareTo_1_) {
            return p_compareTo_1_.field_76332_a < this.field_76332_a ? -1 : p_compareTo_1_.field_76332_a > this.field_76332_a ? 1 : p_compareTo_1_.field_76331_c.compareTo(this.field_76331_c);
        }

        public int func_76329_a() {
            return (this.field_76331_c.hashCode() & 11184810) + 4473924;
        }

        public int compareTo(Object p_compareTo_1_) {
            return this.compareTo((Profiler.Result) p_compareTo_1_);
        }

    }

}
