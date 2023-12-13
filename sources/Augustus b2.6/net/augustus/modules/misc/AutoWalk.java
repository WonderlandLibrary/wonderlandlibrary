// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.misc;

import net.lenni0451.eventapi.reflection.EventTarget;
import net.augustus.events.EventTick;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.minecraft.util.BlockPos;
import java.util.ArrayList;
import net.augustus.modules.Module;

public class AutoWalk extends Module
{
    private static final ArrayList<BlockPos> GROUND_BLOCK_POS;
    private static final ArrayList<BlockPos> GROUND_BLOCK_POS_RENDER;
    private Thread thread;
    
    public AutoWalk() {
        super("AutoWalk", Color.red, Categorys.MISC);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        AutoWalk.mc.gameSettings.keyBindForward.pressed = false;
        AutoWalk.GROUND_BLOCK_POS.clear();
        AutoWalk.GROUND_BLOCK_POS_RENDER.clear();
    }
    
    @EventTarget
    public void onEventTick(final EventTick eventTick) {
        if (AutoWalk.mc.currentScreen == null) {
            AutoWalk.mc.gameSettings.keyBindForward.pressed = true;
        }
    }
    
    static {
        GROUND_BLOCK_POS = new ArrayList<BlockPos>();
        GROUND_BLOCK_POS_RENDER = new ArrayList<BlockPos>();
    }
}
