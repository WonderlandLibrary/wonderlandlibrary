package koks.module.misc;

import koks.Koks;
import koks.api.event.Event;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;
import koks.api.utils.RandomUtil;
import koks.api.utils.TimeHelper;
import koks.event.UpdateEvent;
import net.minecraft.world.gen.structure.StructureVillagePieces;

import java.io.*;
import java.util.*;

@Module.Info(name = "Spammer", description = "You spam the chat full", category = Module.Category.MISC)
public class Spammer extends Module implements Module.NotUnToggle {

    @Value(name = "Delay", minimum = 0, maximum = 10000)
    int delay = 1000;

    @Value(name = "Custom Words")
    boolean customWords = false;

    @Value(name = "Randomizing")
    boolean randomizing = true;

    @Value(name = "RandomChoose")
    boolean randomChoose = false;

    @Value(name = "RandomNumbers")
    boolean randomNumbers = false;

    public final TimeHelper timeHelper = new TimeHelper();

    public int count;

    public final ArrayList<String> words = new ArrayList<>();

    @Override
    @Event.Info
    public void onEvent(Event event) {
        final RandomUtil randomUtil = RandomUtil.getInstance();
        if (event instanceof UpdateEvent) {
            if (words.size() != 0 && timeHelper.hasReached((long) (delay + (randomizing ? randomUtil.getRandomGaussian(20) : 0)))) {
                getPlayer().sendChatMessage((randomChoose ? words.get(randomUtil.getRandomInteger(0, words.size() - 1)) : words.get(count)) + (randomNumbers ? " | " + randomUtil.getRandomInteger(0, 1337) : ""));
                count++;
                if(count > words.size() - 1)
                    count = 0;
                timeHelper.reset();
            }
        }
    }

    @Override
    public void onEnable() {
        words.clear();
        final File file = new File(Koks.getKoks().DIR, "words.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
                if(customWords)
                    setToggled(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(customWords) {
            try {
                final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    words.add(line);
                }
                bufferedReader.close();
            } catch (IOException e) {
                sendError("File not exist", "please create a words.txt file");
            }
        } else {
            words.add("Nein Koks ist keine illegale Droge! Das ist ein Abfallprodukt ausm Bergbau ;)");
            words.add("Ich nehme gerne Koks");
            words.add("Koksig unterwegs meine Jungspunds");
            words.add("Bitte Abonniert mich auf YouTube :)");
            words.add("Haze verkauft nun Vanilla 1.8.9 für 35€");
            words.add("Bei Beschwerden oder Problemen bei Haze melden, ist nicht mehr unser Client!");
            words.add("--AteroIsBest");
            words.add("hm i have the best gomme client without bans hahaha");
            words.add("Paul nimmt Koks, Paul ist cool, sei so wie Paul sei cool");
            words.add("Pablo Escobar hat 80% Anteil des Kokses");
            words.add("Pastebin, weil ich paste bin");
            words.add("Ich spiele jetzt legit! Genau so wie LCA");
            words.add("Nicht vergessen, den Aluhelm aufzusetzen!");
            words.add("Bist du weiblich?");
            words.add("187 = das sind die mit dem Holz");
            words.add("Ih find den chäm geiel");
            words.add("Ja ok, ich bin 12!");
            words.add("Hackerman am start!");
            words.add("Ich bin ein echter Hacker, da ich Kali Linux use");
            words.add("Ich hacke garnicht, ich habe nur einen guten Gaming Chair!");
            words.add("Ich mache meinen GCD Fix mit Moduli!");
            words.add("Du kriegst legale Probleme!");
            words.add("because you are trying to take revenge here, and I can be a real asshole if I want");
            words.add("I think i Spider!");
            words.add("GHG dich");
            words.add("Stell dir vor ich Cheate mit Koks! Ich hab gesagt stell es dir vor! Zitter nicht!");
            final Calendar calendar = Calendar.getInstance();
            if(calendar.get(Calendar.MONTH) == Calendar.SEPTEMBER && calendar.get(Calendar.DAY_OF_MONTH) == 11) {
                words.add("Happy 9/11 meine Arabischen Freunde!");
                words.add("Ich spiele gerne Jenga World Trade Center Edition");
                words.add("Open wide, here comes the Airplane");
                words.add("Jemand hat mir eine Fangfrage über 9/11 gestellt, ich wäre fast drauf Reingeflogen");
                words.add("Dieser Moment wenn man im world Trader Center sitzt und plötzlich Flugzeug WLAN hat");
                words.add("Don't make 9/11 memes, pls my dad died there... He was the best pilot in all of Saudi Arabia");
                words.add("Wollt ihr einen Witz hören? Meiner ist bereits im World Trade Center gut angekommen :)");
            }
        }
        Collections.shuffle(words);

        count = 0;
    }

    @Override
    public void onDisable() {

    }

}
