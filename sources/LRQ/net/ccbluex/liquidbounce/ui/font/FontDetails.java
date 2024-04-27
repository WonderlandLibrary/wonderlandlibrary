/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.font;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value=RetentionPolicy.RUNTIME)
public @interface FontDetails {
    public String fontName();

    public int fontSize() default -1;
}

