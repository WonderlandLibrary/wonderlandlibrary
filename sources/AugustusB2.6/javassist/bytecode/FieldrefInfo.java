// 
// Decompiled by Procyon v0.5.36
// 

package javassist.bytecode;

import java.io.IOException;
import java.io.DataInputStream;

class FieldrefInfo extends MemberrefInfo
{
    static final int tag = 9;
    
    public FieldrefInfo(final int cindex, final int ntindex) {
        super(cindex, ntindex);
    }
    
    public FieldrefInfo(final DataInputStream in) throws IOException {
        super(in);
    }
    
    public int getTag() {
        return 9;
    }
    
    public String getTagName() {
        return "Field";
    }
    
    protected int copy2(final ConstPool dest, final int cindex, final int ntindex) {
        return dest.addFieldrefInfo(cindex, ntindex);
    }
}
