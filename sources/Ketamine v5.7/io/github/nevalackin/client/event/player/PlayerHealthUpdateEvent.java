package io.github.nevalackin.client.event.player;

import io.github.nevalackin.client.event.Event;
import net.minecraft.entity.player.EntityPlayer;

public final class PlayerHealthUpdateEvent implements Event {

    private final EntityPlayer player;
    private final float health, prevHealth;

    public PlayerHealthUpdateEvent(EntityPlayer player, float health, float prevHealth) {
        this.player = player;
        this.health = health;
        this.prevHealth = prevHealth;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public float getHealth() {
        return health;
    }

    public float getPrevHealth() {
        return prevHealth;
    }
}
