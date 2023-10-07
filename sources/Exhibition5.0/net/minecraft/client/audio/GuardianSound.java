// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.monster.EntityGuardian;

public class GuardianSound extends MovingSound
{
    private final EntityGuardian guardian;
    private static final String __OBFID = "CL_00002381";
    
    public GuardianSound(final EntityGuardian guardian) {
        super(new ResourceLocation("minecraft:mob.guardian.attack"));
        this.guardian = guardian;
        this.attenuationType = ISound.AttenuationType.NONE;
        this.repeat = true;
        this.repeatDelay = 0;
    }
    
    @Override
    public void update() {
        if (!this.guardian.isDead && this.guardian.func_175474_cn()) {
            this.xPosF = (float)this.guardian.posX;
            this.yPosF = (float)this.guardian.posY;
            this.zPosF = (float)this.guardian.posZ;
            final float var1 = this.guardian.func_175477_p(0.0f);
            this.volume = 0.0f + 1.0f * var1 * var1;
            this.pitch = 0.7f + 0.5f * var1;
        }
        else {
            this.donePlaying = true;
        }
    }
}
