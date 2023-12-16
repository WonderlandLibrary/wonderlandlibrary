package wtf.expensive.modules.impl.combat;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.client.CConfirmTransactionPacket;
import net.minecraft.network.play.client.CKeepAlivePacket;
import net.minecraft.network.play.client.CUseEntityPacket;
import net.minecraft.network.play.server.*;
import net.minecraft.util.math.vector.Vector3d;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.packet.EventPacket;
import wtf.expensive.events.impl.player.EventMotion;
import wtf.expensive.events.impl.render.EventRender;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanOption;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.util.misc.TimerUtil;
import wtf.expensive.util.render.RenderUtil;
import wtf.expensive.util.render.animation.AnimationMath;

import java.util.concurrent.CopyOnWriteArrayList;

@FunctionAnnotation(name = "BackTrack", type = Type.Combat)
public class BackTrack extends Function {

    private final CopyOnWriteArrayList<IPacket> packets = new CopyOnWriteArrayList<>();

    private PlayerEntity target;

    private final SliderSetting range = new SliderSetting("Дистанция", 5.2f, 3, 6.5f, 0.1f);
    private final SliderSetting timeout = new SliderSetting("Таймаут", 5000, 100, 10000, 100);
    private final BooleanOption draw = new BooleanOption("Рисовать позицию игрока.", true);

    public BackTrack() {
        super();
        addSettings(range, timeout, draw);
    }

    private void findTarget(CUseEntityPacket packet) {
        if (mc.world != null && mc.player != null) {
            if (packet.getEntityFromWorld(mc.world) instanceof PlayerEntity player && player != target) {
                for (IPacket packetss : packets) {
                    mc.player.connection.getNetworkManager().sendPacketWithoutEvent(packetss);
                }
                packets.clear();

                target = player;
                createPositions();
            }
            if (packet.getEntityFromWorld(mc.world) instanceof PlayerEntity player && player == target) {
                timerUtil.reset();
            }
        }
    }

    private void createPositions() {
        target.realX = target.getPosX();
        target.realY = target.getPosY();
        target.realZ = target.getPosZ();
    }

    private void onPacket(EventPacket e) {
        if (e.isSendPacket()) {
            onSendPacket(e);
        }
        if (e.isReceivePacket()) {
            onReceivePacket(e);
        }
    }

    private void onSendPacket(EventPacket e) {
        if (target != null) {
            if (mc.player != null && mc.world != null && !mc.isSingleplayer() && !mc.player.getShouldBeDead()) {
                if ((e.getPacket() instanceof CKeepAlivePacket || e.getPacket() instanceof CConfirmTransactionPacket)) {
                    packets.add(e.getPacket());
                    e.setCancel(true);

                }
            } else toggle();
        }
        if (e.getPacket() instanceof CUseEntityPacket packet) {
            findTarget(packet);
        }
    }

    private void onReceivePacket(EventPacket e) {
        if (target != null) {
            if (e.getPacket() instanceof SEntityVelocityPacket packet) {
                handleVelocityPacket(e);
            }
            if (target != null) {
                if (e.getPacket() instanceof SPlayerPositionLookPacket packet) {
                    handleFlagPacket(e);
                    return;
                }

                // Пакет обновления позиции игрока.
                if (e.getPacket() instanceof SEntityPacket packet) {
                    onUpdatePosition(e);
                }

                // Пакет телепортации позиции игрока.
                if (e.getPacket() instanceof SEntityTeleportPacket packet) {
                    onTeleportPosition(e);
                }
            }
        }
    }


