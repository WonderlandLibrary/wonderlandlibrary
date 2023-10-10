package vestige.module.impl.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import vestige.Vestige;
import vestige.event.Listener;
import vestige.event.impl.MoveEvent;
import vestige.event.impl.PacketSendEvent;
import vestige.module.Category;
import vestige.module.Module;
import vestige.module.impl.movement.Speed;
import vestige.setting.impl.BooleanSetting;
import vestige.setting.impl.DoubleSetting;
import vestige.setting.impl.IntegerSetting;
import vestige.setting.impl.ModeSetting;
import vestige.util.network.PacketUtil;
import vestige.util.player.MovementUtil;

public class Criticals extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", "Packet", "Packet", "Minijump", "NCP");

    private final IntegerSetting hurtTime = new IntegerSetting("Hurt time", () -> mode.is("Minijump"), 2, 1, 6, 1);
    private final DoubleSetting motionY = new DoubleSetting("Motion Y", () -> mode.is("Minijump"), 0.08, 0.005, 0.42, 0.005);
    private final BooleanSetting normalGravity = new BooleanSetting("Normal gravity", () -> mode.is("Minijump"), true);
    private final DoubleSetting xzMotionMult = new DoubleSetting("XZ motion mult", () -> mode.is("Minijump"), 1, 0, 1, 0.02);

    private Killaura killauraModule;
    private Speed speedModule;

    public Criticals() {
        super("Criticals", Category.COMBAT);
        this.addSettings(mode, hurtTime, normalGravity, xzMotionMult);
    }

    @Override
    public void onClientStarted() {
        speedModule = Vestige.instance.getModuleManager().getModule(Speed.class);
        killauraModule = Vestige.instance.getModuleManager().getModule(Killaura.class);
    }

    @Listener
    public void onSend(PacketSendEvent event) {
        if(event.getPacket() instanceof C02PacketUseEntity) {
            C02PacketUseEntity packet = event.getPacket();

            if(packet.getAction() == C02PacketUseEntity.Action.ATTACK) {
                Entity target = packet.getEntity();

                switch (mode.getMode()) {
                    case "Packet":
                        if(mc.thePlayer.onGround) {
                            PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1E-5, mc.thePlayer.posZ, false));
                            PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                        } else {
                            PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                            PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1E-5, mc.thePlayer.posZ, false));
                        }
                        break;
                    case "NCP":
                        if(target != null && target instanceof EntityPlayer) {
                            EntityPlayer player = (EntityPlayer) target;

                            if(player.hurtTime <= 4 && mc.thePlayer.onGround && !Vestige.instance.getModuleManager().getModule(Speed.class).isEnabled()) {
                                PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.1, mc.thePlayer.posZ, false));
                                PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                            }
                        }
                        break;
                }
            }
        }
    }

    @Listener
    public void onMove(MoveEvent event) {
        if(killauraModule.isEnabled() && killauraModule.getTarget() != null) {
            if(killauraModule.getTarget() instanceof EntityPlayer) {
                EntityPlayer target = (EntityPlayer) killauraModule.getTarget();

                switch (mode.getMode()) {
                    case "Minijump":
                        if(mc.thePlayer.onGround && !mc.gameSettings.keyBindJump.isKeyDown() && !speedModule.isEnabled()) {
                            if(target.hurtTime == hurtTime.getValue()) {
                                if(normalGravity.isEnabled()) {
                                    event.setY(mc.thePlayer.motionY = motionY.getValue());
                                } else {
                                    event.setY(motionY.getValue());
                                }

                                MovementUtil.motionMult(event, xzMotionMult.getValue());
                            }
                        }
                        break;
                }
            }
        }
    }

}
