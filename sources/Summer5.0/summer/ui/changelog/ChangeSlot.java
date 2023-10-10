package summer.ui.changelog;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;

public class ChangeSlot extends GuiSlot {
    private GuiChanges guiChanges;
    int selected;

    public ChangeSlot(GuiChanges guiChanges) {
        super(Minecraft.getMinecraft(), guiChanges.width, guiChanges.height, 32, guiChanges.height - 60, 40);
        this.guiChanges = guiChanges;
        this.selected = 0;
    }

    @Override
    protected int getContentHeight() {
        return getSize() * 40;
    }

    @Override
    protected int getSize() {
        return this.guiChanges.getChanges().size();
    }

    @Override
    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
        this.selected = slotIndex;
    }

    @Override
    protected boolean isSelected(int slotIndex) {
        return this.selected == slotIndex;
    }

    protected int getSelected() {
        return this.selected;
    }

    @Override
    protected void drawBackground() {
        this.guiChanges.drawDefaultBackground();
    }

    @Override
    protected void drawSlot(int p_180791_1_, int p_180791_2_, int p_180791_3_, int p_180791_4_, int p_180791_5_,
                            int p_180791_6_) {
        Change change = this.guiChanges.getChanges().get(p_180791_1_);
        Minecraft.fontRendererObj.drawStringWithShadow(change.getType().toString(), p_180791_2_ + 2,
                p_180791_3_ + 2, getColor(change));
        Minecraft.fontRendererObj.drawStringWithShadow(change.getLabel(), p_180791_2_ + 2, p_180791_3_ + 12,
                -1);
    }

    private int getColor(Change change) {
        switch (change.getType()) {
            case ADDED:
                return -16721631;
            case FIXED:
                return 14541684;
            case REMOVED:
                return -1878956;
        }
        return -1;
    }
}