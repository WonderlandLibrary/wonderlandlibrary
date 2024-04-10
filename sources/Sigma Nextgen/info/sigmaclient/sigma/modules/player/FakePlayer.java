package info.sigmaclient.sigma.modules.player;

import com.mojang.authlib.GameProfile;
import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.client.CConfirmTransactionPacket;
import net.minecraft.network.play.client.CPlayerPacket;

import java.util.ArrayList;
import java.util.UUID;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class FakePlayer extends Module {
    public FakePlayer() {
        super("FakePlayer", Category.Player, "WDF");
    }
    RemoteClientPlayerEntity e = null;
    @Override
    public void onEnable() {
        e = new RemoteClientPlayerEntity(mc.world, new GameProfile(UUID.randomUUID(), "sb"));
        e.copyLocationAndAnglesFrom(mc.player);
        e.setEntityId(114514);
        mc.world.addPlayer(114514, e);
        super.onEnable();
    }
    @Override
    public void onDisable() {
        if(e != null)
            mc.world.removeEntityFromWorld(e.getEntityId());
        super.onDisable();
    }
}
