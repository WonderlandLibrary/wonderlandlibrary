/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.text;

import java.text.CharacterIterator;
import java.util.Stack;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
interface LanguageBreakEngine {
    public boolean handles(int var1, int var2);

    public int findBreaks(CharacterIterator var1, int var2, int var3, boolean var4, int var5, Stack<Integer> var6);
}

