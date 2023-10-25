/*
 * Decompiled with CFR 0.152.
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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Profiler {
    private final List<String> sectionList = Lists.newArrayList();
    public boolean profilingEnabled;
    private final Map<String, Long> profilingMap;
    private final List<Long> timestampList = Lists.newArrayList();
    private static final Logger logger = LogManager.getLogger();
    private String profilingSection = "";

    public List<Result> getProfilingData(String string) {
        if (!this.profilingEnabled) {
            return null;
        }
        long l = this.profilingMap.containsKey("root") ? this.profilingMap.get("root") : 0L;
        long l2 = this.profilingMap.containsKey(string) ? this.profilingMap.get(string) : -1L;
        ArrayList arrayList = Lists.newArrayList();
        if (string.length() > 0) {
            string = String.valueOf(string) + ".";
        }
        long l3 = 0L;
        for (String string2 : this.profilingMap.keySet()) {
            if (string2.length() <= string.length() || !string2.startsWith(string) || string2.indexOf(".", string.length() + 1) >= 0) continue;
            l3 += this.profilingMap.get(string2).longValue();
        }
        float f = l3;
        if (l3 < l2) {
            l3 = l2;
        }
        if (l < l3) {
            l = l3;
        }
        for (String string3 : this.profilingMap.keySet()) {
            if (string3.length() <= string.length() || !string3.startsWith(string) || string3.indexOf(".", string.length() + 1) >= 0) continue;
            long l4 = this.profilingMap.get(string3);
            double d = (double)l4 * 100.0 / (double)l3;
            double d2 = (double)l4 * 100.0 / (double)l;
            String string4 = string3.substring(string.length());
            arrayList.add(new Result(string4, d, d2));
        }
        for (String string5 : this.profilingMap.keySet()) {
            this.profilingMap.put(string5, this.profilingMap.get(string5) * 999L / 1000L);
        }
        if ((float)l3 > f) {
            arrayList.add(new Result("unspecified", (double)((float)l3 - f) * 100.0 / (double)l3, (double)((float)l3 - f) * 100.0 / (double)l));
        }
        Collections.sort(arrayList);
        arrayList.add(0, new Result(string, 100.0, (double)l3 * 100.0 / (double)l));
        return arrayList;
    }

    public void startSection(String string) {
        if (this.profilingEnabled) {
            if (this.profilingSection.length() > 0) {
                this.profilingSection = String.valueOf(this.profilingSection) + ".";
            }
            this.profilingSection = String.valueOf(this.profilingSection) + string;
            this.sectionList.add(this.profilingSection);
            this.timestampList.add(System.nanoTime());
        }
    }

    public void clearProfiling() {
        this.profilingMap.clear();
        this.profilingSection = "";
        this.sectionList.clear();
    }

    public void endStartSection(String string) {
        this.endSection();
        this.startSection(string);
    }

    public String getNameOfLastSection() {
        return this.sectionList.size() == 0 ? "[UNKNOWN]" : this.sectionList.get(this.sectionList.size() - 1);
    }

    public Profiler() {
        this.profilingMap = Maps.newHashMap();
    }

    public void endSection() {
        if (this.profilingEnabled) {
            long l = System.nanoTime();
            long l2 = this.timestampList.remove(this.timestampList.size() - 1);
            this.sectionList.remove(this.sectionList.size() - 1);
            long l3 = l - l2;
            if (this.profilingMap.containsKey(this.profilingSection)) {
                this.profilingMap.put(this.profilingSection, this.profilingMap.get(this.profilingSection) + l3);
            } else {
                this.profilingMap.put(this.profilingSection, l3);
            }
            if (l3 > 100000000L) {
                logger.warn("Something's taking too long! '" + this.profilingSection + "' took aprox " + (double)l3 / 1000000.0 + " ms");
            }
            this.profilingSection = !this.sectionList.isEmpty() ? this.sectionList.get(this.sectionList.size() - 1) : "";
        }
    }

    public static final class Result
    implements Comparable<Result> {
        public double field_76332_a;
        public String field_76331_c;
        public double field_76330_b;

        @Override
        public int compareTo(Result result) {
            return result.field_76332_a < this.field_76332_a ? -1 : (result.field_76332_a > this.field_76332_a ? 1 : result.field_76331_c.compareTo(this.field_76331_c));
        }

        public Result(String string, double d, double d2) {
            this.field_76331_c = string;
            this.field_76332_a = d;
            this.field_76330_b = d2;
        }

        public int func_76329_a() {
            return (this.field_76331_c.hashCode() & 0xAAAAAA) + 0x444444;
        }
    }
}

