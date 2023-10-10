// 
// Decompiled by Procyon v0.5.36
// 

package javassist.bytecode;

import java.io.IOException;
import java.io.DataInputStream;

class MethodrefInfo extends MemberrefInfo
{
    static final int tag = 10;
    
    public MethodrefInfo(final int cindex, final int ntindex) {
        super(cindex, ntindex);
    }
    
    public MethodrefInfo(final DataInputStream in) throws IOException {
        super(in);
    }
    
    public int getTag() {
        return 10;
    }
    
    public String getTagName() {
        return "Method";
    }
    
    protected int copy2(final ConstPool dest, final int cindex, final int ntindex) {
        return dest.addMethodrefInfo(cindex, ntindex);
    }
}
