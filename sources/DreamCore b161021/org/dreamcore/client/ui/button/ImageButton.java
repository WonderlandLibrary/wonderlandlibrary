package org.dreamcore.client.ui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import org.dreamcore.client.helpers.input.MouseHelper;
import org.dreamcore.client.helpers.render.RenderHelper;
import org.dreamcore.client.ui.GuiCapeSelector;
import org.dreamcore.client.ui.GuiConfig;
import org.dreamcore.client.ui.components.draggable.GuiHudEditor;

import java.awt.*;

public class ImageButton {

    protected int height;
    protected String description;
    protected int width;
    protected Minecraft mc;
    protected ResourceLocation image;
    protected int target;
    protected int x;
    protected int ani = 0;
    protected int y;

    public ImageButton(ResourceLocation resourceLocation, int x, int y, int width, int height, String description, int target) {
        this.image = resourceLocation;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.description = description;
        this.target = target;
        this.mc = Minecraft.getInstance();
    }

    protected void hoverAnimation(int mouseX, int mouseY) {
        if (this.isHovered(mouseX, mouseY)) {
            if (this.ani < 3) {
                ani++;
            }
        } else if (this.ani > 0) {
            ani--;
        }
    }

    public void onClick(int mouseX, int mouseY) {
        if (this.isHovered(mouseX, mouseY)) {
            if (this.target == 19) {
                Minecraft.getInstance().displayGuiScreen(null);
            }
            if (this.target == 22) {
                Minecraft.getInstance().displayGuiScreen(new GuiConfig());
            }
            if (this.target == 23) {
                Minecraft.getInstance().displayGuiScreen(new GuiCapeSelector());
            }
            if (this.target == 18) {
                mc.displayGuiScreen(new GuiHudEditor());
            }
            if (this.target == 30) {
                GuiChest chest = (GuiChest) mc.currentScreen;
                if (chest != null) {
                    new Thread(() -> {
                        try {
                            for (int i = 0; i < chest.getInventoryRows() * 9; i++) {
                                ContainerChest container = (ContainerChest) mc.player.openContainer;
                                if (container != null) {
                                    Thread.sleep(50L);
                                    mc.playerController.windowClick(container.windowId, i, 0, ClickType.QUICK_MOVE, mc.player);
                                }
                            }
                        } catch (Exception ignored) {
                        }

                    }).start();
                }
            }
            if (this.target == 31) {
                GuiChest chest = (GuiChest) mc.currentScreen;
                if (chest != null) {
                    new Thread(() -> {
                        try {
                            for (int i = chest.getInventoryRows() * 9; i < chest.getInventoryRows() * 9 + 44; i++) {
                                Slot slot = chest.inventorySlots.inventorySlots.get(i);
                                if (slot.getStack() != null) {
                                    Thread.sleep(50L);
                                    chest.handleMouseClick(slot, slot.slotNumber, 0, ClickType.QUICK_MOVE);
                                }
                            }
                        } catch (Exception ignored) {
                        }

                    }).start();
                }
            }
            if (this.target == 32) {
                for (int i = 0; i < 46; i++) {
                    if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, i, 1, ClickType.THROW, mc.player);
                    }
                }
            }
            if (this.target == 55) {
                switch (GuiCapeSelector.Selector.getCapeName()) {
                    case "dreamcorecape2":
                        GuiCapeSelector.Selector.capeName = "nhplash4";
                        break;
                    case "nhplash4":
                        GuiCapeSelector.Selector.capeName = "nhplash5";
                        break;
                    case "nhplash5":
                        GuiCapeSelector.Selector.capeName = "nhplash6";
                        break;
                    case "nhplash6":
                        GuiCapeSelector.Selector.capeName = "dreamcorecape3";
                        break;
                    default:
                        GuiCapeSelector.Selector.capeName = "dreamcorecape2";
                        break;
                }
            }
            if (this.target == 56) {
                switch (GuiCapeSelector.Selector.getCapeName()) {
                    case "dreamcorecape2":
                        GuiCapeSelector.Selector.capeName = "nhplash4";
                        break;
                    case "nhplash4":
                        GuiCapeSelector.Selector.capeName = "nhplash5";
                        break;
                    case "nhplash5":
                        GuiCapeSelector.Selector.capeName = "nhplash6";
                        break;
                    case "nhplash6":
                        GuiCapeSelector.Selector.capeName = "dreamcorecape3";
                        break;
                    default:
                        GuiCapeSelector.Selector.capeName = "dreamcorecape2";
                        break;
                }
            }
        }
    }

    public void draw(int mouseX, int mouseY, Color color) {
        GlStateManager.pushMatrix();
        GlStateManager.disableBlend();
        this.hoverAnimation(mouseX, mouseY);
        if (this.ani > 0) {
            RenderHelper.drawImage(this.image, this.x - this.ani, this.y - this.ani, this.width + this.ani * 2, this.height + this.ani * 2, new Color(156, 156, 156, 255));
        } else {
            RenderHelper.drawImage(this.image, this.x, this.y, this.width, this.height, color);
        }
        GlStateManager.popMatrix();
    }

    protected boolean isHovered(int mouseX, int mouseY) {
        return MouseHelper.isHovered(this.x, this.y, this.x + this.width, this.y + this.height, mouseX, mouseY);
    }
}