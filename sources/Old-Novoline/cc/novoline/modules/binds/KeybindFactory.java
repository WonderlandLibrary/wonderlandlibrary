package cc.novoline.modules.binds;

import org.jetbrains.annotations.NotNull;

/**
 * @author xDelsy
 */
public final class KeybindFactory {

	public static @NotNull MouseKeybind mouse(int key) {
		return MouseKeybind.of(key);
	}

	public static @NotNull KeyboardKeybind keyboard(int key) {
		return KeyboardKeybind.of(key);
	}

	private KeybindFactory() {
		throw new java.lang.UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}
}
