/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.util.internal.ThreadLocalRandom
 *  paulscode.sound.SoundSystem
 *  paulscode.sound.SoundSystemConfig
 *  paulscode.sound.SoundSystemException
 *  paulscode.sound.SoundSystemLogger
 *  paulscode.sound.Source
 *  paulscode.sound.codecs.CodecJOrbis
 *  paulscode.sound.libraries.LibraryLWJGLOpenAL
 */
package net.minecraft.client.audio;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import io.netty.util.internal.ThreadLocalRandom;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.ISoundEventListener;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.SoundSystemLogger;
import paulscode.sound.Source;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;

public class SoundManager {
    private static final Marker LOG_MARKER = MarkerManager.getMarker("SOUNDS");
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Set<ResourceLocation> UNABLE_TO_PLAY = Sets.newHashSet();
    private final SoundHandler sndHandler;
    private final GameSettings options;
    private SoundSystemStarterThread sndSystem;
    private boolean loaded;
    private int playTime;
    private final Map<String, ISound> playingSounds = HashBiMap.create();
    private final Map<ISound, String> invPlayingSounds = ((BiMap)this.playingSounds).inverse();
    private final Multimap<SoundCategory, String> categorySounds = HashMultimap.create();
    private final List<ITickableSound> tickableSounds = Lists.newArrayList();
    private final Map<ISound, Integer> delayedSounds = Maps.newHashMap();
    private final Map<String, Integer> playingSoundsStopTime = Maps.newHashMap();
    private final List<ISoundEventListener> listeners = Lists.newArrayList();
    private final List<String> pausedChannels = Lists.newArrayList();

    public SoundManager(SoundHandler p_i45119_1_, GameSettings p_i45119_2_) {
        this.sndHandler = p_i45119_1_;
        this.options = p_i45119_2_;
        try {
            SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
            SoundSystemConfig.setCodec((String)"ogg", CodecJOrbis.class);
        }
        catch (SoundSystemException soundsystemexception) {
            LOGGER.error(LOG_MARKER, "Error linking with the LibraryJavaSound plug-in", (Throwable)soundsystemexception);
        }
    }

    public void reloadSoundSystem() {
        UNABLE_TO_PLAY.clear();
        for (SoundEvent soundevent : SoundEvent.REGISTRY) {
            ResourceLocation resourcelocation = soundevent.getSoundName();
            if (this.sndHandler.getAccessor(resourcelocation) != null) continue;
            LOGGER.warn("Missing sound for event: {}", SoundEvent.REGISTRY.getNameForObject(soundevent));
            UNABLE_TO_PLAY.add(resourcelocation);
        }
        this.unloadSoundSystem();
        this.loadSoundSystem();
    }

    private synchronized void loadSoundSystem() {
        if (!this.loaded) {
            try {
                new Thread(new Runnable(){

                    @Override
                    public void run() {
                        SoundSystemConfig.setLogger((SoundSystemLogger)new SoundSystemLogger(){

                            public void message(String p_message_1_, int p_message_2_) {
                                if (!p_message_1_.isEmpty()) {
                                    LOGGER.info(p_message_1_);
                                }
                            }

                            public void importantMessage(String p_importantMessage_1_, int p_importantMessage_2_) {
                                if (!p_importantMessage_1_.isEmpty()) {
                                    LOGGER.warn(p_importantMessage_1_);
                                }
                            }

                            public void errorMessage(String p_errorMessage_1_, String p_errorMessage_2_, int p_errorMessage_3_) {
                                if (!p_errorMessage_2_.isEmpty()) {
                                    LOGGER.error("Error in class '{}'", p_errorMessage_1_);
                                    LOGGER.error(p_errorMessage_2_);
                                }
                            }
                        });
                        SoundManager soundManager = SoundManager.this;
                        soundManager.getClass();
                        SoundManager.this.sndSystem = soundManager.new SoundSystemStarterThread();
                        SoundManager.this.loaded = true;
                        SoundManager.this.sndSystem.setMasterVolume(SoundManager.this.options.getSoundLevel(SoundCategory.MASTER));
                        LOGGER.info(LOG_MARKER, "Sound engine started");
                    }
                }, "Sound Library Loader").start();
            }
            catch (RuntimeException runtimeexception) {
                LOGGER.error(LOG_MARKER, "Error starting SoundSystem. Turning off sounds & music", (Throwable)runtimeexception);
                this.options.setSoundLevel(SoundCategory.MASTER, 0.0f);
                this.options.saveOptions();
            }
        }
    }

