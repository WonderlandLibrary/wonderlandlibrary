/*
 * Decompiled with CFR 0.150.
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
import java.util.List;
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
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SoundHandler
implements IResourceManagerReloadListener,
IUpdatePlayerListBox {
    private static final Logger logger = LogManager.getLogger();
    private static final Gson field_147699_c = new GsonBuilder().registerTypeAdapter(SoundList.class, (Object)new SoundListSerializer()).create();
    private static final ParameterizedType field_147696_d = new ParameterizedType(){
        private static final String __OBFID = "CL_00001148";

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
    public static final SoundPoolEntry missing_sound = new SoundPoolEntry(new ResourceLocation("meta:missing_sound"), 0.0, 0.0, false);
    private final SoundRegistry sndRegistry = new SoundRegistry();
    private final SoundManager sndManager;
    private final IResourceManager mcResourceManager;
    private static final String __OBFID = "CL_00001147";

    public SoundHandler(IResourceManager manager, GameSettings p_i45122_2_) {
        this.mcResourceManager = manager;
        this.sndManager = new SoundManager(this, p_i45122_2_);
    }

    @Override
    public void onResourceManagerReload(IResourceManager p_110549_1_) {
        this.sndManager.reloadSoundSystem();
        this.sndRegistry.clearMap();
        for (String var3 : p_110549_1_.getResourceDomains()) {
            try {
                List var4 = p_110549_1_.getAllResources(new ResourceLocation(var3, "sounds.json"));
                for (IResource var6 : var4) {
                    try {
                        Map var7 = this.getSoundMap(var6.getInputStream());
                        for (Map.Entry var9 : var7.entrySet()) {
                            this.loadSoundResource(new ResourceLocation(var3, (String)var9.getKey()), (SoundList)var9.getValue());
                        }
                    }
                    catch (RuntimeException var10) {
                        logger.warn("Invalid sounds.json", (Throwable)var10);
                    }
                }
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }

    protected Map getSoundMap(InputStream p_175085_1_) {
        Map var2;
        try {
            var2 = (Map)field_147699_c.fromJson((Reader)new InputStreamReader(p_175085_1_), (Type)field_147696_d);
        }
        finally {
            IOUtils.closeQuietly((InputStream)p_175085_1_);
        }
        return var2;
    }

    private void loadSoundResource(ResourceLocation p_147693_1_, SoundList p_147693_2_) {
        SoundEventAccessorComposite var3;
        boolean var4;
        boolean bl = var4 = !this.sndRegistry.containsKey(p_147693_1_);
        if (!var4 && !p_147693_2_.canReplaceExisting()) {
            var3 = (SoundEventAccessorComposite)this.sndRegistry.getObject(p_147693_1_);
        } else {
            if (!var4) {
                logger.debug("Replaced sound event location {}", new Object[]{p_147693_1_});
            }
            var3 = new SoundEventAccessorComposite(p_147693_1_, 1.0, 1.0, p_147693_2_.getSoundCategory());
            this.sndRegistry.registerSound(var3);
        }
        block10: for (SoundList.SoundEntry var6 : p_147693_2_.getSoundList()) {
            ISoundEventAccessor var10;
            String var7 = var6.getSoundEntryName();
            ResourceLocation var8 = new ResourceLocation(var7);
            String var9 = var7.contains(":") ? var8.getResourceDomain() : p_147693_1_.getResourceDomain();
            switch (SwitchType.field_148765_a[var6.getSoundEntryType().ordinal()]) {
                case 1: {
                    ResourceLocation var11 = new ResourceLocation(var9, "sounds/" + var8.getResourcePath() + ".ogg");
                    InputStream var12 = null;
                    try {
                        var12 = this.mcResourceManager.getResource(var11).getInputStream();
                    }
                    catch (FileNotFoundException var18) {
                        logger.warn("File {} does not exist, cannot add it to event {}", new Object[]{var11, p_147693_1_});
                        IOUtils.closeQuietly((InputStream)var12);
                        continue block10;
                    }
                    catch (IOException var19) {
                        try {
                            logger.warn("Could not load sound file " + var11 + ", cannot add it to event " + p_147693_1_, (Throwable)var19);
                        }
                        catch (Throwable throwable) {
                            IOUtils.closeQuietly(var12);
                            throw throwable;
                        }
                        IOUtils.closeQuietly((InputStream)var12);
                        continue block10;
                    }
                    IOUtils.closeQuietly((InputStream)var12);
                    var10 = new SoundEventAccessor(new SoundPoolEntry(var11, var6.getSoundEntryPitch(), var6.getSoundEntryVolume(), var6.isStreaming()), var6.getSoundEntryWeight());
                    break;
                }
                case 2: {
                    var10 = new ISoundEventAccessor(var9, var6){
                        final ResourceLocation field_148726_a;
                        private static final String __OBFID = "CL_00001149";
                        {
                            this.field_148726_a = new ResourceLocation(string, soundEntry.getSoundEntryName());
                        }

                        @Override
                        public int getWeight() {
                            SoundEventAccessorComposite var1 = (SoundEventAccessorComposite)SoundHandler.this.sndRegistry.getObject(this.field_148726_a);
                            return var1 == null ? 0 : var1.getWeight();
                        }

                        public SoundPoolEntry getEntry() {
                            SoundEventAccessorComposite var1 = (SoundEventAccessorComposite)SoundHandler.this.sndRegistry.getObject(this.field_148726_a);
                            return (SoundPoolEntry)(var1 == null ? missing_sound : var1.cloneEntry());
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
            var3.addSoundToEventPool(var10);
        }
    }

    public SoundEventAccessorComposite getSound(ResourceLocation p_147680_1_) {
        return (SoundEventAccessorComposite)this.sndRegistry.getObject(p_147680_1_);
    }

    public void playSound(ISound p_147682_1_) {
        this.sndManager.playSound(p_147682_1_);
    }

    public void playDelayedSound(ISound p_147681_1_, int p_147681_2_) {
        this.sndManager.playDelayedSound(p_147681_1_, p_147681_2_);
    }

    public void setListener(EntityPlayer p_147691_1_, float p_147691_2_) {
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

    public void setSoundLevel(SoundCategory p_147684_1_, float volume) {
        if (p_147684_1_ == SoundCategory.MASTER && volume <= 0.0f) {
            this.stopSounds();
        }
        this.sndManager.setSoundCategoryVolume(p_147684_1_, volume);
    }

    public void stopSound(ISound p_147683_1_) {
        this.sndManager.stopSound(p_147683_1_);
    }

    public SoundEventAccessorComposite getRandomSoundFromCategories(SoundCategory ... p_147686_1_) {
        ArrayList var2 = Lists.newArrayList();
        for (ResourceLocation var4 : this.sndRegistry.getKeys()) {
            SoundEventAccessorComposite var5 = (SoundEventAccessorComposite)this.sndRegistry.getObject(var4);
            if (!ArrayUtils.contains((Object[])p_147686_1_, (Object)((Object)var5.getSoundCategory()))) continue;
            var2.add(var5);
        }
        if (var2.isEmpty()) {
            return null;
        }
        return (SoundEventAccessorComposite)var2.get(new Random().nextInt(var2.size()));
    }

    public boolean isSoundPlaying(ISound p_147692_1_) {
        return this.sndManager.isSoundPlaying(p_147692_1_);
    }

    static final class SwitchType {
        static final int[] field_148765_a = new int[SoundList.SoundEntry.Type.values().length];
        private static final String __OBFID = "CL_00001150";

        static {
            try {
                SwitchType.field_148765_a[SoundList.SoundEntry.Type.FILE.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchType.field_148765_a[SoundList.SoundEntry.Type.SOUND_EVENT.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
        }

        SwitchType() {
        }
    }
}

