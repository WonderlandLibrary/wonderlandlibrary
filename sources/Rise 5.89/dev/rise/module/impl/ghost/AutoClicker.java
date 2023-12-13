/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.ghost;

import dev.rise.event.impl.render.Render3DEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.math.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Mouse;

@ModuleInfo(name = "AutoClicker", description = "Automatically clicks for you", category = Category.GHOST)
public final class AutoClicker extends Module {

    private final NumberSetting minCps = new NumberSetting("Min CPS", this, 6, 1, 20, 1);
    private final NumberSetting maxCps = new NumberSetting("Max CPS", this, 8, 1, 20, 1);

    @Override
    public void onRender3DEvent(final Render3DEvent event) {
        if (Mouse.isButtonDown(0) && mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            mc.gameSettings.keyBindAttack.setKeyPressed(true);
            return;
        }

        if (mc.currentScreen == null && !mc.thePlayer.isBlocking()) {
            Mouse.poll();

            if (Mouse.isButtonDown(0) && Math.random() * 50 <= minCps.getValue() + (MathUtil.RANDOM.nextDouble() * (maxCps.getValue() - minCps.getValue()))) {
                sendClick(0, true);
                sendClick(0, false);
            }
        }
    }

    private void sendClick(final int button, final boolean state) {
        final Minecraft mc = Minecraft.getMinecraft();
        final int keyBind = button == 0 ? mc.gameSettings.keyBindAttack.getKeyCode() : mc.gameSettings.keyBindUseItem.getKeyCode();

        KeyBinding.setKeyBindState(button == 0 ? mc.gameSettings.keyBindAttack.getKeyCode() : mc.gameSettings.keyBindUseItem.getKeyCode(), state);

        if (state) {
            KeyBinding.onTick(keyBind);
        }
    }
}
