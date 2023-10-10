package ravenweave.client.event.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.weavemc.loader.api.event.Event;

public class AttackEntityEvent extends Event {
    public Entity target;
    public EntityPlayer player;
    public Minecraft mc;

    public AttackEntityEvent(Minecraft mc, Entity targetEntity, EntityPlayer playerIn) {
        this.mc = mc;
        this.target = targetEntity;
        this.player = playerIn;

    }
}
