/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.NoSuchElementException;
import kotlin.Deprecated;
import kotlin.DeprecatedSinceKotlin;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.SinceKotlin;
import kotlin.collections.ByteIterator;
import kotlin.internal.InlineOnly;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=2, xi=48, d1={"\u0000Z\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0017\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0017\u0010\u0000\u001a\u00020\u0005*\u00020\u00062\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0017\u0010\u0007\u001a\u00020\b*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a\u0017\u0010\u000b\u001a\u00020\f*\u00020\u00062\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a\u0017\u0010\r\u001a\u00020\u000e*\u00020\u000f2\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a\u001c\u0010\u0010\u001a\u00020\u0011*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00062\b\b\u0002\u0010\u0003\u001a\u00020\u0004\u001a\r\u0010\u0013\u001a\u00020\u000e*\u00020\u0014H\u0087\b\u001a\u001d\u0010\u0013\u001a\u00020\u000e*\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u0004H\u0087\b\u001a\r\u0010\u0017\u001a\u00020\u0018*\u00020\u0001H\u0086\u0002\u001a\f\u0010\u0019\u001a\u00020\u0014*\u00020\u0002H\u0007\u001a\u0016\u0010\u0019\u001a\u00020\u0014*\u00020\u00022\b\b\u0002\u0010\u001a\u001a\u00020\u0004H\u0007\u001a\u0017\u0010\u001b\u001a\u00020\u001c*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a\u0017\u0010\u001d\u001a\u00020\u001e*\u00020\u00062\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u00a8\u0006\u001f"}, d2={"buffered", "Ljava/io/BufferedInputStream;", "Ljava/io/InputStream;", "bufferSize", "", "Ljava/io/BufferedOutputStream;", "Ljava/io/OutputStream;", "bufferedReader", "Ljava/io/BufferedReader;", "charset", "Ljava/nio/charset/Charset;", "bufferedWriter", "Ljava/io/BufferedWriter;", "byteInputStream", "Ljava/io/ByteArrayInputStream;", "", "copyTo", "", "out", "inputStream", "", "offset", "length", "iterator", "Lkotlin/collections/ByteIterator;", "readBytes", "estimatedSize", "reader", "Ljava/io/InputStreamReader;", "writer", "Ljava/io/OutputStreamWriter;", "kotlin-stdlib"})
@JvmName(name="ByteStreamsKt")
public final class ByteStreamsKt {
    @NotNull
    public static final ByteIterator iterator(@NotNull BufferedInputStream $this$iterator) {
        Intrinsics.checkNotNullParameter($this$iterator, "<this>");
        return new ByteIterator($this$iterator){
            private int nextByte;
            private boolean nextPrepared;
            private boolean finished;
            final /* synthetic */ BufferedInputStream $this_iterator;
            {
                this.$this_iterator = $receiver;
                this.nextByte = -1;
            }

            public final int getNextByte() {
                return this.nextByte;
            }

            public final void setNextByte(int n) {
                this.nextByte = n;
            }

            public final boolean getNextPrepared() {
                return this.nextPrepared;
            }

            public final void setNextPrepared(boolean bl) {
                this.nextPrepared = bl;
            }

            public final boolean getFinished() {
                return this.finished;
            }

            public final void setFinished(boolean bl) {
                this.finished = bl;
            }

            private final void prepareNext() {
                if (!this.nextPrepared && !this.finished) {
                    this.nextByte = this.$this_iterator.read();
                    this.nextPrepared = true;
                    this.finished = this.nextByte == -1;
                }
            }

            public boolean hasNext() {
                this.prepareNext();
                return !this.finished;
            }

            public byte nextByte() {
                this.prepareNext();
                if (this.finished) {
                    throw new NoSuchElementException("Input stream is over.");
                }
                byte res = (byte)this.nextByte;
                this.nextPrepared = false;
                return res;
            }
        };
    }

    @InlineOnly
    private static final ByteArrayInputStream byteInputStream(String $this$byteInputStream, Charset charset) {
        Intrinsics.checkNotNullParameter($this$byteInputStream, "<this>");
        Intrinsics.checkNotNullParameter(charset, "charset");
        String string = $this$byteInputStream;
        byte[] byArray = string.getBytes(charset);
        Intrinsics.checkNotNullExpressionValue(byArray, "this as java.lang.String).getBytes(charset)");
        return new ByteArrayInputStream(byArray);
    }

