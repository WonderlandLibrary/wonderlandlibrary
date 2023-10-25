package wtf.evolution.module.impl.Render;

import net.minecraft.client.gui.Gui;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.salam4ik.bot.bot.Bot;
import ru.salam4ik.bot.bot.network.BotPlayClient;
import wtf.evolution.Main;
import wtf.evolution.editor.Drag;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventDisplay;
import wtf.evolution.helpers.animation.Counter;
import wtf.evolution.helpers.font.Fonts;
import wtf.evolution.helpers.math.MathHelper;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.helpers.render.RoundedUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@ModuleInfo(name = "BotStatictics", type = Category.Render)
public class BotStatictics extends Module {


    public Counter timer = new Counter();

    public double proxy;
    public double balance;

    @EventTarget
    public void onRender(EventDisplay e) {


        proxy = Main.proxy.proxies.size();
        balance = Main.balance;
        if (timer.hasReached(30000)) {
            balance();
        }

        List<String> botsname = Bot.bots.stream().map(n -> n.getBot().getName()).collect(Collectors.toList());
        int count = (int) mc.getConnection().getPlayerInfoMap().stream().filter(name -> botsname.contains(name.getGameProfile().getName())).count();
        Fonts.pix.drawCenteredStringWithOutline("[ evolution§b.lua§f ]", e.sr.getScaledWidth() / 2f, e.sr.getScaledHeight() - 70, -1);
        Fonts.pix.drawCenteredStringWithOutline("Server brand: §b" + mc.player.getServerBrand() + "§f  |  " + "User name: §b" + mc.getSession().getUsername(), e.sr.getScaledWidth() / 2f, e.sr.getScaledHeight() - 65, -1);
        Fonts.pix.drawCenteredStringWithOutline("Bots total: §b" + Bot.bots.size() + "§f  |  " + "On this server: §b" + count + "§f  |  " + "Proxy: §b" + (int) proxy, e.sr.getScaledWidth() / 2f, e.sr.getScaledHeight() - 60, -1);
        Fonts.pix.drawCenteredStringWithOutline("Balance: §b" + (int) balance + "§f  |  " + "Last captcha: §b" + BotPlayClient.lastSolved, e.sr.getScaledWidth() / 2f, e.sr.getScaledHeight() - 55, -1);

        List<Long> longs = Bot.bots.stream().map(Bot::getTime).collect(Collectors.toList());

        long average = longs.stream().mapToLong(Long::longValue).sum() / longs.size();
        Fonts.pix.drawCenteredStringWithOutline("Average time: §b" + MathHelper.format(average), e.sr.getScaledWidth() / 2f, e.sr.getScaledHeight() - 50, -1);

    }

    public void balance() {
        new Thread(() -> {
            Document site = null;
            try {
                site = Jsoup.connect("http://api.captcha.guru/res.php?action=getbalance&key=" + Main.apiKey).get();
                Main.balance = Double.parseDouble(site.text());
            } catch (IOException | NumberFormatException ignored) {

            }

        }).start();
    }

}
