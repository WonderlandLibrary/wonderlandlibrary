// 
// Decompiled by Procyon v0.5.30
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
    private final List field_148198_l;
    private final List field_148199_m;
    private final IGuiListEntry lanScanEntry;
    private int field_148197_o;
    private static final String __OBFID = "CL_00000819";
    
    public ServerSelectionList(final GuiMultiplayer p_i45049_1_, final Minecraft mcIn, final int p_i45049_3_, final int p_i45049_4_, final int p_i45049_5_, final int p_i45049_6_, final int p_i45049_7_) {
        super(mcIn, p_i45049_3_, p_i45049_4_, p_i45049_5_, p_i45049_6_, p_i45049_7_);
        this.field_148198_l = Lists.newArrayList();
        this.field_148199_m = Lists.newArrayList();
        this.lanScanEntry = new ServerListEntryLanScan();
        this.field_148197_o = -1;
        this.owner = p_i45049_1_;
    }
    
    @Override
    public IGuiListEntry getListEntry(int p_148180_1_) {
        if (p_148180_1_ < this.field_148198_l.size()) {
            return this.field_148198_l.get(p_148180_1_);
        }
        p_148180_1_ -= this.field_148198_l.size();
        if (p_148180_1_ == 0) {
            return this.lanScanEntry;
        }
        --p_148180_1_;
        return this.field_148199_m.get(p_148180_1_);
    }
    
    @Override
    protected int getSize() {
        return this.field_148198_l.size() + 1 + this.field_148199_m.size();
    }
    
    public void func_148192_c(final int p_148192_1_) {
        this.field_148197_o = p_148192_1_;
    }
    
    @Override
    protected boolean isSelected(final int slotIndex) {
        return slotIndex == this.field_148197_o;
    }
    
    public int func_148193_k() {
        return this.field_148197_o;
    }
    
    public void func_148195_a(final ServerList p_148195_1_) {
        this.field_148198_l.clear();
        for (int var2 = 0; var2 < p_148195_1_.countServers(); ++var2) {
            this.field_148198_l.add(new ServerListEntryNormal(this.owner, p_148195_1_.getServerData(var2)));
        }
    }
    
    public void func_148194_a(final List p_148194_1_) {
        this.field_148199_m.clear();
        for (final LanServerDetector.LanServer var3 : p_148194_1_) {
            this.field_148199_m.add(new ServerListEntryLanDetected(this.owner, var3));
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
