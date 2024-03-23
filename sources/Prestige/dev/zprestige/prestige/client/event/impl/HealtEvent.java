package dev.zprestige.prestige.client.event.impl;

import dev.zprestige.prestige.client.event.Event;
import net.minecraft.entity.player.PlayerEntity;

public class HealtEvent
extends Event {
    public PlayerEntity player;
    public String text;

    public HealtEvent(PlayerEntity playerEntity, String string) {
        this.player = playerEntity;
        this.text = string;
    }

    public PlayerEntity getPlayer() {
        return this.player;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
