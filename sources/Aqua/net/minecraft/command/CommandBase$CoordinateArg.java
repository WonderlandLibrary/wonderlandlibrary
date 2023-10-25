package net.minecraft.command;

public static class CommandBase.CoordinateArg {
    private final double field_179633_a;
    private final double field_179631_b;
    private final boolean field_179632_c;

    protected CommandBase.CoordinateArg(double p_i46051_1_, double p_i46051_3_, boolean p_i46051_5_) {
        this.field_179633_a = p_i46051_1_;
        this.field_179631_b = p_i46051_3_;
        this.field_179632_c = p_i46051_5_;
    }

    public double func_179628_a() {
        return this.field_179633_a;
    }

    public double func_179629_b() {
        return this.field_179631_b;
    }

    public boolean func_179630_c() {
        return this.field_179632_c;
    }
}
