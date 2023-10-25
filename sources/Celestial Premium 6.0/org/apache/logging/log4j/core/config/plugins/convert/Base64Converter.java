/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.plugins.convert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.LoaderUtil;

public class Base64Converter {
    private static final Logger LOGGER;
    private static Method method;
    private static Object decoder;

    public static byte[] parseBase64Binary(String encoded) {
        if (method == null) {
            LOGGER.error("No base64 converter");
        } else {
            try {
                return (byte[])method.invoke(decoder, encoded);
            }
            catch (IllegalAccessException | InvocationTargetException ex) {
                LOGGER.error("Error decoding string - " + ex.getMessage());
            }
        }
        return new byte[0];
    }

    static {
        Class<?> clazz2;
        LOGGER = StatusLogger.getLogger();
        method = null;
        decoder = null;
        try {
            clazz2 = LoaderUtil.loadClass("java.util.Base64");
            Method getDecoder = clazz2.getMethod("getDecoder", null);
            decoder = getDecoder.invoke(null, null);
            clazz2 = decoder.getClass();
            method = clazz2.getMethod("decode", String.class);
        }
        catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException clazz2) {
            // empty catch block
        }
        if (method == null) {
            try {
                clazz2 = LoaderUtil.loadClass("javax.xml.bind.DatatypeConverter");
                method = clazz2.getMethod("parseBase64Binary", String.class);
            }
            catch (ClassNotFoundException ex) {
                LOGGER.error("No Base64 Converter is available");
            }
            catch (NoSuchMethodException noSuchMethodException) {
                // empty catch block
            }
        }
    }
}

