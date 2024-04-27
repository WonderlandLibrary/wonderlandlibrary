/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.command.shortcuts;

import net.ccbluex.liquidbounce.features.command.shortcuts.Token;

public final class Literal
extends Token {
    private final String literal;

    public final String getLiteral() {
        return this.literal;
    }

    public Literal(String literal) {
        this.literal = literal;
    }
}