    private float getVolume(SoundCategory category) {
        return category != null && category != SoundCategory.MASTER ? this.options.getSoundLevel(category) : 1.0f;
    }

    public void setVolume(SoundCategory category, float volume) {
        if (this.loaded) {
            if (category == SoundCategory.MASTER) {
                this.sndSystem.setMasterVolume(volume);
            } else {
                for (String s : this.categorySounds.get(category)) {
                    ISound isound = this.playingSounds.get(s);
                    float f = this.getClampedVolume(isound);
                    if (f <= 0.0f) {
                        this.stopSound(isound);
                        continue;
                    }
                    this.sndSystem.setVolume(s, f);
                }
            }
        }
    }

    public void unloadSoundSystem() {
        if (this.loaded) {
            this.stopAllSounds();
            this.sndSystem.cleanup();
            this.loaded = false;
        }
    }

    public void stopAllSounds() {
        if (this.loaded) {
            for (String s : this.playingSounds.keySet()) {
                this.sndSystem.stop(s);
            }
            this.playingSounds.clear();
            this.delayedSounds.clear();
            this.tickableSounds.clear();
            this.categorySounds.clear();
            this.playingSoundsStopTime.clear();
        }
    }

    public void addListener(ISoundEventListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(ISoundEventListener listener) {
        this.listeners.remove(listener);
    }

    public void updateAllSounds() {
        ++this.playTime;
        for (ITickableSound itickablesound : this.tickableSounds) {
            itickablesound.update();
            if (itickablesound.isDonePlaying()) {
                this.stopSound(itickablesound);
                continue;
            }
            String s = this.invPlayingSounds.get(itickablesound);
            this.sndSystem.setVolume(s, this.getClampedVolume(itickablesound));
            this.sndSystem.setPitch(s, this.getClampedPitch(itickablesound));
            this.sndSystem.setPosition(s, itickablesound.getXPosF(), itickablesound.getYPosF(), itickablesound.getZPosF());
        }
        Iterator<Map.Entry<String, ISound>> iterator = this.playingSounds.entrySet().iterator();
        while (iterator.hasNext()) {
            int i;
            Map.Entry<String, ISound> entry = iterator.next();
            String s1 = entry.getKey();
            ISound isound = entry.getValue();
            if (this.sndSystem.playing(s1) || (i = this.playingSoundsStopTime.get(s1).intValue()) > this.playTime) continue;
            int j = isound.getRepeatDelay();
            if (isound.canRepeat() && j > 0) {
                this.delayedSounds.put(isound, this.playTime + j);
            }
            iterator.remove();
            LOGGER.debug(LOG_MARKER, "Removed channel {} because it's not playing anymore", s1);
            this.sndSystem.removeSource(s1);
            this.playingSoundsStopTime.remove(s1);
            try {
                this.categorySounds.remove((Object)isound.getCategory(), s1);
            }
            catch (RuntimeException runtimeException) {
                // empty catch block
            }
            if (!(isound instanceof ITickableSound)) continue;
            this.tickableSounds.remove(isound);
        }
        Iterator<Map.Entry<ISound, Integer>> iterator1 = this.delayedSounds.entrySet().iterator();
        while (iterator1.hasNext()) {
            Map.Entry<ISound, Integer> entry1 = iterator1.next();
            if (this.playTime < entry1.getValue()) continue;
            ISound isound1 = entry1.getKey();
            if (isound1 instanceof ITickableSound) {
                ((ITickableSound)isound1).update();
            }
            this.playSound(isound1);
            iterator1.remove();
        }
    }

    public boolean isSoundPlaying(ISound sound) {
        if (!this.loaded) {
            return false;
        }
        String s = this.invPlayingSounds.get(sound);
        if (s == null) {
            return false;
        }
        return this.sndSystem.playing(s) || this.playingSoundsStopTime.containsKey(s) && this.playingSoundsStopTime.get(s) <= this.playTime;
    }

    public void stopSound(ISound sound) {
        String s;
        if (this.loaded && (s = this.invPlayingSounds.get(sound)) != null) {
            this.sndSystem.stop(s);
        }
    }

    public void playSound(ISound p_sound) {
        if (this.loaded) {
            SoundEventAccessor soundeventaccessor = p_sound.createAccessor(this.sndHandler);
            ResourceLocation resourcelocation = p_sound.getSoundLocation();
            if (soundeventaccessor == null) {
                if (UNABLE_TO_PLAY.add(resourcelocation)) {
                    LOGGER.warn(LOG_MARKER, "Unable to play unknown soundEvent: {}", resourcelocation);
                }
            } else {
                if (!this.listeners.isEmpty()) {
                    for (ISoundEventListener isoundeventlistener : this.listeners) {
                        isoundeventlistener.soundPlay(p_sound, soundeventaccessor);
                    }
                }
                if (this.sndSystem.getMasterVolume() <= 0.0f) {
                    LOGGER.debug(LOG_MARKER, "Skipped playing soundEvent: {}, master volume was zero", resourcelocation);
                } else {
                    Sound sound = p_sound.getSound();
                    if (sound == SoundHandler.MISSING_SOUND) {
                        if (UNABLE_TO_PLAY.add(resourcelocation)) {
                            LOGGER.warn(LOG_MARKER, "Unable to play empty soundEvent: {}", resourcelocation);
                        }
                    } else {
                        float f3 = p_sound.getVolume();
                        float f = 16.0f;
                        if (f3 > 1.0f) {
                            f *= f3;
                        }
                        SoundCategory soundcategory = p_sound.getCategory();
                        float f1 = this.getClampedVolume(p_sound);
                        float f2 = this.getClampedPitch(p_sound);
                        if (f1 == 0.0f) {
                            LOGGER.debug(LOG_MARKER, "Skipped playing sound {}, volume was zero.", sound.getSoundLocation());
                        } else {
                            boolean flag = p_sound.canRepeat() && p_sound.getRepeatDelay() == 0;
                            String s = MathHelper.getRandomUUID((Random)ThreadLocalRandom.current()).toString();
                            ResourceLocation resourcelocation1 = sound.getSoundAsOggLocation();
                            if (sound.isStreaming()) {
                                this.sndSystem.newStreamingSource(false, s, SoundManager.getURLForSoundResource(resourcelocation1), resourcelocation1.toString(), flag, p_sound.getXPosF(), p_sound.getYPosF(), p_sound.getZPosF(), p_sound.getAttenuationType().getTypeInt(), f);
                            } else {
                                this.sndSystem.newSource(false, s, SoundManager.getURLForSoundResource(resourcelocation1), resourcelocation1.toString(), flag, p_sound.getXPosF(), p_sound.getYPosF(), p_sound.getZPosF(), p_sound.getAttenuationType().getTypeInt(), f);
                            }
                            LOGGER.debug(LOG_MARKER, "Playing sound {} for event {} as channel {}", sound.getSoundLocation(), resourcelocation, s);
                            this.sndSystem.setPitch(s, f2);
                            this.sndSystem.setVolume(s, f1);
                            this.sndSystem.play(s);
                            this.playingSoundsStopTime.put(s, this.playTime + 20);
                            this.playingSounds.put(s, p_sound);
                            this.categorySounds.put(soundcategory, s);
                            if (p_sound instanceof ITickableSound) {
                                this.tickableSounds.add((ITickableSound)p_sound);
                            }
                        }
                    }
                }
            }
        }
    }

    private float getClampedPitch(ISound soundIn) {
        return MathHelper.clamp(soundIn.getPitch(), 0.5f, 2.0f);
    }

    private float getClampedVolume(ISound soundIn) {
        return MathHelper.clamp(soundIn.getVolume() * this.getVolume(soundIn.getCategory()), 0.0f, 1.0f);
    }

    public void pauseAllSounds() {
        for (Map.Entry<String, ISound> entry : this.playingSounds.entrySet()) {
            String s = entry.getKey();
            boolean flag = this.isSoundPlaying(entry.getValue());
            if (!flag) continue;
            LOGGER.debug(LOG_MARKER, "Pausing channel {}", s);
            this.sndSystem.pause(s);
            this.pausedChannels.add(s);
        }
    }

    public void resumeAllSounds() {
        for (String s : this.pausedChannels) {
            LOGGER.debug(LOG_MARKER, "Resuming channel {}", s);
            this.sndSystem.play(s);
        }
        this.pausedChannels.clear();
    }

    public void playDelayedSound(ISound sound, int delay) {
        this.delayedSounds.put(sound, this.playTime + delay);
    }

    private static URL getURLForSoundResource(final ResourceLocation p_148612_0_) {
        String s = String.format("%s:%s:%s", "mcsounddomain", p_148612_0_.getNamespace(), p_148612_0_.getPath());
        URLStreamHandler urlstreamhandler = new URLStreamHandler(){

            @Override
            protected URLConnection openConnection(URL p_openConnection_1_) {
                return new URLConnection(p_openConnection_1_){

                    @Override
                    public void connect() throws IOException {
                    }

                    @Override
                    public InputStream getInputStream() throws IOException {
                        return Minecraft.getMinecraft().getResourceManager().getResource(p_148612_0_).getInputStream();
                    }
                };
            }
        };
        try {
            return new URL((URL)null, s, urlstreamhandler);
        }
        catch (MalformedURLException var4) {
            throw new Error("TODO: Sanely handle url exception! :D");
        }
    }

    public void setListener(EntityPlayer player, float p_148615_2_) {
        if (this.loaded && player != null) {
            float f = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * p_148615_2_;
            float f1 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * p_148615_2_;
            double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double)p_148615_2_;
            double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double)p_148615_2_ + (double)player.getEyeHeight();
            double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)p_148615_2_;
            float f2 = MathHelper.cos((f1 + 90.0f) * ((float)Math.PI / 180));
            float f3 = MathHelper.sin((f1 + 90.0f) * ((float)Math.PI / 180));
            float f4 = MathHelper.cos(-f * ((float)Math.PI / 180));
            float f5 = MathHelper.sin(-f * ((float)Math.PI / 180));
            float f6 = MathHelper.cos((-f + 90.0f) * ((float)Math.PI / 180));
            float f7 = MathHelper.sin((-f + 90.0f) * ((float)Math.PI / 180));
            float f8 = f2 * f4;
            float f9 = f3 * f4;
            float f10 = f2 * f6;
            float f11 = f3 * f6;
            this.sndSystem.setListenerPosition((float)d0, (float)d1, (float)d2);
            this.sndSystem.setListenerOrientation(f8, f5, f9, f10, f7, f11);
        }
    }

    public void stop(String p_189567_1_, SoundCategory p_189567_2_) {
        if (p_189567_2_ != null) {
            for (String s : this.categorySounds.get(p_189567_2_)) {
                ISound isound = this.playingSounds.get(s);
                if (p_189567_1_.isEmpty()) {
                    this.stopSound(isound);
                    continue;
                }
                if (!isound.getSoundLocation().equals(new ResourceLocation(p_189567_1_))) continue;
                this.stopSound(isound);
            }
        } else if (p_189567_1_.isEmpty()) {
            this.stopAllSounds();
        } else {
            for (ISound isound1 : this.playingSounds.values()) {
                if (!isound1.getSoundLocation().equals(new ResourceLocation(p_189567_1_))) continue;
                this.stopSound(isound1);
            }
        }
    }

    class SoundSystemStarterThread
    extends SoundSystem {
        private SoundSystemStarterThread() {
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public boolean playing(String p_playing_1_) {
            Object object = SoundSystemConfig.THREAD_SYNC;
            synchronized (object) {
                if (this.soundLibrary == null) {
                    return false;
                }
                Source source = (Source)this.soundLibrary.getSources().get(p_playing_1_);
                if (source == null) {
                    return false;
                }
                return source.playing() || source.paused() || source.preLoad;
            }
        }
    }
}

