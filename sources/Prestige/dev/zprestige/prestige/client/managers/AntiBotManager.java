package dev.zprestige.prestige.client.managers;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.impl.BotEvent;
import dev.zprestige.prestige.client.util.MC;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;

public class AntiBotManager implements MC {
    public ArrayList<PlayerEntity> bots = new ArrayList<>();

    public AntiBotManager() {
        Prestige.Companion.getEventBus().registerListener(this);
    }

    public boolean isNotBot(Entity entity) {
        if (entity == this.getMc().player) {
            return false;
        }
        if (!(entity instanceof PlayerEntity) || !new BotEvent().invoke()) {
            return true;
        }
        return bots.contains(entity) && entity != this.getMc().player && entity.isAlive();
    }

    public void addBot(PlayerEntity playerEntity) {
        this.bots.add(playerEntity);
    }

    public ArrayList<PlayerEntity> getBots() {
        return this.bots;
    }

    @Override
    public MinecraftClient getMc() {
        return MinecraftClient.getInstance();
    }
}
