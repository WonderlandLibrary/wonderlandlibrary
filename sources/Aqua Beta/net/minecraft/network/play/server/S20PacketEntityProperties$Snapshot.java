package net.minecraft.network.play.server;

import java.util.Collection;
import net.minecraft.entity.ai.attributes.AttributeModifier;

public class S20PacketEntityProperties.Snapshot {
    private final String field_151412_b;
    private final double field_151413_c;
    private final Collection<AttributeModifier> field_151411_d;

    public S20PacketEntityProperties.Snapshot(String p_i45235_2_, double p_i45235_3_, Collection<AttributeModifier> p_i45235_5_) {
        this.field_151412_b = p_i45235_2_;
        this.field_151413_c = p_i45235_3_;
        this.field_151411_d = p_i45235_5_;
    }

    public String func_151409_a() {
        return this.field_151412_b;
    }

    public double func_151410_b() {
        return this.field_151413_c;
    }

    public Collection<AttributeModifier> func_151408_c() {
        return this.field_151411_d;
    }
}
