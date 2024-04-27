/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.api.scripting;

import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.internal.runtime.JSType;

class DefaultValueImpl {
    private static final String[] DEFAULT_VALUE_FNS_NUMBER = new String[]{"valueOf", "toString"};
    private static final String[] DEFAULT_VALUE_FNS_STRING = new String[]{"toString", "valueOf"};

    DefaultValueImpl() {
    }

    static Object getDefaultValue(JSObject jsobj, Class<?> hint) throws UnsupportedOperationException {
        boolean isNumber = hint == null || hint == Number.class;
        for (String methodName : isNumber ? DEFAULT_VALUE_FNS_NUMBER : DEFAULT_VALUE_FNS_STRING) {
            Object value;
            JSObject member;
            Object objMember = jsobj.getMember(methodName);
            if (!(objMember instanceof JSObject) || !(member = (JSObject)objMember).isFunction() || !JSType.isPrimitive(value = member.call(jsobj, new Object[0]))) continue;
            return value;
        }
        throw new UnsupportedOperationException(isNumber ? "cannot.get.default.number" : "cannot.get.default.string");
    }
}

