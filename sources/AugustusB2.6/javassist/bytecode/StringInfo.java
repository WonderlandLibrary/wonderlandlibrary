// 
// Decompiled by Procyon v0.5.36
// 

package javassist.bytecode;

import java.io.PrintWriter;
import java.io.DataOutputStream;
import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

class StringInfo extends ConstInfo
{
    static final int tag = 8;
    int string;
    
    public StringInfo(final int str) {
        this.string = str;
    }
    
    public StringInfo(final DataInputStream in) throws IOException {
        this.string = in.readUnsignedShort();
    }
    
    public int getTag() {
        return 8;
    }
    
    public int copy(final ConstPool src, final ConstPool dest, final Map map) {
        return dest.addStringInfo(src.getUtf8Info(this.string));
    }
    
    public void write(final DataOutputStream out) throws IOException {
        out.writeByte(8);
        out.writeShort(this.string);
    }
    
    public void print(final PrintWriter out) {
        out.print("String #");
        out.println(this.string);
    }
}
