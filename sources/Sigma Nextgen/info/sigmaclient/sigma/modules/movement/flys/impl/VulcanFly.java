package info.sigmaclient.sigma.modules.movement.flys.impl;

import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.MoveEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.movement.Fly;
import info.sigmaclient.sigma.modules.movement.flys.FlyModule;
import info.sigmaclient.sigma.premium.PremiumManager;
import info.sigmaclient.sigma.utils.VecUtils;
import info.sigmaclient.sigma.utils.player.MovementUtils;
import net.minecraft.network.play.client.CPlayerTryUseItemOnBlockPacket;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import org.lwjgl.system.CallbackI;

import java.util.Objects;
import java.util.Random;
import top.fl0wowp4rty.phantomshield.annotations.Native;

@Native
public class VulcanFly extends FlyModule {
    public static PremiumManager.PremiumRun premium1 = null;
    public static PremiumManager.PremiumRun premium2 = null;
    public static PremiumManager.PremiumRun premium3 = null;
    public VulcanFly(Fly fly) {
        super("Vulcan", "Fly for Vulcan", fly);
    }
    int flyTicks = 0;
    double y = 0;
    boolean phase = false;
    @Override
    public void onEnable() {
        flyTicks = 0;
        y = 0;
        phase = false;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.timer.setTimerSpeed(1f);
        super.onDisable();
    }
    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPost()) return;
        if(premium1 != null && PremiumManager.isPremium)
            premium1.run(event, parent);
        super.onUpdateEvent(event);
    }

    @Override
    public void onPacketEvent(PacketEvent event) {
        if(premium2 != null && PremiumManager.isPremium)
            premium2.run(event, parent);
        super.onPacketEvent(event);
    }

    @Override
    public void onMoveEvent(MoveEvent event) {
        if(premium3 != null && PremiumManager.isPremium)
            premium3.run(event, parent);
        super.onMoveEvent(event);
    }
}
