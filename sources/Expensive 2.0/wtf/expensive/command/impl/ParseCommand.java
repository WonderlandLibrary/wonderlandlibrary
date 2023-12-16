package wtf.expensive.command.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.util.text.TextFormatting;
import wtf.expensive.command.Command;
import wtf.expensive.command.CommandInfo;
import wtf.expensive.config.ConfigManager;
import wtf.expensive.util.ClientUtil;
import wtf.expensive.util.IMinecraft;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@CommandInfo(name = "parse", description = "Парсит игроков с таба")
public class ParseCommand extends Command implements IMinecraft {


    @Override
    public void run(String[] args) throws Exception {
        final File PARSE_DIR = new File(Minecraft.getInstance().gameDir, "\\expensive\\parser");

        if (!PARSE_DIR.exists()) {
            PARSE_DIR.mkdirs();
        }
        if (args.length > 1) {
            switch (args[1].toLowerCase()) {
                case "start" -> {
                    Collection<NetworkPlayerInfo> playerInfos = mc.player.connection.getPlayerInfoMap();
                    List<String> donate = new ArrayList<>();

                    File file = new File(PARSE_DIR, mc.getCurrentServerData().serverIP + ".txt");
                    FileWriter fileWriter = new FileWriter(file);

                    for (NetworkPlayerInfo playerInfo : playerInfos) {
                        if (playerInfo.getPlayerTeam().getPrefix().getString().length() >= 3) {
                            String text = TextFormatting.getTextWithoutFormattingCodes(playerInfo.getPlayerTeam().getPrefix().getString().substring(0, playerInfo.getPlayerTeam().getPrefix().getString().length() - 1));
                            if (!donate.contains(text))
                                donate.add(text);
                        }
                    }
                    if (donate.size() == 0) {
                        ClientUtil.sendMesage("Донатеры в табе не найдены!");
                        return;
                    }

                    try {
                        for (String don : donate) {
                            fileWriter.append("// " + don + "\n\n");
                            for (NetworkPlayerInfo playerInfo : playerInfos) {
                                if (playerInfo.getPlayerTeam().getPrefix().getString().contains(don))
                                    fileWriter.append(playerInfo.getGameProfile().getName() + "\n");
                            }
                            fileWriter.append("\n");
                        }

                        fileWriter.flush();
                        fileWriter.close();
                        try {
                            Runtime.getRuntime().exec("explorer " + PARSE_DIR.getAbsolutePath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ClientUtil.sendMesage("Успешно.");
                    } catch (Exception e) {
                        ClientUtil.sendMesage("Ошибка. Обратитесь к разработчику");
                        ClientUtil.sendMesage(e.getMessage());
                    }
                }
                case "dir" -> {
                    try {
                        Runtime.getRuntime().exec("explorer " + PARSE_DIR.getAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            error();
        }
    }

    @Override
    public void error() {
        sendMessage(TextFormatting.GRAY + "Ошибка в использовании" + TextFormatting.WHITE + ":");
        sendMessage(TextFormatting.WHITE + "." + "parse start" + TextFormatting.GRAY
                + " - запустить парсер");
        sendMessage(TextFormatting.WHITE + "." + "parse dir" + TextFormatting.GRAY
                + " - открыть папку с серверами");
    }
}
