// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.elements.module;

import moonsense.MoonsenseClient;
import java.util.function.Consumer;
import moonsense.enums.ModuleCategory;
import moonsense.ui.elements.button.GuiCustomButton;
import moonsense.ui.elements.Element;

public class ElementModuleCategorySort extends Element
{
    private final GuiCustomButton sortByAll;
    private final GuiCustomButton sortByHUD;
    private final GuiCustomButton sortByServer;
    private final GuiCustomButton sortByMechanic;
    public ModuleCategory sortingBy;
    
    public ElementModuleCategorySort(final int x, final int y, final int width, final int height) {
        super(x, y, width, height, null);
        int offset = 0;
        final int buttonId = 0;
        final int x2 = x + offset;
        final int widthIn = (int)(6.0f + MoonsenseClient.titleRenderer.getWidth("ALL"));
        final int n = 4;
        MoonsenseClient.titleRenderer.getClass();
        this.sortByAll = new GuiCustomButton(buttonId, x2, y, widthIn, n + 9, "ALL", false);
        offset += (int)(9.0f + MoonsenseClient.titleRenderer.getWidth("ALL"));
        final int buttonId2 = 1;
        final int x3 = x + offset;
        final int widthIn2 = (int)(6.0f + MoonsenseClient.titleRenderer.getWidth("HUD"));
        final int n2 = 4;
        MoonsenseClient.titleRenderer.getClass();
        this.sortByHUD = new GuiCustomButton(buttonId2, x3, y, widthIn2, n2 + 9, "HUD", false);
        offset += (int)(9.0f + MoonsenseClient.titleRenderer.getWidth("HUD"));
        final int buttonId3 = 2;
        final int widthIn3 = (int)(6.0f + MoonsenseClient.titleRenderer.getWidth("SERVER"));
        final int n3 = 4;
        MoonsenseClient.titleRenderer.getClass();
        this.sortByServer = new GuiCustomButton(buttonId3, x, y, widthIn3, n3 + 9, "SERVER", false);
        offset += (int)(9.0f + MoonsenseClient.titleRenderer.getWidth("SERVER"));
        final int buttonId4 = 3;
        final int widthIn4 = (int)(6.0f + MoonsenseClient.titleRenderer.getWidth("MECHANIC"));
        final int n4 = 4;
        MoonsenseClient.titleRenderer.getClass();
        this.sortByMechanic = new GuiCustomButton(buttonId4, x, y, widthIn4, n4 + 9, "MECHANIC", false);
        this.sortingBy = ModuleCategory.ALL;
    }
    
    @Override
    public void render(final int mouseX, final int mouseY, final float partialTicks) {
        int offset = 0;
        this.sortByAll.xPosition = this.getX() + offset;
        offset += (int)(9.0f + MoonsenseClient.titleRenderer.getWidth("ALL"));
        this.sortByHUD.xPosition = this.getX() + offset;
        offset += (int)(9.0f + MoonsenseClient.titleRenderer.getWidth("HUD"));
        this.sortByServer.xPosition = this.getX() + offset;
        offset += (int)(9.0f + MoonsenseClient.titleRenderer.getWidth("SERVER"));
        this.sortByMechanic.xPosition = this.getX() + offset;
        offset += (int)(9.0f + MoonsenseClient.titleRenderer.getWidth("MECHANIC"));
        this.width = 9 + this.sortByAll.getButtonWidth() + this.sortByHUD.getButtonWidth() + this.sortByServer.getButtonWidth() + this.sortByMechanic.getButtonWidth();
        this.hovered = (mouseX >= this.getX() && mouseX <= this.getX() + this.width && mouseY >= this.getY() && mouseY <= this.getY() + this.height);
        this.sortByAll.drawButton(this.mc, mouseX, mouseY);
        this.sortByHUD.drawButton(this.mc, mouseX, mouseY);
        this.sortByServer.drawButton(this.mc, mouseX, mouseY);
        this.sortByMechanic.drawButton(this.mc, mouseX, mouseY);
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.sortByAll.isMouseOver()) {
            this.sortingBy = ModuleCategory.ALL;
            System.out.println("all");
        }
        if (this.sortByHUD.isMouseOver()) {
            this.sortingBy = ModuleCategory.HUD;
            System.out.println("hud");
        }
        if (this.sortByServer.isMouseOver()) {
            this.sortingBy = ModuleCategory.SERVER;
            System.out.println("server");
        }
        if (this.sortByMechanic.isMouseOver()) {
            this.sortingBy = ModuleCategory.MECHANIC;
            System.out.println("mechanic");
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
