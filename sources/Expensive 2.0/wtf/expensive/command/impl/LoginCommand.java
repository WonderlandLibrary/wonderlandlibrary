package wtf.expensive.command.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.RandomStringUtils;
import wtf.expensive.command.Command;
import wtf.expensive.command.CommandInfo;

import java.util.UUID;

/**
 * @author dedinside
 * @since 25.06.2023
 */
@CommandInfo(name = "l", description = "Позволяет сменить ник прямо во время игры")

public class LoginCommand extends Command {
    @Override
    public void run(String[] args) throws Exception {
        String username;
        if (args[1].equalsIgnoreCase("rand")) {
            username = "EXPRel" + RandomStringUtils.randomAlphabetic(5);
        } else if (args.length == 2 && args[1].length() < 20) {
            username = args[1];
        } else {
            error();
            return;
        }
        String uuid = UUID.randomUUID().toString();
        Minecraft.getInstance().session = new Session(username, uuid, "", "mojang");
        sendMessage(TextFormatting.GREEN + "Вы успешно вошли как " + TextFormatting.WHITE + Minecraft.getInstance().session.getUsername());
    }

    @Override
    public void error() {
        sendMessage(TextFormatting.GRAY + "Ошибка в использовании" + TextFormatting.WHITE + ":");

        sendMessage(TextFormatting.WHITE + ".l " + TextFormatting.GRAY + "<"
                + "name" + TextFormatting.GRAY + ">");

        sendMessage(TextFormatting.WHITE + ".l rand" + TextFormatting.GRAY + " (Генерирует случайное имя)");
    }
}
