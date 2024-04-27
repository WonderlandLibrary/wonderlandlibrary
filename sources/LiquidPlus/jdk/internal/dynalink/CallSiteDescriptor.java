/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public interface CallSiteDescriptor {
    public static final int SCHEME = 0;
    public static final int OPERATOR = 1;
    public static final int NAME_OPERAND = 2;
    public static final String TOKEN_DELIMITER = ":";
    public static final String OPERATOR_DELIMITER = "|";

    public int getNameTokenCount();

    public String getNameToken(int var1);

    public String getName();

    public MethodType getMethodType();

    public MethodHandles.Lookup getLookup();

    public CallSiteDescriptor changeMethodType(MethodType var1);
}