    public TimerUtil timerUtil = new TimerUtil();

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventPacket e) {
            onPacket(e);
        }
        if (event instanceof EventRender e) {
            handleRenderEvent(e);
        }
        if (event instanceof EventMotion e) {
            handleMotionEvent(e);
        }
    }

    private void handleMotionEvent(EventMotion e) {
        if (target != null) {
            if (!target.isAlive()) {
                target.func_242277_a(new Vector3d(target.realX, target.realY, target.realZ));
                target.setRawPosition(target.realX, target.realY, target.realZ);

                target.realXI = 0;
                target.realYI = 0;
                target.realZI = 0;

                target = null;
                for (IPacket packet : packets) {
                    mc.player.connection.getNetworkManager().sendPacketWithoutEvent(packet);
                }
                packets.clear();
                timerUtil.reset();
                return;
            }
        }

        if (timerUtil.hasTimeElapsed(timeout.getValue().intValue())) {
            if (target != null) {

                target.func_242277_a(new Vector3d(target.realX, target.realY, target.realZ));
                target.setRawPosition(target.realX, target.realY, target.realZ);

                target.realXI = 0;
                target.realYI = 0;
                target.realZI = 0;

                target = null;
                for (IPacket packet : packets) {
                    mc.player.connection.getNetworkManager().sendPacketWithoutEvent(packet);
                }
                packets.clear();
                timerUtil.reset();
                return;
            }
        }

        if (target != null) {
            if (!target.isAlive()) {
                target = null;
                return;
            }

            if (mc.player.getPositionVec().distanceTo(new Vector3d(target.realX, target.realY, target.realZ)) >= range.getValue().floatValue()) {
                target.func_242277_a(new Vector3d(target.realX, target.realY, target.realZ));
                target.setRawPosition(target.realX, target.realY, target.realZ);

                target.realXI = 0;
                target.realYI = 0;
                target.realZI = 0;

                target = null;
                for (IPacket packet : packets) {
                    mc.player.connection.getNetworkManager().sendPacketWithoutEvent(packet);
                }
                packets.clear();
            }
        }
    }

    private void handleRenderEvent(EventRender e) {
        if (e.isRender3D() && target != null && draw.get()) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            {
                target.realXI = AnimationMath.lerp(target.realXI, target.realX - target.getPosX(), 10);
                target.realYI = AnimationMath.lerp(target.realYI, target.realY - target.getPosY(), 10);
                target.realZI = AnimationMath.lerp(target.realZI, target.realZ - target.getPosZ(), 10);

                RenderUtil.Render3D.drawBox(target.getBoundingBox().offset(target.realXI, target.realYI, target.realZI).offset(-mc.getRenderManager().info.getProjectedView().x, -mc.getRenderManager().info.getProjectedView().y, -mc.getRenderManager().info.getProjectedView().z), -1);
            }
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    private void handleVelocityPacket(EventPacket e) {
        SEntityVelocityPacket packet = (SEntityVelocityPacket) e.getPacket();
        if (packet.getEntityID() == mc.player.getEntityId()) {
            target.func_242277_a(new Vector3d(target.realX, target.realY, target.realZ));
            target.setRawPosition(target.realX, target.realY, target.realZ);

            target.realXI = 0;
            target.realYI = 0;
            target.realZI = 0;

            target = null;
            for (IPacket packetss : packets) {
                mc.player.connection.getNetworkManager().sendPacketWithoutEvent(packetss);
            }
            packets.clear();
            timerUtil.reset();
        }
    }

    private void handleFlagPacket(EventPacket e) {
        target.func_242277_a(new Vector3d(target.realX, target.realY, target.realZ));
        target.setRawPosition(target.realX, target.realY, target.realZ);

        target.realXI = 0;
        target.realYI = 0;
        target.realZI = 0;

        target = null;
        for (IPacket packetss : packets) {
            mc.player.connection.getNetworkManager().sendPacketWithoutEvent(packetss);
        }
        packets.clear();
        timerUtil.reset();
    }

    private void onTeleportPosition(EventPacket e) {
        SEntityTeleportPacket packet = (SEntityTeleportPacket) e.getPacket();

        if (packet.getEntityId() == target.getEntityId()) {
            target.realX = packet.getX();
            target.realY = packet.getY();
            target.realZ = packet.getZ();

            e.setCancel(true);
        }
    }

    private void onUpdatePosition(EventPacket e) {
        SEntityPacket packet = (SEntityPacket) e.getPacket();
        if (packet.entityId == target.getEntityId()) {
            target.realX += packet.posX / 4096d;
            target.realY += packet.posY / 4096d;
            target.realZ += packet.posZ / 4096d;

            e.setCancel(true);
        }
    }
}