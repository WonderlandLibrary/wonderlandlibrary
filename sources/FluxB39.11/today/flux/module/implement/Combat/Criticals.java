package today.flux.module.implement.Combat;

import com.darkmagician6.eventapi.EventTarget;
import com.soterdev.SoterObfuscator;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumAction;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import today.flux.event.PacketSendEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.ModuleManager;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.FloatValue;
import today.flux.module.value.ModeValue;
import today.flux.utility.ChatUtils;
import today.flux.utility.ServerUtils;
import today.flux.utility.TimeHelper;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Criticals extends Module {
    public static TimeHelper delayHoped = new TimeHelper();
    public static ModeValue mode = new ModeValue("Criticals", "Mode", "Packet", "Packet", "Jump", "Hypixel");
    public static FloatValue hurtTime = new FloatValue("Criticals", "Hurt Time", 15, 10, 20, 1);
    public static FloatValue delay = new FloatValue("Criticals", "Delay", 300, 0, 5000, 100);

    public static BooleanValue postPacketCheck = new BooleanValue("Criticals", "Post Motion", true);
    public static TimeHelper timer = new TimeHelper();

    static double[] y1 = {0.104080378093037, 0.105454222033912, 0.102888018147468, 0.099634532004642};
    double lastY = -1;

    public Criticals() {
        super("Criticals", Category.Combat, mode);
    }

    public static void doCrits() {
        double[] offsets = new double[]{0.06D, 0.0D, 0.03D, 0.0D};

        for (int i = 0; i < offsets.length; ++i)
            mc.thePlayer.sendQueue
                    .addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                            mc.thePlayer.posY + offsets[i], mc.thePlayer.posZ, false));
    }

    public static boolean isEating() {
        return mc.thePlayer.isUsingItem() && mc.thePlayer.getItemInUse().getItem()
                .getItemUseAction(mc.thePlayer.getItemInUse()) == EnumAction.EAT;
    }

    @EventTarget
    public void onEvent(PacketSendEvent event) {
        if (event.getPacket() instanceof C03PacketPlayer) {
            lastY = ((C03PacketPlayer) event.getPacket()).getPositionY();
        }

        if (!(event.getPacket() instanceof C02PacketUseEntity))
            return;

        if (isEating() || ModuleManager.longJumpMod.isEnabled() || ModuleManager.speedMod.isEnabled())
            return;

        C02PacketUseEntity packet = (C02PacketUseEntity) event.getPacket();

        if (packet.getAction() == C02PacketUseEntity.Action.ATTACK && mc.thePlayer.isCollidedVertically && this.hurtTimeCheck(packet.getEntityFromWorld(mc.theWorld))) {
            if (mode.getValue().equals("Packet")) {
                doCrits();
            }

            if (mode.getValue().equals("Jump")) {
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                }
            }
        }
    }

    private boolean hurtTimeCheck(Entity entity) {
        return entity != null && entity.hurtResistantTime <= 15;
    }

    @SoterObfuscator.Obfuscation(flags = "+native")
    public void doHoverCrit() {
        timer.reset();
        if (ServerUtils.INSTANCE.isOnHypixel()) {
            if (mode.getValue().equals("Hypixel")) {
                if ((lastY - ((int)lastY)) == 0 || !postPacketCheck.getValueState()) {
                    ChatUtils.debug("Critical!");

                    double[] edit = new double[]{0.075 + ThreadLocalRandom.current().nextDouble(0.008) * (new Random().nextBoolean() ? 0.96 : 0.97) + mc.thePlayer.ticksExisted % 0.0215 * 0.92,
                            (new Random().nextBoolean() ? 0.010634691223 : 0.013999777) * (new Random().nextBoolean() ? 0.95 : 0.96) * y1[new Random().nextInt(y1.length)] * 9.5f};

                    EntityPlayerSP p = mc.thePlayer;

                    if (!postPacketCheck.getValueState()) {
                        mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(p.posX, p.posY, p.posZ, false));
                    }

                    for (double offset : edit) {
                        mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(p.posX, p.posY + (offset * (1 + KillAura.getRandomDoubleInRange(-0.005, 0.005))), p.posZ, false));
                    }
                }
            }
        }
    }
}

