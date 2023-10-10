// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.io;

import kotlin.jvm.internal.Intrinsics;
import java.io.File;

class FilesKt__FileTreeWalkKt extends CloseableKt
{
    public static String getNameWithoutExtension(final File $this$nameWithoutExtension) {
        Intrinsics.checkParameterIsNotNull($this$nameWithoutExtension, "$this$nameWithoutExtension");
        final String name = $this$nameWithoutExtension.getName();
        Intrinsics.checkExpressionValueIsNotNull(name, "name");
        return StringsKt__StringsKt.substringBeforeLast$default$48f06b8c(name, ".", null, 2);
    }
}
