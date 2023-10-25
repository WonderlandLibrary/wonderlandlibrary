package net.minecraft.profiler;

public static final class Profiler.Result
implements Comparable<Profiler.Result> {
    public double field_76332_a;
    public double field_76330_b;
    public String field_76331_c;

    public Profiler.Result(String profilerName, double usePercentage, double totalUsePercentage) {
        this.field_76331_c = profilerName;
        this.field_76332_a = usePercentage;
        this.field_76330_b = totalUsePercentage;
    }

    public int compareTo(Profiler.Result p_compareTo_1_) {
        return p_compareTo_1_.field_76332_a < this.field_76332_a ? -1 : (p_compareTo_1_.field_76332_a > this.field_76332_a ? 1 : p_compareTo_1_.field_76331_c.compareTo(this.field_76331_c));
    }

    public int getColor() {
        return (this.field_76331_c.hashCode() & 0xAAAAAA) + 0x444444;
    }
}
