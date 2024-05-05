/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.SPacketEntity
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketEntity;
import net.ccbluex.liquidbounce.api.minecraft.world.IWorld;
import net.ccbluex.liquidbounce.injection.backend.EntityImpl;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.ccbluex.liquidbounce.injection.backend.WorldImpl;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntity;

public final class SPacketEntityImpl<T extends SPacketEntity>
extends PacketImpl<T>
implements ISPacketEntity {
    @Override
    public boolean getOnGround() {
        return ((SPacketEntity)this.getWrapped()).func_179742_g();
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public IEntity getEntity(IWorld world) {
        IEntity iEntity;
        void $this$unwrap$iv;
        IWorld iWorld = world;
        SPacketEntity sPacketEntity = (SPacketEntity)this.getWrapped();
        boolean $i$f$unwrap = false;
        Object t = ((WorldImpl)$this$unwrap$iv).getWrapped();
        Entity entity = sPacketEntity.func_149065_a(t);
        if (entity != null) {
            Entity $this$wrap$iv = entity;
            boolean $i$f$wrap = false;
            iEntity = new EntityImpl<Entity>($this$wrap$iv);
        } else {
            iEntity = null;
        }
        return iEntity;
    }

    public SPacketEntityImpl(T wrapped) {
        super((Packet)wrapped);
    }
}

