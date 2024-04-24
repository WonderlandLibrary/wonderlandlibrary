package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class ChatFormatting extends Block {

    public ChatFormatting() {
        super("ChatFormatting", MainBlockType.String, Tabs.Utils, BlockObjects.Name, BlockObjects.DropDown);
        ddOptions.add("BLACK");
        ddOptions.add("DARK_BLUE");
        ddOptions.add("DARK_GREEN");
        ddOptions.add("DARK_AQUA");
        ddOptions.add("DARK_RED");
        ddOptions.add("DARK_PURPLE");
        ddOptions.add("GOLD");
        ddOptions.add("GRAY");
        ddOptions.add("DARK_GRAY");
        ddOptions.add("BLUE");
        ddOptions.add("GREEN");
        ddOptions.add("AQUA");
        ddOptions.add("RED");
        ddOptions.add("LIGHT_PURPLE");
        ddOptions.add("YELLOW");
        ddOptions.add("WHITE");
        ddOptions.add("OBFUSCATED");
        ddOptions.add("BOLD");
        ddOptions.add("STRIKETHROUGH");
        ddOptions.add("UNDERLINE");
        ddOptions.add("ITALIC");
        ddOptions.add("RESET");
        this.description = "Returns a ChatFormatting object" + "\n" + "Used to change the color of the text";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return com.mojang.realmsclient.gui.ChatFormatting.getByName(dragableBlock.dropDowns.getSelected());
    }

}
