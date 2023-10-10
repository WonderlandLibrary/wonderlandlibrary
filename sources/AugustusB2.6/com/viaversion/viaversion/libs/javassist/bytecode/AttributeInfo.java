// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist.bytecode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.DataOutputStream;
import java.util.Arrays;
import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

public class AttributeInfo
{
    protected ConstPool constPool;
    int name;
    byte[] info;
    
    protected AttributeInfo(final ConstPool cp, final int attrname, final byte[] attrinfo) {
        this.constPool = cp;
        this.name = attrname;
        this.info = attrinfo;
    }
    
    protected AttributeInfo(final ConstPool cp, final String attrname) {
        this(cp, attrname, null);
    }
    
    public AttributeInfo(final ConstPool cp, final String attrname, final byte[] attrinfo) {
        this(cp, cp.addUtf8Info(attrname), attrinfo);
    }
    
    protected AttributeInfo(final ConstPool cp, final int n, final DataInputStream in) throws IOException {
        this.constPool = cp;
        this.name = n;
        final int len = in.readInt();
        this.info = new byte[len];
        if (len > 0) {
            in.readFully(this.info);
        }
    }
    
    static AttributeInfo read(final ConstPool cp, final DataInputStream in) throws IOException {
        final int name = in.readUnsignedShort();
        final String nameStr = cp.getUtf8Info(name);
        final char first = nameStr.charAt(0);
        if (first < 'E') {
            if (nameStr.equals("AnnotationDefault")) {
                return new AnnotationDefaultAttribute(cp, name, in);
            }
            if (nameStr.equals("BootstrapMethods")) {
                return new BootstrapMethodsAttribute(cp, name, in);
            }
            if (nameStr.equals("Code")) {
                return new CodeAttribute(cp, name, in);
            }
            if (nameStr.equals("ConstantValue")) {
                return new ConstantAttribute(cp, name, in);
            }
            if (nameStr.equals("Deprecated")) {
                return new DeprecatedAttribute(cp, name, in);
            }
        }
        if (first < 'M') {
            if (nameStr.equals("EnclosingMethod")) {
                return new EnclosingMethodAttribute(cp, name, in);
            }
            if (nameStr.equals("Exceptions")) {
                return new ExceptionsAttribute(cp, name, in);
            }
            if (nameStr.equals("InnerClasses")) {
                return new InnerClassesAttribute(cp, name, in);
            }
            if (nameStr.equals("LineNumberTable")) {
                return new LineNumberAttribute(cp, name, in);
            }
            if (nameStr.equals("LocalVariableTable")) {
                return new LocalVariableAttribute(cp, name, in);
            }
            if (nameStr.equals("LocalVariableTypeTable")) {
                return new LocalVariableTypeAttribute(cp, name, in);
            }
        }
        if (first < 'S') {
            if (nameStr.equals("MethodParameters")) {
                return new MethodParametersAttribute(cp, name, in);
            }
            if (nameStr.equals("NestHost")) {
                return new NestHostAttribute(cp, name, in);
            }
            if (nameStr.equals("NestMembers")) {
                return new NestMembersAttribute(cp, name, in);
            }
            if (nameStr.equals("RuntimeVisibleAnnotations") || nameStr.equals("RuntimeInvisibleAnnotations")) {
                return new AnnotationsAttribute(cp, name, in);
            }
            if (nameStr.equals("RuntimeVisibleParameterAnnotations") || nameStr.equals("RuntimeInvisibleParameterAnnotations")) {
                return new ParameterAnnotationsAttribute(cp, name, in);
            }
            if (nameStr.equals("RuntimeVisibleTypeAnnotations") || nameStr.equals("RuntimeInvisibleTypeAnnotations")) {
                return new TypeAnnotationsAttribute(cp, name, in);
            }
        }
        if (first >= 'S') {
            if (nameStr.equals("Signature")) {
                return new SignatureAttribute(cp, name, in);
            }
            if (nameStr.equals("SourceFile")) {
                return new SourceFileAttribute(cp, name, in);
            }
            if (nameStr.equals("Synthetic")) {
                return new SyntheticAttribute(cp, name, in);
            }
            if (nameStr.equals("StackMap")) {
                return new StackMap(cp, name, in);
            }
            if (nameStr.equals("StackMapTable")) {
                return new StackMapTable(cp, name, in);
            }
        }
        return new AttributeInfo(cp, name, in);
    }
    
    public String getName() {
        return this.constPool.getUtf8Info(this.name);
    }
    
    public ConstPool getConstPool() {
        return this.constPool;
    }
    
    public int length() {
        return this.info.length + 6;
    }
    
    public byte[] get() {
        return this.info;
    }
    
    public void set(final byte[] newinfo) {
        this.info = newinfo;
    }
    
    public AttributeInfo copy(final ConstPool newCp, final Map<String, String> classnames) {
        return new AttributeInfo(newCp, this.getName(), Arrays.copyOf(this.info, this.info.length));
    }
    
    void write(final DataOutputStream out) throws IOException {
        out.writeShort(this.name);
        out.writeInt(this.info.length);
        if (this.info.length > 0) {
            out.write(this.info);
        }
    }
    
    static int getLength(final List<AttributeInfo> attributes) {
        int size = 0;
        for (final AttributeInfo attr : attributes) {
            size += attr.length();
        }
        return size;
    }
    
    static AttributeInfo lookup(final List<AttributeInfo> attributes, final String name) {
        if (attributes == null) {
            return null;
        }
        for (final AttributeInfo ai : attributes) {
            if (ai.getName().equals(name)) {
                return ai;
            }
        }
        return null;
    }
    
    static synchronized AttributeInfo remove(final List<AttributeInfo> attributes, final String name) {
        if (attributes == null) {
            return null;
        }
        for (final AttributeInfo ai : attributes) {
            if (ai.getName().equals(name) && attributes.remove(ai)) {
                return ai;
            }
        }
        return null;
    }
    
    static void writeAll(final List<AttributeInfo> attributes, final DataOutputStream out) throws IOException {
        if (attributes == null) {
            return;
        }
        for (final AttributeInfo attr : attributes) {
            attr.write(out);
        }
    }
    
    static List<AttributeInfo> copyAll(final List<AttributeInfo> attributes, final ConstPool cp) {
        if (attributes == null) {
            return null;
        }
        final List<AttributeInfo> newList = new ArrayList<AttributeInfo>();
        for (final AttributeInfo attr : attributes) {
            newList.add(attr.copy(cp, null));
        }
        return newList;
    }
    
    void renameClass(final String oldname, final String newname) {
    }
    
    void renameClass(final Map<String, String> classnames) {
    }
    
    static void renameClass(final List<AttributeInfo> attributes, final String oldname, final String newname) {
        if (attributes == null) {
            return;
        }
        for (final AttributeInfo ai : attributes) {
            ai.renameClass(oldname, newname);
        }
    }
    
    static void renameClass(final List<AttributeInfo> attributes, final Map<String, String> classnames) {
        if (attributes == null) {
            return;
        }
        for (final AttributeInfo ai : attributes) {
            ai.renameClass(classnames);
        }
    }
    
    void getRefClasses(final Map<String, String> classnames) {
    }
    
    static void getRefClasses(final List<AttributeInfo> attributes, final Map<String, String> classnames) {
        if (attributes == null) {
            return;
        }
        for (final AttributeInfo ai : attributes) {
            ai.getRefClasses(classnames);
        }
    }
}
