// 
// Decompiled by Procyon v0.5.36
// 

package javassist.bytecode;

import java.io.PrintWriter;
import java.io.DataOutputStream;
import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

class FloatInfo extends ConstInfo
{
    static final int tag = 4;
    float value;
    
    public FloatInfo(final float f) {
        this.value = f;
    }
    
    public FloatInfo(final DataInputStream in) throws IOException {
        this.value = in.readFloat();
    }
    
    public int getTag() {
        return 4;
    }
    
    public int copy(final ConstPool src, final ConstPool dest, final Map map) {
        return dest.addFloatInfo(this.value);
    }
    
    public void write(final DataOutputStream out) throws IOException {
        out.writeByte(4);
        out.writeFloat(this.value);
    }
    
    public void print(final PrintWriter out) {
        out.print("Float ");
        out.println(this.value);
    }
}
