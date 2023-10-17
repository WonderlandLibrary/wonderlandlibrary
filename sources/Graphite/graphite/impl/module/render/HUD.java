package graphite.impl.module.render;

import graphite.api.event.Listen;
import graphite.api.module.Module;
import graphite.api.module.util.Category;
import graphite.api.module.util.ModuleInfo;
import graphite.api.property.impl.*;
import graphite.api.util.render.animate.Translate;
import graphite.api.util.render.font.TTFFontRenderer;
import graphite.impl.Graphite;
import graphite.impl.event.render.Render2DEvent;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@ModuleInfo(name = "HUD", category = Category.Render, key = Keyboard.KEY_R)
public class HUD extends Module {
	private final Translate translate = new Translate(2, 8);

    private final BooleanProperty chestsProperty = new BooleanProperty("Niggers Test", true);
    private final BooleanProperty cheSstsProperty = new BooleanProperty("AVASVasdfasdfD Test", true);
    private final BooleanProperty cheSsStsProperty = new BooleanProperty("WTFFF Test", true, chestsProperty::getValue);

    private final DoubleProperty doubleProperty = new DoubleProperty("Nigger slider", 5, 1.0, 20, 5);

    private final EnumProperty<ColourMode> colourModeProperty = new EnumProperty<>("Colour Mode", ColourMode.TEAMS);
    private final ColourProperty color = new ColourProperty("ColorPicker", new Color(108, 224, 0).getRGB());
	
    public HUD() {
        setEnabled(true);

        this.register(chestsProperty, cheSstsProperty, cheSsStsProperty, doubleProperty, colourModeProperty, color);
    }

    @Listen
    public void onRender(Render2DEvent event) {
        final TTFFontRenderer fontRenderer = Graphite.INSTANCE.getFontManager().getFontRenderer("TAHOMA_16");

        fontRenderer.drawStringWithShadow("Graphite", 3, 3, -1);
        drawBuildInfo(translate.getX(), translate.getY(), Graphite.DEVELOPER_MODE);
        
        if (mc.currentScreen instanceof GuiChat) {
        	translate.interpolate(2, 22, 0.35F);
        } else {
        	translate.interpolate(2, 8, 0.35F);
        }
    }
    
    public void drawBuildInfo(float x, float y, boolean devBuild) {
    	ScaledResolution resolution = new ScaledResolution(mc);

        final TTFFontRenderer normal = Graphite.INSTANCE.getFontManager().getFontRenderer("TAHOMA_16");
        final TTFFontRenderer bold = Graphite.INSTANCE.getFontManager().getFontRenderer("TAHOMA_BOLD_16");
        
        String buildType = String.format("%s Build", devBuild ? "Developer" : "Release");
        String user = "vadis";
        String buildDate = Graphite.VERSION;
        
        bold.drawStringWithShadow(user, resolution.getScaledWidth() - bold.getWidth(user) - x, resolution.getScaledHeight() - y, 0xAAAAAA);
        normal.drawStringWithShadow("-", resolution.getScaledWidth() - bold.getWidth(user) - normal.getWidth("- ") - x, resolution.getScaledHeight() - y, 0xAAAAAA);
        bold.drawStringWithShadow(buildDate, resolution.getScaledWidth() - bold.getWidth(user) - normal.getWidth(" - ") - bold.getWidth(buildDate) - x, resolution.getScaledHeight() - y, 0xFFFFFF);
        normal.drawStringWithShadow("-", resolution.getScaledWidth() - bold.getWidth(user) - normal.getWidth("- ") - bold.getWidth(buildDate) - normal.getWidth(" - ") - x, resolution.getScaledHeight() - y, 0xAAAAAA);
        normal.drawStringWithShadow(buildType, resolution.getScaledWidth() - bold.getWidth(user) - normal.getWidth(" - ") - bold.getWidth(buildDate) - normal.getWidth(" - ") - normal.getWidth(buildType) - x, resolution.getScaledHeight() - y, 0xAAAAAA);
    }

    private enum ColourMode {
        STATIC("Static"),
        TEAMS("Teams");

        private final String name;

        ColourMode(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
