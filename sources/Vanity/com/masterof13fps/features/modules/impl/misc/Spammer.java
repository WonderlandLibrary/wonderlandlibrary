package com.masterof13fps.features.modules.impl.misc;

import com.masterof13fps.Client;
import com.masterof13fps.features.modules.Category;
import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventTick;
import com.masterof13fps.manager.notificationmanager.NotificationType;
import com.masterof13fps.manager.settingsmanager.Setting;
import com.masterof13fps.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ModuleInfo(name = "Spammer", category = Category.MISC, description = "Spams the chat with pre-defined messages")
public class Spammer extends Module {

    Setting mode = new Setting("Mode", this, "Normal", new String[]{"Normal", "Memes", "Custom"});
    Setting antiSpam = new Setting("Anti Spam", this, true);
    Setting delay = new Setting("Delay", this, 2, 0.1, 20, false);

    String[] messages = {"Got Rekt? Get " + getClientName() + "!", "Enjoy Eskay!", "N00B G3T R3KT", "www" +
            ".masterof13fps" +
            ".com", "Sub " +
            "CrazyMemeCoke on YT!"};
    String[] memeMessages = {"Pastebin, weil Ich ein Paste bin", "Ja Ok, Ich bin 12", "Ich fand Nero dumm, und jetzt " +
            "ist er auch noch Asiate", "Jannick hat eine Gomme Aura", "Ich weiß, woran's liegt ...", "Du kannst aus " +
            "jeder Schlampe eine Bitch machen ...", "Ich glaub Ich muss noch meine Tabletten nehmen.", "Spiel mit " +
            "mir, Ich bin performanter als Eject", "Ich bin Jannick, mich besteigt man.", "Kein Ding für'n Ping.",
            "Woran erkennt man wo Osten ist? Man legt eine Banane auf die Mauer und guckt wo abgebissen wird.",
            "Welcher Client ist unperformanter als Eject?", "Ich hab das Video nur am Handy installiert."};

    int lastUsed;

    private File customSpamFile;

    private String randomPhrase() {
        Random rand = new Random();
        int randInt = 0;
        switch (mode.getCurrentMode()) {
            case "Normal": {
                randInt = rand.nextInt(messages.length);
                while (lastUsed == randInt) {
                    randInt = rand.nextInt(messages.length);
                }
                lastUsed = randInt;
                return messages[randInt];
            }
            case "Memes": {
                randInt = rand.nextInt(memeMessages.length);
                while (lastUsed == randInt) {
                    randInt = rand.nextInt(memeMessages.length);
                }
                lastUsed = randInt;
                return memeMessages[randInt];
            }
            case "Custom": {
                List<String> lines = FileUtils.loadFile(customSpamFile);
                ArrayList<String> customMessages = new ArrayList<>();

                lines.forEach(line -> {
                    customMessages.add(line);
                });

                randInt = rand.nextInt(customMessages.size());
                while (lastUsed == randInt) {
                    randInt = rand.nextInt(customMessages.size());
                }
                lastUsed = randInt;
                return customMessages.get(randInt);
            }
        }
        return null;
    }

    @Override
    public void onToggle() {

    }

    @Override
    public void onEnable() {
        try {
            customSpamFile = new File(Client.main().getClientDir() + "/messages.txt");
            if (customSpamFile.createNewFile()) {
                notify.debug("File created: " + customSpamFile.getName());
            } else {
                notify.debug("File \"messages.txt\" already exists.");
            }
        } catch (IOException e) {
            notify.debug("An error occurred.");
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventTick) {
            List<String> lines = FileUtils.loadFile(customSpamFile);

            if (timeHelper.hasReached((long) (delay.getCurrentValue() * 1000))) {
                if (mode.getCurrentMode().equalsIgnoreCase("Custom") && lines.isEmpty()) {
                    notify.notification("Keine Spammer Messages", "Keine Messages gefunden, lege diese im Client-Verzeichnis manuell an!", NotificationType.ERROR, 5);
                    toggle();
                    return;
                }

                if (mode.getCurrentMode().equalsIgnoreCase("Custom") && lines.size() == 1) {
                    notify.notification("Zu wenig Spammer Messages", "Es müssen mindestens 2 Nachrichten in der \"messages.txt\" enthalten sein!", NotificationType.ERROR, 5);
                    toggle();
                    return;
                }

                if (antiSpam.isToggled()) {
                    Random random = new Random();
                    int randomInt = random.nextInt(2000) + 2000;
                    sendChatMessage(randomPhrase() + " | " + randomInt);
                } else {
                    sendChatMessage(randomPhrase());
                }

                timeHelper.reset();
            }
        }
    }
}
