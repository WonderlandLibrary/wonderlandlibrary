/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.button;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import org.celestial.client.helpers.file.FileHelper;
import org.celestial.client.helpers.input.MouseHelper;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.ui.GuiCapeSelector;
import org.celestial.client.ui.GuiConfig;
import org.celestial.client.ui.clickgui.ClickGuiScreen;
import org.celestial.client.ui.components.draggable.GuiHudEditor;

public class ImageButton {
    protected int height;
    protected String description;
    protected int width;
    protected Minecraft mc;
    protected ResourceLocation image;
    protected int target;
    protected int x;
    protected float ani = 0.0f;
    protected int y;
    public static boolean hoverShop;

    public ImageButton(ResourceLocation resourceLocation, int x, int y, int width, int height, String description, int target) {
        this.image = resourceLocation;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.description = description;
        this.target = target;
        this.mc = Minecraft.getMinecraft();
    }

    protected void hoverAnimation(int mouseX, int mouseY) {
        if (this.isHovered(mouseX, mouseY)) {
            if (this.target == 228) {
                hoverShop = true;
            }
            if (this.ani < 5.0f) {
                this.ani = (float)((double)this.ani + 0.3 * Minecraft.frameTime * 0.1);
            }
        } else if (this.ani > 0.0f) {
            this.ani = (float)((double)this.ani - 0.2 * Minecraft.frameTime * 0.1);
            hoverShop = false;
        }
    }

    public void onClick(int mouseX, int mouseY) {
        if (this.isHovered(mouseX, mouseY)) {
            GuiChest chest;
            if (this.target == 16) {
                this.mc.displayGuiScreen(new GuiOptions(null, this.mc.gameSettings));
            }
            if (this.target == 15) {
                this.mc.displayGuiScreen(new GuiLanguage(null, this.mc.gameSettings, this.mc.getLanguageManager()));
            }
            if (this.target == 14) {
                this.mc.shutdown();
            }
            if (this.target == 13) {
                FileHelper.showURL("https://vk.com/celestialclient");
            }
            if (this.target == 12) {
                FileHelper.showURL("https://vk.com/celestialclient");
            }
            if (this.target == 22) {
                Minecraft.getMinecraft().displayGuiScreen(new GuiConfig());
            }
            if (this.target == 19) {
                Minecraft.getMinecraft().displayGuiScreen(ClickGuiScreen.oldScreen);
            }
            if (this.target == 23) {
                Minecraft.getMinecraft().displayGuiScreen(new GuiCapeSelector());
            }
            if (this.target == 18) {
                this.mc.displayGuiScreen(new GuiHudEditor());
            }
            if (this.target == 30 && (chest = (GuiChest)this.mc.currentScreen) != null) {
                new Thread(() -> {
                    try {
                        for (int i = 0; i < chest.getInventoryRows() * 9; ++i) {
                            ContainerChest container = (ContainerChest)this.mc.player.openContainer;
                            if (container == null) continue;
                            Thread.sleep(50L);
                            this.mc.playerController.windowClick(container.windowId, i, 0, ClickType.QUICK_MOVE, this.mc.player);
                        }
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }).start();
            }
            if (this.target == 31 && (chest = (GuiChest)this.mc.currentScreen) != null) {
                new Thread(() -> {
                    try {
                        for (int i = chest.getInventoryRows() * 9; i < chest.getInventoryRows() * 9 + 44; ++i) {
                            Slot slot = chest.inventorySlots.inventorySlots.get(i);
                            if (slot.getStack() == null) continue;
                            Thread.sleep(50L);
                            chest.handleMouseClick(slot, slot.slotNumber, 0, ClickType.QUICK_MOVE);
                        }
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }).start();
            }
            if (this.target == 32) {
                for (int i = 0; i < 46; ++i) {
                    if (!this.mc.player.inventoryContainer.getSlot(i).getHasStack()) continue;
                    this.mc.playerController.windowClick(this.mc.player.inventoryContainer.windowId, i, 1, ClickType.THROW, this.mc.player);
                }
            }
            if (this.target == 55) {
                GuiCapeSelector.Selector.capeName = GuiCapeSelector.Selector.getCapeName().equals("celestial1") ? "celestial2" : "celestial1";
            }
            if (this.target == 56) {
                GuiCapeSelector.Selector.capeName = GuiCapeSelector.Selector.getCapeName().equals("celestial2") ? "celestial1" : "celestial2";
            }
            if (this.target == 228) {
                FileHelper.showURL("http://celestialstore.xyz/");
            }
        }
    }

    public void draw(int mouseX, int mouseY, Color color) {
        GlStateManager.pushMatrix();
        GlStateManager.disableBlend();
        this.hoverAnimation(mouseX, mouseY);
        if (this.ani > 0.0f) {
            RenderHelper.drawImage(this.image, (float)this.x - this.ani, (float)this.y - this.ani, (float)(this.width + 4) + this.ani * 2.0f, (float)(this.height + 4) + this.ani * 2.0f, Color.BLACK);
            RenderHelper.drawImage(this.image, (float)this.x - this.ani, (float)this.y - this.ani, (float)this.width + this.ani * 2.0f, (float)this.height + this.ani * 2.0f, this.isHovered(mouseX, mouseY) ? new Color(156, 156, 156, 255) : Color.WHITE);
            if (this.isHovered(mouseX, mouseY)) {
                this.mc.robotoRegularFontRender.drawStringWithShadow(this.description, (float)this.x + (float)this.width / 2.0f + (float)this.mc.robotoRegularFontRender.getStringWidth(this.description) / 2.0f - (float)(this.target == 228 ? 35 : 6), this.y + this.height - 17, new Color(255, 255, 255, 255).getRGB());
            }
        } else {
            RenderHelper.drawImage(this.image, this.x, this.y, this.width + 3, this.height + 3, Color.BLACK);
            RenderHelper.drawImage(this.image, this.x, this.y, this.width, this.height, color);
        }
        GlStateManager.popMatrix();
    }

    protected boolean isHovered(int mouseX, int mouseY) {
        return MouseHelper.isHovered(this.x, this.y, this.x + this.width, this.y + this.height, mouseX, mouseY);
    }
}

