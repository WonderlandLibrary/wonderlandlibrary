package wtf.dawn;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.input.Keyboard;
import wtf.dawn.command.CommandManager;
import wtf.dawn.event.events.EventChat;
import wtf.dawn.gui.clickgui.Click;
import wtf.dawn.module.ModuleManager;
import wtf.dawn.module.impl.visual.HUD.ArrayList;
import wtf.dawn.module.impl.visual.HUD.Hud;
import org.lwjgl.opengl.Display;
import wtf.dawn.utils.font.FontUtil;
import wtf.dawn.event.Event;


public class Dawn {
    private static final String name = "Dawn";
    private final String version = "0.0.1";
    private final String build = "Development";
    private final static Dawn instance = new Dawn();
    private static final ModuleManager moduleManager = new ModuleManager();
    public static Hud hud = new Hud();
    public static ArrayList arrayList = new ArrayList();
    public static CommandManager commandManager = new CommandManager();

    public static Click click = new Click(moduleManager.getModules());

    public void startup() {
        Display.setTitle(name + " - " + version + " - " + build);
        FontUtil.bootstrap();
    }

    public void onKeyPressed(int key) {

        if(key == Keyboard.KEY_RSHIFT) {
            Minecraft.getMinecraft().displayGuiScreen(click);
        }

        getModuleManager().getModules().stream().filter(m -> m.getKeyCode() == key).forEach(m -> m.toggle());
    }

    public static void onEventChat(EventChat event) {
        if (event instanceof EventChat) {
            commandManager.handleChat((EventChat) event);
        }
    }

            public static void addChatMessage (String format){
                String message = EventChat.getMessage();
                String msg = "§f[§7" + name + "§f] " + message;

                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
            }


    public static Dawn getInstance() {
        return instance;
    }
    public String getName() {
        return this.name;
    }
    public String getVersion() {
        return this.version;
    }
    public String getBuild() {
        return this.build;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }
}
