package me.crimp.claudius.mod.modules.text;

import me.crimp.claudius.mod.command.Command;
import me.crimp.claudius.mod.modules.Module;
import me.crimp.claudius.mod.setting.Setting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PopCounter
        extends Module {
        public Setting<Boolean> PopMsg = this.register(new Setting<>("Send in Chat", false));
        public static HashMap<String, Integer> TotemPopContainer = new HashMap();
        private Map<EntityPlayer, Integer> poplist = new ConcurrentHashMap<EntityPlayer, Integer>();

        public PopCounter() {
            super("PopCounter", "Counts other players totem pops.", Category.Text, false, false);
        }
        public static PopCounter INSTANCE = new PopCounter();

        @Override
        public void onEnable() {
            TotemPopContainer.clear();
            l_Count = 0;
        }


        public void onDeath(EntityPlayer player) {
            this.resetPops(player);
            if (TotemPopContainer.containsKey(player.getName())) {
                int l_Count = TotemPopContainer.get(player.getName());
                TotemPopContainer.remove(player.getName());
                if (this.enabled.getValue()) {

                    if (l_Count == 1) {
                        Command.sendMessage(player.getName() + " died after popping " + l_Count + " Totem");
                        if (this.PopMsg.getValue()) {
                            mc.player.connection.sendPacket(new CPacketChatMessage(player.getName() + " died after popping " + l_Count + " Totem, Thanks To The Power Of claudius"));
                        }
                        if (this.PopMsg.getValue()) mc.player.connection.sendPacket(new CPacketChatMessage(player.getName() + " died after popping " + l_Count + " Totem, Thanks To The Power Of claudius"));
                    } else {
                        Command.sendMessage(player.getName() + " died after popping " +  l_Count + " Totems");
                        if (this.PopMsg.getValue()) {
                            mc.player.connection.sendPacket(new CPacketChatMessage(player.getName() + " died after popping " +  l_Count + " Totems, Thanks To The Power Of claudius"));
                        }
                        if (this.PopMsg.getValue()) {
                            mc.player.connection.sendPacket(new CPacketChatMessage(player.getName() + " died after popping " + l_Count + " Totems, Thanks To The Power Of claudius"));
                        }
                    } l_Count = 0;
                }
            }
        }

        int l_Count = 0;
        public void onTotemPop(EntityPlayer player) {
            this.popTotem(player);
            if (PopCounter.fullNullCheck()) {
                return;
            }
            if (PopCounter.mc.player.equals(player)) {
                return;
            }
            int l_Count = 1;
            if (TotemPopContainer.containsKey(player.getName())) {
                l_Count = TotemPopContainer.get(player.getName());
                TotemPopContainer.put(player.getName(), ++l_Count);
            } else {
                TotemPopContainer.put(player.getName(), l_Count);
            }
            if (PopCounter.fullNullCheck() || PopCounter.mc.player.equals(player)) return;

            l_Count++;
            if (this.enabled.getValue()) {
                if (l_Count == 1) {
                    Command.sendMessage(player.getName() + " popped " + l_Count + " Totem.");
                    if (this.PopMsg.getValue()) {
                        mc.player.connection.sendPacket(new CPacketChatMessage(player.getName() + " popped " + l_Count + " Totem, Thanks To The Power Of claudius"));
                    }
                    if (this.PopMsg.getValue()) mc.player.connection.sendPacket(new CPacketChatMessage(player.getName() + " popped " + l_Count + " Totem, Thanks To The Power Of claudius"));
                } else {
                    Command.sendMessage(player.getName() + " popped " + l_Count  + " Totems.");
                    if (this.PopMsg.getValue()) {
                        mc.player.connection.sendPacket(new CPacketChatMessage(player.getName() + " popped " + l_Count  + " Totems, Thanks To The Power Of claudius"));
                    }
                    if (this.PopMsg.getValue()) mc.player.connection.sendPacket(new CPacketChatMessage(player.getName() + " popped " + l_Count  + " Totems, Thanks To The Power Of claudius"));
                }
            }
        }


    public void resetPops(EntityPlayer player) {
        this.setTotemPops(player, 0);
    }

    public void popTotem(EntityPlayer player) {
        this.poplist.merge(player, 1, Integer::sum);
    }

    public void setTotemPops(EntityPlayer player, int amount) {
        this.poplist.put(player, amount);
    }

    public int getTotemPops(EntityPlayer player) {
        Integer pops = this.poplist.get(player);
        if (pops == null) {
            return 0;
        }
        return pops;
    }

    public String getTotemPopString(EntityPlayer player) {
        return (this.getTotemPops(player) <= 0 ? "" : "-" + this.getTotemPops(player) + " ");
    }
}
