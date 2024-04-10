package info.sigmaclient.sigma.modules.world;

import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.modules.movement.Fly;
import info.sigmaclient.sigma.modules.movement.Speed;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import net.minecraft.util.Hand;

import java.util.Random;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class Disabler extends Module {
    public static ModeValue mode = new ModeValue("Type","Verus",
            new String[]{
                    "Verus", // verus sprint...
                    "Karhu",
                    "ColdPVP",
                    "MineMalia"
            });
    public Disabler() {
        super("Disabler", Category.World, "disable some anti-cheats");
     registerValue(mode);
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        this.suffix = mode.getValue();
        switch (mode.getValue()) {
            case "Karhu":
                if (event.isPre() && mc.timer.getTimerSpeed() > 1.0F) {
                    mc.getConnection().sendPacket(new CPlayerTryUseItemPacket(
                            Hand.MAIN_HAND
                    ));
                }
                break;
            case "MineMalia":
//                if (event.isPre() && mc.player.ticksExisted % 5 == 0 && mc.player.onGround && (SigmaNG.getSigmaNG().moduleManager.getModule(Speed.class).enabled || SigmaNG.getSigmaNG().moduleManager.getModule(Fly.class).enabled)) {
//                    double doubleValue;
//                    doubleValue = -0.5;
//                    final double posX = mc.player.getPosX();
//                    final double posY = mc.player.getPosY();
//                    final CPlayerPacket.PositionPacket position = new CPlayerPacket.PositionPacket(posX, posY + doubleValue, mc.player.getPosZ(), mc.player.onGround);
////                    packetPlayerList.add((CPlayerPacket)position);
//                    mc.getConnection().sendPacket((IPacket<?>)position);
//                }
                break;
            case "ColdPVP":
                if (event.isPre() && mc.player.ticksExisted % 10 == 0 && (SigmaNG.getSigmaNG().moduleManager.getModule(Speed.class).enabled || SigmaNG.getSigmaNG().moduleManager.getModule(Fly.class).enabled)) {
                    double doubleValue;
                    doubleValue = -new Random().nextInt(10)-1;
                    final double posX = mc.player.getPosX();
                    final double posY = mc.player.getPosY();
                    final CPlayerPacket.PositionPacket position = new CPlayerPacket.PositionPacket(posX, posY + doubleValue, mc.player.getPosZ(), mc.player.onGround);
//                    packetPlayerList.add((CPlayerPacket)position);
                    mc.getConnection().sendPacket((IPacket<?>)position);
                }
                break;
            case "Intave":
                break;
        }
        super.onUpdateEvent(event);
    }
    public float fixedFacing(float face){
        return face > 0 ? 0.5f : (face < 0 ? -0.5f : 0);
    }
    @Override
    public void onPacketEvent(PacketEvent event) {
        switch (mode.getValue()) {
            case "MineMalia":
                if (event.packet instanceof SPlayerPositionLookPacket && mc.currentScreen == null && (SigmaNG.getSigmaNG().moduleManager.getModule(Speed.class).enabled || SigmaNG.getSigmaNG().moduleManager.getModule(Fly.class).enabled)) {
                    ClientPlayerEntity playerentity = mc.player;
                    SPlayerPositionLookPacket packetIn = (SPlayerPositionLookPacket) event.packet;
                    mc.getConnection().sendPacket(new CConfirmTeleportPacket(packetIn.getTeleportId()));
                    mc.getConnection().sendPacket(new CPlayerPacket.PositionRotationPacket(playerentity.getPosX(), playerentity.getPosY(), playerentity.getPosZ(), playerentity.rotationYaw, playerentity.rotationPitch, false));

                    event.cancelable = true;
                }
                break;
            case "Karhu":
            case "Verus":
                if (event.isSend()) {
                    if (event.packet instanceof CEntityActionPacket) {
                        if (((CEntityActionPacket) event.packet).getAction() == CEntityActionPacket.Action.START_SPRINTING) {
                            event.cancelable = true;
                        }
                        if (((CEntityActionPacket) event.packet).getAction() == CEntityActionPacket.Action.STOP_SPRINTING) {
                            event.cancelable = true;
                        }
                    }
                }
                break;
        }
        super.onPacketEvent(event);
    }
}
