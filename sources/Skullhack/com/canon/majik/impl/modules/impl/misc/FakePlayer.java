package com.canon.majik.impl.modules.impl.misc;

import com.canon.majik.api.event.eventBus.EventListener;
import com.canon.majik.api.event.events.TickEvent;
import com.canon.majik.impl.modules.api.Category;
import com.canon.majik.impl.modules.api.Module;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityOtherPlayerMP;

import java.util.UUID;

public class FakePlayer extends Module {
    public FakePlayer(String name, Category category) {
        super(name, category);
    }

    EntityOtherPlayerMP fake_player;

    @Override
    public void onEnable() {
        if (mc.world == null || mc.player == null)
            return;

        fake_player = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString("12cbdfad-33b7-4c07-aeac-01766e609482"), "BigBot"));
        fake_player.copyLocationAndAnglesFrom(mc.player);
        fake_player.inventory = mc.player.inventory;
        fake_player.setHealth(36);
        mc.world.addEntityToWorld(-100, fake_player);
    }

    @EventListener
    public void onTick(TickEvent event) {
        if (mc.world == null || mc.player == null)
            return;
        if (fake_player != null && fake_player.getDistanceSq(mc.player) > (100 * 100))
            mc.world.removeEntityFromWorld(-100);
    }

    @Override
    public void onDisable() {
        if (fake_player != null)
            mc.world.removeEntityFromWorld(-100);
    }
}
