package net.minecraft.client.gui.spectator.categories;

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
implements ISpectatorMenuView,
ISpectatorMenuObject {
    private static final Ordering<NetworkPlayerInfo> field_178674_a = Ordering.from((Comparator)new /* Unavailable Anonymous Inner Class!! */);
    private final List<ISpectatorMenuObject> field_178673_b = Lists.newArrayList();

    public TeleportToPlayer() {
        this((Collection<NetworkPlayerInfo>)field_178674_a.sortedCopy((Iterable)Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()));
    }

    public TeleportToPlayer(Collection<NetworkPlayerInfo> p_i45493_1_) {
        for (NetworkPlayerInfo networkplayerinfo : field_178674_a.sortedCopy(p_i45493_1_)) {
            if (networkplayerinfo.getGameType() == WorldSettings.GameType.SPECTATOR) continue;
            this.field_178673_b.add((Object)new PlayerMenuObject(networkplayerinfo.getGameProfile()));
        }
    }

    public List<ISpectatorMenuObject> func_178669_a() {
        return this.field_178673_b;
    }

    public IChatComponent func_178670_b() {
        return new ChatComponentText("Select a player to teleport to");
    }

    public void func_178661_a(SpectatorMenu menu) {
        menu.func_178647_a((ISpectatorMenuView)this);
    }

    public IChatComponent getSpectatorName() {
        return new ChatComponentText("Teleport to player");
    }

    public void func_178663_a(float p_178663_1_, int alpha) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(GuiSpectator.field_175269_a);
        Gui.drawModalRectWithCustomSizedTexture((int)0, (int)0, (float)0.0f, (float)0.0f, (int)16, (int)16, (float)256.0f, (float)256.0f);
    }

    public boolean func_178662_A_() {
        return !this.field_178673_b.isEmpty();
    }
}
