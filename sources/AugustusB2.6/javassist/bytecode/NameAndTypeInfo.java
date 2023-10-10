// 
// Decompiled by Procyon v0.5.36
// 

package javassist.bytecode;

import java.io.PrintWriter;
import java.io.DataOutputStream;
import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

class NameAndTypeInfo extends ConstInfo
{
    static final int tag = 12;
    int memberName;
    int typeDescriptor;
    
    public NameAndTypeInfo(final int name, final int type) {
        this.memberName = name;
        this.typeDescriptor = type;
    }
    
    public NameAndTypeInfo(final DataInputStream in) throws IOException {
        this.memberName = in.readUnsignedShort();
        this.typeDescriptor = in.readUnsignedShort();
    }
    
    boolean hashCheck(final int a, final int b) {
        return a == this.memberName && b == this.typeDescriptor;
    }
    
    public int getTag() {
        return 12;
    }
    
    public void renameClass(final ConstPool cp, final String oldName, final String newName) {
        final String type = cp.getUtf8Info(this.typeDescriptor);
        final String type2 = Descriptor.rename(type, oldName, newName);
        if (type != type2) {
            this.typeDescriptor = cp.addUtf8Info(type2);
        }
    }
    
    public void renameClass(final ConstPool cp, final Map map) {
        final String type = cp.getUtf8Info(this.typeDescriptor);
        final String type2 = Descriptor.rename(type, map);
        if (type != type2) {
            this.typeDescriptor = cp.addUtf8Info(type2);
        }
    }
    
    public int copy(final ConstPool src, final ConstPool dest, final Map map) {
        final String mname = src.getUtf8Info(this.memberName);
        String tdesc = src.getUtf8Info(this.typeDescriptor);
        tdesc = Descriptor.rename(tdesc, map);
        return dest.addNameAndTypeInfo(dest.addUtf8Info(mname), dest.addUtf8Info(tdesc));
    }
    
    public void write(final DataOutputStream out) throws IOException {
        out.writeByte(12);
        out.writeShort(this.memberName);
        out.writeShort(this.typeDescriptor);
    }
    
    public void print(final PrintWriter out) {
        out.print("NameAndType #");
        out.print(this.memberName);
        out.print(", type #");
        out.println(this.typeDescriptor);
    }
}
