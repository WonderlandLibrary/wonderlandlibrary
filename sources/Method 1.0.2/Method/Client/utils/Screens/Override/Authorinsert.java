package Method.Client.utils.Screens.Override;

import Method.Client.utils.visual.ChatUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagString;

import java.io.IOException;

public class Authorinsert extends GuiScreen {
    GuiTextField textbox;

    @Override
    public void initGui() {
        textbox = new GuiTextField(0, mc.fontRenderer, (int) (mc.displayWidth / 5.4), 180, 240, mc.displayWidth / 100);
        textbox.setFocused(true);
        addButton(new GuiButton(200, width / 2 - 50, height / 4 + 120, 120, 20, "Done"));

    }

    public void doitnow() {
        if (!mc.player.capabilities.isCreativeMode) {
            ChatUtils.error("Creative mode only.");
            mc.displayGuiScreen(null);
            return;
        }
        ItemStack heldItem = mc.player.inventory.getCurrentItem();
        int heldItemID = Item.getIdFromItem(heldItem.getItem());
        int writtenBookID = Item.getIdFromItem(Items.WRITTEN_BOOK);

        if (heldItemID != writtenBookID)
            ChatUtils.error("You must hold a written book in your main hand.");
        else {
            heldItem.setTagInfo("author", new NBTTagString(textbox.getText()));
            ChatUtils.message("Author Changed! Open Inventory.");
        }
        mc.displayGuiScreen(null);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        drawCenteredString(fontRenderer, "Change Author", width / 2, 40, 16777215);

        textbox.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }


    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        textbox.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == 1) { // Escape
            mc.displayGuiScreen(null);
        } else {
            textbox.textboxKeyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == 200) {
            doitnow();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}
