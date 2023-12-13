package me.nyan.flush.module.impl.misc;

import me.nyan.flush.Flush;
import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventUpdate;
import me.nyan.flush.event.impl.EventWorldChange;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.utils.other.Timer;
import net.minecraft.network.play.client.C01PacketChatMessage;

import java.io.*;
import java.util.Random;

public class Spammer extends Module {
    private String message = "flu client or something idk";
    private final File dataFile;

    private final Random random;
    private final Timer timer;

    private final NumberSetting spammerDelay = new NumberSetting("Delay", this, 800, 0, 10000, 10);
    private final BooleanSetting antiSpamBypass = new BooleanSetting("AntiSpam Bypass", this, true);

    public Spammer() {
        super("Spammer", Category.MISC);
        random = new Random();
        timer = new Timer();
        dataFile = new File(Flush.getClientPath(), "spammer.txt");

        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        load();
    }

    @SubscribeEvent
    public void onUpdate(EventUpdate e) {
        if (timer.hasTimeElapsed(spammerDelay.getValueInt(), true))
            mc.getNetHandler().addToSendQueue(new C01PacketChatMessage(message + (antiSpamBypass.getValue() ? " >" + (random.ints(
                    97, 123).limit(6).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString()) + "<" : "")));
    }


    public void save() {
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            PrintWriter printWriter = new PrintWriter(dataFile);
            printWriter.println(message);
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        if (!dataFile.exists())
            return;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(dataFile));
            String line = reader.readLine();
            if (line != null)
                message = line;
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SubscribeEvent
    public void onWorldChange(EventWorldChange e) {
        disable();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}