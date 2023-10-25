/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.pattern;

public interface PatternConverter {
    public void format(Object var1, StringBuilder var2);

    public String getName();

    public String getStyleClass(Object var1);
}

