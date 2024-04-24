package Method.Client.utils.Screens;

import Method.Client.utils.visual.CGuiScrollingList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;

import java.util.ArrayList;
import java.util.List;

public class ListGui extends CGuiScrollingList {
    public List<SubGui.SelectedThing> list;
    public List<SubGui.SelectedThing> temp;
    public int lastSelected = -1;
    public SubGui parent;

    public ListGui(List<SubGui.SelectedThing> list, SubGui subGui) {
        super(Minecraft.getMinecraft(), 50, 50, 32, 100, 50, 16, Minecraft.getMinecraft().displayWidth,
                Minecraft.getMinecraft().displayHeight);
        // String arraylist called buttons
        this.list = list;
        this.parent = subGui;
        this.temp = null;
    }

    @Override
    protected int getSize() {
        if (temp != null)
            return temp.size();
        return list.size();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {
        if (temp == null) {
            if (index >= 0 && index < list.size()) {
                if (parent.elementClicked(list.get(index))) {
                    list.get(index).isSelected = !list.get(index).isSelected;
                    lastSelected = index;
                }
            }
        } else {
            if (index >= 0 && index < temp.size()) {
                if (parent.elementClicked(temp.get(index))) {
                    lastSelected = index;
                    for (SubGui.SelectedThing selectedThing : list) {
                        if (selectedThing.name.equalsIgnoreCase(temp.get(lastSelected).name)) {
                            selectedThing.isSelected = !selectedThing.isSelected;
                            temp.get(lastSelected).isSelected = !temp.get(lastSelected).isSelected;
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    protected boolean isSelected(int index) {
        return index == lastSelected;
    }

    @Override
    protected void drawBackground() {
    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
        if (temp == null)
            parent.drawSlot(list.get(slotIdx).name, list.get(slotIdx).isSelected, slotIdx, entryRight, slotTop, slotBuffer, tess);
        else
            parent.drawSlot(temp.get(slotIdx).name, temp.get(slotIdx).isSelected, slotIdx, entryRight, slotTop, slotBuffer, tess);
    }

    public void updateSize(int x, int y, int width, int height) {
        this.listWidth = width;
        this.listHeight = height;
        this.top = y;
        this.left = x;
        this.bottom = y + height;
        this.right = x + width;
        // slot height so there 25 items in the list
        this.slotHeight = height / 20;
    }

    public void search(String text, ArrayList<SubGui.SelectedThing> items) {
        if (!text.isEmpty()) {
            temp = items;
            for (SubGui.SelectedThing selectedThing : list) {
                for (SubGui.SelectedThing thing : temp) {
                    if (selectedThing.name.equalsIgnoreCase(thing.name)) {
                        thing.isSelected = selectedThing.isSelected;
                    }
                }
            }
            // remove
            ArrayList<SubGui.SelectedThing> temp2 = new ArrayList<>();
            for (SubGui.SelectedThing selectedThing : temp) {
                if (!selectedThing.name.toLowerCase().contains(text.toLowerCase())) {
                    temp2.add(selectedThing);
                }
            }
            temp.removeAll(temp2);
        } else {
            temp = null;
        }
    }

}
