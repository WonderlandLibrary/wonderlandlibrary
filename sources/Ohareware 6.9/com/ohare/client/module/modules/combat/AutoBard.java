package com.ohare.client.module.modules.combat;

import com.ohare.client.Client;
import com.ohare.client.event.events.player.UpdateEvent;
import com.ohare.client.event.events.world.PacketEvent;
import com.ohare.client.module.Module;
import com.ohare.client.utils.MathUtils;
import com.ohare.client.utils.TimerUtil;
import com.ohare.client.utils.value.impl.NumberValue;

import dorkbox.messageBus.annotations.Subscribe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S3BPacketScoreboardObjective;
import net.minecraft.potion.Potion;
import net.minecraft.scoreboard.Score;

public class AutoBard extends Module {
    private NumberValue<Integer> delay = new NumberValue("Delay", 6000, 0, 20000, 10);
    private NumberValue<Integer> minenergy = new NumberValue("MinEnergy", 41, 0, 100, 5);
    
    private TimerUtil timer = new TimerUtil();
    private TimerUtil usetimer = new TimerUtil();
    
    private int energy, lastitem;
    public static int stage;
    
    public AutoBard() {
        super("AutoBard", Category.COMBAT, 0xDE981F);
        setDescription("Automatically uses the strength effect");
        setRenderlabel("Auto Bard");
        addValues(delay, minenergy);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (!event.isPre()) {
            KillAura killAura = (KillAura) Client.INSTANCE.getModuleManager().getModule("killaura");
            if (killAura.target instanceof EntityPlayer && canStrength() && energy >= minenergy.getValue() && timer.sleep(delay.getValue()) && stage == 0) {
                final int powderSlot = getPowderSlot();
                if (powderSlot != -1) {
                    lastitem = mc.thePlayer.inventory.currentItem;
                    mc.thePlayer.inventory.currentItem = powderSlot;
                    mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(powderSlot));
                    ++stage;
                }
            }
            switch (stage) {
                case 1:
                    if (usetimer.sleep(MathUtils.getRandom(115, 200))) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                        ++stage;
                    }
                    break;
                case 2:
                    if (usetimer.sleep(MathUtils.getRandom(200, 350))) {
                        mc.thePlayer.inventory.currentItem = lastitem;
                        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(lastitem));
                        stage = 0;
                    }
                    break;
            }
        }
    }

    @Subscribe
    public void handle(PacketEvent event) {
        if (!event.isSending() && event.getPacket() instanceof S3BPacketScoreboardObjective) {
            S3BPacketScoreboardObjective packet = (S3BPacketScoreboardObjective) event.getPacket();
            for (Score s : mc.theWorld.getScoreboard().getSortedScores(mc.theWorld.getScoreboard().getObjective(packet.func_149339_c()))) {
                if (s.getPlayerName().equals("§eEnergy")) {
                    energy = Integer.parseInt(mc.theWorld.getScoreboard().getPlayersTeam(s.getPlayerName()).getColorSuffix().split("§7: §c")[1]);
                    break;
                }
            }
        }
    }

    @Override
    public void onEnable() {
       stage = 0;
    }

    private boolean canStrength() {
        return mc.thePlayer.isPotionActive(Potion.damageBoost) ? mc.thePlayer.getActivePotionEffect(Potion.damageBoost).getAmplifier() != 1 : true;
    }

    private int getPowderSlot() {
        for (int i = 0; i <= 8; ++i) {
            final ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() == Items.blaze_powder) {
                return i;
            }
        }
        return -1;
    }
}
