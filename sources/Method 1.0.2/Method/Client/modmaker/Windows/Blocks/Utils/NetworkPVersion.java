package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.realms.RealmsSharedConstants;

public class NetworkPVersion extends Block {

    public NetworkPVersion() {
        super("NetworkPVersion", MainBlockType.Default, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.description = "Sets the network protocol version";

    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        RealmsSharedConstants.NETWORK_PROTOCOL_VERSION = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
        super.runCode(dragableBlock, event);
    }


}
