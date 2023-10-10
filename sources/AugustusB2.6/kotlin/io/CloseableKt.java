// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.io;

import kotlin.Unit;
import java.io.FileOutputStream;
import kotlin.text.Charsets;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.io.FileInputStream;
import kotlin.jvm.internal.Intrinsics;
import java.io.File;
import kotlin.ExceptionsKt__ExceptionsKt;
import java.io.Closeable;

public class CloseableKt
{
    public static final void closeFinally(final Closeable $this$closeFinally, final Throwable cause) {
        if (cause == null) {
            $this$closeFinally.close();
            return;
        }
        try {
            $this$closeFinally.close();
        }
        catch (Throwable closeException) {
            ExceptionsKt__ExceptionsKt.addSuppressed(cause, closeException);
        }
    }
    
    public static byte[] readBytes(final File $this$readBytes) {
        Intrinsics.checkParameterIsNotNull($this$readBytes, "$this$readBytes");
        final FileInputStream fileInputStream = new FileInputStream($this$readBytes);
        Throwable cause = null;
        try {
            final FileInputStream input = fileInputStream;
            int offset = 0;
            final long length;
            if ((length = $this$readBytes.length()) > 2147483647L) {
                throw new OutOfMemoryError("File " + $this$readBytes + " is too big (" + length + " bytes) to fit in memory.");
            }
            int remaining;
            byte[] result;
            int read;
            for (result = new byte[remaining = (int)length]; remaining > 0 && (read = input.read(result, offset, remaining)) >= 0; remaining -= read, offset += read) {}
            byte[] copy;
            if (remaining == 0) {
                copy = result;
            }
            else {
                Intrinsics.checkExpressionValueIsNotNull(copy = Arrays.copyOf(result, offset), "java.util.Arrays.copyOf(this, newSize)");
            }
            final byte[] array = copy;
            closeFinally(fileInputStream, null);
            return array;
        }
        catch (Throwable t2) {
            final Throwable t = t2;
            cause = t2;
            throw t;
        }
        finally {
            closeFinally(fileInputStream, cause);
        }
    }
}
