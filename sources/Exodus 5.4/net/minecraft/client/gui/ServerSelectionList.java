/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.ServerListEntryLanDetected;
import net.minecraft.client.gui.ServerListEntryLanScan;
import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.LanServerDetector;

public class ServerSelectionList
extends GuiListExtended {
    private final GuiMultiplayer owner;
    private int selectedSlotIndex = -1;
    private final List<ServerListEntryLanDetected> field_148199_m;
    private final GuiListExtended.IGuiListEntry lanScanEntry;
    private final List<ServerListEntryNormal> field_148198_l = Lists.newArrayList();

    public void func_148195_a(ServerList serverList) {
        this.field_148198_l.clear();
        int n = 0;
        while (n < serverList.countServers()) {
            this.field_148198_l.add(new ServerListEntryNormal(this.owner, serverList.getServerData(n)));
            ++n;
        }
    }

    public int func_148193_k() {
        return this.selectedSlotIndex;
    }

    @Override
    protected int getSize() {
        return this.field_148198_l.size() + 1 + this.field_148199_m.size();
    }

    @Override
    public GuiListExtended.IGuiListEntry getListEntry(int n) {
        if (n < this.field_148198_l.size()) {
            return this.field_148198_l.get(n);
        }
        if ((n -= this.field_148198_l.size()) == 0) {
            return this.lanScanEntry;
        }
        return this.field_148199_m.get(--n);
    }

    @Override
    protected boolean isSelected(int n) {
        return n == this.selectedSlotIndex;
    }

    public void func_148194_a(List<LanServerDetector.LanServer> list) {
        this.field_148199_m.clear();
        for (LanServerDetector.LanServer lanServer : list) {
            this.field_148199_m.add(new ServerListEntryLanDetected(this.owner, lanServer));
        }
    }

    @Override
    protected int getScrollBarX() {
        return super.getScrollBarX() + 30;
    }

    @Override
    public int getListWidth() {
        return super.getListWidth() + 85;
    }

    public void setSelectedSlotIndex(int n) {
        this.selectedSlotIndex = n;
    }

    public ServerSelectionList(GuiMultiplayer guiMultiplayer, Minecraft minecraft, int n, int n2, int n3, int n4, int n5) {
        super(minecraft, n, n2, n3, n4, n5);
        this.field_148199_m = Lists.newArrayList();
        this.lanScanEntry = new ServerListEntryLanScan();
        this.owner = guiMultiplayer;
    }
}

