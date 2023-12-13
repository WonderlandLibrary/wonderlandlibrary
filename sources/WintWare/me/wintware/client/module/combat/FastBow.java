/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.combat;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.math.BlockPos;

public class FastBow
extends Module {
    public FastBow() {
        super("FastBow", Category.Combat);
    }

    @EventTarget
    public void onEventUpdate(EventUpdate e) {
        if (Minecraft.player.inventory.getCurrentItem().getItem() instanceof ItemBow) {
            if (Minecraft.player.isHandActive()) {
                if ((double)Minecraft.player.getItemInUseMaxCount() >= 2.4) {
                    Minecraft.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Minecraft.player.getHorizontalFacing()));
                    Minecraft.player.connection.sendPacket(new CPacketPlayerTryUseItem(Minecraft.player.getActiveHand()));
                    Minecraft.player.stopActiveHand();
                }
            }
        }
    }
}

