// 
// Decompiled by Procyon v0.5.36
// 

package javassist.bytecode;

import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.DataOutputStream;
import java.util.Map;

abstract class ConstInfo
{
    public abstract int getTag();
    
    public String getClassName(final ConstPool cp) {
        return null;
    }
    
    public void renameClass(final ConstPool cp, final String oldName, final String newName) {
    }
    
    public void renameClass(final ConstPool cp, final Map classnames) {
    }
    
    public abstract int copy(final ConstPool p0, final ConstPool p1, final Map p2);
    
    public abstract void write(final DataOutputStream p0) throws IOException;
    
    public abstract void print(final PrintWriter p0);
    
    void makeHashtable(final ConstPool cp) {
    }
    
    boolean hashCheck(final int a, final int b) {
        return false;
    }
    
    public String toString() {
        final ByteArrayOutputStream bout = new ByteArrayOutputStream();
        final PrintWriter out = new PrintWriter(bout);
        this.print(out);
        return bout.toString();
    }
}
