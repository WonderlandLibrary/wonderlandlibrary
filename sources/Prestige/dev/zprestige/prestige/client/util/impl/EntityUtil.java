package dev.zprestige.prestige.client.util.impl;

import dev.zprestige.prestige.client.util.MC;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;

public class EntityUtil implements MC {

    public static EntityUtil INSTANCE = new EntityUtil();

    public PlayerEntity getPlayer() {
        PlayerEntity playerEntity = null;
        for (PlayerEntity player : getMc().world.getPlayers()) {
            if (player.isAlive() && !player.equals(getMc().player)) {
                playerEntity = player;
            }
        }
        return playerEntity;
    }

    public boolean isUsingShield(PlayerEntity playerEntity) {
        return (playerEntity.getMainHandStack().getItem() == Items.SHIELD || playerEntity.getOffHandStack().getItem() == Items.SHIELD) && playerEntity.isUsingItem();
    }

    @Override
    public MinecraftClient getMc() {
        return MinecraftClient.getInstance();
    }
}