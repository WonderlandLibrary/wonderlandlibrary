package summer.ui.changelog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import summer.Summer;
import summer.ui.GuiCustomMainMenu;

public final class GuiChanges extends GuiScreen {
    private final List<Change> changes = new ArrayList();
    private ChangeSlot changeSlot;

    public GuiChanges() {
        this.changes.add(new Change("Recoded parts of base", Type.FIXED));
        this.changes.add(new Change("Scaffold Changes", Type.FIXED));
        this.changes.add(new Change("Radius for TargetStrafe", Type.ADDED));
        this.changes.add(new Change("Radar Removed", Type.ADDED));
        this.changes.add(new Change("Antibot no longer false flagging", Type.FIXED));
        this.changes.add(new Change("Exhi TargetHUD + Summer TargetHUD still in", Type.ADDED));
        this.changes.add(new Change("OG Changelog :)", Type.ADDED));
        this.changes.add(new Change("Rare care of ESP disconnecting from entity", Type.FIXED));
        this.changes.add(new Change("More subnames on arraylist", Type.ADDED));
        this.changes.add(new Change("Configs load without needing to restart", Type.FIXED));
        this.changes.add(new Change("More options for visuals", Type.ADDED));
        this.changes.add(new Change("More useful modules", Type.ADDED));
        this.changes.add(new Change("WD Bans", Type.FIXED));
        this.changes.add(new Change("Flaggy fly + Smoother Fly", Type.FIXED));
        this.changes.add(new Change("Better Crits", Type.FIXED));
        this.changes.add(new Change("CRASHING", Type.FIXED));
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.changeSlot.handleMouseInput();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.changeSlot.drawScreen(mouseX, mouseY, partialTicks);
        drawCenteredString(this.fontRendererObj,
                String.format("§f§kf§eSummer Reborn b" + Summer.VERSION + " ChangeLog!§f§k1§e§f",
                        new Object[]{"Summer", 3}),
                this.width / 2, 4, -1);
        this.fontRendererObj.drawStringWithShadow(
                String.format("Changes: %s", this.changes.size()), 2.0F, 14.0F, -1);
        this.fontRendererObj.drawStringWithShadow(
                String.format("Changelog Made by Volcano"), 2.0F, 2.0F, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 76, this.height - 26, 149, 20, "Back"));
        this.changeSlot = new ChangeSlot(this);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == 200) {
            if (this.changeSlot.selected == 0) {
                this.changeSlot.selected = this.changes.size();
            }
            this.changeSlot.selected -= 1;
        }
        if (keyCode == 208) {
            if (this.changeSlot.selected == this.changes.size()) {
                this.changeSlot.selected = 0;
            }
            this.changeSlot.selected += 1;
        }
    }

    @Override
    public void actionPerformed(GuiButton button) {
        try {
            super.actionPerformed(button);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiCustomMainMenu());
        }
    }

    public final List<Change> getChanges() {
        return this.changes;
    }

    public static enum Type {
        ADDED, REMOVED, FIXED;

        private Type() {
        }
    }
}