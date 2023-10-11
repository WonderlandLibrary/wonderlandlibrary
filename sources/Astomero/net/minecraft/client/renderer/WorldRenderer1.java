package net.minecraft.client.renderer;

import java.util.*;
import com.google.common.primitives.*;

class WorldRenderer1 implements Comparator
{
    final float[] field_181659_a;
    final WorldRenderer field_181660_b;
    
    WorldRenderer1(final WorldRenderer p_i46500_1_, final float[] p_i46500_2_) {
        this.field_181660_b = p_i46500_1_;
        this.field_181659_a = p_i46500_2_;
    }
    
    public int compare(final Integer p_compare_1_, final Integer p_compare_2_) {
        return Floats.compare(this.field_181659_a[p_compare_2_], this.field_181659_a[p_compare_1_]);
    }
    
    @Override
    public int compare(final Object p_compare_1_, final Object p_compare_2_) {
        return this.compare((Integer)p_compare_1_, (Integer)p_compare_2_);
    }
}
