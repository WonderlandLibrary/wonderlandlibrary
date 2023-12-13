package com.ohare.client.module.modules.movement;

import java.awt.Color;

import com.ohare.client.event.events.player.SlowdownEvent;
import com.ohare.client.event.events.player.UpdateEvent;
import com.ohare.client.module.Module;
import com.ohare.client.utils.value.impl.BooleanValue;

import dorkbox.messageBus.annotations.Subscribe;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;


/**
 * made by oHare for HTB V4
 *
 * @since 4/22/2019
 **/
public class NoSlowdown extends Module {
    private BooleanValue vanilla = new BooleanValue("Vanilla", false);
    private BooleanValue items = new BooleanValue("Items", true);
    private BooleanValue sprint = new BooleanValue("Sprint", true);
    private BooleanValue water = new BooleanValue("Water", true);
    private BooleanValue soulsand = new BooleanValue("SoulSand", true);

    public NoSlowdown() {
        super("NoSlowdown", Category.MOVEMENT, new Color(102, 100, 100, 255).getRGB());
        setDescription("No slow down from blocking / eating");
        setRenderlabel("No Slowdown");
        addValues(vanilla, items, sprint, water, soulsand);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        setSuffix(vanilla.isEnabled() ? "Vanilla":"Packet");
        if (vanilla.getValue() || !mc.thePlayer.isBlocking() || !mc.thePlayer.isMoving()) return;
        if (event.isPre()) {
            if (mc.thePlayer.isBlocking() && (mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0)) {
                mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
        } else {
            if (mc.thePlayer.isBlocking() && (mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0)) {
                mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
            }
        }
    }


    @Subscribe
    public void onSlowDown(SlowdownEvent event) {
        switch (event.getType()) {
            case Item:
                if (items.getValue()) event.setCanceled(true);
                break;
            case Sprinting:
                if (sprint.getValue()) event.setCanceled(true);
                break;
            case SoulSand:
                if (soulsand.getValue()) event.setCanceled(true);
                break;
            case Water:
                if (water.getValue()) event.setCanceled(true);
                break;
        }
    }
}
