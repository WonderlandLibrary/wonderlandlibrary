package info.sigmaclient.sigma.modules.render;

import com.mojang.authlib.GameProfile;
import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.MoveEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.utils.player.MovementUtils;
import net.minecraft.client.GameSettings;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.client.util.InputMappings;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.client.CConfirmTransactionPacket;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.util.math.vector.Vector3d;

import java.util.ArrayList;
import java.util.UUID;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class Freecam extends Module {
    RemoteClientPlayerEntity e = null;
    BooleanValue noPackets = new BooleanValue("No Packets", true);
    public NumberValue vanillaSpeed = new NumberValue("Speed", 7, 0, 9, NumberValue.NUMBER_TYPE.FLOAT);
    public NumberValue vanillaUpSpeed = new NumberValue("Vertical - Speed", 7, 0, 9, NumberValue.NUMBER_TYPE.FLOAT);
    //
    public Freecam() {
        super("Freecam", Category.Player, "Freecam");
     registerValue(noPackets);
     registerValue(vanillaSpeed);
     registerValue(vanillaUpSpeed);
    }
    Vector3d lastPos = null;
    @Override
    public void onEnable() {
        lastPos = mc.player.getPositionVec();
        e = new RemoteClientPlayerEntity(mc.world, new GameProfile(UUID.randomUUID(), "You"));
        e.copyLocationAndAnglesFrom(mc.player);
        e.setEntityId(114514);
        mc.world.addPlayer(114514, e);
        super.onEnable();
    }
    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPre()){
            event.cancelable = true;
            if(!noPackets.isEnable()){
                mc.getConnection().sendPacket(new CPlayerPacket(lastPos.y % 0.125 == 0));
            }
            mc.gameSettings.keyBindJump.pressed = false;
            mc.gameSettings.keyBindSneak.pressed = false;
            mc.player.setSneaking(false);
        }
        super.onUpdateEvent(event);
    }

    @Override
    public void onMoveEvent(MoveEvent event) {
        mc.player.noClip = true;
        MovementUtils.strafing(event, vanillaSpeed.getValue().floatValue());
        event.setY(mc.player.getMotion().y = InputMappings.isKeyDown(mc.gameSettings.keyBindJump) ? vanillaUpSpeed.getValue().doubleValue() :
                (InputMappings.isKeyDown(mc.gameSettings.keyBindSneak) ? -vanillaUpSpeed.getValue().doubleValue() : 0));
        super.onMoveEvent(event);
    }

    @Override
    public void onDisable() {
        if(lastPos == null) return;
        mc.player.getMotion().y = 0;
        mc.player.getMotion().x = 0;
        mc.player.getMotion().z = 0;
        mc.player.setPosition(lastPos.x, lastPos.y, lastPos.z);
        if(e != null)
        mc.world.removeEntityFromWorld(e.getEntityId());
        super.onDisable();
    }
}
