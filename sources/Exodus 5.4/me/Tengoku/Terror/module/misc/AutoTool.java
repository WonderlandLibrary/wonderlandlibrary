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
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;

public class AutoTool
extends Module {
    private int oldSlot = -1;
    private boolean wasBreaking = false;

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public void onUpdate(EventUpdate var1_1) {
        block10: {
            block9: {
                if (AutoTool.mc.currentScreen != null) break block9;
                if (Minecraft.thePlayer == null) break block9;
                if (Minecraft.theWorld == null || AutoTool.mc.objectMouseOver == null || AutoTool.mc.objectMouseOver.getBlockPos() == null || AutoTool.mc.objectMouseOver.entityHit != null || !Mouse.isButtonDown((int)0)) break block9;
                var2_2 = 1.0f;
                var3_3 = -1;
                var4_4 = Minecraft.theWorld.getBlockState(AutoTool.mc.objectMouseOver.getBlockPos()).getBlock();
                var5_5 = 0;
                while (var5_5 < 9) {
                    var6_6 = Minecraft.thePlayer.inventory.getStackInSlot(var5_5);
                    if (var6_6 != null && (var7_7 = var6_6.getStrVsBlock(var4_4)) > var2_2) {
                        var2_2 = var7_7;
                        var3_3 = var5_5;
                    }
                    ++var5_5;
                }
                if (var3_3 == -1) ** GOTO lbl-1000
                if (Minecraft.thePlayer.inventory.currentItem != var3_3) {
                    Minecraft.thePlayer.inventory.currentItem = var3_3;
                    this.wasBreaking = true;
                } else if (var3_3 == -1) {
                    if (this.wasBreaking) {
                        Minecraft.thePlayer.inventory.currentItem = this.oldSlot;
                        this.wasBreaking = false;
                    }
                    this.oldSlot = Minecraft.thePlayer.inventory.currentItem;
                }
                break block10;
            }
            if (Minecraft.thePlayer != null) {
                if (Minecraft.theWorld != null) {
                    if (this.wasBreaking) {
                        Minecraft.thePlayer.inventory.currentItem = this.oldSlot;
                        this.wasBreaking = false;
                    }
                    this.oldSlot = Minecraft.thePlayer.inventory.currentItem;
                }
            }
        }
    }

    public AutoTool() {
        super("Auto Tool", 0, Category.MISC, "Automatically equips the needed tool for breaking the block.");
    }
}

