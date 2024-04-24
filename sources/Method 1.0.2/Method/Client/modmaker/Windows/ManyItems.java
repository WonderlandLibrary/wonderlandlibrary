package Method.Client.modmaker.Windows;

import Method.Client.modmaker.BlockObjects;
import Method.Client.utils.visual.ColorUtils;
import Method.Client.utils.visual.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.Serializable;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;

public class ManyItems implements Serializable {

    private final BlockObjects mainType;

    public int scroll;

    public boolean open = false;

    public String finaltext = "";

     public Block storedblock;

     public Item storeditem;

    public ManyItems(BlockObjects mainBlockTypeGiven) {
        this.mainType = mainBlockTypeGiven;
    }


    public void keyTyped(char typedChar, int keyCode) {
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
    }

    private void scrollCheck(int x, int y, int mouseX, int mouseY) {
        if (mouseX > x && mouseX < (x + 300) && mouseY > y && mouseY < (y + 300)) {
            int wheel = Mouse.getDWheel();
            if (wheel < 0 && scroll > -50)
                scroll--;
            else if (wheel > 0 && scroll < 0)
                scroll++;
            if (scroll > 0)
                scroll = 0;
        }
    }

    public void mouseClicked(int mouseButton) {
        if (!finaltext.isEmpty() && mouseButton == 0) {
            open = !open;
        }
    }

    private boolean Mousecheck(int x, int y, int mouseX, int mouseY, int i) {
        if (mouseX > x + ((i % 10) * 20) && mouseX < x + 20 + ((i % 10) * 20)) {
            if (mouseY > y + ((i / 10) * 20) + (scroll * 20) && mouseY < y + ((i / 10) * 20) + 20 + (scroll * 20)) {
                return true;
            }
        }
        return false;
    }


    public void draw(int x, int y, int mouseX, int mouseY) {
        if (open) {
            //    Gui.drawRect(x, y, x + 200, y + 200, new Color(245, 255, 1, 195).getRGB());
            scrollCheck(x, y, mouseX, mouseY);
            finaltext = "";
            int i = 0;
            switch (mainType) {
                case BlockSearch:
                    for (Block block : ForgeRegistries.BLOCKS) {
                        glEnable(GL_SCISSOR_TEST);
                        GL11.glScissor(x * 2, (-y * 2) + 680, 600, 350);

                        RenderHelper.enableGUIStandardItemLighting();
                        ItemStack stack = new ItemStack(block);
                        RenderHelper.enableGUIStandardItemLighting();
                        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(stack, x + 2 + ((i % 10) * 20), y + 2 + ((i / 10) * 20) + (scroll * 20));
                        Minecraft.getMinecraft().getRenderItem().renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, stack, x + 2 + ((i % 10) * 20), y + 2 + ((i / 10) * 20) + (scroll * 20), null);
                        RenderHelper.disableStandardItemLighting();

                        if (Mousecheck(x, y, mouseX, mouseY, i)) {
                            storedblock = block;
                            finaltext = Objects.requireNonNull(block.getRegistryName()).toString().replace("minecraft:", "");
                            Gui.drawRect(x + ((i % 10) * 20), y + ((i / 10) * 20) + (scroll * 20), x + 20 + ((i % 10) * 20), y + ((i / 10) * 20) + 20 + (scroll * 20), new Color(245, 255, 1, 150).getRGB());
                        }
                        RenderUtils.drawRectOutline(x + ((i % 10) * 20), y + ((i / 10) * 20) + (scroll * 20), x + 20 + ((i % 10) * 20), y + ((i / 10) * 20) + 20 + (scroll * 20), 1, ColorUtils.rainbow().getRGB());
                        glDisable(GL_SCISSOR_TEST);
                        i++;
                    }
                    break;
                case ItemSearch:
                    for (Item item : ForgeRegistries.ITEMS) {
                        glEnable(GL_SCISSOR_TEST);
                        GL11.glScissor(x * 2, (-y * 2) + 680, 600, 350);

                        RenderHelper.enableGUIStandardItemLighting();
                        ItemStack stack = new ItemStack(item);
                        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(stack, x + 2 + ((i % 10) * 20), y + 2 + ((i / 10) * 20) + (scroll * 20));
                        RenderHelper.disableStandardItemLighting();
                        Minecraft.getMinecraft().getRenderItem().zLevel = 0.0F;

                        if (Mousecheck(x, y, mouseX, mouseY, i)) {
                            storeditem = item;
                            finaltext = Objects.requireNonNull(item.getRegistryName()).toString().replace("minecraft:", "");
                            Gui.drawRect(x + ((i % 10) * 20), y + ((i / 10) * 20) + (scroll * 20), x + 20 + ((i % 10) * 20), y + ((i / 10) * 20) + 20 + (scroll * 20), new Color(245, 255, 1, 150).getRGB());
                        }
                        RenderUtils.drawRectOutline(x + ((i % 10) * 20), y + ((i / 10) * 20) + (scroll * 20), x + 20 + ((i % 10) * 20), y + ((i / 10) * 20) + 20 + (scroll * 20), 1, ColorUtils.rainbow().getRGB());
                        glDisable(GL_SCISSOR_TEST);
                        i++;
                    }
                    break;
            }
        }
        if (!finaltext.isEmpty()) {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(finaltext, x - 46, y + 4, -1);
        }
    }

    public void chooseClick() {
        if (open)
            open = false;
    }

}
