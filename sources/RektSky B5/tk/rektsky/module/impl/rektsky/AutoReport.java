/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.rektsky;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C02PacketUseEntity;
import tk.rektsky.Client;
import tk.rektsky.event.Event;
import tk.rektsky.event.impl.PacketSentEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.settings.BooleanSetting;

public class AutoReport
extends Module {
    ArrayList<String> reported = new ArrayList();
    private static Random random = new Random();
    public BooleanSetting automsg = new BooleanSetting("Auto message", false);

    public AutoReport() {
        super("AutoReport", "Automatically reports players", Category.REKTSKY);
    }

    @Override
    public void onEnable() {
        this.reported.clear();
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof PacketSentEvent && ((PacketSentEvent)event).getPacket() instanceof C02PacketUseEntity) {
            try {
                C02PacketUseEntity p2 = (C02PacketUseEntity)((PacketSentEvent)event).getPacket();
                Entity entity = p2.getEntityFromWorld(this.mc.theWorld);
                if (entity instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer)entity;
                    this.report(player.getName());
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    private void report(String username) {
        if (!this.reported.contains(username)) {
            this.reported.add(username);
            this.mc.getNetHandler().addToSendQueue(new C01PacketChatMessage("/report " + username));
            Client.addClientChat((Object)((Object)ChatFormatting.GREEN) + username + " has been reported to staff!");
            if (this.automsg.getValue().booleanValue()) {
                StringBuilder ks = new StringBuilder();
                int l2 = (int)(Math.floor(random.nextFloat() * 15.0f) + 3.0);
                for (int i2 = 0; i2 < l2; ++i2) {
                    ks.append("k");
                }
                this.mc.getNetHandler().addToSendQueue(new C01PacketChatMessage("/tell " + username + " lixo hack " + ks + "!"));
            }
        }
    }
}

