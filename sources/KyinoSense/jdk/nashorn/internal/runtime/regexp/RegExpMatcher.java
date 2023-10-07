/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp;

import java.util.regex.MatchResult;

public interface RegExpMatcher
extends MatchResult {
    public boolean search(int var1);

    public String getInput();
}

