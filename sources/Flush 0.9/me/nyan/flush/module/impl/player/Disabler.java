package me.nyan.flush.module.impl.player;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventMotion;
import me.nyan.flush.event.impl.EventPacket;
import me.nyan.flush.event.impl.EventWorldChange;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.ModeSetting;
import me.nyan.flush.utils.movement.PathFinder;
import me.nyan.flush.utils.other.Timer;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class Disabler extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", this, "Verus Combat", "Verus Combat", "Erisium", "Ghostly");

    private final Timer timer = new Timer();
    private final Timer timer2 = new Timer();
    private int currentTrans;
    private boolean hypixel;
    private int hypixel2;
    private Vec3 vec;
    private boolean disabledOnce;

    public Disabler() {
        super("Disabler", Category.PLAYER);
    }

    @Override
    public void onEnable() {
        timer.reset();
        timer2.reset();
        currentTrans = 0;
        disabledOnce = false;

        super.onEnable();
    }

    @SubscribeEvent
    public void onMotion(EventMotion e) {
        for (int y = (int) mc.thePlayer.posY; y > 0; y--) {
            BlockPos pos = new BlockPos(mc.thePlayer.posX, y, mc.thePlayer.posZ);
            if (pos.getBlock() instanceof BlockAir || !pos.getBlock().isFullBlock())
                continue;
            vec = new Vec3(pos.add(0, 1, 0));
            break;
        }

        switch (mode.getValue().toUpperCase()) {
            case "HYPIXELSLIMESKYWARS":
                if (timer.hasTimeElapsed(1200, true))
                    hypixel = true;

                if (!hypixel && mc.thePlayer.onGround) {
                    return;
                }

                if (mc.thePlayer.capabilities.allowFlying) {
                    PlayerCapabilities capabilities = new PlayerCapabilities();
                    capabilities.allowFlying = true;
                    capabilities.isFlying = true;
                    mc.getNetHandler().addToSendQueue(new C13PacketPlayerAbilities(capabilities));
                    hypixel = false;
                    hypixel2 = 1;
                    disabledOnce = true;
                    timer.reset();
                    timer2.reset();
                } else if (!mc.thePlayer.onGround && disabledOnce) {
                    if (!timer2.hasTimeElapsed(800, false)) {
                        return;
                    }

                    if (hypixel2 > 0) {
                        for (Vec3 vec : PathFinder.findPathTo(vec)) {
                            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec.xCoord, vec.yCoord, vec.zCoord, false));
                        }
                        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec.xCoord, vec.yCoord, vec.zCoord, true));

                        /*
                        *for (int y = (int) mc.thePlayer.posY; y > 0; y--) {
                            BlockPos pos = new BlockPos(mc.thePlayer.posX, y, mc.thePlayer.posZ);
                            if (pos.getBlock() instanceof BlockAir || !pos.getBlock().isFullBlock())
                                continue;
                            e.setY(pos.getY() + 1);
                            break;
                        }
                        */
                        e.setGround(true);
                        hypixel2--;
                    }
                }
                break;

            case "GHOSTLY":
                if (e.isPre()) {
                    mc.getNetHandler().addToSendQueue(new C0CPacketInput());
                    mc.getNetHandler().addToSendQueue(new C18PacketSpectate(mc.thePlayer.getUniqueID()));
                }
                break;
        }
    }

    @SubscribeEvent
    public void onPacket(EventPacket e) {
        if (mode.is("hypixelslimeskywars")) {
            if (e.getPacket() instanceof S08PacketPlayerPosLook)
                disabledOnce = false;
        } else if (mode.is("verus combat")) {
            /*
            if (e.isOutgoing()) {
                String c = e.getPacket().getClass().getName();
                ChatUtils.println(c.substring(c.lastIndexOf('.') + 1).replace('$', '.'));
            }
             */
            if (e.getPacket() instanceof /*C00PacketKeepAlive*/C0FPacketConfirmTransaction) {
                e.cancel();
                //mc.getNetHandler().addToSendQueueNoEvent(new C00PacketKeepAlive(((C00PacketKeepAlive) e.getPacket()).key - new Random().nextInt(Integer.MAX_VALUE)));
            }
        } else if (mode.is("erisium")) {
            if (e.getPacket() instanceof S00PacketKeepAlive || e.getPacket() instanceof S32PacketConfirmTransaction ||
                    ((e.getPacket() instanceof C00PacketKeepAlive || e.getPacket() instanceof C0FPacketConfirmTransaction) &&
                            !timer.hasTimeElapsed(6000, true))) {
                e.cancel();
            }
        }
    }

    @Override
    public String getSuffix() {
        return mode.getValue();
    }

    @SubscribeEvent
    public void onWorldChange(EventWorldChange e) {
        disabledOnce = false;
    }
}