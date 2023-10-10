// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist.bytecode.analysis;

import java.util.Iterator;
import java.util.HashMap;
import com.viaversion.viaversion.libs.javassist.CtClass;
import java.util.Map;

public class MultiType extends Type
{
    private Map<String, CtClass> interfaces;
    private Type resolved;
    private Type potentialClass;
    private MultiType mergeSource;
    private boolean changed;
    
    public MultiType(final Map<String, CtClass> interfaces) {
        this(interfaces, null);
    }
    
    public MultiType(final Map<String, CtClass> interfaces, final Type potentialClass) {
        super(null);
        this.changed = false;
        this.interfaces = interfaces;
        this.potentialClass = potentialClass;
    }
    
    @Override
    public CtClass getCtClass() {
        if (this.resolved != null) {
            return this.resolved.getCtClass();
        }
        return Type.OBJECT.getCtClass();
    }
    
    @Override
    public Type getComponent() {
        return null;
    }
    
    @Override
    public int getSize() {
        return 1;
    }
    
    @Override
    public boolean isArray() {
        return false;
    }
    
    @Override
    boolean popChanged() {
        final boolean changed = this.changed;
        this.changed = false;
        return changed;
    }
    
    @Override
    public boolean isAssignableFrom(final Type type) {
        throw new UnsupportedOperationException("Not implemented");
    }
    
    public boolean isAssignableTo(final Type type) {
        if (this.resolved != null) {
            return type.isAssignableFrom(this.resolved);
        }
        if (Type.OBJECT.equals(type)) {
            return true;
        }
        if (this.potentialClass != null && !type.isAssignableFrom(this.potentialClass)) {
            this.potentialClass = null;
        }
        final Map<String, CtClass> map = this.mergeMultiAndSingle(this, type);
        if (map.size() == 1 && this.potentialClass == null) {
            this.resolved = Type.get(map.values().iterator().next());
            this.propogateResolved();
            return true;
        }
        if (map.size() >= 1) {
            this.interfaces = map;
            this.propogateState();
            return true;
        }
        if (this.potentialClass != null) {
            this.resolved = this.potentialClass;
            this.propogateResolved();
            return true;
        }
        return false;
    }
    
    private void propogateState() {
        for (MultiType source = this.mergeSource; source != null; source = source.mergeSource) {
            source.interfaces = this.interfaces;
            source.potentialClass = this.potentialClass;
        }
    }
    
    private void propogateResolved() {
        for (MultiType source = this.mergeSource; source != null; source = source.mergeSource) {
            source.resolved = this.resolved;
        }
    }
    
    @Override
    public boolean isReference() {
        return true;
    }
    
    private Map<String, CtClass> getAllMultiInterfaces(final MultiType type) {
        final Map<String, CtClass> map = new HashMap<String, CtClass>();
        for (final CtClass intf : type.interfaces.values()) {
            map.put(intf.getName(), intf);
            this.getAllInterfaces(intf, map);
        }
        return map;
    }
    
    private Map<String, CtClass> mergeMultiInterfaces(final MultiType type1, final MultiType type2) {
        final Map<String, CtClass> map1 = this.getAllMultiInterfaces(type1);
        final Map<String, CtClass> map2 = this.getAllMultiInterfaces(type2);
        return this.findCommonInterfaces(map1, map2);
    }
    
    private Map<String, CtClass> mergeMultiAndSingle(final MultiType multi, final Type single) {
        final Map<String, CtClass> map1 = this.getAllMultiInterfaces(multi);
        final Map<String, CtClass> map2 = this.getAllInterfaces(single.getCtClass(), null);
        return this.findCommonInterfaces(map1, map2);
    }
    
    private boolean inMergeSource(MultiType source) {
        while (source != null) {
            if (source == this) {
                return true;
            }
            source = source.mergeSource;
        }
        return false;
    }
    
    @Override
    public Type merge(final Type type) {
        if (this == type) {
            return this;
        }
        if (type == MultiType.UNINIT) {
            return this;
        }
        if (type == MultiType.BOGUS) {
            return MultiType.BOGUS;
        }
        if (type == null) {
            return this;
        }
        if (this.resolved != null) {
            return this.resolved.merge(type);
        }
        if (this.potentialClass != null) {
            final Type mergePotential = this.potentialClass.merge(type);
            if (!mergePotential.equals(this.potentialClass) || mergePotential.popChanged()) {
                this.potentialClass = (Type.OBJECT.equals(mergePotential) ? null : mergePotential);
                this.changed = true;
            }
        }
        Map<String, CtClass> merged;
        if (type instanceof MultiType) {
            final MultiType multi = (MultiType)type;
            if (multi.resolved != null) {
                merged = this.mergeMultiAndSingle(this, multi.resolved);
            }
            else {
                merged = this.mergeMultiInterfaces(multi, this);
                if (!this.inMergeSource(multi)) {
                    this.mergeSource = multi;
                }
            }
        }
        else {
            merged = this.mergeMultiAndSingle(this, type);
        }
        if (merged.size() > 1 || (merged.size() == 1 && this.potentialClass != null)) {
            if (merged.size() != this.interfaces.size()) {
                this.changed = true;
            }
            else if (!this.changed) {
                for (final String key : merged.keySet()) {
                    if (!this.interfaces.containsKey(key)) {
                        this.changed = true;
                    }
                }
            }
            this.interfaces = merged;
            this.propogateState();
            return this;
        }
        if (merged.size() == 1) {
            this.resolved = Type.get(merged.values().iterator().next());
        }
        else if (this.potentialClass != null) {
            this.resolved = this.potentialClass;
        }
        else {
            this.resolved = MultiType.OBJECT;
        }
        this.propogateResolved();
        return this.resolved;
    }
    
    @Override
    public int hashCode() {
        if (this.resolved != null) {
            return this.resolved.hashCode();
        }
        return this.interfaces.keySet().hashCode();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof MultiType)) {
            return false;
        }
        final MultiType multi = (MultiType)o;
        if (this.resolved != null) {
            return this.resolved.equals(multi.resolved);
        }
        return multi.resolved == null && this.interfaces.keySet().equals(multi.interfaces.keySet());
    }
    
    @Override
    public String toString() {
        if (this.resolved != null) {
            return this.resolved.toString();
        }
        final StringBuffer buffer = new StringBuffer("{");
        for (final String key : this.interfaces.keySet()) {
            buffer.append(key).append(", ");
        }
        if (this.potentialClass != null) {
            buffer.append("*").append(this.potentialClass.toString());
        }
        else {
            buffer.setLength(buffer.length() - 2);
        }
        buffer.append("}");
        return buffer.toString();
    }
}
