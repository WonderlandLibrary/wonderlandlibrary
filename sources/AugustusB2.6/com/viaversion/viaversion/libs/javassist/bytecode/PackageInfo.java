// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist.bytecode;

import java.io.PrintWriter;
import java.io.DataOutputStream;
import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

class PackageInfo extends ConstInfo
{
    static final int tag = 20;
    int name;
    
    public PackageInfo(final int moduleName, final int index) {
        super(index);
        this.name = moduleName;
    }
    
    public PackageInfo(final DataInputStream in, final int index) throws IOException {
        super(index);
        this.name = in.readUnsignedShort();
    }
    
    @Override
    public int hashCode() {
        return this.name;
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof PackageInfo && ((PackageInfo)obj).name == this.name;
    }
    
    @Override
    public int getTag() {
        return 20;
    }
    
    public String getPackageName(final ConstPool cp) {
        return cp.getUtf8Info(this.name);
    }
    
    @Override
    public int copy(final ConstPool src, final ConstPool dest, final Map<String, String> map) {
        final String packageName = src.getUtf8Info(this.name);
        final int newName = dest.addUtf8Info(packageName);
        return dest.addModuleInfo(newName);
    }
    
    @Override
    public void write(final DataOutputStream out) throws IOException {
        out.writeByte(20);
        out.writeShort(this.name);
    }
    
    @Override
    public void print(final PrintWriter out) {
        out.print("Package #");
        out.println(this.name);
    }
}
