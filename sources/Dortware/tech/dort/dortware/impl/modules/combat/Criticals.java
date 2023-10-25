package tech.dort.dortware.impl.modules.combat;

import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import skidmonke.Client;
import tech.dort.dortware.api.module.Module;
import tech.dort.dortware.api.module.ModuleData;
import tech.dort.dortware.api.property.impl.EnumValue;
import tech.dort.dortware.api.property.impl.interfaces.INameable;
import tech.dort.dortware.impl.events.PacketEvent;
import tech.dort.dortware.impl.modules.movement.Speed;
import tech.dort.dortware.impl.utils.movement.MotionUtils;
import tech.dort.dortware.impl.utils.networking.PacketUtil;
import tech.dort.dortware.impl.utils.time.Stopwatch;

public class Criticals extends Module {
    public EnumValue<Mode> mode = new EnumValue<>("Mode", this, Mode.values());

    public final double[] packetValues = new double[]{0.0625D, 0.0D, 0.05D, 0.0D};
    public final double[] hypixelValues = new double[]{0.0625D, 0.0D};
    private final Stopwatch timer = new Stopwatch();

    public Criticals(ModuleData data) {
        super(data);
        register(mode);
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        switch (mode.getValue()) {
            case PACKET:
                if (event.getPacket() instanceof C02PacketUseEntity) {
                    final C02PacketUseEntity packetUseEntity = event.getPacket();
                    if (packetUseEntity.getAction() == C02PacketUseEntity.Action.ATTACK) {
                        final Entity entity = packetUseEntity.getEntityFromWorld(mc.theWorld);
                        if (mc.thePlayer.onGround && entity.hurtResistantTime != -1) {
                            for (double o : packetValues) {
                                PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + o, mc.thePlayer.posZ, false));
                            }
                            entity.hurtResistantTime = -1;
                        }
                    }
                }
                break;

            case HYPIXEL:
                if (event.getPacket() instanceof C02PacketUseEntity) {
                    final C02PacketUseEntity packetUseEntity = event.getPacket();
                    if (packetUseEntity.getAction() == C02PacketUseEntity.Action.ATTACK) {
                        final Entity entity = packetUseEntity.getEntityFromWorld(mc.theWorld);
                        if (mc.thePlayer.onGround && entity.hurtResistantTime != -1 && !Client.INSTANCE.getModuleManager().get(Speed.class).isToggled()) {
                            for (double o : hypixelValues) {
                                PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + o, mc.thePlayer.posZ, false));
                            }
                            entity.hurtResistantTime = -1;
                        }
                    }
                }
                break;

            case SPOOF:
                if (event.getPacket() instanceof C03PacketPlayer) {
                    final C03PacketPlayer packetPlayer = event.getPacket();
                    packetPlayer.y += 0.0625D;
                    event.setPacket(packetPlayer);
                }
                break;

            case JUMP:
                if (event.getPacket() instanceof C02PacketUseEntity) {
                    final C02PacketUseEntity packetUseEntity = event.getPacket();
                    if (packetUseEntity.getAction() == C02PacketUseEntity.Action.ATTACK) {
                        if (mc.thePlayer.onGround) {
                            mc.thePlayer.motionY = MotionUtils.getMotion(0.42F);
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        timer.resetTime();
    }

    public String getSuffix() {
        return " \2477" + mode.getValue().getDisplayName();
    }

    public enum Mode implements INameable {
        PACKET("Packet"), HYPIXEL("Hypixel"), SPOOF("Spoof"), JUMP("Jump");

        private final String name;

        Mode(String name) {
            this.name = name;
        }

        @Override
        public String getDisplayName() {
            return name;
        }
    }

}