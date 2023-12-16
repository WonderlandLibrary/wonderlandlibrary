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
@CommandInfo(name = "bind", description = "Позволяет забиндить модуль на определенную клавишу")
public class BindCommand extends Command {
    @Override
    public void run(String[] args) throws Exception {
        try {
            if (args.length >= 2) {
                switch (args[1].toLowerCase()) {
                    case "list" -> listBoundKeys();
                    case "clear" -> clearAllBindings();
                    case "add" -> {
                        if (args.length >= 4) {
                            addKeyBinding(args[2], args[3]);
                        } else {
                            error();
                        }
                    }
                    case "remove" -> {
                        if (args.length >= 4) {
                            removeKeyBinding(args[2], args[3]);
                        } else {
                            error();
                        }
                    }
                    default -> error();
                }
            } else {
                error();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод для вывода списка модулей с привязанными клавишами
     */
    private void listBoundKeys() {
        sendMessage(TextFormatting.GRAY + "Список всех модулей с привязанными клавишами:");
        for (Function f : Managment.FUNCTION_MANAGER.getFunctions()) {
            if (f.bind == 0) continue;
            sendMessage(f.name + " [" + TextFormatting.GRAY + (GLFW.glfwGetKeyName(f.bind, -1) == null ? "" : GLFW.glfwGetKeyName(f.bind, -1)) + TextFormatting.RESET + "]");
        }
    }

    /**
     * Метод для очистки всех привязок клавиш
     */
    private void clearAllBindings() {
        for (Function f : Managment.FUNCTION_MANAGER.getFunctions()) {
            f.bind = 0;
        }
        sendMessage(TextFormatting.GREEN + "Все клавиши были отвязаны от модулей");
    }

    /**
     * Метод для добавления привязки клавиши к модулю
     *
     * @param moduleName имя модуля
     * @param keyName    название клавиши
     */
    private void addKeyBinding(String moduleName, String keyName) {
        Integer key = null;

        try {
            key = KeyMappings.keyMap.get(keyName.toUpperCase());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Function module = Managment.FUNCTION_MANAGER.get(moduleName);
        if (key != null) {
            if (module != null) {
                module.bind = key;
                sendMessage("Клавиша " + TextFormatting.GRAY + keyName + TextFormatting.WHITE + " была привязана к модулю " + TextFormatting.GRAY + module.name);
            } else {
                sendMessage("Модуль " + moduleName + " не был найден");
            }
        } else {
            sendMessage("Клавиша " + keyName + " не была найдена!");
        }
    }

    /**
     * Метод для удаления привязки клавиши
     *
     * @param moduleName имя модуля
     * @param keyName    название клавиши
     */
    private void removeKeyBinding(String moduleName, String keyName) {
        for (Function f : Managment.FUNCTION_MANAGER.getFunctions()) {
            if (f.name.equalsIgnoreCase(moduleName)) {
                f.bind = 0;
                sendMessage("Клавиша " + TextFormatting.GRAY + keyName + TextFormatting.RESET + " была отвязана от модуля " + TextFormatting.GRAY + f.name);
            }
        }
    }

    /**
     * Метод для обработки ошибки неверного синтаксиса команды
     */
    @Override
    public void error() {
        sendMessage(TextFormatting.WHITE + "Неверный синтаксис команды. " + TextFormatting.GRAY + "Используйте:");
        sendMessage(TextFormatting.WHITE + ".bind add " + TextFormatting.DARK_GRAY + "<name> <key>");
        sendMessage(TextFormatting.WHITE + ".bind remove " + TextFormatting.DARK_GRAY + "<name> <key>");
        sendMessage(TextFormatting.WHITE + ".bind list");
        sendMessage(TextFormatting.WHITE + ".bind clear");
    }
}