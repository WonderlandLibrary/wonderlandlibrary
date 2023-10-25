package ru.smertnix.celestial.ui.clickgui.component.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.feature.impl.hud.ClickGUI;
import ru.smertnix.celestial.ui.clickgui.Panel;
import ru.smertnix.celestial.ui.clickgui.component.Component;
import ru.smertnix.celestial.ui.clickgui.component.ExpandableComponent;
import ru.smertnix.celestial.ui.clickgui.component.PropertyComponent;
import ru.smertnix.celestial.ui.settings.Setting;
import ru.smertnix.celestial.ui.settings.impl.ListSetting;
import ru.smertnix.celestial.utils.render.RenderUtils;

import java.awt.*;

import org.lwjgl.opengl.GL11;


public class ListSettingComponent extends ExpandableComponent implements PropertyComponent {

    private final ListSetting listSetting;
    Minecraft mc = Minecraft.getMinecraft();

    public ListSettingComponent(Component parent, ListSetting listSetting, int x, int y, int width, int height) {
        super(parent, listSetting.getName(), x, y, width, height);
        this.listSetting = listSetting;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
    	ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        GlStateManager.pushMatrix();
    	GL11.glEnable(GL11.GL_SCISSOR_TEST);
    	RenderUtils.drawRect(0, 0, 0, 0, new Color(30, 30, 30, 255).getRGB());
    	RenderUtils.scissorRect(0, 25.5f, sr.getScaledWidth(), 239);
        super.drawComponent(scaledResolution, mouseX, mouseY);
        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();
        String selectedText = listSetting.currentMode;
        int dropDownBoxY = y + 4;
        int textColor = 0xFFFFFF;
        if (ClickGUI.shadow.getBoolValue()) {
        RenderUtils.drawBlurredShadow(x + 0.5F - 3, dropDownBoxY + 5, x + getWidth() - 0.5F - 2 - (x + 0.5F - 3), (int) (dropDownBoxY + 16) - (dropDownBoxY + 5), 5, new Color(25, 25, 25, 200));
        }
        Gui.drawRect(x + 0.5F - 3, dropDownBoxY + 5, x + getWidth() - 0.5F - 2, (int) (dropDownBoxY + 16), new Color(25, 25, 25, 200).getRGB());
        mc.mntsb_13.drawString(getName() + ":", x - 2, dropDownBoxY - 1, Celestial.instance.featureManager.getFeature(parent.getName()).isEnabled() ? Color.WHITE.getRGB() : Color.BLACK.getRGB());
        if (isExpanded()) {
        	if (ClickGUI.shadow.getBoolValue()) {
            	RenderUtils.drawBlurredShadow(x + ru.smertnix.celestial.ui.clickgui.Panel.X_ITEM_OFFSET - 2, y + height - 1, x - (x + ru.smertnix.celestial.ui.clickgui.Panel.X_ITEM_OFFSET - 3) - 3 + width - ru.smertnix.celestial.ui.clickgui.Panel.X_ITEM_OFFSET, y + getHeightWithExpand() - (y + height - 1), 5, new Color(25, 25, 25, 200));
        	}
        	Gui.drawRect(x + ru.smertnix.celestial.ui.clickgui.Panel.X_ITEM_OFFSET - 2, y + height - 1, x - 3 + width - ru.smertnix.celestial.ui.clickgui.Panel.X_ITEM_OFFSET, y + getHeightWithExpand(), new Color(25, 25, 25, 200).getRGB());
        }
        handleRender(x, y + getHeight() + 2, width, textColor);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
  		GlStateManager.popMatrix();
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        super.onMouseClick(mouseX, mouseY, button);
        if (isExpanded()) {
            handleClick(mouseX, mouseY, getX(), getY() + getHeight() + 2, getWidth());
        }
    }

    private void handleRender(int x, int y, int width, int textColor) {
        int color = 0;
        Color onecolor = new Color(ClickGUI.color.getColorValue());

        for (String e : listSetting.getModes()) {
            if (listSetting.currentMode.equals(e)) {
            		mc.mntsb_14.drawString(e, x + ru.smertnix.celestial.ui.clickgui.Panel.X_ITEM_OFFSET + width / 2 + 0.5f - 48, getY() + 13, -1);
            	
            		if (isExpanded()) {
            		  mc.mntsb_14.drawCenteredString(e, x + ru.smertnix.celestial.ui.clickgui.Panel.X_ITEM_OFFSET + width / 2 + 0.5f- 2, y + 0.5F, ClickGUI.color.getColorValue());
            		}
            		} else {
            			if (isExpanded()) {
                mc.mntsb_14.drawCenteredString(e, x + ru.smertnix.celestial.ui.clickgui.Panel.X_ITEM_OFFSET + width / 2 + 0.5f - 2, y + 0.5F, Color.WHITE.getRGB());
            			}
            }
            y += (ru.smertnix.celestial.ui.clickgui.Panel.ITEM_HEIGHT - 3);
        }
    }

    private void handleClick(int mouseX, int mouseY, int x, int y, int width) {
        for (String e : this.listSetting.getModes()) {
            if (mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + ru.smertnix.celestial.ui.clickgui.Panel.ITEM_HEIGHT - 3) {
                listSetting.setListMode(e);
            }

            y += ru.smertnix.celestial.ui.clickgui.Panel.ITEM_HEIGHT - 3;
        }
    }


    @Override
    public int getHeightWithExpand() {
        return getHeight() + listSetting.getModes().toArray().length * (Panel.ITEM_HEIGHT - 3);
    }

    @Override
    public void onPress(int mouseX, int mouseY, int button) {
    }

    @Override
    public boolean canExpand() {
        return listSetting.modes.toArray().length > 0;
    }

    @Override
    public Setting getSetting() {
        return listSetting;
    }
}