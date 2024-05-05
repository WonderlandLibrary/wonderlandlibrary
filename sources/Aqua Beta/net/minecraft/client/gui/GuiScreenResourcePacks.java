package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiResourcePackAvailable;
import net.minecraft.client.gui.GuiResourcePackSelected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.ResourcePackListEntry;
import net.minecraft.client.resources.ResourcePackListEntryDefault;
import net.minecraft.client.resources.ResourcePackListEntryFound;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.Sys;

public class GuiScreenResourcePacks
extends GuiScreen {
    private static final Logger logger = LogManager.getLogger();
    private final GuiScreen parentScreen;
    private List<ResourcePackListEntry> availableResourcePacks;
    private List<ResourcePackListEntry> selectedResourcePacks;
    private GuiResourcePackAvailable availableResourcePacksList;
    private GuiResourcePackSelected selectedResourcePacksList;
    private boolean changed = false;

    public GuiScreenResourcePacks(GuiScreen parentScreenIn) {
        this.parentScreen = parentScreenIn;
    }

    public void initGui() {
        this.buttonList.add((Object)new GuiOptionButton(2, width / 2 - 154, height - 48, I18n.format((String)"resourcePack.openFolder", (Object[])new Object[0])));
        this.buttonList.add((Object)new GuiOptionButton(1, width / 2 + 4, height - 48, I18n.format((String)"gui.done", (Object[])new Object[0])));
        if (!this.changed) {
            this.availableResourcePacks = Lists.newArrayList();
            this.selectedResourcePacks = Lists.newArrayList();
            ResourcePackRepository resourcepackrepository = this.mc.getResourcePackRepository();
            resourcepackrepository.updateRepositoryEntriesAll();
            ArrayList list = Lists.newArrayList((Iterable)resourcepackrepository.getRepositoryEntriesAll());
            list.removeAll((Collection)resourcepackrepository.getRepositoryEntries());
            for (ResourcePackRepository.Entry resourcepackrepository$entry : list) {
                this.availableResourcePacks.add((Object)new ResourcePackListEntryFound(this, resourcepackrepository$entry));
            }
            for (ResourcePackRepository.Entry resourcepackrepository$entry1 : Lists.reverse((List)resourcepackrepository.getRepositoryEntries())) {
                this.selectedResourcePacks.add((Object)new ResourcePackListEntryFound(this, resourcepackrepository$entry1));
            }
            this.selectedResourcePacks.add((Object)new ResourcePackListEntryDefault(this));
        }
        this.availableResourcePacksList = new GuiResourcePackAvailable(this.mc, 200, height, this.availableResourcePacks);
        this.availableResourcePacksList.setSlotXBoundsFromLeft(width / 2 - 4 - 200);
        this.availableResourcePacksList.registerScrollButtons(7, 8);
        this.selectedResourcePacksList = new GuiResourcePackSelected(this.mc, 200, height, this.selectedResourcePacks);
        this.selectedResourcePacksList.setSlotXBoundsFromLeft(width / 2 + 4);
        this.selectedResourcePacksList.registerScrollButtons(7, 8);
    }

    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.selectedResourcePacksList.handleMouseInput();
        this.availableResourcePacksList.handleMouseInput();
    }

    public boolean hasResourcePackEntry(ResourcePackListEntry p_146961_1_) {
        return this.selectedResourcePacks.contains((Object)p_146961_1_);
    }

    public List<ResourcePackListEntry> getListContaining(ResourcePackListEntry p_146962_1_) {
        return this.hasResourcePackEntry(p_146962_1_) ? this.selectedResourcePacks : this.availableResourcePacks;
    }

    public List<ResourcePackListEntry> getAvailableResourcePacks() {
        return this.availableResourcePacks;
    }

    public List<ResourcePackListEntry> getSelectedResourcePacks() {
        return this.selectedResourcePacks;
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 2) {
                File file1 = this.mc.getResourcePackRepository().getDirResourcepacks();
                String s = file1.getAbsolutePath();
                if (Util.getOSType() == Util.EnumOS.OSX) {
                    try {
                        logger.info(s);
                        Runtime.getRuntime().exec(new String[]{"/usr/bin/open", s});
                        return;
                    }
                    catch (IOException ioexception1) {
                        logger.error("Couldn't open file", (Throwable)ioexception1);
                    }
                } else if (Util.getOSType() == Util.EnumOS.WINDOWS) {
                    String s1 = String.format((String)"cmd.exe /C start \"Open file\" \"%s\"", (Object[])new Object[]{s});
                    try {
                        Runtime.getRuntime().exec(s1);
                        return;
                    }
                    catch (IOException ioexception) {
                        logger.error("Couldn't open file", (Throwable)ioexception);
                    }
                }
                boolean flag = false;
                try {
                    Class oclass = Class.forName((String)"java.awt.Desktop");
                    Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
                    oclass.getMethod("browse", new Class[]{URI.class}).invoke(object, new Object[]{file1.toURI()});
                }
                catch (Throwable throwable) {
                    logger.error("Couldn't open link", throwable);
                    flag = true;
                }
                if (flag) {
                    logger.info("Opening via system class!");
                    Sys.openURL((String)("file://" + s));
                }
            } else if (button.id == 1) {
                if (this.changed) {
                    ArrayList list = Lists.newArrayList();
                    for (ResourcePackListEntry resourcepacklistentry : this.selectedResourcePacks) {
                        if (!(resourcepacklistentry instanceof ResourcePackListEntryFound)) continue;
                        list.add((Object)((ResourcePackListEntryFound)resourcepacklistentry).func_148318_i());
                    }
                    Collections.reverse((List)list);
                    this.mc.getResourcePackRepository().setRepositories((List)list);
                    this.mc.gameSettings.resourcePacks.clear();
                    this.mc.gameSettings.incompatibleResourcePacks.clear();
                    for (ResourcePackRepository.Entry resourcepackrepository$entry : list) {
                        this.mc.gameSettings.resourcePacks.add((Object)resourcepackrepository$entry.getResourcePackName());
                        if (resourcepackrepository$entry.func_183027_f() == 1) continue;
                        this.mc.gameSettings.incompatibleResourcePacks.add((Object)resourcepackrepository$entry.getResourcePackName());
                    }
                    this.mc.gameSettings.saveOptions();
                    this.mc.refreshResources();
                }
                this.mc.displayGuiScreen(this.parentScreen);
            }
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.availableResourcePacksList.mouseClicked(mouseX, mouseY, mouseButton);
        this.selectedResourcePacksList.mouseClicked(mouseX, mouseY, mouseButton);
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawBackground(0);
        this.availableResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
        this.selectedResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, I18n.format((String)"resourcePack.title", (Object[])new Object[0]), width / 2, 16, 0xFFFFFF);
        this.drawCenteredString(this.fontRendererObj, I18n.format((String)"resourcePack.folderInfo", (Object[])new Object[0]), width / 2 - 77, height - 26, 0x808080);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void markChanged() {
        this.changed = true;
    }
}
