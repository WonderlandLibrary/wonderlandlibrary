/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.resources;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenWorking;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.FileResourcePack;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.LegacyV2Adapter;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResourcePackRepository {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final FileFilter RESOURCE_PACK_FILTER = new FileFilter(){

        @Override
        public boolean accept(File p_accept_1_) {
            boolean flag = p_accept_1_.isFile() && p_accept_1_.getName().endsWith(".zip");
            boolean flag1 = p_accept_1_.isDirectory() && new File(p_accept_1_, "pack.mcmeta").isFile();
            return flag || flag1;
        }
    };
    private static final Pattern SHA1 = Pattern.compile("^[a-fA-F0-9]{40}$");
    private static final ResourceLocation field_191400_f = new ResourceLocation("textures/misc/unknown_pack.png");
    private final File dirResourcepacks;
    public final IResourcePack rprDefaultResourcePack;
    private final File dirServerResourcepacks;
    public final MetadataSerializer rprMetadataSerializer;
    private IResourcePack resourcePackInstance;
    private final ReentrantLock lock = new ReentrantLock();
    private ListenableFuture<Object> downloadingPacks;
    private List<Entry> repositoryEntriesAll = Lists.newArrayList();
    public final List<Entry> repositoryEntries = Lists.newArrayList();

    public ResourcePackRepository(File dirResourcepacksIn, File dirServerResourcepacksIn, IResourcePack rprDefaultResourcePackIn, MetadataSerializer rprMetadataSerializerIn, GameSettings settings) {
        this.dirResourcepacks = dirResourcepacksIn;
        this.dirServerResourcepacks = dirServerResourcepacksIn;
        this.rprDefaultResourcePack = rprDefaultResourcePackIn;
        this.rprMetadataSerializer = rprMetadataSerializerIn;
        this.fixDirResourcepacks();
        this.updateRepositoryEntriesAll();
        Iterator<String> iterator = settings.resourcePacks.iterator();
        block0: while (iterator.hasNext()) {
            String s = iterator.next();
            for (Entry resourcepackrepository$entry : this.repositoryEntriesAll) {
                if (!resourcepackrepository$entry.getResourcePackName().equals(s)) continue;
                if (resourcepackrepository$entry.getPackFormat() == 3 || settings.incompatibleResourcePacks.contains(resourcepackrepository$entry.getResourcePackName())) {
                    this.repositoryEntries.add(resourcepackrepository$entry);
                    continue block0;
                }
                iterator.remove();
                LOGGER.warn("Removed selected resource pack {} because it's no longer compatible", resourcepackrepository$entry.getResourcePackName());
            }
        }
    }

    public static Map<String, String> getDownloadHeaders() {
        HashMap<String, String> map = Maps.newHashMap();
        map.put("X-Minecraft-Username", Minecraft.getMinecraft().getSession().getUsername());
        map.put("X-Minecraft-UUID", Minecraft.getMinecraft().getSession().getPlayerID());
        map.put("X-Minecraft-Version", "1.12.2");
        return map;
    }

    private void fixDirResourcepacks() {
        if (this.dirResourcepacks.exists()) {
            if (!(this.dirResourcepacks.isDirectory() || this.dirResourcepacks.delete() && this.dirResourcepacks.mkdirs())) {
                LOGGER.warn("Unable to recreate resourcepack folder, it exists but is not a directory: {}", this.dirResourcepacks);
            }
        } else if (!this.dirResourcepacks.mkdirs()) {
            LOGGER.warn("Unable to create resourcepack folder: {}", this.dirResourcepacks);
        }
    }

    private List<File> getResourcePackFiles() {
        return this.dirResourcepacks.isDirectory() ? Arrays.asList(this.dirResourcepacks.listFiles(RESOURCE_PACK_FILTER)) : Collections.emptyList();
    }

    private IResourcePack func_191399_b(File p_191399_1_) {
        AbstractResourcePack iresourcepack = p_191399_1_.isDirectory() ? new FolderResourcePack(p_191399_1_) : new FileResourcePack(p_191399_1_);
        try {
            PackMetadataSection packmetadatasection = (PackMetadataSection)iresourcepack.getPackMetadata(this.rprMetadataSerializer, "pack");
            if (packmetadatasection != null && packmetadatasection.getPackFormat() == 2) {
                return new LegacyV2Adapter(iresourcepack);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return iresourcepack;
    }

    public void updateRepositoryEntriesAll() {
        ArrayList<Entry> list = Lists.newArrayList();
        for (File file1 : this.getResourcePackFiles()) {
            Entry resourcepackrepository$entry = new Entry(file1);
            if (this.repositoryEntriesAll.contains(resourcepackrepository$entry)) {
                int i = this.repositoryEntriesAll.indexOf(resourcepackrepository$entry);
                if (i <= -1 || i >= this.repositoryEntriesAll.size()) continue;
                list.add(this.repositoryEntriesAll.get(i));
                continue;
            }
            try {
                resourcepackrepository$entry.updateResourcePack();
                list.add(resourcepackrepository$entry);
            }
            catch (Exception var61) {
                list.remove(resourcepackrepository$entry);
            }
        }
        this.repositoryEntriesAll.removeAll(list);
        for (Entry resourcepackrepository$entry1 : this.repositoryEntriesAll) {
            resourcepackrepository$entry1.closeResourcePack();
        }
        this.repositoryEntriesAll = list;
    }

    @Nullable
    public Entry getResourcePackEntry() {
        if (this.resourcePackInstance != null) {
            Entry resourcepackrepository$entry = new Entry(this.resourcePackInstance);
            try {
                resourcepackrepository$entry.updateResourcePack();
                return resourcepackrepository$entry;
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        return null;
    }

    public List<Entry> getRepositoryEntriesAll() {
        return ImmutableList.copyOf(this.repositoryEntriesAll);
    }

    public List<Entry> getRepositoryEntries() {
        return ImmutableList.copyOf(this.repositoryEntries);
    }

    public void setRepositories(List<Entry> repositories) {
        this.repositoryEntries.clear();
        this.repositoryEntries.addAll(repositories);
    }

    public File getDirResourcepacks() {
        return this.dirResourcepacks;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public ListenableFuture<Object> downloadResourcePack(String url, String hash) {
        String s = DigestUtils.sha1Hex(url);
        final String s1 = SHA1.matcher(hash).matches() ? hash : "";
        final File file1 = new File(this.dirServerResourcepacks, s);
        this.lock.lock();
        try {
            ListenableFuture<Object> listenablefuture;
            ListenableFuture<Object> listenablefuture1;
            this.clearResourcePack();
            if (file1.exists()) {
                if (this.checkHash(s1, file1)) {
                    ListenableFuture<Object> listenablefuture2;
                    ListenableFuture<Object> listenablefuture3;
                    ListenableFuture<Object> listenableFuture = listenablefuture3 = (listenablefuture2 = this.setResourcePackInstance(file1));
                    return listenableFuture;
                }
                LOGGER.warn("Deleting file {}", file1);
                FileUtils.deleteQuietly(file1);
            }
            this.deleteOldServerResourcesPacks();
            final GuiScreenWorking guiscreenworking = new GuiScreenWorking();
            Map<String, String> map = ResourcePackRepository.getDownloadHeaders();
            final Minecraft minecraft = Minecraft.getMinecraft();
            Futures.getUnchecked(minecraft.addScheduledTask(new Runnable(){

                @Override
                public void run() {
                    minecraft.displayGuiScreen(guiscreenworking);
                }
            }));
            final SettableFuture settablefuture = SettableFuture.create();
            this.downloadingPacks = HttpUtil.downloadResourcePack(file1, url, map, 0x3200000, guiscreenworking, minecraft.getProxy());
            Futures.addCallback(this.downloadingPacks, new FutureCallback<Object>(){

                @Override
                public void onSuccess(@Nullable Object p_onSuccess_1_) {
                    if (ResourcePackRepository.this.checkHash(s1, file1)) {
                        ResourcePackRepository.this.setResourcePackInstance(file1);
                        settablefuture.set(null);
                    } else {
                        LOGGER.warn("Deleting file {}", file1);
                        FileUtils.deleteQuietly(file1);
                    }
                }

                @Override
                public void onFailure(Throwable p_onFailure_1_) {
                    FileUtils.deleteQuietly(file1);
                    settablefuture.setException(p_onFailure_1_);
                }
            });
            ListenableFuture<Object> listenableFuture = listenablefuture1 = (listenablefuture = this.downloadingPacks);
            return listenableFuture;
        }
        finally {
            this.lock.unlock();
        }
    }

    private boolean checkHash(String p_190113_1_, File p_190113_2_) {
        try {
            String s = DigestUtils.sha1Hex(new FileInputStream(p_190113_2_));
            if (p_190113_1_.isEmpty()) {
                LOGGER.info("Found file {} without verification hash", p_190113_2_);
                return true;
            }
            if (s.toLowerCase(Locale.ROOT).equals(p_190113_1_.toLowerCase(Locale.ROOT))) {
                LOGGER.info("Found file {} matching requested hash {}", p_190113_2_, p_190113_1_);
                return true;
            }
            LOGGER.warn("File {} had wrong hash (expected {}, found {}).", p_190113_2_, p_190113_1_, s);
        }
        catch (IOException ioexception1) {
            LOGGER.warn("File {} couldn't be hashed.", p_190113_2_, ioexception1);
        }
        return false;
    }

    private boolean validatePack(File p_190112_1_) {
        Entry resourcepackrepository$entry = new Entry(p_190112_1_);
        try {
            resourcepackrepository$entry.updateResourcePack();
            return true;
        }
        catch (Exception exception) {
            LOGGER.warn("Server resourcepack is invalid, ignoring it", (Throwable)exception);
            return false;
        }
    }

    private void deleteOldServerResourcesPacks() {
        try {
            ArrayList<File> list = Lists.newArrayList(FileUtils.listFiles(this.dirServerResourcepacks, TrueFileFilter.TRUE, null));
            Collections.sort(list, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            int i = 0;
            for (File file1 : list) {
                if (i++ < 10) continue;
                LOGGER.info("Deleting old server resource pack {}", file1.getName());
                FileUtils.deleteQuietly(file1);
            }
        }
        catch (IllegalArgumentException illegalargumentexception1) {
            LOGGER.error("Error while deleting old server resource pack : {}", illegalargumentexception1.getMessage());
        }
    }

    public ListenableFuture<Object> setResourcePackInstance(File resourceFile) {
        if (!this.validatePack(resourceFile)) {
            return Futures.immediateFailedFuture(new RuntimeException("Invalid resourcepack"));
        }
        this.resourcePackInstance = new FileResourcePack(resourceFile);
        return Minecraft.getMinecraft().scheduleResourcesRefresh();
    }

    @Nullable
    public IResourcePack getResourcePackInstance() {
        return this.resourcePackInstance;
    }

    public void clearResourcePack() {
        this.lock.lock();
        try {
            if (this.downloadingPacks != null) {
                this.downloadingPacks.cancel(true);
            }
            this.downloadingPacks = null;
            if (this.resourcePackInstance != null) {
                this.resourcePackInstance = null;
                Minecraft.getMinecraft().scheduleResourcesRefresh();
            }
        }
        finally {
            this.lock.unlock();
        }
    }

    public class Entry {
        private final IResourcePack reResourcePack;
        private PackMetadataSection rePackMetadataSection;
        private ResourceLocation locationTexturePackIcon;

        private Entry(File resourcePackFileIn) {
            this(this$0.func_191399_b(resourcePackFileIn));
        }

        private Entry(IResourcePack reResourcePackIn) {
            this.reResourcePack = reResourcePackIn;
        }

        public void updateResourcePack() throws IOException {
            this.rePackMetadataSection = (PackMetadataSection)this.reResourcePack.getPackMetadata(ResourcePackRepository.this.rprMetadataSerializer, "pack");
            this.closeResourcePack();
        }

        public void bindTexturePackIcon(TextureManager textureManagerIn) {
            BufferedImage bufferedimage = null;
            if (this.locationTexturePackIcon == null) {
                try {
                    bufferedimage = this.reResourcePack.getPackImage();
                }
                catch (IOException iOException) {
                    // empty catch block
                }
                if (bufferedimage == null) {
                    try {
                        bufferedimage = TextureUtil.readBufferedImage(Minecraft.getMinecraft().getResourceManager().getResource(field_191400_f).getInputStream());
                    }
                    catch (IOException ioexception) {
                        throw new Error("Couldn't bind resource pack icon", ioexception);
                    }
                }
            }
            if (this.locationTexturePackIcon == null) {
                this.locationTexturePackIcon = textureManagerIn.getDynamicTextureLocation("texturepackicon", new DynamicTexture(bufferedimage));
            }
            textureManagerIn.bindTexture(this.locationTexturePackIcon);
        }

        public void closeResourcePack() {
            if (this.reResourcePack instanceof Closeable) {
                IOUtils.closeQuietly((Closeable)((Object)this.reResourcePack));
            }
        }

        public IResourcePack getResourcePack() {
            return this.reResourcePack;
        }

        public String getResourcePackName() {
            return this.reResourcePack.getPackName();
        }

        public String getTexturePackDescription() {
            return this.rePackMetadataSection == null ? (Object)((Object)TextFormatting.RED) + "Invalid pack.mcmeta (or missing 'pack' section)" : this.rePackMetadataSection.getPackDescription().getFormattedText();
        }

        public int getPackFormat() {
            return this.rePackMetadataSection == null ? 0 : this.rePackMetadataSection.getPackFormat();
        }

        public boolean equals(Object p_equals_1_) {
            if (this == p_equals_1_) {
                return true;
            }
            return p_equals_1_ instanceof Entry ? this.toString().equals(p_equals_1_.toString()) : false;
        }

        public int hashCode() {
            return this.toString().hashCode();
        }

        public String toString() {
            return String.format("%s:%s", this.reResourcePack.getPackName(), this.reResourcePack instanceof FolderResourcePack ? "folder" : "zip");
        }
    }
}

