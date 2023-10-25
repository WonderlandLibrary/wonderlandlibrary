package ru.smertnix.celestial.command.impl;


import com.mojang.realmsclient.gui.ChatFormatting;

import ru.smertnix.celestial.command.CommandAbstract;
import ru.smertnix.celestial.utils.other.ChatUtils;

public class HelpCommand
        extends CommandAbstract {
    public HelpCommand() {
        super("help", "help", ".help", "help");
    }

    @Override
    public void execute(String... args) {
        if (args.length == 1) {
            if (args[0].equals("help")) {
            	 ChatUtils.addChatMessage("--- Help Manager ---");
                 ChatUtils.addChatMessage(ChatFormatting.WHITE + "hclip | Телепортирует вас вперед на определеные координаты");
                 ChatUtils.addChatMessage(ChatFormatting.WHITE + "help | Узнать весь список доступных команд");
                 ChatUtils.addChatMessage(ChatFormatting.WHITE + "macro | Добавить макрос");
                 ChatUtils.addChatMessage(ChatFormatting.WHITE + "bind | Добавить бинд функции");
                 ChatUtils.addChatMessage(ChatFormatting.WHITE + "cfg | Добавить нужную конфигурацию");
                 ChatUtils.addChatMessage(ChatFormatting.WHITE + "friend | Добавить человека в друзья");
                 ChatUtils.addChatMessage(ChatFormatting.WHITE + "vclip | Телепортирует игрока вверх или вниз");
                 ChatUtils.addChatMessage(ChatFormatting.WHITE + "way | Рисует стрелку до назначенных координат");
            }
        } else {
            ChatUtils.addChatMessage("--- Help Manager ---");
            ChatUtils.addChatMessage(ChatFormatting.WHITE + "hclip | Телепортирует вас вперед на определеные координаты");
            ChatUtils.addChatMessage(ChatFormatting.WHITE + "help | Узнать весь список доступных команд");
            ChatUtils.addChatMessage(ChatFormatting.WHITE + "macro | Добавить макрос");
            ChatUtils.addChatMessage(ChatFormatting.WHITE + "bind | Добавить бинд функции");
            ChatUtils.addChatMessage(ChatFormatting.WHITE + "cfg | Добавить нужную конфигурацию");
            ChatUtils.addChatMessage(ChatFormatting.WHITE + "friend | Добавить человека в друзья");
            ChatUtils.addChatMessage(ChatFormatting.WHITE + "vclip | Телепортирует игрока вверх или вниз");
            ChatUtils.addChatMessage(ChatFormatting.WHITE + "way | Рисует стрелку до назначенных координат");
        }
    }
}
