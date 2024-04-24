package me.crimp.claudius.mod.modules.misc;


import com.mojang.realmsclient.gui.ChatFormatting;
import me.crimp.claudius.claudius;
import me.crimp.claudius.event.events.PacketEvent;
import me.crimp.claudius.mod.command.Command;
import me.crimp.claudius.mod.modules.Module;
import me.crimp.claudius.mod.setting.Setting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AutoEmemy extends Module {
    public Setting<Boolean> Crashgame = register(new Setting<>("Add Msgs", false, "wiw"));
    public AutoEmemy() {
        super("AutoEnemy", "Automatic way of adding Players to enemy list. it adds when killed by them or you kill them", Category.Misc, false, true);
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketChat) {
            String text = ((SPacketChat) event.getPacket()).getChatComponent().getFormattedText();
            if (text.contains("by") || text.contains("got") || text.contains("fucked") || text.contains("shoved") || text.contains("whilst") ) {
                if (text.contains(mc.player.getName())) {
                    for (Object o : mc.world.loadedEntityList) {
                        if (o instanceof EntityPlayer && o != mc.player) {
                            EntityPlayer entity = (EntityPlayer) o;
                            String name = entity.getGameProfile().getName();

                            if (text.contains(name)) {
                                claudius.enemyManager.addEnemy(name);
                                if (Crashgame.getValue() && !mc.player.isDead) {
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    if (claudius.enemyManager.isEnemy(name)) {
                                        Command.sendMessage(name + " Is Now A " + ChatFormatting.RED + "Enemy");
                                    } else {
                                        Command.sendMessage(name + " Is Already A " + ChatFormatting.RED + "Enemy");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}