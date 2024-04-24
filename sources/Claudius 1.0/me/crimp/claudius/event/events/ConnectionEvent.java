package me.crimp.claudius.event.events;

import me.crimp.claudius.event.EventStage;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

public class ConnectionEvent
        extends EventStage {
    private final UUID uuid;
    private final EntityPlayer entity;
    private final String name;

    public ConnectionEvent(int stage, UUID uuid, String name) {
        super(stage);
        this.uuid = uuid;
        this.name = name;
        this.entity = null;
    }
    Type type;


    public ConnectionEvent(int stage, EntityPlayer entity, UUID uuid, String name) {
        super(stage);
        this.entity = entity;
        this.uuid = uuid;
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        Join, Leave, Other
    }

    public String getName() {
        return this.name;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public EntityPlayer getEntity() {
        return this.entity;
    }
}

