// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render.killeffects;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.entity.effect.EntityLightningBolt;
import dev.tenacity.utils.Utils;

public class EffectManager implements Utils
{
    public void playKillEffect(final KillEffects.Location location) {
        final String mode = KillEffects.killEffect.getMode();
        switch (mode) {
            case "Blood Explosion": {
                this.playBlockBreak(location.x, location.y, location.z, location.eyeHeight, 152);
                break;
            }
            case "Lightning Bolt": {
                EffectManager.mc.theWorld.addWeatherEffect(new EntityLightningBolt(EffectManager.mc.theWorld, location.x, location.y, location.z));
                EffectManager.mc.theWorld.playSoundAtPos(new BlockPos(location.x, location.y, location.z), "ambient.weather.thunder", 5.0f, 1.0f, false);
                break;
            }
        }
    }
    
    private void playBlockBreak(final double x, final double y, final double z, final double eyeHeight, final int blockId) {
        EffectManager.mc.renderGlobal.playAuxSFX(EffectManager.mc.thePlayer, 2001, new BlockPos(x, y + eyeHeight, z), blockId);
    }
}
