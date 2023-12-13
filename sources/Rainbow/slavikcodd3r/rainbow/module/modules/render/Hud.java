package slavikcodd3r.rainbow.module.modules.render;

import net.minecraft.client.Minecraft;

import java.util.Date;
import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.text.SimpleDateFormat;

import net.minecraft.client.gui.GuiOverlayDebug;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import slavikcodd3r.rainbow.Rainbow;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.KeyPressEvent;
import slavikcodd3r.rainbow.event.events.Render2DEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.ModuleManager;
import slavikcodd3r.rainbow.option.Option.Op;
import slavikcodd3r.rainbow.utils.ClientUtils;
import slavikcodd3r.rainbow.utils.FontUtils;
import slavikcodd3r.rainbow.utils.GuiUtils;
import slavikcodd3r.rainbow.utils.RenderUtils;

@Module.Mod (displayName = "Hud")
public class Hud extends Module
{
	public static FontUtils font;
	Minecraft mc = Minecraft.getMinecraft();
	
    @EventTarget
    private void onRender2D(final Render2DEvent event) {
        if (font == null) {
            font = new FontUtils("Verdana", 18.0f);
        }
        String time = new SimpleDateFormat("hh:mm a").format(new Date());
        if (time.startsWith("0")) {
            time = time.replaceFirst("0", "");
        }
        font.drawString(String.valueOf("R"), 4.0f, 2.0f, Rainbow.getRainbow(2500, -15), -16777216);
        font.drawString(String.valueOf("  ainbow 2.0 (" + time + ")"), 5.0f, 2.0f, Color.LIGHT_GRAY.getRGB(), -16777216);
        int y = 2;
            for (final Module mod : ModuleManager.getModulesForRender()) {
                if (mod.isShown() && mod.isEnabled()) {
                    if (mod.isShown()) {
                        font.drawString(String.format("%s" + ((mod.getSuffix().length() > 0) ? " §7%s" : ""), mod.getDisplayName(), mod.getSuffix()), event.getWidth() - font.getStringWidth(String.format("%s" + ((mod.getSuffix().length() > 0) ? " %s" : ""), mod.getDisplayName(), mod.getSuffix())) - 4.0f, (float)y, Rainbow.getRainbow(2500, -15 * y), -16777216);       
                        y += 12;
                }
            }
        }
    }
}
