// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.movement;

import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;
import net.augustus.events.EventSaveWalk;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.BooleanValue;
import net.augustus.modules.Module;

public class SafeWalk extends Module
{
    public final BooleanValue inAir;
    public final BooleanValue minemen;
    
    public SafeWalk() {
        super("SafeWalk", new Color(115, 135, 26), Categorys.MOVEMENT);
        this.inAir = new BooleanValue(1, "InAir", this, true);
        this.minemen = new BooleanValue(2, "Minemen", this, true);
    }
    
    @EventTarget
    public void onEventSaveWalk(final EventSaveWalk eventSaveWalk) {
        final Block blockUnder1 = SafeWalk.mc.theWorld.getBlockState(new BlockPos(SafeWalk.mc.thePlayer.posX, SafeWalk.mc.thePlayer.posY - 1.0, SafeWalk.mc.thePlayer.posZ)).getBlock();
        final Block blockUnder2 = SafeWalk.mc.theWorld.getBlockState(new BlockPos(SafeWalk.mc.thePlayer.posX, SafeWalk.mc.thePlayer.posY - 2.0, SafeWalk.mc.thePlayer.posZ)).getBlock();
        if (SafeWalk.mc.thePlayer.onGround) {
            eventSaveWalk.setSaveWalk(true);
        }
        else if (this.inAir.getBoolean() && ((blockUnder1 instanceof BlockAir && blockUnder2 instanceof BlockAir) || !this.minemen.getBoolean())) {
            eventSaveWalk.setSaveWalk(true);
        }
    }
}
