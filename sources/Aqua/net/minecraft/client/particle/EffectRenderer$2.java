package net.minecraft.client.particle;

import java.util.concurrent.Callable;

class EffectRenderer.2
implements Callable<String> {
    final /* synthetic */ int val$i;

    EffectRenderer.2(int n) {
        this.val$i = n;
    }

    public String call() throws Exception {
        return this.val$i == 0 ? "MISC_TEXTURE" : (this.val$i == 1 ? "TERRAIN_TEXTURE" : (this.val$i == 3 ? "ENTITY_PARTICLE_TEXTURE" : "Unknown - " + this.val$i));
    }
}
