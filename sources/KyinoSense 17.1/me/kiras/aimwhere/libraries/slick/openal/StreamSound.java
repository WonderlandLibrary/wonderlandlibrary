/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.openal.AL10
 */
package me.kiras.aimwhere.libraries.slick.openal;

import java.io.IOException;
import java.nio.IntBuffer;
import me.kiras.aimwhere.libraries.slick.openal.AudioImpl;
import me.kiras.aimwhere.libraries.slick.openal.OpenALStreamPlayer;
import me.kiras.aimwhere.libraries.slick.openal.SoundStore;
import me.kiras.aimwhere.libraries.slick.util.Log;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

public class StreamSound
extends AudioImpl {
    private OpenALStreamPlayer player;

    public StreamSound(OpenALStreamPlayer player) {
        this.player = player;
    }

    @Override
    public boolean isPlaying() {
        return SoundStore.get().isPlaying(this.player);
    }

    @Override
    public int playAsMusic(float pitch, float gain, boolean loop) {
        try {
            this.cleanUpSource();
            this.player.setup(pitch);
            this.player.play(loop);
            SoundStore.get().setStream(this.player);
        }
        catch (IOException e) {
            Log.error("Failed to read OGG source: " + this.player.getSource());
        }
        return SoundStore.get().getSource(0);
    }

    private void cleanUpSource() {
        SoundStore store = SoundStore.get();
        AL10.alSourceStop((int)store.getSource(0));
        IntBuffer buffer = BufferUtils.createIntBuffer((int)1);
        for (int queued = AL10.alGetSourcei((int)store.getSource(0), (int)4117); queued > 0; --queued) {
            AL10.alSourceUnqueueBuffers((int)store.getSource(0), (IntBuffer)buffer);
        }
        AL10.alSourcei((int)store.getSource(0), (int)4105, (int)0);
    }

    @Override
    public int playAsSoundEffect(float pitch, float gain, boolean loop, float x, float y, float z) {
        return this.playAsMusic(pitch, gain, loop);
    }

    @Override
    public int playAsSoundEffect(float pitch, float gain, boolean loop) {
        return this.playAsMusic(pitch, gain, loop);
    }

    @Override
    public void stop() {
        SoundStore.get().setStream(null);
    }

    @Override
    public boolean setPosition(float position) {
        return this.player.setPosition(position);
    }

    @Override
    public float getPosition() {
        return this.player.getPosition();
    }
}

