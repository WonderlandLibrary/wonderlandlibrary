package org.dreamcore.client.feature.impl.misc;

import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.lang3.RandomStringUtils;
import org.lwjgl.Sys;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventUpdate;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.ListSetting;
import org.dreamcore.client.settings.impl.NumberSetting;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Spammer extends Feature {

    private final NumberSetting delay;
    private final BooleanSetting randomSymbols;
    private final ListSetting spammerMode;
    private int ticks, counter;

    public Spammer() {
        super("Spammer", "Автоматически спамит сообщениями в чат", Type.Misc);
        spammerMode = new ListSetting("Spammer Mode", "Default", () -> true, "Default", "HvH?", "Custom", "Direct");
        delay = new NumberSetting("Spammer Delay", 100, 10, 500, 10, () -> true);
        randomSymbols = new BooleanSetting("Random Symbols", true, () -> spammerMode.currentMode.equals("Custom"));
        addSettings(spammerMode, delay, randomSymbols);
    }

    private List<EntityPlayer> getPlayerByTab() {
        ArrayList<EntityPlayer> list = new ArrayList<>();
        for (NetworkPlayerInfo info : mc.player.connection.getPlayerInfoMap()) {
            if (info == null) {
                continue;
            }
            list.add(mc.world.getPlayerEntityByName(info.getGameProfile().getName()));
        }
        return list;
    }

    @EventTarget
    public void onUpdate(EventUpdate event) throws IOException {
        String mode = spammerMode.getOptions();
        this.setSuffix(mode + ", " + (int) delay.getNumberValue());
        if (mode.equalsIgnoreCase("Default")) {
            try {
                String str1 = RandomStringUtils.randomAlphabetic(3);
                String str2 = RandomStringUtils.randomPrint(5);
                this.setSuffix("" + (int) delay.getNumberValue());
                if (ticks++ % (int) delay.getNumberValue() == 0) {
                    mc.player.sendChatMessage("![" + str1 + "]" + "`vk.com/dreamcore ` " + "[" + str2 + "]");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (mode.equalsIgnoreCase("HvH?")) {
            String str1 = RandomStringUtils.randomAlphabetic(3);
            String str2 = RandomStringUtils.randomPrint(5);
            String str3 = "";
            this.setSuffix("" + (int) delay.getNumberValue());
            if (ticks++ % (int) delay.getNumberValue() == 0) {
                switch (counter) {
                    case 0:
                        mc.player.sendChatMessage(str3 + "!Твой клиент зaлупa ебaная)) Кид@й мнe дyэль: \"/duel " + mc.player.getName() + "\".  Карта: Пляж " + " [" + str1 + "]" + " [" + str2 + "]");
                        counter++;
                        break;
                    case 1:
                        mc.player.sendChatMessage(str3 + "!Правда думаешь твой клиент лучше?) Кидaй мне дуэль: \"/duel " + mc.player.getName() + "\". Карта: Пляж " + " [" + str1 + "]" + " [" + str2 + "]");
                        counter++;
                        break;
                    case 2:
                        mc.player.sendChatMessage(str3 + "!Ты как себя ведешь бл9дина eбaнaя? Кiдай мне дуэль: \"/duel " + mc.player.getName() + "\".  Карта: Пляж " + " [" + str1 + "]" + " [" + str2 + "]");
                        counter = 0;
                        break;
                }
            }
        } else if (mode.equalsIgnoreCase("Custom")) {
            String str1 = RandomStringUtils.randomAlphabetic(3);
            String str2 = RandomStringUtils.randomPrint(5);
            File file = new File(mc.mcDataDir + "\\dreamcore", "spammer.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                if (ticks++ % (int) delay.getNumberValue() == 0) {
                    if (counter == 0) {
                        if (randomSymbols.getBoolValue()) {
                            mc.player.sendChatMessage("! '" + scanner.nextLine() + "' " + str2 + str1);
                        } else {
                            mc.player.sendChatMessage(scanner.nextLine());
                        }
                        counter = 0;
                    }
                }
                scanner.close();
            }
        } else if (mode.equalsIgnoreCase("Direct")) {
            for (EntityPlayer e : this.getPlayerByTab()) {
                if (e == null)
                    continue;
                if (ticks++ % (int) delay.getNumberValue() == 0) {
                    if (e != mc.player) {
                        mc.player.sendChatMessage("/msg " + e.getName() + " vk.com/dreamcore лучший чит, быстрее покупай!");
                    }
                }
            }
        }
    }

    @Override
    public void onEnable() {
        String mode = spammerMode.getOptions();
        if (mode.equalsIgnoreCase("Custom")) {
            Sys.openURL(mc.mcDataDir + "\\dreamcore\\spammer.txt");
        }
        super.onEnable();
    }
}