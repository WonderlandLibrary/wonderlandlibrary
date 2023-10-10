// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist.bytecode;

import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

public class NestHostAttribute extends AttributeInfo
{
    public static final String tag = "NestHost";
    
    NestHostAttribute(final ConstPool cp, final int n, final DataInputStream in) throws IOException {
        super(cp, n, in);
    }
    
    private NestHostAttribute(final ConstPool cp, final int hostIndex) {
        super(cp, "NestHost", new byte[2]);
        ByteArray.write16bit(hostIndex, this.get(), 0);
    }
    
    @Override
    public AttributeInfo copy(final ConstPool newCp, final Map<String, String> classnames) {
        final int hostIndex = ByteArray.readU16bit(this.get(), 0);
        final int newHostIndex = this.getConstPool().copy(hostIndex, newCp, classnames);
        return new NestHostAttribute(newCp, newHostIndex);
    }
    
    public int hostClassIndex() {
        return ByteArray.readU16bit(this.info, 0);
    }
}
