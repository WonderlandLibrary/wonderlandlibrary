/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package me.Tengoku.Terror.module.misc;

import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;

public class FastMine
extends Module {
    private int oldSlot = -1;
    private boolean wasBreaking = false;

    public FastMine() {
        super("Fast Mine", 0, Category.MISC, "Mines blocks faster than usual.");
    }

    /*
     * Enabled aggressive block sorting
     */
    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if (FastMine.mc.currentScreen == null) {
            if (Minecraft.thePlayer != null) {
                if (Minecraft.theWorld != null && FastMine.mc.objectMouseOver != null && FastMine.mc.objectMouseOver.getBlockPos() != null && FastMine.mc.objectMouseOver.entityHit == null && Mouse.isButtonDown((int)0)) {
                    Block block = Minecraft.theWorld.getBlockState(FastMine.mc.objectMouseOver.getBlockPos()).getBlock();
                    if (block instanceof BlockAir) return;
                    this.wasBreaking = true;
                    FastMine.mc.timer.timerSpeed = 3.0f;
                    return;
                }
            }
        }
        FastMine.mc.timer.timerSpeed = 1.0f;
    }
}

