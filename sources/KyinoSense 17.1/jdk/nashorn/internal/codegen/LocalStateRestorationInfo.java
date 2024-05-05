/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.codegen;

import jdk.nashorn.internal.codegen.types.Type;

public class LocalStateRestorationInfo {
    private final Type[] localVariableTypes;
    private final int[] stackLoads;

    LocalStateRestorationInfo(Type[] localVariableTypes, int[] stackLoads) {
        this.localVariableTypes = localVariableTypes;
        this.stackLoads = stackLoads;
    }

    public Type[] getLocalVariableTypes() {
        return (Type[])this.localVariableTypes.clone();
    }

    public int[] getStackLoads() {
        return (int[])this.stackLoads.clone();
    }
}

