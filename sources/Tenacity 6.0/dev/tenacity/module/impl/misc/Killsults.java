// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.misc;

import dev.tenacity.Tenacity;
import java.util.Scanner;
import dev.tenacity.utils.misc.NetworkingUtils;
import java.util.Collection;
import java.util.Arrays;
import dev.tenacity.utils.misc.FileUtils;
import dev.tenacity.utils.player.ChatUtil;
import dev.tenacity.utils.server.ServerUtils;
import net.minecraft.util.StringUtils;
import dev.tenacity.event.impl.player.ChatReceivedEvent;
import java.util.ArrayList;
import dev.tenacity.module.Category;
import java.util.List;
import java.io.File;
import dev.tenacity.module.Module;

public class Killsults extends Module
{
    public static final File INSULT_DIR;
    private final List<String> messages;
    private int index;
    
    public Killsults() {
        super("Killsults", "Killsults", Category.MISC, "Insults the player that you just killed");
        this.messages = new ArrayList<String>();
    }
    
    @Override
    public void onChatReceivedEvent(final ChatReceivedEvent event) {
        final String message = StringUtils.stripControlCodes(event.message.getUnformattedText());
        if (!message.contains(":") && (message.contains("by " + Killsults.mc.thePlayer.getName()) || message.contains("para " + Killsults.mc.thePlayer.getName()) || message.contains("fue destrozado a manos de " + Killsults.mc.thePlayer.getName()))) {
            if (this.index >= this.messages.size()) {
                this.index = 0;
            }
            ChatUtil.send((ServerUtils.isGeniuneHypixel() ? "/ac " : "") + this.messages.get(this.index).replace("{player}", message.trim().split(" ")[0]));
            ++this.index;
        }
    }
    
    @Override
    public void onEnable() {
        this.messages.clear();
        if (Killsults.INSULT_DIR.exists()) {
            final String killsults = FileUtils.readFile(Killsults.INSULT_DIR);
            this.messages.addAll(Arrays.asList(killsults.split("\n")));
        }
        else {
            this.fetch();
            this.writeToFile();
        }
        super.onEnable();
    }
    
    private void fetch() {
        try {
            final NetworkingUtils.HttpResponse res = NetworkingUtils.httpsConnection("https://raw.githubusercontent.com/Tenacity-Client/Public-Repo/main/killsults.txt");
            if (res != null && res.getResponse() == 200) {
                final Scanner scanner = new Scanner(res.getContent());
                while (scanner.hasNextLine()) {
                    this.messages.add(scanner.nextLine());
                }
                scanner.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void writeToFile() {
        FileUtils.writeFile(Killsults.INSULT_DIR, String.join("\n", this.messages));
    }
    
    static {
        INSULT_DIR = new File(Tenacity.DIRECTORY, "Killsults.txt");
    }
}
