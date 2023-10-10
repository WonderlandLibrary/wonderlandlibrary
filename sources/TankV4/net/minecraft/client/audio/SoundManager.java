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
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
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
   private final Map playingSoundsStopTime;
   private Map playingSoundPoolEntries;
   private boolean loaded;
   private final Multimap categorySounds;
   private final GameSettings options;
   private static final Marker LOG_MARKER = MarkerManager.getMarker("SOUNDS");
   private final Map playingSounds = HashBiMap.create();
   private final Map invPlayingSounds;
   private static final Logger logger = LogManager.getLogger();
   private final Map delayedSounds;
   private SoundManager.SoundSystemStarterThread sndSystem;
   private int playTime = 0;
   private final List tickableSounds;
   private final SoundHandler sndHandler;

   static void access$1(SoundManager var0, SoundManager.SoundSystemStarterThread var1) {
      var0.sndSystem = var1;
   }

   public SoundManager(SoundHandler var1, GameSettings var2) {
      this.invPlayingSounds = ((BiMap)this.playingSounds).inverse();
      this.playingSoundPoolEntries = Maps.newHashMap();
      this.categorySounds = HashMultimap.create();
      this.tickableSounds = Lists.newArrayList();
      this.delayedSounds = Maps.newHashMap();
      this.playingSoundsStopTime = Maps.newHashMap();
      this.sndHandler = var1;
      this.options = var2;

      try {
         SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
         SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
      } catch (SoundSystemException var4) {
         logger.error(LOG_MARKER, (String)"Error linking with the LibraryJavaSound plug-in", (Throwable)var4);
      }

   }

   static void access$2(SoundManager var0, boolean var1) {
      var0.loaded = var1;
   }

   public void updateAllSounds() {
      ++this.playTime;
      Iterator var2 = this.tickableSounds.iterator();

      String var3;
      while(var2.hasNext()) {
         ITickableSound var1 = (ITickableSound)var2.next();
         var1.update();
         if (var1.isDonePlaying()) {
            this.stopSound(var1);
         } else {
            var3 = (String)this.invPlayingSounds.get(var1);
            this.sndSystem.setVolume(var3, this.getNormalizedVolume(var1, (SoundPoolEntry)this.playingSoundPoolEntries.get(var1), this.sndHandler.getSound(var1.getSoundLocation()).getSoundCategory()));
            this.sndSystem.setPitch(var3, this.getNormalizedPitch(var1, (SoundPoolEntry)this.playingSoundPoolEntries.get(var1)));
            this.sndSystem.setPosition(var3, var1.getXPosF(), var1.getYPosF(), var1.getZPosF());
         }
      }

      Iterator var9 = this.playingSounds.entrySet().iterator();

      ISound var4;
      while(var9.hasNext()) {
         Entry var10 = (Entry)var9.next();
         var3 = (String)var10.getKey();
         var4 = (ISound)var10.getValue();
         if (!this.sndSystem.playing(var3)) {
            int var5 = (Integer)this.playingSoundsStopTime.get(var3);
            if (var5 <= this.playTime) {
               int var6 = var4.getRepeatDelay();
               if (var4.canRepeat() && var6 > 0) {
                  this.delayedSounds.put(var4, this.playTime + var6);
               }

               var9.remove();
               logger.debug(LOG_MARKER, "Removed channel {} because it's not playing anymore", var3);
               this.sndSystem.removeSource(var3);
               this.playingSoundsStopTime.remove(var3);
               this.playingSoundPoolEntries.remove(var4);

               try {
                  this.categorySounds.remove(this.sndHandler.getSound(var4.getSoundLocation()).getSoundCategory(), var3);
               } catch (RuntimeException var8) {
               }

               if (var4 instanceof ITickableSound) {
                  this.tickableSounds.remove(var4);
               }
            }
         }
      }

      var2 = this.delayedSounds.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var11 = (Entry)var2.next();
         if (this.playTime >= (Integer)var11.getValue()) {
            var4 = (ISound)var11.getKey();
            if (var4 instanceof ITickableSound) {
               ((ITickableSound)var4).update();
            }

            this.playSound(var4);
            var2.remove();
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

   private static URL getURLForSoundResource(ResourceLocation var0) {
      String var1 = String.format("%s:%s:%s", "mcsounddomain", var0.getResourceDomain(), var0.getResourcePath());
      URLStreamHandler var2 = new URLStreamHandler(var0) {
         private final ResourceLocation val$p_148612_0_;

         protected URLConnection openConnection(URL var1) {
            return new URLConnection(this, var1, this.val$p_148612_0_) {
               final <undefinedtype> this$1;
               private final ResourceLocation val$p_148612_0_;

               public void connect() throws IOException {
               }

               {
                  this.this$1 = var1;
                  this.val$p_148612_0_ = var3;
               }

               public InputStream getInputStream() throws IOException {
                  return Minecraft.getMinecraft().getResourceManager().getResource(this.val$p_148612_0_).getInputStream();
               }
            };
         }

         {
            this.val$p_148612_0_ = var1;
         }
      };

      try {
         return new URL((URL)null, var1, var2);
      } catch (MalformedURLException var4) {
         throw new Error("TODO: Sanely handle url exception! :D");
      }
   }

   static GameSettings access$4(SoundManager var0) {
      return var0.options;
   }

   public void playSound(ISound var1) {
      if (this.loaded) {
         if (this.sndSystem.getMasterVolume() <= 0.0F) {
            logger.debug(LOG_MARKER, "Skipped playing soundEvent: {}, master volume was zero", var1.getSoundLocation());
         } else {
            SoundEventAccessorComposite var2 = this.sndHandler.getSound(var1.getSoundLocation());
            if (var2 == null) {
               logger.warn(LOG_MARKER, "Unable to play unknown soundEvent: {}", var1.getSoundLocation());
            } else {
               SoundPoolEntry var3 = var2.cloneEntry();
               if (var3 == SoundHandler.missing_sound) {
                  logger.warn(LOG_MARKER, "Unable to play empty soundEvent: {}", var2.getSoundEventLocation());
               } else {
                  float var4 = var1.getVolume();
                  float var5 = 16.0F;
                  if (var4 > 1.0F) {
                     var5 *= var4;
                  }

                  SoundCategory var6 = var2.getSoundCategory();
                  float var7 = this.getNormalizedVolume(var1, var3, var6);
                  double var8 = (double)this.getNormalizedPitch(var1, var3);
                  ResourceLocation var10 = var3.getSoundPoolEntryLocation();
                  if (var7 == 0.0F) {
                     logger.debug(LOG_MARKER, "Skipped playing sound {}, volume was zero.", var10);
                  } else {
                     boolean var11 = var1.canRepeat() && var1.getRepeatDelay() == 0;
                     String var12 = MathHelper.getRandomUuid(ThreadLocalRandom.current()).toString();
                     if (var3.isStreamingSound()) {
                        this.sndSystem.newStreamingSource(false, var12, getURLForSoundResource(var10), var10.toString(), var11, var1.getXPosF(), var1.getYPosF(), var1.getZPosF(), var1.getAttenuationType().getTypeInt(), var5);
                     } else {
                        this.sndSystem.newSource(false, var12, getURLForSoundResource(var10), var10.toString(), var11, var1.getXPosF(), var1.getYPosF(), var1.getZPosF(), var1.getAttenuationType().getTypeInt(), var5);
                     }

                     logger.debug(LOG_MARKER, "Playing sound {} for event {} as channel {}", var3.getSoundPoolEntryLocation(), var2.getSoundEventLocation(), var12);
                     this.sndSystem.setPitch(var12, (float)var8);
                     this.sndSystem.setVolume(var12, var7);
                     this.sndSystem.play(var12);
                     this.playingSoundsStopTime.put(var12, this.playTime + 20);
                     this.playingSounds.put(var12, var1);
                     this.playingSoundPoolEntries.put(var1, var3);
                     if (var6 != SoundCategory.MASTER) {
                        this.categorySounds.put(var6, var12);
                     }

                     if (var1 instanceof ITickableSound) {
                        this.tickableSounds.add((ITickableSound)var1);
                     }
                  }
               }
            }
         }
      }

   }

   public void stopAllSounds() {
      if (this.loaded) {
         Iterator var2 = this.playingSounds.keySet().iterator();

         while(var2.hasNext()) {
            String var1 = (String)var2.next();
            this.sndSystem.stop(var1);
         }

         this.playingSounds.clear();
         this.delayedSounds.clear();
         this.tickableSounds.clear();
         this.categorySounds.clear();
         this.playingSoundPoolEntries.clear();
         this.playingSoundsStopTime.clear();
      }

   }

   public void stopSound(ISound var1) {
      if (this.loaded) {
         String var2 = (String)this.invPlayingSounds.get(var1);
         if (var2 != null) {
            this.sndSystem.stop(var2);
         }
      }

   }

   public void setListener(EntityPlayer var1, float var2) {
      if (this.loaded && var1 != null) {
         float var3 = var1.prevRotationPitch + (var1.rotationPitch - var1.prevRotationPitch) * var2;
         float var4 = var1.prevRotationYaw + (var1.rotationYaw - var1.prevRotationYaw) * var2;
         double var5 = var1.prevPosX + (var1.posX - var1.prevPosX) * (double)var2;
         double var7 = var1.prevPosY + (var1.posY - var1.prevPosY) * (double)var2 + (double)var1.getEyeHeight();
         double var9 = var1.prevPosZ + (var1.posZ - var1.prevPosZ) * (double)var2;
         float var11 = MathHelper.cos((var4 + 90.0F) * 0.017453292F);
         float var12 = MathHelper.sin((var4 + 90.0F) * 0.017453292F);
         float var13 = MathHelper.cos(-var3 * 0.017453292F);
         float var14 = MathHelper.sin(-var3 * 0.017453292F);
         float var15 = MathHelper.cos((-var3 + 90.0F) * 0.017453292F);
         float var16 = MathHelper.sin((-var3 + 90.0F) * 0.017453292F);
         float var17 = var11 * var13;
         float var18 = var12 * var13;
         float var19 = var11 * var15;
         float var20 = var12 * var15;
         this.sndSystem.setListenerPosition((float)var5, (float)var7, (float)var9);
         this.sndSystem.setListenerOrientation(var17, var14, var18, var19, var16, var20);
      }

   }

   static Logger access$0() {
      return logger;
   }

   static SoundManager.SoundSystemStarterThread access$3(SoundManager var0) {
      return var0.sndSystem;
   }

   private float getNormalizedVolume(ISound var1, SoundPoolEntry var2, SoundCategory var3) {
      return (float)MathHelper.clamp_double((double)var1.getVolume() * var2.getVolume(), 0.0D, 1.0D) * this.getSoundCategoryVolume(var3);
   }

   static Marker access$5() {
      return LOG_MARKER;
   }

   private synchronized void loadSoundSystem() {
      if (!this.loaded) {
         try {
            (new Thread(new Runnable(this) {
               final SoundManager this$0;

               public void run() {
                  SoundSystemConfig.setLogger(new SoundSystemLogger(this) {
                     final <undefinedtype> this$1;

                     public void errorMessage(String var1, String var2, int var3) {
                        if (!var2.isEmpty()) {
                           SoundManager.access$0().error("Error in class '" + var1 + "'");
                           SoundManager.access$0().error(var2);
                        }

                     }

                     public void importantMessage(String var1, int var2) {
                        if (!var1.isEmpty()) {
                           SoundManager.access$0().warn(var1);
                        }

                     }

                     {
                        this.this$1 = var1;
                     }

                     public void message(String var1, int var2) {
                        if (!var1.isEmpty()) {
                           SoundManager.access$0().info(var1);
                        }

                     }
                  });
                  SoundManager.access$1(this.this$0, this.this$0.new SoundSystemStarterThread(this.this$0, (SoundManager.SoundSystemStarterThread)null));
                  SoundManager.access$2(this.this$0, true);
                  SoundManager.access$3(this.this$0).setMasterVolume(SoundManager.access$4(this.this$0).getSoundLevel(SoundCategory.MASTER));
                  SoundManager.access$0().info(SoundManager.access$5(), "Sound engine started");
               }

               {
                  this.this$0 = var1;
               }
            }, "Sound Library Loader")).start();
         } catch (RuntimeException var2) {
            logger.error(LOG_MARKER, (String)"Error starting SoundSystem. Turning off sounds & music", (Throwable)var2);
            this.options.setSoundLevel(SoundCategory.MASTER, 0.0F);
            this.options.saveOptions();
         }
      }

   }

   public void reloadSoundSystem() {
      this.unloadSoundSystem();
      this.loadSoundSystem();
   }

   public void pauseAllSounds() {
      Iterator var2 = this.playingSounds.keySet().iterator();

      while(var2.hasNext()) {
         String var1 = (String)var2.next();
         logger.debug(LOG_MARKER, "Pausing channel {}", var1);
         this.sndSystem.pause(var1);
      }

   }

   private float getSoundCategoryVolume(SoundCategory var1) {
      return var1 != null && var1 != SoundCategory.MASTER ? this.options.getSoundLevel(var1) : 1.0F;
   }

   public void playDelayedSound(ISound var1, int var2) {
      this.delayedSounds.put(var1, this.playTime + var2);
   }

   public boolean isSoundPlaying(ISound var1) {
      if (!this.loaded) {
         return false;
      } else {
         String var2 = (String)this.invPlayingSounds.get(var1);
         return var2 == null ? false : this.sndSystem.playing(var2) || this.playingSoundsStopTime.containsKey(var2) && (Integer)this.playingSoundsStopTime.get(var2) <= this.playTime;
      }
   }

   public void setSoundCategoryVolume(SoundCategory var1, float var2) {
      if (this.loaded) {
         if (var1 == SoundCategory.MASTER) {
            this.sndSystem.setMasterVolume(var2);
         } else {
            Iterator var4 = this.categorySounds.get(var1).iterator();

            while(var4.hasNext()) {
               String var3 = (String)var4.next();
               ISound var5 = (ISound)this.playingSounds.get(var3);
               float var6 = this.getNormalizedVolume(var5, (SoundPoolEntry)this.playingSoundPoolEntries.get(var5), var1);
               if (var6 <= 0.0F) {
                  this.stopSound(var5);
               } else {
                  this.sndSystem.setVolume(var3, var6);
               }
            }
         }
      }

   }

   private float getNormalizedPitch(ISound var1, SoundPoolEntry var2) {
      return (float)MathHelper.clamp_double((double)var1.getPitch() * var2.getPitch(), 0.5D, 2.0D);
   }

   public void resumeAllSounds() {
      Iterator var2 = this.playingSounds.keySet().iterator();

      while(var2.hasNext()) {
         String var1 = (String)var2.next();
         logger.debug(LOG_MARKER, "Resuming channel {}", var1);
         this.sndSystem.play(var1);
      }

   }

   class SoundSystemStarterThread extends SoundSystem {
      final SoundManager this$0;

      public boolean playing(String var1) {
         Object var2;
         synchronized(var2 = SoundSystemConfig.THREAD_SYNC){}
         if (this.soundLibrary == null) {
            return false;
         } else {
            Source var3 = (Source)this.soundLibrary.getSources().get(var1);
            return var3 == null ? false : var3.playing() || var3.paused() || var3.preLoad;
         }
      }

      SoundSystemStarterThread(SoundManager var1, SoundManager.SoundSystemStarterThread var2) {
         this(var1);
      }

      private SoundSystemStarterThread(SoundManager var1) {
         this.this$0 = var1;
      }
   }
}
