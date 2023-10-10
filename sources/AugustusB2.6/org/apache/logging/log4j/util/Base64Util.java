// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.util;

import org.apache.logging.log4j.LoggingException;
import java.lang.reflect.Method;

public final class Base64Util
{
    private static Method encodeMethod;
    private static Object encoder;
    
    private Base64Util() {
    }
    
    public static String encode(final String str) {
        if (str == null) {
            return null;
        }
        final byte[] data = str.getBytes();
        if (Base64Util.encodeMethod != null) {
            try {
                return (String)Base64Util.encodeMethod.invoke(Base64Util.encoder, data);
            }
            catch (Exception ex) {
                throw new LoggingException("Unable to encode String", ex);
            }
        }
        throw new LoggingException("No Encoder, unable to encode string");
    }
    
    static {
        Base64Util.encodeMethod = null;
        Base64Util.encoder = null;
        try {
            final Class<?> clazz = LoaderUtil.loadClass("java.util.Base64");
            final Class<?> encoderClazz = LoaderUtil.loadClass("java.util.Base64$Encoder");
            final Method method = clazz.getMethod("getEncoder", (Class<?>[])new Class[0]);
            Base64Util.encoder = method.invoke(null, new Object[0]);
            Base64Util.encodeMethod = encoderClazz.getMethod("encodeToString", byte[].class);
        }
        catch (Exception ex3) {
            try {
                final Class<?> clazz2 = LoaderUtil.loadClass("javax.xml.bind.DataTypeConverter");
                Base64Util.encodeMethod = clazz2.getMethod("printBase64Binary", (Class<?>[])new Class[0]);
            }
            catch (Exception ex2) {
                LowLevelLogUtil.logException("Unable to create a Base64 Encoder", ex2);
            }
        }
    }
}
