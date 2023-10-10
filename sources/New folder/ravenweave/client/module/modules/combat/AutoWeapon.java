package ravenweave.client.module.modules.combat;

import net.weavemc.loader.api.event.RenderGameOverlayEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import org.lwjgl.input.Mouse;
import ravenweave.client.module.Module;
import ravenweave.client.module.setting.impl.TickSetting;
import ravenweave.client.utils.Utils;

public class AutoWeapon extends Module {
    public static TickSetting onlyWhenHoldingDown;
    public static TickSetting goBackToPrevSlot;
    private boolean onWeapon;
    private int prevSlot;

    public AutoWeapon() {
        super("AutoWeapon", ModuleCategory.combat);

        this.registerSetting(onlyWhenHoldingDown = new TickSetting("Only when holding lmb", true));
        this.registerSetting(goBackToPrevSlot = new TickSetting("Revert to old slot", true));
    }

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent ev) {
        if (!this.enabled) return;
        if (!Utils.Player.isPlayerInGame() || mc.currentScreen != null)
            return;

        if (mc.objectMouseOver == null || mc.objectMouseOver.entityHit == null
                || (onlyWhenHoldingDown.isToggled() && !Mouse.isButtonDown(0))) {
            if (onWeapon) {
                onWeapon = false;
                if (goBackToPrevSlot.isToggled()) {
                    mc.thePlayer.inventory.currentItem = prevSlot;
                }
            }
        } else {
            if (onlyWhenHoldingDown.isToggled()) {
                if (!Mouse.isButtonDown(0))
                    return;
            }

            if (!onWeapon) {
                prevSlot = mc.thePlayer.inventory.currentItem;
                onWeapon = true;

                int maxDamageSlot = Utils.Player.getMaxDamageSlot();

                if (maxDamageSlot > 0 && Utils.Player.getSlotDamage(maxDamageSlot) > Utils.Player
                        .getSlotDamage(mc.thePlayer.inventory.currentItem)) {
                    mc.thePlayer.inventory.currentItem = maxDamageSlot;
                }
            }
        }
    }
}
