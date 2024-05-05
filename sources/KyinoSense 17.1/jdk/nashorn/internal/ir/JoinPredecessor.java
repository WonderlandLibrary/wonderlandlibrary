/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.LocalVariableConversion;

public interface JoinPredecessor {
    public JoinPredecessor setLocalVariableConversion(LexicalContext var1, LocalVariableConversion var2);

    public LocalVariableConversion getLocalVariableConversion();
}

