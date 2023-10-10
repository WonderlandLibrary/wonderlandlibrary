/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.audio;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundEventAccessorComposite;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundPoolEntry;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
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
    private static final Logger logger = LogManager.getLogger();
    private final SoundHandler sndHandler;
    private final GameSettings options;
    private SoundSystemStarterThread sndSystem;
    private boolean loaded;
    private int playTime = 0;
    private final Map<String, ISound> playingSounds = HashBiMap.create();
    private final Map<ISound, String> invPlayingSounds = ((BiMap)this.playingSounds).inverse();
    private Map<ISound, SoundPoolEntry> playingSoundPoolEntries = Maps.newHashMap();
    private final Multimap<SoundCategory, String> categorySounds = HashMultimap.create();
    private final List<ITickableSound> tickableSounds = Lists.newArrayList();
    private final Map<ISound, Integer> delayedSounds = Maps.newHashMap();
    private final Map<String, Integer> playingSoundsStopTime = Maps.newHashMap();

    public SoundManager(SoundHandler p_i45119_1_, GameSettings p_i45119_2_) {
        this.sndHandler = p_i45119_1_;
        this.options = p_i45119_2_;
        try {
            SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
            SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
        }
        catch (SoundSystemException soundsystemexception) {
            logger.error(LOG_MARKER, "Error linking with the LibraryJavaSound plug-in", (Throwable)soundsystemexception);
        }
    }

    public void reloadSoundSystem() {
        this.unloadSoundSystem();
        this.loadSoundSystem();
    }

    private synchronized void loadSoundSystem() {
        if (!this.loaded) {
            try {
                new Thread(new Runnable(){

                    @Override
                    public void run() {
                        SoundSystemConfig.setLogger(new SoundSystemLogger(){

                            @Override
                            public void message(String p_message_1_, int p_message_2_) {
                                if (!p_message_1_.isEmpty()) {
                                    logger.info(p_message_1_);
                                }
                            }

                            @Override
                            public void importantMessage(String p_importantMessage_1_, int p_importantMessage_2_) {
                                if (!p_importantMessage_1_.isEmpty()) {
                                    logger.warn(p_importantMessage_1_);
                                }
                            }

                            @Override
                            public void errorMessage(String p_errorMessage_1_, String p_errorMessage_2_, int p_errorMessage_3_) {
                                if (!p_errorMessage_2_.isEmpty()) {
                                    logger.error("Error in class '" + p_errorMessage_1_ + "'");
                                    logger.error(p_errorMessage_2_);
                                }
                            }
                        });
                        SoundManager soundManager = SoundManager.this;
                        soundManager.getClass();
                        SoundManager.this.sndSystem = soundManager.new SoundSystemStarterThread();
                        SoundManager.this.loaded = true;
                        SoundManager.this.sndSystem.setMasterVolume(SoundManager.this.options.getSoundLevel(SoundCategory.MASTER));
                        logger.info(LOG_MARKER, "Sound engine started");
                    }
                }, "Sound Library Loader").start();
            }
            catch (RuntimeException runtimeexception) {
                logger.error(LOG_MARKER, "Error starting SoundSystem. Turning off sounds & sound", (Throwable)runtimeexception);
                this.options.setSoundLevel(SoundCategory.MASTER, 0.0f);
                this.options.saveOptions();
            }
        }
    }

    private float getSoundCategoryVolume(SoundCategory category) {
        return category != null && category != SoundCategory.MASTER ? this.options.getSoundLevel(category) : 1.0f;
    }

    public void setSoundCategoryVolume(SoundCategory category, float volume) {
        if (this.loaded) {
            if (category == SoundCategory.MASTER) {
                this.sndSystem.setMasterVolume(volume);
            } else {
                for (String s2 : this.categorySounds.get(category)) {
                    ISound isound = this.playingSounds.get(s2);
                    float f2 = this.getNormalizedVolume(isound, this.playingSoundPoolEntries.get(isound), category);
                    if (f2 <= 0.0f) {
                        this.stopSound(isound);
                        continue;
                    }
                    this.sndSystem.setVolume(s2, f2);
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
            for (String s2 : this.playingSounds.keySet()) {
                this.sndSystem.stop(s2);
            }
            this.playingSounds.clear();
            this.delayedSounds.clear();
            this.tickableSounds.clear();
            this.categorySounds.clear();
            this.playingSoundPoolEntries.clear();
            this.playingSoundsStopTime.clear();
        }
    }

    public void updateAllSounds() {
        ++this.playTime;
        for (ITickableSound itickablesound : this.tickableSounds) {
            itickablesound.update();
            if (itickablesound.isDonePlaying()) {
                this.stopSound(itickablesound);
                continue;
            }
            String s2 = this.invPlayingSounds.get(itickablesound);
            this.sndSystem.setVolume(s2, this.getNormalizedVolume(itickablesound, this.playingSoundPoolEntries.get(itickablesound), this.sndHandler.getSound(itickablesound.getSoundLocation()).getSoundCategory()));
            this.sndSystem.setPitch(s2, this.getNormalizedPitch(itickablesound, this.playingSoundPoolEntries.get(itickablesound)));
            this.sndSystem.setPosition(s2, itickablesound.getXPosF(), itickablesound.getYPosF(), itickablesound.getZPosF());
        }
        Iterator<Map.Entry<String, ISound>> iterator = this.playingSounds.entrySet().iterator();
        while (iterator.hasNext()) {
            int i2;
            Map.Entry<String, ISound> entry = iterator.next();
            String s1 = entry.getKey();
            ISound isound = entry.getValue();
            if (this.sndSystem.playing(s1) || (i2 = this.playingSoundsStopTime.get(s1).intValue()) > this.playTime) continue;
            int j2 = isound.getRepeatDelay();
            if (isound.canRepeat() && j2 > 0) {
                this.delayedSounds.put(isound, this.playTime + j2);
            }
            iterator.remove();
            logger.debug(LOG_MARKER, "Removed channel {} because it's not playing anymore", s1);
            this.sndSystem.removeSource(s1);
            this.playingSoundsStopTime.remove(s1);
            this.playingSoundPoolEntries.remove(isound);
            try {
                this.categorySounds.remove((Object)this.sndHandler.getSound(isound.getSoundLocation()).getSoundCategory(), s1);
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
        String s2 = this.invPlayingSounds.get(sound);
        return s2 == null ? false : this.sndSystem.playing(s2) || this.playingSoundsStopTime.containsKey(s2) && this.playingSoundsStopTime.get(s2) <= this.playTime;
    }

    public void stopSound(ISound sound) {
        String s2;
        if (this.loaded && (s2 = this.invPlayingSounds.get(sound)) != null) {
            this.sndSystem.stop(s2);
        }
    }

    public void playSound(ISound sound) {
        if (this.loaded) {
            if (this.sndSystem.getMasterVolume() <= 0.0f) {
                logger.debug(LOG_MARKER, "Skipped playing soundEvent: {}, master volume was zero", sound.getSoundLocation());
            } else {
                SoundEventAccessorComposite soundeventaccessorcomposite = this.sndHandler.getSound(sound.getSoundLocation());
                if (soundeventaccessorcomposite == null) {
                    logger.warn(LOG_MARKER, "Unable to play unknown soundEvent: {}", sound.getSoundLocation());
                } else {
                    SoundPoolEntry soundpoolentry = soundeventaccessorcomposite.cloneEntry();
                    if (soundpoolentry == SoundHandler.missing_sound) {
                        logger.warn(LOG_MARKER, "Unable to play empty soundEvent: {}", soundeventaccessorcomposite.getSoundEventLocation());
                    } else {
                        float f2 = sound.getVolume();
                        float f1 = 16.0f;
                        if (f2 > 1.0f) {
                            f1 *= f2;
                        }
                        SoundCategory soundcategory = soundeventaccessorcomposite.getSoundCategory();
                        float f22 = this.getNormalizedVolume(sound, soundpoolentry, soundcategory);
                        double d0 = this.getNormalizedPitch(sound, soundpoolentry);
                        ResourceLocation resourcelocation = soundpoolentry.getSoundPoolEntryLocation();
                        if (f22 == 0.0f) {
                            logger.debug(LOG_MARKER, "Skipped playing sound {}, volume was zero.", resourcelocation);
                        } else {
                            boolean flag = sound.canRepeat() && sound.getRepeatDelay() == 0;
                            String s2 = MathHelper.getRandomUuid(ThreadLocalRandom.current()).toString();
                            if (soundpoolentry.isStreamingSound()) {
                                this.sndSystem.newStreamingSource(false, s2, SoundManager.getURLForSoundResource(resourcelocation), resourcelocation.toString(), flag, sound.getXPosF(), sound.getYPosF(), sound.getZPosF(), sound.getAttenuationType().getTypeInt(), f1);
                            } else {
                                this.sndSystem.newSource(false, s2, SoundManager.getURLForSoundResource(resourcelocation), resourcelocation.toString(), flag, sound.getXPosF(), sound.getYPosF(), sound.getZPosF(), sound.getAttenuationType().getTypeInt(), f1);
                            }
                            logger.debug(LOG_MARKER, "Playing sound {} for event {} as channel {}", soundpoolentry.getSoundPoolEntryLocation(), soundeventaccessorcomposite.getSoundEventLocation(), s2);
                            this.sndSystem.setPitch(s2, (float)d0);
                            this.sndSystem.setVolume(s2, f22);
                            this.sndSystem.play(s2);
                            this.playingSoundsStopTime.put(s2, this.playTime + 20);
                            this.playingSounds.put(s2, sound);
                            this.playingSoundPoolEntries.put(sound, soundpoolentry);
                            if (soundcategory != SoundCategory.MASTER) {
                                this.categorySounds.put(soundcategory, s2);
                            }
                            if (sound instanceof ITickableSound) {
                                this.tickableSounds.add((ITickableSound)sound);
                            }
                        }
                    }
                }
            }
        }
    }

    private float getNormalizedPitch(ISound sound, SoundPoolEntry entry) {
        return (float)MathHelper.clamp_double((double)sound.getPitch() * entry.getPitch(), 0.5, 2.0);
    }

    private float getNormalizedVolume(ISound sound, SoundPoolEntry entry, SoundCategory category) {
        return (float)MathHelper.clamp_double((double)sound.getVolume() * entry.getVolume(), 0.0, 1.0) * this.getSoundCategoryVolume(category);
    }

    public void pauseAllSounds() {
        for (String s2 : this.playingSounds.keySet()) {
            logger.debug(LOG_MARKER, "Pausing channel {}", s2);
            this.sndSystem.pause(s2);
        }
    }

    public void resumeAllSounds() {
        for (String s2 : this.playingSounds.keySet()) {
            logger.debug(LOG_MARKER, "Resuming channel {}", s2);
            this.sndSystem.play(s2);
        }
    }

    public void playDelayedSound(ISound sound, int delay) {
        this.delayedSounds.put(sound, this.playTime + delay);
    }

    private static URL getURLForSoundResource(final ResourceLocation p_148612_0_) {
        String s2 = String.format("%s:%s:%s", "mcsounddomain", p_148612_0_.getResourceDomain(), p_148612_0_.getResourcePath());
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
            return new URL((URL)null, s2, urlstreamhandler);
        }
        catch (MalformedURLException var4) {
            throw new Error("TODO: Sanely handle url exception! :D");
        }
    }

    public void setListener(EntityPlayer player, float p_148615_2_) {
        if (this.loaded && player != null) {
            float f2 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * p_148615_2_;
            float f1 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * p_148615_2_;
            double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double)p_148615_2_;
            double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double)p_148615_2_ + (double)player.getEyeHeight();
            double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)p_148615_2_;
            float f22 = MathHelper.cos((f1 + 90.0f) * ((float)Math.PI / 180));
            float f3 = MathHelper.sin((f1 + 90.0f) * ((float)Math.PI / 180));
            float f4 = MathHelper.cos(-f2 * ((float)Math.PI / 180));
            float f5 = MathHelper.sin(-f2 * ((float)Math.PI / 180));
            float f6 = MathHelper.cos((-f2 + 90.0f) * ((float)Math.PI / 180));
            float f7 = MathHelper.sin((-f2 + 90.0f) * ((float)Math.PI / 180));
            float f8 = f22 * f4;
            float f9 = f3 * f4;
            float f10 = f22 * f6;
            float f11 = f3 * f6;
            this.sndSystem.setListenerPosition((float)d0, (float)d1, (float)d2);
            this.sndSystem.setListenerOrientation(f8, f5, f9, f10, f7, f11);
        }
    }

    class SoundSystemStarterThread
    extends SoundSystem {
        private SoundSystemStarterThread() {
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean playing(String p_playing_1_) {
            Object object = SoundSystemConfig.THREAD_SYNC;
            synchronized (object) {
                if (this.soundLibrary == null) {
                    return false;
                }
                Source source = this.soundLibrary.getSources().get(p_playing_1_);
                boolean bl = source == null ? false : source.playing() || source.paused() || source.preLoad;
                return bl;
            }
        }
    }
}

