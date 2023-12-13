// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

public class EntityFireworkStarterFX_Factory implements IParticleFactory
{
    private static final String __OBFID = "CL_00002603";
    
    @Override
    public EntityFX func_178902_a(final int p_178902_1_, final World worldIn, final double p_178902_3_, final double p_178902_5_, final double p_178902_7_, final double p_178902_9_, final double p_178902_11_, final double p_178902_13_, final int... p_178902_15_) {
        final EntityFireworkSparkFX var16 = new EntityFireworkSparkFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_, Minecraft.getMinecraft().effectRenderer);
        var16.setAlphaF(0.99f);
        return var16;
    }
}
