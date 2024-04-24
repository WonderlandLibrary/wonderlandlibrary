package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketRecipeBook;

public class recipeBook extends Block {

    public recipeBook() {
        super("recipeBook", MainBlockType.RecipeBook, Tabs.NewItem, BlockObjects.Name, BlockObjects.DropDown);
        for (SPacketRecipeBook.State value : SPacketRecipeBook.State.values()) {
            ddOptions.add(value.toString());
        }
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return SPacketRecipeBook.State.valueOf(dragableBlock.dropDowns.getSelected());
    }


}
