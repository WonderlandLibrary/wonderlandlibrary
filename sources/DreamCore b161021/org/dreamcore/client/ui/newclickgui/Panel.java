package org.dreamcore.client.ui.newclickgui;

import net.minecraft.util.ResourceLocation;
import org.dreamcore.client.dreamcore;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.feature.impl.hud.ClickGui;
import org.dreamcore.client.helpers.Helper;
import org.dreamcore.client.helpers.misc.ClientHelper;
import org.dreamcore.client.helpers.palette.PaletteHelper;
import org.dreamcore.client.helpers.render.RenderHelper;
import org.dreamcore.client.helpers.render.rect.RectHelper;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Panel implements Helper {

    public Type type;
    public int x, y, width, height, dragX, dragY;
    public boolean drag;
    public ArrayList<FeaturePanel> featurePanel = new ArrayList<>();
    public Theme theme = new Theme();
    private boolean open;

    public Panel(Type type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = 105;
        this.height = 21;
        this.open = false;

        for (Feature feature : dreamcore.instance.featureManager.getFeaturesForCategory(type)) {
            featurePanel.add(new FeaturePanel(feature));
        }
    }

    public void drawScreen(int mouseX, int mouseY) {
        featurePanel.sort(new SorterHelper());
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

        RectHelper.drawRoundedRect(x - 0.5, y, width - 1.5, height, 4, new Color(20, 20, 20));
        RenderHelper.drawImage(new ResourceLocation("dreamcore/clickguiicons/" + type.getName() + ".png"), x + 4, y + 4, 14, 14, new Color(color));

        mc.circleregular.drawStringWithOutline(type.getName(), x + 22, y + height / 2F - 3.5F, Color.GRAY.getRGB());

        int yOffset = 0;
        if (this.open) {
            for (FeaturePanel featurePanel : featurePanel) {
                featurePanel.setPosition(x, y + height + yOffset, width, 15);
                featurePanel.drawScreen(mouseX, mouseY);
                yOffset += 15;
            }
        }

        if (drag) {
            this.x = dragX + mouseX;
            this.y = dragY + mouseY;
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        drag = false;

        if (this.open) {
            for (FeaturePanel featurePanel : featurePanel) {
                featurePanel.mouseReleased(mouseX, mouseY, state);
            }
        }
    }


    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (open) {
            for (FeaturePanel featurePanel : featurePanel) {
                featurePanel.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }

        if (dreamcore.instance.newClickGui.usingSetting)
            return;

        if (isHovering(mouseX, mouseY) && mouseButton == 0) {
            this.dragX = x - mouseX;
            this.dragY = y - mouseY;
            drag = true;
        }
    }

    public void keyTyped(char chars, int keyCode) throws IOException {
        if (this.open) {
            for (FeaturePanel featurePanel : featurePanel) {
                featurePanel.keyTyped(chars, keyCode);
            }
        }
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isHovering(int mouseX, int mouseY) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    public boolean isWithinHeader(int x, int y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
