package net.minecraft.client.particle;

import java.util.concurrent.Callable;

class EffectRenderer.4
implements Callable<String> {
    final /* synthetic */ int val$i_f;

    EffectRenderer.4(int n) {
        this.val$i_f = n;
    }

    public String call() throws Exception {
        return this.val$i_f == 0 ? "MISC_TEXTURE" : (this.val$i_f == 1 ? "TERRAIN_TEXTURE" : (this.val$i_f == 3 ? "ENTITY_PARTICLE_TEXTURE" : "Unknown - " + this.val$i_f));
    }
}
