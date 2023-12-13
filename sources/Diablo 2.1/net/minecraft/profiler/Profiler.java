package net.minecraft.profiler;
import java.util.List;
public class Profiler {
    public boolean profilingEnabled;
    public Profiler() {
    }
    public void clearProfiling() {
    }
    public void startSection(String name) {
    }
    public void endSection() {
    }
    public List<Profiler.Result> getProfilingData(String profilerName) {
        return null;
    }
    public void endStartSection(String name) {
    }
    public String getNameOfLastSection() {
        return "[REMOVED]";
    }
    public void startSection(Class<?> p_startSection_1_) {}
    public static final class Result implements Comparable<Profiler.Result> {
        public String profilerName;
        public double usePercentage;
        public double totalUsePercentage;
        public Result(String profilerName, double usePercentage, double totalUsePercentage) {
            this.profilerName = profilerName;
            this.usePercentage = usePercentage;
            this.totalUsePercentage = totalUsePercentage;
        }
        public int compareTo(Profiler.Result p_compareTo_1_) {
            return p_compareTo_1_.usePercentage < this.usePercentage ? -1 : (p_compareTo_1_.usePercentage > this.usePercentage ? 1 : p_compareTo_1_.profilerName.compareTo(this.profilerName));
        }
        public int getColor() {
            return (this.profilerName.hashCode() & 11184810) + 4473924;
        }
    }
}