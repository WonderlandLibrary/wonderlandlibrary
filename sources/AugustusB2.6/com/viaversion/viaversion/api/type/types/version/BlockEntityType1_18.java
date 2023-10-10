// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types.version;

import com.viaversion.viaversion.api.minecraft.blockentity.BlockEntityImpl;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.minecraft.blockentity.BlockEntity;
import com.viaversion.viaversion.api.type.Type;

public class BlockEntityType1_18 extends Type<BlockEntity>
{
    public BlockEntityType1_18() {
        super(BlockEntity.class);
    }
    
    @Override
    public BlockEntity read(final ByteBuf buffer) throws Exception {
        final byte xz = buffer.readByte();
        final short y = buffer.readShort();
        final int typeId = Type.VAR_INT.readPrimitive(buffer);
        final CompoundTag tag = Type.NBT.read(buffer);
        return new BlockEntityImpl(xz, y, typeId, tag);
    }
    
    @Override
    public void write(final ByteBuf buffer, final BlockEntity entity) throws Exception {
        buffer.writeByte(entity.packedXZ());
        buffer.writeShort(entity.y());
        Type.VAR_INT.writePrimitive(buffer, entity.typeId());
        Type.NBT.write(buffer, entity.tag());
    }
}
