// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import java.util.Iterator;
import net.minecraft.client.network.LanServerDetector;
import net.minecraft.client.multiplayer.ServerList;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import java.util.List;

public class ServerSelectionList extends GuiListExtended
{
    private final GuiMultiplayer owner;
    private final List<ServerListEntryNormal> serverListInternet;
    private final List<ServerListEntryLanDetected> serverListLan;
    private final IGuiListEntry lanScanEntry;
    private int selectedSlotIndex;
    
    public ServerSelectionList(final GuiMultiplayer ownerIn, final Minecraft mcIn, final int widthIn, final int heightIn, final int topIn, final int bottomIn, final int slotHeightIn) {
        super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
        this.serverListInternet = (List<ServerListEntryNormal>)Lists.newArrayList();
        this.serverListLan = (List<ServerListEntryLanDetected>)Lists.newArrayList();
        this.lanScanEntry = new ServerListEntryLanScan();
        this.selectedSlotIndex = -1;
        this.owner = ownerIn;
    }
    
    @Override
    public IGuiListEntry getListEntry(int index) {
        if (index < this.serverListInternet.size()) {
            return this.serverListInternet.get(index);
        }
        index -= this.serverListInternet.size();
        if (index == 0) {
            return this.lanScanEntry;
        }
        --index;
        return this.serverListLan.get(index);
    }
    
    @Override
    protected int getSize() {
        return this.serverListInternet.size() + 1 + this.serverListLan.size();
    }
    
    public void setSelectedSlotIndex(final int selectedSlotIndexIn) {
        this.selectedSlotIndex = selectedSlotIndexIn;
    }
    
    @Override
    protected boolean isSelected(final int slotIndex) {
        return slotIndex == this.selectedSlotIndex;
    }
    
    public int func_148193_k() {
        return this.selectedSlotIndex;
    }
    
    public void func_148195_a(final ServerList p_148195_1_) {
        this.serverListInternet.clear();
        for (int i = 0; i < p_148195_1_.countServers(); ++i) {
            this.serverListInternet.add(new ServerListEntryNormal(this.owner, p_148195_1_.getServerData(i)));
        }
    }
    
    public void func_148194_a(final List<LanServerDetector.LanServer> p_148194_1_) {
        this.serverListLan.clear();
        for (final LanServerDetector.LanServer lanserverdetector$lanserver : p_148194_1_) {
            this.serverListLan.add(new ServerListEntryLanDetected(this.owner, lanserverdetector$lanserver));
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
}
