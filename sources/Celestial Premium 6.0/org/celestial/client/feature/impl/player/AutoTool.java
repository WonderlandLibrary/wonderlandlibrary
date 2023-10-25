/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.player;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.BooleanSetting;

public class AutoTool
extends Feature {
    private final BooleanSetting switchBack = new BooleanSetting("Switchback", false, () -> true);
    private int oldSlot = -1;
    private boolean wasBreaking;

    public AutoTool() {
        super("AutoTool", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0431\u0435\u0440\u0435\u0442 \u043b\u0443\u0447\u0448\u0438\u0439 \u0438\u043d\u0441\u0442\u0440\u0443\u043c\u0435\u043d\u0442 \u0432 \u0440\u0443\u043a\u0438 \u043f\u0440\u0438 \u043b\u043e\u043c\u0430\u043d\u0438\u0438 \u0431\u043b\u043e\u043a\u0430", Type.Player);
        this.addSettings(this.switchBack);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (AutoTool.mc.currentScreen == null && AutoTool.mc.player != null && AutoTool.mc.world != null && AutoTool.mc.objectMouseOver != null && AutoTool.mc.objectMouseOver.entityHit == null && AutoTool.mc.gameSettings.keyBindAttack.isKeyDown()) {
            int bestSlot = -1;
            float bestSpeed = 1.0f;
            Block block = AutoTool.mc.world.getBlockState(AutoTool.mc.objectMouseOver.getBlockPos()).getBlock();
            for (int slot = 0; slot < 9; ++slot) {
                ItemStack item = AutoTool.mc.player.inventory.getStackInSlot(slot);
                float speed = item.getDestroySpeed(block.getDefaultState());
                if (!(speed > bestSpeed)) continue;
                bestSpeed = speed;
                bestSlot = slot;
            }
            if (bestSlot != -1 && AutoTool.mc.player.inventory.currentItem != bestSlot) {
                AutoTool.mc.player.inventory.currentItem = bestSlot;
                this.wasBreaking = true;
            } else if (bestSlot == -1) {
                if (this.wasBreaking) {
                    if (this.switchBack.getCurrentValue()) {
                        AutoTool.mc.player.inventory.currentItem = this.oldSlot;
                    }
                    this.wasBreaking = false;
                }
                this.oldSlot = AutoTool.mc.player.inventory.currentItem;
            }
        } else if (AutoTool.mc.player != null && AutoTool.mc.world != null) {
            if (this.wasBreaking && this.switchBack.getCurrentValue()) {
                if (this.switchBack.getCurrentValue()) {
                    AutoTool.mc.player.inventory.currentItem = this.oldSlot;
                }
                this.wasBreaking = false;
            }
            this.oldSlot = AutoTool.mc.player.inventory.currentItem;
        }
    }
}

