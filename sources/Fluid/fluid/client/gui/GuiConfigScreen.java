// 
// Decompiled by Procyon v0.6.0
// 

package fluid.client.gui;

import fluid.client.Client;
import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.events.impl.EventRenderDummy;
import net.minecraft.client.gui.GuiScreen;

public class GuiConfigScreen extends GuiScreen
{
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        EventManager.call(new EventRenderDummy(mouseX, mouseY));
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public void onGuiClosed() {
        try {
            Client.INSTANCE.configManager.save();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        super.onGuiClosed();
    }
}
