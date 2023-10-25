/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiListWorldSelectionEntry;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldSummary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiListWorldSelection
extends GuiListExtended {
    private static final Logger LOGGER = LogManager.getLogger();
    private final GuiWorldSelection worldSelectionObj;
    private final List<GuiListWorldSelectionEntry> entries = Lists.newArrayList();
    private int selectedIdx = -1;

    public GuiListWorldSelection(GuiWorldSelection p_i46590_1_, Minecraft clientIn, int p_i46590_3_, int p_i46590_4_, int p_i46590_5_, int p_i46590_6_, int p_i46590_7_) {
        super(clientIn, p_i46590_3_, p_i46590_4_, p_i46590_5_, p_i46590_6_, p_i46590_7_);
        this.worldSelectionObj = p_i46590_1_;
        this.refreshList();
    }

    public void refreshList() {
        List<WorldSummary> list;
        ISaveFormat isaveformat = this.mc.getSaveLoader();
        try {
            list = isaveformat.getSaveList();
        }
        catch (AnvilConverterException anvilconverterexception) {
            LOGGER.error("Couldn't load level list", (Throwable)anvilconverterexception);
            this.mc.displayGuiScreen(new GuiErrorScreen(I18n.format("selectWorld.unable_to_load", new Object[0]), anvilconverterexception.getMessage()));
            return;
        }
        Collections.sort(list);
        for (WorldSummary worldsummary : list) {
            this.entries.add(new GuiListWorldSelectionEntry(this, worldsummary, this.mc.getSaveLoader()));
        }
    }

    @Override
    public GuiListWorldSelectionEntry getListEntry(int index) {
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

    public void selectWorld(int idx) {
        this.selectedIdx = idx;
        this.worldSelectionObj.selectWorld(this.getSelectedWorld());
    }

    @Override
    protected boolean isSelected(int slotIndex) {
        return slotIndex == this.selectedIdx;
    }

    @Nullable
    public GuiListWorldSelectionEntry getSelectedWorld() {
        return this.selectedIdx >= 0 && this.selectedIdx < this.getSize() ? this.getListEntry(this.selectedIdx) : null;
    }

    public GuiWorldSelection getGuiWorldSelection() {
        return this.worldSelectionObj;
    }
}

