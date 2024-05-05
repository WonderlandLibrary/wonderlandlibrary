package tech.drainwalk.gui.menu.window.windows.menuwindow.components.panel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import tech.drainwalk.animation.EasingList;
import tech.drainwalk.font.FontManager;
import tech.drainwalk.gui.menu.hovered.Hovered;
import tech.drainwalk.client.theme.ClientColor;
import tech.drainwalk.gui.menu.window.windows.menuwindow.MenuWindow;
import tech.drainwalk.gui.menu.window.windows.menuwindow.components.Component;
import tech.drainwalk.client.module.category.Category;
import tech.drainwalk.utility.color.ColorUtility;
import tech.drainwalk.utility.render.RenderUtility;

public class CategoryComponent extends Component {
    public CategoryComponent(float x, float y, MenuWindow parent) {
        super(x, y, 85.5f, 21, parent);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        float offset = 0;
        for (Category category : Category.values()) {
            if (Hovered.isHovered(mouseX, mouseY, x + 7, y + 51.5f + offset, width, height) && mouseButton == 0) {
                parent.setSelectedCategory(category);
            }
            offset += 5f + height;
        }
    }

    @Override
    public void updateScreen(int mouseX, int mouseY) {
        float offset = 0;
        for (Category category : Category.values()) {
            boolean hovered = Hovered.isHovered(mouseX, mouseY, x + 7, y + 51.5f + offset, width, height);
            if (category != parent.getSelectedCategory()) {
                category.getAnimation().update(category.getAnimation().getAnimationValue() > 1);
            } else {
                parent.getSelectedCategory().getAnimation().update(parent.getSelectedCategory().getAnimation().getAnimationValue() != 1.1);
            }

            category.getHoveredAnimation().update(hovered);

            if (hovered) {
                canDragging = false;
            }
            offset += 5f + height;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        float[] color = ColorUtility.getRGBAf(ClientColor.category);
        float offset = 0;
        for (Category category : Category.values()) {
            category.getHoveredAnimation().animate(0, 1f, 0.25f, EasingList.NONE, Minecraft.getMinecraft().getRenderPartialTicks());
            float hoverAnimationValue = category.getHoveredAnimation().getAnimationValue() * 0.25f;
            category.getAnimation().animate(0, 1, 0.15f, EasingList.NONE, Minecraft.getMinecraft().getRenderPartialTicks());
            parent.getSelectedCategory().getAnimation().animate(0, 1, 0.15f, EasingList.NONE, Minecraft.getMinecraft().getRenderPartialTicks());
            GlStateManager.disableAlpha();
            RenderUtility.drawRoundedRect(x + 7, y + 51.5f + offset, width * category.getAnimation().getAnimationValue(), height, 5, ColorUtility.rgbaFloat(color[0], color[1], color[2], category.getAnimation().getAnimationValue()));
            RenderUtility.drawRoundedRect(x + 7, y + 51.5f + offset, width, height, 5, ColorUtility.rgbaFloat(color[0], color[1], color[2], hoverAnimationValue));
            float[] c = ColorUtility.getRGBAf(ColorUtility.interpolateColor(ClientColor.textStay, ClientColor.textMain, category.getAnimation().getAnimationValue()));
            FontManager.SEMI_BOLD_16.drawString(category.getName(), x + 30, y + 57 + 2.5f + offset, ColorUtility.rgbFloat(
                    c[0] + hoverAnimationValue,
                    c[1] + hoverAnimationValue,
                    c[2] + hoverAnimationValue
            ));
            FontManager.ICONS_21.drawString(String.valueOf(category.getIcon()), x + 13f, y + 55.5f + (FontManager.ICONS.getStringHeight(String.valueOf(category.getIcon())) /2f) + offset, ColorUtility.rgbFloat(
                    c[0] + hoverAnimationValue,
                    c[1] + hoverAnimationValue,
                    c[2] + hoverAnimationValue
            ));
            offset += 5f + height;
        }
    }
}
