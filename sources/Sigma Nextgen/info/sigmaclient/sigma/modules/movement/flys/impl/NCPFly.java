package info.sigmaclient.sigma.modules.movement.flys.impl;

import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.MoveEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.movement.Fly;
import info.sigmaclient.sigma.modules.movement.flys.FlyModule;
import info.sigmaclient.sigma.utils.player.MovementUtils;
import net.minecraft.client.gui.screen.DownloadTerrainScreen;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.client.CConfirmTeleportPacket;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class NCPFly extends FlyModule {
    public NCPFly(Fly fly) {
        super("NCP", "Fly for NewNCP", fly);
    }
    public float yDistance = 1;
    public float speed = 1F;
    public int teleportId;
    public List<CPlayerPacket> packetPlayerList;

    public void sendMotion(final double n, final double n2, final double n3) {
        final CPlayerPacket.PositionPacket position = new CPlayerPacket.PositionPacket(mc.player.getPosX() + n, mc.player.getPosY(), mc.player.getPosZ() + n3, mc.player.onGround);
        packetPlayerList.add((CPlayerPacket)position);
        mc.getConnection().sendPacket((IPacket<?>)position);
        final double n4 = mc.player.getPosX() + n;
        final double posY = mc.player.getPosY();
        double doubleValue;
        doubleValue = -3;
        final CPlayerPacket.PositionPacket position2 = new CPlayerPacket.PositionPacket(n4, posY + doubleValue, mc.player.getPosZ() + n3, mc.player.onGround);
        packetPlayerList.add((CPlayerPacket)position2);
        mc.getConnection().sendPacket((IPacket<?>)position2);
        ++teleportId;
//        mc.getConnection().sendPacket((IPacket<?>)new CConfirmTeleportPacket(teleportId - 1));
        if(n2 > 1.0)
            mc.getConnection().sendPacket((IPacket<?>)new CConfirmTeleportPacket(teleportId));
//        mc.getConnection().sendPacket((IPacket<?>)new CConfirmTeleportPacket(teleportId + 1));
    }

    public boolean isTeleportMode(){
        return parent.NCPMode.is("Fast");
    }
    public double[] getHorizontalSpeed(final double n) {
        float n2 = 0.0f;
        float n3 = 0.0f;
        final double n4 = 2.7999100260353087 * n;
        final float sin = MathHelper.sin(mc.player.rotationYaw * 3.1415927f / 180.0f);
        final float cos = MathHelper.cos(mc.player.rotationYaw * 3.1415927f / 180.0f);
        n2 += 0.1f;
        return new double[] { (n3 * cos - n2 * sin) * n4, (n2 * cos + n3 * sin) * n4 };
    }
    public class TickTimer {
        public float tick=0;
        public boolean hasTimePassed(int tick){
            return this.tick >= tick;
        }
        public void reset(){
            tick=0;
        }
        public void set(int tick){
            this.tick=tick;
        }
        public void update(){
            this.tick += 1;
        }
    }

    TickTimer tt = new TickTimer();
    public float boostMulti = 1;

    @Override
    public void onEnable() {
        packetPlayerList = new ArrayList<>();
        teleportId = 0;
        yDistance = 1;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        tt.reset();
        boostMulti = 1;
        final double posX = mc.player.getPosX();
        final double posY = mc.player.getPosY();
        double doubleValue;
        doubleValue = -3;
        final CPlayerPacket.PositionPacket position = new CPlayerPacket.PositionPacket(posX, posY + doubleValue, mc.player.getPosZ(), mc.player.onGround);
        packetPlayerList.add((CPlayerPacket)position);
        mc.getConnection().sendPacket((IPacket<?>)position);
        final CPlayerPacket.PositionPacket position2 = new CPlayerPacket.PositionPacket(posX, posY, mc.player.getPosZ(), mc.player.onGround);
        packetPlayerList.add((CPlayerPacket)position2);
        mc.getConnection().sendPacket((IPacket<?>)position2);
        mc.player.getMotion().y = 0;
        mc.player.getMotion().x = 0;
        mc.player.getMotion().z = 0;
        super.onDisable();
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPost()) return;
        if(isTeleportMode()){
            mc.timer.tickLength = 1000.0F / 20F / 0.2F;
        }
        if (teleportId <= 0) {
            MovementUtils.strafing(0);
            mc.player.getMotion().y = 0;
            final double posX = mc.player.getPosX();
            final double posY = mc.player.getPosY();
            double doubleValue;
            doubleValue = -3;
            final CPlayerPacket.PositionPacket position = new CPlayerPacket.PositionPacket(posX, posY + doubleValue, mc.player.getPosZ(), mc.player.onGround);
            packetPlayerList.add((CPlayerPacket)position);
            mc.getConnection().sendPacket((IPacket<?>)position);
            return;
        }
        if(mc.player.hurtTime == 0 && !isTeleportMode()){
            mc.timer.tickLength = 1000.0F / 20F / Math.max(yDistance, 0.5F);
            yDistance -= 0.01;
        }
        float speed = 0.72F;
        final double[] array = getHorizontalSpeed(speed);
        if(!isTeleportMode()){
            if (array[0] != 0.0 || array[1] != 0.0) {
                {
                    mc.player.setMotion(array[0], 0, array[1]);
                    sendMotion(array[0], 2.0, array[1]);
                    mc.player.setMotion(array[0] * 2, 0, array[1] * 2);
                    sendMotion(array[0] * 2, 2.0, array[1] * 2);
                    mc.player.setMotion(array[0] * 3, 0, array[1] * 3);
                    sendMotion(array[0] * 3, 2.0, array[1] * 3);
                    mc.player.setMotion(array[0] * 4, 0, array[1] * 4);
                    sendMotion(array[0] * 4, 2.0, array[1] * 4);
                }
            }
        }else{
            mc.player.getMotion().y = 0;
            if(tt.hasTimePassed(5)){
                sendMotion(array[0], 2.0, array[1]);
                sendMotion(array[0] * 2, 2.0, array[1] * 2);
                sendMotion(array[0] * 3, 2.0, array[1] * 3);
                sendMotion(array[0] * 4, 2.0, array[1] * 4);
                sendMotion(array[0] * 5, 2.0, array[1] * 5);
                sendMotion(array[0] * 6, 2.0, array[1] * 6);
                sendMotion(array[0] * 7, 2.0, array[1] * 7);
                sendMotion(array[0] * 8, 2.0, array[1] * 8);
                mc.player.setPosition(mc.player.getPosX() + array[0] * 8, mc.player.getPosY(), mc.player.getPosZ() + array[1] * 8);
                tt.reset();
            }
            tt.update();
        }
        super.onUpdateEvent(event);
    }

    @Override
    public void onPacketEvent(PacketEvent event) {
        if(event.isSend()){
            if (event.getPacket() instanceof CPlayerPacket && !(event.getPacket() instanceof CPlayerPacket.PositionPacket)) {
                event.cancelable = (true);
            }
            if (event.getPacket() instanceof CPlayerPacket) {
                final CPlayerPacket CPlayerPacket = (CPlayerPacket)event.getPacket();
                if (packetPlayerList.contains(CPlayerPacket)) {
                    packetPlayerList.remove(CPlayerPacket);
                    return;
                }
                event.cancelable = (true);
            }
        }else{
            if (event.getPacket() instanceof SPlayerPositionLookPacket) {
                final SPlayerPositionLookPacket sPacketPlayerPosLook = (SPlayerPositionLookPacket)event.getPacket();
                if (mc.player != null && !(mc.currentScreen instanceof DownloadTerrainScreen)) {
                    if (teleportId <= 0) {
                        teleportId = sPacketPlayerPosLook.getTeleportId();
                        if(!isTeleportMode())
                            mc.player.handleStatusUpdate((byte) 2);
                        if(!isTeleportMode())
                            mc.timer.tickLength = 1000.0F / 50F;
                    }else {
                        event.cancelable = (true);
                    }
                }
            }
        }
        super.onPacketEvent(event);
    }

    @Override
    public void onMoveEvent(MoveEvent event) {
        super.onMoveEvent(event);
    }
}
