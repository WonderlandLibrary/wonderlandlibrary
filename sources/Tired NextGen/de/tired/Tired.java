package de.tired;

import de.tired.base.guis.newclickgui.setting.SettingsManager;
import de.tired.base.guis.newaltmanager.save.AltFile;
import de.tired.base.guis.newclickgui.ClickGUINew;
import de.tired.util.file.FileUtil;
import de.tired.util.hook.Rotations;
import de.tired.util.misc.DiscordRPC;
import de.tired.base.font.FontManager;
import de.tired.base.command.CommandManager;
import de.tired.base.config.ConfigManager;
import de.tired.base.dragging.DragHandler;
import de.tired.base.event.EventManager;
import de.tired.base.interfaces.IHook;
import de.tired.base.module.ModuleCategory;
import de.tired.base.module.ModuleManager;
import de.tired.base.module.implementation.visual.ClickGUI;
import de.tired.util.render.RenderUtil;
import de.tired.util.render.shaderloader.ShaderManager;
import de.tired.util.render.shaderloader.ShaderRenderer;
import de.tired.util.thread.Ticker;
import de.tired.util.user.User;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import java.io.*;

public enum Tired implements IHook {

    INSTANCE("Tired-Nextgen", "18", new String[]{"Felix1337"});
    public ShaderRenderer shaderRenderer;
    public SettingsManager settingsManager;
    public ModuleCategory hoverCategory = null;
    public static final File DIR = new File(Minecraft.getMinecraft().mcDataDir + "/" + Tired.INSTANCE.CLIENT_NAME, "data");
    public ClickGUINew clickGUINew;
    public ModuleManager moduleManager;
    public FontManager fontManager;
    public ConfigManager configManager;
    public User tiredUser;

    public Rotations rotations;

    public ShaderManager shaderManager;

    public EventManager eventManager;
    public CommandManager commandManager;

    public Ticker ticker;

    public void doClient() {
        if (!DIR.exists()) {
            if (DIR.mkdirs())
                System.out.println("Created Client Folder.");
        }
        this.eventManager = EventManager.SINGLETON;
        this.commandManager = new CommandManager();

        this.settingsManager = new SettingsManager();

        setupInstances();

        (this.fontManager = new FontManager()).bootstrap();

        moduleManager = new ModuleManager();

        (configManager = new ConfigManager()).init();

        shaderManager = new ShaderManager();

        shaderRenderer = new ShaderRenderer();

        clickGUINew = new ClickGUINew();

        DragHandler.loadDraggables();

        AltFile.loadAlts();

        ticker = new Ticker();

        if (MC.gameSettings.ofFastRender)
            MC.gameSettings.ofFastRender = false;

        if (MC.gameSettings.fancyGraphics)
            MC.gameSettings.fancyGraphics = false;

        if (moduleManager.findModuleByClass(ClickGUI.class).key == -1)
            moduleManager.findModuleByClass(ClickGUI.class).key = Keyboard.KEY_RSHIFT;

        rotations = new Rotations();
        ticker.createLoopWithDelay(10, "Rotation Normalize Event", () -> Rotations.instance = rotations.normalize());

        Display.setTitle("Tired Nextgen B" + VERSION + " Cotton Candy");
    }

    @Getter
    public final String CLIENT_NAME;
    @Getter
    public final String VERSION;

    public final String[] CODER;

    Tired(String clientName, String clientVersion, String[] coder) {
        this.CLIENT_NAME = clientName;
        this.VERSION = clientVersion;
        this.CODER = coder;
    }

    public boolean onSendChatMessage(String s) {
        if (s.startsWith(".")) {
            commandManager.execute(s.substring(1));
            return false;
        }
        return true;
    }

    private void setupInstances() {
        RenderUtil.instance = new RenderUtil();
        FileUtil.instance = new FileUtil();
        new DiscordRPC().start();
    }

}
