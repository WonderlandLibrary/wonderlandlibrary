package net.minecraft.world.gen.layer;

import java.util.concurrent.Callable;
import net.minecraft.world.biome.BiomeGenBase;

static final class GenLayer.2
implements Callable<String> {
    final /* synthetic */ BiomeGenBase val$biomegenbase1;

    GenLayer.2(BiomeGenBase biomeGenBase) {
        this.val$biomegenbase1 = biomeGenBase;
    }

    public String call() throws Exception {
        return String.valueOf((Object)this.val$biomegenbase1);
    }
}
