package slavikcodd3r.rainbow.module.modules.player;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.Render2DEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.Module.Mod;
import slavikcodd3r.rainbow.utils.ClientUtils;
@Mod(displayName = "InventoryMove")
public class InventoryMove extends Module
{
	Minecraft mc = Minecraft.getMinecraft();
	
    @EventTarget
    private void onRender(final Render2DEvent event) {
        if (ClientUtils.mc().currentScreen != null) {
            if (Keyboard.isKeyDown(200)) {
                ClientUtils.pitch(ClientUtils.pitch() - 2.0f);
            }
            if (Keyboard.isKeyDown(208)) {
                ClientUtils.pitch(ClientUtils.pitch() + 2.0f);
            }
            if (Keyboard.isKeyDown(203)) {
                ClientUtils.yaw(ClientUtils.yaw() - 3.0f);
            }
            if (Keyboard.isKeyDown(205)) {
                ClientUtils.yaw(ClientUtils.yaw() + 3.0f);
            }
        }
    }
}
