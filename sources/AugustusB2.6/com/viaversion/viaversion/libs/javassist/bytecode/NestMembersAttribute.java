// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist.bytecode;

import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

public class NestMembersAttribute extends AttributeInfo
{
    public static final String tag = "NestMembers";
    
    NestMembersAttribute(final ConstPool cp, final int n, final DataInputStream in) throws IOException {
        super(cp, n, in);
    }
    
    private NestMembersAttribute(final ConstPool cp, final byte[] info) {
        super(cp, "NestMembers", info);
    }
    
    @Override
    public AttributeInfo copy(final ConstPool newCp, final Map<String, String> classnames) {
        final byte[] src = this.get();
        final byte[] dest = new byte[src.length];
        final ConstPool cp = this.getConstPool();
        final int n = ByteArray.readU16bit(src, 0);
        ByteArray.write16bit(n, dest, 0);
        for (int i = 0, j = 2; i < n; ++i, j += 2) {
            final int index = ByteArray.readU16bit(src, j);
            final int newIndex = cp.copy(index, newCp, classnames);
            ByteArray.write16bit(newIndex, dest, j);
        }
        return new NestMembersAttribute(newCp, dest);
    }
    
    public int numberOfClasses() {
        return ByteArray.readU16bit(this.info, 0);
    }
    
    public int memberClass(final int index) {
        return ByteArray.readU16bit(this.info, index * 2 + 2);
    }
}
