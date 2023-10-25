package net.minecraft.entity.passive;

static enum EntityRabbit.EnumMoveType {
    NONE(0.0f, 0.0f, 30, 1),
    HOP(0.8f, 0.2f, 20, 10),
    STEP(1.0f, 0.45f, 14, 14),
    SPRINT(1.75f, 0.4f, 1, 8),
    ATTACK(2.0f, 0.7f, 7, 8);

    private final float speed;
    private final float field_180077_g;
    private final int duration;
    private final int field_180085_i;

    private EntityRabbit.EnumMoveType(float typeSpeed, float p_i45866_4_, int typeDuration, int p_i45866_6_) {
        this.speed = typeSpeed;
        this.field_180077_g = p_i45866_4_;
        this.duration = typeDuration;
        this.field_180085_i = p_i45866_6_;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float func_180074_b() {
        return this.field_180077_g;
    }

    public int getDuration() {
        return this.duration;
    }

    public int func_180073_d() {
        return this.field_180085_i;
    }
}
