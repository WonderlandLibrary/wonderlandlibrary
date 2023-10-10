// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna;

import java.util.Iterator;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class Union extends Structure
{
    private StructField activeField;
    
    protected Union() {
    }
    
    protected Union(final Pointer p) {
        super(p);
    }
    
    protected Union(final Pointer p, final int alignType) {
        super(p, alignType);
    }
    
    protected Union(final TypeMapper mapper) {
        super(mapper);
    }
    
    protected Union(final Pointer p, final int alignType, final TypeMapper mapper) {
        super(p, alignType, mapper);
    }
    
    @Override
    protected List<String> getFieldOrder() {
        final List<Field> flist = this.getFieldList();
        final List<String> list = new ArrayList<String>(flist.size());
        for (final Field f : flist) {
            list.add(f.getName());
        }
        return list;
    }
    
    public void setType(final Class<?> type) {
        this.ensureAllocated();
        for (final StructField f : this.fields().values()) {
            if (f.type == type) {
                this.activeField = f;
                return;
            }
        }
        throw new IllegalArgumentException("No field of type " + type + " in " + this);
    }
    
    public void setType(final String fieldName) {
        this.ensureAllocated();
        final StructField f = this.fields().get(fieldName);
        if (f != null) {
            this.activeField = f;
            return;
        }
        throw new IllegalArgumentException("No field named " + fieldName + " in " + this);
    }
    
    @Override
    public Object readField(final String fieldName) {
        this.ensureAllocated();
        this.setType(fieldName);
        return super.readField(fieldName);
    }
    
    @Override
    public void writeField(final String fieldName) {
        this.ensureAllocated();
        this.setType(fieldName);
        super.writeField(fieldName);
    }
    
    @Override
    public void writeField(final String fieldName, final Object value) {
        this.ensureAllocated();
        this.setType(fieldName);
        super.writeField(fieldName, value);
    }
    
    public Object getTypedValue(final Class<?> type) {
        this.ensureAllocated();
        for (final StructField f : this.fields().values()) {
            if (f.type == type) {
                this.activeField = f;
                this.read();
                return this.getFieldValue(this.activeField.field);
            }
        }
        throw new IllegalArgumentException("No field of type " + type + " in " + this);
    }
    
    public Object setTypedValue(final Object object) {
        final StructField f = this.findField(object.getClass());
        if (f != null) {
            this.activeField = f;
            this.setFieldValue(f.field, object);
            return this;
        }
        throw new IllegalArgumentException("No field of type " + object.getClass() + " in " + this);
    }
    
    private StructField findField(final Class<?> type) {
        this.ensureAllocated();
        for (final StructField f : this.fields().values()) {
            if (f.type.isAssignableFrom(type)) {
                return f;
            }
        }
        return null;
    }
    
    @Override
    protected void writeField(final StructField field) {
        if (field == this.activeField) {
            super.writeField(field);
        }
    }
    
    @Override
    protected Object readField(final StructField field) {
        if (field == this.activeField || (!Structure.class.isAssignableFrom(field.type) && !String.class.isAssignableFrom(field.type) && !WString.class.isAssignableFrom(field.type))) {
            return super.readField(field);
        }
        return null;
    }
    
    @Override
    protected int getNativeAlignment(final Class<?> type, final Object value, final boolean isFirstElement) {
        return super.getNativeAlignment(type, value, true);
    }
}
