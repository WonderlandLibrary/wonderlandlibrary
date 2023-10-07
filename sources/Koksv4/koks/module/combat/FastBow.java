package koks.module.combat;

import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.manager.value.annotation.Value;
import koks.event.UpdateEvent;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "FastBow", description = "You can shoot fast", category = Module.Category.COMBAT)
public class FastBow extends Module {

    @Value(name = "Mode", modes = {"Vanilla", "Timer"})
    String mode = "Vanilla";

    @Value(name = "Strength", minimum =  5, maximum = 20)
    int strength = 20;

    @Value(name = "Timer", minimum = 1, maximum = 10)
    double timer = 5;

    @Override
    public boolean isVisible(koks.api.manager.value.Value<?> value, String name) {
        switch(name) {
            case "Strength":
                return mode.equalsIgnoreCase("Vanilla");
            case "Timer":
                return mode.equalsIgnoreCase("Timer");
        }
        return super.isVisible(value, name);
    }

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (event instanceof UpdateEvent) {
            switch (mode) {
                case "Vanilla":
                    if (getPlayer().isUsingItem() && getPlayer().getCurrentEquippedItem().getItem() instanceof ItemBow) {
                        for (int i = 0; i < strength; i++)
                            sendPacket(new C03PacketPlayer(false));
                        sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                        getPlayer().stopUsingItem();
                    }
                    break;
                case "Timer":
                    if (getPlayer().isUsingItem() && getPlayer().getCurrentEquippedItem().getItem() instanceof ItemBow) {
                        getTimer().timerSpeed = (float) timer;
                        int i = getPlayer().getCurrentEquippedItem().getMaxItemUseDuration() - getPlayer().getItemInUseCount();
                        if (i >= 18) {
                            getTimer().timerSpeed = 1.0F;
                            sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                            getPlayer().stopUsingItem();
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
