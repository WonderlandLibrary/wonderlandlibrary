// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.audio;

import paulscode.sound.Source;
import paulscode.sound.SoundSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.MarkerManager;
import net.minecraft.entity.player.EntityPlayer;
import java.net.MalformedURLException;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import java.io.InputStream;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URL;
import net.minecraft.util.ResourceLocation;
import java.util.Random;
import net.minecraft.util.MathHelper;
import io.netty.util.internal.ThreadLocalRandom;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import paulscode.sound.SoundSystemLogger;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;
import com.google.common.collect.Lists;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.List;
import com.google.common.collect.Multimap;
import java.util.Map;
import net.minecraft.client.settings.GameSettings;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;

public class SoundManager
{
    private static final Marker LOG_MARKER;
    private static final Logger logger;
    private final SoundHandler sndHandler;
    private final GameSettings options;
    private SoundSystemStarterThread sndSystem;
    private boolean loaded;
    private int playTime;
    private final Map playingSounds;
    private final Map invPlayingSounds;
    private Map playingSoundPoolEntries;
    private final Multimap categorySounds;
    private final List tickableSounds;
    private final Map delayedSounds;
    private final Map playingSoundsStopTime;
    private static final String __OBFID = "CL_00001141";
    
    public SoundManager(final SoundHandler p_i45119_1_, final GameSettings p_i45119_2_) {
        this.playTime = 0;
        this.playingSounds = (Map)HashBiMap.create();
        this.invPlayingSounds = (Map)((BiMap)this.playingSounds).inverse();
        this.playingSoundPoolEntries = Maps.newHashMap();
        this.categorySounds = (Multimap)HashMultimap.create();
        this.tickableSounds = Lists.newArrayList();
        this.delayedSounds = Maps.newHashMap();
        this.playingSoundsStopTime = Maps.newHashMap();
        this.sndHandler = p_i45119_1_;
        this.options = p_i45119_2_;
        try {
            SoundSystemConfig.addLibrary((Class)LibraryLWJGLOpenAL.class);
            SoundSystemConfig.setCodec("ogg", (Class)CodecJOrbis.class);
        }
        catch (SoundSystemException var4) {
            SoundManager.logger.error(SoundManager.LOG_MARKER, "Error linking with the LibraryJavaSound plug-in", (Throwable)var4);
        }
    }
    
    public void reloadSoundSystem() {
        this.unloadSoundSystem();
        this.loadSoundSystem();
    }
    
    private synchronized void loadSoundSystem() {
        if (!this.loaded) {
            try {
                new Thread(new Runnable() {
                    private static final String __OBFID = "CL_00001142";
                    
                    @Override
                    public void run() {
                        SoundSystemConfig.setLogger((SoundSystemLogger)new SoundSystemLogger() {
                            private static final String __OBFID = "CL_00002378";
                            
                            public void message(final String p_message_1_, final int p_message_2_) {
                                if (!p_message_1_.isEmpty()) {
                                    SoundManager.logger.info(p_message_1_);
                                }
                            }
                            
                            public void importantMessage(final String p_importantMessage_1_, final int p_importantMessage_2_) {
                                if (!p_importantMessage_1_.isEmpty()) {
                                    SoundManager.logger.warn(p_importantMessage_1_);
                                }
                            }
                            
                            public void errorMessage(final String p_errorMessage_1_, final String p_errorMessage_2_, final int p_errorMessage_3_) {
                                if (!p_errorMessage_2_.isEmpty()) {
                                    SoundManager.logger.error("Error in class '" + p_errorMessage_1_ + "'");
                                    SoundManager.logger.error(p_errorMessage_2_);
                                }
                            }
                        });
                        SoundManager.this.sndSystem = new SoundSystemStarterThread(null);
                        SoundManager.this.loaded = true;
                        SoundManager.this.sndSystem.setMasterVolume(SoundManager.this.options.getSoundLevel(SoundCategory.MASTER));
                        SoundManager.logger.info(SoundManager.LOG_MARKER, "Sound engine started");
                    }
                }, "Sound Library Loader").start();
            }
            catch (RuntimeException var2) {
                SoundManager.logger.error(SoundManager.LOG_MARKER, "Error starting SoundSystem. Turning off sounds & music", (Throwable)var2);
                this.options.setSoundLevel(SoundCategory.MASTER, 0.0f);
                this.options.saveOptions();
            }
        }
    }
    
