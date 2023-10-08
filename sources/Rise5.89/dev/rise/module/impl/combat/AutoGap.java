/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.combat;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.packet.PacketSendEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.player.PlayerUtil;
import net.minecraft.network.play.client.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@ModuleInfo(name = "AutoGap", description = "Eats Golden Apples when your health is getting low", category = Category.COMBAT)
public final class AutoGap extends Module {

    private int ticks;
    public static int gap = -37;

    private final NumberSetting health = new NumberSetting("Health", this, 10, 0, 20, 0.1);

    private final BooleanSetting switchSlot = new BooleanSetting("Switch Slots", this, true);

    @Override
    protected void onDisable() {
        gap = -37;
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        ++ticks;

        if (ticks > 10 && mc.thePlayer.getHealth() < health.getValue()) {
            gap = PlayerUtil.findGap() - 36;

            if (gap != -37) {
                mc.getNetHandler().addToSendQueueWithoutEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                if (switchSlot.isEnabled()) mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(gap));
                mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(gap)));

                for (int i = 0; i <= 31; i++) mc.getNetHandler().addToSendQueue(new C03PacketPlayer());

                if (switchSlot.isEnabled())
                    mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));

                mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));

                ticks = 0;
            }
        }

        gap = -37;
    }

    @Override
    public void onPacketSend(final PacketSendEvent event) {
        if (event.getPacket() instanceof C0APacketAnimation && gap != -37) {
            event.setCancelled(true);
        }
    }
}
