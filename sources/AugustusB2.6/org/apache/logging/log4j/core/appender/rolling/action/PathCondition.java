// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling.action;

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.Path;
import java.util.Arrays;

public interface PathCondition
{
    public static final PathCondition[] EMPTY_ARRAY = new PathCondition[0];
    
    default PathCondition[] copy(final PathCondition[] source) {
        return (source == null) ? PathCondition.EMPTY_ARRAY : Arrays.copyOf(source, source.length);
    }
    
    void beforeFileTreeWalk();
    
    boolean accept(final Path baseDir, final Path relativePath, final BasicFileAttributes attrs);
}
