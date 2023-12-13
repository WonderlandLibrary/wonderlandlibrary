// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.io;

import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import me.gong.mcleaks.util.google.common.annotations.Beta;
import java.nio.file.FileSystemException;

@Beta
@AndroidIncompatible
@GwtIncompatible
public final class InsecureRecursiveDeleteException extends FileSystemException
{
    public InsecureRecursiveDeleteException(@Nullable final String file) {
        super(file, null, "unable to guarantee security of recursive delete");
    }
}
