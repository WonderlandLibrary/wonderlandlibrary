package cc.novoline.modules.misc;

import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.BooleanProperty;
import cc.novoline.modules.configurations.property.object.PropertyFactory;
import cc.novoline.modules.move.FastSneak;
import net.minecraft.util.MovementInput;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.lwjgl.input.Keyboard;

public final class GuiMove extends AbstractModule {

    @Property("sneak")
    private final BooleanProperty sneak = PropertyFactory.createBoolean(false);

    /* constructors */
    public GuiMove(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "GuiMove", "Gui Move", EnumModuleType.MISC, "Allows you to walk with an opened GUI");
        Manager.put(new Setting("GM_SNEAK", "Sneak", SettingType.CHECKBOX, this, sneak));
    }

    public void updatePlayerMoveState() {
        MovementInput movementInput = mc.player.movementInput();

        movementInput.setMoveStrafe(0.0F);
        movementInput.setMoveForward(0.0F);

        if (Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode())) {
            movementInput.setMoveForward(movementInput.getMoveForward() + 1);
        }

        if (Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode())) {
            movementInput.setMoveForward(movementInput.getMoveForward() - 1);
        }

        if (Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode())) {
            movementInput.setMoveStrafe(movementInput.getMoveStrafe() + 1);
        }

        if (Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode())) {
            movementInput.setMoveStrafe(movementInput.getMoveStrafe() - 1);
        }

        movementInput.setJump(Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode()));
        movementInput.setSneak(sneak.get() && Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode()) || mc.gameSettings.keyBindSneak.isKeyDown());

        if (movementInput.sneak() && !isEnabled(FastSneak.class)) {
            movementInput.setMoveStrafe(movementInput.getMoveStrafe() * 0.3F);
            movementInput.setMoveForward(movementInput.getMoveForward() * 0.3F);
        }
    }
}
