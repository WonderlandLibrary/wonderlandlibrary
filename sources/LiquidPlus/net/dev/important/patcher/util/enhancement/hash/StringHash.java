/*
 * Decompiled with CFR 0.152.
 */
package net.dev.important.patcher.util.enhancement.hash;

import net.dev.important.patcher.util.enhancement.hash.impl.AbstractHash;

public class StringHash
extends AbstractHash {
    public StringHash(String text, float red2, float green2, float blue2, float alpha2, boolean shadow) {
        super(text, Float.valueOf(red2), Float.valueOf(green2), Float.valueOf(blue2), Float.valueOf(alpha2), shadow);
    }
}

