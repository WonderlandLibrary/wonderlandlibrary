/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.audio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.ISoundEventAccessor;
import net.minecraft.client.audio.ISoundEventListener;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.SoundList;
import net.minecraft.client.audio.SoundListSerializer;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.audio.SoundRegistry;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ITickable;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SoundHandler
implements IResourceManagerReloadListener,
ITickable {
    public static final Sound MISSING_SOUND = new Sound("meta:missing_sound", 1.0f, 1.0f, 1, Sound.Type.FILE, false);
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().registerTypeHierarchyAdapter(ITextComponent.class, new ITextComponent.Serializer()).registerTypeAdapter((Type)((Object)SoundList.class), new SoundListSerializer()).create();
    private static final ParameterizedType TYPE = new ParameterizedType(){

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{String.class, SoundList.class};
        }

        @Override
        public Type getRawType() {
            return Map.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    };
    private final SoundRegistry soundRegistry = new SoundRegistry();
    private final SoundManager sndManager;
    private final IResourceManager mcResourceManager;

    public SoundHandler(IResourceManager manager, GameSettings gameSettingsIn) {
        this.mcResourceManager = manager;
        this.sndManager = new SoundManager(this, gameSettingsIn);
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        this.soundRegistry.clearMap();
        for (String s : resourceManager.getResourceDomains()) {
            try {
                for (IResource iresource : resourceManager.getAllResources(new ResourceLocation(s, "sounds.json"))) {
                    try {
                        Map<String, SoundList> map = this.getSoundMap(iresource.getInputStream());
                        for (Map.Entry<String, SoundList> entry : map.entrySet()) {
                            this.loadSoundResource(new ResourceLocation(s, entry.getKey()), entry.getValue());
                        }
                    }
                    catch (RuntimeException runtimeexception) {
                        LOGGER.warn("Invalid sounds.json", (Throwable)runtimeexception);
                    }
                }
            }
            catch (IOException iOException) {
            }
        }
        for (ResourceLocation resourcelocation : this.soundRegistry.getKeys()) {
            String s1;
            SoundEventAccessor soundeventaccessor = (SoundEventAccessor)this.soundRegistry.getObject(resourcelocation);
            if (!(soundeventaccessor.getSubtitle() instanceof TextComponentTranslation) || I18n.hasKey(s1 = ((TextComponentTranslation)soundeventaccessor.getSubtitle()).getKey())) continue;
            LOGGER.debug("Missing subtitle {} for event: {}", s1, resourcelocation);
        }
        for (ResourceLocation resourcelocation1 : this.soundRegistry.getKeys()) {
            if (SoundEvent.REGISTRY.getObject(resourcelocation1) != null) continue;
            LOGGER.debug("Not having sound event for: {}", resourcelocation1);
        }
        this.sndManager.reloadSoundSystem();
    }

    @Nullable
    protected Map<String, SoundList> getSoundMap(InputStream stream) {
        Map map;
        try {
            map = (Map)JsonUtils.func_193841_a(GSON, new InputStreamReader(stream, StandardCharsets.UTF_8), TYPE);
        }
        finally {
            IOUtils.closeQuietly(stream);
        }
        return map;
    }

    private void loadSoundResource(ResourceLocation location, SoundList sounds) {
        boolean flag;
        SoundEventAccessor soundeventaccessor = (SoundEventAccessor)this.soundRegistry.getObject(location);
        boolean bl = flag = soundeventaccessor == null;
        if (flag || sounds.canReplaceExisting()) {
            if (!flag) {
                LOGGER.debug("Replaced sound event location {}", location);
            }
            soundeventaccessor = new SoundEventAccessor(location, sounds.getSubtitle());
            this.soundRegistry.add(soundeventaccessor);
        }
        block4: for (final Sound sound : sounds.getSounds()) {
            ISoundEventAccessor<Sound> isoundeventaccessor;
            final ResourceLocation resourcelocation = sound.getSoundLocation();
            switch (sound.getType()) {
                case FILE: {
                    if (!this.validateSoundResource(sound, location)) continue block4;
                    isoundeventaccessor = sound;
                    break;
                }
                case SOUND_EVENT: {
                    isoundeventaccessor = new ISoundEventAccessor<Sound>(){

                        @Override
                        public int getWeight() {
                            SoundEventAccessor soundeventaccessor1 = (SoundEventAccessor)SoundHandler.this.soundRegistry.getObject(resourcelocation);
                            return soundeventaccessor1 == null ? 0 : soundeventaccessor1.getWeight();
                        }

                        @Override
                        public Sound cloneEntry() {
                            SoundEventAccessor soundeventaccessor1 = (SoundEventAccessor)SoundHandler.this.soundRegistry.getObject(resourcelocation);
                            if (soundeventaccessor1 == null) {
                                return MISSING_SOUND;
                            }
                            Sound sound1 = soundeventaccessor1.cloneEntry();
                            return new Sound(sound1.getSoundLocation().toString(), sound1.getVolume() * sound.getVolume(), sound1.getPitch() * sound.getPitch(), sound.getWeight(), Sound.Type.FILE, sound1.isStreaming() || sound.isStreaming());
                        }
                    };
                    break;
                }
                default: {
                    throw new IllegalStateException("Unknown SoundEventRegistration type: " + (Object)((Object)sound.getType()));
                }
            }
            soundeventaccessor.addSound(isoundeventaccessor);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private boolean validateSoundResource(Sound p_184401_1_, ResourceLocation p_184401_2_) {
        boolean bl;
        ResourceLocation resourcelocation = p_184401_1_.getSoundAsOggLocation();
        IResource iresource = null;
        try {
            iresource = this.mcResourceManager.getResource(resourcelocation);
            iresource.getInputStream();
            bl = true;
        }
        catch (FileNotFoundException var11) {
            LOGGER.warn("File {} does not exist, cannot add it to event {}", resourcelocation, p_184401_2_);
            boolean flag = false;
            {
                catch (Throwable throwable) {
                    IOUtils.closeQuietly(iresource);
                    throw throwable;
                }
            }
            IOUtils.closeQuietly((Closeable)iresource);
            return flag;
            catch (IOException ioexception) {
                boolean flag2;
                LOGGER.warn("Could not load sound file {}, cannot add it to event {}", resourcelocation, p_184401_2_, ioexception);
                boolean bl2 = flag2 = false;
                IOUtils.closeQuietly((Closeable)iresource);
                return bl2;
            }
        }
        IOUtils.closeQuietly((Closeable)iresource);
        return bl;
    }

    @Nullable
    public SoundEventAccessor getAccessor(ResourceLocation location) {
        return (SoundEventAccessor)this.soundRegistry.getObject(location);
    }

    public void playSound(ISound sound) {
        this.sndManager.playSound(sound);
    }

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

    @Override
    public void update() {
        this.sndManager.updateAllSounds();
    }

    public void resumeSounds() {
        this.sndManager.resumeAllSounds();
    }

    public void setSoundLevel(SoundCategory category, float volume) {
        if (category == SoundCategory.MASTER && volume <= 0.0f) {
            this.stopSounds();
        }
        this.sndManager.setVolume(category, volume);
    }

    public void stopSound(ISound soundIn) {
        this.sndManager.stopSound(soundIn);
    }

    public boolean isSoundPlaying(ISound sound) {
        return this.sndManager.isSoundPlaying(sound);
    }

    public void addListener(ISoundEventListener listener) {
        this.sndManager.addListener(listener);
    }

    public void removeListener(ISoundEventListener listener) {
        this.sndManager.removeListener(listener);
    }

    public void stop(String p_189520_1_, SoundCategory p_189520_2_) {
        this.sndManager.stop(p_189520_1_, p_189520_2_);
    }
}

