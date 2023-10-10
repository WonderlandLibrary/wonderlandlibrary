// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist.bytecode;

import java.io.PrintWriter;
import java.io.DataOutputStream;
import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

class ModuleInfo extends ConstInfo
{
    static final int tag = 19;
    int name;
    
    public ModuleInfo(final int moduleName, final int index) {
        super(index);
        this.name = moduleName;
    }
    
    public ModuleInfo(final DataInputStream in, final int index) throws IOException {
        super(index);
        this.name = in.readUnsignedShort();
    }
    
    @Override
    public int hashCode() {
        return this.name;
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof ModuleInfo && ((ModuleInfo)obj).name == this.name;
    }
    
    @Override
    public int getTag() {
        return 19;
    }
    
    public String getModuleName(final ConstPool cp) {
        return cp.getUtf8Info(this.name);
    }
    
    @Override
    public int copy(final ConstPool src, final ConstPool dest, final Map<String, String> map) {
        final String moduleName = src.getUtf8Info(this.name);
        final int newName = dest.addUtf8Info(moduleName);
        return dest.addModuleInfo(newName);
    }
    
    @Override
    public void write(final DataOutputStream out) throws IOException {
        out.writeByte(19);
        out.writeShort(this.name);
    }
    
    @Override
    public void print(final PrintWriter out) {
        out.print("Module #");
        out.println(this.name);
    }
}