    private float getSoundCategoryVolume(final SoundCategory p_148595_1_) {
        return (p_148595_1_ != null && p_148595_1_ != SoundCategory.MASTER) ? this.options.getSoundLevel(p_148595_1_) : 1.0f;
    }
    
    public void setSoundCategoryVolume(final SoundCategory p_148601_1_, final float p_148601_2_) {
        if (this.loaded) {
            if (p_148601_1_ == SoundCategory.MASTER) {
                this.sndSystem.setMasterVolume(p_148601_2_);
            }
            else {
                for (final String var4 : this.categorySounds.get((Object)p_148601_1_)) {
                    final ISound var5 = this.playingSounds.get(var4);
                    final float var6 = this.getNormalizedVolume(var5, this.playingSoundPoolEntries.get(var5), p_148601_1_);
                    if (var6 <= 0.0f) {
                        this.stopSound(var5);
                    }
                    else {
                        this.sndSystem.setVolume(var4, var6);
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
            for (final String var2 : this.playingSounds.keySet()) {
                this.sndSystem.stop(var2);
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
        for (final ITickableSound sound : this.tickableSounds) {
            sound.update();
            if (sound.isDonePlaying()) {
                this.stopSound(sound);
            }
            else {
                final String nextKey = this.invPlayingSounds.get(sound);
                this.sndSystem.setVolume(nextKey, this.getNormalizedVolume(sound, this.playingSoundPoolEntries.get(sound), this.sndHandler.getSound(sound.getSoundLocation()).getSoundCategory()));
                this.sndSystem.setPitch(nextKey, this.getNormalizedPitch(sound, this.playingSoundPoolEntries.get(sound)));
                this.sndSystem.setPosition(nextKey, sound.getXPosF(), sound.getYPosF(), sound.getZPosF());
            }
        }
        final Iterator soundEntries = this.playingSounds.entrySet().iterator();
        try {
            while (soundEntries.hasNext()) {
                final Map.Entry nextEntry = soundEntries.next();
                final String nextKey = nextEntry.getKey();
                final ISound nextSound = nextEntry.getValue();
                if (!this.sndSystem.playing(nextKey)) {
                    final int var5 = this.playingSoundsStopTime.get(nextKey);
                    if (var5 > this.playTime) {
                        continue;
                    }
                    final int var6 = nextSound.getRepeatDelay();
                    if (nextSound.canRepeat() && var6 > 0) {
                        this.delayedSounds.put(nextSound, this.playTime + var6);
                    }
                    soundEntries.remove();
                    SoundManager.logger.debug(SoundManager.LOG_MARKER, "Removed channel {} because it's not playing anymore", new Object[] { nextKey });
                    this.sndSystem.removeSource(nextKey);
                    this.playingSoundsStopTime.remove(nextKey);
                    this.playingSoundPoolEntries.remove(nextSound);
                    try {
                        this.categorySounds.remove((Object)this.sndHandler.getSound(nextSound.getSoundLocation()).getSoundCategory(), (Object)nextKey);
                    }
                    catch (RuntimeException ex) {}
                    if (!(nextSound instanceof ITickableSound)) {
                        continue;
                    }
                    this.tickableSounds.remove(nextSound);
                }
            }
        }
        catch (ConcurrentModificationException e) {
            System.out.println(this.playingSounds.size());
            e.printStackTrace();
        }
        final Iterator var7 = this.delayedSounds.entrySet().iterator();
        while (var7.hasNext()) {
            final Map.Entry var8 = var7.next();
            if (this.playTime >= var8.getValue()) {
                final ISound nextSound = var8.getKey();
                if (nextSound instanceof ITickableSound) {
                    ((ITickableSound)nextSound).update();
                }
                this.playSound(nextSound);
                var7.remove();
            }
        }
    }
    
    public boolean isSoundPlaying(final ISound p_148597_1_) {
        if (!this.loaded) {
            return false;
        }
        final String var2 = this.invPlayingSounds.get(p_148597_1_);
        return var2 != null && (this.sndSystem.playing(var2) || (this.playingSoundsStopTime.containsKey(var2) && this.playingSoundsStopTime.get(var2) <= this.playTime));
    }
    
    public void stopSound(final ISound p_148602_1_) {
        if (this.loaded) {
            final String var2 = this.invPlayingSounds.get(p_148602_1_);
            if (var2 != null) {
                this.sndSystem.stop(var2);
            }
        }
    }
    
    public void playSound(final ISound p_148611_1_) {
        if (this.loaded) {
            if (this.sndSystem.getMasterVolume() <= 0.0f) {
                SoundManager.logger.debug(SoundManager.LOG_MARKER, "Skipped playing soundEvent: {}, master volume was zero", new Object[] { p_148611_1_.getSoundLocation() });
            }
            else {
                final SoundEventAccessorComposite var2 = this.sndHandler.getSound(p_148611_1_.getSoundLocation());
                if (var2 == null) {
                    SoundManager.logger.warn(SoundManager.LOG_MARKER, "Unable to play unknown soundEvent: {}", new Object[] { p_148611_1_.getSoundLocation() });
                }
                else {
                    final SoundPoolEntry var3 = var2.cloneEntry();
                    if (var3 == SoundHandler.missing_sound) {
                        SoundManager.logger.warn(SoundManager.LOG_MARKER, "Unable to play empty soundEvent: {}", new Object[] { var2.getSoundEventLocation() });
                    }
                    else {
                        final float var4 = p_148611_1_.getVolume();
                        float var5 = 16.0f;
                        if (var4 > 1.0f) {
                            var5 *= var4;
                        }
                        final SoundCategory var6 = var2.getSoundCategory();
                        final float var7 = this.getNormalizedVolume(p_148611_1_, var3, var6);
                        final double var8 = this.getNormalizedPitch(p_148611_1_, var3);
                        final ResourceLocation var9 = var3.getSoundPoolEntryLocation();
                        if (var7 == 0.0f) {
                            SoundManager.logger.debug(SoundManager.LOG_MARKER, "Skipped playing sound {}, volume was zero.", new Object[] { var9 });
                        }
                        else {
                            final boolean var10 = p_148611_1_.canRepeat() && p_148611_1_.getRepeatDelay() == 0;
                            final String var11 = MathHelper.func_180182_a((Random)ThreadLocalRandom.current()).toString();
                            if (var3.isStreamingSound()) {
                                this.sndSystem.newStreamingSource(false, var11, getURLForSoundResource(var9), var9.toString(), var10, p_148611_1_.getXPosF(), p_148611_1_.getYPosF(), p_148611_1_.getZPosF(), p_148611_1_.getAttenuationType().getTypeInt(), var5);
                            }
                            else {
                                this.sndSystem.newSource(false, var11, getURLForSoundResource(var9), var9.toString(), var10, p_148611_1_.getXPosF(), p_148611_1_.getYPosF(), p_148611_1_.getZPosF(), p_148611_1_.getAttenuationType().getTypeInt(), var5);
                            }
                            SoundManager.logger.debug(SoundManager.LOG_MARKER, "Playing sound {} for event {} as channel {}", new Object[] { var3.getSoundPoolEntryLocation(), var2.getSoundEventLocation(), var11 });
                            this.sndSystem.setPitch(var11, (float)var8);
                            this.sndSystem.setVolume(var11, var7);
                            this.sndSystem.play(var11);
                            this.playingSoundsStopTime.put(var11, this.playTime + 20);
                            this.playingSounds.put(var11, p_148611_1_);
                            this.playingSoundPoolEntries.put(p_148611_1_, var3);
                            if (var6 != SoundCategory.MASTER) {
                                this.categorySounds.put((Object)var6, (Object)var11);
                            }
                            if (p_148611_1_ instanceof ITickableSound) {
                                this.tickableSounds.add(p_148611_1_);
                            }
                        }
                    }
                }
            }
        }
    }
    
    private float getNormalizedPitch(final ISound p_148606_1_, final SoundPoolEntry p_148606_2_) {
        return (float)MathHelper.clamp_double(p_148606_1_.getPitch() * p_148606_2_.getPitch(), 0.5, 2.0);
    }
    
    private float getNormalizedVolume(final ISound p_148594_1_, final SoundPoolEntry p_148594_2_, final SoundCategory p_148594_3_) {
        return (float)MathHelper.clamp_double(p_148594_1_.getVolume() * p_148594_2_.getVolume(), 0.0, 1.0) * this.getSoundCategoryVolume(p_148594_3_);
    }
    
    public void pauseAllSounds() {
        for (final String var2 : this.playingSounds.keySet()) {
            SoundManager.logger.debug(SoundManager.LOG_MARKER, "Pausing channel {}", new Object[] { var2 });
            this.sndSystem.pause(var2);
        }
    }
    
    public void resumeAllSounds() {
        for (final String var2 : this.playingSounds.keySet()) {
            SoundManager.logger.debug(SoundManager.LOG_MARKER, "Resuming channel {}", new Object[] { var2 });
            this.sndSystem.play(var2);
        }
    }
    
    public void playDelayedSound(final ISound p_148599_1_, final int p_148599_2_) {
        this.delayedSounds.put(p_148599_1_, this.playTime + p_148599_2_);
    }
    
    private static URL getURLForSoundResource(final ResourceLocation p_148612_0_) {
        final String var1 = String.format("%s:%s:%s", "mcsounddomain", p_148612_0_.getResourceDomain(), p_148612_0_.getResourcePath());
        final URLStreamHandler var2 = new URLStreamHandler() {
            private static final String __OBFID = "CL_00001143";
            
            @Override
            protected URLConnection openConnection(final URL p_openConnection_1_) {
                return new URLConnection(p_openConnection_1_) {
                    private static final String __OBFID = "CL_00001144";
                    
                    @Override
                    public void connect() {
                    }
                    
                    @Override
                    public InputStream getInputStream() throws IOException {
                        return Minecraft.getMinecraft().getResourceManager().getResource(p_148612_0_).getInputStream();
                    }
                };
            }
        };
        try {
            return new URL(null, var1, var2);
        }
        catch (MalformedURLException var3) {
            throw new Error("TODO: Sanely handle url exception! :D");
        }
    }
    
    public void setListener(final EntityPlayer p_148615_1_, final float p_148615_2_) {
        if (this.loaded && p_148615_1_ != null) {
            final float var3 = p_148615_1_.prevRotationPitch + (p_148615_1_.rotationPitch - p_148615_1_.prevRotationPitch) * p_148615_2_;
            final float var4 = p_148615_1_.prevRotationYaw + (p_148615_1_.rotationYaw - p_148615_1_.prevRotationYaw) * p_148615_2_;
            final double var5 = p_148615_1_.prevPosX + (p_148615_1_.posX - p_148615_1_.prevPosX) * p_148615_2_;
            final double var6 = p_148615_1_.prevPosY + (p_148615_1_.posY - p_148615_1_.prevPosY) * p_148615_2_ + p_148615_1_.getEyeHeight();
            final double var7 = p_148615_1_.prevPosZ + (p_148615_1_.posZ - p_148615_1_.prevPosZ) * p_148615_2_;
            final float var8 = MathHelper.cos((var4 + 90.0f) * 0.017453292f);
            final float var9 = MathHelper.sin((var4 + 90.0f) * 0.017453292f);
            final float var10 = MathHelper.cos(-var3 * 0.017453292f);
            final float var11 = MathHelper.sin(-var3 * 0.017453292f);
            final float var12 = MathHelper.cos((-var3 + 90.0f) * 0.017453292f);
            final float var13 = MathHelper.sin((-var3 + 90.0f) * 0.017453292f);
            final float var14 = var8 * var10;
            final float var15 = var9 * var10;
            final float var16 = var8 * var12;
            final float var17 = var9 * var12;
            this.sndSystem.setListenerPosition((float)var5, (float)var6, (float)var7);
            this.sndSystem.setListenerOrientation(var14, var11, var15, var16, var13, var17);
        }
    }
    
    static {
        LOG_MARKER = MarkerManager.getMarker("SOUNDS");
        logger = LogManager.getLogger();
    }
    
    class SoundSystemStarterThread extends SoundSystem
    {
        private static final String __OBFID = "CL_00001145";
        
        private SoundSystemStarterThread() {
        }
        
        public boolean playing(final String p_playing_1_) {
            final Object var2 = SoundSystemConfig.THREAD_SYNC;
            synchronized (SoundSystemConfig.THREAD_SYNC) {
                if (this.soundLibrary == null) {
                    return false;
                }
                final Source var3 = this.soundLibrary.getSources().get(p_playing_1_);
                return var3 != null && (var3.playing() || var3.paused() || var3.preLoad);
            }
        }
        
        SoundSystemStarterThread(final SoundManager this$0, final Object p_i45118_2_) {
            this(this$0);
        }
    }
}
