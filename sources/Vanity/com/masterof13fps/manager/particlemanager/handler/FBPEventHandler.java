package com.masterof13fps.manager.particlemanager.handler;

import com.masterof13fps.manager.particlemanager.particle.FBPParticleDigging;
import com.masterof13fps.manager.particlemanager.particle.FBPParticleManager;
import com.masterof13fps.manager.particlemanager.FBP;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class FBPEventHandler {
    Minecraft mc = Minecraft.getInstance();

    public void onWorldLoadEvent() {
    }

    public void onEntityJoinWorldEvent(Entity entity, World world) {
        if (entity == this.mc.thePlayer) {
            FBP.fancyEffectRenderer = new FBPParticleManager(world, this.mc.getRenderManager().renderEngine, (IParticleFactory) new FBPParticleDigging.Factory());
            if (FBP.originalEffectRenderer == null || (FBP.originalEffectRenderer != this.mc.effectRenderer && FBP.originalEffectRenderer != FBP.fancyEffectRenderer))
                FBP.originalEffectRenderer = this.mc.effectRenderer;
            this.mc.effectRenderer = (EffectRenderer) FBP.fancyEffectRenderer;
        }
    }
}
