package Method.Client.modmaker.Windows;

import Method.Client.utils.font.CFont;
import Method.Client.utils.visual.RenderUtils;
import Method.Client.utils.visual.SerializableGuiTextField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class DropDownList implements Serializable {

    public DropDownList(ArrayList<String> Drop) {
        if (Drop != null)
            dropDowns = Drop;
        else
            dropDowns.add("Empty");

        dropSize = 10;
        selected = 0;
        if (dropDowns.size() > 0)
            this.width = Math.max(CFont.afontRenderer16.getStringWidth(dropDowns.get(selected) + " "), 25);
        int ddwidth = dropDowns.stream().mapToInt(dropDown -> Minecraft.getMinecraft().fontRenderer.getStringWidth(dropDown + " ")).filter(dropDown -> dropDown >= 20).max().orElse(20);
        searchField = new SerializableGuiTextField(0, this.x, this.y - 10, ddwidth, 10);
        searchField.setMaxStringLength(20);
    }

    public int x;
    public int y;
    public int width;
    public int dropSize;

    public SerializableGuiTextField searchField;

    public int hovered; // int for list element hovered
    public int selected; // Selected list element
    public boolean open;
    public ArrayList<String> dropDowns = new ArrayList<>();

    public void mouseClicked(int mouseX, int mouseY) {
        if (hovered != -1) {
            selected = hovered;
            hovered = -1;
            this.open = false;
            this.width = Math.max(CFont.afontRenderer16.getStringWidth(dropDowns.get(selected) + " "), 20);
            return;
        }
        if (withinBoundsComponent(mouseX, mouseY)) {
            this.open = !this.open;
        }
        if (this.open) {
            searchField.mouseClicked(mouseX, mouseY, 0);
            this.width = dropDowns.stream().mapToInt(dropDown -> Minecraft.getMinecraft().fontRenderer.getStringWidth(dropDown + " ")).filter(dropDown -> dropDown >= 20).max().orElse(20);
        }
        if (!this.open)
            this.width = Math.max(CFont.afontRenderer16.getStringWidth(dropDowns.get(selected) + " "), 20);
    }

    public void drawScreen(int mouseX, int mouseY) {
        Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.dropSize, this.open ? new Color(100, 252, 95, 188).getRGB() : new Color(243, 45, 38, 189).getRGB());
        RenderUtils.drawRectOutline(this.x, this.y, this.x + this.width, this.y + this.dropSize, 1, -1);
        if (selected > -1 && !dropDowns.isEmpty()) {
            CFont.afontRenderer16.drawStringWithShadow(dropDowns.get(selected), this.x + 2, this.y + 1, -1);
        }

        if (this.open) {
            searchField.x = this.x;
            searchField.y = this.y - 10;
            searchField.drawTextBox();
            boolean inbounds = this.withinBoundsComponentExtended(mouseX, mouseY);
            if (dropDowns != null && !dropDowns.isEmpty()) {
                ArrayList<String> searchable = new ArrayList<>(dropDowns);
                if (!searchField.getText().isEmpty()) {
                    searchable.clear();
                    for (String s : dropDowns) {
                        if (s.toLowerCase().contains(searchField.getText().toLowerCase()))
                            searchable.add(s);
                    }
                }
                for (int i = 0; i < searchable.size(); i++) {
                    if (inbounds)
                        if (mouseY > this.y + this.dropSize + (i * dropSize) && mouseY < this.y + this.dropSize + dropSize + (i * dropSize)) {
                            String word = searchable.get(i);
                            hovered = dropDowns.indexOf(word);
                        }
                    Gui.drawRect(this.x, this.y + (i * dropSize) + dropSize, this.x + this.width, this.y + this.dropSize + (i * dropSize) + dropSize, new Color(54, 51, 51, 238).getRGB());
                    RenderUtils.drawRectOutline(this.x, this.y + (i * dropSize) + dropSize, this.x + this.width, this.y + this.dropSize + (i * dropSize) + dropSize, 1, -1);


                    CFont.afontRenderer16.drawStringWithShadow(searchable.get(i), this.x + 2, 1 + this.y + (i * dropSize) + this.dropSize, -1);


                    if (hovered == i)
                        RenderUtils.drawRectOutline(this.x, this.y + (i * dropSize) + dropSize, this.x + this.width, this.y + this.dropSize + (i * dropSize) + dropSize, 1, new Color(178, 149, 1, 255).getRGB());

                }
            }
            if (!inbounds)
                hovered = -1;
        } else hovered = -1;
    }

    private boolean withinBoundsComponent(int mouseX, int mouseY) {
        return mouseX > this.x && mouseX < this.x + this.width && mouseY > this.y && mouseY < this.y + this.dropSize;
    }

    private boolean withinBoundsComponentExtended(int mouseX, int mouseY) {
        if (mouseX > this.x && mouseX < this.x + this.width) {
            return mouseY > this.y && mouseY < this.y + this.dropSize + (dropDowns.size() * dropSize);
        }
        return false;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean isState() {
        return open;
    }

    public void setState(boolean state) {
        this.open = state;
    }

    public String getSelected() {
        return this.dropDowns.get(selected);
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (this.open)
            searchField.textboxKeyTyped(typedChar, keyCode);
        else searchField.setText("");
    }

    public void updateScreen() {
        if (this.open)
            searchField.updateCursorCounter();
    }
}
