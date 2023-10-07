/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.LexicalContextNode;

public interface Flags<T extends LexicalContextNode> {
    public int getFlags();

    public boolean getFlag(int var1);

    public T clearFlag(LexicalContext var1, int var2);

    public T setFlag(LexicalContext var1, int var2);

    public T setFlags(LexicalContext var1, int var2);
}

