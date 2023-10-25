package org.dreamcore.client.ui.clickgui.component.impl;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.dreamcore.client.feature.impl.hud.ClickGui;
import org.dreamcore.client.helpers.misc.ClientHelper;
import org.dreamcore.client.helpers.palette.PaletteHelper;
import org.dreamcore.client.helpers.render.rect.RectHelper;
import org.dreamcore.client.settings.Setting;
import org.dreamcore.client.settings.impl.ListSetting;
import org.dreamcore.client.ui.clickgui.Panel;
import org.dreamcore.client.ui.clickgui.component.Component;
import org.dreamcore.client.ui.clickgui.component.ExpandableComponent;
import org.dreamcore.client.ui.clickgui.component.PropertyComponent;

import java.awt.*;

public class ListSettingComponent extends ExpandableComponent implements PropertyComponent {

    private final ListSetting listSetting;

    public ListSettingComponent(Component parent, ListSetting listSetting, int x, int y, int width, int height) {
        super(parent, listSetting.getName(), x, y, width, height);
        this.listSetting = listSetting;
    }

    @Override
    public Setting getSetting() {
        return listSetting;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        super.drawComponent(scaledResolution, mouseX, mouseY);
        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();
        String selectedText = listSetting.getCurrentMode();
        int dropDownBoxY = y + 10;
        int textColor = 0xFFFFFF;
        Gui.drawRect(x, y, x + width, y + height, new Color(15, 15, 15).getRGB());
        mc.smallfontRenderer.drawStringWithShadow(getName(), x + 2, y + 3, textColor);
        Gui.drawRect(x + 2, dropDownBoxY, x + getWidth() - 2, (int) (dropDownBoxY + 10.5), new Color(30, 30, 30).getRGB());
        RectHelper.drawRect(x + 2.5, dropDownBoxY + 0.5, x + getWidth() - 2.5, dropDownBoxY + 10, 0xFF3C3F41);
        mc.circleregularSmall.drawStringWithShadow(selectedText, x + 3.5F, dropDownBoxY + 2.5F, -1);
        if (isExpanded()) {
            Gui.drawRect(x + Panel.X_ITEM_OFFSET, y + height, x + width - Panel.X_ITEM_OFFSET, y + getHeightWithExpand(), new Color(30, 30, 30).getRGB());
            handleRender(x, y + getHeight() + 2, width, textColor);
        }
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
        Color onecolor = new Color(ClickGui.color.getColorValue());
        Color twocolor = new Color(ClickGui.colorTwo.getColorValue());
        double speed = ClickGui.speed.getNumberValue();
        switch (ClickGui.clickGuiColor.currentMode) {
            case "Client":
                color = PaletteHelper.fadeColor(ClientHelper.getClientColor().getRGB(), (ClientHelper.getClientColor().darker().getRGB()), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60 * 2) % 2) - 1));
                break;
            case "Fade":
                color = PaletteHelper.fadeColor(onecolor.getRGB(), onecolor.darker().getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60F * 2) % 2) - 1));
                break;
            case "Color Two":
                color = PaletteHelper.fadeColor(onecolor.getRGB(), twocolor.getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60F * 2) % 2) - 1));
                break;
            case "Astolfo":
                color = PaletteHelper.astolfo(true, y).getRGB();
                break;
            case "Static":
                color = onecolor.getRGB();
                break;
            case "Rainbow":
                color = PaletteHelper.rainbow(300, 1, 1).getRGB();
                break;
        }

        for (String e : listSetting.getModes()) {
            if (listSetting.currentMode.equals(e)) {
                GlStateManager.pushMatrix();
                GlStateManager.disableBlend();
                RectHelper.drawGradientRect(x + Panel.X_ITEM_OFFSET, y - 2, x + width - Panel.X_ITEM_OFFSET, y + Panel.ITEM_HEIGHT - 6, color, new Color(color).darker().getRGB());
                GlStateManager.popMatrix();
            }
            mc.fontRenderer.drawStringWithShadow(e, x + Panel.X_ITEM_OFFSET + 4, y + 1, textColor);
            y += (Panel.ITEM_HEIGHT - 3);
        }
    }

    private void handleClick(int mouseX, int mouseY, int x, int y, int width) {
        for (String e : this.listSetting.getModes()) {
            if (mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + Panel.ITEM_HEIGHT - 3) {
                listSetting.setListMode(e);
            }

            y += Panel.ITEM_HEIGHT - 3;
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
        return listSetting.getModes().toArray().length > 0;
    }
}
