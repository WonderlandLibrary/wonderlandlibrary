// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.io;

import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;
import java.nio.file.FileSystemException;

@ElementTypesAreNonnullByDefault
@Beta
@GwtIncompatible
public final class InsecureRecursiveDeleteException extends FileSystemException
{
    public InsecureRecursiveDeleteException(@CheckForNull final String file) {
        super(file, null, "unable to guarantee security of recursive delete");
    }
}
