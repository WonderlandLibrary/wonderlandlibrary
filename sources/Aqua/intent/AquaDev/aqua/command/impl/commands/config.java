package intent.AquaDev.aqua.command.impl.commands;

import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.command.Command;
import intent.AquaDev.aqua.config.Config;
import intent.AquaDev.aqua.utils.ChatUtil;
import java.io.File;
import net.minecraft.client.Minecraft;

public class config
extends Command {
    private final File dir;

    public config() {
        super("config");
        this.dir = new File(Minecraft.getMinecraft().mcDataDir + "/" + Aqua.name + "/configs/");
    }

    public void execute(String[] args) {
        if (args.length >= 2) {
            Config cfg;
            String s;
            if (args[0].equalsIgnoreCase("load")) {
                s = "";
                for (int i = 1; i < args.length; ++i) {
                    s = s + args[i];
                }
                s = s.trim();
                cfg = new Config(s);
                cfg.load();
            }
            if (args[0].equalsIgnoreCase("onlineload")) {
                s = "";
                for (int i = 1; i < args.length; ++i) {
                    s = s + args[i];
                }
                s = s.trim();
                cfg = new Config(s, true);
                try {
                    cfg.load();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (args[0].equalsIgnoreCase("save")) {
                s = "";
                for (int i = 1; i < args.length; ++i) {
                    s = s + args[i];
                }
                s = s.trim();
                cfg = new Config(s);
                cfg.saveCurrent();
            }
            if (args[0].equalsIgnoreCase("list")) {
                try {
                    File[] files = this.dir.listFiles();
                    String list = "";
                    for (int i = 0; i < files.length; ++i) {
                        list = list + ", " + files[i].getName().replace((CharSequence)".txt", (CharSequence)"");
                    }
                    ChatUtil.sendChatMessageWithPrefix((String)("\u00a77Configs: \u00a7f" + list.substring(2)));
                }
                catch (Exception exception) {}
            }
        } else {
            ChatUtil.sendChatMessageWithPrefix((String)"\u00a77config \u00a78<\u00a7bload/save/listlist\u00a78> \u00a78<\u00a7bname\u00a78>\u00a7f");
        }
    }
}
