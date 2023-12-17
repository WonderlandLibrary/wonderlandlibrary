// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.audio;

import paulscode.sound.Source;
import paulscode.sound.SoundSystem;
import com.google.common.collect.Sets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.MarkerManager;
import net.minecraft.entity.player.EntityPlayer;
import java.net.MalformedURLException;
import net.minecraft.client.Minecraft;
import java.io.InputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URL;
import java.util.Random;
import net.minecraft.util.math.MathHelper;
import io.netty.util.internal.ThreadLocalRandom;
import paulscode.sound.SoundSystemLogger;
import java.util.Iterator;
import net.minecraft.util.SoundEvent;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.List;
import net.minecraft.util.SoundCategory;
import com.google.common.collect.Multimap;
import java.util.Map;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import java.util.Set;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;

public class SoundManager
{
    private static final Marker LOG_MARKER;
    private static final Logger LOGGER;
    private static final Set<ResourceLocation> UNABLE_TO_PLAY;
    private final SoundHandler sndHandler;
    private final GameSettings options;
    private SoundSystemStarterThread sndSystem;
    private boolean loaded;
    private int playTime;
    private final Map<String, ISound> playingSounds;
    private final Map<ISound, String> invPlayingSounds;
    private final Multimap<SoundCategory, String> categorySounds;
    private final List<ITickableSound> tickableSounds;
    private final Map<ISound, Integer> delayedSounds;
    private final Map<String, Integer> playingSoundsStopTime;
    private final List<ISoundEventListener> listeners;
    private final List<String> pausedChannels;
    
    public SoundManager(final SoundHandler p_i45119_1_, final GameSettings p_i45119_2_) {
        this.playingSounds = (Map<String, ISound>)HashBiMap.create();
        this.invPlayingSounds = (Map<ISound, String>)((BiMap)this.playingSounds).inverse();
        this.categorySounds = (Multimap<SoundCategory, String>)HashMultimap.create();
        this.tickableSounds = (List<ITickableSound>)Lists.newArrayList();
        this.delayedSounds = (Map<ISound, Integer>)Maps.newHashMap();
        this.playingSoundsStopTime = (Map<String, Integer>)Maps.newHashMap();
        this.listeners = (List<ISoundEventListener>)Lists.newArrayList();
        this.pausedChannels = (List<String>)Lists.newArrayList();
        this.sndHandler = p_i45119_1_;
        this.options = p_i45119_2_;
        try {
            SoundSystemConfig.addLibrary((Class)LibraryLWJGLOpenAL.class);
            SoundSystemConfig.setCodec("ogg", (Class)CodecJOrbis.class);
        }
        catch (SoundSystemException soundsystemexception) {
            SoundManager.LOGGER.error(SoundManager.LOG_MARKER, "Error linking with the LibraryJavaSound plug-in", (Throwable)soundsystemexception);
        }
    }
    
    public void reloadSoundSystem() {
        SoundManager.UNABLE_TO_PLAY.clear();
        for (final SoundEvent soundevent : SoundEvent.REGISTRY) {
            final ResourceLocation resourcelocation = soundevent.getSoundName();
            if (this.sndHandler.getAccessor(resourcelocation) == null) {
                SoundManager.LOGGER.warn("Missing sound for event: {}", (Object)SoundEvent.REGISTRY.getNameForObject(soundevent));
                SoundManager.UNABLE_TO_PLAY.add(resourcelocation);
            }
        }
        this.unloadSoundSystem();
        this.loadSoundSystem();
    }
    
