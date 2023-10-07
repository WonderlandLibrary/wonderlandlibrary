/**
 * Time: 5:29:22 AM
 * Date: Dec 29, 2016
 * Creator: cool1
 */
package info.sigmaclient.module.impl.combat;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.management.command.Command;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.misc.ChatUtil;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.util.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class AntiBot extends Module {

    public static String MODE = "MODE";
    private String DEAD = "DEAD";

    private info.sigmaclient.util.Timer timer = new info.sigmaclient.util.Timer();

    public AntiBot(ModuleData data) {
        super(data);
        settings.put(DEAD, new Setting<>(DEAD, true, "Removes dead bodies from the game."));
        settings.put(MODE, new Setting<>(MODE, new Options("Mode", "Hypixel", new String[]{"Hypixel", "Packet"}), "Check method for bots."));
    }

    public static List<EntityPlayer> getInvalid() {
        return invalid;
    }

    private static List<EntityPlayer> invalid = new ArrayList<>();

    public void onEnable() {
        invalid.clear();
    }

    public void onDisable() {
        invalid.clear();
        if (mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel") || mc.getCurrentServerData().serverIP.toLowerCase().contains("mineplex")) {
            ChatUtil.printChat(Command.chatPrefix + "AntiBot was kept enabled for your protection.");
            toggle();
        }
    }

    @Override
    @RegisterEvent(events = {EventPacket.class, EventUpdate.class})
    public void onEvent(Event event) {
        String currentSetting = ((Options) settings.get(MODE).getValue()).getSelected();
        if (event instanceof EventPacket && currentSetting.equalsIgnoreCase("Packet")) {
            EventPacket ep = (EventPacket) event;
            if (ep.isIncoming() && ep.getPacket() instanceof S0CPacketSpawnPlayer) {
                S0CPacketSpawnPlayer packet = (S0CPacketSpawnPlayer) ep.getPacket();
                double entX = packet.func_148942_f() / 32;
                double entY = packet.func_148949_g() / 32;
                double entZ = packet.func_148946_h() / 32;
                double posX = mc.thePlayer.posX;
                double posY = mc.thePlayer.posY;
                double posZ = mc.thePlayer.posZ;
                double var7 = posX - entX;
                double var9 = posY - entY;
                double var11 = posZ - entZ;
                float distance = MathHelper.sqrt_double(var7 * var7 + var9 * var9 + var11 * var11);
                if (distance <= 17 && entY > mc.thePlayer.posY + 1 && (mc.thePlayer.posX != entX && mc.thePlayer.posY != entY && mc.thePlayer.posZ != entZ)) {
                    ep.setCancelled(true);
                }
            }
        }
        if (event instanceof EventUpdate) {
            EventUpdate em = (EventUpdate) event;
            setSuffix(currentSetting);
            if (em.isPre()) {
                if (mc.getIntegratedServer() == null) {
                    if (mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel") && !currentSetting.equals("Hypixel")) {
                        ((Options) settings.get(MODE).getValue()).setSelected("Hypixel");
                        ChatUtil.printChat(Command.chatPrefix + "AntiBot has been set to the proper mode.");
                    } else if (mc.getCurrentServerData().serverIP.toLowerCase().contains("mineplex") && !currentSetting.equals("Packet")) {
                        ((Options) settings.get(MODE).getValue()).setSelected("Packet");
                        ChatUtil.printChat(Command.chatPrefix + "AntiBot has been set to the proper mode.");
                    }
                }
                if (((Boolean) settings.get(DEAD).getValue()))
                    for (Object o : mc.theWorld.loadedEntityList) {
                        if (o instanceof EntityPlayer) {
                            EntityPlayer ent = (EntityPlayer) o;
                            assert ent != mc.thePlayer;
                            if (ent.isPlayerSleeping()) {
                                mc.theWorld.removeEntity(ent);
                            }
                        }
                    }
            }
            if (em.isPre() && !currentSetting.equalsIgnoreCase("Packet")) {
                //Clears the invalid player list after a second to prevent false positives staying permanent.

                if (!invalid.isEmpty() && timer.delay(1000)) {
                    invalid.clear();
                    timer.reset();
                }
                // Loop through entity list
                for (Object o : mc.theWorld.getLoadedEntityList()) {
                    if (o instanceof EntityPlayer) {
                        EntityPlayer ent = (EntityPlayer) o;
                        //Make sure it's not the local player + they are in a worrying distance. Ignore them if they're already invalid.
                        if (ent != mc.thePlayer && mc.thePlayer.getDistanceToEntity(ent) < 10 && !invalid.contains(ent)) {
                            //Handle current mode
                            switch (currentSetting) {
                                case "Hypixel": {
                                    //Get formatted name
                                    String str = ent.getDisplayName().getFormattedText();
                                    //if the formatted name is equal to a default name "name + \247r" or the name contains "NPC" or they're not in the tab list, remove the entity.
                                    if ((str.equalsIgnoreCase(ent.getName() + "\247r") || str.contains("NPC")) || (!getTabPlayerList().contains(ent))) {
                                        timer.reset();
                                        invalid.add(ent);
                                    }
                                    break;
                                }

                                //Experimental, removed until further testing is done.
                                case "Mineplex": {
                                    if (ent.onGround) {
                                        ent.onGroundTicks++;
                                    }
                                    if (ent.onGroundTicks < 3) {
                                        timer.reset();
                                        invalid.add(ent);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    public static List<EntityPlayer> getTabPlayerList() {
        final NetHandlerPlayClient var4 = mc.thePlayer.sendQueue;
        final List<EntityPlayer> list = new ArrayList<>();
        final List players = GuiPlayerTabOverlay.field_175252_a.sortedCopy(var4.func_175106_d());
        for (final Object o : players) {
            final NetworkPlayerInfo info = (NetworkPlayerInfo) o;
            if (info == null) {
                continue;
            }
            list.add(mc.theWorld.getPlayerEntityByName(info.getGameProfile().getName()));
        }
        return list;
    }

}
