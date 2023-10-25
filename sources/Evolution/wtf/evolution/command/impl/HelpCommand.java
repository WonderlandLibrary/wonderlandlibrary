package wtf.evolution.command.impl;

import wtf.evolution.command.Command;
import wtf.evolution.command.CommandInfo;
import wtf.evolution.helpers.ChatUtil;

@CommandInfo(name = "help")
public class HelpCommand extends Command {

    @Override
    public void execute(String[] args) {
        super.execute(args);
        ChatUtil.print("Список команд: ");
        ChatUtil.print(".vclip <дистанция> - вертикальная телепортация");
        ChatUtil.print(".hclip <дистанция> - горизонтальная телепортация");
        ChatUtil.print(".toggle <имя модуля> - включить/выключить модуль");
        ChatUtil.print(".crash - краш сервера с помощью скинов");
        ChatUtil.print(".help - показать список команд");
        ChatUtil.print(".config load <имя конфига> - загрузить конфиг");
        ChatUtil.print(".config save <имя конфига> - сохранить конфиг");
        ChatUtil.print(".config list - список конфигов");
        ChatUtil.print(".friend <имя друга> - добавить/удалить друга");
        ChatUtil.print(".bind <имя модуля> <клавиша> - назначить модуль на клавишу");
        ChatUtil.print(".set <имя модуля> <тип настройки> <имя настройки> <значение> - изменить настройку модуля");
        ChatUtil.print(".kick <ник> - кикает игрока через эксплойт");
        ChatUtil.print(".bot join <колво> - создает ботов которые заходят на сервер");
        ChatUtil.print(".bot message <сообщение> - отправляет сообщение от всех ботов");
        ChatUtil.print(".bot jump - прыгает за всех ботов");
        ChatUtil.print(".bot kick - кикает всех ботов");
        ChatUtil.print(".bot follow - боты ходят за вами");
        ChatUtil.print(".bot look - боты смотрят на вас");
        ChatUtil.print(".bot slot <слот> - боты меняют руку");
        ChatUtil.print(".bot swap - боты используют предмет");
        ChatUtil.print(".bot random - боты просто бегают");
        ChatUtil.print(".bot strafe - боты делают карусель :)");
        ChatUtil.print(".bot mimic - боты повторяют движения");

    }
}
