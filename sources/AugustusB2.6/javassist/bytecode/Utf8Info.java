// 
// Decompiled by Procyon v0.5.36
// 

package javassist.bytecode;

import java.io.PrintWriter;
import java.io.DataOutputStream;
import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

class Utf8Info extends ConstInfo
{
    static final int tag = 1;
    String string;
    int index;
    
    public Utf8Info(final String utf8, final int i) {
        this.string = utf8;
        this.index = i;
    }
    
    public Utf8Info(final DataInputStream in, final int i) throws IOException {
        this.string = in.readUTF();
        this.index = i;
    }
    
    public int getTag() {
        return 1;
    }
    
    public int copy(final ConstPool src, final ConstPool dest, final Map map) {
        return dest.addUtf8Info(this.string);
    }
    
    public void write(final DataOutputStream out) throws IOException {
        out.writeByte(1);
        out.writeUTF(this.string);
    }
    
    public void print(final PrintWriter out) {
        out.print("UTF8 \"");
        out.print(this.string);
        out.println("\"");
    }
}
