package net.minecraft.world.gen.layer;

import java.util.concurrent.Callable;
import net.minecraft.world.biome.BiomeGenBase;

static final class GenLayer.1
implements Callable<String> {
    final /* synthetic */ BiomeGenBase val$biomegenbase;

    GenLayer.1(BiomeGenBase biomeGenBase) {
        this.val$biomegenbase = biomeGenBase;
    }

    public String call() throws Exception {
        return String.valueOf((Object)this.val$biomegenbase);
    }
}
