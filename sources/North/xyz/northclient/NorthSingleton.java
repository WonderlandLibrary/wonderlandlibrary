package xyz.northclient;

import com.mojang.realmsclient.gui.ChatFormatting;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.opengl.Display;
import xyz.northclient.auth.Auth;
import xyz.northclient.commands.CommandManager;
import xyz.northclient.draggable.DraggableManager;
import xyz.northclient.features.DiscordRP;
import xyz.northclient.features.EventBus;
import xyz.northclient.features.Modules;
import xyz.northclient.features.ui.AltManager;
import xyz.northclient.features.ui.AltManagerLogin;
import xyz.northclient.util.Gif;
import xyz.northclient.util.font.FontUtil;
import xyz.northclient.util.shader.ShaderUtil;

import java.io.File;
import java.net.URI;

public class NorthSingleton {
    public static NorthSingleton INSTANCE = new NorthSingleton();

    public static final boolean BETA = false;

    public static final String GuiText = "Designed by tecnessino, qreaj, groszus, Axioo";
    @Getter
    private UIHook uiHook;

    @Getter
    private Modules modules;

    @Getter
    private EventBus eventBus;

    @Getter
    private DraggableManager draggableManager;

    @Getter
    public Auth auth;

    @Getter
    private CommandManager commandManager;

    @Getter
    private DiscordRP discordRP;

    @Getter
    private Gif gatoCape;

    public void start() {
        this.eventBus = new EventBus();
        this.uiHook = new UIHook();
        this.modules = new Modules();
        this.draggableManager = new DraggableManager();
        this.commandManager = new CommandManager();
        this.discordRP = new DiscordRP();
        this.modules.finish();
        // this.discordRP.start();

        Display.setTitle("North " + (BETA ? "Beta" : "Release"));

        this.gatoCape = new Gif("tecness/animated/newgato.gif",100);

        FontUtil.Init();
        ShaderUtil.init();

        if(!new File("./North").exists()) {
            new File("./North").mkdirs();
            new File("./North/Configs").mkdirs();
            new File("./North/Scripts").mkdirs();
        }

        AltManager.load();
    }

    public static void logChat(final Object message) {
        if (Minecraft.getMinecraft().thePlayer != null)
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(NorthSingleton.INSTANCE.getUiHook().getTheme().getChatPrefixColor() + "North" + ChatFormatting.RESET + " » " + message));
    }
}
