// 
// Decompiled by Procyon v0.5.36
// 

package javassist.bytecode;

import java.io.PrintWriter;
import java.io.DataOutputStream;
import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

class LongInfo extends ConstInfo
{
    static final int tag = 5;
    long value;
    
    public LongInfo(final long l) {
        this.value = l;
    }
    
    public LongInfo(final DataInputStream in) throws IOException {
        this.value = in.readLong();
    }
    
    public int getTag() {
        return 5;
    }
    
    public int copy(final ConstPool src, final ConstPool dest, final Map map) {
        return dest.addLongInfo(this.value);
    }
    
    public void write(final DataOutputStream out) throws IOException {
        out.writeByte(5);
        out.writeLong(this.value);
    }
    
    public void print(final PrintWriter out) {
        out.print("Long ");
        out.println(this.value);
    }
}
