/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  io.netty.buffer.ByteBufInputStream
 *  io.netty.buffer.ByteBufOutputStream
 */
package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types;

import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.libs.opennbt.NBTIO;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class NBTType
extends Type<CompoundTag> {
    public NBTType() {
        super(CompoundTag.class);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public CompoundTag read(ByteBuf buffer) {
        short length = buffer.readShort();
        if (length < 0) {
            return null;
        }
        ByteBufInputStream byteBufInputStream = new ByteBufInputStream(buffer);
        DataInputStream dataInputStream = new DataInputStream((InputStream)byteBufInputStream);
        try {
            CompoundTag compoundTag = NBTIO.readTag(dataInputStream);
            return compoundTag;
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        finally {
            try {
                dataInputStream.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void write(ByteBuf buffer, CompoundTag nbt) throws Exception {
        if (nbt == null) {
            buffer.writeShort(-1);
        } else {
            ByteBuf buf = buffer.alloc().buffer();
            ByteBufOutputStream bytebufStream = new ByteBufOutputStream(buf);
            DataOutputStream dataOutputStream = new DataOutputStream((OutputStream)bytebufStream);
            NBTIO.writeTag(dataOutputStream, nbt);
            dataOutputStream.close();
            buffer.writeShort(buf.readableBytes());
            buffer.writeBytes(buf);
            buf.release();
        }
    }
}

