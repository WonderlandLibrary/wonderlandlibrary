package net.minecraft.client.renderer;

import com.google.common.primitives.Floats;
import java.util.Comparator;

class WorldRenderer.1
implements Comparator<Integer> {
    final /* synthetic */ float[] val$afloat;

    WorldRenderer.1(float[] fArray) {
        this.val$afloat = fArray;
    }

    public int compare(Integer p_compare_1_, Integer p_compare_2_) {
        return Floats.compare((float)this.val$afloat[p_compare_2_], (float)this.val$afloat[p_compare_1_]);
    }
}
