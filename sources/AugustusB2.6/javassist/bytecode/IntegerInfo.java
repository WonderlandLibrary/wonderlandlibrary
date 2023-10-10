// 
// Decompiled by Procyon v0.5.36
// 

package javassist.bytecode;

import java.io.PrintWriter;
import java.io.DataOutputStream;
import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

class IntegerInfo extends ConstInfo
{
    static final int tag = 3;
    int value;
    
    public IntegerInfo(final int i) {
        this.value = i;
    }
    
    public IntegerInfo(final DataInputStream in) throws IOException {
        this.value = in.readInt();
    }
    
    public int getTag() {
        return 3;
    }
    
    public int copy(final ConstPool src, final ConstPool dest, final Map map) {
        return dest.addIntegerInfo(this.value);
    }
    
    public void write(final DataOutputStream out) throws IOException {
        out.writeByte(3);
        out.writeInt(this.value);
    }
    
    public void print(final PrintWriter out) {
        out.print("Integer ");
        out.println(this.value);
    }
}
