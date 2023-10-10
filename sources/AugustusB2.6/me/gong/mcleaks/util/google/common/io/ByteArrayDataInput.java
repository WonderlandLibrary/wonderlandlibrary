// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.io;

import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import java.io.DataInput;

@GwtIncompatible
public interface ByteArrayDataInput extends DataInput
{
    void readFully(final byte[] p0);
    
    void readFully(final byte[] p0, final int p1, final int p2);
    
    int skipBytes(final int p0);
    
    @CanIgnoreReturnValue
    boolean readBoolean();
    
    @CanIgnoreReturnValue
    byte readByte();
    
    @CanIgnoreReturnValue
    int readUnsignedByte();
    
    @CanIgnoreReturnValue
    short readShort();
    
    @CanIgnoreReturnValue
    int readUnsignedShort();
    
    @CanIgnoreReturnValue
    char readChar();
    
    @CanIgnoreReturnValue
    int readInt();
    
    @CanIgnoreReturnValue
    long readLong();
    
    @CanIgnoreReturnValue
    float readFloat();
    
    @CanIgnoreReturnValue
    double readDouble();
    
    @CanIgnoreReturnValue
    String readLine();
    
    @CanIgnoreReturnValue
    String readUTF();
}
