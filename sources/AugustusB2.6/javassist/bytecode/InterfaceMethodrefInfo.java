// 
// Decompiled by Procyon v0.5.36
// 

package javassist.bytecode;

import java.io.IOException;
import java.io.DataInputStream;

class InterfaceMethodrefInfo extends MemberrefInfo
{
    static final int tag = 11;
    
    public InterfaceMethodrefInfo(final int cindex, final int ntindex) {
        super(cindex, ntindex);
    }
    
    public InterfaceMethodrefInfo(final DataInputStream in) throws IOException {
        super(in);
    }
    
    public int getTag() {
        return 11;
    }
    
    public String getTagName() {
        return "Interface";
    }
    
    protected int copy2(final ConstPool dest, final int cindex, final int ntindex) {
        return dest.addInterfaceMethodrefInfo(cindex, ntindex);
    }
}
