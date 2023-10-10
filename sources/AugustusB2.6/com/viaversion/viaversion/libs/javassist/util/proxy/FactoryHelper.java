// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist.util.proxy;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;
import java.lang.invoke.MethodHandles;
import java.io.IOException;
import com.viaversion.viaversion.libs.javassist.CannotCompileException;
import java.security.ProtectionDomain;
import com.viaversion.viaversion.libs.javassist.bytecode.ClassFile;

public class FactoryHelper
{
    public static final Class<?>[] primitiveTypes;
    public static final String[] wrapperTypes;
    public static final String[] wrapperDesc;
    public static final String[] unwarpMethods;
    public static final String[] unwrapDesc;
    public static final int[] dataSize;
    
    public static final int typeIndex(final Class<?> type) {
        for (int i = 0; i < FactoryHelper.primitiveTypes.length; ++i) {
            if (FactoryHelper.primitiveTypes[i] == type) {
                return i;
            }
        }
        throw new RuntimeException("bad type:" + type.getName());
    }
    
    @Deprecated
    public static Class<?> toClass(final ClassFile cf, final ClassLoader loader) throws CannotCompileException {
        return toClass(cf, null, loader, null);
    }
    
    @Deprecated
    public static Class<?> toClass(final ClassFile cf, final ClassLoader loader, final ProtectionDomain domain) throws CannotCompileException {
        return toClass(cf, null, loader, domain);
    }
    
    public static Class<?> toClass(final ClassFile cf, final Class<?> neighbor, final ClassLoader loader, final ProtectionDomain domain) throws CannotCompileException {
        try {
            final byte[] b = toBytecode(cf);
            if (ProxyFactory.onlyPublicMethods) {
                return DefineClassHelper.toPublicClass(cf.getName(), b);
            }
            return DefineClassHelper.toClass(cf.getName(), neighbor, loader, domain, b);
        }
        catch (IOException e) {
            throw new CannotCompileException(e);
        }
    }
    
    public static Class<?> toClass(final ClassFile cf, final MethodHandles.Lookup lookup) throws CannotCompileException {
        try {
            final byte[] b = toBytecode(cf);
            return DefineClassHelper.toClass(lookup, b);
        }
        catch (IOException e) {
            throw new CannotCompileException(e);
        }
    }
    
    private static byte[] toBytecode(final ClassFile cf) throws IOException {
        final ByteArrayOutputStream barray = new ByteArrayOutputStream();
        final DataOutputStream out = new DataOutputStream(barray);
        try {
            cf.write(out);
        }
        finally {
            out.close();
        }
        return barray.toByteArray();
    }
    
    public static void writeFile(final ClassFile cf, final String directoryName) throws CannotCompileException {
        try {
            writeFile0(cf, directoryName);
        }
        catch (IOException e) {
            throw new CannotCompileException(e);
        }
    }
    
    private static void writeFile0(final ClassFile cf, final String directoryName) throws CannotCompileException, IOException {
        final String classname = cf.getName();
        final String filename = directoryName + File.separatorChar + classname.replace('.', File.separatorChar) + ".class";
        final int pos = filename.lastIndexOf(File.separatorChar);
        if (pos > 0) {
            final String dir = filename.substring(0, pos);
            if (!dir.equals(".")) {
                new File(dir).mkdirs();
            }
        }
        final DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(filename)));
        try {
            cf.write(out);
        }
        catch (IOException e) {
            throw e;
        }
        finally {
            out.close();
        }
    }
    
    static {
        primitiveTypes = new Class[] { Boolean.TYPE, Byte.TYPE, Character.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Void.TYPE };
        wrapperTypes = new String[] { "java.lang.Boolean", "java.lang.Byte", "java.lang.Character", "java.lang.Short", "java.lang.Integer", "java.lang.Long", "java.lang.Float", "java.lang.Double", "java.lang.Void" };
        wrapperDesc = new String[] { "(Z)V", "(B)V", "(C)V", "(S)V", "(I)V", "(J)V", "(F)V", "(D)V" };
        unwarpMethods = new String[] { "booleanValue", "byteValue", "charValue", "shortValue", "intValue", "longValue", "floatValue", "doubleValue" };
        unwrapDesc = new String[] { "()Z", "()B", "()C", "()S", "()I", "()J", "()F", "()D" };
        dataSize = new int[] { 1, 1, 1, 1, 1, 2, 1, 2 };
    }
}
