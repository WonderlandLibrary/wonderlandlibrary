// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist.bytecode.annotation;

import java.io.IOException;
import com.viaversion.viaversion.libs.javassist.ClassPool;
import java.util.Set;
import java.util.Iterator;
import com.viaversion.viaversion.libs.javassist.NotFoundException;
import com.viaversion.viaversion.libs.javassist.CtMethod;
import java.util.LinkedHashMap;
import com.viaversion.viaversion.libs.javassist.CtClass;
import com.viaversion.viaversion.libs.javassist.bytecode.Descriptor;
import java.util.Map;
import com.viaversion.viaversion.libs.javassist.bytecode.ConstPool;

public class Annotation
{
    ConstPool pool;
    int typeIndex;
    Map<String, Pair> members;
    
    public Annotation(final int type, final ConstPool cp) {
        this.pool = cp;
        this.typeIndex = type;
        this.members = null;
    }
    
    public Annotation(final String typeName, final ConstPool cp) {
        this(cp.addUtf8Info(Descriptor.of(typeName)), cp);
    }
    
    public Annotation(final ConstPool cp, final CtClass clazz) throws NotFoundException {
        this(cp.addUtf8Info(Descriptor.of(clazz.getName())), cp);
        if (!clazz.isInterface()) {
            throw new RuntimeException("Only interfaces are allowed for Annotation creation.");
        }
        final CtMethod[] methods = clazz.getDeclaredMethods();
        if (methods.length > 0) {
            this.members = new LinkedHashMap<String, Pair>();
        }
        for (final CtMethod m : methods) {
            this.addMemberValue(m.getName(), createMemberValue(cp, m.getReturnType()));
        }
    }
    
    public static MemberValue createMemberValue(final ConstPool cp, final CtClass type) throws NotFoundException {
        if (type == CtClass.booleanType) {
            return new BooleanMemberValue(cp);
        }
        if (type == CtClass.byteType) {
            return new ByteMemberValue(cp);
        }
        if (type == CtClass.charType) {
            return new CharMemberValue(cp);
        }
        if (type == CtClass.shortType) {
            return new ShortMemberValue(cp);
        }
        if (type == CtClass.intType) {
            return new IntegerMemberValue(cp);
        }
        if (type == CtClass.longType) {
            return new LongMemberValue(cp);
        }
        if (type == CtClass.floatType) {
            return new FloatMemberValue(cp);
        }
        if (type == CtClass.doubleType) {
            return new DoubleMemberValue(cp);
        }
        if (type.getName().equals("java.lang.Class")) {
            return new ClassMemberValue(cp);
        }
        if (type.getName().equals("java.lang.String")) {
            return new StringMemberValue(cp);
        }
        if (type.isArray()) {
            final CtClass arrayType = type.getComponentType();
            final MemberValue member = createMemberValue(cp, arrayType);
            return new ArrayMemberValue(member, cp);
        }
        if (type.isInterface()) {
            final Annotation info = new Annotation(cp, type);
            return new AnnotationMemberValue(info, cp);
        }
        final EnumMemberValue emv = new EnumMemberValue(cp);
        emv.setType(type.getName());
        return emv;
    }
    
    public void addMemberValue(final int nameIndex, final MemberValue value) {
        final Pair p = new Pair();
        p.name = nameIndex;
        p.value = value;
        this.addMemberValue(p);
    }
    
    public void addMemberValue(final String name, final MemberValue value) {
        final Pair p = new Pair();
        p.name = this.pool.addUtf8Info(name);
        p.value = value;
        if (this.members == null) {
            this.members = new LinkedHashMap<String, Pair>();
        }
        this.members.put(name, p);
    }
    
    private void addMemberValue(final Pair pair) {
        final String name = this.pool.getUtf8Info(pair.name);
        if (this.members == null) {
            this.members = new LinkedHashMap<String, Pair>();
        }
        this.members.put(name, pair);
    }
    
    @Override
    public String toString() {
        final StringBuffer buf = new StringBuffer("@");
        buf.append(this.getTypeName());
        if (this.members != null) {
            buf.append("(");
            for (final String name : this.members.keySet()) {
                buf.append(name).append("=").append(this.getMemberValue(name)).append(", ");
            }
            buf.setLength(buf.length() - 2);
            buf.append(")");
        }
        return buf.toString();
    }
    
    public String getTypeName() {
        return Descriptor.toClassName(this.pool.getUtf8Info(this.typeIndex));
    }
    
    public Set<String> getMemberNames() {
        if (this.members == null) {
            return null;
        }
        return this.members.keySet();
    }
    
    public MemberValue getMemberValue(final String name) {
        if (this.members == null || this.members.get(name) == null) {
            return null;
        }
        return this.members.get(name).value;
    }
    
    public Object toAnnotationType(final ClassLoader cl, final ClassPool cp) throws ClassNotFoundException, NoSuchClassError {
        final Class<?> clazz = MemberValue.loadClass(cl, this.getTypeName());
        try {
            return AnnotationImpl.make(cl, clazz, cp, this);
        }
        catch (IllegalArgumentException e) {
            throw new ClassNotFoundException(clazz.getName(), e);
        }
        catch (IllegalAccessError e2) {
            throw new ClassNotFoundException(clazz.getName(), e2);
        }
    }
    
    public void write(final AnnotationsWriter writer) throws IOException {
        final String typeName = this.pool.getUtf8Info(this.typeIndex);
        if (this.members == null) {
            writer.annotation(typeName, 0);
            return;
        }
        writer.annotation(typeName, this.members.size());
        for (final Pair pair : this.members.values()) {
            writer.memberValuePair(pair.name);
            pair.value.write(writer);
        }
    }
    
    @Override
    public int hashCode() {
        return this.getTypeName().hashCode() + ((this.members == null) ? 0 : this.members.hashCode());
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof Annotation)) {
            return false;
        }
        final Annotation other = (Annotation)obj;
        if (!this.getTypeName().equals(other.getTypeName())) {
            return false;
        }
        final Map<String, Pair> otherMembers = other.members;
        if (this.members == otherMembers) {
            return true;
        }
        if (this.members == null) {
            return otherMembers == null;
        }
        return otherMembers != null && this.members.equals(otherMembers);
    }
    
    static class Pair
    {
        int name;
        MemberValue value;
    }
}
