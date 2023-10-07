package cc.novoline.modules.configurations.property.object;

import cc.novoline.modules.binds.*;
import cc.novoline.modules.configurations.property.AbstractProperty;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * @author Artyom Popov
 * @since March 14, 2021 @ 11:34 AM
 */
public final class KeyBindProperty extends AbstractProperty<ModuleKeybind> {

	@Contract(pure = true)
	public KeyBindProperty(@Nullable ModuleKeybind value) {
		super(value);
	}

	public boolean isHeld() {
		ModuleKeybind value = this.value;
		int key = value.getKey();

		if(value instanceof KeyboardKeybind) {
			return Keyboard.isKeyDown(key);
		} else {
			throw new UnsupportedOperationException();
		}
	}
}
