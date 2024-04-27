/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.SPacketEntity
 *  net.minecraft.world.World
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
import net.minecraft.world.World;

public final class SPacketEntityImpl
extends PacketImpl
implements ISPacketEntity {
    public SPacketEntityImpl(SPacketEntity sPacketEntity) {
        super((Packet)sPacketEntity);
    }

    @Override
    public boolean getOnGround() {
        return ((SPacketEntity)this.getWrapped()).func_179742_g();
    }

    @Override
    public IEntity getEntity(IWorld iWorld) {
        IEntity iEntity;
        IWorld iWorld2 = iWorld;
        SPacketEntity sPacketEntity = (SPacketEntity)this.getWrapped();
        boolean bl = false;
        World world = ((WorldImpl)iWorld2).getWrapped();
        Entity entity = sPacketEntity.func_149065_a(world);
        if (entity != null) {
            iWorld2 = entity;
            bl = false;
            iEntity = new EntityImpl((Entity)iWorld2);
        } else {
            iEntity = null;
        }
        return iEntity;
    }
}

