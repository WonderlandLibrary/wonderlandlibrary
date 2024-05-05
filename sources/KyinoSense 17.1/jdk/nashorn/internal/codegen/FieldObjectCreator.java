/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.codegen;

import java.util.List;
import jdk.nashorn.internal.codegen.CodeGenerator;
import jdk.nashorn.internal.codegen.Compiler;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.codegen.MapTuple;
import jdk.nashorn.internal.codegen.MethodEmitter;
import jdk.nashorn.internal.codegen.ObjectClassGenerator;
import jdk.nashorn.internal.codegen.ObjectCreator;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.Symbol;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.arrays.ArrayIndex;

public abstract class FieldObjectCreator<T>
extends ObjectCreator<T> {
    private String fieldObjectClassName;
    private Class<? extends ScriptObject> fieldObjectClass;
    private int fieldCount;
    private int paddedFieldCount;
    private int paramCount;
    private final int callSiteFlags;
    private final boolean evalCode;

    FieldObjectCreator(CodeGenerator codegen, List<MapTuple<T>> tuples) {
        this(codegen, tuples, false, false);
    }

    FieldObjectCreator(CodeGenerator codegen, List<MapTuple<T>> tuples, boolean isScope, boolean hasArguments) {
        super(codegen, tuples, isScope, hasArguments);
        this.callSiteFlags = codegen.getCallSiteFlags();
        this.evalCode = codegen.isEvalCode();
        this.countFields();
        this.findClass();
    }

    @Override
    public void createObject(MethodEmitter method) {
        this.makeMap();
        String className = this.getClassName();
        assert (this.fieldObjectClass != null);
        method._new(this.fieldObjectClass).dup();
        this.loadMap(method);
        if (this.isScope()) {
            this.loadScope(method);
            if (this.hasArguments()) {
                method.loadCompilerConstant(CompilerConstants.ARGUMENTS);
                method.invoke(CompilerConstants.constructorNoLookup(className, PropertyMap.class, ScriptObject.class, CompilerConstants.ARGUMENTS.type()));
            } else {
                method.invoke(CompilerConstants.constructorNoLookup(className, PropertyMap.class, ScriptObject.class));
            }
        } else {
            method.invoke(CompilerConstants.constructorNoLookup(className, PropertyMap.class));
        }
    }

    @Override
    public void populateRange(MethodEmitter method, Type objectType, int objectSlot, int start, int end) {
        method.load(objectType, objectSlot);
        for (int i = start; i < end; ++i) {
            MapTuple tuple = (MapTuple)this.tuples.get(i);
            if (tuple.symbol == null || tuple.value == null) continue;
            int index = ArrayIndex.getArrayIndex(tuple.key);
            method.dup();
            if (!ArrayIndex.isValidArrayIndex(index)) {
                this.putField(method, tuple.key, tuple.symbol.getFieldIndex(), tuple);
            } else {
                this.putSlot(method, ArrayIndex.toLongIndex(index), tuple);
            }
            method.invalidateSpecialName(tuple.key);
        }
    }

    @Override
    protected PropertyMap makeMap() {
        assert (this.propertyMap == null) : "property map already initialized";
        this.propertyMap = this.newMapCreator(this.fieldObjectClass).makeFieldMap(this.hasArguments(), this.codegen.useDualFields(), this.fieldCount, this.paddedFieldCount, this.evalCode);
        return this.propertyMap;
    }

    private void putField(MethodEmitter method, String key, int fieldIndex, MapTuple<T> tuple) {
        Type fieldType = this.codegen.useDualFields() && tuple.isPrimitive() ? ObjectClassGenerator.PRIMITIVE_FIELD_TYPE : Type.OBJECT;
        String fieldClass = this.getClassName();
        String fieldName = ObjectClassGenerator.getFieldName(fieldIndex, fieldType);
        String fieldDesc = CompilerConstants.typeDescriptor(fieldType.getTypeClass());
        assert (fieldName.equals(ObjectClassGenerator.getFieldName(fieldIndex, ObjectClassGenerator.PRIMITIVE_FIELD_TYPE)) || fieldType.isObject()) : key + " object keys must store to L*-fields";
        assert (fieldName.equals(ObjectClassGenerator.getFieldName(fieldIndex, Type.OBJECT)) || fieldType.isPrimitive()) : key + " primitive keys must store to J*-fields";
        this.loadTuple(method, tuple, true);
        method.putField(fieldClass, fieldName, fieldDesc);
    }

    private void putSlot(MethodEmitter method, long index, MapTuple<T> tuple) {
        this.loadIndex(method, index);
        this.loadTuple(method, tuple, false);
        method.dynamicSetIndex(this.callSiteFlags);
    }

    private void findClass() {
        this.fieldObjectClassName = this.isScope() ? ObjectClassGenerator.getClassName(this.fieldCount, this.paramCount, this.codegen.useDualFields()) : ObjectClassGenerator.getClassName(this.paddedFieldCount, this.codegen.useDualFields());
        try {
            this.fieldObjectClass = Context.forStructureClass(Compiler.binaryName(this.fieldObjectClassName));
        }
        catch (ClassNotFoundException e) {
            throw new AssertionError((Object)"Nashorn has encountered an internal error.  Structure can not be created.");
        }
    }

    @Override
    protected Class<? extends ScriptObject> getAllocatorClass() {
        return this.fieldObjectClass;
    }

    String getClassName() {
        return this.fieldObjectClassName;
    }

    private void countFields() {
        for (MapTuple tuple : this.tuples) {
            Symbol symbol = tuple.symbol;
            if (symbol == null) continue;
            if (this.hasArguments() && symbol.isParam()) {
                symbol.setFieldIndex(this.paramCount++);
                continue;
            }
            if (ArrayIndex.isValidArrayIndex(ArrayIndex.getArrayIndex(tuple.key))) continue;
            symbol.setFieldIndex(this.fieldCount++);
        }
        this.paddedFieldCount = ObjectClassGenerator.getPaddedFieldCount(this.fieldCount);
    }
}

