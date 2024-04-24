package Method.Client.utils.Screens.Custom;

import Method.Client.managers.FileManager;
import Method.Client.utils.Screens.SubGui;
import Method.Client.utils.visual.RenderUtils;
import Method.Client.utils.visual.SimpleButton;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;

import java.awt.*;
import java.util.ArrayList;

public class XrayGui extends SubGui {

    public XrayGui() {
        super("Xray");
    }

    @Override
    public ArrayList<SelectedThing> getItems() {
        ArrayList<SelectedThing> items = new ArrayList<>();
        for (Block block : Block.REGISTRY) {
            items.add(new SelectedThing("" + Block.REGISTRY.getNameForObject(block), false));
        }
        return items;
    }

    public static final ArrayList<String> important = new ArrayList<>();

    static {
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.END_PORTAL_FRAME));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.END_PORTAL));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.PORTAL));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.MOB_SPAWNER));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.LAVA));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.EMERALD_ORE));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.EMERALD_BLOCK));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.DIAMOND_ORE));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.DIAMOND_BLOCK));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.QUARTZ_ORE));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.REDSTONE_BLOCK));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.LIT_REDSTONE_ORE));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.REDSTONE_ORE));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.LAPIS_BLOCK));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.LAPIS_ORE));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.GOLD_BLOCK));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.GOLD_ORE));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.IRON_BLOCK));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.IRON_ORE));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.COAL_BLOCK));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.COAL_ORE));

    }

    @Override
    public void close() {
        FileManager.saveData(FileManager.files[2]);
    }

    @Override
    public ArrayList<SimpleButton> getButtons() {
        ArrayList<SimpleButton> buttons = new ArrayList<>();
        buttons.add(new SimpleButton(0, 10, 10, 100, 20, "Important", "Important"));
        buttons.add(new SimpleButton(1, 10, 10, 100, 20, "All", "All"));
        buttons.add(new SimpleButton(2, 10, 10, 100, 20, "Clear", "Clear"));
        return buttons;
    }

    @Override
    public void buttonPressed(SimpleButton button) {
        if (button.id == 0) {
            for (SelectedThing item : listGui.list) {
                if (important.contains(item.name)) {
                    item.isSelected = true;
                }
            }
        } else if (button.id == 1) {
            for (SelectedThing item : listGui.list) {
                item.isSelected = true;
            }
        } else if (button.id == 2) {
            for (SelectedThing item : listGui.list) {
                item.isSelected = false;
            }
        }
    }

    @Override
    public void firstTime() {
        for (SelectedThing item : listGui.list) {
            if (important.contains(item.name)) {
                item.isSelected = true;
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


    public void checkfirstrun() {
        if (listGui.list.isEmpty())
            firstTime();
    }
}
