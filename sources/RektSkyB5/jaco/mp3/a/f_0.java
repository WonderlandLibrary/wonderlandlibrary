/*
 * Decompiled with CFR 0.152.
 */
package jaco.mp3.a;

import jaco.mp3.a.a.a;
import jaco.mp3.a.b;
import jaco.mp3.a.q;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/*
 * Renamed from jaco.mp3.a.F
 */
public final class f_0
extends a {
    private SourceDataLine a = null;
    private AudioFormat b = null;
    private byte[] c = new byte[4096];

    @Override
    protected final void b() {
        if (this.a != null) {
            this.a.close();
        }
    }

    @Override
    protected final void b(short[] objectArray, int n2, int n3) {
        if (this.a == null) {
            f_0 f_02 = this;
            Throwable throwable = null;
            try {
                f_0 f_03 = f_02;
                Object object = f_03;
                object = f_03;
                if (f_03.b == null) {
                    b b2 = ((a)object).e();
                    ((f_0)object).b = new AudioFormat(b2.a(), 16, b2.b(), true, false);
                }
                object = ((f_0)object).b;
                object = new DataLine.Info(SourceDataLine.class, (AudioFormat)object);
                if ((object = AudioSystem.getLine((Line.Info)object)) instanceof SourceDataLine) {
                    f_02.a = (SourceDataLine)object;
                    f_02.a.open(f_02.b);
                    f_02.a.start();
                }
            }
            catch (RuntimeException runtimeException) {
                RuntimeException runtimeException2 = runtimeException;
                throwable = runtimeException;
            }
            catch (LinkageError linkageError) {
                LinkageError linkageError2 = linkageError;
                throwable = linkageError;
            }
            catch (LineUnavailableException lineUnavailableException) {
                LineUnavailableException lineUnavailableException2 = lineUnavailableException;
                throwable = lineUnavailableException;
            }
            if (f_02.a == null) {
                throw new q("cannot obtain source audio line", throwable);
            }
        }
        objectArray = this.c((short[])objectArray, n2, n3);
        this.a.write((byte[])objectArray, 0, n3 << 1);
    }

    private byte[] c(short[] sArray, int n2, int n3) {
        int n4 = n3 << 1;
        if (((f_0)object).c.length < n4) {
            ((f_0)object).c = new byte[n4 + 1024];
        }
        Object object = ((f_0)object).c;
        n4 = 0;
        while (n3-- > 0) {
            short s2 = sArray[n2++];
            object[n4++] = (byte)s2;
            object[n4++] = (byte)(s2 >>> 8);
        }
        return object;
    }

    @Override
    protected final void d() {
        if (this.a != null) {
            this.a.drain();
        }
    }
}

