/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.Sys
 */
package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;
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
    private boolean changed = false;
    private GuiResourcePackSelected selectedResourcePacksList;
    private final GuiScreen parentScreen;
    private List<ResourcePackListEntry> availableResourcePacks;
    private List<ResourcePackListEntry> selectedResourcePacks;
    private static final Logger logger = LogManager.getLogger();
    private GuiResourcePackAvailable availableResourcePacksList;

    public List<ResourcePackListEntry> getSelectedResourcePacks() {
        return this.selectedResourcePacks;
    }

    public void markChanged() {
        this.changed = true;
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.selectedResourcePacksList.handleMouseInput();
        this.availableResourcePacksList.handleMouseInput();
    }

    public GuiScreenResourcePacks(GuiScreen guiScreen) {
        this.parentScreen = guiScreen;
    }

    public List<ResourcePackListEntry> getAvailableResourcePacks() {
        return this.availableResourcePacks;
    }

    public List<ResourcePackListEntry> getListContaining(ResourcePackListEntry resourcePackListEntry) {
        return this.hasResourcePackEntry(resourcePackListEntry) ? this.selectedResourcePacks : this.availableResourcePacks;
    }

    public boolean hasResourcePackEntry(ResourcePackListEntry resourcePackListEntry) {
        return this.selectedResourcePacks.contains(resourcePackListEntry);
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.drawBackground(0);
        this.availableResourcePacksList.drawScreen(n, n2, f);
        this.selectedResourcePacksList.drawScreen(n, n2, f);
        this.drawCenteredString(this.fontRendererObj, I18n.format("resourcePack.title", new Object[0]), width / 2, 16, 0xFFFFFF);
        this.drawCenteredString(this.fontRendererObj, I18n.format("resourcePack.folderInfo", new Object[0]), width / 2 - 77, height - 26, 0x808080);
        super.drawScreen(n, n2, f);
    }

    @Override
    protected void mouseClicked(int n, int n2, int n3) throws IOException {
        super.mouseClicked(n, n2, n3);
        this.availableResourcePacksList.mouseClicked(n, n2, n3);
        this.selectedResourcePacksList.mouseClicked(n, n2, n3);
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiOptionButton(2, width / 2 - 154, height - 48, I18n.format("resourcePack.openFolder", new Object[0])));
        this.buttonList.add(new GuiOptionButton(1, width / 2 + 4, height - 48, I18n.format("gui.done", new Object[0])));
        if (!this.changed) {
            this.availableResourcePacks = Lists.newArrayList();
            this.selectedResourcePacks = Lists.newArrayList();
            ResourcePackRepository resourcePackRepository = this.mc.getResourcePackRepository();
            resourcePackRepository.updateRepositoryEntriesAll();
            ArrayList arrayList = Lists.newArrayList(resourcePackRepository.getRepositoryEntriesAll());
            arrayList.removeAll(resourcePackRepository.getRepositoryEntries());
            for (ResourcePackRepository.Entry entry : arrayList) {
                this.availableResourcePacks.add(new ResourcePackListEntryFound(this, entry));
            }
            for (ResourcePackRepository.Entry entry : Lists.reverse(resourcePackRepository.getRepositoryEntries())) {
                this.selectedResourcePacks.add(new ResourcePackListEntryFound(this, entry));
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

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        if (guiButton.enabled) {
            if (guiButton.id == 2) {
                File file = this.mc.getResourcePackRepository().getDirResourcepacks();
                String string = file.getAbsolutePath();
                if (Util.getOSType() == Util.EnumOS.OSX) {
                    try {
                        logger.info(string);
                        Runtime.getRuntime().exec(new String[]{"/usr/bin/open", string});
                        return;
                    }
                    catch (IOException bl) {
                        logger.error("Couldn't open file", (Throwable)bl);
                    }
                } else if (Util.getOSType() == Util.EnumOS.WINDOWS) {
                    String string2 = String.format("cmd.exe /C start \"Open file\" \"%s\"", string);
                    try {
                        Runtime.getRuntime().exec(string2);
                        return;
                    }
                    catch (IOException throwable) {
                        logger.error("Couldn't open file", (Throwable)throwable);
                    }
                }
                boolean bl = false;
                try {
                    Class<?> clazz = Class.forName("java.awt.Desktop");
                    Object object = clazz.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
                    clazz.getMethod("browse", URI.class).invoke(object, file.toURI());
                }
                catch (Throwable throwable) {
                    logger.error("Couldn't open link", throwable);
                    bl = true;
                }
                if (bl) {
                    logger.info("Opening via system class!");
                    Sys.openURL((String)("file://" + string));
                }
            } else if (guiButton.id == 1) {
                if (this.changed) {
                    ArrayList arrayList = Lists.newArrayList();
                    for (ResourcePackListEntry object : this.selectedResourcePacks) {
                        if (!(object instanceof ResourcePackListEntryFound)) continue;
                        arrayList.add(((ResourcePackListEntryFound)object).func_148318_i());
                    }
                    Collections.reverse(arrayList);
                    this.mc.getResourcePackRepository().setRepositories(arrayList);
                    Minecraft.gameSettings.resourcePacks.clear();
                    Minecraft.gameSettings.field_183018_l.clear();
                    for (ResourcePackRepository.Entry entry : arrayList) {
                        Minecraft.gameSettings.resourcePacks.add(entry.getResourcePackName());
                        if (entry.func_183027_f() == 1) continue;
                        Minecraft.gameSettings.field_183018_l.add(entry.getResourcePackName());
                    }
                    Minecraft.gameSettings.saveOptions();
                    this.mc.refreshResources();
                }
                this.mc.displayGuiScreen(this.parentScreen);
            }
        }
    }

    @Override
    protected void mouseReleased(int n, int n2, int n3) {
        super.mouseReleased(n, n2, n3);
    }
}

