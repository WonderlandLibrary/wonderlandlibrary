/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.internal.InlineOnly;
import kotlin.io.ByteStreamsKt;
import kotlin.io.CloseableKt;
import kotlin.io.ExposingBufferByteArrayOutputStream;
import kotlin.io.FilesKt;
import kotlin.io.FilesKt__FilePathComponentsKt;
import kotlin.io.TextStreamsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.sequences.Sequence;
import kotlin.text.Charsets;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=5, xi=49, d1={"\u0000z\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\u001a\u0012\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004\u001a\u001c\u0010\u0005\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t\u001a!\u0010\n\u001a\u00020\u000b*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\f\u001a\u00020\rH\u0087\b\u001a!\u0010\u000e\u001a\u00020\u000f*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\f\u001a\u00020\rH\u0087\b\u001aB\u0010\u0010\u001a\u00020\u0001*\u00020\u000226\u0010\u0011\u001a2\u0012\u0013\u0012\u00110\u0004\u00a2\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0015\u0012\u0013\u0012\u00110\r\u00a2\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0016\u0012\u0004\u0012\u00020\u00010\u0012\u001aJ\u0010\u0010\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0017\u001a\u00020\r26\u0010\u0011\u001a2\u0012\u0013\u0012\u00110\u0004\u00a2\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0015\u0012\u0013\u0012\u00110\r\u00a2\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0016\u0012\u0004\u0012\u00020\u00010\u0012\u001a7\u0010\u0018\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2!\u0010\u0011\u001a\u001d\u0012\u0013\u0012\u00110\u0007\u00a2\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u001a\u0012\u0004\u0012\u00020\u00010\u0019\u001a\r\u0010\u001b\u001a\u00020\u001c*\u00020\u0002H\u0087\b\u001a\r\u0010\u001d\u001a\u00020\u001e*\u00020\u0002H\u0087\b\u001a\u0017\u0010\u001f\u001a\u00020 *\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\tH\u0087\b\u001a\n\u0010!\u001a\u00020\u0004*\u00020\u0002\u001a\u001a\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00070#*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t\u001a\u0014\u0010$\u001a\u00020\u0007*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t\u001a\u0017\u0010%\u001a\u00020&*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\tH\u0087\b\u001aB\u0010'\u001a\u0002H(\"\u0004\b\u0000\u0010(*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2\u0018\u0010)\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070*\u0012\u0004\u0012\u0002H(0\u0019H\u0086\b\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0002\u0010,\u001a\u0012\u0010-\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004\u001a\u001c\u0010.\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t\u001a\u0017\u0010/\u001a\u000200*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\tH\u0087\b\u0082\u0002\u000f\n\u0006\b\u0011(+0\u0001\n\u0005\b\u009920\u0001\u00a8\u00061"}, d2={"appendBytes", "", "Ljava/io/File;", "array", "", "appendText", "text", "", "charset", "Ljava/nio/charset/Charset;", "bufferedReader", "Ljava/io/BufferedReader;", "bufferSize", "", "bufferedWriter", "Ljava/io/BufferedWriter;", "forEachBlock", "action", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "buffer", "bytesRead", "blockSize", "forEachLine", "Lkotlin/Function1;", "line", "inputStream", "Ljava/io/FileInputStream;", "outputStream", "Ljava/io/FileOutputStream;", "printWriter", "Ljava/io/PrintWriter;", "readBytes", "readLines", "", "readText", "reader", "Ljava/io/InputStreamReader;", "useLines", "T", "block", "Lkotlin/sequences/Sequence;", "Requires newer compiler version to be inlined correctly.", "(Ljava/io/File;Ljava/nio/charset/Charset;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "writeBytes", "writeText", "writer", "Ljava/io/OutputStreamWriter;", "kotlin-stdlib"}, xs="kotlin/io/FilesKt")
class FilesKt__FileReadWriteKt
extends FilesKt__FilePathComponentsKt {
    @InlineOnly
    private static final InputStreamReader reader(File $this$reader, Charset charset) {
        Intrinsics.checkNotNullParameter($this$reader, "<this>");
        Intrinsics.checkNotNullParameter(charset, "charset");
        Object object = $this$reader;
        object = new FileInputStream((File)object);
        return new InputStreamReader((InputStream)object, charset);
    }

    static /* synthetic */ InputStreamReader reader$default(File $this$reader_u24default, Charset charset, int n, Object object) {
        if ((n & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        Intrinsics.checkNotNullParameter($this$reader_u24default, "<this>");
        Intrinsics.checkNotNullParameter(charset, "charset");
        Object object2 = $this$reader_u24default;
        object2 = new FileInputStream((File)object2);
        return new InputStreamReader((InputStream)object2, charset);
    }

    @InlineOnly
    private static final BufferedReader bufferedReader(File $this$bufferedReader, Charset charset, int bufferSize) {
        Object object;
        Intrinsics.checkNotNullParameter($this$bufferedReader, "<this>");
        Intrinsics.checkNotNullParameter(charset, "charset");
        Object object2 = object = $this$bufferedReader;
        object2 = new FileInputStream((File)object2);
        object = new InputStreamReader((InputStream)object2, charset);
        return object instanceof BufferedReader ? (BufferedReader)object : new BufferedReader((Reader)object, bufferSize);
    }

    static /* synthetic */ BufferedReader bufferedReader$default(File $this$bufferedReader_u24default, Charset charset, int bufferSize, int n, Object object) {
        Object object2;
        if ((n & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        if ((n & 2) != 0) {
            bufferSize = 8192;
        }
        Intrinsics.checkNotNullParameter($this$bufferedReader_u24default, "<this>");
        Intrinsics.checkNotNullParameter(charset, "charset");
        object = object2 = $this$bufferedReader_u24default;
        object = new FileInputStream((File)object);
        object2 = new InputStreamReader((InputStream)object, charset);
        return object2 instanceof BufferedReader ? (BufferedReader)object2 : new BufferedReader((Reader)object2, bufferSize);
    }

    @InlineOnly
    private static final OutputStreamWriter writer(File $this$writer, Charset charset) {
        Intrinsics.checkNotNullParameter($this$writer, "<this>");
        Intrinsics.checkNotNullParameter(charset, "charset");
        Object object = $this$writer;
        object = new FileOutputStream((File)object);
        return new OutputStreamWriter((OutputStream)object, charset);
    }

    static /* synthetic */ OutputStreamWriter writer$default(File $this$writer_u24default, Charset charset, int n, Object object) {
        if ((n & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        Intrinsics.checkNotNullParameter($this$writer_u24default, "<this>");
        Intrinsics.checkNotNullParameter(charset, "charset");
        Object object2 = $this$writer_u24default;
        object2 = new FileOutputStream((File)object2);
        return new OutputStreamWriter((OutputStream)object2, charset);
    }

    @InlineOnly
    private static final BufferedWriter bufferedWriter(File $this$bufferedWriter, Charset charset, int bufferSize) {
        Object object;
        Intrinsics.checkNotNullParameter($this$bufferedWriter, "<this>");
        Intrinsics.checkNotNullParameter(charset, "charset");
        Object object2 = object = $this$bufferedWriter;
        object2 = new FileOutputStream((File)object2);
        object = new OutputStreamWriter((OutputStream)object2, charset);
        return object instanceof BufferedWriter ? (BufferedWriter)object : new BufferedWriter((Writer)object, bufferSize);
    }

    static /* synthetic */ BufferedWriter bufferedWriter$default(File $this$bufferedWriter_u24default, Charset charset, int bufferSize, int n, Object object) {
        Object object2;
        if ((n & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        if ((n & 2) != 0) {
            bufferSize = 8192;
        }
        Intrinsics.checkNotNullParameter($this$bufferedWriter_u24default, "<this>");
        Intrinsics.checkNotNullParameter(charset, "charset");
        object = object2 = $this$bufferedWriter_u24default;
        object = new FileOutputStream((File)object);
        object2 = new OutputStreamWriter((OutputStream)object, charset);
        return object2 instanceof BufferedWriter ? (BufferedWriter)object2 : new BufferedWriter((Writer)object2, bufferSize);
    }

    @InlineOnly
    private static final PrintWriter printWriter(File $this$printWriter, Charset charset) {
        Object object;
        Intrinsics.checkNotNullParameter($this$printWriter, "<this>");
        Intrinsics.checkNotNullParameter(charset, "charset");
        File file = $this$printWriter;
        int n = 8192;
        Object object2 = object = file;
        object2 = new FileOutputStream((File)object2);
        object = new OutputStreamWriter((OutputStream)object2, charset);
        return new PrintWriter(object instanceof BufferedWriter ? (BufferedWriter)object : new BufferedWriter((Writer)object, n));
    }

    static /* synthetic */ PrintWriter printWriter$default(File $this$printWriter_u24default, Charset charset, int n, Object object) {
        Object object2;
        if ((n & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        Intrinsics.checkNotNullParameter($this$printWriter_u24default, "<this>");
        Intrinsics.checkNotNullParameter(charset, "charset");
        File file = $this$printWriter_u24default;
        int n2 = 8192;
        Object object3 = object2 = file;
        object3 = new FileOutputStream((File)object3);
        object2 = new OutputStreamWriter((OutputStream)object3, charset);
        return new PrintWriter(object2 instanceof BufferedWriter ? (BufferedWriter)object2 : new BufferedWriter((Writer)object2, n2));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @NotNull
    public static final byte[] readBytes(@NotNull File $this$readBytes) {
        byte[] byArray;
        Intrinsics.checkNotNullParameter($this$readBytes, "<this>");
        Closeable closeable = new FileInputStream($this$readBytes);
        Throwable throwable = null;
        try {
            byte[] byArray2;
            int read2;
            long l;
            FileInputStream input = (FileInputStream)closeable;
            boolean bl = false;
            int offset = 0;
            long length = l = $this$readBytes.length();
            boolean bl2 = false;
            if (length > Integer.MAX_VALUE) {
                throw new OutOfMemoryError("File " + $this$readBytes + " is too big (" + length + " bytes) to fit in memory.");
            }
            int remaining = (int)l;
            byte[] result = new byte[remaining];
            while (remaining > 0 && (read2 = input.read(result, offset, remaining)) >= 0) {
                remaining -= read2;
                offset += read2;
            }
            if (remaining > 0) {
                byte[] read2 = result;
                byte[] byArray3 = Arrays.copyOf(read2, offset);
                Intrinsics.checkNotNullExpressionValue(byArray3, "copyOf(this, newSize)");
                byArray2 = byArray3;
            } else {
                int extraByte = input.read();
                if (extraByte == -1) {
                    byArray2 = result;
                } else {
                    ExposingBufferByteArrayOutputStream extra = new ExposingBufferByteArrayOutputStream(8193);
                    extra.write(extraByte);
                    ByteStreamsKt.copyTo$default(input, extra, 0, 2, null);
                    int resultingSize = result.length + extra.size();
                    if (resultingSize < 0) {
                        throw new OutOfMemoryError("File " + $this$readBytes + " is too big to fit in memory.");
                    }
                    byte[] byArray4 = extra.getBuffer();
                    byte[] byArray5 = result;
                    byte[] byArray6 = Arrays.copyOf(byArray5, resultingSize);
                    Intrinsics.checkNotNullExpressionValue(byArray6, "copyOf(this, newSize)");
                    byArray2 = ArraysKt.copyInto(byArray4, byArray6, result.length, 0, extra.size());
                }
            }
            byArray = byArray2;
        }
        catch (Throwable throwable2) {
            throwable = throwable2;
            throw throwable2;
        }
        finally {
            CloseableKt.closeFinally(closeable, throwable);
        }
        return byArray;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static final void writeBytes(@NotNull File $this$writeBytes, @NotNull byte[] array) {
        Intrinsics.checkNotNullParameter($this$writeBytes, "<this>");
        Intrinsics.checkNotNullParameter(array, "array");
        Closeable closeable = new FileOutputStream($this$writeBytes);
        Throwable throwable = null;
        try {
            FileOutputStream it = (FileOutputStream)closeable;
            boolean bl = false;
            it.write(array);
            Unit unit = Unit.INSTANCE;
        }
        catch (Throwable throwable2) {
            throwable = throwable2;
            throw throwable2;
        }
        finally {
            CloseableKt.closeFinally(closeable, throwable);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static final void appendBytes(@NotNull File $this$appendBytes, @NotNull byte[] array) {
        Intrinsics.checkNotNullParameter($this$appendBytes, "<this>");
        Intrinsics.checkNotNullParameter(array, "array");
        Closeable closeable = new FileOutputStream($this$appendBytes, true);
        Throwable throwable = null;
        try {
            FileOutputStream it = (FileOutputStream)closeable;
            boolean bl = false;
            it.write(array);
            Unit unit = Unit.INSTANCE;
        }
        catch (Throwable throwable2) {
            throwable = throwable2;
            throw throwable2;
        }
        finally {
            CloseableKt.closeFinally(closeable, throwable);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @NotNull
    public static final String readText(@NotNull File $this$readText, @NotNull Charset charset) {
        String string;
        Object object;
        Intrinsics.checkNotNullParameter($this$readText, "<this>");
        Intrinsics.checkNotNullParameter(charset, "charset");
        Object object2 = object = $this$readText;
        object2 = new FileInputStream((File)object2);
        object = new InputStreamReader((InputStream)object2, charset);
        object2 = null;
        try {
            InputStreamReader it = (InputStreamReader)object;
            boolean bl = false;
            string = TextStreamsKt.readText(it);
        }
        catch (Throwable throwable) {
            object2 = throwable;
            throw throwable;
        }
        finally {
            CloseableKt.closeFinally((Closeable)object, (Throwable)object2);
        }
        return string;
    }

    public static /* synthetic */ String readText$default(File file, Charset charset, int n, Object object) {
        if ((n & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        return FilesKt.readText(file, charset);
    }

    public static final void writeText(@NotNull File $this$writeText, @NotNull String text, @NotNull Charset charset) {
        Intrinsics.checkNotNullParameter($this$writeText, "<this>");
        Intrinsics.checkNotNullParameter(text, "text");
        Intrinsics.checkNotNullParameter(charset, "charset");
        String string = text;
        byte[] byArray = string.getBytes(charset);
        Intrinsics.checkNotNullExpressionValue(byArray, "this as java.lang.String).getBytes(charset)");
        FilesKt.writeBytes($this$writeText, byArray);
    }

    public static /* synthetic */ void writeText$default(File file, String string, Charset charset, int n, Object object) {
        if ((n & 2) != 0) {
            charset = Charsets.UTF_8;
        }
        FilesKt.writeText(file, string, charset);
    }

    public static final void appendText(@NotNull File $this$appendText, @NotNull String text, @NotNull Charset charset) {
        Intrinsics.checkNotNullParameter($this$appendText, "<this>");
        Intrinsics.checkNotNullParameter(text, "text");
        Intrinsics.checkNotNullParameter(charset, "charset");
        String string = text;
        byte[] byArray = string.getBytes(charset);
        Intrinsics.checkNotNullExpressionValue(byArray, "this as java.lang.String).getBytes(charset)");
        FilesKt.appendBytes($this$appendText, byArray);
    }

    public static /* synthetic */ void appendText$default(File file, String string, Charset charset, int n, Object object) {
        if ((n & 2) != 0) {
            charset = Charsets.UTF_8;
        }
        FilesKt.appendText(file, string, charset);
    }

    public static final void forEachBlock(@NotNull File $this$forEachBlock, @NotNull Function2<? super byte[], ? super Integer, Unit> action) {
        Intrinsics.checkNotNullParameter($this$forEachBlock, "<this>");
        Intrinsics.checkNotNullParameter(action, "action");
        FilesKt.forEachBlock($this$forEachBlock, 4096, action);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static final void forEachBlock(@NotNull File $this$forEachBlock, int blockSize, @NotNull Function2<? super byte[], ? super Integer, Unit> action) {
        Intrinsics.checkNotNullParameter($this$forEachBlock, "<this>");
        Intrinsics.checkNotNullParameter(action, "action");
        byte[] arr = new byte[RangesKt.coerceAtLeast(blockSize, 512)];
        Closeable closeable = new FileInputStream($this$forEachBlock);
        Throwable throwable = null;
        try {
            int size;
            FileInputStream input = (FileInputStream)closeable;
            boolean bl = false;
            while ((size = input.read(arr)) > 0) {
                action.invoke((byte[])arr, (Integer)size);
            }
            Unit unit = Unit.INSTANCE;
        }
        catch (Throwable throwable2) {
            throwable = throwable2;
            throw throwable2;
        }
        finally {
            CloseableKt.closeFinally(closeable, throwable);
        }
    }

    public static final void forEachLine(@NotNull File $this$forEachLine, @NotNull Charset charset, @NotNull Function1<? super String, Unit> action) {
        Intrinsics.checkNotNullParameter($this$forEachLine, "<this>");
        Intrinsics.checkNotNullParameter(charset, "charset");
        Intrinsics.checkNotNullParameter(action, "action");
        TextStreamsKt.forEachLine(new BufferedReader(new InputStreamReader((InputStream)new FileInputStream($this$forEachLine), charset)), action);
    }

    public static /* synthetic */ void forEachLine$default(File file, Charset charset, Function1 function1, int n, Object object) {
        if ((n & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        FilesKt.forEachLine(file, charset, function1);
    }

    @InlineOnly
    private static final FileInputStream inputStream(File $this$inputStream) {
        Intrinsics.checkNotNullParameter($this$inputStream, "<this>");
        return new FileInputStream($this$inputStream);
    }

    @InlineOnly
    private static final FileOutputStream outputStream(File $this$outputStream) {
        Intrinsics.checkNotNullParameter($this$outputStream, "<this>");
        return new FileOutputStream($this$outputStream);
    }

    @NotNull
    public static final List<String> readLines(@NotNull File $this$readLines, @NotNull Charset charset) {
        Intrinsics.checkNotNullParameter($this$readLines, "<this>");
        Intrinsics.checkNotNullParameter(charset, "charset");
        ArrayList<String> result = new ArrayList<String>();
        FilesKt.forEachLine($this$readLines, charset, (Function1<? super String, Unit>)new Function1<String, Unit>(result){
            final /* synthetic */ ArrayList<String> $result;
            {
                this.$result = $result;
                super(1);
            }

            public final void invoke(@NotNull String it) {
                Intrinsics.checkNotNullParameter(it, "it");
                this.$result.add(it);
            }
        });
        return result;
    }

    public static /* synthetic */ List readLines$default(File file, Charset charset, int n, Object object) {
        if ((n & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        return FilesKt.readLines(file, charset);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static final <T> T useLines(@NotNull File $this$useLines, @NotNull Charset charset, @NotNull Function1<? super Sequence<String>, ? extends T> block) {
        Object object;
        Intrinsics.checkNotNullParameter($this$useLines, "<this>");
        Intrinsics.checkNotNullParameter(charset, "charset");
        Intrinsics.checkNotNullParameter(block, "block");
        boolean $i$f$useLines = false;
        Object object2 = $this$useLines;
        int n = 8192;
        Object object3 = object = object2;
        object3 = new FileInputStream((File)object3);
        object = new InputStreamReader((InputStream)object3, charset);
        object2 = object instanceof BufferedReader ? (BufferedReader)object : new BufferedReader((Reader)object, n);
        Throwable throwable = null;
        try {
            BufferedReader it = (BufferedReader)object2;
            boolean bl = false;
            object = block.invoke(TextStreamsKt.lineSequence(it));
        }
        catch (Throwable throwable2) {
            throwable = throwable2;
            throw throwable2;
        }
        finally {
            InlineMarker.finallyStart(1);
            CloseableKt.closeFinally((Closeable)object2, throwable);
            InlineMarker.finallyEnd(1);
        }
        return (T)object;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * WARNING - void declaration
     */
    public static /* synthetic */ Object useLines$default(File $this$useLines_u24default, Charset charset, Function1 block, int n, Object object) {
        void $this$useLines$iv;
        if ((n & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        File file = $this$useLines_u24default;
        Charset charset$iv = charset;
        boolean $i$f$useLines = false;
        Closeable closeable = $this$useLines$iv;
        int n2 = 8192;
        Reader reader = closeable;
        InputStream inputStream = reader;
        reader = new InputStreamReader(inputStream = (InputStream)new FileInputStream((File)((Object)inputStream)), charset$iv);
        closeable = reader instanceof BufferedReader ? (BufferedReader)reader : new BufferedReader(reader, n2);
        Throwable throwable = null;
        try {
            BufferedReader it$iv = (BufferedReader)closeable;
            boolean bl = false;
            reader = block.invoke(TextStreamsKt.lineSequence(it$iv));
        }
        catch (Throwable throwable2) {
            throwable = throwable2;
            throw throwable2;
        }
        finally {
            InlineMarker.finallyStart(1);
            CloseableKt.closeFinally(closeable, throwable);
            InlineMarker.finallyEnd(1);
        }
        return reader;
    }
}

