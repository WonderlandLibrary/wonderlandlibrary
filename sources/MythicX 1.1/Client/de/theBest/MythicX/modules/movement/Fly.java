package de.theBest.MythicX.modules.movement;

import de.theBest.MythicX.MythicX;
import de.theBest.MythicX.events.EventPacket;
import de.theBest.MythicX.events.EventReceivedPacket;
import de.theBest.MythicX.events.EventUpdate;
import de.theBest.MythicX.modules.Category;
import de.theBest.MythicX.modules.Module;
import de.Hero.settings.Setting;
import de.theBest.MythicX.utils.PlayerUtil;
import eventapi.EventTarget;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;

import java.awt.*;
import java.util.ArrayList;

public class Fly extends Module {

    public Fly() {
        super("Fly", Type.Movement, 0, Category.MOVEMENT, Color.green, "Makes You Fly");
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (MythicX.setmgr.getSettingByName("Fly").getValString().equalsIgnoreCase("Creative")){
            setCapabilities(true);
        } else if (MythicX.setmgr.getSettingByName("Fly").getValString().equalsIgnoreCase("Float")){
            mc.thePlayer.motionY = 0;
            mc.timer.timerSpeed = 2;
        } else if (MythicX.setmgr.getSettingByName("Fly").getValString().equalsIgnoreCase("BlocksMC")) {
            if (!started) return;
            mc.timer.timerSpeed = 0.4f;
            PlayerUtil.strafe(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);

            if(mc.thePlayer.onGround){
                wasOnGround = true;
                mc.thePlayer.motionY = 0.42;
                PlayerUtil.strafe(10f);
            } else if (wasOnGround) {
                PlayerUtil.strafe(6.9f);
                wasOnGround = false;
            }
        }

    }

    private boolean started = false;
    private boolean wasOnGround = false;

    public void onDisable() {
        mc.timer.timerSpeed = 1;

        setCapabilities(false);

        mc.thePlayer.capabilities.setFlySpeed(0.1F);
    }

    private void setCapabilities(final boolean state) {
        mc.thePlayer.capabilities.isFlying = state;
        mc.thePlayer.capabilities.isCreativeMode = state;
        mc.thePlayer.capabilities.allowFlying = state;
        mc.thePlayer.capabilities.setFlySpeed(0.1F);
    }



    @Override
    public void onEnable() {
        if(MythicX.setmgr.getSettingByName("Fly").getValString().equalsIgnoreCase("BlocksMC")){
            BlockPos pos = mc.thePlayer.getPosition().add(0.0, -1.5, 0.0);
            mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(pos, 1, new ItemStack(Blocks.stone.getItem(mc.theWorld, pos)), 0.0F, (float) (0.5F + Math.random() * 0.44), 0.0F));
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.motionY, mc.thePlayer.motionZ, false));
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.motionY - 0.1, mc.thePlayer.motionZ, false));
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.motionY, mc.thePlayer.motionZ, false));
            started = true;
        }
    }

    @EventTarget
    public void onPacket(EventPacket eventPacket){
        Packet<?> p = EventPacket.getPacket();
        if(p instanceof S08PacketPlayerPosLook){
            eventPacket.setCanceled(true);
        }
    }

    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("Creative");
        options.add("Float");
        options.add("BlocksMC");
        MythicX.setmgr.rSetting(new Setting("Fly", this, "Creative", options));
        MythicX.setmgr.rSetting(new Setting("Timer", this, false));
        MythicX.setmgr.rSetting(new Setting("Timer-Speed", this, 2, 1, 20, false));
    }
}