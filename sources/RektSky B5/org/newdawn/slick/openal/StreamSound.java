/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.openal;

import java.io.IOException;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.newdawn.slick.openal.AudioImpl;
import org.newdawn.slick.openal.OpenALStreamPlayer;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.util.Log;

public class StreamSound
extends AudioImpl {
    private OpenALStreamPlayer player;

    public StreamSound(OpenALStreamPlayer player) {
        this.player = player;
    }

    public boolean isPlaying() {
        return SoundStore.get().isPlaying(this.player);
    }

    public int playAsMusic(float pitch, float gain, boolean loop) {
        try {
            this.cleanUpSource();
            this.player.setup(pitch);
            this.player.play(loop);
            SoundStore.get().setStream(this.player);
        }
        catch (IOException e2) {
            Log.error("Failed to read OGG source: " + this.player.getSource());
        }
        return SoundStore.get().getSource(0);
    }

    private void cleanUpSource() {
        SoundStore store = SoundStore.get();
        AL10.alSourceStop(store.getSource(0));
        IntBuffer buffer = BufferUtils.createIntBuffer(1);
        for (int queued = AL10.alGetSourcei(store.getSource(0), 4117); queued > 0; --queued) {
            AL10.alSourceUnqueueBuffers(store.getSource(0), buffer);
        }
        AL10.alSourcei(store.getSource(0), 4105, 0);
    }

    public int playAsSoundEffect(float pitch, float gain, boolean loop, float x2, float y2, float z2) {
        return this.playAsMusic(pitch, gain, loop);
    }

    public int playAsSoundEffect(float pitch, float gain, boolean loop) {
        return this.playAsMusic(pitch, gain, loop);
    }

    public void stop() {
        SoundStore.get().setStream(null);
    }

    public boolean setPosition(float position) {
        return this.player.setPosition(position);
    }

    public float getPosition() {
        return this.player.getPosition();
    }
}

