/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.util;

import java.lang.reflect.Method;
import org.apache.logging.log4j.LoggingException;
import org.apache.logging.log4j.util.LoaderUtil;
import org.apache.logging.log4j.util.LowLevelLogUtil;

public final class Base64Util {
    private static Method encodeMethod = null;
    private static Object encoder = null;

    private Base64Util() {
    }

    public static String encode(String str) {
        if (str == null) {
            return null;
        }
        byte[] data = str.getBytes();
        if (encodeMethod != null) {
            try {
                return (String)encodeMethod.invoke(encoder, new Object[]{data});
            }
            catch (Exception ex) {
                throw new LoggingException("Unable to encode String", ex);
            }
        }
        throw new LoggingException("No Encoder, unable to encode string");
    }

    static {
        try {
            Class<?> clazz = LoaderUtil.loadClass("java.util.Base64");
            Class<?> encoderClazz = LoaderUtil.loadClass("java.util.Base64$Encoder");
            Method method = clazz.getMethod("getEncoder", new Class[0]);
            encoder = method.invoke(null, new Object[0]);
            encodeMethod = encoderClazz.getMethod("encodeToString", byte[].class);
        }
        catch (Exception ex) {
            try {
                Class<?> clazz = LoaderUtil.loadClass("javax.xml.bind.DataTypeConverter");
                encodeMethod = clazz.getMethod("printBase64Binary", new Class[0]);
            }
            catch (Exception ex2) {
                LowLevelLogUtil.logException("Unable to create a Base64 Encoder", ex2);
            }
        }
    }
}

