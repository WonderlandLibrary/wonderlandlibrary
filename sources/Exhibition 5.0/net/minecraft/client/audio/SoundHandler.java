// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.audio;

import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import java.util.ArrayList;
import java.util.Random;
import org.apache.commons.lang3.ArrayUtils;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import java.io.FileNotFoundException;
import org.apache.commons.io.IOUtils;
import java.lang.reflect.Type;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.List;
import java.util.Iterator;
import java.io.IOException;
import java.util.Map;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.resources.IResourceManager;
import java.lang.reflect.ParameterizedType;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.client.resources.IResourceManagerReloadListener;

public class SoundHandler implements IResourceManagerReloadListener, IUpdatePlayerListBox
{
    private static final Logger logger;
    private static final Gson field_147699_c;
    private static final ParameterizedType field_147696_d;
    public static final SoundPoolEntry missing_sound;
    private final SoundRegistry sndRegistry;
    private final SoundManager sndManager;
    private final IResourceManager mcResourceManager;
    private static final String __OBFID = "CL_00001147";
    
    public SoundHandler(final IResourceManager manager, final GameSettings p_i45122_2_) {
        this.sndRegistry = new SoundRegistry();
        this.mcResourceManager = manager;
        this.sndManager = new SoundManager(this, p_i45122_2_);
    }
    
    @Override
    public void onResourceManagerReload(final IResourceManager p_110549_1_) {
        this.sndManager.reloadSoundSystem();
        this.sndRegistry.clearMap();
        for (final String var3 : p_110549_1_.getResourceDomains()) {
            try {
                final List var4 = p_110549_1_.getAllResources(new ResourceLocation(var3, "sounds.json"));
                for (final IResource var6 : var4) {
                    try {
                        final Map var7 = this.getSoundMap(var6.getInputStream());
                        for (final Map.Entry var9 : var7.entrySet()) {
                            this.loadSoundResource(new ResourceLocation(var3, var9.getKey()), var9.getValue());
                        }
                    }
                    catch (RuntimeException var10) {
                        SoundHandler.logger.warn("Invalid sounds.json", (Throwable)var10);
                    }
                }
            }
            catch (IOException ex) {}
        }
    }
    
    protected Map getSoundMap(final InputStream p_175085_1_) {
        Map var2;
        try {
            var2 = (Map)SoundHandler.field_147699_c.fromJson((Reader)new InputStreamReader(p_175085_1_), (Type)SoundHandler.field_147696_d);
        }
        finally {
            IOUtils.closeQuietly(p_175085_1_);
        }
        return var2;
    }
    
    private void loadSoundResource(final ResourceLocation p_147693_1_, final SoundList p_147693_2_) {
        final boolean var4 = !this.sndRegistry.containsKey(p_147693_1_);
        SoundEventAccessorComposite var5;
        if (!var4 && !p_147693_2_.canReplaceExisting()) {
            var5 = (SoundEventAccessorComposite)this.sndRegistry.getObject(p_147693_1_);
        }
        else {
            if (!var4) {
                SoundHandler.logger.debug("Replaced sound event location {}", new Object[] { p_147693_1_ });
            }
            var5 = new SoundEventAccessorComposite(p_147693_1_, 1.0, 1.0, p_147693_2_.getSoundCategory());
            this.sndRegistry.registerSound(var5);
        }
        for (final SoundList.SoundEntry var7 : p_147693_2_.getSoundList()) {
            final String var8 = var7.getSoundEntryName();
            final ResourceLocation var9 = new ResourceLocation(var8);
            final String var10 = var8.contains(":") ? var9.getResourceDomain() : p_147693_1_.getResourceDomain();
            Object var14 = null;
            switch (SwitchType.field_148765_a[var7.getSoundEntryType().ordinal()]) {
                case 1: {
                    final ResourceLocation var11 = new ResourceLocation(var10, "sounds/" + var9.getResourcePath() + ".ogg");
                    InputStream var12 = null;
                    try {
                        var12 = this.mcResourceManager.getResource(var11).getInputStream();
                    }
                    catch (FileNotFoundException var15) {
                        SoundHandler.logger.warn("File {} does not exist, cannot add it to event {}", new Object[] { var11, p_147693_1_ });
                    }
                    catch (IOException var13) {
                        SoundHandler.logger.warn("Could not load sound file " + var11 + ", cannot add it to event " + p_147693_1_, (Throwable)var13);
                    }
                    finally {
                        IOUtils.closeQuietly(var12);
                    }
                    var14 = new SoundEventAccessor(new SoundPoolEntry(var11, var7.getSoundEntryPitch(), var7.getSoundEntryVolume(), var7.isStreaming()), var7.getSoundEntryWeight());
                    break;
                }
                case 2: {
                    var14 = new ISoundEventAccessor() {
                        final ResourceLocation field_148726_a = new ResourceLocation(var10, var7.getSoundEntryName());
                        private static final String __OBFID = "CL_00001149";
                        
                        @Override
                        public int getWeight() {
                            final SoundEventAccessorComposite var1 = (SoundEventAccessorComposite)SoundHandler.this.sndRegistry.getObject(this.field_148726_a);
                            return (var1 == null) ? 0 : var1.getWeight();
                        }
                        
                        public SoundPoolEntry getEntry() {
                            final SoundEventAccessorComposite var1 = (SoundEventAccessorComposite)SoundHandler.this.sndRegistry.getObject(this.field_148726_a);
                            return (var1 == null) ? SoundHandler.missing_sound : var1.cloneEntry();
                        }
                        
                        @Override
                        public Object cloneEntry() {
                            return this.getEntry();
                        }
                    };
                    break;
                }
                default: {
                    throw new IllegalStateException("IN YOU FACE");
                }
            }
            var5.addSoundToEventPool((ISoundEventAccessor)var14);
        }
    }
    
