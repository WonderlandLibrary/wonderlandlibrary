package dev.zprestige.prestige.client.managers;

import dev.zprestige.prestige.api.interfaces.IRotatable;
import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.JumpEvent;
import dev.zprestige.prestige.client.event.impl.MoveEvent;
import dev.zprestige.prestige.client.event.impl.TickEvent;
import dev.zprestige.prestige.client.util.impl.Rotation;

import java.util.ArrayList;

public class RotationManager {
    public Rotation rotation;
    public ArrayList<IRotatable> rotations = new ArrayList();

    public RotationManager() {
        Prestige.Companion.getEventBus().registerListener(this);
    }


    @EventListener
    public void event(TickEvent event) {
        this.rotation = null;
        for (IRotatable iRotatable : this.rotations) {
            Rotation rotation = iRotatable.getRotation();
            if (rotation != null) {
                this.rotation = rotation;
            }
        }
    }

    @EventListener
    public void event(MoveEvent event) {
        if (this.rotation != null) {
            event.setYaw(this.rotation.getYaw());
            event.setPitch(this.rotation.getPitch());
        }
        event.setCancelled();
    }

    @EventListener
    public void event(JumpEvent event) {
        if (this.rotation != null) {
            Rotation rotation = this.rotation;
            event.setYaw(rotation.getYaw());
        }
        event.setCancelled();
    }

    public void addRotation(IRotatable iRotatable) {
        this.rotations.add(iRotatable);
    }

    public void removeRotation(IRotatable iRotatable) {
        this.rotations.remove(iRotatable);
    }
}
