// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist.bytecode;

import java.io.PrintWriter;
import java.io.DataOutputStream;
import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

class DynamicInfo extends ConstInfo
{
    static final int tag = 17;
    int bootstrap;
    int nameAndType;
    
    public DynamicInfo(final int bootstrapMethod, final int ntIndex, final int index) {
        super(index);
        this.bootstrap = bootstrapMethod;
        this.nameAndType = ntIndex;
    }
    
    public DynamicInfo(final DataInputStream in, final int index) throws IOException {
        super(index);
        this.bootstrap = in.readUnsignedShort();
        this.nameAndType = in.readUnsignedShort();
    }
    
    @Override
    public int hashCode() {
        return this.bootstrap << 16 ^ this.nameAndType;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof DynamicInfo) {
            final DynamicInfo iv = (DynamicInfo)obj;
            return iv.bootstrap == this.bootstrap && iv.nameAndType == this.nameAndType;
        }
        return false;
    }
    
    @Override
    public int getTag() {
        return 17;
    }
    
    @Override
    public int copy(final ConstPool src, final ConstPool dest, final Map<String, String> map) {
        return dest.addDynamicInfo(this.bootstrap, src.getItem(this.nameAndType).copy(src, dest, map));
    }
    
    @Override
    public void write(final DataOutputStream out) throws IOException {
        out.writeByte(17);
        out.writeShort(this.bootstrap);
        out.writeShort(this.nameAndType);
    }
    
    @Override
    public void print(final PrintWriter out) {
        out.print("Dynamic #");
        out.print(this.bootstrap);
        out.print(", name&type #");
        out.println(this.nameAndType);
    }
}
