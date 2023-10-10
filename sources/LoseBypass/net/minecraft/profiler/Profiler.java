/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.profiler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Profiler {
    private static final Logger logger = LogManager.getLogger();
    private final List<String> sectionList = Lists.newArrayList();
    private final List<Long> timestampList = Lists.newArrayList();
    public boolean profilingEnabled;
    private String profilingSection = "";
    private final Map<String, Long> profilingMap = Maps.newHashMap();

    public void clearProfiling() {
        this.profilingMap.clear();
        this.profilingSection = "";
        this.sectionList.clear();
    }

    public void startSection(String name) {
        if (!this.profilingEnabled) return;
        if (this.profilingSection.length() > 0) {
            this.profilingSection = this.profilingSection + ".";
        }
        this.profilingSection = this.profilingSection + name;
        this.sectionList.add(this.profilingSection);
        this.timestampList.add(System.nanoTime());
    }

    public void endSection() {
        if (!this.profilingEnabled) return;
        long i = System.nanoTime();
        long j = this.timestampList.remove(this.timestampList.size() - 1);
        this.sectionList.remove(this.sectionList.size() - 1);
        long k = i - j;
        if (this.profilingMap.containsKey(this.profilingSection)) {
            this.profilingMap.put(this.profilingSection, this.profilingMap.get(this.profilingSection) + k);
        } else {
            this.profilingMap.put(this.profilingSection, k);
        }
        if (k > 100000000L) {
            logger.warn("Something's taking too long! '" + this.profilingSection + "' took aprox " + (double)k / 1000000.0 + " ms");
        }
        this.profilingSection = !this.sectionList.isEmpty() ? this.sectionList.get(this.sectionList.size() - 1) : "";
    }

    public List<Result> getProfilingData(String p_76321_1_) {
        if (!this.profilingEnabled) {
            return null;
        }
        long i = this.profilingMap.containsKey("root") ? this.profilingMap.get("root") : 0L;
        long j = this.profilingMap.containsKey(p_76321_1_) ? this.profilingMap.get(p_76321_1_) : -1L;
        ArrayList<Result> list = Lists.newArrayList();
        if (p_76321_1_.length() > 0) {
            p_76321_1_ = p_76321_1_ + ".";
        }
        long k = 0L;
        for (String s : this.profilingMap.keySet()) {
            if (s.length() <= p_76321_1_.length() || !s.startsWith(p_76321_1_) || s.indexOf(".", p_76321_1_.length() + 1) >= 0) continue;
            k += this.profilingMap.get(s).longValue();
        }
        float f = k;
        if (k < j) {
            k = j;
        }
        if (i < k) {
            i = k;
        }
        for (String s1 : this.profilingMap.keySet()) {
            if (s1.length() <= p_76321_1_.length() || !s1.startsWith(p_76321_1_) || s1.indexOf(".", p_76321_1_.length() + 1) >= 0) continue;
            long l = this.profilingMap.get(s1);
            double d0 = (double)l * 100.0 / (double)k;
            double d1 = (double)l * 100.0 / (double)i;
            String s2 = s1.substring(p_76321_1_.length());
            list.add(new Result(s2, d0, d1));
        }
        for (String s3 : this.profilingMap.keySet()) {
            this.profilingMap.put(s3, this.profilingMap.get(s3) * 999L / 1000L);
        }
        if ((float)k > f) {
            list.add(new Result("unspecified", (double)((float)k - f) * 100.0 / (double)k, (double)((float)k - f) * 100.0 / (double)i));
        }
        Collections.sort(list);
        list.add(0, new Result(p_76321_1_, 100.0, (double)k * 100.0 / (double)i));
        return list;
    }

    public void endStartSection(String name) {
        this.endSection();
        this.startSection(name);
    }

    public String getNameOfLastSection() {
        if (this.sectionList.size() == 0) {
            return "[UNKNOWN]";
        }
        String string = this.sectionList.get(this.sectionList.size() - 1);
        return string;
    }

    public static final class Result
    implements Comparable<Result> {
        public double field_76332_a;
        public double field_76330_b;
        public String field_76331_c;

        public Result(String p_i1554_1_, double p_i1554_2_, double p_i1554_4_) {
            this.field_76331_c = p_i1554_1_;
            this.field_76332_a = p_i1554_2_;
            this.field_76330_b = p_i1554_4_;
        }

        @Override
        public int compareTo(Result p_compareTo_1_) {
            if (p_compareTo_1_.field_76332_a < this.field_76332_a) {
                return -1;
            }
            if (p_compareTo_1_.field_76332_a > this.field_76332_a) {
                return 1;
            }
            int n = p_compareTo_1_.field_76331_c.compareTo(this.field_76331_c);
            return n;
        }

        public int func_76329_a() {
            return (this.field_76331_c.hashCode() & 0xAAAAAA) + 0x444444;
        }
    }
}

