package koks.module.player;

import god.buddy.aot.BCompiler;
import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.manager.value.annotation.Value;
import koks.event.UpdateEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;

/**
 * Copyright 2021, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "FastUse", description = "You use items fast", category = Module.Category.PLAYER)
public class FastUse extends Module {

    @Value(name = "Packets", minimum = 1, maximum = 100)
    int packets = 15;

    @Value(name = "inAir")
    boolean inAir = false;

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (event instanceof UpdateEvent) {
            if (getPlayer().isEating() && getPlayer().getHeldItem() != null && getPlayer().getHeldItem().getItem() != null && !(getPlayer().getHeldItem().getItem() instanceof ItemSword)) {
                doFastUse(getPlayer().getHeldItem());
            }
        }
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public void doFastUse(ItemStack item) {

        if ((inAir || getPlayer().onGround))
            for (int i = 0; i < packets; i++)
                sendPacket(new C03PacketPlayer(true));
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
