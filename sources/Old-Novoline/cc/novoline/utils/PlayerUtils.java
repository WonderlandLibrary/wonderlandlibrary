package cc.novoline.utils;

import cc.novoline.Novoline;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.binds.KeyboardKeybind;
import cc.novoline.modules.configurations.holder.ModuleHolder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

public final class PlayerUtils {

	private static int pressCount;

	public static void onBindInGui() {
		if(Keyboard.getEventKeyState()) {
			int k = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();

			for(ModuleHolder<?> holder : Novoline.getInstance().getModuleManager().getModuleManager().values()) {
				AbstractModule module = holder.getModule();

				//noinspection InstanceofConcreteClass
				if(module.getKeybind().get() instanceof KeyboardKeybind && module.getKeybind().get().getKey() == k) {
					if(Keyboard.isKeyDown(k)) {
						pressCount++;
					}

					if(pressCount == 1) {
						module.toggle();
					}
				}
			}
		} else {
			pressCount = 0;
		}
	}

	public static boolean inTeam(@NotNull ICommandSender entity0, @NotNull ICommandSender entity1) {
		String s = "\u00a7" + teamColor(entity0);

		return     entity0.getDisplayName().getFormattedText().contains(s)
				&& entity1.getDisplayName().getFormattedText().contains(s);
	}

	public static boolean inTeamWithMinecraftPlayer(@NotNull ICommandSender entity) {
		return inTeam(Minecraft.getInstance().player, entity);
	}

	public static @NotNull String teamColor(@NotNull ICommandSender player) {
		Matcher matcher = Pattern.compile("\u00a7(.).*\u00a7r").matcher(player.getDisplayName().getFormattedText());
		return matcher.find() ? matcher.group(1) : "f";
	}

	@Contract(value = "-> fail", pure = true)
	private PlayerUtils() {
		throw new java.lang.UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}
}