    static /* synthetic */ ByteArrayInputStream byteInputStream$default(String $this$byteInputStream_u24default, Charset charset, int n, Object object) {
        if ((n & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        Intrinsics.checkNotNullParameter($this$byteInputStream_u24default, "<this>");
        Intrinsics.checkNotNullParameter(charset, "charset");
        String string = $this$byteInputStream_u24default;
        object = string.getBytes(charset);
        Intrinsics.checkNotNullExpressionValue(object, "this as java.lang.String).getBytes(charset)");
        return new ByteArrayInputStream((byte[])object);
    }

    @InlineOnly
    private static final ByteArrayInputStream inputStream(byte[] $this$inputStream) {
        Intrinsics.checkNotNullParameter($this$inputStream, "<this>");
        return new ByteArrayInputStream($this$inputStream);
    }

    @InlineOnly
    private static final ByteArrayInputStream inputStream(byte[] $this$inputStream, int offset, int length) {
        Intrinsics.checkNotNullParameter($this$inputStream, "<this>");
        return new ByteArrayInputStream($this$inputStream, offset, length);
    }

    @InlineOnly
    private static final BufferedInputStream buffered(InputStream $this$buffered, int bufferSize) {
        Intrinsics.checkNotNullParameter($this$buffered, "<this>");
        return $this$buffered instanceof BufferedInputStream ? (BufferedInputStream)$this$buffered : new BufferedInputStream($this$buffered, bufferSize);
    }

    static /* synthetic */ BufferedInputStream buffered$default(InputStream $this$buffered_u24default, int bufferSize, int n, Object object) {
        if ((n & 1) != 0) {
            bufferSize = 8192;
        }
        Intrinsics.checkNotNullParameter($this$buffered_u24default, "<this>");
        return $this$buffered_u24default instanceof BufferedInputStream ? (BufferedInputStream)$this$buffered_u24default : new BufferedInputStream($this$buffered_u24default, bufferSize);
    }

    @InlineOnly
    private static final InputStreamReader reader(InputStream $this$reader, Charset charset) {
        Intrinsics.checkNotNullParameter($this$reader, "<this>");
        Intrinsics.checkNotNullParameter(charset, "charset");
        return new InputStreamReader($this$reader, charset);
    }

    static /* synthetic */ InputStreamReader reader$default(InputStream $this$reader_u24default, Charset charset, int n, Object object) {
        if ((n & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        Intrinsics.checkNotNullParameter($this$reader_u24default, "<this>");
        Intrinsics.checkNotNullParameter(charset, "charset");
        return new InputStreamReader($this$reader_u24default, charset);
    }

    @InlineOnly
    private static final BufferedReader bufferedReader(InputStream $this$bufferedReader, Charset charset) {
        Intrinsics.checkNotNullParameter($this$bufferedReader, "<this>");
        Intrinsics.checkNotNullParameter(charset, "charset");
        Closeable closeable = $this$bufferedReader;
        closeable = new InputStreamReader((InputStream)closeable, charset);
        int n = 8192;
        return closeable instanceof BufferedReader ? (BufferedReader)closeable : new BufferedReader((Reader)closeable, n);
    }

    static /* synthetic */ BufferedReader bufferedReader$default(InputStream $this$bufferedReader_u24default, Charset charset, int n, Object object) {
        if ((n & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        Intrinsics.checkNotNullParameter($this$bufferedReader_u24default, "<this>");
        Intrinsics.checkNotNullParameter(charset, "charset");
        Closeable closeable = $this$bufferedReader_u24default;
        closeable = new InputStreamReader((InputStream)closeable, charset);
        int n2 = 8192;
        return closeable instanceof BufferedReader ? (BufferedReader)closeable : new BufferedReader((Reader)closeable, n2);
    }

    @InlineOnly
    private static final BufferedOutputStream buffered(OutputStream $this$buffered, int bufferSize) {
        Intrinsics.checkNotNullParameter($this$buffered, "<this>");
        return $this$buffered instanceof BufferedOutputStream ? (BufferedOutputStream)$this$buffered : new BufferedOutputStream($this$buffered, bufferSize);
    }

    static /* synthetic */ BufferedOutputStream buffered$default(OutputStream $this$buffered_u24default, int bufferSize, int n, Object object) {
        if ((n & 1) != 0) {
            bufferSize = 8192;
        }
        Intrinsics.checkNotNullParameter($this$buffered_u24default, "<this>");
        return $this$buffered_u24default instanceof BufferedOutputStream ? (BufferedOutputStream)$this$buffered_u24default : new BufferedOutputStream($this$buffered_u24default, bufferSize);
    }

    @InlineOnly
    private static final OutputStreamWriter writer(OutputStream $this$writer, Charset charset) {
        Intrinsics.checkNotNullParameter($this$writer, "<this>");
        Intrinsics.checkNotNullParameter(charset, "charset");
        return new OutputStreamWriter($this$writer, charset);
    }

    static /* synthetic */ OutputStreamWriter writer$default(OutputStream $this$writer_u24default, Charset charset, int n, Object object) {
        if ((n & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        Intrinsics.checkNotNullParameter($this$writer_u24default, "<this>");
        Intrinsics.checkNotNullParameter(charset, "charset");
        return new OutputStreamWriter($this$writer_u24default, charset);
    }

    @InlineOnly
    private static final BufferedWriter bufferedWriter(OutputStream $this$bufferedWriter, Charset charset) {
        Intrinsics.checkNotNullParameter($this$bufferedWriter, "<this>");
        Intrinsics.checkNotNullParameter(charset, "charset");
        Closeable closeable = $this$bufferedWriter;
        closeable = new OutputStreamWriter((OutputStream)closeable, charset);
        int n = 8192;
        return closeable instanceof BufferedWriter ? (BufferedWriter)closeable : new BufferedWriter((Writer)closeable, n);
    }

    static /* synthetic */ BufferedWriter bufferedWriter$default(OutputStream $this$bufferedWriter_u24default, Charset charset, int n, Object object) {
        if ((n & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        Intrinsics.checkNotNullParameter($this$bufferedWriter_u24default, "<this>");
        Intrinsics.checkNotNullParameter(charset, "charset");
        Closeable closeable = $this$bufferedWriter_u24default;
        closeable = new OutputStreamWriter((OutputStream)closeable, charset);
        int n2 = 8192;
        return closeable instanceof BufferedWriter ? (BufferedWriter)closeable : new BufferedWriter((Writer)closeable, n2);
    }

    public static final long copyTo(@NotNull InputStream $this$copyTo, @NotNull OutputStream out, int bufferSize) {
        Intrinsics.checkNotNullParameter($this$copyTo, "<this>");
        Intrinsics.checkNotNullParameter(out, "out");
        long bytesCopied = 0L;
        byte[] buffer = new byte[bufferSize];
        int bytes = $this$copyTo.read(buffer);
        while (bytes >= 0) {
            out.write(buffer, 0, bytes);
            bytesCopied += (long)bytes;
            bytes = $this$copyTo.read(buffer);
        }
        return bytesCopied;
    }

    public static /* synthetic */ long copyTo$default(InputStream inputStream, OutputStream outputStream, int n, int n2, Object object) {
        if ((n2 & 2) != 0) {
            n = 8192;
        }
        return ByteStreamsKt.copyTo(inputStream, outputStream, n);
    }

    @Deprecated(message="Use readBytes() overload without estimatedSize parameter", replaceWith=@ReplaceWith(expression="readBytes()", imports={}))
    @DeprecatedSinceKotlin(warningSince="1.3", errorSince="1.5")
    @NotNull
    public static final byte[] readBytes(@NotNull InputStream $this$readBytes, int estimatedSize) {
        Intrinsics.checkNotNullParameter($this$readBytes, "<this>");
        int n = $this$readBytes.available();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(Math.max(estimatedSize, n));
        ByteStreamsKt.copyTo$default($this$readBytes, buffer, 0, 2, null);
        byte[] byArray = buffer.toByteArray();
        Intrinsics.checkNotNullExpressionValue(byArray, "buffer.toByteArray()");
        return byArray;
    }

    public static /* synthetic */ byte[] readBytes$default(InputStream inputStream, int n, int n2, Object object) {
        if ((n2 & 1) != 0) {
            n = 8192;
        }
        return ByteStreamsKt.readBytes(inputStream, n);
    }

    @SinceKotlin(version="1.3")
    @NotNull
    public static final byte[] readBytes(@NotNull InputStream $this$readBytes) {
        Intrinsics.checkNotNullParameter($this$readBytes, "<this>");
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(Math.max(8192, $this$readBytes.available()));
        ByteStreamsKt.copyTo$default($this$readBytes, buffer, 0, 2, null);
        byte[] byArray = buffer.toByteArray();
        Intrinsics.checkNotNullExpressionValue(byArray, "buffer.toByteArray()");
        return byArray;
    }
}

