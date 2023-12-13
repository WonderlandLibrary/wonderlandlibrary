package me.nyan.flush.module.impl.combat;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventDrawGuiScreen;
import me.nyan.flush.event.impl.EventUpdate;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.utils.other.MathUtils;
import me.nyan.flush.utils.other.Timer;
import me.nyan.flush.utils.player.PlayerUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class AutoClicker extends Module {
    private final Timer cpsTimer = new Timer();

    private final NumberSetting maxCps = new NumberSetting("Max CPS", this, 12, 1, 20, 0.5),
            minCps = new NumberSetting("Min CPS", this, 9, 1, 20, 0.5),
            jitter = new NumberSetting("Jitter", this, 1, 0, 2, 0.1);
    private final BooleanSetting weaponOnly = new BooleanSetting("Weapon Only", this, false),
            blockHit = new BooleanSetting("Block Hit", this, true),
            inventory = new BooleanSetting("In Inventory", this, true);
    public final BooleanSetting removeCpsCap = new BooleanSetting("Remove CPS Cap", this, true);

    public AutoClicker() {
        super("AutoClicker", Category.COMBAT);
    }

    @SubscribeEvent
    public void onUpdate(EventUpdate e) {
        if ((weaponOnly.getValue() && !PlayerUtils.isHoldingWeapon()) || !Mouse.isButtonDown(0) || mc.currentScreen != null) {
            return;
        }

        float cps = MathUtils.getRandomNumber(maxCps.getValueInt(), minCps.getValueInt());

        if (cpsTimer.hasTimeElapsed((long) (1000F / cps), true)) {
            if (Mouse.isButtonDown(1) && blockHit.getValue() && mc.thePlayer.getItemInUse() != null && mc.thePlayer.getItemInUse().getItem() instanceof ItemSword)
                mc.thePlayer.stopUsingItem();

            mc.clickMouse();
            if (mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && isEnabled(Criticals.class)) {
                mc.thePlayer.onEnchantmentCritical(mc.objectMouseOver.entityHit);
            }
        }

        if (jitter.getValue() > 0) {
            mc.thePlayer.rotationYaw += MathUtils.getRandomNumber(-jitter.getValue(), jitter.getValue());
            mc.thePlayer.rotationPitch += MathUtils.getRandomNumber(-jitter.getValue(), jitter.getValue());
        }
    }

    @SubscribeEvent
    public void onDrawGui(EventDrawGuiScreen e) {
        if (!inventory.getValue() || !Mouse.isButtonDown(0) || !(e.getGuiScreen() instanceof GuiContainer)) {
            return;
        }

        float cps = MathUtils.getRandomNumber(maxCps.getValueInt(), minCps.getValueInt());

        if (cpsTimer.hasTimeElapsed((long) (1000F / cps), true)) {
            try {
                e.getGuiScreen().mouseClicked(e.getMouseX(), e.getMouseY(), 0);
            } catch (IOException ignored) {

            }
        }
    }
}
