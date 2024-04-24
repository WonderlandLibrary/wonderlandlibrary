package Method.Client.utils.Screens;

import Method.Client.utils.Utils;
import Method.Client.utils.visual.RenderUtils;
import Method.Client.utils.visual.SimpleButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SubGui {
    // a Window is a container that sits inside our ClickGui
    // it has a title, a size, and a position
    // it can be moved and resized
    // it can have a background color

    // the title of the window
    private String title;

    private GuiTextField textbox;

    public ListGui listGui;

    // the size of the window
    public double width, height;

    // the position of the window
    public double x, y;

    public double dragX, dragY;

    public boolean isDragging;

    public boolean isExpanding;

    // SimpleButton arraylist
    public ArrayList<SimpleButton> buttons;

    // the background color of the window
    private int backgroundColor;

    // constructor
    public SubGui(String title) {
        this.title = title;
        this.x = 100;
        this.y = 100;
        this.width = 200;
        this.height = 300;
        this.backgroundColor = 0xf0f0f0;
        this.buttons = getButtons();
        listGui = new ListGui(getItems(), this);
        textbox = new GuiTextField(0, Minecraft.getMinecraft().fontRenderer, (int) x, (int) y, (int) width, 15);
        textbox.setMaxStringLength(20);
        textbox.setEnableBackgroundDrawing(true);

    }

    // to be overridden by subclasses
    public ArrayList<SimpleButton> getButtons() {
        return null;
    }


    // to be overridden by subclasses
    public ArrayList<SelectedThing> getItems() {
        return null;
    }


    //Top that holds the title of the window
    public void drawTitle() {
        RenderUtils.scaledRect((int) x, (int) y, width, 20, 0xFF000000);
        RenderUtils.scaledText(title, x + (width / 2), y + 5, 0xFFFFFF, true);
    }

    //Bottom that holds the content of the window
    // This should be Overridden by the child class
    public void drawContent(int mouseX, int mouseY, float partialTicks) {
        listGui.drawScreen(mouseX, mouseY, partialTicks);
    }

    // if mouse clicked, check if it is inside of the window
    // if it is, return true
    // if it is not, return false
    public boolean isMouseInside(int mouseX, int mouseY) {
        return Utils.ScaledMouseCheck(mouseX, mouseY, x, y, width, height);
    }

    // is mouse clicked in the header of the window
    public boolean isMouseInsideHeader(int mouseX, int mouseY) {
        return Utils.ScaledMouseCheck(mouseX, mouseY, x, y, width, 20);
    }


    // drag the window
    public void updatePosition(int mouseX, int mouseY) {
        if (isDragging) {
            x = Math.max(0, Math.min(mouseX - dragX, (Minecraft.getMinecraft().displayWidth)));
            x = x * (1920.0 / Minecraft.getMinecraft().displayWidth);
            y = Math.max(0, Math.min(mouseY - dragY, Minecraft.getMinecraft().displayHeight));
            y = y * (1027.0 / Minecraft.getMinecraft().displayHeight);
        }
        if (isExpanding) {
            width = Math.max(100, Math.min(mouseX - x, (Minecraft.getMinecraft().displayWidth)));
            height = Math.max(100, Math.min(mouseY - y, Minecraft.getMinecraft().displayHeight));
        }
    }


    // Draw the window
    public void draw(int mouseX, int mouseY, float partialTicks) {
        // draw the title
        drawTitle();
        // draw a background color
        RenderUtils.scaledRect((int) x, (int) y + 20, (int) (width), (int) (height - 20), 0xa0c9c9c9);
        // draw the border
        RenderUtils.scaledRectOutline((int) x, (int) y, (int) (x + width), (int) (y + height), 1, 0xFF000000);
        // put a small rectangle in the bottom right corner to indicate that the window is draggable
        RenderUtils.scaledRect((int) (x + width - 10), (int) (y + height - 10), 10, 10, 0xFF000000);

        // draw the content
        drawContent(mouseX, mouseY, partialTicks);

        listGui.updateSize((int) (x * (Minecraft.getMinecraft().displayWidth / 1920.0)), (int) ((y + 20) *
                        (Minecraft.getMinecraft().displayHeight / 1027.0))
                , (int) (width * (Minecraft.getMinecraft().displayWidth / 1920.0)), (int) ((height - 40) * (Minecraft.getMinecraft().displayHeight / 1027.0)));

        textbox.drawTextBox();
        textbox.x = (int) (x * (Minecraft.getMinecraft().displayWidth / 1920.0));
        textbox.y = (int) ((y + height - 10) * (Minecraft.getMinecraft().displayHeight / 1027.0));
        textbox.width = (int) ((width - 10) * (Minecraft.getMinecraft().displayWidth / 1920.0));
        textbox.height = (int) (10 * (Minecraft.getMinecraft().displayHeight / 1027.0));

        for (int i = 0; i < buttons.size(); i++) {
            SimpleButton button = buttons.get(i);
            button.drawScreen(mouseX, mouseY);
            button.updatePos(x + (i * (width / buttons.size())), y + height - 20, width / buttons.size(), 10);
        }
    }

    // Check if mouse is clicked in the bottom right corner of the window
    public boolean isMouseInsideBottomRightCorner(int mouseX, int mouseY) {
        return Utils.ScaledMouseCheck(mouseX, mouseY, x + width - 10, y + height - 10, 10, 10);
    }

    // mouse released
    public void mouseReleased() {
        isDragging = false;
        isExpanding = false;
    }

    // mouse clicked
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        textbox.mouseClicked(mouseX, mouseY, mouseButton);
        if (isMouseInsideHeader(mouseX, mouseY)) {
            isDragging = true;
            dragX = mouseX - (x * Minecraft.getMinecraft().displayWidth / 1920.0);
            dragY = mouseY - (y * Minecraft.getMinecraft().displayHeight / 1027.0);
        }
        if (isMouseInsideBottomRightCorner(mouseX, mouseY)) {
            isExpanding = true;
            dragX = mouseX - x;
            dragY = mouseY - y;
        }
        if (isMouseInside(mouseX, mouseY)) {
            for (SimpleButton button : buttons)
                if (button.mouseClicked(mouseX, mouseY, mouseButton)) {
                    buttonPressed(button);
                    break;
                }

            commonMouseClicked(mouseX, mouseY, mouseButton);
        } else listGui.lastSelected = -1;
    }

    // this is to be overridden by the child class
    public void commonMouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

    // this is to be overridden by the child class
    public boolean elementClicked(SelectedThing selectedThing) {
        return true;
    }

    // this is to be overridden by the child class
    public void buttonPressed(SimpleButton button) {
    }

    // key typed
    public void keyTyped(char typedChar, int keyCode) {
        textbox.textboxKeyTyped(typedChar, keyCode);
        listGui.search(textbox.getText(), getItems());
    }

    // updateScreen
    public void updateScreen() {
        textbox.updateCursorCounter();
    }

    public void handleMouseInput(int mouseX, int mouseY) throws IOException {
        listGui.handleMouseInput(mouseX, mouseY);
    }

    public void drawSlot(String name, boolean isSelected, int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
    }

    public void drawBackground() {
    }

    public void open() {
        this.x = 100;
        this.y = 100;
    }

    public List<SelectedThing> getListGui() {
        return listGui.list;
    }

    public void close() {
    }

    public void firstTime() {
    }

    public static class SelectedThing {
        public String name;
        public boolean isSelected;
        public Packet packet;
        public Entity entity;

        public SelectedThing(String name, boolean isSelected, Entity entity) {
            this.name = name;
            this.entity = entity;
            this.isSelected = isSelected;
        }
        public SelectedThing(String name, boolean isSelected) {
            this.name = name;
            this.isSelected = isSelected;
        }
    }

    public void fromJson(JsonElement json) {
        if (!json.isJsonArray())
            return;
        for (JsonElement element : json.getAsJsonArray()) {
            if (!element.isJsonPrimitive())
                continue;
            String name = element.getAsString();
            for (SelectedThing selectedThing : listGui.list) {
                if (selectedThing.name.equalsIgnoreCase(name)) {
                    selectedThing.isSelected = true;
                }
            }
        }
    }

    public JsonElement toJson() {
        JsonArray json = new JsonArray();
        for (SelectedThing selectedThing : listGui.list) {
            if (selectedThing.isSelected)
                json.add(new JsonPrimitive(selectedThing.name));
        }
        return json;
    }

}
