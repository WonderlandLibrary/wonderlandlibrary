package Method.Client.utils.Screens.Custom;

import Method.Client.managers.FileManager;
import Method.Client.utils.Screens.SubGui;
import Method.Client.utils.visual.RenderUtils;
import Method.Client.utils.visual.SimpleButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;

public class EspSub extends SubGui {

    public EspSub() {
        super("Esp");
    }

    @Override
    public ArrayList<SelectedThing> getItems() {
        ArrayList<SelectedThing> items = new ArrayList<>();
        for (ResourceLocation resourceLocation : EntityList.getEntityNameList())
            items.add(new SelectedThing(resourceLocation.toString(), false));
        return items;
    }

    @Override
    public void close() {
        FileManager.saveData(FileManager.files[1]);
    }

    @Override
    public ArrayList<SimpleButton> getButtons() {
        ArrayList<SimpleButton> buttons = new ArrayList<>();
        buttons.add(new SimpleButton(0, 10, 10, 100, 20, "Passive", "Passive"));
        buttons.add(new SimpleButton(1, 10, 10, 100, 20, "Hostile", "Hostile"));
        buttons.add(new SimpleButton(2, 10, 10, 100, 20, "All", "All"));
        buttons.add(new SimpleButton(3, 10, 10, 100, 20, "Clear", "Clear"));
        return buttons;
    }

    @Override
    public void buttonPressed(SimpleButton button) {
        if (button.id == 0) {
            if (Minecraft.getMinecraft().world != null)
                for (ResourceLocation resourceLocation : EntityList.getEntityNameList()) {
                    if (EntityList.createEntityByIDFromName(resourceLocation, Minecraft.getMinecraft().world) instanceof IAnimals && !(EntityList.createEntityByIDFromName(resourceLocation, Minecraft.getMinecraft().world) instanceof IMob)) {
                        for (SelectedThing item : listGui.list) {
                            if (item.name.equalsIgnoreCase(resourceLocation.toString()))
                                item.isSelected = true;
                        }
                    }
                }
        } else if (button.id == 1) {
            if (Minecraft.getMinecraft().world != null)
                for (ResourceLocation resourceLocation : EntityList.getEntityNameList()) {
                    if (EntityList.createEntityByIDFromName(resourceLocation, Minecraft.getMinecraft().world) instanceof IMob) {
                        for (SelectedThing item : listGui.list) {
                            if (item.name.equalsIgnoreCase(resourceLocation.toString())) {
                                item.isSelected = true;
                            }
                        }
                    }
                }
        } else if (button.id == 2) {
            if (Minecraft.getMinecraft().world != null)
                for (ResourceLocation resourceLocation : EntityList.getEntityNameList()) {
                    for (SelectedThing item : listGui.list) {
                        if (item.name.equalsIgnoreCase(resourceLocation.toString()))
                            item.isSelected = true;
                    }
                }
        } else if (button.id == 3) {
            for (SelectedThing item : listGui.list) {
                item.isSelected = false;
            }
        }
    }

    @Override
    public void drawSlot(String name, boolean isSelected, int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
        if (isSelected)
            RenderUtils.scaledText(name, x + 5, slotTop, new Color(25, 211, 25, 1).getRGB(), true);
        else
            RenderUtils.scaledText(name, x + 5, slotTop, new Color(211, 25, 25, 1).getRGB(), true);
    }


}
