// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.spectator.categories;

import com.google.common.collect.ComparisonChain;
import java.util.Comparator;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSpectator;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.ITextComponent;
import java.util.Iterator;
import net.minecraft.client.gui.spectator.PlayerMenuObject;
import net.minecraft.world.GameType;
import com.google.common.collect.Lists;
import java.util.Collection;
import net.minecraft.client.Minecraft;
import java.util.List;
import net.minecraft.client.network.NetworkPlayerInfo;
import com.google.common.collect.Ordering;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.ISpectatorMenuView;

public class TeleportToPlayer implements ISpectatorMenuView, ISpectatorMenuObject
{
    private static final Ordering<NetworkPlayerInfo> PROFILE_ORDER;
    private final List<ISpectatorMenuObject> items;
    
    public TeleportToPlayer() {
        this(TeleportToPlayer.PROFILE_ORDER.sortedCopy((Iterable)Minecraft.getMinecraft().getConnection().getPlayerInfoMap()));
    }
    
    public TeleportToPlayer(final Collection<NetworkPlayerInfo> profiles) {
        this.items = (List<ISpectatorMenuObject>)Lists.newArrayList();
        for (final NetworkPlayerInfo networkplayerinfo : TeleportToPlayer.PROFILE_ORDER.sortedCopy((Iterable)profiles)) {
            if (networkplayerinfo.getGameType() != GameType.SPECTATOR) {
                this.items.add(new PlayerMenuObject(networkplayerinfo.getGameProfile()));
            }
        }
    }
    
    @Override
    public List<ISpectatorMenuObject> getItems() {
        return this.items;
    }
    
    @Override
    public ITextComponent getPrompt() {
        return new TextComponentTranslation("spectatorMenu.teleport.prompt", new Object[0]);
    }
    
    @Override
    public void selectItem(final SpectatorMenu menu) {
        menu.selectCategory(this);
    }
    
    @Override
    public ITextComponent getSpectatorName() {
        return new TextComponentTranslation("spectatorMenu.teleport", new Object[0]);
    }
    
    @Override
    public void renderIcon(final float brightness, final int alpha) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(GuiSpectator.SPECTATOR_WIDGETS);
        Gui.drawModalRectWithCustomSizedTexture(0.0f, 0.0f, 0.0f, 0.0f, 16.0f, 16.0f, 256.0f, 256.0f);
    }
    
    @Override
    public boolean isEnabled() {
        return !this.items.isEmpty();
    }
    
    static {
        PROFILE_ORDER = Ordering.from((Comparator)new Comparator<NetworkPlayerInfo>() {
            @Override
            public int compare(final NetworkPlayerInfo p_compare_1_, final NetworkPlayerInfo p_compare_2_) {
                return ComparisonChain.start().compare((Comparable)p_compare_1_.getGameProfile().getId(), (Comparable)p_compare_2_.getGameProfile().getId()).result();
            }
        });
    }
}
