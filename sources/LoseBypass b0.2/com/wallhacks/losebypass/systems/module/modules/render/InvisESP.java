/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.systems.module.modules.render;

import com.wallhacks.losebypass.systems.module.Module;
import com.wallhacks.losebypass.systems.setting.settings.BooleanSetting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

@Module.Registration(name="InvisESP", description="Shows invisible players", category=Module.Category.RENDER)
public class InvisESP
extends Module {
    BooleanSetting playerOnly = this.booleanSetting("PlayerOnly", false).description("Only show players that are invisible");
    private static InvisESP instance;

    public InvisESP() {
        instance = this;
    }

    public static boolean enabled(EntityLivingBase entityLivingBase) {
        if (!(entityLivingBase instanceof EntityPlayer)) {
            if (InvisESP.instance.playerOnly.isOn()) return false;
        }
        if (!instance.isEnabled()) return false;
        return true;
    }
}

