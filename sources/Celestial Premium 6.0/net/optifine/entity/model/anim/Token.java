/*
 * Decompiled with CFR 0.150.
 */
package net.optifine.entity.model.anim;

import net.optifine.entity.model.anim.EnumTokenType;

public class Token {
    private EnumTokenType type;
    private String text;

    public Token(EnumTokenType type, String text) {
        this.type = type;
        this.text = text;
    }

    public EnumTokenType getType() {
        return this.type;
    }

    public String getText() {
        return this.text;
    }

    public String toString() {
        return this.text;
    }
}

