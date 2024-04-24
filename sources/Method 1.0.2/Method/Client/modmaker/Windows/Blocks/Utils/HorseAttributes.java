package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.passive.AbstractHorse;

public class HorseAttributes extends Block {

    public HorseAttributes() {
        super("HorseAttributes", MainBlockType.Numbers, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter, BlockObjects.DropDown);
        ddOptions.add("getAIMoveSpeed");
        ddOptions.add("Jump");
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Returns the value of the selected attribute of the horse getAIMoveSpeed";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "getAIMoveSpeed":
                return ((AbstractHorse) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getAIMoveSpeed() * 43.17;
            case "Jump":
                return (-0.1817584952
                        * Math.pow(((AbstractHorse) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getHorseJumpStrength(), 3) + 3.689713992
                        * Math.pow(((AbstractHorse) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getHorseJumpStrength(), 2) + 2.128599134
                        * ((AbstractHorse) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getHorseJumpStrength() - 0.343930367);


        }
        return 0;
    }

}
