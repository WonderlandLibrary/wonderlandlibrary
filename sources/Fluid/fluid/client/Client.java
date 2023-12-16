// 
// Decompiled by Procyon v0.6.0
// 

package fluid.client;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.gui.GuiScreen;
import fluid.client.gui.ClickGUI;
import net.minecraft.client.Minecraft;
import com.darkmagician6.eventapi.events.impl.EventKey;
import com.darkmagician6.eventapi.EventManager;
import fluid.client.config.ConfigManager;
import fluid.client.mods.ModManager;

public class Client
{
    public static Client INSTANCE;
    public ModManager modManager;
    public ConfigManager configManager;
    
    public Client() {
        this.modManager = new ModManager();
        EventManager.register(this);
    }
    
    public static void init() {
        Client.INSTANCE = new Client();
    }
    
    @EventTarget
    public void onKey(final EventKey e) {
        final KeyBinding keyb = Minecraft.getMinecraft().gameSettings.config;
        if (e.key == keyb.getKeyCode() && keyb.isPressed()) {
            Minecraft.getMinecraft().displayGuiScreen(new ClickGUI(Minecraft.getMinecraft().currentScreen));
        }
    }
}
