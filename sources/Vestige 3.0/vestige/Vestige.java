package vestige;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import vestige.anticheat.Anticheat;
import vestige.handler.client.CameraHandler;
import vestige.handler.client.SlotSpoofHandler;
import vestige.command.CommandManager;
import vestige.event.EventManager;
import vestige.filesystem.FileSystem;
import vestige.handler.client.BalanceHandler;
import vestige.handler.client.KeybindHandler;
import vestige.handler.packet.PacketBlinkHandler;
import vestige.handler.packet.PacketDelayHandler;
import vestige.module.ModuleManager;
import vestige.font.FontManager;
import vestige.ui.menu.VestigeMainMenu;
import vestige.util.IMinecraft;
import vestige.util.render.FontUtil;

import java.io.IOException;

@Getter
public class Vestige implements IMinecraft {

    public static final Vestige instance = new Vestige();

    public final String name = "Vestige";
    public final String version = "3.0";

    private EventManager eventManager;
    private ModuleManager moduleManager;
    private CommandManager commandManager;

    private PacketDelayHandler packetDelayHandler;
    private PacketBlinkHandler packetBlinkHandler;
    private KeybindHandler keybindHandler;
    private BalanceHandler balanceHandler;
    private CameraHandler cameraHandler;
    private SlotSpoofHandler slotSpoofHandler;

    private Anticheat anticheat;

    private FileSystem fileSystem;

    private FontManager fontManager;

    @Setter
    private boolean destructed;

    public void start() throws IOException {
        eventManager = new EventManager();
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();

        packetDelayHandler = new PacketDelayHandler();
        packetBlinkHandler = new PacketBlinkHandler();

        keybindHandler = new KeybindHandler();
        balanceHandler = new BalanceHandler();
        slotSpoofHandler = new SlotSpoofHandler();

        cameraHandler = new CameraHandler();

        anticheat = new Anticheat();

        fileSystem = new FileSystem();

        fontManager = new FontManager();

        fileSystem.loadDefaultConfig();
        fileSystem.loadKeybinds();

        moduleManager.modules.forEach(m -> m.onClientStarted());

        FontUtil.initFonts();
    }

    public void shutdown() {
        if(!destructed) {
            instance.fileSystem.saveDefaultConfig();
            instance.fileSystem.saveKeybinds();
        }
    }

    public GuiScreen getMainMenu() {
        return destructed ? new GuiMainMenu() : new VestigeMainMenu();
    }

}