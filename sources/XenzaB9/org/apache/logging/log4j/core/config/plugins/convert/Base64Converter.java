// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.config.plugins.convert;

import org.apache.logging.log4j.util.LoaderUtil;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.Constants;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.logging.log4j.Logger;

public class Base64Converter
{
    private static final Logger LOGGER;
    private static Method method;
    private static Object decoder;
    
    public static byte[] parseBase64Binary(final String encoded) {
        if (Base64Converter.method == null) {
            Base64Converter.LOGGER.error("No base64 converter");
        }
        else {
            try {
                return (byte[])Base64Converter.method.invoke(Base64Converter.decoder, encoded);
            }
            catch (final IllegalAccessException | InvocationTargetException ex) {
                Base64Converter.LOGGER.error("Error decoding string - " + ex.getMessage());
            }
        }
        return Constants.EMPTY_BYTE_ARRAY;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        Base64Converter.method = null;
        Base64Converter.decoder = null;
        try {
            Class<?> clazz = LoaderUtil.loadClass("java.util.Base64");
            final Method getDecoder = clazz.getMethod("getDecoder", (Class<?>[])null);
            Base64Converter.decoder = getDecoder.invoke(null, (Object[])null);
            clazz = Base64Converter.decoder.getClass();
            Base64Converter.method = clazz.getMethod("decode", String.class);
        }
        catch (final ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException ex2) {}
        if (Base64Converter.method == null) {
            try {
                final Class<?> clazz = LoaderUtil.loadClass("javax.xml.bind.DatatypeConverter");
                Base64Converter.method = clazz.getMethod("parseBase64Binary", String.class);
            }
            catch (final ClassNotFoundException ex) {
                Base64Converter.LOGGER.error("No Base64 Converter is available");
            }
            catch (final NoSuchMethodException ex3) {}
        }
    }
}
