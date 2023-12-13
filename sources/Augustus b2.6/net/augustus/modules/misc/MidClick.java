// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.misc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.augustus.events.EventMidClick;
import net.lenni0451.eventapi.reflection.EventTarget;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.augustus.events.EventTick;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.BooleanValue;
import java.util.ArrayList;
import net.augustus.modules.Module;

public class MidClick extends Module
{
    public static ArrayList<String> friends;
    private final ArrayList<String> backUpFriends;
    private final BooleanValue autoUnfriend;
    public boolean noFiends;
    
    public MidClick() {
        super("MidClick", new Color(76, 78, 76), Categorys.MISC);
        this.backUpFriends = new ArrayList<String>();
        this.autoUnfriend = new BooleanValue(1, "AutoUnfriend", this, true);
        this.noFiends = false;
    }
    
    @Override
    public void onEnable() {
        MidClick.friends = new ArrayList<String>();
    }
    
    @EventTarget
    public void onEventTick(final EventTick eventTick) {
        boolean b = true;
        if (this.autoUnfriend.getBoolean()) {
            b = false;
            final NetHandlerPlayClient nethandlerplayclient = MidClick.mc.thePlayer.sendQueue;
            final List<NetworkPlayerInfo> list = GuiPlayerTabOverlay.field_175252_a.sortedCopy(nethandlerplayclient.getPlayerInfoMap());
            for (final NetworkPlayerInfo networkplayerinfo : list) {
                final String string = networkplayerinfo.getGameProfile().getName();
                if (!MidClick.friends.contains(string) && !MidClick.mc.thePlayer.getName().equals(string)) {
                    b = true;
                    break;
                }
            }
        }
        this.noFiends = !b;
    }
    
    @EventTarget
    public void onEventMidClick(final EventMidClick eventMidClick) {
        if (MidClick.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && MidClick.mc.objectMouseOver.entityHit instanceof EntityPlayer) {
            if (MidClick.friends.contains(MidClick.mc.objectMouseOver.entityHit.getName())) {
                MidClick.friends.remove(MidClick.mc.objectMouseOver.entityHit.getName());
            }
            else {
                MidClick.friends.add(MidClick.mc.objectMouseOver.entityHit.getName());
            }
        }
    }
    
    @Override
    public void onDisable() {
        MidClick.friends = new ArrayList<String>();
    }
    
    static {
        MidClick.friends = new ArrayList<String>();
    }
}