    private synchronized void loadSoundSystem() {
        if (!this.loaded) {
            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SoundSystemConfig.setLogger((SoundSystemLogger)new SoundSystemLogger() {
                            public void message(final String p_message_1_, final int p_message_2_) {
                                if (!p_message_1_.isEmpty()) {
                                    SoundManager.LOGGER.info(p_message_1_);
                                }
                            }
                            
                            public void importantMessage(final String p_importantMessage_1_, final int p_importantMessage_2_) {
                                if (!p_importantMessage_1_.isEmpty()) {
                                    SoundManager.LOGGER.warn(p_importantMessage_1_);
                                }
                            }
                            
                            public void errorMessage(final String p_errorMessage_1_, final String p_errorMessage_2_, final int p_errorMessage_3_) {
                                if (!p_errorMessage_2_.isEmpty()) {
                                    SoundManager.LOGGER.error("Error in class '{}'", (Object)p_errorMessage_1_);
                                    SoundManager.LOGGER.error(p_errorMessage_2_);
                                }
                            }
                        });
                        SoundManager.this.sndSystem = new SoundSystemStarterThread();
                        SoundManager.this.loaded = true;
                        SoundManager.this.sndSystem.setMasterVolume(SoundManager.this.options.getSoundLevel(SoundCategory.MASTER));
                        SoundManager.LOGGER.info(SoundManager.LOG_MARKER, "Sound engine started");
                    }
                }, "Sound Library Loader").start();
            }
            catch (RuntimeException runtimeexception) {
                SoundManager.LOGGER.error(SoundManager.LOG_MARKER, "Error starting SoundSystem. Turning off sounds & music", (Throwable)runtimeexception);
                this.options.setSoundLevel(SoundCategory.MASTER, 0.0f);
                this.options.saveOptions();
            }
        }
    }
    
    private float getVolume(final SoundCategory category) {
        return (category != null && category != SoundCategory.MASTER) ? this.options.getSoundLevel(category) : 1.0f;
    }
    
    public void setVolume(final SoundCategory category, final float volume) {
        if (this.loaded) {
            if (category == SoundCategory.MASTER) {
                this.sndSystem.setMasterVolume(volume);
            }
            else {
                for (final String s : this.categorySounds.get((Object)category)) {
                    final ISound isound = this.playingSounds.get(s);
                    final float f = this.getClampedVolume(isound);
                    if (f <= 0.0f) {
                        this.stopSound(isound);
                    }
                    else {
                        this.sndSystem.setVolume(s, f);
                    }
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
            for (final String s : this.playingSounds.keySet()) {
                this.sndSystem.stop(s);
            }
            this.playingSounds.clear();
            this.delayedSounds.clear();
            this.tickableSounds.clear();
            this.categorySounds.clear();
            this.playingSoundsStopTime.clear();
        }
    }
    
    public void addListener(final ISoundEventListener listener) {
        this.listeners.add(listener);
    }
    
    public void removeListener(final ISoundEventListener listener) {
        this.listeners.remove(listener);
    }
    
    public void updateAllSounds() {
        ++this.playTime;
        for (final ITickableSound itickablesound : this.tickableSounds) {
            itickablesound.update();
            if (itickablesound.isDonePlaying()) {
                this.stopSound(itickablesound);
            }
            else {
                final String s = this.invPlayingSounds.get(itickablesound);
                this.sndSystem.setVolume(s, this.getClampedVolume(itickablesound));
                this.sndSystem.setPitch(s, this.getClampedPitch(itickablesound));
                this.sndSystem.setPosition(s, itickablesound.getXPosF(), itickablesound.getYPosF(), itickablesound.getZPosF());
            }
        }
        final Iterator<Map.Entry<String, ISound>> iterator = this.playingSounds.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<String, ISound> entry = iterator.next();
            final String s2 = entry.getKey();
            final ISound isound = entry.getValue();
            if (!this.sndSystem.playing(s2)) {
                final int i = this.playingSoundsStopTime.get(s2);
                if (i > this.playTime) {
                    continue;
                }
                final int j = isound.getRepeatDelay();
                if (isound.canRepeat() && j > 0) {
                    this.delayedSounds.put(isound, this.playTime + j);
                }
                iterator.remove();
                SoundManager.LOGGER.debug(SoundManager.LOG_MARKER, "Removed channel {} because it's not playing anymore", (Object)s2);
                this.sndSystem.removeSource(s2);
                this.playingSoundsStopTime.remove(s2);
                try {
                    this.categorySounds.remove((Object)isound.getCategory(), (Object)s2);
                }
                catch (RuntimeException ex) {}
                if (!(isound instanceof ITickableSound)) {
                    continue;
                }
                this.tickableSounds.remove(isound);
            }
        }
        final Iterator<Map.Entry<ISound, Integer>> iterator2 = this.delayedSounds.entrySet().iterator();
        while (iterator2.hasNext()) {
            final Map.Entry<ISound, Integer> entry2 = iterator2.next();
            if (this.playTime >= entry2.getValue()) {
                final ISound isound2 = entry2.getKey();
                if (isound2 instanceof ITickableSound) {
                    ((ITickableSound)isound2).update();
                }
                this.playSound(isound2);
                iterator2.remove();
            }
        }
    }
    
    public boolean isSoundPlaying(final ISound sound) {
        if (!this.loaded) {
            return false;
        }
        final String s = this.invPlayingSounds.get(sound);
        return s != null && (this.sndSystem.playing(s) || (this.playingSoundsStopTime.containsKey(s) && this.playingSoundsStopTime.get(s) <= this.playTime));
    }
    
    public void stopSound(final ISound sound) {
        if (this.loaded) {
            final String s = this.invPlayingSounds.get(sound);
            if (s != null) {
                this.sndSystem.stop(s);
            }
        }
    }
    
    public void playSound(final ISound p_sound) {
        if (this.loaded) {
            final SoundEventAccessor soundeventaccessor = p_sound.createAccessor(this.sndHandler);
            final ResourceLocation resourcelocation = p_sound.getSoundLocation();
            if (soundeventaccessor == null) {
                if (SoundManager.UNABLE_TO_PLAY.add(resourcelocation)) {
                    SoundManager.LOGGER.warn(SoundManager.LOG_MARKER, "Unable to play unknown soundEvent: {}", (Object)resourcelocation);
                }
            }
            else {
                if (!this.listeners.isEmpty()) {
                    for (final ISoundEventListener isoundeventlistener : this.listeners) {
                        isoundeventlistener.soundPlay(p_sound, soundeventaccessor);
                    }
                }
                if (this.sndSystem.getMasterVolume() <= 0.0f) {
                    SoundManager.LOGGER.debug(SoundManager.LOG_MARKER, "Skipped playing soundEvent: {}, master volume was zero", (Object)resourcelocation);
                }
                else {
                    final Sound sound = p_sound.getSound();
                    if (sound == SoundHandler.MISSING_SOUND) {
                        if (SoundManager.UNABLE_TO_PLAY.add(resourcelocation)) {
                            SoundManager.LOGGER.warn(SoundManager.LOG_MARKER, "Unable to play empty soundEvent: {}", (Object)resourcelocation);
                        }
                    }
                    else {
                        final float f3 = p_sound.getVolume();
                        float f4 = 16.0f;
                        if (f3 > 1.0f) {
                            f4 *= f3;
                        }
                        final SoundCategory soundcategory = p_sound.getCategory();
                        final float f5 = this.getClampedVolume(p_sound);
                        final float f6 = this.getClampedPitch(p_sound);
                        if (f5 == 0.0f) {
                            SoundManager.LOGGER.debug(SoundManager.LOG_MARKER, "Skipped playing sound {}, volume was zero.", (Object)sound.getSoundLocation());
                        }
                        else {
                            final boolean flag = p_sound.canRepeat() && p_sound.getRepeatDelay() == 0;
                            final String s = MathHelper.getRandomUUID((Random)ThreadLocalRandom.current()).toString();
                            final ResourceLocation resourcelocation2 = sound.getSoundAsOggLocation();
                            if (sound.isStreaming()) {
                                this.sndSystem.newStreamingSource(false, s, getURLForSoundResource(resourcelocation2), resourcelocation2.toString(), flag, p_sound.getXPosF(), p_sound.getYPosF(), p_sound.getZPosF(), p_sound.getAttenuationType().getTypeInt(), f4);
                            }
                            else {
                                this.sndSystem.newSource(false, s, getURLForSoundResource(resourcelocation2), resourcelocation2.toString(), flag, p_sound.getXPosF(), p_sound.getYPosF(), p_sound.getZPosF(), p_sound.getAttenuationType().getTypeInt(), f4);
                            }
                            SoundManager.LOGGER.debug(SoundManager.LOG_MARKER, "Playing sound {} for event {} as channel {}", (Object)sound.getSoundLocation(), (Object)resourcelocation, (Object)s);
                            this.sndSystem.setPitch(s, f6);
                            this.sndSystem.setVolume(s, f5);
                            this.sndSystem.play(s);
                            this.playingSoundsStopTime.put(s, this.playTime + 20);
                            this.playingSounds.put(s, p_sound);
                            this.categorySounds.put((Object)soundcategory, (Object)s);
                            if (p_sound instanceof ITickableSound) {
                                this.tickableSounds.add((ITickableSound)p_sound);
                            }
                        }
                    }
                }
            }
        }
    }
    
    private float getClampedPitch(final ISound soundIn) {
        return MathHelper.clamp(soundIn.getPitch(), 0.5f, 2.0f);
    }
    
    private float getClampedVolume(final ISound soundIn) {
        return MathHelper.clamp(soundIn.getVolume() * this.getVolume(soundIn.getCategory()), 0.0f, 1.0f);
    }
    
    public void pauseAllSounds() {
        for (final Map.Entry<String, ISound> entry : this.playingSounds.entrySet()) {
            final String s = entry.getKey();
            final boolean flag = this.isSoundPlaying(entry.getValue());
            if (flag) {
                SoundManager.LOGGER.debug(SoundManager.LOG_MARKER, "Pausing channel {}", (Object)s);
                this.sndSystem.pause(s);
                this.pausedChannels.add(s);
            }
        }
    }
    
    public void resumeAllSounds() {
        for (final String s : this.pausedChannels) {
            SoundManager.LOGGER.debug(SoundManager.LOG_MARKER, "Resuming channel {}", (Object)s);
            this.sndSystem.play(s);
        }
        this.pausedChannels.clear();
    }
    
    public void playDelayedSound(final ISound sound, final int delay) {
        this.delayedSounds.put(sound, this.playTime + delay);
    }
    
    private static URL getURLForSoundResource(final ResourceLocation p_148612_0_) {
        final String s = String.format("%s:%s:%s", "mcsounddomain", p_148612_0_.getNamespace(), p_148612_0_.getPath());
        final URLStreamHandler urlstreamhandler = new URLStreamHandler() {
            @Override
            protected URLConnection openConnection(final URL p_openConnection_1_) {
                return new URLConnection(p_openConnection_1_) {
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
            return new URL(null, s, urlstreamhandler);
        }
        catch (MalformedURLException var4) {
            throw new Error("TODO: Sanely handle url exception! :D");
        }
    }
    
    public void setListener(final EntityPlayer player, final float p_148615_2_) {
        if (this.loaded && player != null) {
            final float f = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * p_148615_2_;
            final float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * p_148615_2_;
            final double d0 = player.prevPosX + (player.posX - player.prevPosX) * p_148615_2_;
            final double d2 = player.prevPosY + (player.posY - player.prevPosY) * p_148615_2_ + player.getEyeHeight();
            final double d3 = player.prevPosZ + (player.posZ - player.prevPosZ) * p_148615_2_;
            final float f3 = MathHelper.cos((f2 + 90.0f) * 0.017453292f);
            final float f4 = MathHelper.sin((f2 + 90.0f) * 0.017453292f);
            final float f5 = MathHelper.cos(-f * 0.017453292f);
            final float f6 = MathHelper.sin(-f * 0.017453292f);
            final float f7 = MathHelper.cos((-f + 90.0f) * 0.017453292f);
            final float f8 = MathHelper.sin((-f + 90.0f) * 0.017453292f);
            final float f9 = f3 * f5;
            final float f10 = f4 * f5;
            final float f11 = f3 * f7;
            final float f12 = f4 * f7;
            this.sndSystem.setListenerPosition((float)d0, (float)d2, (float)d3);
            this.sndSystem.setListenerOrientation(f9, f6, f10, f11, f8, f12);
        }
    }
    
    public void stop(final String p_189567_1_, final SoundCategory p_189567_2_) {
        if (p_189567_2_ != null) {
            for (final String s : this.categorySounds.get((Object)p_189567_2_)) {
                final ISound isound = this.playingSounds.get(s);
                if (p_189567_1_.isEmpty()) {
                    this.stopSound(isound);
                }
                else {
                    if (!isound.getSoundLocation().equals(new ResourceLocation(p_189567_1_))) {
                        continue;
                    }
                    this.stopSound(isound);
                }
            }
        }
        else if (p_189567_1_.isEmpty()) {
            this.stopAllSounds();
        }
        else {
            for (final ISound isound2 : this.playingSounds.values()) {
                if (isound2.getSoundLocation().equals(new ResourceLocation(p_189567_1_))) {
                    this.stopSound(isound2);
                }
            }
        }
    }
    
    static {
        LOG_MARKER = MarkerManager.getMarker("SOUNDS");
        LOGGER = LogManager.getLogger();
        UNABLE_TO_PLAY = Sets.newHashSet();
    }
    
    class SoundSystemStarterThread extends SoundSystem
    {
        private SoundSystemStarterThread() {
        }
        
        public boolean playing(final String p_playing_1_) {
            synchronized (SoundSystemConfig.THREAD_SYNC) {
                if (this.soundLibrary == null) {
                    return false;
                }
                final Source source = this.soundLibrary.getSources().get(p_playing_1_);
                return source != null && (source.playing() || source.paused() || source.preLoad);
            }
        }
    }
}
