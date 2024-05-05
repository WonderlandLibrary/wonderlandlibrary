package club.dortware.client.module.impl.render;

import club.dortware.client.Client;
import club.dortware.client.event.impl.RenderHudEvent;
import club.dortware.client.module.Module;
import club.dortware.client.module.annotations.ModuleData;
import club.dortware.client.module.enums.ModuleCategory;
import club.dortware.client.util.impl.render.ColorUtil;
import club.dortware.client.util.impl.render.CustomFontRenderer;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.io.IOException;

@ModuleData(name = "HUD", description = "Renders the game HUD", defaultKeyBind = 0, category = ModuleCategory.RENDER)
public final class Hud extends Module {

    private CustomFontRenderer mainFont;

    @Override
    public void setup() {
        try {
            mainFont = new CustomFontRenderer(
                    Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/Font.ttf"))
                            .deriveFont(Font.TRUETYPE_FONT, 20F));
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        PropertyManager propertyManager = Client.INSTANCE.getPropertyManager();
    }
    @Subscribe
    public void renderHUD(RenderHudEvent event) {
        int y = 2;
        Client.INSTANCE.getModuleManager().sort(mc.fontRendererObj);

        mc.fontRendererObj.drawStringWithShadow("Dortware v1.0", 5, 5, -1);

        ScaledResolution scaledResolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        for (Module module : Client.INSTANCE.getModuleManager().getList()) {
            if (module.isToggled()) {
                float xPos = scaledResolution.getScaledWidth() - mc.fontRendererObj.getStringWidth(module.getModuleData().name()) - 2;
                mc.fontRendererObj.drawStringWithShadow(module.getModuleData().name(), xPos, y, ColorUtil.getRainbow(6000, y * -24));
                y += 11;
            }
        }
    }

}
