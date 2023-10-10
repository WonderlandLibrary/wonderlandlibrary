// 
// Decompiled by Procyon v0.5.36
// 

package javassist.bytecode;

import java.io.PrintWriter;
import java.io.DataOutputStream;
import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

class ClassInfo extends ConstInfo
{
    static final int tag = 7;
    int name;
    int index;
    
    public ClassInfo(final int className, final int i) {
        this.name = className;
        this.index = i;
    }
    
    public ClassInfo(final DataInputStream in, final int i) throws IOException {
        this.name = in.readUnsignedShort();
        this.index = i;
    }
    
    public int getTag() {
        return 7;
    }
    
    public String getClassName(final ConstPool cp) {
        return cp.getUtf8Info(this.name);
    }
    
    public void renameClass(final ConstPool cp, final String oldName, final String newName) {
        final String nameStr = cp.getUtf8Info(this.name);
        if (nameStr.equals(oldName)) {
            this.name = cp.addUtf8Info(newName);
        }
        else if (nameStr.charAt(0) == '[') {
            final String nameStr2 = Descriptor.rename(nameStr, oldName, newName);
            if (nameStr != nameStr2) {
                this.name = cp.addUtf8Info(nameStr2);
            }
        }
    }
    
    public void renameClass(final ConstPool cp, final Map map) {
        final String oldName = cp.getUtf8Info(this.name);
        if (oldName.charAt(0) == '[') {
            final String newName = Descriptor.rename(oldName, map);
            if (oldName != newName) {
                this.name = cp.addUtf8Info(newName);
            }
        }
        else {
            final String newName = map.get(oldName);
            if (newName != null && !newName.equals(oldName)) {
                this.name = cp.addUtf8Info(newName);
            }
        }
    }
    
    public int copy(final ConstPool src, final ConstPool dest, final Map map) {
        String classname = src.getUtf8Info(this.name);
        if (map != null) {
            final String newname = map.get(classname);
            if (newname != null) {
                classname = newname;
            }
        }
        return dest.addClassInfo(classname);
    }
    
    public void write(final DataOutputStream out) throws IOException {
        out.writeByte(7);
        out.writeShort(this.name);
    }
    
    public void print(final PrintWriter out) {
        out.print("Class #");
        out.println(this.name);
    }
    
    void makeHashtable(final ConstPool cp) {
        final String name = Descriptor.toJavaName(this.getClassName(cp));
        cp.classes.put(name, this);
    }
}
