// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.player;

import net.minecraft.client.settings.KeyBinding;
import com.elementars.eclient.module.Category;
import com.elementars.eclient.module.Module;

public class AutoWalk extends Module
{
    public AutoWalk() {
        super("AutoWalk", "Automatically walks", 0, Category.PLAYER, true);
    }
    
    @Override
    public void onUpdate() {
        KeyBinding.setKeyBindState(AutoWalk.mc.gameSettings.keyBindForward.getKeyCode(), true);
    }
}
