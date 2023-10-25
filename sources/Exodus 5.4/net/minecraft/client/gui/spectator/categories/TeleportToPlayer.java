/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ComparisonChain
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Ordering
 */
package net.minecraft.client.gui.spectator.categories;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSpectator;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.ISpectatorMenuView;
import net.minecraft.client.gui.spectator.PlayerMenuObject;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.WorldSettings;

public class TeleportToPlayer
implements ISpectatorMenuObject,
ISpectatorMenuView {
    private static final Ordering<NetworkPlayerInfo> field_178674_a = Ordering.from((Comparator)new Comparator<NetworkPlayerInfo>(){

        @Override
        public int compare(NetworkPlayerInfo networkPlayerInfo, NetworkPlayerInfo networkPlayerInfo2) {
            return ComparisonChain.start().compare((Comparable)networkPlayerInfo.getGameProfile().getId(), (Comparable)networkPlayerInfo2.getGameProfile().getId()).result();
        }
    });
    private final List<ISpectatorMenuObject> field_178673_b = Lists.newArrayList();

    public TeleportToPlayer(Collection<NetworkPlayerInfo> collection) {
        for (NetworkPlayerInfo networkPlayerInfo : field_178674_a.sortedCopy(collection)) {
            if (networkPlayerInfo.getGameType() == WorldSettings.GameType.SPECTATOR) continue;
            this.field_178673_b.add(new PlayerMenuObject(networkPlayerInfo.getGameProfile()));
        }
    }

    @Override
    public void func_178661_a(SpectatorMenu spectatorMenu) {
        spectatorMenu.func_178647_a(this);
    }

    public TeleportToPlayer() {
        this(field_178674_a.sortedCopy(Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()));
    }

    @Override
    public void func_178663_a(float f, int n) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(GuiSpectator.field_175269_a);
        Gui.drawModalRectWithCustomSizedTexture(0.0f, 0.0f, 0.0f, 0.0f, 16, 16, 256.0f, 256.0f);
    }

    @Override
    public boolean func_178662_A_() {
        return !this.field_178673_b.isEmpty();
    }

    @Override
    public IChatComponent getSpectatorName() {
        return new ChatComponentText("Teleport to player");
    }

    @Override
    public List<ISpectatorMenuObject> func_178669_a() {
        return this.field_178673_b;
    }

    @Override
    public IChatComponent func_178670_b() {
        return new ChatComponentText("Select a player to teleport to");
    }
}

