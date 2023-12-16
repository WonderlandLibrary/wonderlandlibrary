package wtf.expensive.command.impl;

import net.minecraft.util.text.TextFormatting;
import org.lwjgl.glfw.GLFW;
import wtf.expensive.command.Command;
import wtf.expensive.command.CommandInfo;
import wtf.expensive.managment.Managment;
import wtf.expensive.modules.Function;
import wtf.expensive.util.math.KeyMappings;

/**
 * @author dedinside
 * @since 25.06.2023
 */
@CommandInfo(name = "reload", description = "Перезагрузка плагина")
public class ReloadCommand extends Command {
    @Override
    public void run(String[] args) throws Exception {
        Managment.SCRIPT_MANAGER.reload();
        sendMessage("Все скрипты перезагружены.");
    }

    @Override
    public void error() {

    }
}