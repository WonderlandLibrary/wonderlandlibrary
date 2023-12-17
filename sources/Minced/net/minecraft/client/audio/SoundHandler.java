// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.audio;

import net.minecraft.util.registry.RegistrySimple;
import net.minecraft.util.text.ITextComponent;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import net.minecraft.util.SoundCategory;
import net.minecraft.entity.player.EntityPlayer;
import java.io.Closeable;
import java.io.FileNotFoundException;
import javax.annotation.Nullable;
import org.apache.commons.io.IOUtils;
import java.lang.reflect.Type;
import java.io.Reader;
import net.minecraft.util.JsonUtils;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.io.InputStream;
import java.util.Iterator;
import net.minecraft.util.SoundEvent;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;
import java.io.IOException;
import java.util.Map;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.resources.IResourceManager;
import java.lang.reflect.ParameterizedType;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;
import net.minecraft.util.ITickable;
import net.minecraft.client.resources.IResourceManagerReloadListener;

public class SoundHandler implements IResourceManagerReloadListener, ITickable
{
    public static final Sound MISSING_SOUND;
    private static final Logger LOGGER;
    private static final Gson GSON;
    private static final ParameterizedType TYPE;
    private final SoundRegistry soundRegistry;
    private final SoundManager sndManager;
    private final IResourceManager resourceManager;
    
    public SoundHandler(final IResourceManager manager, final GameSettings gameSettingsIn) {
        this.soundRegistry = new SoundRegistry();
        this.resourceManager = manager;
        this.sndManager = new SoundManager(this, gameSettingsIn);
    }
    
    @Override
    public void onResourceManagerReload(final IResourceManager resourceManager) {
        this.soundRegistry.clearMap();
        for (final String s : resourceManager.getResourceDomains()) {
            try {
                for (final IResource iresource : resourceManager.getAllResources(new ResourceLocation(s, "sounds.json"))) {
                    try {
                        final Map<String, SoundList> map = this.getSoundMap(iresource.getInputStream());
                        for (final Map.Entry<String, SoundList> entry : map.entrySet()) {
                            this.loadSoundResource(new ResourceLocation(s, entry.getKey()), entry.getValue());
                        }
                    }
                    catch (RuntimeException runtimeexception) {
                        SoundHandler.LOGGER.warn("Invalid sounds.json", (Throwable)runtimeexception);
                    }
                }
            }
            catch (IOException ex) {}
        }
        for (final ResourceLocation resourcelocation : ((RegistrySimple<ResourceLocation, V>)this.soundRegistry).getKeys()) {
            final SoundEventAccessor soundeventaccessor = this.soundRegistry.getObject(resourcelocation);
            if (soundeventaccessor.getSubtitle() instanceof TextComponentTranslation) {
                final String s2 = ((TextComponentTranslation)soundeventaccessor.getSubtitle()).getKey();
                if (I18n.hasKey(s2)) {
                    continue;
                }
                SoundHandler.LOGGER.debug("Missing subtitle {} for event: {}", (Object)s2, (Object)resourcelocation);
            }
        }
        for (final ResourceLocation resourcelocation2 : ((RegistrySimple<ResourceLocation, V>)this.soundRegistry).getKeys()) {
            if (SoundEvent.REGISTRY.getObject(resourcelocation2) == null) {
                SoundHandler.LOGGER.debug("Not having sound event for: {}", (Object)resourcelocation2);
            }
        }
        this.sndManager.reloadSoundSystem();
    }
    
    @Nullable
    protected Map<String, SoundList> getSoundMap(final InputStream stream) {
        Map map;
        try {
            map = JsonUtils.fromJson(SoundHandler.GSON, new InputStreamReader(stream, StandardCharsets.UTF_8), SoundHandler.TYPE);
        }
        finally {
            IOUtils.closeQuietly(stream);
        }
        return (Map<String, SoundList>)map;
    }
    
