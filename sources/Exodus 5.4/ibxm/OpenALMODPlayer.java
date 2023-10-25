/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.LWJGLException
 *  org.lwjgl.openal.AL
 *  org.lwjgl.openal.AL10
 */
package ibxm;

import ibxm.FastTracker2;
import ibxm.IBXM;
import ibxm.Module;
import ibxm.ProTracker;
import ibxm.ScreamTracker3;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;

public class OpenALMODPlayer {
    private IBXM ibxm;
    private int remainingBufferCount;
    private int songDuration;
    private ByteBuffer bufferData;
    private byte[] data = new byte[163840];
    private static final int sectionSize = 40960;
    private Module module;
    private IntBuffer unqueued;
    private int source;
    private boolean soundWorks = true;
    private boolean done = true;
    private IntBuffer bufferNames;
    private boolean loop;

    public static Module loadModule(InputStream inputStream) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        Module module = null;
        byte[] byArray = new byte[60];
        dataInputStream.readFully(byArray);
        if (FastTracker2.is_xm(byArray)) {
            module = FastTracker2.load_xm(byArray, dataInputStream);
        } else {
            byte[] byArray2 = new byte[96];
            System.arraycopy(byArray, 0, byArray2, 0, 60);
            dataInputStream.readFully(byArray2, 60, 36);
            if (ScreamTracker3.is_s3m(byArray2)) {
                module = ScreamTracker3.load_s3m(byArray2, dataInputStream);
            } else {
                byte[] byArray3 = new byte[1084];
                System.arraycopy(byArray2, 0, byArray3, 0, 96);
                dataInputStream.readFully(byArray3, 96, 988);
                module = ProTracker.load_mod(byArray3, dataInputStream);
            }
        }
        dataInputStream.close();
        return module;
    }

    public boolean stream(int n) {
        int n2 = 40960;
        boolean bl = false;
        boolean bl2 = true;
        if (n2 > this.songDuration) {
            n2 = this.songDuration;
            bl = true;
        }
        this.ibxm.get_audio(this.data, n2);
        this.bufferData.clear();
        this.bufferData.put(this.data);
        this.bufferData.limit(n2 * 4);
        if (bl) {
            if (this.loop) {
                this.ibxm.seek(0);
                this.ibxm.set_module(this.module);
                this.songDuration = this.ibxm.calculate_song_duration();
            } else {
                bl2 = false;
                this.songDuration -= n2;
            }
        } else {
            this.songDuration -= n2;
        }
        this.bufferData.flip();
        AL10.alBufferData((int)n, (int)4355, (ByteBuffer)this.bufferData, (int)48000);
        return bl2;
    }

    public void init() {
        try {
            AL.create();
            this.soundWorks = true;
        }
        catch (LWJGLException lWJGLException) {
            System.err.println("Failed to initialise LWJGL OpenAL");
            this.soundWorks = false;
            return;
        }
        if (this.soundWorks) {
            IntBuffer intBuffer = BufferUtils.createIntBuffer((int)1);
            AL10.alGenSources((IntBuffer)intBuffer);
            if (AL10.alGetError() != 0) {
                System.err.println("Failed to create sources");
                this.soundWorks = false;
            } else {
                this.source = intBuffer.get(0);
            }
        }
    }

    public boolean done() {
        return this.done;
    }

    public void play(InputStream inputStream, boolean bl, boolean bl2) throws IOException {
        this.play(this.source, inputStream, bl, bl2);
    }

    public void play(Module module, int n, boolean bl, boolean bl2) {
        this.source = n;
        this.loop = bl;
        this.module = module;
        this.done = false;
        this.ibxm = new IBXM(48000);
        this.ibxm.set_module(module);
        this.songDuration = this.ibxm.calculate_song_duration();
        if (this.bufferNames != null) {
            AL10.alSourceStop((int)n);
            this.bufferNames.flip();
            AL10.alDeleteBuffers((IntBuffer)this.bufferNames);
        }
        this.bufferNames = BufferUtils.createIntBuffer((int)2);
        AL10.alGenBuffers((IntBuffer)this.bufferNames);
        this.remainingBufferCount = 2;
        int n2 = 0;
        while (n2 < 2) {
            this.stream(this.bufferNames.get(n2));
            ++n2;
        }
        AL10.alSourceQueueBuffers((int)n, (IntBuffer)this.bufferNames);
        AL10.alSourcef((int)n, (int)4099, (float)1.0f);
        AL10.alSourcef((int)n, (int)4106, (float)1.0f);
        if (bl2) {
            AL10.alSourcePlay((int)n);
        }
    }

    public OpenALMODPlayer() {
        this.bufferData = BufferUtils.createByteBuffer((int)163840);
        this.unqueued = BufferUtils.createIntBuffer((int)1);
    }

    public void setup(float f, float f2) {
        AL10.alSourcef((int)this.source, (int)4099, (float)f);
        AL10.alSourcef((int)this.source, (int)4106, (float)f2);
    }

    public void update() {
        if (this.done) {
            return;
        }
        int n = AL10.alGetSourcei((int)this.source, (int)4118);
        while (n > 0) {
            this.unqueued.clear();
            AL10.alSourceUnqueueBuffers((int)this.source, (IntBuffer)this.unqueued);
            if (this.stream(this.unqueued.get(0))) {
                AL10.alSourceQueueBuffers((int)this.source, (IntBuffer)this.unqueued);
            } else {
                --this.remainingBufferCount;
                if (this.remainingBufferCount == 0) {
                    this.done = true;
                }
            }
            --n;
        }
        int n2 = AL10.alGetSourcei((int)this.source, (int)4112);
        if (n2 != 4114) {
            AL10.alSourcePlay((int)this.source);
        }
    }

    public void play(int n, InputStream inputStream, boolean bl, boolean bl2) throws IOException {
        if (!this.soundWorks) {
            return;
        }
        this.done = false;
        this.loop = bl;
        this.source = n;
        this.module = OpenALMODPlayer.loadModule(inputStream);
        this.play(this.module, n, bl, bl2);
    }
}

