/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiResourcePackList;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.ResourcePackListEntry;

public class GuiResourcePackSelected
extends GuiResourcePackList {
    public GuiResourcePackSelected(Minecraft minecraft, int n, int n2, List<ResourcePackListEntry> list) {
        super(minecraft, n, n2, list);
    }

    @Override
    protected String getListHeader() {
        return I18n.format("resourcePack.selected.title", new Object[0]);
    }
}