    private void loadSoundResource(final ResourceLocation location, final SoundList sounds) {
        SoundEventAccessor soundeventaccessor = this.soundRegistry.getObject(location);
        final boolean flag = soundeventaccessor == null;
        if (flag || sounds.canReplaceExisting()) {
            if (!flag) {
                SoundHandler.LOGGER.debug("Replaced sound event location {}", (Object)location);
            }
            soundeventaccessor = new SoundEventAccessor(location, sounds.getSubtitle());
            this.soundRegistry.add(soundeventaccessor);
        }
        for (final Sound sound : sounds.getSounds()) {
            final ResourceLocation resourcelocation = sound.getSoundLocation();
            ISoundEventAccessor<Sound> isoundeventaccessor = null;
            switch (sound.getType()) {
                case FILE: {
                    if (!this.validateSoundResource(sound, location)) {
                        continue;
                    }
                    isoundeventaccessor = sound;
                    break;
                }
                case SOUND_EVENT: {
                    isoundeventaccessor = new ISoundEventAccessor<Sound>() {
                        @Override
                        public int getWeight() {
                            final SoundEventAccessor soundeventaccessor1 = SoundHandler.this.soundRegistry.getObject(resourcelocation);
                            return (soundeventaccessor1 == null) ? 0 : soundeventaccessor1.getWeight();
                        }
                        
                        @Override
                        public Sound cloneEntry() {
                            final SoundEventAccessor soundeventaccessor1 = SoundHandler.this.soundRegistry.getObject(resourcelocation);
                            if (soundeventaccessor1 == null) {
                                return SoundHandler.MISSING_SOUND;
                            }
                            final Sound sound1 = soundeventaccessor1.cloneEntry();
                            return new Sound(sound1.getSoundLocation().toString(), sound1.getVolume() * sound.getVolume(), sound1.getPitch() * sound.getPitch(), sound.getWeight(), Sound.Type.FILE, sound1.isStreaming() || sound.isStreaming());
                        }
                    };
                    break;
                }
                default: {
                    throw new IllegalStateException("Unknown SoundEventRegistration type: " + sound.getType());
                }
            }
            soundeventaccessor.addSound(isoundeventaccessor);
        }
    }
    
    private boolean validateSoundResource(final Sound p_184401_1_, final ResourceLocation p_184401_2_) {
        final ResourceLocation resourcelocation = p_184401_1_.getSoundAsOggLocation();
        IResource iresource = null;
        boolean flag;
        try {
            iresource = this.resourceManager.getResource(resourcelocation);
            iresource.getInputStream();
            return true;
        }
        catch (FileNotFoundException var11) {
            SoundHandler.LOGGER.warn("File {} does not exist, cannot add it to event {}", (Object)resourcelocation, (Object)p_184401_2_);
            flag = false;
        }
        catch (IOException ioexception) {
            SoundHandler.LOGGER.warn("Could not load sound file {}, cannot add it to event {}", (Object)resourcelocation, (Object)p_184401_2_, (Object)ioexception);
            flag = false;
            return flag;
        }
        finally {
            IOUtils.closeQuietly((Closeable)iresource);
        }
        return flag;
    }
    
    @Nullable
    public SoundEventAccessor getAccessor(final ResourceLocation location) {
        return this.soundRegistry.getObject(location);
    }
    
    public void playSound(final ISound sound) {
        this.sndManager.playSound(sound);
    }
    
    public void playDelayedSound(final ISound sound, final int delay) {
        this.sndManager.playDelayedSound(sound, delay);
    }
    
    public void setListener(final EntityPlayer player, final float p_147691_2_) {
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
    
    public void setSoundLevel(final SoundCategory category, final float volume) {
        if (category == SoundCategory.MASTER && volume <= 0.0f) {
            this.stopSounds();
        }
        this.sndManager.setVolume(category, volume);
    }
    
    public void stopSound(final ISound soundIn) {
        this.sndManager.stopSound(soundIn);
    }
    
    public boolean isSoundPlaying(final ISound sound) {
        return this.sndManager.isSoundPlaying(sound);
    }
    
    public void addListener(final ISoundEventListener listener) {
        this.sndManager.addListener(listener);
    }
    
    public void removeListener(final ISoundEventListener listener) {
        this.sndManager.removeListener(listener);
    }
    
    public void stop(final String p_189520_1_, final SoundCategory p_189520_2_) {
        this.sndManager.stop(p_189520_1_, p_189520_2_);
    }
    
    static {
        MISSING_SOUND = new Sound("meta:missing_sound", 1.0f, 1.0f, 1, Sound.Type.FILE, false);
        LOGGER = LogManager.getLogger();
        GSON = new GsonBuilder().registerTypeHierarchyAdapter((Class)ITextComponent.class, (Object)new ITextComponent.Serializer()).registerTypeAdapter((Type)SoundList.class, (Object)new SoundListSerializer()).create();
        TYPE = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] { String.class, SoundList.class };
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
    }
}
