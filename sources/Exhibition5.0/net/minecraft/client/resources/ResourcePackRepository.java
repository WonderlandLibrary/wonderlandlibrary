// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.resources;

import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.io.IOUtils;
import java.io.Closeable;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import java.awt.image.BufferedImage;
import net.minecraft.client.resources.data.PackMetadataSection;
import org.apache.logging.log4j.LogManager;
import java.util.Map;
import com.google.common.util.concurrent.FutureCallback;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.HttpUtil;
import com.google.common.util.concurrent.SettableFuture;
import java.util.concurrent.Future;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenWorking;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import com.google.common.io.Files;
import com.google.common.hash.Hashing;
import com.google.common.util.concurrent.Futures;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Arrays;
import java.util.Iterator;
import com.google.common.collect.Lists;
import net.minecraft.client.settings.GameSettings;
import java.util.List;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.client.resources.data.IMetadataSerializer;
import java.io.File;
import java.io.FileFilter;
import org.apache.logging.log4j.Logger;

public class ResourcePackRepository
{
    private static final Logger field_177320_c;
    private static final FileFilter resourcePackFilter;
    private final File dirResourcepacks;
    public final IResourcePack rprDefaultResourcePack;
    private final File field_148534_e;
    public final IMetadataSerializer rprMetadataSerializer;
    private IResourcePack field_148532_f;
    private final ReentrantLock field_177321_h;
    private ListenableFuture field_177322_i;
    private List repositoryEntriesAll;
    private List repositoryEntries;
    private static final String __OBFID = "CL_00001087";
    
    public ResourcePackRepository(final File p_i45101_1_, final File p_i45101_2_, final IResourcePack p_i45101_3_, final IMetadataSerializer p_i45101_4_, final GameSettings p_i45101_5_) {
        this.field_177321_h = new ReentrantLock();
        this.repositoryEntriesAll = Lists.newArrayList();
        this.repositoryEntries = Lists.newArrayList();
        this.dirResourcepacks = p_i45101_1_;
        this.field_148534_e = p_i45101_2_;
        this.rprDefaultResourcePack = p_i45101_3_;
        this.rprMetadataSerializer = p_i45101_4_;
        this.fixDirResourcepacks();
        this.updateRepositoryEntriesAll();
        for (final String var7 : p_i45101_5_.resourcePacks) {
            for (final Entry var9 : this.repositoryEntriesAll) {
                if (var9.getResourcePackName().equals(var7)) {
                    this.repositoryEntries.add(var9);
                    break;
                }
            }
        }
    }
    
    private void fixDirResourcepacks() {
        if (!this.dirResourcepacks.isDirectory() && (!this.dirResourcepacks.delete() || !this.dirResourcepacks.mkdirs())) {
            ResourcePackRepository.field_177320_c.debug("Unable to create resourcepack folder: " + this.dirResourcepacks);
        }
    }
    
    private List getResourcePackFiles() {
        return this.dirResourcepacks.isDirectory() ? Arrays.asList(this.dirResourcepacks.listFiles(ResourcePackRepository.resourcePackFilter)) : Collections.emptyList();
    }
    
    public void updateRepositoryEntriesAll() {
        final ArrayList var1 = Lists.newArrayList();
        for (final File var3 : this.getResourcePackFiles()) {
            final Entry var4 = new Entry(var3, null);
            if (!this.repositoryEntriesAll.contains(var4)) {
                try {
                    var4.updateResourcePack();
                    var1.add(var4);
                }
                catch (Exception var7) {
                    var1.remove(var4);
                }
            }
            else {
                final int var5 = this.repositoryEntriesAll.indexOf(var4);
                if (var5 <= -1 || var5 >= this.repositoryEntriesAll.size()) {
                    continue;
                }
                var1.add(this.repositoryEntriesAll.get(var5));
            }
        }
        this.repositoryEntriesAll.removeAll(var1);
        for (final Entry var6 : this.repositoryEntriesAll) {
            var6.closeResourcePack();
        }
        this.repositoryEntriesAll = var1;
    }
    
    public List getRepositoryEntriesAll() {
        return (List)ImmutableList.copyOf((Collection)this.repositoryEntriesAll);
    }
    
    public List getRepositoryEntries() {
        return (List)ImmutableList.copyOf((Collection)this.repositoryEntries);
    }
    
    public void func_148527_a(final List p_148527_1_) {
        this.repositoryEntries.clear();
        this.repositoryEntries.addAll(p_148527_1_);
    }
    
    public File getDirResourcepacks() {
        return this.dirResourcepacks;
    }
    
