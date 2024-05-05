package net.minecraft.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

class EntityMinecartCommandBlock.1
extends CommandBlockLogic {
    EntityMinecartCommandBlock.1() {
    }

    public void updateCommand() {
        EntityMinecartCommandBlock.this.getDataWatcher().updateObject(23, (Object)this.getCommand());
        EntityMinecartCommandBlock.this.getDataWatcher().updateObject(24, (Object)IChatComponent.Serializer.componentToJson((IChatComponent)this.getLastOutput()));
    }

    public int func_145751_f() {
        return 1;
    }

    public void func_145757_a(ByteBuf p_145757_1_) {
        p_145757_1_.writeInt(EntityMinecartCommandBlock.this.getEntityId());
    }

    public BlockPos getPosition() {
        return new BlockPos(EntityMinecartCommandBlock.this.posX, EntityMinecartCommandBlock.this.posY + 0.5, EntityMinecartCommandBlock.this.posZ);
    }

    public Vec3 getPositionVector() {
        return new Vec3(EntityMinecartCommandBlock.this.posX, EntityMinecartCommandBlock.this.posY, EntityMinecartCommandBlock.this.posZ);
    }

    public World getEntityWorld() {
        return EntityMinecartCommandBlock.this.worldObj;
    }

    public Entity getCommandSenderEntity() {
        return EntityMinecartCommandBlock.this;
    }
}
