// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.gson.internal.reflect;

import java.lang.reflect.Method;
import com.viaversion.viaversion.libs.gson.JsonIOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

final class UnsafeReflectionAccessor extends ReflectionAccessor
{
    private static Class unsafeClass;
    private final Object theUnsafe;
    private final Field overrideField;
    
    UnsafeReflectionAccessor() {
        this.theUnsafe = getUnsafeInstance();
        this.overrideField = getOverrideField();
    }
    
    @Override
    public void makeAccessible(final AccessibleObject ao) {
        final boolean success = this.makeAccessibleWithUnsafe(ao);
        if (!success) {
            try {
                ao.setAccessible(true);
            }
            catch (SecurityException e) {
                throw new JsonIOException("Gson couldn't modify fields for " + ao + "\nand sun.misc.Unsafe not found.\nEither write a custom type adapter, or make fields accessible, or include sun.misc.Unsafe.", e);
            }
        }
    }
    
    boolean makeAccessibleWithUnsafe(final AccessibleObject ao) {
        if (this.theUnsafe != null && this.overrideField != null) {
            try {
                final Method method = UnsafeReflectionAccessor.unsafeClass.getMethod("objectFieldOffset", Field.class);
                final long overrideOffset = (long)method.invoke(this.theUnsafe, this.overrideField);
                final Method putBooleanMethod = UnsafeReflectionAccessor.unsafeClass.getMethod("putBoolean", Object.class, Long.TYPE, Boolean.TYPE);
                putBooleanMethod.invoke(this.theUnsafe, ao, overrideOffset, true);
                return true;
            }
            catch (Exception ex) {}
        }
        return false;
    }
    
    private static Object getUnsafeInstance() {
        try {
            UnsafeReflectionAccessor.unsafeClass = Class.forName("sun.misc.Unsafe");
            final Field unsafeField = UnsafeReflectionAccessor.unsafeClass.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            return unsafeField.get(null);
        }
        catch (Exception e) {
            return null;
        }
    }
    
    private static Field getOverrideField() {
        try {
            return AccessibleObject.class.getDeclaredField("override");
        }
        catch (Exception e) {
            return null;
        }
    }
}