    public ListenableFuture func_180601_a(final String p_180601_1_, final String p_180601_2_) {
        String var3;
        if (p_180601_2_.matches("^[a-f0-9]{40}$")) {
            var3 = p_180601_2_;
        }
        else {
            var3 = p_180601_1_.substring(p_180601_1_.lastIndexOf("/") + 1);
            if (var3.contains("?")) {
                var3 = var3.substring(0, var3.indexOf("?"));
            }
            if (!var3.endsWith(".zip")) {
                return Futures.immediateFailedFuture((Throwable)new IllegalArgumentException("Invalid filename; must end in .zip"));
            }
            var3 = "legacy_" + var3.replaceAll("\\W", "");
        }
        final File var4 = new File(this.field_148534_e, var3);
        this.field_177321_h.lock();
        ListenableFuture var12;
        try {
            this.func_148529_f();
            if (var4.exists() && p_180601_2_.length() == 40) {
                try {
                    final String var5 = Hashing.sha1().hashBytes(Files.toByteArray(var4)).toString();
                    if (var5.equals(p_180601_2_)) {
                        final ListenableFuture var6 = this.func_177319_a(var4);
                        return var6;
                    }
                    ResourcePackRepository.field_177320_c.warn("File " + var4 + " had wrong hash (expected " + p_180601_2_ + ", found " + var5 + "). Deleting it.");
                    FileUtils.deleteQuietly(var4);
                }
                catch (IOException var7) {
                    ResourcePackRepository.field_177320_c.warn("File " + var4 + " couldn't be hashed. Deleting it.", (Throwable)var7);
                    FileUtils.deleteQuietly(var4);
                }
            }
            final GuiScreenWorking var8 = new GuiScreenWorking();
            final Map var9 = Minecraft.func_175596_ai();
            final Minecraft var10 = Minecraft.getMinecraft();
            Futures.getUnchecked((Future)var10.addScheduledTask(new Runnable() {
                private static final String __OBFID = "CL_00001089";
                
                @Override
                public void run() {
                    var10.displayGuiScreen(var8);
                }
            }));
            final SettableFuture var11 = SettableFuture.create();
            Futures.addCallback(this.field_177322_i = HttpUtil.func_180192_a(var4, p_180601_1_, var9, 52428800, var8, var10.getProxy()), (FutureCallback)new FutureCallback() {
                private static final String __OBFID = "CL_00002394";
                
                public void onSuccess(final Object p_onSuccess_1_) {
                    ResourcePackRepository.this.func_177319_a(var4);
                    var11.set((Object)null);
                }
                
                public void onFailure(final Throwable p_onFailure_1_) {
                    var11.setException(p_onFailure_1_);
                }
            });
            var12 = this.field_177322_i;
        }
        finally {
            this.field_177321_h.unlock();
        }
        return var12;
    }
    
    public ListenableFuture func_177319_a(final File p_177319_1_) {
        this.field_148532_f = new FileResourcePack(p_177319_1_);
        return Minecraft.getMinecraft().func_175603_A();
    }
    
    public IResourcePack getResourcePackInstance() {
        return this.field_148532_f;
    }
    
    public void func_148529_f() {
        this.field_177321_h.lock();
        try {
            if (this.field_177322_i != null) {
                this.field_177322_i.cancel(true);
            }
            this.field_177322_i = null;
            this.field_148532_f = null;
        }
        finally {
            this.field_177321_h.unlock();
        }
    }
    
    static {
        field_177320_c = LogManager.getLogger();
        resourcePackFilter = new FileFilter() {
            private static final String __OBFID = "CL_00001088";
            
            @Override
            public boolean accept(final File p_accept_1_) {
                final boolean var2 = p_accept_1_.isFile() && p_accept_1_.getName().endsWith(".zip");
                final boolean var3 = p_accept_1_.isDirectory() && new File(p_accept_1_, "pack.mcmeta").isFile();
                return var2 || var3;
            }
        };
    }
    
    public class Entry
    {
        private final File resourcePackFile;
        private IResourcePack reResourcePack;
        private PackMetadataSection rePackMetadataSection;
        private BufferedImage texturePackIcon;
        private ResourceLocation locationTexturePackIcon;
        private static final String __OBFID = "CL_00001090";
        
        private Entry(final File p_i1295_2_) {
            this.resourcePackFile = p_i1295_2_;
        }
        
        public void updateResourcePack() throws IOException {
            this.reResourcePack = (this.resourcePackFile.isDirectory() ? new FolderResourcePack(this.resourcePackFile) : new FileResourcePack(this.resourcePackFile));
            this.rePackMetadataSection = (PackMetadataSection)this.reResourcePack.getPackMetadata(ResourcePackRepository.this.rprMetadataSerializer, "pack");
            try {
                this.texturePackIcon = this.reResourcePack.getPackImage();
            }
            catch (IOException ex) {}
            if (this.texturePackIcon == null) {
                this.texturePackIcon = ResourcePackRepository.this.rprDefaultResourcePack.getPackImage();
            }
            this.closeResourcePack();
        }
        
        public void bindTexturePackIcon(final TextureManager p_110518_1_) {
            if (this.locationTexturePackIcon == null) {
                this.locationTexturePackIcon = p_110518_1_.getDynamicTextureLocation("texturepackicon", new DynamicTexture(this.texturePackIcon));
            }
            p_110518_1_.bindTexture(this.locationTexturePackIcon);
        }
        
        public void closeResourcePack() {
            if (this.reResourcePack instanceof Closeable) {
                IOUtils.closeQuietly((Closeable)this.reResourcePack);
            }
        }
        
        public IResourcePack getResourcePack() {
            return this.reResourcePack;
        }
        
        public String getResourcePackName() {
            return this.reResourcePack.getPackName();
        }
        
        public String getTexturePackDescription() {
            return (this.rePackMetadataSection == null) ? (EnumChatFormatting.RED + "Invalid pack.mcmeta (or missing 'pack' section)") : this.rePackMetadataSection.func_152805_a().getFormattedText();
        }
        
        @Override
        public boolean equals(final Object p_equals_1_) {
            return this == p_equals_1_ || (p_equals_1_ instanceof Entry && this.toString().equals(p_equals_1_.toString()));
        }
        
        @Override
        public int hashCode() {
            return this.toString().hashCode();
        }
        
        @Override
        public String toString() {
            return String.format("%s:%s:%d", this.resourcePackFile.getName(), this.resourcePackFile.isDirectory() ? "folder" : "zip", this.resourcePackFile.lastModified());
        }
        
        Entry(final ResourcePackRepository this$0, final File p_i1296_2_, final Object p_i1296_3_) {
            this(this$0, p_i1296_2_);
        }
    }
}
