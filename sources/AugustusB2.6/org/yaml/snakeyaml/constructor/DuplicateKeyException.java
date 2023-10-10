// 
// Decompiled by Procyon v0.5.36
// 

package org.yaml.snakeyaml.constructor;

import org.yaml.snakeyaml.error.Mark;

public class DuplicateKeyException extends ConstructorException
{
    protected DuplicateKeyException(final Mark contextMark, final Object key, final Mark problemMark) {
        super("while constructing a mapping", contextMark, "found duplicate key " + String.valueOf(key), problemMark);
    }
}
