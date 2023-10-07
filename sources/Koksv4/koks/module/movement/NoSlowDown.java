package koks.module.movement;

import god.buddy.aot.BCompiler;
import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.manager.value.annotation.Value;
import koks.event.AttackEvent;
import koks.event.UpdateEvent;
import koks.event.UpdateMotionEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "NoSlowDown", description = "", category = Module.Category.MOVEMENT)
public class NoSlowDown extends Module {

    @Value(name = "Speed", minimum = 0.1, maximum = 1)
    double speed = 1F;

    @Value(name = "CanSprint")
    boolean canSprint = true;

    @Value(name = "OnlySword")
    boolean onlySword = false;

    @Value(name = "Spoofing")
    boolean spoof = false;

    @Value(name = "Spoof-Mode", displayName = "Mode", modes = {"NCP", "AAC4", "AAC3", "Intave13"})
    String spoofMode = "NCP";

    boolean hasUsed;

    @Override
    public boolean isVisible(koks.api.manager.value.Value<?> value, String name) {
        switch (name) {
            case "Spoof-Mode":
                return spoof;
        }
        return super.isVisible(value, name);
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (!onlySword || (getPlayer().getHeldItem() != null && getPlayer().getHeldItem().getItem() instanceof ItemSword)) {
            if (event instanceof final UpdateMotionEvent motionEvent) {
                if (spoof && spoofMode.equalsIgnoreCase("NCP") && getPlayer().inventory != null && getPlayer().inventory.getCurrentItem() != null && getPlayer().isUsingItem()) {
                    if (motionEvent.getType() == UpdateMotionEvent.Type.PRE) {
                        sendPacketUnlogged(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    } else {
                        sendPacketUnlogged(new C08PacketPlayerBlockPlacement(getPlayer().inventory.getCurrentItem()));
                    }
                }
            }

            if (event instanceof UpdateEvent) {
                if (spoof && getPlayer().isUsingItem())
                    switch (spoofMode) {
                        case "Intave13" -> {
                            final int curSlot = getPlayer().inventory.currentItem;
                            final int spoof = curSlot == 0 ? 1 : -1;
                            sendPacketUnlogged(new C09PacketHeldItemChange(curSlot + spoof));
                            sendPacketUnlogged(new C09PacketHeldItemChange(curSlot));
                        }
                        case "AAC4" -> sendPacketUnlogged(new C09PacketHeldItemChange(getPlayer().inventory.currentItem));
                        case "AAC3" -> sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    }
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        hasUsed = false;
    }
}
