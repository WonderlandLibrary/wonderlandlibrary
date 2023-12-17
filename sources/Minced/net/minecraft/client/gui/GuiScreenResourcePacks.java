// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import java.io.File;
import java.util.Collections;
import net.minecraft.client.renderer.OpenGlHelper;
import java.io.IOException;
import java.util.Iterator;
import net.minecraft.client.resources.ResourcePackListEntryDefault;
import net.minecraft.client.resources.ResourcePackListEntryServer;
import net.minecraft.client.resources.ResourcePackListEntryFound;
import net.minecraft.client.resources.ResourcePackRepository;
import java.util.Collection;
import com.google.common.collect.Lists;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.ResourcePackListEntry;
import java.util.List;

public class GuiScreenResourcePacks extends GuiScreen
{
    private final GuiScreen parentScreen;
    private List<ResourcePackListEntry> availableResourcePacks;
    private List<ResourcePackListEntry> selectedResourcePacks;
    private GuiResourcePackAvailable availableResourcePacksList;
    private GuiResourcePackSelected selectedResourcePacksList;
    private boolean changed;
    
    public GuiScreenResourcePacks(final GuiScreen parentScreenIn) {
        this.parentScreen = parentScreenIn;
    }
    
    @Override
    public void initGui() {
        this.buttonList.add(new GuiOptionButton(2, this.width / 2 - 154, this.height - 48, I18n.format("resourcePack.openFolder", new Object[0])));
        this.buttonList.add(new GuiOptionButton(1, this.width / 2 + 4, this.height - 48, I18n.format("gui.done", new Object[0])));
        if (!this.changed) {
            this.availableResourcePacks = (List<ResourcePackListEntry>)Lists.newArrayList();
            this.selectedResourcePacks = (List<ResourcePackListEntry>)Lists.newArrayList();
            final ResourcePackRepository resourcepackrepository = GuiScreenResourcePacks.mc.getResourcePackRepository();
            resourcepackrepository.updateRepositoryEntriesAll();
            final List<ResourcePackRepository.Entry> list = (List<ResourcePackRepository.Entry>)Lists.newArrayList((Iterable)resourcepackrepository.getRepositoryEntriesAll());
            list.removeAll(resourcepackrepository.getRepositoryEntries());
            for (final ResourcePackRepository.Entry resourcepackrepository$entry : list) {
                this.availableResourcePacks.add(new ResourcePackListEntryFound(this, resourcepackrepository$entry));
            }
            final ResourcePackRepository.Entry resourcepackrepository$entry2 = resourcepackrepository.getResourcePackEntry();
            if (resourcepackrepository$entry2 != null) {
                this.selectedResourcePacks.add(new ResourcePackListEntryServer(this, resourcepackrepository.getServerResourcePack()));
            }
            for (final ResourcePackRepository.Entry resourcepackrepository$entry3 : Lists.reverse((List)resourcepackrepository.getRepositoryEntries())) {
                this.selectedResourcePacks.add(new ResourcePackListEntryFound(this, resourcepackrepository$entry3));
            }
            this.selectedResourcePacks.add(new ResourcePackListEntryDefault(this));
        }
        (this.availableResourcePacksList = new GuiResourcePackAvailable(GuiScreenResourcePacks.mc, 200, this.height, this.availableResourcePacks)).setSlotXBoundsFromLeft(this.width / 2 - 4 - 200);
        this.availableResourcePacksList.registerScrollButtons(7, 8);
        (this.selectedResourcePacksList = new GuiResourcePackSelected(GuiScreenResourcePacks.mc, 200, this.height, this.selectedResourcePacks)).setSlotXBoundsFromLeft(this.width / 2 + 4);
        this.selectedResourcePacksList.registerScrollButtons(7, 8);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.selectedResourcePacksList.handleMouseInput();
        this.availableResourcePacksList.handleMouseInput();
    }
    
    public boolean hasResourcePackEntry(final ResourcePackListEntry resourcePackEntry) {
        return this.selectedResourcePacks.contains(resourcePackEntry);
    }
    
    public List<ResourcePackListEntry> getListContaining(final ResourcePackListEntry resourcePackEntry) {
        return this.hasResourcePackEntry(resourcePackEntry) ? this.selectedResourcePacks : this.availableResourcePacks;
    }
    
    public List<ResourcePackListEntry> getAvailableResourcePacks() {
        return this.availableResourcePacks;
    }
    
    public List<ResourcePackListEntry> getSelectedResourcePacks() {
        return this.selectedResourcePacks;
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 2) {
                final File file1 = GuiScreenResourcePacks.mc.getResourcePackRepository().getDirResourcepacks();
                OpenGlHelper.openFile(file1);
            }
            else if (button.id == 1) {
                if (this.changed) {
                    final List<ResourcePackRepository.Entry> list = (List<ResourcePackRepository.Entry>)Lists.newArrayList();
                    for (final ResourcePackListEntry resourcepacklistentry : this.selectedResourcePacks) {
                        if (resourcepacklistentry instanceof ResourcePackListEntryFound) {
                            list.add(((ResourcePackListEntryFound)resourcepacklistentry).getResourcePackEntry());
                        }
                    }
                    Collections.reverse(list);
                    GuiScreenResourcePacks.mc.getResourcePackRepository().setRepositories(list);
                    GuiScreenResourcePacks.mc.gameSettings.resourcePacks.clear();
                    GuiScreenResourcePacks.mc.gameSettings.incompatibleResourcePacks.clear();
                    for (final ResourcePackRepository.Entry resourcepackrepository$entry : list) {
                        GuiScreenResourcePacks.mc.gameSettings.resourcePacks.add(resourcepackrepository$entry.getResourcePackName());
                        if (resourcepackrepository$entry.getPackFormat() != 3) {
                            GuiScreenResourcePacks.mc.gameSettings.incompatibleResourcePacks.add(resourcepackrepository$entry.getResourcePackName());
                        }
                    }
                    GuiScreenResourcePacks.mc.gameSettings.saveOptions();
                    GuiScreenResourcePacks.mc.refreshResources();
                }
                GuiScreenResourcePacks.mc.displayGuiScreen(this.parentScreen);
            }
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.availableResourcePacksList.mouseClicked(mouseX, mouseY, mouseButton);
        this.selectedResourcePacksList.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawBackground(0);
        this.availableResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
        this.selectedResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRenderer, I18n.format("resourcePack.title", new Object[0]), this.width / 2, 16, 16777215);
        this.drawCenteredString(this.fontRenderer, I18n.format("resourcePack.folderInfo", new Object[0]), this.width / 2 - 77, this.height - 26, 8421504);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public void markChanged() {
        this.changed = true;
    }
}
