package net.minecraft.client.audio;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class SoundHandler implements IResourceManagerReloadListener, ITickable {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(SoundList.class, new SoundListSerializer()).create();
    private static final ParameterizedType TYPE = new ParameterizedType() {

        public Type[] getActualTypeArguments() {
            return new Type[]{String.class, SoundList.class};
        }

        public Type getRawType() {
            return Map.class;
        }

        public Type getOwnerType() {
            return null;
        }
    };
    public static final SoundPoolEntry missing_sound = new SoundPoolEntry(new ResourceLocation("meta:missing_sound"), 0.0D, 0.0D, false);
    private final SoundRegistry sndRegistry = new SoundRegistry();
    private final SoundManager sndManager;
    private final IResourceManager mcResourceManager;

    public SoundHandler(IResourceManager manager, GameSettings gameSettingsIn) {
        this.mcResourceManager = manager;
        this.sndManager = new SoundManager(this, gameSettingsIn);
    }

    public SoundRegistry getSndRegistry() {
        return sndRegistry;
    }

    public void registerChris() {
        final SoundList sounds = new SoundList();
        sounds.setSoundCategory(SoundCategory.MASTER);
        final SoundList.SoundEntry sound = new SoundList.SoundEntry();

        {
            sound.setSoundEntryName("daun/chris");
            sound.setSoundEntryType(SoundList.SoundEntry.Type.FILE);
            sound.setSoundEntryPitch(1.0F);
            sound.setSoundEntryVolume(1.0F);
            sound.setSoundEntryWeight(1);
        }

        sounds.getSoundList().add(sound);
        loadSoundResource(new ResourceLocation("minecraft", "daun.chris"), sounds);
    }

    public void onResourceManagerReload(IResourceManager resourceManager) {
        this.sndManager.reloadSoundSystem();
        this.sndRegistry.clearMap();

        /* registering chris */
        registerChris();
        /* don't touch */

        for (String s : resourceManager.getResourceDomains()) {
            try {
                for (IResource iresource : resourceManager.getAllResources(new ResourceLocation(s, "sounds.json"))) {
                    try {
                        final Map<String, SoundList> map = this.getSoundMap(iresource.getInputStream());

                        for (Entry<String, SoundList> entry : map.entrySet()) {
                            this.loadSoundResource(new ResourceLocation(s, entry.getKey()), entry.getValue());
                        }
                    } catch (RuntimeException runtimeexception) {
                        LOGGER.warn("Invalid sounds.json", runtimeexception);
                    }
                }
            } catch (IOException ignored) {
            }
        }
    }

    protected Map<String, SoundList> getSoundMap(InputStream stream) {
        try {
            return GSON.fromJson(new InputStreamReader(stream), TYPE);
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    private void loadSoundResource(ResourceLocation location, SoundList sounds) {
        final boolean flag = !this.sndRegistry.containsKey(location);
        final SoundEventAccessorComposite soundeventaccessorcomposite;

        if (!flag && !sounds.canReplaceExisting()) {
            soundeventaccessorcomposite = this.sndRegistry.getObject(location);
        } else {
            if (!flag) {
                LOGGER.debug("Replaced sound event location {}", location);
            }

            soundeventaccessorcomposite = new SoundEventAccessorComposite(location, 1.0D, 1.0D, sounds.getSoundCategory());
            this.sndRegistry.registerSound(soundeventaccessorcomposite);
        }

        for (final SoundList.SoundEntry entry : sounds.getSoundList()) {
            final String s = entry.getSoundEntryName();
            final ResourceLocation resourcelocation = new ResourceLocation(s);
            final String s1 = s.contains(":") ? resourcelocation.getResourceDomain() : location.getResourceDomain();
            final ISoundEventAccessor<SoundPoolEntry> accessor;

            switch (entry.getSoundEntryType()) {
                case FILE:
                    final ResourceLocation resourcelocation1 = new ResourceLocation(s1, "sounds/" + resourcelocation.getResourcePath() + ".ogg");
                    InputStream inputstream = null;

                    try {
                        inputstream = this.mcResourceManager.getResource(resourcelocation1).getInputStream();
                    } catch (FileNotFoundException var18) {
                        LOGGER.warn("File {} does not exist, cannot add it to event {}", resourcelocation1, location);
                        continue;
                    } catch (IOException ioexception) {
                        LOGGER.warn("Could not load sound file " + resourcelocation1 + ", cannot add it to event " + location, ioexception);
                        continue;
                    } finally {
                        IOUtils.closeQuietly(inputstream);
                    }

                    accessor = new SoundEventAccessor(new SoundPoolEntry(resourcelocation1, entry.getSoundEntryPitch(), entry.getSoundEntryVolume(), entry.isStreaming()), entry.getSoundEntryWeight());
                    break;

                case SOUND_EVENT:
                    accessor = new ISoundEventAccessor<SoundPoolEntry>() {

                        final ResourceLocation field_148726_a = new ResourceLocation(s1, entry.getSoundEntryName());

                        public int getWeight() {
                            final SoundEventAccessorComposite soundeventaccessorcomposite1 = SoundHandler.this.sndRegistry.getObject(this.field_148726_a);
                            return soundeventaccessorcomposite1 == null ? 0 : soundeventaccessorcomposite1.getWeight();
                        }

                        public SoundPoolEntry cloneEntry() {
                            final SoundEventAccessorComposite soundeventaccessorcomposite1 = SoundHandler.this.sndRegistry.getObject(this.field_148726_a);
                            return soundeventaccessorcomposite1 == null ? SoundHandler.missing_sound : soundeventaccessorcomposite1.cloneEntry();
                        }
                    };

                    break;
                default:
                    throw new IllegalStateException("IN YOU FACE");
            }

            soundeventaccessorcomposite.addSoundToEventPool(accessor);
        }
    }

    public SoundEventAccessorComposite getSound(ResourceLocation location) {
        return this.sndRegistry.getObject(location);
    }

    /**
     * Play a sound
     */
    public void playSound(ISound sound) {
        this.sndManager.playSound(sound);
    }

    /**
     * Plays the sound in n ticks
     */
    public void playDelayedSound(ISound sound, int delay) {
        this.sndManager.playDelayedSound(sound, delay);
    }

    public void setListener(EntityPlayer player, float p_147691_2_) {
        this.sndManager.setListener(player, p_147691_2_);
    }

    public void pauseSounds() {
        this.sndManager.pauseAllSounds();
    }

    public void stopSounds() {
        this.sndManager.stopAllSounds();
    }

    public void unloadSounds() {
        this.sndManager.unloadSoundSystem();
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    public void update() {
        this.sndManager.updateAllSounds();
    }

    public void resumeSounds() {
        this.sndManager.resumeAllSounds();
    }

    public void setSoundLevel(SoundCategory category, float volume) {
        if (category == SoundCategory.MASTER && volume <= 0.0F) {
            this.stopSounds();
        }

        this.sndManager.setSoundCategoryVolume(category, volume);
    }

    public void stopSound(ISound p_147683_1_) {
        this.sndManager.stopSound(p_147683_1_);
    }

    /**
     * Returns a random sound from one or more categories
     */
    public SoundEventAccessorComposite getRandomSoundFromCategories(SoundCategory... categories) {
        final List<SoundEventAccessorComposite> list = Lists.newArrayList();

        for (ResourceLocation resourcelocation : this.sndRegistry.getKeys()) {
            final SoundEventAccessorComposite soundeventaccessorcomposite = this.sndRegistry.getObject(resourcelocation);

            if (ArrayUtils.contains(categories, soundeventaccessorcomposite.getSoundCategory())) {
                list.add(soundeventaccessorcomposite);
            }
        }

        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(new Random().nextInt(list.size()));
        }
    }

    public boolean isSoundPlaying(ISound sound) {
        return this.sndManager.isSoundPlaying(sound);
    }

}
