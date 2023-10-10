package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.ResourcePackListEntry;
import net.minecraft.client.resources.ResourcePackListEntryDefault;
import net.minecraft.client.resources.ResourcePackListEntryFound;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.Sys;

public class GuiScreenResourcePacks extends GuiScreen {
   private List selectedResourcePacks;
   private GuiResourcePackSelected selectedResourcePacksList;
   private GuiResourcePackAvailable availableResourcePacksList;
   private boolean changed = false;
   private List availableResourcePacks;
   private final GuiScreen parentScreen;
   private static final Logger logger = LogManager.getLogger();

   protected void actionPerformed(GuiButton var1) throws IOException {
      if (var1.enabled) {
         if (var1.id == 2) {
            File var2 = this.mc.getResourcePackRepository().getDirResourcepacks();
            String var3 = var2.getAbsolutePath();
            if (Util.getOSType() == Util.EnumOS.OSX) {
               try {
                  logger.info(var3);
                  Runtime.getRuntime().exec(new String[]{"/usr/bin/open", var3});
                  return;
               } catch (IOException var9) {
                  logger.error((String)"Couldn't open file", (Throwable)var9);
               }
            } else if (Util.getOSType() == Util.EnumOS.WINDOWS) {
               String var4 = String.format("cmd.exe /C start \"Open file\" \"%s\"", var3);

               try {
                  Runtime.getRuntime().exec(var4);
                  return;
               } catch (IOException var8) {
                  logger.error((String)"Couldn't open file", (Throwable)var8);
               }
            }

            boolean var13 = false;

            try {
               Class var5 = Class.forName("java.awt.Desktop");
               Object var6 = var5.getMethod("getDesktop").invoke((Object)null);
               var5.getMethod("browse", URI.class).invoke(var6, var2.toURI());
            } catch (Throwable var7) {
               logger.error("Couldn't open link", var7);
               var13 = true;
            }

            if (var13) {
               logger.info("Opening via system class!");
               Sys.openURL("file://" + var3);
            }
         } else if (var1.id == 1) {
            if (this.changed) {
               ArrayList var10 = Lists.newArrayList();
               Iterator var14 = this.selectedResourcePacks.iterator();

               while(var14.hasNext()) {
                  ResourcePackListEntry var11 = (ResourcePackListEntry)var14.next();
                  if (var11 instanceof ResourcePackListEntryFound) {
                     var10.add(((ResourcePackListEntryFound)var11).func_148318_i());
                  }
               }

               Collections.reverse(var10);
               this.mc.getResourcePackRepository().setRepositories(var10);
               this.mc.gameSettings.resourcePacks.clear();
               this.mc.gameSettings.field_183018_l.clear();
               var14 = var10.iterator();

               while(var14.hasNext()) {
                  ResourcePackRepository.Entry var12 = (ResourcePackRepository.Entry)var14.next();
                  this.mc.gameSettings.resourcePacks.add(var12.getResourcePackName());
                  if (var12.func_183027_f() != 1) {
                     this.mc.gameSettings.field_183018_l.add(var12.getResourcePackName());
                  }
               }

               this.mc.gameSettings.saveOptions();
               this.mc.refreshResources();
            }

            this.mc.displayGuiScreen(this.parentScreen);
         }
      }

   }

   public List getAvailableResourcePacks() {
      return this.availableResourcePacks;
   }

   public void drawScreen(int var1, int var2, float var3) {
      this.drawBackground(0);
      this.availableResourcePacksList.drawScreen(var1, var2, var3);
      this.selectedResourcePacksList.drawScreen(var1, var2, var3);
      this.drawCenteredString(this.fontRendererObj, I18n.format("resourcePack.title"), width / 2, 16, 16777215);
      this.drawCenteredString(this.fontRendererObj, I18n.format("resourcePack.folderInfo"), width / 2 - 77, height - 26, 8421504);
      super.drawScreen(var1, var2, var3);
   }

   public List getSelectedResourcePacks() {
      return this.selectedResourcePacks;
   }

   public GuiScreenResourcePacks(GuiScreen var1) {
      this.parentScreen = var1;
   }

   protected void mouseClicked(int var1, int var2, int var3) throws IOException {
      super.mouseClicked(var1, var2, var3);
      this.availableResourcePacksList.mouseClicked(var1, var2, var3);
      this.selectedResourcePacksList.mouseClicked(var1, var2, var3);
   }

   protected void mouseReleased(int var1, int var2, int var3) {
      super.mouseReleased(var1, var2, var3);
   }

   public void handleMouseInput() throws IOException {
      super.handleMouseInput();
      this.selectedResourcePacksList.handleMouseInput();
      this.availableResourcePacksList.handleMouseInput();
   }

   public void markChanged() {
      this.changed = true;
   }

   public void initGui() {
      this.buttonList.add(new GuiOptionButton(2, width / 2 - 154, height - 48, I18n.format("resourcePack.openFolder")));
      this.buttonList.add(new GuiOptionButton(1, width / 2 + 4, height - 48, I18n.format("gui.done")));
      if (!this.changed) {
         this.availableResourcePacks = Lists.newArrayList();
         this.selectedResourcePacks = Lists.newArrayList();
         ResourcePackRepository var1 = this.mc.getResourcePackRepository();
         var1.updateRepositoryEntriesAll();
         ArrayList var2 = Lists.newArrayList((Iterable)var1.getRepositoryEntriesAll());
         var2.removeAll(var1.getRepositoryEntries());
         Iterator var4 = var2.iterator();

         ResourcePackRepository.Entry var3;
         while(var4.hasNext()) {
            var3 = (ResourcePackRepository.Entry)var4.next();
            this.availableResourcePacks.add(new ResourcePackListEntryFound(this, var3));
         }

         var4 = Lists.reverse(var1.getRepositoryEntries()).iterator();

         while(var4.hasNext()) {
            var3 = (ResourcePackRepository.Entry)var4.next();
            this.selectedResourcePacks.add(new ResourcePackListEntryFound(this, var3));
         }

         this.selectedResourcePacks.add(new ResourcePackListEntryDefault(this));
      }

      this.availableResourcePacksList = new GuiResourcePackAvailable(this.mc, 200, height, this.availableResourcePacks);
      this.availableResourcePacksList.setSlotXBoundsFromLeft(width / 2 - 4 - 200);
      this.availableResourcePacksList.registerScrollButtons(7, 8);
      this.selectedResourcePacksList = new GuiResourcePackSelected(this.mc, 200, height, this.selectedResourcePacks);
      this.selectedResourcePacksList.setSlotXBoundsFromLeft(width / 2 + 4);
      this.selectedResourcePacksList.registerScrollButtons(7, 8);
   }

   public List getListContaining(ResourcePackListEntry var1) {
      return this.hasResourcePackEntry(var1) ? this.selectedResourcePacks : this.availableResourcePacks;
   }

   public boolean hasResourcePackEntry(ResourcePackListEntry var1) {
      return this.selectedResourcePacks.contains(var1);
   }
}
