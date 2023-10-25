/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.misc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.lang3.RandomStringUtils;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;
import org.lwjgl.Sys;

public class Spammer
extends Feature {
    private final NumberSetting delay;
    private final BooleanSetting randomSymbols;
    private final ListSetting spammerMode = new ListSetting("Spammer Mode", "Default", () -> true, "Default", "HvH?", "Custom", "Direct");
    private int ticks;
    private int counter;

    public Spammer() {
        super("Spammer", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0441\u043f\u0430\u043c\u0438\u0442 \u0441\u043e\u043e\u0431\u0449\u0435\u043d\u0438\u044f\u043c\u0438 \u0432 \u0447\u0430\u0442", Type.Misc);
        this.delay = new NumberSetting("Spammer Delay", 100.0f, 10.0f, 500.0f, 10.0f, () -> true);
        this.randomSymbols = new BooleanSetting("Random Symbols", true, () -> this.spammerMode.currentMode.equals("Custom"));
        this.addSettings(this.spammerMode, this.delay, this.randomSymbols);
    }

    private List<EntityPlayer> getPlayerByTab() {
        ArrayList<EntityPlayer> list = new ArrayList<EntityPlayer>();
        for (NetworkPlayerInfo info : Spammer.mc.player.connection.getPlayerInfoMap()) {
            if (info == null) continue;
            list.add(Spammer.mc.world.getPlayerEntityByName(info.getGameProfile().getName()));
        }
        return list;
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String mode = this.spammerMode.getOptions();
        this.setSuffix(mode + ", " + (int)this.delay.getCurrentValue());
        if (mode.equalsIgnoreCase("Default")) {
            try {
                String str1 = RandomStringUtils.randomAlphabetic(3);
                String str2 = RandomStringUtils.randomPrint(5);
                this.setSuffix("" + (int)this.delay.getCurrentValue());
                if (this.ticks++ % (int)this.delay.getCurrentValue() == 0 && this.counter == 0) {
                    Spammer.mc.player.sendChatMessage("![" + str1 + "]`vk.com/celestialclient ` [" + str2 + "]");
                    this.counter = 0;
                }
            }
            catch (Exception str1) {}
        } else if (mode.equalsIgnoreCase("HvH?")) {
            String str1 = RandomStringUtils.randomAlphabetic(3);
            String str2 = RandomStringUtils.randomPrint(5);
            String str3 = "";
            this.setSuffix("" + (int)this.delay.getCurrentValue());
            if (this.ticks++ % (int)this.delay.getCurrentValue() == 0) {
                switch (this.counter) {
                    case 0: {
                        Spammer.mc.player.sendChatMessage(str3 + "!\u0422\u0432\u043e\u0439 \u043a\u043b\u0438\u0435\u043d\u0442 \u0437a\u043b\u0443\u043fa \u0435\u0431a\u043d\u0430\u044f)) \u041a\u0438\u0434@\u0439 \u043c\u043de \u0434y\u044d\u043b\u044c: \"/duel " + Spammer.mc.player.getName() + "\".  \u041a\u0430\u0440\u0442\u0430: \u041f\u043b\u044f\u0436  [" + str1 + "] [" + str2 + "]");
                        ++this.counter;
                        break;
                    }
                    case 1: {
                        Spammer.mc.player.sendChatMessage(str3 + "!\u041f\u0440\u0430\u0432\u0434\u0430 \u0434\u0443\u043c\u0430\u0435\u0448\u044c \u0442\u0432\u043e\u0439 \u043a\u043b\u0438\u0435\u043d\u0442 \u043b\u0443\u0447\u0448\u0435?) \u041a\u0438\u0434a\u0439 \u043c\u043d\u0435 \u0434\u0443\u044d\u043b\u044c: \"/duel " + Spammer.mc.player.getName() + "\". \u041a\u0430\u0440\u0442\u0430: \u041f\u043b\u044f\u0436  [" + str1 + "] [" + str2 + "]");
                        ++this.counter;
                        break;
                    }
                    case 2: {
                        Spammer.mc.player.sendChatMessage(str3 + "!\u0422\u044b \u043a\u0430\u043a \u0441\u0435\u0431\u044f \u0432\u0435\u0434\u0435\u0448\u044c \u0431\u043b9\u0434\u0438\u043d\u0430 e\u0431a\u043da\u044f? \u041ai\u0434\u0430\u0439 \u043c\u043d\u0435 \u0434\u0443\u044d\u043b\u044c: \"/duel " + Spammer.mc.player.getName() + "\".  \u041a\u0430\u0440\u0442\u0430: \u041f\u043b\u044f\u0436  [" + str1 + "] [" + str2 + "]");
                        this.counter = 0;
                    }
                }
            }
        } else if (mode.equalsIgnoreCase("Custom")) {
            try {
                String str1 = RandomStringUtils.randomAlphabetic(3);
                String str2 = RandomStringUtils.randomPrint(5);
                File file = new File(Spammer.mc.gameDir + "\\celestial", "Spammer.json");
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    if (this.ticks++ % (int)this.delay.getCurrentValue() == 0 && this.counter == 0) {
                        if (this.randomSymbols.getCurrentValue()) {
                            Spammer.mc.player.sendChatMessage("! '" + scanner.nextLine() + "' " + str2 + str1);
                        } else {
                            Spammer.mc.player.sendChatMessage(scanner.nextLine());
                        }
                        this.counter = 0;
                    }
                    scanner.close();
                }
            }
            catch (Exception exception) {}
        } else if (mode.equalsIgnoreCase("Direct")) {
            for (EntityPlayer e : this.getPlayerByTab()) {
                if (e == null || this.ticks++ % (int)this.delay.getCurrentValue() != 0 || e == Spammer.mc.player) continue;
                try {
                    File file = new File(Spammer.mc.gameDir + "\\celestial", "Spammer.json");
                    Scanner scanner = new Scanner(file);
                    while (scanner.hasNextLine()) {
                        Spammer.mc.player.sendChatMessage("/msg " + e.getName() + " " + scanner.nextLine());
                    }
                }
                catch (Exception exception) {
                }
            }
        }
    }

    @Override
    public void onEnable() {
        if (this.spammerMode.currentMode.equals("Custom") || this.spammerMode.currentMode.equals("Direct")) {
            try {
                File file = new File(Spammer.mc.gameDir + "\\celestial", "Spammer.json");
                if (!file.exists()) {
                    file.createNewFile();
                }
                Sys.openURL(file.getAbsolutePath());
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        super.onEnable();
    }
}

