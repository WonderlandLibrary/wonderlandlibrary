package net.minecraft.client.resources;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenWorking;
import net.minecraft.client.resources.FileResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.IProgressUpdate;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResourcePackRepository {
    private static final Logger logger = LogManager.getLogger();
    private static final FileFilter resourcePackFilter = new /* Unavailable Anonymous Inner Class!! */;
    private final File dirResourcepacks;
    public final IResourcePack rprDefaultResourcePack;
    private final File dirServerResourcepacks;
    public final IMetadataSerializer rprMetadataSerializer;
    private IResourcePack resourcePackInstance;
    private final ReentrantLock lock = new ReentrantLock();
    private ListenableFuture<Object> downloadingPacks;
    private List<Entry> repositoryEntriesAll = Lists.newArrayList();
    public List<Entry> repositoryEntries = Lists.newArrayList();

    public ResourcePackRepository(File dirResourcepacksIn, File dirServerResourcepacksIn, IResourcePack rprDefaultResourcePackIn, IMetadataSerializer rprMetadataSerializerIn, GameSettings settings) {
        this.dirResourcepacks = dirResourcepacksIn;
        this.dirServerResourcepacks = dirServerResourcepacksIn;
        this.rprDefaultResourcePack = rprDefaultResourcePackIn;
        this.rprMetadataSerializer = rprMetadataSerializerIn;
        this.fixDirResourcepacks();
        this.updateRepositoryEntriesAll();
        Iterator iterator = settings.resourcePacks.iterator();
        block0: while (iterator.hasNext()) {
            String s = (String)iterator.next();
            for (Entry resourcepackrepository$entry : this.repositoryEntriesAll) {
                if (!resourcepackrepository$entry.getResourcePackName().equals((Object)s)) continue;
                if (resourcepackrepository$entry.func_183027_f() == 1 || settings.incompatibleResourcePacks.contains((Object)resourcepackrepository$entry.getResourcePackName())) {
                    this.repositoryEntries.add((Object)resourcepackrepository$entry);
                    continue block0;
                }
                iterator.remove();
                logger.warn("Removed selected resource pack {} because it's no longer compatible", new Object[]{resourcepackrepository$entry.getResourcePackName()});
            }
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

    private List<File> getResourcePackFiles() {
        return this.dirResourcepacks.isDirectory() ? Arrays.asList((Object[])this.dirResourcepacks.listFiles(resourcePackFilter)) : Collections.emptyList();
    }

    public void updateRepositoryEntriesAll() {
        ArrayList list = Lists.newArrayList();
        for (File file1 : this.getResourcePackFiles()) {
            Entry resourcepackrepository$entry = new Entry(this, file1, null);
            if (!this.repositoryEntriesAll.contains((Object)resourcepackrepository$entry)) {
                try {
                    resourcepackrepository$entry.updateResourcePack();
                    list.add((Object)resourcepackrepository$entry);
                }
                catch (Exception var61) {
                    list.remove((Object)resourcepackrepository$entry);
                }
                continue;
            }
            int i = this.repositoryEntriesAll.indexOf((Object)resourcepackrepository$entry);
            if (i <= -1 || i >= this.repositoryEntriesAll.size()) continue;
            list.add(this.repositoryEntriesAll.get(i));
        }
        this.repositoryEntriesAll.removeAll((Collection)list);
        for (Entry resourcepackrepository$entry1 : this.repositoryEntriesAll) {
            resourcepackrepository$entry1.closeResourcePack();
        }
        this.repositoryEntriesAll = list;
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
        String s = hash.matches("^[a-f0-9]{40}$") ? hash : "legacy";
        File file1 = new File(this.dirServerResourcepacks, s);
        this.lock.lock();
        try {
            ListenableFuture<Object> listenablefuture;
            ListenableFuture<Object> listenablefuture11;
            this.clearResourcePack();
            if (file1.exists() && hash.length() == 40) {
                String s1;
                block8: {
                    ListenableFuture<Object> listenablefuture2;
                    ListenableFuture<Object> listenablefuture3;
                    s1 = Hashing.sha1().hashBytes(Files.toByteArray((File)file1)).toString();
                    if (!s1.equals((Object)hash)) break block8;
                    ListenableFuture<Object> listenableFuture = listenablefuture3 = (listenablefuture2 = this.setResourcePackInstance(file1));
                    return listenableFuture;
                }
                try {
                    logger.warn("File " + file1 + " had wrong hash (expected " + hash + ", found " + s1 + "). Deleting it.");
                    FileUtils.deleteQuietly((File)file1);
                }
                catch (IOException ioexception) {
                    logger.warn("File " + file1 + " couldn't be hashed. Deleting it.", (Throwable)ioexception);
                    FileUtils.deleteQuietly((File)file1);
                }
            }
            this.deleteOldServerResourcesPacks();
            GuiScreenWorking guiscreenworking = new GuiScreenWorking();
            Map map = Minecraft.getSessionInfo();
            Minecraft minecraft = Minecraft.getMinecraft();
            Futures.getUnchecked((Future)minecraft.addScheduledTask((Runnable)new /* Unavailable Anonymous Inner Class!! */));
            SettableFuture settablefuture = SettableFuture.create();
            this.downloadingPacks = HttpUtil.downloadResourcePack((File)file1, (String)url, (Map)map, (int)0x3200000, (IProgressUpdate)guiscreenworking, (Proxy)minecraft.getProxy());
            Futures.addCallback(this.downloadingPacks, (FutureCallback)new /* Unavailable Anonymous Inner Class!! */);
            ListenableFuture<Object> listenableFuture = listenablefuture11 = (listenablefuture = this.downloadingPacks);
            return listenableFuture;
        }
        finally {
            this.lock.unlock();
        }
    }

    private void deleteOldServerResourcesPacks() {
        ArrayList list = Lists.newArrayList((Iterable)FileUtils.listFiles((File)this.dirServerResourcepacks, (IOFileFilter)TrueFileFilter.TRUE, (IOFileFilter)null));
        Collections.sort((List)list, (Comparator)LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        int i = 0;
        for (File file1 : list) {
            if (i++ < 10) continue;
            logger.info("Deleting old server resource pack " + file1.getName());
            FileUtils.deleteQuietly((File)file1);
        }
    }

    public ListenableFuture<Object> setResourcePackInstance(File resourceFile) {
        this.resourcePackInstance = new FileResourcePack(resourceFile);
        return Minecraft.getMinecraft().scheduleResourcesRefresh();
    }

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
}
