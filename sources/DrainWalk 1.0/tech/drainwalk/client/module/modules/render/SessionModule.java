package tech.drainwalk.client.module.modules.render;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.ScaledResolution;
import tech.drainwalk.DrainWalk;
import tech.drainwalk.client.module.Module;
import tech.drainwalk.client.module.category.Category;
import tech.drainwalk.client.option.options.BooleanOption;
import tech.drainwalk.client.theme.ClientColor;
import tech.drainwalk.events.EventRender2D;
import tech.drainwalk.font.FontManager;
import tech.drainwalk.utility.render.RenderUtility;

public class SessionModule extends Module {
    BooleanOption booleanOption = new BooleanOption("Enable", true)
            .addSettingDescription("CheckBox setting");

    public SessionModule() {
        super("Session Info", Category.RENDER);
        register(booleanOption);

    }
    @EventTarget
    public void onRender2D(EventRender2D eventRender2D) {
        ScaledResolution sc = new ScaledResolution(mc,2);
        float height = sc.getScaledHeight();
        RenderUtility.drawRoundedShadow(3, height - 16, 80, 14, 8, 3, ClientColor.panel);
        RenderUtility.drawRoundedRect(3, height - 15, 81, 13, 3, ClientColor.panel);
        FontManager.SEMI_BOLD_12.drawString("Build: "+ DrainWalk.BUILD, 6.5f, height - 9.5f, ClientColor.textMain);

    }
}