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
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenWorking;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResourcePackRepository {
   private List repositoryEntriesAll = Lists.newArrayList();
   public final IMetadataSerializer rprMetadataSerializer;
   private List repositoryEntries = Lists.newArrayList();
   private static final Logger logger = LogManager.getLogger();
   private static final FileFilter resourcePackFilter = new FileFilter() {
      public boolean accept(File var1) {
         boolean var2 = var1.isFile() && var1.getName().endsWith(".zip");
         boolean var3 = var1.isDirectory() && (new File(var1, "pack.mcmeta")).isFile();
         return var2 || var3;
      }
   };
   private final File dirResourcepacks;
   private final ReentrantLock lock = new ReentrantLock();
   private IResourcePack resourcePackInstance;
   private ListenableFuture field_177322_i;
   public final IResourcePack rprDefaultResourcePack;
   private final File dirServerResourcepacks;

   public IResourcePack getResourcePackInstance() {
      return this.resourcePackInstance;
   }

   public ResourcePackRepository(File var1, File var2, IResourcePack var3, IMetadataSerializer var4, GameSettings var5) {
      this.dirResourcepacks = var1;
      this.dirServerResourcepacks = var2;
      this.rprDefaultResourcePack = var3;
      this.rprMetadataSerializer = var4;
      this.fixDirResourcepacks();
      this.updateRepositoryEntriesAll();
      Iterator var6 = var5.resourcePacks.iterator();

      while(true) {
         while(var6.hasNext()) {
            String var7 = (String)var6.next();
            Iterator var9 = this.repositoryEntriesAll.iterator();

            while(var9.hasNext()) {
               ResourcePackRepository.Entry var8 = (ResourcePackRepository.Entry)var9.next();
               if (var8.getResourcePackName().equals(var7)) {
                  if (var8.func_183027_f() == 1 || var5.field_183018_l.contains(var8.getResourcePackName())) {
                     this.repositoryEntries.add(var8);
                     break;
                  }

                  var6.remove();
                  logger.warn("Removed selected resource pack {} because it's no longer compatible", var8.getResourcePackName());
               }
            }
         }

         return;
      }
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

   public void setRepositories(List var1) {
      this.repositoryEntries.clear();
      this.repositoryEntries.addAll(var1);
   }

   private void fixDirResourcepacks() {
      if (this.dirResourcepacks.exists()) {
         if (!this.dirResourcepacks.isDirectory() && (!this.dirResourcepacks.delete() || !this.dirResourcepacks.mkdirs())) {
            logger.warn("Unable to recreate resourcepack folder, it exists but is not a directory: " + this.dirResourcepacks);
         }
      } else if (!this.dirResourcepacks.mkdirs()) {
         logger.warn("Unable to create resourcepack folder: " + this.dirResourcepacks);
      }

   }

   public ListenableFuture setResourcePackInstance(File var1) {
      this.resourcePackInstance = new FileResourcePack(var1);
      return Minecraft.getMinecraft().scheduleResourcesRefresh();
   }

   public ListenableFuture downloadResourcePack(String var1, String var2) {
      String var3;
      if (var2.matches("^[a-f0-9]{40}$")) {
         var3 = var2;
      } else {
         var3 = "legacy";
      }

      ListenableFuture var11;
      label28: {
         File var4 = new File(this.dirServerResourcepacks, var3);
         this.lock.lock();
         this.func_148529_f();
         if (var4.exists() && var2.length() == 40) {
            try {
               String var5 = Hashing.sha1().hashBytes(Files.toByteArray(var4)).toString();
               if (var5.equals(var2)) {
                  ListenableFuture var14 = this.setResourcePackInstance(var4);
                  var11 = var14;
                  break label28;
               }

               logger.warn("File " + var4 + " had wrong hash (expected " + var2 + ", found " + var5 + "). Deleting it.");
               FileUtils.deleteQuietly(var4);
            } catch (IOException var12) {
               logger.warn((String)("File " + var4 + " couldn't be hashed. Deleting it."), (Throwable)var12);
               FileUtils.deleteQuietly(var4);
            }
         }

         this.func_183028_i();
         GuiScreenWorking var13 = new GuiScreenWorking();
         Map var6 = Minecraft.getSessionInfo();
         Minecraft var7 = Minecraft.getMinecraft();
         Futures.getUnchecked(var7.addScheduledTask(new Runnable(this, var7, var13) {
            final ResourcePackRepository this$0;
            private final GuiScreenWorking val$guiscreenworking;
            private final Minecraft val$minecraft;

            public void run() {
               this.val$minecraft.displayGuiScreen(this.val$guiscreenworking);
            }

            {
               this.this$0 = var1;
               this.val$minecraft = var2;
               this.val$guiscreenworking = var3;
            }
         }));
         SettableFuture var8 = SettableFuture.create();
         this.field_177322_i = HttpUtil.downloadResourcePack(var4, var1, var6, 52428800, var13, var7.getProxy());
         Futures.addCallback(this.field_177322_i, new FutureCallback(this, var4, var8) {
            private final File val$file1;
            final ResourcePackRepository this$0;
            private final SettableFuture val$settablefuture;

            public void onFailure(Throwable var1) {
               this.val$settablefuture.setException(var1);
            }

            {
               this.this$0 = var1;
               this.val$file1 = var2;
               this.val$settablefuture = var3;
            }

            public void onSuccess(Object var1) {
               this.this$0.setResourcePackInstance(this.val$file1);
               this.val$settablefuture.set((Object)null);
            }
         });
         ListenableFuture var9 = this.field_177322_i;
         this.lock.unlock();
         return var9;
      }

      this.lock.unlock();
      return var11;
   }

   private void func_183028_i() {
      ArrayList var1 = Lists.newArrayList((Iterable)FileUtils.listFiles(this.dirServerResourcepacks, TrueFileFilter.TRUE, (IOFileFilter)null));
      Collections.sort(var1, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
      int var2 = 0;
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         File var3 = (File)var4.next();
         if (var2++ >= 10) {
            logger.info("Deleting old server resource pack " + var3.getName());
            FileUtils.deleteQuietly(var3);
         }
      }

   }

   public List getRepositoryEntriesAll() {
      return ImmutableList.copyOf((Collection)this.repositoryEntriesAll);
   }

   private List getResourcePackFiles() {
      return this.dirResourcepacks.isDirectory() ? Arrays.asList(this.dirResourcepacks.listFiles(resourcePackFilter)) : Collections.emptyList();
   }

   public File getDirResourcepacks() {
      return this.dirResourcepacks;
   }

   public void updateRepositoryEntriesAll() {
      ArrayList var1 = Lists.newArrayList();
      Iterator var3 = this.getResourcePackFiles().iterator();

      while(var3.hasNext()) {
         File var2 = (File)var3.next();
         ResourcePackRepository.Entry var4 = new ResourcePackRepository.Entry(this, var2, (ResourcePackRepository.Entry)null);
         if (!this.repositoryEntriesAll.contains(var4)) {
            try {
               var4.updateResourcePack();
               var1.add(var4);
            } catch (Exception var6) {
               var1.remove(var4);
            }
         } else {
            int var5 = this.repositoryEntriesAll.indexOf(var4);
            if (var5 > -1 && var5 < this.repositoryEntriesAll.size()) {
               var1.add((ResourcePackRepository.Entry)this.repositoryEntriesAll.get(var5));
            }
         }
      }

      this.repositoryEntriesAll.removeAll(var1);
      var3 = this.repositoryEntriesAll.iterator();

      while(var3.hasNext()) {
         ResourcePackRepository.Entry var7 = (ResourcePackRepository.Entry)var3.next();
         var7.closeResourcePack();
      }

      this.repositoryEntriesAll = var1;
   }

   public List getRepositoryEntries() {
      return ImmutableList.copyOf((Collection)this.repositoryEntries);
   }

   public class Entry {
      private PackMetadataSection rePackMetadataSection;
      private ResourceLocation locationTexturePackIcon;
      private IResourcePack reResourcePack;
      final ResourcePackRepository this$0;
      private BufferedImage texturePackIcon;
      private final File resourcePackFile;

      private Entry(ResourcePackRepository var1, File var2) {
         this.this$0 = var1;
         this.resourcePackFile = var2;
      }

      public String getResourcePackName() {
         return this.reResourcePack.getPackName();
      }

      public String toString() {
         return String.format("%s:%s:%d", this.resourcePackFile.getName(), this.resourcePackFile.isDirectory() ? "folder" : "zip", this.resourcePackFile.lastModified());
      }

      public void closeResourcePack() {
         if (this.reResourcePack instanceof Closeable) {
            IOUtils.closeQuietly((Closeable)this.reResourcePack);
         }

      }

      public int hashCode() {
         return this.toString().hashCode();
      }

      public IResourcePack getResourcePack() {
         return this.reResourcePack;
      }

      Entry(ResourcePackRepository var1, File var2, ResourcePackRepository.Entry var3) {
         this(var1, var2);
      }

      public void bindTexturePackIcon(TextureManager var1) {
         if (this.locationTexturePackIcon == null) {
            this.locationTexturePackIcon = var1.getDynamicTextureLocation("texturepackicon", new DynamicTexture(this.texturePackIcon));
         }

         var1.bindTexture(this.locationTexturePackIcon);
      }

      public String getTexturePackDescription() {
         return this.rePackMetadataSection == null ? EnumChatFormatting.RED + "Invalid pack.mcmeta (or missing 'pack' section)" : this.rePackMetadataSection.getPackDescription().getFormattedText();
      }

      public int func_183027_f() {
         return this.rePackMetadataSection.getPackFormat();
      }

      public void updateResourcePack() throws IOException {
         this.reResourcePack = (IResourcePack)(this.resourcePackFile.isDirectory() ? new FolderResourcePack(this.resourcePackFile) : new FileResourcePack(this.resourcePackFile));
         this.rePackMetadataSection = (PackMetadataSection)this.reResourcePack.getPackMetadata(this.this$0.rprMetadataSerializer, "pack");

         try {
            this.texturePackIcon = this.reResourcePack.getPackImage();
         } catch (IOException var2) {
         }

         if (this.texturePackIcon == null) {
            this.texturePackIcon = this.this$0.rprDefaultResourcePack.getPackImage();
         }

         this.closeResourcePack();
      }

      public boolean equals(Object var1) {
         return this == var1 ? true : (var1 instanceof ResourcePackRepository.Entry ? this.toString().equals(var1.toString()) : false);
      }
   }
}
