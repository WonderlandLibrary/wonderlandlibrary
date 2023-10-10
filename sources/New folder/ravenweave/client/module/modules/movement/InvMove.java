package ravenweave.client.module.modules.movement;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;
import org.lwjgl.input.Keyboard;
import ravenweave.client.clickgui.raven.ClickGui;
import ravenweave.client.module.Module;
import ravenweave.client.module.setting.impl.DescriptionSetting;
import ravenweave.client.module.setting.impl.TickSetting;

public class InvMove extends Module {
    private final TickSetting rotate, clickgui;

    public InvMove() {
        super("InvMove", ModuleCategory.movement);
        registerSetting(new DescriptionSetting("This does not bypass"));
        registerSetting(rotate = new TickSetting("Rotate", false));
        registerSetting(clickgui = new TickSetting("Only ClickGui", true));
    }

    @SubscribeEvent
    public void onTick(TickEvent e) {
        if (mc.currentScreen != null) {
            if (mc.currentScreen instanceof GuiChat) {
                return;
            }

            if (clickgui.isToggled() && !(mc.currentScreen instanceof ClickGui))
                return;

            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(),
                    Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode()));
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(),
                    Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode()));
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(),
                    Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode()));
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(),
                    Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode()));
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(),
                    Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode()));
            EntityPlayerSP var1;
            if (rotate.isToggled()) {
                if (Keyboard.isKeyDown(208) && mc.thePlayer.rotationPitch < 90.0F) {
                    var1 = mc.thePlayer;
                    var1.rotationPitch += 6.0F;
                }

                if (Keyboard.isKeyDown(200) && mc.thePlayer.rotationPitch > -90.0F) {
                    var1 = mc.thePlayer;
                    var1.rotationPitch -= 6.0F;
                }

                if (Keyboard.isKeyDown(205)) {
                    var1 = mc.thePlayer;
                    var1.rotationYaw += 6.0F;
                }

                if (Keyboard.isKeyDown(203)) {
                    var1 = mc.thePlayer;
                    var1.rotationYaw -= 6.0F;
                }
            }
        }

    }
}
