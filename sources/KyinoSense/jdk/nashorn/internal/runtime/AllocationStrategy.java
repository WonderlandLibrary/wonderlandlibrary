/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.io.Serializable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.ref.WeakReference;
import jdk.nashorn.internal.codegen.Compiler;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.codegen.ObjectClassGenerator;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.SharedPropertyMap;

public final class AllocationStrategy
implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
    private final int fieldCount;
    private final boolean dualFields;
    private transient String allocatorClassName;
    private transient MethodHandle allocator;
    private transient AllocatorMap lastMap;

    public AllocationStrategy(int fieldCount, boolean dualFields) {
        this.fieldCount = fieldCount;
        this.dualFields = dualFields;
    }

    private String getAllocatorClassName() {
        if (this.allocatorClassName == null) {
            this.allocatorClassName = Compiler.binaryName(ObjectClassGenerator.getClassName(this.fieldCount, this.dualFields)).intern();
        }
        return this.allocatorClassName;
    }

    synchronized PropertyMap getAllocatorMap(ScriptObject prototype) {
        assert (prototype != null);
        PropertyMap protoMap = prototype.getMap();
        if (this.lastMap != null) {
            if (!this.lastMap.hasSharedProtoMap()) {
                if (this.lastMap.hasSamePrototype(prototype)) {
                    return this.lastMap.allocatorMap;
                }
                if (this.lastMap.hasSameProtoMap(protoMap) && this.lastMap.hasUnchangedProtoMap()) {
                    PropertyMap allocatorMap = PropertyMap.newMap(null, this.getAllocatorClassName(), 0, this.fieldCount, 0);
                    SharedPropertyMap sharedProtoMap = new SharedPropertyMap(protoMap);
                    allocatorMap.setSharedProtoMap(sharedProtoMap);
                    prototype.setMap(sharedProtoMap);
                    this.lastMap = new AllocatorMap(prototype, protoMap, allocatorMap);
                    return allocatorMap;
                }
            }
            if (this.lastMap.hasValidSharedProtoMap() && this.lastMap.hasSameProtoMap(protoMap)) {
                prototype.setMap(this.lastMap.getSharedProtoMap());
                return this.lastMap.allocatorMap;
            }
        }
        PropertyMap allocatorMap = PropertyMap.newMap(null, this.getAllocatorClassName(), 0, this.fieldCount, 0);
        this.lastMap = new AllocatorMap(prototype, protoMap, allocatorMap);
        return allocatorMap;
    }

    ScriptObject allocate(PropertyMap map) {
        try {
            if (this.allocator == null) {
                this.allocator = Lookup.MH.findStatic(LOOKUP, Context.forStructureClass(this.getAllocatorClassName()), CompilerConstants.ALLOCATE.symbolName(), Lookup.MH.type(ScriptObject.class, PropertyMap.class));
            }
            return this.allocator.invokeExact(map);
        }
        catch (Error | RuntimeException e) {
            throw e;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public String toString() {
        return "AllocationStrategy[fieldCount=" + this.fieldCount + "]";
    }

    static class AllocatorMap {
        private final WeakReference<ScriptObject> prototype;
        private final WeakReference<PropertyMap> prototypeMap;
        private PropertyMap allocatorMap;

        AllocatorMap(ScriptObject prototype, PropertyMap protoMap, PropertyMap allocMap) {
            this.prototype = new WeakReference<ScriptObject>(prototype);
            this.prototypeMap = new WeakReference<PropertyMap>(protoMap);
            this.allocatorMap = allocMap;
        }

        boolean hasSamePrototype(ScriptObject proto) {
            return this.prototype.get() == proto;
        }

        boolean hasSameProtoMap(PropertyMap protoMap) {
            return this.prototypeMap.get() == protoMap || this.allocatorMap.getSharedProtoMap() == protoMap;
        }

        boolean hasUnchangedProtoMap() {
            ScriptObject proto = (ScriptObject)this.prototype.get();
            return proto != null && proto.getMap() == this.prototypeMap.get();
        }

        boolean hasSharedProtoMap() {
            return this.getSharedProtoMap() != null;
        }

        boolean hasValidSharedProtoMap() {
            return this.hasSharedProtoMap() && this.getSharedProtoMap().isValidSharedProtoMap();
        }

        PropertyMap getSharedProtoMap() {
            return this.allocatorMap.getSharedProtoMap();
        }
    }
}

