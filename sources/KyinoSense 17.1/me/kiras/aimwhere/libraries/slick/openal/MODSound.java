/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ibxm.FastTracker2
 *  ibxm.IBXM
 *  ibxm.Module
 *  ibxm.ProTracker
 *  ibxm.ScreamTracker3
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.LWJGLException
 *  org.lwjgl.openal.AL
 *  org.lwjgl.openal.AL10
 */
package me.kiras.aimwhere.libraries.slick.openal;

import ibxm.FastTracker2;
import ibxm.IBXM;
import ibxm.Module;
import ibxm.ProTracker;
import ibxm.ScreamTracker3;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import me.kiras.aimwhere.libraries.slick.openal.AudioImpl;
import me.kiras.aimwhere.libraries.slick.openal.SoundStore;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;

public class MODSound
extends AudioImpl {
    private static OpenALMODPlayer player = new OpenALMODPlayer();
    private Module module;
    private SoundStore store;

    public MODSound(SoundStore store, InputStream in) throws IOException {
        this.store = store;
        this.module = OpenALMODPlayer.loadModule(in);
    }

    @Override
    public int playAsMusic(float pitch, float gain, boolean loop) {
        this.cleanUpSource();
        player.play(this.module, this.store.getSource(0), loop, SoundStore.get().isMusicOn());
        player.setup(pitch, 1.0f);
        this.store.setCurrentMusicVolume(gain);
        this.store.setMOD(this);
        return this.store.getSource(0);
    }

    private void cleanUpSource() {
        AL10.alSourceStop((int)this.store.getSource(0));
        IntBuffer buffer = BufferUtils.createIntBuffer((int)1);
        for (int queued = AL10.alGetSourcei((int)this.store.getSource(0), (int)4117); queued > 0; --queued) {
            AL10.alSourceUnqueueBuffers((int)this.store.getSource(0), (IntBuffer)buffer);
        }
        AL10.alSourcei((int)this.store.getSource(0), (int)4105, (int)0);
    }

    public void poll() {
        player.update();
    }

    @Override
    public int playAsSoundEffect(float pitch, float gain, boolean loop) {
        return -1;
    }

    @Override
    public void stop() {
        this.store.setMOD(null);
    }

    @Override
    public float getPosition() {
        throw new RuntimeException("Positioning on modules is not currently supported");
    }

    @Override
    public boolean setPosition(float position) {
        throw new RuntimeException("Positioning on modules is not currently supported");
    }

    static class OpenALMODPlayer {
        private static final int sectionSize = 40960;
        private IntBuffer bufferNames;
        private IBXM ibxm;
        private int songDuration;
        private byte[] data = new byte[163840];
        private ByteBuffer bufferData = BufferUtils.createByteBuffer((int)163840);
        private IntBuffer unqueued = BufferUtils.createIntBuffer((int)1);
        private int source;
        private boolean soundWorks = true;
        private Module module;
        private boolean loop;
        private boolean done = true;
        private int remainingBufferCount;

        OpenALMODPlayer() {
        }

        public void init() {
            try {
                AL.create();
                this.soundWorks = true;
            }
            catch (LWJGLException e) {
                System.err.println("Failed to initialise LWJGL OpenAL");
                this.soundWorks = false;
                return;
            }
            if (this.soundWorks) {
                IntBuffer sources = BufferUtils.createIntBuffer((int)1);
                AL10.alGenSources((IntBuffer)sources);
                if (AL10.alGetError() != 0) {
                    System.err.println("Failed to create sources");
                    this.soundWorks = false;
                } else {
                    this.source = sources.get(0);
                }
            }
        }

        public void play(InputStream in, boolean loop, boolean start) throws IOException {
            this.play(this.source, in, loop, start);
        }

        public void play(int source, InputStream in, boolean loop, boolean start) throws IOException {
            if (!this.soundWorks) {
                return;
            }
            this.done = false;
            this.loop = loop;
            this.source = source;
            this.module = OpenALMODPlayer.loadModule(in);
            this.play(this.module, source, loop, start);
        }

        public void play(Module module, int source, boolean loop, boolean start) {
            this.source = source;
            this.loop = loop;
            this.module = module;
            this.done = false;
            this.ibxm = new IBXM(48000);
            this.ibxm.set_module(module);
            this.songDuration = this.ibxm.calculate_song_duration();
            if (this.bufferNames != null) {
                AL10.alSourceStop((int)source);
                this.bufferNames.flip();
                AL10.alDeleteBuffers((IntBuffer)this.bufferNames);
            }
            this.bufferNames = BufferUtils.createIntBuffer((int)2);
            AL10.alGenBuffers((IntBuffer)this.bufferNames);
            this.remainingBufferCount = 2;
            for (int i = 0; i < 2; ++i) {
                this.stream(this.bufferNames.get(i));
            }
            AL10.alSourceQueueBuffers((int)source, (IntBuffer)this.bufferNames);
            AL10.alSourcef((int)source, (int)4099, (float)1.0f);
            AL10.alSourcef((int)source, (int)4106, (float)1.0f);
            if (start) {
                AL10.alSourcePlay((int)source);
            }
        }

        public void setup(float pitch, float gain) {
            AL10.alSourcef((int)this.source, (int)4099, (float)pitch);
            AL10.alSourcef((int)this.source, (int)4106, (float)gain);
        }

        public boolean done() {
            return this.done;
        }

        public static Module loadModule(InputStream in) throws IOException {
            DataInputStream din = new DataInputStream(in);
            Module module = null;
            byte[] xm_header = new byte[60];
            din.readFully(xm_header);
            if (FastTracker2.is_xm((byte[])xm_header)) {
                module = FastTracker2.load_xm((byte[])xm_header, (DataInput)din);
            } else {
                byte[] s3m_header = new byte[96];
                System.arraycopy(xm_header, 0, s3m_header, 0, 60);
                din.readFully(s3m_header, 60, 36);
                if (ScreamTracker3.is_s3m((byte[])s3m_header)) {
                    module = ScreamTracker3.load_s3m((byte[])s3m_header, (DataInput)din);
                } else {
                    byte[] mod_header = new byte[1084];
                    System.arraycopy(s3m_header, 0, mod_header, 0, 96);
                    din.readFully(mod_header, 96, 988);
                    module = ProTracker.load_mod((byte[])mod_header, (DataInput)din);
                }
            }
            din.close();
            return module;
        }

        public void update() {
            if (this.done) {
                return;
            }
            for (int processed = AL10.alGetSourcei((int)this.source, (int)4118); processed > 0; --processed) {
                this.unqueued.clear();
                AL10.alSourceUnqueueBuffers((int)this.source, (IntBuffer)this.unqueued);
                if (this.stream(this.unqueued.get(0))) {
                    AL10.alSourceQueueBuffers((int)this.source, (IntBuffer)this.unqueued);
                    continue;
                }
                --this.remainingBufferCount;
                if (this.remainingBufferCount != 0) continue;
                this.done = true;
            }
            int state = AL10.alGetSourcei((int)this.source, (int)4112);
            if (state != 4114) {
                AL10.alSourcePlay((int)this.source);
            }
        }

        public boolean stream(int bufferId) {
            int frames = 40960;
            boolean reset = false;
            boolean more = true;
            if (frames > this.songDuration) {
                frames = this.songDuration;
                reset = true;
            }
            this.ibxm.get_audio(this.data, frames);
            this.bufferData.clear();
            this.bufferData.put(this.data);
            this.bufferData.limit(frames * 4);
            if (reset) {
                if (this.loop) {
                    this.ibxm.seek(0);
                    this.ibxm.set_module(this.module);
                    this.songDuration = this.ibxm.calculate_song_duration();
                } else {
                    more = false;
                    this.songDuration -= frames;
                }
            } else {
                this.songDuration -= frames;
            }
            this.bufferData.flip();
            AL10.alBufferData((int)bufferId, (int)4355, (ByteBuffer)this.bufferData, (int)48000);
            return more;
        }
    }
}

