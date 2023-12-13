// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.types;

import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.type.Type;

public class VarLongType extends Type<Long>
{
    public static final VarLongType VAR_LONG;
    
    public VarLongType() {
        super("VarLong", Long.class);
    }
    
    @Override
    public Long read(final ByteBuf byteBuf) throws Exception {
        long i = 0L;
        int j = 0;
        byte b0;
        do {
            b0 = byteBuf.readByte();
            i |= (b0 & 0x7F) << j++ * 7;
            if (j > 10) {
                throw new RuntimeException("VarLong too big");
            }
        } while ((b0 & 0x80) == 0x80);
        return i;
    }
    
    @Override
    public void write(final ByteBuf byteBuf, Long i) throws Exception {
        while (((long)i & 0xFFFFFFFFFFFFFF80L) != 0x0L) {
            byteBuf.writeByte((int)((long)i & 0x7FL) | 0x80);
            i = (long)i >>> 7;
        }
        byteBuf.writeByte(i.intValue());
    }
    
    static {
        VAR_LONG = new VarLongType();
    }
}
