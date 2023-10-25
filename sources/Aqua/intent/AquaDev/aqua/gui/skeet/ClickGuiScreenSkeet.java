package intent.AquaDev.aqua.gui.skeet;

import intent.AquaDev.aqua.gui.skeet.components.CategoryPaneSkeet;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.utils.RenderUtil;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ClickGuiScreenSkeet
extends GuiScreen {
    private final List<CategoryPaneSkeet> categoryPanes = new ArrayList();

    public void initGui() {
        int x = 120;
        for (Category category : Category.values()) {
            CategoryPaneSkeet categoryPane = new CategoryPaneSkeet(x, 10, 100, 20, category, this);
            this.categoryPanes.add((Object)categoryPane);
            x += 60;
        }
        super.initGui();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(this.mc);
        int posX = (int)((float)sr.getScaledWidth() / 2.0f - 170.0f);
        int posY = 120;
        int width = (int)((float)sr.getScaledWidth() / 2.0f + 170.0f);
        int height = (int)((float)sr.getScaledHeight() / 2.0f + 150.0f);
        Gui.drawRect2((double)(posX - 2), (double)(posY - 2), (double)(width + 2), (double)((double)height + 1.5), (int)new Color(60, 60, 60, 255).getRGB());
        GL11.glEnable((int)3089);
        RenderUtil.scissor((double)posX, (double)posY, (double)(width - posX), (double)(height - posY));
        Gui.drawRect2((double)posX, (double)posY, (double)width, (double)height, (int)new Color(20, 20, 20, 255).getRGB());
        RenderUtil.drawImageDarker((int)0, (int)0, (int)this.mc.displayWidth, (int)this.mc.displayHeight, (ResourceLocation)new ResourceLocation("Aqua/gui/skeet.png"));
        RenderUtil.drawImage((int)(posX + 5), (int)(posY + 5), (int)40, (int)40, (ResourceLocation)new ResourceLocation("Aqua/gui/1.png"));
        RenderUtil.drawImage((int)(posX + 2), (int)(posY + 85), (int)40, (int)40, (ResourceLocation)new ResourceLocation("Aqua/gui/2.png"));
        RenderUtil.drawImage((int)(posX + 9), (int)(posY + 135), (int)30, (int)30, (ResourceLocation)new ResourceLocation("Aqua/gui/4.png"));
        RenderUtil.drawImage((int)(posX + 9), (int)(posY + 175), (int)30, (int)30, (ResourceLocation)new ResourceLocation("Aqua/gui/3.png"));
        GL11.glDisable((int)3089);
        RenderUtil.drawRGBLineHorizontal((double)((float)posX + 0.5f), (double)(posY + 1), (double)(width - posX - 1), (float)2.0f, (float)0.2f, (boolean)true);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (CategoryPaneSkeet categoryPaneSkeet : this.categoryPanes) {
        }
    }

    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        for (CategoryPaneSkeet categoryPane : this.categoryPanes) {
            categoryPane.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        }
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }

    public void onGuiClosed() {
        super.onGuiClosed();
    }

    private boolean mouseOver(int x, int y, int modX, int modY, int modWidth, int modHeight) {
        return x >= modX && x <= modX + modWidth && y >= modY && y <= modY + modHeight;
    }

    protected void actionPerformed(GuiButton button) throws IOException {
    }

    public List<CategoryPaneSkeet> getCategoryPanes() {
        return this.categoryPanes;
    }
}
