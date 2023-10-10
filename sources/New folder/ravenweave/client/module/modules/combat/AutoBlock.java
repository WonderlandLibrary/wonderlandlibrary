package ravenweave.client.module.modules.combat;

import net.minecraft.client.settings.KeyBinding;
import net.weavemc.loader.api.event.RenderGameOverlayEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import org.lwjgl.input.Mouse;
import ravenweave.client.module.Module;
import ravenweave.client.module.setting.impl.DoubleSliderSetting;
import ravenweave.client.module.setting.impl.SliderSetting;
import ravenweave.client.utils.CoolDown;
import ravenweave.client.utils.Utils;

public class AutoBlock extends Module {
    public static DoubleSliderSetting duration, distance;
    public static SliderSetting chance;
    private boolean engaged;
    private final CoolDown engagedTime = new CoolDown(0);

    public AutoBlock() {
        super("AutoBlock", ModuleCategory.combat);

        this.registerSetting(duration = new DoubleSliderSetting("Block duration (MS)", 20, 100, 1, 500, 1));
        this.registerSetting(distance = new DoubleSliderSetting("Distance to player (blocks)", 0, 3, 0, 6, 0.01));
        this.registerSetting(chance = new SliderSetting("Chance %", 100, 0, 100, 1));
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent e) {
        if (!this.enabled) return;
        if (!Utils.Player.isPlayerInGame() || !Utils.Player.isPlayerHoldingSword())
            return;

        if (engaged) {
            if ((engagedTime.hasFinished() || !Mouse.isButtonDown(0))
                    && duration.getInputMin() <= engagedTime.getElapsedTime()) {
                engaged = false;
                release();
            }
            return;
        }

        if (Mouse.isButtonDown(0) && mc.objectMouseOver != null && mc.objectMouseOver.entityHit != null
                && mc.thePlayer.getDistanceToEntity(mc.objectMouseOver.entityHit) >= distance.getInputMin()
                && mc.objectMouseOver.entityHit != null
                && mc.thePlayer.getDistanceToEntity(mc.objectMouseOver.entityHit) <= distance.getInputMax()
                && (chance.getInput() == 100 || Math.random() <= chance.getInput() / 100)) {
            engaged = true;
            engagedTime.setCooldown((long) duration.getInputMax());
            engagedTime.start();
            press();
        }
    }

    private static void release() {
        int key = mc.gameSettings.keyBindUseItem.getKeyCode();
        KeyBinding.setKeyBindState(key, false);
        Utils.Client.setMouseButtonState(1, false);
    }

    private static void press() {
        int key = mc.gameSettings.keyBindUseItem.getKeyCode();
        KeyBinding.setKeyBindState(key, true);
        KeyBinding.onTick(key);
        Utils.Client.setMouseButtonState(1, true);
    }
}
