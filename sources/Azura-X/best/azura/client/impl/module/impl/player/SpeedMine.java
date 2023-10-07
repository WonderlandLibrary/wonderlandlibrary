package best.azura.client.impl.module.impl.player;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventUpdate;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.util.math.MathUtil;
import best.azura.client.impl.value.ModeValue;
import best.azura.client.impl.value.NumberValue;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.MovingObjectPosition;

@ModuleInfo(name = "Speed Mine", category = Category.PLAYER, description = "Break blocks faster")
public class SpeedMine extends Module {
    private final ModeValue mode = new ModeValue("Mode", "Mode of speed mine", "Vanilla", "Vanilla", "NCP", "Watchdog", "Instant");
    private final NumberValue<Double> speed = new NumberValue<>("Speed", "Speed of breaking blocks", () -> !mode.getObject().equals("NCP"), 1.0, 0.1, 1.0, 2.0);
    @EventHandler
    public final Listener<EventUpdate> eventUpdateListener = e -> {
        switch (mode.getObject()) {
            case "Vanilla":
                mc.playerController.curBlockDamageMP *= speed.getObject();
                break;
            case "Instant":
                if (mc.playerController.curBlockDamageMP > 0 &&
                        mc.gameSettings.keyBindAttack.pressed && mc.objectMouseOver != null &&
                        mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK &&
                        mc.objectMouseOver.getBlockPos() != null) {
                    mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                            mc.objectMouseOver.getBlockPos(), mc.objectMouseOver.sideHit));
                }
                break;
            case "Watchdog":
                if (mc.playerController.curBlockDamageMP > 1.0 / speed.getObject() &&
                        mc.gameSettings.keyBindAttack.pressed && (mc.objectMouseOver != null &&
                        mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK &&
                        mc.objectMouseOver.getBlockPos() != null)) {
                    mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                            mc.objectMouseOver.getBlockPos(), mc.objectMouseOver.sideHit));
                    mc.playerController.curBlockDamageMP = 0;
                }
                break;
            case "NCP":
                if (mc.gameSettings.keyBindAttack.pressed && (mc.objectMouseOver != null &&
                        mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK &&
                        mc.objectMouseOver.getBlockPos() != null)) {
                    if (mc.playerController.curBlockDamageMP >= 0.5f && !mc.thePlayer.isDead) {
                        mc.playerController.curBlockDamageMP += MathUtil.getDifference(mc.playerController.curBlockDamageMP, 1.0f) * 0.7f;
                    }
                }
                break;
        }
    };
}