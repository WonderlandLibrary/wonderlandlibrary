package ru.smertnix.celestial.ui.clickgui.component.impl;

import java.awt.Color;
import java.io.IOException;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.event.events.EventInitGui;
import ru.smertnix.celestial.feature.impl.hud.ClickGUI;
import ru.smertnix.celestial.ui.clickgui.ClickGuiScreen;
import ru.smertnix.celestial.ui.clickgui.component.Component;
import ru.smertnix.celestial.ui.clickgui.component.PropertyComponent;
import ru.smertnix.celestial.ui.settings.Setting;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.StringSetting;
import ru.smertnix.celestial.utils.math.AnimationHelper;
import ru.smertnix.celestial.utils.render.RenderUtils;
import ru.smertnix.celestial.utils.render.RoundedUtil;
import ru.smertnix.celestial.utils.render.StencilUtil;

public class StringComponent extends Component implements PropertyComponent {
    public StringSetting stringset;
    Minecraft mc = Minecraft.getMinecraft();
    
    public StringComponent(Component parent, StringSetting set, int x, int y, int width, int height) {
        super(parent, set.getName(), x, y, width, height);
        this.stringset = set;
    }
    
    public void a(EventInitGui e) {
    	ClickGuiScreen.str.setText(stringset.getDefaultText());
    }
    
    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        if (stringset.isVisible()) {
        	ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            GlStateManager.pushMatrix();
        	GL11.glEnable(GL11.GL_SCISSOR_TEST);
        	int x = getX();
            int y = getY() + 8;
            int width = getWidth();
            int color = Celestial.instance.featureManager.getFeature(parent.getName()).isEnabled() ? Color.WHITE.getRGB() : Color.BLACK.getRGB();
            int height = getHeight();
            if (!ClickGuiScreen.str.getText().equals("")) {
            	   stringset.setCurrentText(ClickGuiScreen.str.getText());
                   stringset.setDefaultText(ClickGuiScreen.str.getText());
            }
        	ClickGuiScreen.str.xPosition = x;
            ClickGuiScreen.str.yPosition = y;
            RenderUtils.drawRect(x -1, y + 10, x + width - 4, y + 9.5f, color);
            	 if (ClickGuiScreen.str.isFocused) {
              	   mc.mntsb_15.drawString(ClickGuiScreen.str.getText() + "_", ClickGuiScreen.str.xPosition + 2, ClickGuiScreen.str.yPosition + 2, color);
               } else {
              	   mc.mntsb_15.drawString(stringset.getDefaultText(), ClickGuiScreen.str.xPosition + 2, ClickGuiScreen.str.yPosition + 2, color);
               }
        	
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
      		GlStateManager.popMatrix();
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
    	  ClickGuiScreen.str.mouseClicked(mouseX, mouseY, button);
    }

	@Override
	public Setting getSetting() {
		return stringset;
	}
	
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        	if ((typedChar == '\t' || typedChar == '\r') && ClickGuiScreen.str.isFocused())
                ClickGuiScreen.str.setFocused(!ClickGuiScreen.str.isFocused());
    }
}
