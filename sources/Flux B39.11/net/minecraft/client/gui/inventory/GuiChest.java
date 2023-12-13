package net.minecraft.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import today.flux.Flux;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.module.Module;
import today.flux.module.ModuleManager;
import today.flux.module.implement.World.ChestStealer;
import today.flux.utility.TimeHelper;

import java.io.IOException;
import java.nio.IntBuffer;

public class GuiChest extends GuiContainer {
    /**
     * The ResourceLocation containing the chest GUI texture.
     */
    private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation(
            "textures/gui/container/generic_54.png");
    private IInventory upperChestInventory;
    private IInventory lowerChestInventory;
    public String chestTitle;
    public boolean isChest, isSilent;
    public int lastX, lastY;
    public static TimeHelper firstItem = new TimeHelper();

    /**
     * window height is calculated with these values; the more rows, the heigher
     */
    private int inventoryRows;

    public GuiChest(IInventory upperInv, IInventory lowerInv) {
        super(new ContainerChest(upperInv, lowerInv, Minecraft.getMinecraft().thePlayer));
        this.upperChestInventory = upperInv;
        this.lowerChestInventory = lowerInv;
        this.allowUserInput = false;
        int i = 222;
        int j = i - 108;
        this.inventoryRows = lowerInv.getSizeInventory() / 9;
        this.ySize = j + this.inventoryRows * 18;
        chestTitle = lowerChestInventory.getDisplayName().getUnformattedText();
        ChestStealer.isChest = isChest = chestTitle.equals(StatCollector.translateToLocal("container.chest")) || chestTitle.equals(StatCollector.translateToLocal("container.chestDouble")) || chestTitle.equals("LOW");
        isSilent = ModuleManager.chestStealerMod.isEnabled() && ChestStealer.silent.getValueState() && isChest;
        if (isSilent) {
            try {
                int min = org.lwjgl.input.Cursor.getMinCursorSize();
                IntBuffer tmp = BufferUtils.createIntBuffer(min * min);
                Cursor emptyCursor = new Cursor(min, min, min / 2, min / 2, 1, tmp, null);
                Mouse.setNativeCursor(emptyCursor);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        lastX = lastY = -1;
        firstItem.reset();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (isSilent) {
            for (Module mod : Flux.INSTANCE.getModuleManager().getModList()) {
                if (keyCode == mod.getBind()) {
                    mod.toggle();
                }
            }
        }

        super.keyTyped(typedChar, keyCode);
    }

    // Client override drawScreen
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (isSilent) {
            FontManager.wqy18.drawCenteredStringWithShadow("Stealing Chest", width / 2, height / 2 - 20, 0xFFFFFF);
            if (lastX == -1 && lastY == -1) {
                lastX = mouseX;
                lastY = mouseY;
            }
            int mouseMotionX = mouseX - lastX;
            int mouseMotionY = mouseY - lastY;
            mc.thePlayer.rotationYaw += mouseMotionX * mc.gameSettings.mouseSensitivity;
            mc.thePlayer.rotationPitch += mouseMotionY * mc.gameSettings.mouseSensitivity;
            lastX = mouseX;
            lastY = mouseY;
        } else {
            super.drawScreen(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void onGuiClosed() {
        try {
            Mouse.setNativeCursor(null);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        super.onGuiClosed();
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the
     * items). Args : mouseX, mouseY
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        mc.fontRendererObj.drawString(this.lowerChestInventory.getDisplayName().getUnformattedText(), 8, 6, 4210752);
        mc.fontRendererObj.drawString(this.upperChestInventory.getDisplayName().getUnformattedText(), 8,
                this.ySize - 96 + 2, 4210752);
    }


    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException {

    }


    /**
     * Args : renderPartialTicks, mouseX, mouseY
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.drawTexturedModalRect(i, j + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }
}