    public SoundEventAccessorComposite getSound(final ResourceLocation p_147680_1_) {
        return (SoundEventAccessorComposite)this.sndRegistry.getObject(p_147680_1_);
    }
    
    public void playSound(final ISound p_147682_1_) {
        this.sndManager.playSound(p_147682_1_);
    }
    
    public void playDelayedSound(final ISound p_147681_1_, final int p_147681_2_) {
        this.sndManager.playDelayedSound(p_147681_1_, p_147681_2_);
    }
    
    public void setListener(final EntityPlayer p_147691_1_, final float p_147691_2_) {
        this.sndManager.setListener(p_147691_1_, p_147691_2_);
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
    
    public void setSoundLevel(final SoundCategory p_147684_1_, final float volume) {
        if (p_147684_1_ == SoundCategory.MASTER && volume <= 0.0f) {
            this.stopSounds();
        }
        this.sndManager.setSoundCategoryVolume(p_147684_1_, volume);
    }
    
    public void stopSound(final ISound p_147683_1_) {
        this.sndManager.stopSound(p_147683_1_);
    }
    
    public SoundEventAccessorComposite getRandomSoundFromCategories(final SoundCategory... p_147686_1_) {
        final ArrayList var2 = Lists.newArrayList();
        for (final ResourceLocation var4 : this.sndRegistry.getKeys()) {
            final SoundEventAccessorComposite var5 = (SoundEventAccessorComposite)this.sndRegistry.getObject(var4);
            if (ArrayUtils.contains((Object[])p_147686_1_, (Object)var5.getSoundCategory())) {
                var2.add(var5);
            }
        }
        if (var2.isEmpty()) {
            return null;
        }
        return var2.get(new Random().nextInt(var2.size()));
    }
    
    public boolean isSoundPlaying(final ISound p_147692_1_) {
        return this.sndManager.isSoundPlaying(p_147692_1_);
    }
    
    static {
        logger = LogManager.getLogger();
        field_147699_c = new GsonBuilder().registerTypeAdapter((Type)SoundList.class, (Object)new SoundListSerializer()).create();
        field_147696_d = new ParameterizedType() {
            private static final String __OBFID = "CL_00001148";
            
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
        missing_sound = new SoundPoolEntry(new ResourceLocation("meta:missing_sound"), 0.0, 0.0, false);
    }
    
    static final class SwitchType
    {
        static final int[] field_148765_a;
        private static final String __OBFID = "CL_00001150";
        
        static {
            field_148765_a = new int[SoundList.SoundEntry.Type.values().length];
            try {
                SwitchType.field_148765_a[SoundList.SoundEntry.Type.FILE.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchType.field_148765_a[SoundList.SoundEntry.Type.SOUND_EVENT.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
        }
    }
}
