// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui.spectator.categories;

import com.google.common.collect.ComparisonChain;
import java.util.Comparator;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSpectator;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import java.util.Iterator;
import net.minecraft.client.gui.spectator.PlayerMenuObject;
import net.minecraft.world.WorldSettings;
import net.minecraft.client.network.NetworkPlayerInfo;
import com.google.common.collect.Lists;
import java.util.Collection;
import net.minecraft.client.Minecraft;
import java.util.List;
import com.google.common.collect.Ordering;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.ISpectatorMenuView;

public class TeleportToPlayer implements ISpectatorMenuView, ISpectatorMenuObject
{
    private static final Ordering field_178674_a;
    private final List field_178673_b;
    private static final String __OBFID = "CL_00001922";
    
    public TeleportToPlayer() {
        this(TeleportToPlayer.field_178674_a.sortedCopy((Iterable)Minecraft.getMinecraft().getNetHandler().func_175106_d()));
    }
    
    public TeleportToPlayer(final Collection p_i45493_1_) {
        this.field_178673_b = Lists.newArrayList();
        for (final NetworkPlayerInfo var3 : TeleportToPlayer.field_178674_a.sortedCopy((Iterable)p_i45493_1_)) {
            if (var3.getGameType() != WorldSettings.GameType.SPECTATOR) {
                this.field_178673_b.add(new PlayerMenuObject(var3.getGameProfile()));
            }
        }
    }
    
    @Override
    public List func_178669_a() {
        return this.field_178673_b;
    }
    
    @Override
    public IChatComponent func_178670_b() {
        return new ChatComponentText("Select a player to teleport to");
    }
    
    @Override
    public void func_178661_a(final SpectatorMenu p_178661_1_) {
        p_178661_1_.func_178647_a(this);
    }
    
    @Override
    public IChatComponent func_178664_z_() {
        return new ChatComponentText("Teleport to player");
    }
    
    @Override
    public void func_178663_a(final float p_178663_1_, final int p_178663_2_) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(GuiSpectator.field_175269_a);
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, 16, 16, 256.0f, 256.0f);
    }
    
    @Override
    public boolean func_178662_A_() {
        return !this.field_178673_b.isEmpty();
    }
    
    static {
        field_178674_a = Ordering.from((Comparator)new Comparator() {
            private static final String __OBFID = "CL_00001921";
            
            public int func_178746_a(final NetworkPlayerInfo p_178746_1_, final NetworkPlayerInfo p_178746_2_) {
                return ComparisonChain.start().compare((Comparable)p_178746_1_.getGameProfile().getId(), (Comparable)p_178746_2_.getGameProfile().getId()).result();
            }
            
            @Override
            public int compare(final Object p_compare_1_, final Object p_compare_2_) {
                return this.func_178746_a((NetworkPlayerInfo)p_compare_1_, (NetworkPlayerInfo)p_compare_2_);
            }
        });
    }
}
