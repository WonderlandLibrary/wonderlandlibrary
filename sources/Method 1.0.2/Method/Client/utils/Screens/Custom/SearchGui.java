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

public class SearchGui extends SubGui {

    public SearchGui() {
        super("Search");
    }

    public static final ArrayList<String> important = new ArrayList<>();

    static {
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.ENDER_CHEST));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.CHEST));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.TRAPPED_CHEST));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.CRAFTING_TABLE));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.ANVIL));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.BREWING_STAND));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.HOPPER));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.DROPPER));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.DISPENSER));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.TRAPDOOR));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.ENCHANTING_TABLE));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.WHITE_SHULKER_BOX));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.ORANGE_SHULKER_BOX));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.MAGENTA_SHULKER_BOX));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.LIGHT_BLUE_SHULKER_BOX));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.YELLOW_SHULKER_BOX));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.LIME_SHULKER_BOX));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.PINK_SHULKER_BOX));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.GRAY_SHULKER_BOX));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.SILVER_SHULKER_BOX));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.CYAN_SHULKER_BOX));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.PURPLE_SHULKER_BOX));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.BLUE_SHULKER_BOX));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.BROWN_SHULKER_BOX));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.GREEN_SHULKER_BOX));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.RED_SHULKER_BOX));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.BLACK_SHULKER_BOX));
        important.add("" + Block.REGISTRY.getNameForObject(Blocks.PORTAL));
    }

    @Override
    public ArrayList<SelectedThing> getItems() {
        ArrayList<SelectedThing> items = new ArrayList<>();
        for (Block block : Block.REGISTRY) {
            items.add(new SelectedThing("" + Block.REGISTRY.getNameForObject(block), false));
        }
        return items;
    }

    @Override
    public void close() {
        FileManager.saveData(FileManager.files[5]);
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


}
