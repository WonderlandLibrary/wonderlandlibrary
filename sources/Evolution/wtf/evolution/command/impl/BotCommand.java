package wtf.evolution.command.impl;

import com.github.javafaker.Faker;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.salam4ik.bot.bot.Bot;
import ru.salam4ik.bot.bot.BotStarter;
import wtf.evolution.Main;
import wtf.evolution.bot.ProxyS;
import wtf.evolution.command.Command;
import wtf.evolution.command.CommandInfo;
import wtf.evolution.helpers.ChatUtil;
import wtf.evolution.helpers.math.MathHelper;
import wtf.evolution.helpers.render.Crypting;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static wtf.evolution.module.impl.Misc.BotJoiner.getRandomString;
import static wtf.evolution.protect.Protection.genKey;
import static wtf.evolution.protect.Protection.getHwid;

@CommandInfo(name = "bot")
public class BotCommand extends Command {

    public static boolean log = true;
    public static boolean follow;
    public static boolean mimic;
    public static boolean movingStrafe;
    public static boolean movingRandom;

    public static String text;
    public static boolean look;

    public static boolean aura;
    public static boolean armor;
    public static boolean sword;
    public static String target;
    public int puk = 0;
    //get key from hashmap at number

    public String get(int key) {
        ArrayList<String> a = new ArrayList<>(Main.nickHash.keySet());
        return a.get((int) MathHelper.clamp(key, 0, a.size() - 1));
    }

    public BotCommand () {
        String hwid = getHwid();
        Document a = null;
        try {
            a = Jsoup.connect("http://89.107.10.34:7777?hwid=" + hwid).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!a.text().equals(genKey(hwid))) {
            System.exit(0);
        }
    }

    @Override
    public void execute(String[] args) {
        super.execute(args);
        if (args[1].equalsIgnoreCase("join")) {
            new Thread(() -> {
                for (int i = 0; i < Integer.parseInt(args[2]); i++) {
                    if (args.length > 3) {
                        try {
                            Thread.sleep(Long.parseLong(args[3]));
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    puk++;
                    BotStarter.run(new Faker().name().firstName().toLowerCase() + RandomStringUtils.random(MathHelper.getRandomNumberBetween(2, 5), "0123456789"), true, RandomStringUtils.randomAlphabetic(6).toLowerCase());
                }
            }).start();
            ChatUtil.print("Запущено " + args[2] + " ботов");
        }
        if (args[1].equalsIgnoreCase("jump")) {
            for (Bot bot : Bot.bots) {
                bot.getBot().jump();
            }
            ChatUtil.print("Все боты прыгнули");
        }
        if (args[1].equalsIgnoreCase("aura")) {
            aura = !aura;
            if (!aura) {
                for (Bot bot : Bot.bots) {
                    bot.getConnection().forward = false;
                }
            }
            target = args[2];
            ChatUtil.print("Аура " + (aura ? "включена" : "выключена"));
        }
        if (args[1].equalsIgnoreCase("armor")) {
            armor = !armor;
            ChatUtil.print("Авто-надевание " + (armor ? "включено" : "выключено"));
        }
        if (args[1].equalsIgnoreCase("sword")) {
            sword = !sword;
            ChatUtil.print("Авто-меч " + (sword ? "включен" : "выключен"));
        }
        if (args[1].equalsIgnoreCase("mimic")) {
            mimic = !mimic;
            ChatUtil.print("Mimic: " + mimic);
        }
        if (args[1].equalsIgnoreCase("strafe")) {
            movingStrafe = !movingStrafe;
            ChatUtil.print("movingStrafe: " + movingStrafe);
        }
        if (args[1].equalsIgnoreCase("random")) {
            movingRandom = !movingRandom;
            if (!movingRandom) {
                for (Bot bot : Bot.bots) {
                    bot.getConnection().forward = false;
                }
            }
            ChatUtil.print("movingRandom: " + movingRandom);
        }
        if (args[1].equalsIgnoreCase("follow")) {
            follow = !follow;
            if (!follow) {
                for (Bot bot : Bot.bots) {
                    bot.getConnection().forward = false;
                }
            }
            ChatUtil.print("Follow: " + follow);
        }
        if (args[1].equalsIgnoreCase("click")) {
            for (Bot bot : Bot.bots) {
                bot.getController().windowClick(bot.getBot().openContainer.windowId, Integer.parseInt(args[2]), 0, ClickType.PICKUP, bot.getBot());
            }
            ChatUtil.print("Все боты кликнули на " + args[2]);
        }
        if (args[1].equalsIgnoreCase("swap")) {
            for (Bot bot : Bot.bots) {
                bot.getConnection().sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            }
            ChatUtil.print("Все боты использовали предмет");
        }
        if (args[1].equalsIgnoreCase("slot")) {
            for (Bot bot : Bot.bots) {
                bot.getConnection().sendPacket(new CPacketHeldItemChange(Integer.parseInt(args[2])));
            }
            ChatUtil.print("Все боты выбрали слот " + args[2]);
        }
        if (args[1].equalsIgnoreCase("kick")) {
            for (Bot bot : Bot.bots) {
                bot.getNetManager().closeChannel();
                Bot.bots.remove(bot);
            }
            ChatUtil.print("Все боты кикнуты");
        } else if (args[1].equalsIgnoreCase("look")) {
            look = !look;
            if (look) {
                for (Bot bot : Bot.bots) {
                    bot.getBot().moveForward = 0;
                }
            }
            ChatUtil.print("Look: " + look);
        }
        if (args[1].equalsIgnoreCase("message")) {
            new Thread(() -> {
                for (Bot bot : Bot.bots) {
                    if (args.length > 2) {
                        StringBuilder text = new StringBuilder();
                        for (int i = 2; i < args.length; i++) {
                            text.append(args[i]).append(" ");
                        }
                        bot.getConnection().sendPacket(new CPacketChatMessage(text.substring(0, text.length() - 1)));
                    }
                }
            }).start();

        }
        if (args[1].equalsIgnoreCase("text")) {
                if (args.length > 2) {
                    StringBuilder text = new StringBuilder();
                    for (int i = 2; i < args.length; i++) {
                        text.append(args[i]).append(" ");
                    }
                    BotCommand.text = text.substring(0, text.length() - 1);
                }

        }
        if (args[1].equalsIgnoreCase("invclear")) {
            for (Bot bot : Bot.bots) {
                for (int o = 0; o < 46; ++o) {
                    bot.getController().windowClick(bot.getBot().inventoryContainer.windowId, o, 1, ClickType.THROW, bot.getBot());
                }
            }
            ChatUtil.print("Все боты очистили инвентарь");
        }
    }

    public static String getRandomString(int length) {
        return RandomStringUtils.random(length, "0123456789");
    }

    @Override
    public List<String> getSuggestions(String[] args) {
        if (args.length == 1) {
            return Arrays.asList("join", "kick", "follow", "strafe", "random", "click", "swap", "look", "message", "invclear", "slot");
        }
        if (args.length > 1) {
            if (args[1].equalsIgnoreCase("slot")) {
                return Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8");
            }
        }
        return null;
    }
}
