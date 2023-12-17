// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.util;

import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.reflect.Array;

public class ArrayUtils
{
    public static boolean contains(final Object[] arr, final Object val) {
        if (arr == null) {
            return false;
        }
        for (int i = 0; i < arr.length; ++i) {
            final Object object = arr[i];
            if (object == val) {
                return true;
            }
        }
        return false;
    }
    
    public static int[] addIntsToArray(final int[] intArray, final int[] copyFrom) {
        if (intArray != null && copyFrom != null) {
            final int i = intArray.length;
            final int j = i + copyFrom.length;
            final int[] aint = new int[j];
            System.arraycopy(intArray, 0, aint, 0, i);
            for (int k = 0; k < copyFrom.length; ++k) {
                aint[k + i] = copyFrom[k];
            }
            return aint;
        }
        throw new NullPointerException("The given array is NULL");
    }
    
    public static int[] addIntToArray(final int[] intArray, final int intValue) {
        return addIntsToArray(intArray, new int[] { intValue });
    }
    
    public static Object[] addObjectsToArray(final Object[] arr2, final Object[] objs) {
        if (arr2 == null) {
            throw new NullPointerException("The given array is NULL");
        }
        if (objs.length == 0) {
            return arr2;
        }
        final int arrLen = arr2.length;
        final int newLen = arrLen + objs.length;
        final Object[] newArr = (Object[])Array.newInstance(arr2.getClass().getComponentType(), newLen);
        System.arraycopy(arr2, 0, newArr, 0, arrLen);
        System.arraycopy(objs, 0, newArr, arrLen, objs.length);
        return newArr;
    }
    
    public static Object[] addObjectToArray(final Object[] arr2, final Object obj) {
        if (arr2 == null) {
            throw new NullPointerException("The given array is NULL");
        }
        final int arrLen = arr2.length;
        final int newLen = arrLen + 1;
        final Object[] newArr = (Object[])Array.newInstance(arr2.getClass().getComponentType(), newLen);
        System.arraycopy(arr2, 0, newArr, 0, arrLen);
        newArr[arrLen] = obj;
        return newArr;
    }
    
    public static Object[] addObjectToArray(final Object[] arr2, final Object obj, final int index) {
        final ArrayList<Object> list = new ArrayList<Object>(Arrays.asList(arr2));
        list.add(index, obj);
        final Object[] newArr = (Object[])Array.newInstance(arr2.getClass().getComponentType(), list.size());
        return list.toArray(newArr);
    }
    
    public static String arrayToString(final boolean[] arr, final String separator) {
        if (arr == null) {
            return "";
        }
        final StringBuffer stringbuffer = new StringBuffer(arr.length * 5);
        for (int i = 0; i < arr.length; ++i) {
            final boolean flag = arr[i];
            if (i > 0) {
                stringbuffer.append(separator);
            }
            stringbuffer.append(String.valueOf(flag));
        }
        return stringbuffer.toString();
    }
    
    public static String arrayToString(final float[] arr) {
        return arrayToString(arr, ", ");
    }
    
    public static String arrayToString(final float[] arr, final String separator) {
        if (arr == null) {
            return "";
        }
        final StringBuffer stringbuffer = new StringBuffer(arr.length * 5);
        for (int i = 0; i < arr.length; ++i) {
            final float f = arr[i];
            if (i > 0) {
                stringbuffer.append(separator);
            }
            stringbuffer.append(String.valueOf(f));
        }
        return stringbuffer.toString();
    }
    
    public static String arrayToString(final float[] arr, final String separator, final String format) {
        if (arr == null) {
            return "";
        }
        final StringBuffer stringbuffer = new StringBuffer(arr.length * 5);
        for (int i = 0; i < arr.length; ++i) {
            final float f = arr[i];
            if (i > 0) {
                stringbuffer.append(separator);
            }
            stringbuffer.append(String.format(format, f));
        }
        return stringbuffer.toString();
    }
    
    public static String arrayToString(final int[] arr) {
        return arrayToString(arr, ", ");
    }
    
    public static String arrayToString(final int[] arr, final String separator) {
        if (arr == null) {
            return "";
        }
        final StringBuffer stringbuffer = new StringBuffer(arr.length * 5);
        for (int i = 0; i < arr.length; ++i) {
            final int j = arr[i];
            if (i > 0) {
                stringbuffer.append(separator);
            }
            stringbuffer.append(String.valueOf(j));
        }
        return stringbuffer.toString();
    }
    
    public static String arrayToHexString(final int[] arr, final String separator) {
        if (arr == null) {
            return "";
        }
        final StringBuffer stringbuffer = new StringBuffer(arr.length * 5);
        for (int i = 0; i < arr.length; ++i) {
            final int j = arr[i];
            if (i > 0) {
                stringbuffer.append(separator);
            }
            stringbuffer.append("0x");
            stringbuffer.append(Integer.toHexString(j));
        }
        return stringbuffer.toString();
    }
    
    public static String arrayToString(final Object[] arr) {
        return arrayToString(arr, ", ");
    }
    
    public static String arrayToString(final Object[] arr, final String separator) {
        if (arr == null) {
            return "";
        }
        final StringBuffer stringbuffer = new StringBuffer(arr.length * 5);
        for (int i = 0; i < arr.length; ++i) {
            final Object object = arr[i];
            if (i > 0) {
                stringbuffer.append(separator);
            }
            stringbuffer.append(String.valueOf(object));
        }
        return stringbuffer.toString();
    }
    
    public static Object[] collectionToArray(final Collection coll, final Class elementClass) {
        if (coll == null) {
            return null;
        }
        if (elementClass == null) {
            return null;
        }
        if (elementClass.isPrimitive()) {
            throw new IllegalArgumentException("Can not make arrays with primitive elements (int, double), element class: " + elementClass);
        }
        final Object[] array = (Object[])Array.newInstance(elementClass, coll.size());
        return coll.toArray(array);
    }
    
    public static boolean equalsOne(final int val, final int[] vals) {
        for (int i = 0; i < vals.length; ++i) {
            if (vals[i] == val) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean equalsOne(final Object a, final Object[] bs) {
        if (bs == null) {
            return false;
        }
        for (int i = 0; i < bs.length; ++i) {
            final Object object = bs[i];
            if (equals(a, object)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean equals(final Object o1, final Object o2) {
        return o1 == o2 || (o1 != null && o1.equals(o2));
    }
    
    public static boolean isSameOne(final Object a, final Object[] bs) {
        if (bs == null) {
            return false;
        }
        for (int i = 0; i < bs.length; ++i) {
            final Object object = bs[i];
            if (a == object) {
                return true;
            }
        }
        return false;
    }
    
    public static Object[] removeObjectFromArray(final Object[] arr, final Object obj) {
        final List list = new ArrayList(Arrays.asList(arr));
        list.remove(obj);
        final Object[] aobject = collectionToArray(list, arr.getClass().getComponentType());
        return aobject;
    }
    
    public static int[] toPrimitive(final Integer[] arr) {
        if (arr == null) {
            return null;
        }
        if (arr.length == 0) {
            return new int[0];
        }
        final int[] aint = new int[arr.length];
        for (int i = 0; i < aint.length; ++i) {
            aint[i] = arr[i];
        }
        return aint;
    }
}
