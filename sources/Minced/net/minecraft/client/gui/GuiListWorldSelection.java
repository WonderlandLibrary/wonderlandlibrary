// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import org.apache.logging.log4j.LogManager;
import javax.annotation.Nullable;
import java.util.Iterator;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldSummary;
import java.util.Collections;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.resources.I18n;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class GuiListWorldSelection extends GuiListExtended
{
    private static final Logger LOGGER;
    private final GuiWorldSelection worldSelection;
    private final List<GuiListWorldSelectionEntry> entries;
    private int selectedIdx;
    
    public GuiListWorldSelection(final GuiWorldSelection p_i46590_1_, final Minecraft clientIn, final int widthIn, final int heightIn, final int topIn, final int bottomIn, final int slotHeightIn) {
        super(clientIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
        this.entries = (List<GuiListWorldSelectionEntry>)Lists.newArrayList();
        this.selectedIdx = -1;
        this.worldSelection = p_i46590_1_;
        this.refreshList();
    }
    
    public void refreshList() {
        final ISaveFormat isaveformat = this.mc.getSaveLoader();
        List<WorldSummary> list;
        try {
            list = isaveformat.getSaveList();
        }
        catch (AnvilConverterException anvilconverterexception) {
            GuiListWorldSelection.LOGGER.error("Couldn't load level list", (Throwable)anvilconverterexception);
            this.mc.displayGuiScreen(new GuiErrorScreen(I18n.format("selectWorld.unable_to_load", new Object[0]), anvilconverterexception.getMessage()));
            return;
        }
        Collections.sort(list);
        for (final WorldSummary worldsummary : list) {
            this.entries.add(new GuiListWorldSelectionEntry(this, worldsummary, this.mc.getSaveLoader()));
        }
    }
    
    @Override
    public GuiListWorldSelectionEntry getListEntry(final int index) {
        return this.entries.get(index);
    }
    
    @Override
    protected int getSize() {
        return this.entries.size();
    }
    
    @Override
    protected int getScrollBarX() {
        return super.getScrollBarX() + 20;
    }
    
    @Override
    public int getListWidth() {
        return super.getListWidth() + 50;
    }
    
    public void selectWorld(final int idx) {
        this.selectedIdx = idx;
        this.worldSelection.selectWorld(this.getSelectedWorld());
    }
    
    @Override
    protected boolean isSelected(final int slotIndex) {
        return slotIndex == this.selectedIdx;
    }
    
    @Nullable
    public GuiListWorldSelectionEntry getSelectedWorld() {
        return (this.selectedIdx >= 0 && this.selectedIdx < this.getSize()) ? this.getListEntry(this.selectedIdx) : null;
    }
    
    public GuiWorldSelection getGuiWorldSelection() {
        return this.worldSelection;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
