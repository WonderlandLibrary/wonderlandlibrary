package io.github.nevalackin.client.event.player;

import io.github.nevalackin.client.event.Event;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;

public final class PlayerPositionUpdateEvent implements Event {

    private final EntityPlayer player;
    private final Vec3 move;

    public PlayerPositionUpdateEvent(EntityPlayer player, Vec3 move) {
        this.player = player;
        this.move = move;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public Vec3 getMove() {
        return move;
    }
}
