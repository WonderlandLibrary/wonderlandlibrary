/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.util;

import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.util.MessageSupplier;
import org.apache.logging.log4j.util.Supplier;

public final class LambdaUtil {
    private LambdaUtil() {
    }

    public static Object[] getAll(Supplier<?> ... suppliers) {
        if (suppliers == null) {
            return null;
        }
        Object[] result = new Object[suppliers.length];
        for (int i = 0; i < result.length; ++i) {
            result[i] = LambdaUtil.get(suppliers[i]);
        }
        return result;
    }

    public static Object get(Supplier<?> supplier) {
        if (supplier == null) {
            return null;
        }
        Object result = supplier.get();
        return result instanceof Message ? ((Message)result).getFormattedMessage() : result;
    }

    public static Message get(MessageSupplier supplier) {
        if (supplier == null) {
            return null;
        }
        return supplier.get();
    }

    public static Message getMessage(Supplier<?> supplier, MessageFactory messageFactory) {
        if (supplier == null) {
            return null;
        }
        Object result = supplier.get();
        return result instanceof Message ? (Message)result : messageFactory.newMessage(result);
    }
}

