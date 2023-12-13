/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.TypeMapper;
import com.sun.jna.WString;
import java.util.Iterator;

public abstract class Union
extends Structure {
    private Structure.StructField activeField;
    Structure.StructField biggestField;

    protected Union() {
    }

    protected Union(Pointer p2) {
        super(p2);
    }

    protected Union(Pointer p2, int alignType) {
        super(p2, alignType);
    }

    protected Union(TypeMapper mapper) {
        super(mapper);
    }

    protected Union(Pointer p2, int alignType, TypeMapper mapper) {
        super(p2, alignType, mapper);
    }

    public void setType(Class type) {
        this.ensureAllocated();
        Iterator i2 = this.fields().values().iterator();
        while (i2.hasNext()) {
            Structure.StructField f2 = (Structure.StructField)i2.next();
            if (f2.type != type) continue;
            this.activeField = f2;
            return;
        }
        throw new IllegalArgumentException("No field of type " + type + " in " + this);
    }

    public void setType(String fieldName) {
        this.ensureAllocated();
        Structure.StructField f2 = (Structure.StructField)this.fields().get(fieldName);
        if (f2 == null) {
            throw new IllegalArgumentException("No field named " + fieldName + " in " + this);
        }
        this.activeField = f2;
    }

    public Object readField(String fieldName) {
        this.ensureAllocated();
        this.setType(fieldName);
        return super.readField(fieldName);
    }

    public void writeField(String fieldName) {
        this.ensureAllocated();
        this.setType(fieldName);
        super.writeField(fieldName);
    }

    public void writeField(String fieldName, Object value) {
        this.ensureAllocated();
        this.setType(fieldName);
        super.writeField(fieldName, value);
    }

    public Object getTypedValue(Class type) {
        this.ensureAllocated();
        Iterator i2 = this.fields().values().iterator();
        while (i2.hasNext()) {
            Structure.StructField f2 = (Structure.StructField)i2.next();
            if (f2.type != type) continue;
            this.activeField = f2;
            this.read();
            return this.getField(this.activeField);
        }
        throw new IllegalArgumentException("No field of type " + type + " in " + this);
    }

    public Object setTypedValue(Object object) {
        Structure.StructField f2 = this.findField(object.getClass());
        if (f2 != null) {
            this.activeField = f2;
            this.setField(f2, object);
            return this;
        }
        throw new IllegalArgumentException("No field of type " + object.getClass() + " in " + this);
    }

    private Structure.StructField findField(Class type) {
        this.ensureAllocated();
        Iterator i2 = this.fields().values().iterator();
        while (i2.hasNext()) {
            Structure.StructField f2 = (Structure.StructField)i2.next();
            if (!f2.type.isAssignableFrom(type)) continue;
            return f2;
        }
        return null;
    }

    void writeField(Structure.StructField field) {
        if (field == this.activeField) {
            super.writeField(field);
        }
    }

    Object readField(Structure.StructField field) {
        if (field == this.activeField || !Structure.class.isAssignableFrom(field.type) && !String.class.isAssignableFrom(field.type) && !WString.class.isAssignableFrom(field.type)) {
            return super.readField(field);
        }
        return null;
    }

    int calculateSize(boolean force, boolean avoidFFIType) {
        int size = super.calculateSize(force, avoidFFIType);
        if (size != -1) {
            int fsize = 0;
            Iterator i2 = this.fields().values().iterator();
            while (i2.hasNext()) {
                Structure.StructField f2 = (Structure.StructField)i2.next();
                f2.offset = 0;
                if (f2.size <= fsize && (f2.size != fsize || !(class$com$sun$jna$Structure == null ? Union.class$("com.sun.jna.Structure") : class$com$sun$jna$Structure).isAssignableFrom(f2.type))) continue;
                fsize = f2.size;
                this.biggestField = f2;
            }
            size = this.calculateAlignedSize(fsize);
            if (size > 0 && this instanceof Structure.ByValue && !avoidFFIType) {
                this.getTypeInfo();
            }
        }
        return size;
    }

    protected int getNativeAlignment(Class type, Object value, boolean isFirstElement) {
        return super.getNativeAlignment(type, value, true);
    }

    Pointer getTypeInfo() {
        if (this.biggestField == null) {
            return null;
        }
        return super.getTypeInfo();
    }
}

