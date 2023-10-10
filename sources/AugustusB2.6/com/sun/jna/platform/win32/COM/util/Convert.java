// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM.util;

import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.WTypes;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import com.sun.jna.platform.win32.COM.IUnknown;
import com.sun.jna.platform.win32.OaIdl;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Constructor;
import com.sun.jna.platform.win32.WinDef;
import java.lang.reflect.Proxy;
import java.util.Date;
import com.sun.jna.platform.win32.COM.Dispatch;
import com.sun.jna.platform.win32.Variant;

class Convert
{
    public static Variant.VARIANT toVariant(final Object value) {
        if (value instanceof Variant.VARIANT) {
            return (Variant.VARIANT)value;
        }
        if (value instanceof Byte) {
            return new Variant.VARIANT((byte)value);
        }
        if (value instanceof Character) {
            return new Variant.VARIANT((char)value);
        }
        if (value instanceof Short) {
            return new Variant.VARIANT((short)value);
        }
        if (value instanceof Integer) {
            return new Variant.VARIANT((int)value);
        }
        if (value instanceof Long) {
            return new Variant.VARIANT((long)value);
        }
        if (value instanceof Float) {
            return new Variant.VARIANT((float)value);
        }
        if (value instanceof Double) {
            return new Variant.VARIANT((double)value);
        }
        if (value instanceof String) {
            return new Variant.VARIANT((String)value);
        }
        if (value instanceof Boolean) {
            return new Variant.VARIANT((boolean)value);
        }
        if (value instanceof Dispatch) {
            return new Variant.VARIANT((Dispatch)value);
        }
        if (value instanceof Date) {
            return new Variant.VARIANT((Date)value);
        }
        if (value instanceof Proxy) {
            final InvocationHandler ih = Proxy.getInvocationHandler(value);
            final ProxyObject pobj = (ProxyObject)ih;
            return new Variant.VARIANT(pobj.getRawDispatch());
        }
        if (value instanceof IComEnum) {
            final IComEnum enm = (IComEnum)value;
            return new Variant.VARIANT(new WinDef.LONG(enm.getValue()));
        }
        Constructor<Variant.VARIANT> constructor = null;
        if (value != null) {
            for (final Constructor<Variant.VARIANT> m : Variant.VARIANT.class.getConstructors()) {
                final Class<?>[] parameters = m.getParameterTypes();
                if (parameters.length == 1 && parameters[0].isAssignableFrom(value.getClass())) {
                    constructor = m;
                }
            }
        }
        if (constructor != null) {
            try {
                return constructor.newInstance(value);
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return null;
    }
    
    public static Object toJavaObject(Variant.VARIANT value, Class<?> targetClass, final ObjectFactory factory, final boolean addReference, final boolean freeValue) {
        int varType = (value != null) ? value.getVarType().intValue() : 1;
        if (varType == 0 || varType == 1) {
            return null;
        }
        if (targetClass != null && !targetClass.isAssignableFrom(Object.class)) {
            if (targetClass.isAssignableFrom(value.getClass())) {
                return value;
            }
            final Object vobj = value.getValue();
            if (vobj != null && targetClass.isAssignableFrom(vobj.getClass())) {
                return vobj;
            }
        }
        final Variant.VARIANT inputValue = value;
        if (varType == 16396) {
            value = (Variant.VARIANT)value.getValue();
            varType = value.getVarType().intValue();
        }
        if (targetClass == null || targetClass.isAssignableFrom(Object.class)) {
            targetClass = null;
            switch (varType) {
                case 16:
                case 17: {
                    targetClass = Byte.class;
                    break;
                }
                case 2: {
                    targetClass = Short.class;
                    break;
                }
                case 18: {
                    targetClass = Character.class;
                    break;
                }
                case 3:
                case 19:
                case 22:
                case 23: {
                    targetClass = Integer.class;
                    break;
                }
                case 20:
                case 21: {
                    targetClass = Long.class;
                    break;
                }
                case 4: {
                    targetClass = Float.class;
                    break;
                }
                case 5: {
                    targetClass = Double.class;
                    break;
                }
                case 11: {
                    targetClass = Boolean.class;
                    break;
                }
                case 10: {
                    targetClass = WinDef.SCODE.class;
                    break;
                }
                case 6: {
                    targetClass = OaIdl.CURRENCY.class;
                    break;
                }
                case 7: {
                    targetClass = Date.class;
                    break;
                }
                case 8: {
                    targetClass = String.class;
                    break;
                }
                case 13: {
                    targetClass = IUnknown.class;
                    break;
                }
                case 9: {
                    targetClass = IDispatch.class;
                    break;
                }
                case 16396: {
                    targetClass = Variant.class;
                    break;
                }
                case 16384: {
                    targetClass = WinDef.PVOID.class;
                    break;
                }
                case 16398: {
                    targetClass = OaIdl.DECIMAL.class;
                    break;
                }
                default: {
                    if ((varType & 0x2000) > 0) {
                        targetClass = OaIdl.SAFEARRAY.class;
                        break;
                    }
                    break;
                }
            }
        }
        Object result;
        if (Byte.class.equals(targetClass) || Byte.TYPE.equals(targetClass)) {
            result = value.byteValue();
        }
        else if (Short.class.equals(targetClass) || Short.TYPE.equals(targetClass)) {
            result = value.shortValue();
        }
        else if (Character.class.equals(targetClass) || Character.TYPE.equals(targetClass)) {
            result = (char)value.intValue();
        }
        else if (Integer.class.equals(targetClass) || Integer.TYPE.equals(targetClass)) {
            result = value.intValue();
        }
        else if (Long.class.equals(targetClass) || Long.TYPE.equals(targetClass) || IComEnum.class.isAssignableFrom(targetClass)) {
            result = value.longValue();
        }
        else if (Float.class.equals(targetClass) || Float.TYPE.equals(targetClass)) {
            result = value.floatValue();
        }
        else if (Double.class.equals(targetClass) || Double.TYPE.equals(targetClass)) {
            result = value.doubleValue();
        }
        else if (Boolean.class.equals(targetClass) || Boolean.TYPE.equals(targetClass)) {
            result = value.booleanValue();
        }
        else if (Date.class.equals(targetClass)) {
            result = value.dateValue();
        }
        else if (String.class.equals(targetClass)) {
            result = value.stringValue();
        }
        else {
            result = value.getValue();
            if (result instanceof Dispatch) {
                final Dispatch d = (Dispatch)result;
                if (targetClass != null && targetClass.isInterface()) {
                    final Object proxy = factory.createProxy(targetClass, d);
                    if (!addReference) {
                        d.Release();
                    }
                    result = proxy;
                }
                else {
                    result = d;
                }
            }
        }
        if (IComEnum.class.isAssignableFrom(targetClass)) {
            result = targetClass.cast(toComEnum(targetClass, result));
        }
        if (freeValue) {
            free(inputValue, result);
        }
        return result;
    }
    
    public static <T extends IComEnum> T toComEnum(final Class<T> enumType, final Object value) {
        try {
            final Method m = enumType.getMethod("values", (Class<?>[])new Class[0]);
            final IComEnum[] array;
            final T[] values = (T[])(array = (IComEnum[])m.invoke(null, new Object[0]));
            for (final T t : array) {
                if (value.equals(t.getValue())) {
                    return t;
                }
            }
        }
        catch (NoSuchMethodException ex) {}
        catch (IllegalAccessException ex2) {}
        catch (IllegalArgumentException ex3) {}
        catch (InvocationTargetException ex4) {}
        return null;
    }
    
    public static void free(final Variant.VARIANT variant, final Class<?> javaType) {
        if ((javaType == null || !WTypes.BSTR.class.isAssignableFrom(javaType)) && variant != null && variant.getVarType().intValue() == 8) {
            final Object value = variant.getValue();
            if (value instanceof WTypes.BSTR) {
                OleAuto.INSTANCE.SysFreeString((WTypes.BSTR)value);
            }
        }
    }
    
    public static void free(final Variant.VARIANT variant, final Object value) {
        free(variant, (value == null) ? null : value.getClass());
    }
}
