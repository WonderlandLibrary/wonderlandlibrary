/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.rolling.action;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public interface PathCondition {
    public void beforeFileTreeWalk();

    public boolean accept(Path var1, Path var2, BasicFileAttributes var3);
}

