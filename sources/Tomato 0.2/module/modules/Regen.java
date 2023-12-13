/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.Cheese;
import cow.milkgod.cheese.commands.Command;
import cow.milkgod.cheese.commands.CommandManager;
import cow.milkgod.cheese.events.EventChatSend;
import cow.milkgod.cheese.events.EventPreMotionUpdates;
import cow.milkgod.cheese.events.EventTick;
import cow.milkgod.cheese.files.FileManager;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.properties.Property;
import cow.milkgod.cheese.utils.Logger;
import cow.milkgod.cheese.utils.Timer;
import cow.milkgod.cheese.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.FoodStats;

public class Regen
extends Module {
    public static Property<Double> health;
    public static Property<Integer> packets;
    private Timer timer = new Timer();

    public Regen() {
        super("Regen", 0, Category.COMBAT, 16777215, true, "Regenerates faster, duh.", new String[]{"reg"});
        health = new Property<Double>(this, "health", 14.0);
        packets = new Property<Integer>(this, "packets", 2);
    }

    @EventTarget
    public void onPre(EventPreMotionUpdates e2) {
        int i2;
        if ((Integer)Regen.packets.value < 10 && Wrapper.mc.thePlayer.getHealth() < Wrapper.mc.thePlayer.getMaxHealth() && this.timer.hasTimeElapsed(160, true)) {
            i2 = 1;
            while (i2 < (Integer)Regen.packets.value + 1) {
                Wrapper.sendPacket(new C03PacketPlayer());
                ++i2;
            }
        }
        if (Wrapper.mc.thePlayer.getHealth() < Wrapper.mc.thePlayer.getMaxHealth() && (Integer)Regen.packets.value == 69 && Wrapper.mc.thePlayer.getActivePotionEffect(Potion.regeneration) != null) {
            i2 = 0;
            while (i2 < 5) {
                Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(Wrapper.mc.thePlayer.onGround));
                ++i2;
            }
        }
        if (Wrapper.mc.thePlayer.getHealth() < Wrapper.mc.thePlayer.getMaxHealth() && (Integer)Regen.packets.value == 70) {
            i2 = 1;
            while ((float)i2 < Wrapper.mc.thePlayer.getMaxHealth() - Wrapper.mc.thePlayer.getHealth()) {
                Wrapper.sendPacket(new C03PacketPlayer.C05PacketPlayerLook(0.0f, 0.0f, true));
                Wrapper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.mc.thePlayer.posX, Wrapper.mc.thePlayer.posY - 0.05, Wrapper.mc.thePlayer.posZ, true));
                ++i2;
            }
        }
        int index = 0;
        while (index < (Integer)Regen.packets.value) {
            if (Wrapper.mc.thePlayer.onGround && Wrapper.mc.thePlayer.getHealth() <= (float)((Double)Regen.health.value).intValue() && Wrapper.mc.thePlayer.isEntityAlive() && Wrapper.mc.thePlayer.getFoodStats().getFoodLevel() > 10 && (Integer)Regen.packets.value > 10 && (Integer)Regen.packets.value != 69 && (Integer)Regen.packets.value != 70) {
                if (Wrapper.mc.thePlayer.onGround) {
                    Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
                } else {
                    Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
                }
            }
            ++index;
        }
    }

    @Override
    protected void addCommand() {
        Cheese.getInstance();
        CommandManager commandManager = Cheese.commandManager;
        CommandManager.addCommand(new Command("Regen", "Unknown Option! ", null, "<health | packets | values>", "Manages Regen"){

            @EventTarget
            public void onTick(EventTick e2) {
                block16 : {
                    try {
                        boolean old = (Integer)Regen.packets.value > 10;
                        String mode = EventChatSend.getMessage().split(" ")[1];
                        if (!mode.equalsIgnoreCase("health") && !mode.equalsIgnoreCase("hp")) {
                            if (!mode.equalsIgnoreCase("packets") && !mode.equalsIgnoreCase("pc")) {
                                if (mode.equalsIgnoreCase("actual") || mode.equalsIgnoreCase("values")) {
                                    Logger.logChat("Health: \u00a7e" + String.valueOf(Regen.health.getValue()));
                                    Logger.logChat("Packets: \u00a7e" + String.valueOf(Regen.packets.getValue()));
                                    Logger.logChat("Actual mode: " + (old ? "\u00a7eOld" : "\u00a7eNew"));
                                } else {
                                    Logger.logChat(String.valueOf(String.valueOf(this.getErrorMessage())) + this.getArguments());
                                }
                                break block16;
                            }
                            try {
                                String packetse = EventChatSend.getMessage().split(" ")[2];
                                if (packetse.equalsIgnoreCase("actual")) {
                                    Logger.logChat("Actual packets: \u00a7e" + String.valueOf(Regen.packets.getValue()));
                                    break block16;
                                }
                                if (packetse.equalsIgnoreCase("reset")) {
                                    Regen.packets.setValue(Regen.packets.getDefaultValue());
                                    Logger.logChat("Regen's packets reset. Set packets to: \u00a7e" + Regen.packets.getValue());
                                    break block16;
                                }
                                int pakets = Integer.parseInt(packetse);
                                if (pakets < 1) {
                                    pakets = 1;
                                }
                                Regen.packets.setValue(pakets);
                                Logger.logChat("Regen's packets set to: \u00a7e" + Regen.packets.getValue());
                            }
                            catch (Exception ex) {
                                Logger.logChat("Invalid Value! Packets <actual | reset || Packets>");
                            }
                            break block16;
                        }
                        try {
                            String delayerino = EventChatSend.getMessage().split(" ")[2];
                            if (delayerino.equalsIgnoreCase("actual")) {
                                Logger.logChat("Actual health: \u00a7e" + String.valueOf(Regen.health.getValue()));
                                break block16;
                            }
                            if (delayerino.equalsIgnoreCase("reset")) {
                                Regen.health.setValue(Regen.health.getDefaultValue());
                                Logger.logChat("Regen's health reset. Set health to: \u00a7e" + Regen.health.getValue());
                                break block16;
                            }
                            Regen.health.setValue(Double.parseDouble(delayerino));
                            if (Regen.health.getValue() > 20.0) {
                                Regen.health.setValue(20.0);
                                Logger.logChat("Cannot make the delay higher than 25!");
                            }
                            Logger.logChat("Regen's health set to: \u00a7e" + Regen.health.getValue());
                        }
                        catch (Exception ex) {
                            Logger.logChat("Invalid Value! Health <actual | reset || health>");
                        }
                    }
                    catch (Exception ex2) {
                        Logger.logChat(String.valueOf(String.valueOf(this.getErrorMessage())) + this.getArguments());
                    }
                }
                Cheese.getInstance();
                Cheese.fileManager.saveFiles();
                this.Toggle();
            }
        });
    }

}

