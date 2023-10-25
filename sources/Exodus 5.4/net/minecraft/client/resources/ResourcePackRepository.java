/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.Lists
 *  com.google.common.hash.Hashing
 *  com.google.common.io.Files
 *  com.google.common.util.concurrent.FutureCallback
 *  com.google.common.util.concurrent.Futures
 *  com.google.common.util.concurrent.ListenableFuture
 *  com.google.common.util.concurrent.SettableFuture
 *  org.apache.commons.io.FileUtils
 *  org.apache.commons.io.IOUtils
 *  org.apache.commons.io.comparator.LastModifiedFileComparator
 *  org.apache.commons.io.filefilter.IOFileFilter
 *  org.apache.commons.io.filefilter.TrueFileFilter
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.resources;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenWorking;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.FileResourcePack;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResourcePackRepository {
    private List<Entry> repositoryEntries;
    public final IResourcePack rprDefaultResourcePack;
    private IResourcePack resourcePackInstance;
    private static final FileFilter resourcePackFilter;
    private List<Entry> repositoryEntriesAll;
    private ListenableFuture<Object> field_177322_i;
    private static final Logger logger;
    private final ReentrantLock lock = new ReentrantLock();
    private final File dirServerResourcepacks;
    private final File dirResourcepacks;
    public final IMetadataSerializer rprMetadataSerializer;

    public List<Entry> getRepositoryEntriesAll() {
        return ImmutableList.copyOf(this.repositoryEntriesAll);
    }

    public List<Entry> getRepositoryEntries() {
        return ImmutableList.copyOf(this.repositoryEntries);
    }

    public ListenableFuture<Object> setResourcePackInstance(File file) {
        this.resourcePackInstance = new FileResourcePack(file);
        return Minecraft.getMinecraft().scheduleResourcesRefresh();
    }

    private List<File> getResourcePackFiles() {
        return this.dirResourcepacks.isDirectory() ? Arrays.asList(this.dirResourcepacks.listFiles(resourcePackFilter)) : Collections.emptyList();
    }

    public void func_148529_f() {
        this.lock.lock();
        if (this.field_177322_i != null) {
            this.field_177322_i.cancel(true);
        }
        this.field_177322_i = null;
        if (this.resourcePackInstance != null) {
            this.resourcePackInstance = null;
            Minecraft.getMinecraft().scheduleResourcesRefresh();
        }
        this.lock.unlock();
    }

    static {
        logger = LogManager.getLogger();
        resourcePackFilter = new FileFilter(){

            @Override
            public boolean accept(File file) {
                boolean bl;
                boolean bl2 = file.isFile() && file.getName().endsWith(".zip");
                boolean bl3 = bl = file.isDirectory() && new File(file, "pack.mcmeta").isFile();
                return bl2 || bl;
            }
        };
    }

    public File getDirResourcepacks() {
        return this.dirResourcepacks;
    }

    public IResourcePack getResourcePackInstance() {
        return this.resourcePackInstance;
    }

    public ResourcePackRepository(File file, File file2, IResourcePack iResourcePack, IMetadataSerializer iMetadataSerializer, GameSettings gameSettings) {
        this.repositoryEntriesAll = Lists.newArrayList();
        this.repositoryEntries = Lists.newArrayList();
        this.dirResourcepacks = file;
        this.dirServerResourcepacks = file2;
        this.rprDefaultResourcePack = iResourcePack;
        this.rprMetadataSerializer = iMetadataSerializer;
        this.fixDirResourcepacks();
        this.updateRepositoryEntriesAll();
        Iterator<String> iterator = gameSettings.resourcePacks.iterator();
        block0: while (iterator.hasNext()) {
            String string = iterator.next();
            for (Entry entry : this.repositoryEntriesAll) {
                if (!entry.getResourcePackName().equals(string)) continue;
                if (entry.func_183027_f() == 1 || gameSettings.field_183018_l.contains(entry.getResourcePackName())) {
                    this.repositoryEntries.add(entry);
                    continue block0;
                }
                iterator.remove();
                logger.warn("Removed selected resource pack {} because it's no longer compatible", new Object[]{entry.getResourcePackName()});
            }
        }
    }

    public void setRepositories(List<Entry> list) {
        this.repositoryEntries.clear();
        this.repositoryEntries.addAll(list);
    }

    public ListenableFuture<Object> downloadResourcePack(String string, String string2) {
        ListenableFuture<Object> listenableFuture;
        Object object;
        String string3 = string2.matches("^[a-f0-9]{40}$") ? string2 : "legacy";
        final File file = new File(this.dirServerResourcepacks, string3);
        this.lock.lock();
        this.func_148529_f();
        if (file.exists() && string2.length() == 40) {
            block4: {
                ListenableFuture<Object> listenableFuture2;
                object = Hashing.sha1().hashBytes(Files.toByteArray((File)file)).toString();
                if (!((String)object).equals(string2)) break block4;
                ListenableFuture<Object> listenableFuture3 = listenableFuture2 = this.setResourcePackInstance(file);
                this.lock.unlock();
                return listenableFuture3;
            }
            try {
                logger.warn("File " + file + " had wrong hash (expected " + string2 + ", found " + (String)object + "). Deleting it.");
                FileUtils.deleteQuietly((File)file);
            }
            catch (IOException iOException) {
                logger.warn("File " + file + " couldn't be hashed. Deleting it.", (Throwable)iOException);
                FileUtils.deleteQuietly((File)file);
            }
        }
        this.func_183028_i();
        object = new GuiScreenWorking();
        Map<String, String> map = Minecraft.getSessionInfo();
        final Minecraft minecraft = Minecraft.getMinecraft();
        Futures.getUnchecked(minecraft.addScheduledTask(new Runnable((GuiScreenWorking)object){
            private final /* synthetic */ GuiScreenWorking val$guiscreenworking;
            {
                this.val$guiscreenworking = guiScreenWorking;
            }

            @Override
            public void run() {
                minecraft.displayGuiScreen(this.val$guiscreenworking);
            }
        }));
        final SettableFuture settableFuture = SettableFuture.create();
        this.field_177322_i = HttpUtil.downloadResourcePack(file, string, map, 0x3200000, (IProgressUpdate)object, minecraft.getProxy());
        Futures.addCallback(this.field_177322_i, (FutureCallback)new FutureCallback<Object>(){

            public void onSuccess(Object object) {
                ResourcePackRepository.this.setResourcePackInstance(file);
                settableFuture.set(null);
            }

            public void onFailure(Throwable throwable) {
                settableFuture.setException(throwable);
            }
        });
        ListenableFuture<Object> listenableFuture4 = listenableFuture = this.field_177322_i;
        this.lock.unlock();
        return listenableFuture4;
    }

    public void updateRepositoryEntriesAll() {
        ArrayList arrayList = Lists.newArrayList();
        for (File object : this.getResourcePackFiles()) {
            Entry entry = new Entry(object);
            if (!this.repositoryEntriesAll.contains(entry)) {
                try {
                    entry.updateResourcePack();
                    arrayList.add(entry);
                }
                catch (Exception exception) {
                    arrayList.remove(entry);
                }
                continue;
            }
            int n = this.repositoryEntriesAll.indexOf(entry);
            if (n <= -1 || n >= this.repositoryEntriesAll.size()) continue;
            arrayList.add(this.repositoryEntriesAll.get(n));
        }
        this.repositoryEntriesAll.removeAll(arrayList);
        for (Entry entry : this.repositoryEntriesAll) {
            entry.closeResourcePack();
        }
        this.repositoryEntriesAll = arrayList;
    }

    private void func_183028_i() {
        ArrayList arrayList = Lists.newArrayList((Iterable)FileUtils.listFiles((File)this.dirServerResourcepacks, (IOFileFilter)TrueFileFilter.TRUE, null));
        Collections.sort(arrayList, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        int n = 0;
        for (File file : arrayList) {
            if (n++ < 10) continue;
            logger.info("Deleting old server resource pack " + file.getName());
            FileUtils.deleteQuietly((File)file);
        }
    }

    private void fixDirResourcepacks() {
        if (this.dirResourcepacks.exists()) {
            if (!(this.dirResourcepacks.isDirectory() || this.dirResourcepacks.delete() && this.dirResourcepacks.mkdirs())) {
                logger.warn("Unable to recreate resourcepack folder, it exists but is not a directory: " + this.dirResourcepacks);
            }
        } else if (!this.dirResourcepacks.mkdirs()) {
            logger.warn("Unable to create resourcepack folder: " + this.dirResourcepacks);
        }
    }

    public class Entry {
        private BufferedImage texturePackIcon;
        private IResourcePack reResourcePack;
        private final File resourcePackFile;
        private ResourceLocation locationTexturePackIcon;
        private PackMetadataSection rePackMetadataSection;

        public void bindTexturePackIcon(TextureManager textureManager) {
            if (this.locationTexturePackIcon == null) {
                this.locationTexturePackIcon = textureManager.getDynamicTextureLocation("texturepackicon", new DynamicTexture(this.texturePackIcon));
            }
            textureManager.bindTexture(this.locationTexturePackIcon);
        }

        public String getTexturePackDescription() {
            return this.rePackMetadataSection == null ? (Object)((Object)EnumChatFormatting.RED) + "Invalid pack.mcmeta (or missing 'pack' section)" : this.rePackMetadataSection.getPackDescription().getFormattedText();
        }

        public void closeResourcePack() {
            if (this.reResourcePack instanceof Closeable) {
                IOUtils.closeQuietly((Closeable)((Closeable)((Object)this.reResourcePack)));
            }
        }

        public IResourcePack getResourcePack() {
            return this.reResourcePack;
        }

        private Entry(File file) {
            this.resourcePackFile = file;
        }

        public boolean equals(Object object) {
            return this == object ? true : (object instanceof Entry ? this.toString().equals(object.toString()) : false);
        }

        public int hashCode() {
            return this.toString().hashCode();
        }

        public String getResourcePackName() {
            return this.reResourcePack.getPackName();
        }

        public void updateResourcePack() throws IOException {
            this.reResourcePack = this.resourcePackFile.isDirectory() ? new FolderResourcePack(this.resourcePackFile) : new FileResourcePack(this.resourcePackFile);
            this.rePackMetadataSection = (PackMetadataSection)this.reResourcePack.getPackMetadata(ResourcePackRepository.this.rprMetadataSerializer, "pack");
            try {
                this.texturePackIcon = this.reResourcePack.getPackImage();
            }
            catch (IOException iOException) {
                // empty catch block
            }
            if (this.texturePackIcon == null) {
                this.texturePackIcon = ResourcePackRepository.this.rprDefaultResourcePack.getPackImage();
            }
            this.closeResourcePack();
        }

        public String toString() {
            return String.format("%s:%s:%d", this.resourcePackFile.getName(), this.resourcePackFile.isDirectory() ? "folder" : "zip", this.resourcePackFile.lastModified());
        }

        public int func_183027_f() {
            return this.rePackMetadataSection.getPackFormat();
        }
    }
}

