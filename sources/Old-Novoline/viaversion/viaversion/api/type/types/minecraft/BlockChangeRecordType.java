package viaversion.viaversion.api.type.types.minecraft;

import io.netty.buffer.ByteBuf;
import viaversion.viaversion.api.minecraft.BlockChangeRecord;
import viaversion.viaversion.api.minecraft.BlockChangeRecord1_8;
import viaversion.viaversion.api.type.Type;

public class BlockChangeRecordType extends Type<BlockChangeRecord> {

    public BlockChangeRecordType() {
        super("BlockChangeRecord", BlockChangeRecord.class);
    }

    @Override
    public BlockChangeRecord read(ByteBuf buffer) throws Exception {
        short position = Type.SHORT.readPrimitive(buffer);
        int blockId = Type.VAR_INT.readPrimitive(buffer);
        return new BlockChangeRecord1_8(position >> 12 & 0xF, position & 0xFF, position >> 8 & 0xF, blockId);
    }

    @Override
    public void write(ByteBuf buffer, BlockChangeRecord object) throws Exception {
        Type.SHORT.writePrimitive(buffer, (short) (object.getSectionX() << 12 | object.getSectionZ() << 8 | object.getY()));
        Type.VAR_INT.writePrimitive(buffer, object.getBlockId());
    }
}
