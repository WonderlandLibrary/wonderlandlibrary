package com.masterof13fps.manager.clickguimanager.components;

import com.masterof13fps.Client;
import com.masterof13fps.features.modules.impl.gui.ClickGUI;
import com.masterof13fps.utils.render.RenderUtils;
import com.masterof13fps.manager.clickguimanager.ClickGui;
import com.masterof13fps.manager.clickguimanager.Panel;
import com.masterof13fps.manager.clickguimanager.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author sendQueue <Vinii>
 * <p>
 * Further info at Vinii.de or github@vinii.de, file created at
 * 11.11.2020. Use is only authorized if given credit!
 */
public class GuiFrame implements Frame {
    public static int dragID;
    Minecraft mc = Minecraft.mc();
    ScaledResolution s = new ScaledResolution(mc);
    float MAX_HEIGHT = s.height();
    private ArrayList<GuiButton> buttons = new ArrayList<GuiButton>();
    private boolean isExpaned, isDragging;
    private int id, posX, posY, prevPosX, prevPosY, scrollHeight;
    private String title;

    public GuiFrame(String title, int posX, int posY, boolean expanded) {
        this.title = title;
        this.posX = posX;
        this.posY = posY;
        this.isExpaned = expanded;
        this.id = ClickGui.compID += 1;
        this.scrollHeight = 0;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        switch (Panel.theme) {
            case "Caesium":
                renderCaesium(mouseX, mouseY);
                break;

            default:
                break;
        }
    }

    private void renderCaesium(int mouseX, int mouseY) {
        final int color = Panel.color;
        final int fontColor = Panel.fontColor;
        int width = Math.max(Panel.FRAME_WIDTH, Panel.fR.getStringWidth(title) + 15);
        if (isDragging && Mouse.isButtonDown(0)) {
            posX = mouseX - prevPosX;
            posY = mouseY - prevPosY;
            dragID = id;
        } else {
            isDragging = false;
            dragID = -1;
        }
        for (GuiButton button : buttons) {
            width = Math.max(width, button.getWidth() + 15);
        }
        RenderUtil.drawRect(posX + 1, posY - 5, posX + width, posY + 12, color);
        RenderUtils.drawVerticalGradient(posX + 1, posY - 5, width - 1, 17, new Color(color).brighter().getRGB(),
                new Color(color).darker().getRGB());
        Panel.fR.drawStringWithShadow(title, (posX + (width / 2)) - Panel.fR.getStringWidth(title) / 2, posY,
                fontColor);
        Panel.fR.drawStringWithShadow(isExpaned ? "-" : "+",
                posX + width - Panel.fR.getStringWidth(isExpaned ? "-" : "+") - 4, posY, fontColor);
        if (isExpaned) {
            int height = 0;
            for (GuiButton button : buttons) {
                button.render(posX + 1, posY + height + 12, width, mouseX, mouseY);
                // check if -1
                if (button.getButtonID() == GuiButton.expandedID) {
                    ArrayList<GuiComponent> components = button.getComponents();
                    if (!components.isEmpty()) {
                        int xOffset = 100;
                        int yOffset = 0;
                        for (GuiComponent component : components) {
                            xOffset = Math.max(xOffset, component.getWidth());
                            yOffset += component.getHeight();
                        }

                        int maxScrollHeight = yOffset;
                        if (yOffset > 150) yOffset = 150;
                        maxScrollHeight -= yOffset;

                        final int left = posX + width + 2;
                        final int right = left + xOffset;
                        final int top = posY + height + 12;
                        final int bottom = top + yOffset + 1;
                        int height2 = 0;

                        int wheelY = Mouse.getDWheel() / 8;
                        scrollHeight += wheelY;

                        if (scrollHeight >= 0) scrollHeight = 0;
                        if (scrollHeight <= -maxScrollHeight) scrollHeight = -maxScrollHeight;

                        RenderUtil.drawRect(left + 1, top + 1, right, bottom,
                                Panel.black100);

                        GL11.glEnable(GL11.GL_SCISSOR_TEST);

                        int x = left + 1;
                        int y = top + 1;

                        RenderUtils.scissor(x, y, right, bottom);
                        for (GuiComponent component : components) {
                            component.render(left, top + height2 + 2 + scrollHeight, xOffset, mouseX, mouseY);
                            height2 += component.getHeight();
                        }
                        GL11.glDisable(GL11.GL_SCISSOR_TEST);

                        RenderUtil.drawVerticalLine(left, top, bottom, color);
                        RenderUtil.drawVerticalLine(right, top, bottom, color);
                        RenderUtil.drawHorizontalLine(left, right, top, color);
                        RenderUtil.drawHorizontalLine(left, right, bottom, color);
                    }
                }
                height += button.getHeight();
            }
            RenderUtil.drawHorizontalLine(posX + 1, posX + width - 1, posY + height + 12, color);
            RenderUtil.drawVerticalLine(posX + width, posY - 5, posY + height + 14, Panel.black100);
            RenderUtil.drawVerticalLine(posX + width, posY - 4, posY + height + 14, Panel.black100);
            RenderUtil.drawVerticalLine(posX + width + 1, posY - 4, posY + height + 15, Panel.black100);
            RenderUtil.drawHorizontalLine(posX + 2, posX + width - 1, posY + height + 13, Panel.black100);
            RenderUtil.drawHorizontalLine(posX + 2, posX + width - 1, posY + height + 13, Panel.black100);
            RenderUtil.drawHorizontalLine(posX + 3, posX + width, posY + height + 14, Panel.black100);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        int width = Panel.FRAME_WIDTH;
        if (isExpaned) {
            for (GuiButton button : buttons) {
                // sort for the max needed width
                width = Math.max(width, button.getWidth());
                button.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
        if (RenderUtil.isHovered(posX, posY, width, 13, mouseX, mouseY)) {
            if (mouseButton == 0) {
                prevPosX = mouseX - posX;
                prevPosY = mouseY - posY;
                isDragging = true;
                dragID = id;
            } else if (mouseButton == 1) {
                if (Client.main().setMgr().settingByName("Sound", Client.main().modMgr().getModule(ClickGUI.class)).isToggled()) {
                    mc.thePlayer.playSound("random.click", 1f, 1f);
                }
                isExpaned = !isExpaned;
                scrollHeight = 0;
                isDragging = false;
                dragID = -1;
            }
        }
    }

    @Override
    public void keyTyped(int keyCode, char typedChar) {
        if (isExpaned) {
            for (GuiButton button : buttons) {
                button.keyTyped(keyCode, typedChar);
            }
        }
    }

    @Override
    public void initialize() {
        // TODO Auto-generated method stub

    }

    public void addButton(GuiButton button) {
        if (!buttons.contains(button)) {
            buttons.add(button);
        }
    }

    public int getButtonID() {
        return id;
    }

    /**
     * @return isExpaned
     */
    public boolean isExpaned() {
        return isExpaned;
    }

    /**
     * @return the posX
     */
    public int getPosX() {
        return posX;
    }

    /**
     * @return the posY
     */
    public int getPosY() {
        return posY;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

}
