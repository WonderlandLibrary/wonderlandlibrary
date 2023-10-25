/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  org.apache.commons.io.IOUtils
 *  org.apache.commons.lang3.ArrayUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.audio;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.ISoundEventAccessor;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.SoundEventAccessorComposite;
import net.minecraft.client.audio.SoundList;
import net.minecraft.client.audio.SoundListSerializer;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.audio.SoundPoolEntry;
import net.minecraft.client.audio.SoundRegistry;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SoundHandler
implements IResourceManagerReloadListener,
ITickable {
    private static final ParameterizedType TYPE;
    private final IResourceManager mcResourceManager;
    private static final Logger logger;
    public static final SoundPoolEntry missing_sound;
    private static final Gson GSON;
    private final SoundManager sndManager;
    private final SoundRegistry sndRegistry = new SoundRegistry();

    public void playDelayedSound(ISound iSound, int n) {
        this.sndManager.playDelayedSound(iSound, n);
    }

    public void setListener(EntityPlayer entityPlayer, float f) {
        this.sndManager.setListener(entityPlayer, f);
    }

    public void resumeSounds() {
        this.sndManager.resumeAllSounds();
    }

    @Override
    public void update() {
        this.sndManager.updateAllSounds();
    }

    public void stopSound(ISound iSound) {
        this.sndManager.stopSound(iSound);
    }

    protected Map<String, SoundList> getSoundMap(InputStream inputStream) {
        Map map = (Map)GSON.fromJson((Reader)new InputStreamReader(inputStream), (Type)TYPE);
        IOUtils.closeQuietly((InputStream)inputStream);
        return map;
    }

    public boolean isSoundPlaying(ISound iSound) {
        return this.sndManager.isSoundPlaying(iSound);
    }

    public SoundEventAccessorComposite getSound(ResourceLocation resourceLocation) {
        return (SoundEventAccessorComposite)this.sndRegistry.getObject(resourceLocation);
    }

    @Override
    public void onResourceManagerReload(IResourceManager iResourceManager) {
        this.sndManager.reloadSoundSystem();
        this.sndRegistry.clearMap();
        for (String string : iResourceManager.getResourceDomains()) {
            try {
                for (IResource iResource : iResourceManager.getAllResources(new ResourceLocation(string, "sounds.json"))) {
                    try {
                        Map<String, SoundList> map = this.getSoundMap(iResource.getInputStream());
                        for (Map.Entry<String, SoundList> entry : map.entrySet()) {
                            this.loadSoundResource(new ResourceLocation(string, entry.getKey()), entry.getValue());
                        }
                    }
                    catch (RuntimeException runtimeException) {
                        logger.warn("Invalid sounds.json", (Throwable)runtimeException);
                    }
                }
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }

    public SoundHandler(IResourceManager iResourceManager, GameSettings gameSettings) {
        this.mcResourceManager = iResourceManager;
        this.sndManager = new SoundManager(this, gameSettings);
    }

    public void playSound(ISound iSound) {
        this.sndManager.playSound(iSound);
    }

    public void unloadSounds() {
        this.sndManager.unloadSoundSystem();
    }

    public SoundEventAccessorComposite getRandomSoundFromCategories(SoundCategory ... soundCategoryArray) {
        ArrayList arrayList = Lists.newArrayList();
        for (ResourceLocation resourceLocation : this.sndRegistry.getKeys()) {
            SoundEventAccessorComposite soundEventAccessorComposite = (SoundEventAccessorComposite)this.sndRegistry.getObject(resourceLocation);
            if (!ArrayUtils.contains((Object[])soundCategoryArray, (Object)((Object)soundEventAccessorComposite.getSoundCategory()))) continue;
            arrayList.add(soundEventAccessorComposite);
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return (SoundEventAccessorComposite)arrayList.get(new Random().nextInt(arrayList.size()));
    }

    static {
        logger = LogManager.getLogger();
        GSON = new GsonBuilder().registerTypeAdapter(SoundList.class, (Object)new SoundListSerializer()).create();
        TYPE = new ParameterizedType(){

            @Override
            public Type getRawType() {
                return Map.class;
            }

            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{String.class, SoundList.class};
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
        missing_sound = new SoundPoolEntry(new ResourceLocation("meta:missing_sound"), 0.0, 0.0, false);
    }

    private void loadSoundResource(ResourceLocation resourceLocation, SoundList soundList) {
        SoundEventAccessorComposite soundEventAccessorComposite;
        boolean bl;
        boolean bl2 = bl = !this.sndRegistry.containsKey(resourceLocation);
        if (!bl && !soundList.canReplaceExisting()) {
            soundEventAccessorComposite = (SoundEventAccessorComposite)this.sndRegistry.getObject(resourceLocation);
        } else {
            if (!bl) {
                logger.debug("Replaced sound event location {}", new Object[]{resourceLocation});
            }
            soundEventAccessorComposite = new SoundEventAccessorComposite(resourceLocation, 1.0, 1.0, soundList.getSoundCategory());
            this.sndRegistry.registerSound(soundEventAccessorComposite);
        }
        block7: for (SoundList.SoundEntry soundEntry : soundList.getSoundList()) {
            ISoundEventAccessor<SoundPoolEntry> iSoundEventAccessor;
            String string = soundEntry.getSoundEntryName();
            ResourceLocation resourceLocation2 = new ResourceLocation(string);
            String string2 = string.contains(":") ? resourceLocation2.getResourceDomain() : resourceLocation.getResourceDomain();
            switch (soundEntry.getSoundEntryType()) {
                case FILE: {
                    ResourceLocation resourceLocation3 = new ResourceLocation(string2, "sounds/" + resourceLocation2.getResourcePath() + ".ogg");
                    InputStream inputStream = null;
                    try {
                        inputStream = this.mcResourceManager.getResource(resourceLocation3).getInputStream();
                    }
                    catch (FileNotFoundException fileNotFoundException) {
                        logger.warn("File {} does not exist, cannot add it to event {}", new Object[]{resourceLocation3, resourceLocation});
                        IOUtils.closeQuietly((InputStream)inputStream);
                        continue block7;
                    }
                    catch (IOException iOException) {
                        logger.warn("Could not load sound file " + resourceLocation3 + ", cannot add it to event " + resourceLocation, (Throwable)iOException);
                        IOUtils.closeQuietly((InputStream)inputStream);
                        continue block7;
                    }
                    IOUtils.closeQuietly((InputStream)inputStream);
                    iSoundEventAccessor = new SoundEventAccessor(new SoundPoolEntry(resourceLocation3, soundEntry.getSoundEntryPitch(), soundEntry.getSoundEntryVolume(), soundEntry.isStreaming()), soundEntry.getSoundEntryWeight());
                    break;
                }
                case SOUND_EVENT: {
                    iSoundEventAccessor = new ISoundEventAccessor<SoundPoolEntry>(string2, soundEntry){
                        final ResourceLocation field_148726_a;

                        @Override
                        public SoundPoolEntry cloneEntry() {
                            SoundEventAccessorComposite soundEventAccessorComposite = (SoundEventAccessorComposite)SoundHandler.this.sndRegistry.getObject(this.field_148726_a);
                            return soundEventAccessorComposite == null ? missing_sound : soundEventAccessorComposite.cloneEntry();
                        }
                        {
                            this.field_148726_a = new ResourceLocation(string, soundEntry.getSoundEntryName());
                        }

                        @Override
                        public int getWeight() {
                            SoundEventAccessorComposite soundEventAccessorComposite = (SoundEventAccessorComposite)SoundHandler.this.sndRegistry.getObject(this.field_148726_a);
                            return soundEventAccessorComposite == null ? 0 : soundEventAccessorComposite.getWeight();
                        }
                    };
                    break;
                }
                default: {
                    throw new IllegalStateException("IN YOU FACE");
                }
            }
            soundEventAccessorComposite.addSoundToEventPool(iSoundEventAccessor);
        }
    }

    public void setSoundLevel(SoundCategory soundCategory, float f) {
        if (soundCategory == SoundCategory.MASTER && f <= 0.0f) {
            this.stopSounds();
        }
        this.sndManager.setSoundCategoryVolume(soundCategory, f);
    }

    public void pauseSounds() {
        this.sndManager.pauseAllSounds();
    }

    public void stopSounds() {
        this.sndManager.stopAllSounds();
    }
}

