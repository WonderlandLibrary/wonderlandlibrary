// 
// Decompiled by Procyon v0.5.36
// 

package javassist.bytecode;

import java.io.PrintWriter;
import java.io.DataOutputStream;
import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

class DoubleInfo extends ConstInfo
{
    static final int tag = 6;
    double value;
    
    public DoubleInfo(final double d) {
        this.value = d;
    }
    
    public DoubleInfo(final DataInputStream in) throws IOException {
        this.value = in.readDouble();
    }
    
    public int getTag() {
        return 6;
    }
    
    public int copy(final ConstPool src, final ConstPool dest, final Map map) {
        return dest.addDoubleInfo(this.value);
    }
    
    public void write(final DataOutputStream out) throws IOException {
        out.writeByte(6);
        out.writeDouble(this.value);
    }
    
    public void print(final PrintWriter out) {
        out.print("Double ");
        out.println(this.value);
    }
}
