// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.other;

import exhibition.event.RegisterEvent;
import java.util.Iterator;
import exhibition.Client;
import net.minecraft.entity.player.EntityPlayer;
import exhibition.util.misc.ChatUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import exhibition.management.friend.Friend;
import exhibition.management.friend.FriendManager;
import net.minecraft.network.play.server.S02PacketChat;
import exhibition.event.impl.EventPacket;
import exhibition.util.misc.PathFind;
import exhibition.event.impl.EventMotion;
import exhibition.event.Event;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class CMDBot extends Module
{
    private boolean following;
    private String followName;
    
    public CMDBot(final ModuleData data) {
        super(data);
    }
    
    @RegisterEvent(events = { EventPacket.class, EventMotion.class })
    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventMotion) {
            final EventMotion e = (EventMotion)event;
            if (e.isPre()) {
                if (CMDBot.mc.thePlayer.isDead) {
                    CMDBot.mc.thePlayer.respawnPlayer();
                }
                if (this.following) {
                    try {
                        final PathFind pf = new PathFind(this.followName);
                        e.setPitch(PathFind.fakePitch - 30.0f);
                        e.setYaw(PathFind.fakeYaw);
                    }
                    catch (Exception ex) {}
                }
            }
        }
        if (event instanceof EventPacket) {
            final EventPacket ep = (EventPacket)event;
            if (ep.isIncoming() && ep.getPacket() instanceof S02PacketChat) {
                final S02PacketChat message = (S02PacketChat)ep.getPacket();
                if (message.func_148915_c().getFormattedText().contains("-follow")) {
                    for (final Friend friend : FriendManager.friendsList) {
                        if (message.func_148915_c().getFormattedText().contains(friend.name)) {
                            String s = message.func_148915_c().getFormattedText();
                            s = s.substring(s.indexOf("-follow ") + 8);
                            s = s.substring(0, s.indexOf("§"));
                            this.following = true;
                            this.followName = s;
                            break;
                        }
                    }
                }
                if (message.func_148915_c().getFormattedText().contains("-stopfollow")) {
                    for (final Friend friend : FriendManager.friendsList) {
                        if (message.func_148915_c().getFormattedText().contains(friend.name)) {
                            String s = message.func_148915_c().getFormattedText();
                            s = s.substring(s.indexOf("-stopfollow ") + 12);
                            s = s.substring(0, s.indexOf("§"));
                            this.following = false;
                            this.followName = "";
                            break;
                        }
                    }
                }
                if (message.func_148915_c().getFormattedText().contains("-amandatodd")) {
                    for (final Friend friend : FriendManager.friendsList) {
                        if (message.func_148915_c().getFormattedText().contains(friend.name)) {
                            for (int index = 0; index < 81; ++index) {
                                CMDBot.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(CMDBot.mc.thePlayer.posX, CMDBot.mc.thePlayer.posY + 20.0, CMDBot.mc.thePlayer.posZ, false));
                                CMDBot.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(CMDBot.mc.thePlayer.posX, CMDBot.mc.thePlayer.posY, CMDBot.mc.thePlayer.posZ, false));
                            }
                            break;
                        }
                    }
                }
                if (message.func_148915_c().getFormattedText().contains("-tpahere")) {
                    for (final Friend friend : FriendManager.friendsList) {
                        if (message.func_148915_c().getFormattedText().contains(friend.name)) {
                            ChatUtil.sendChat("/tpa " + friend.name);
                            break;
                        }
                    }
                }
                if (message.func_148915_c().getFormattedText().contains("-friend ")) {
                    for (final Object o : CMDBot.mc.theWorld.getLoadedEntityList()) {
                        if (o instanceof EntityPlayer) {
                            final EntityPlayer mod = (EntityPlayer)o;
                            if (!message.func_148915_c().getFormattedText().contains("-friend " + mod.getName())) {
                                continue;
                            }
                            for (final Friend friend2 : FriendManager.friendsList) {
                                if (message.func_148915_c().getFormattedText().contains(friend2.name)) {
                                    if (FriendManager.isFriend(mod.getName())) {
                                        ChatUtil.sendChat(mod.getName() + " is already a friend.");
                                        break;
                                    }
                                    if (!FriendManager.isFriend(mod.getName())) {
                                        ChatUtil.sendChat(mod.getName() + " has been friended.");
                                        FriendManager.addFriend(mod.getName(), mod.getName());
                                        break;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
                if (message.func_148915_c().getFormattedText().contains("-friendremove ")) {
                    for (final Object o : CMDBot.mc.theWorld.getLoadedEntityList()) {
                        if (o instanceof EntityPlayer) {
                            final EntityPlayer mod = (EntityPlayer)o;
                            if (!message.func_148915_c().getFormattedText().contains("-friendremove " + mod.getName())) {
                                continue;
                            }
                            for (final Friend friend2 : FriendManager.friendsList) {
                                if (message.func_148915_c().getFormattedText().contains(friend2.name)) {
                                    if (FriendManager.isFriend(mod.getName())) {
                                        FriendManager.removeFriend(mod.getName());
                                        ChatUtil.sendChat(mod.getName() + " has been removed from friends.");
                                        break;
                                    }
                                    if (!FriendManager.isFriend(mod.getName())) {
                                        ChatUtil.sendChat(mod.getName() + " is not friended.");
                                        break;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
                if (message.func_148915_c().getFormattedText().contains("-toggle ")) {
                    for (final Module mod2 : Client.getModuleManager().getArray()) {
                        if (message.func_148915_c().getFormattedText().contains("-toggle " + mod2.getName())) {
                            for (final Friend friend3 : FriendManager.friendsList) {
                                if (message.func_148915_c().getFormattedText().contains(friend3.name)) {
                                    mod2.toggle();
                                    final boolean state = mod2.isEnabled();
                                    final String s2 = state ? "On" : "Off";
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
