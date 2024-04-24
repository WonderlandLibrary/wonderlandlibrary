package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class FovSettingGet extends Block {

    public FovSettingGet() {
        super("FovSettingGet", MainBlockType.Numbers, Tabs.Game, BlockObjects.Name);

        this.description = "Sets ingame Fov setting" + "\n" + "mc.gameSettings.fovSetting";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
      return   Minecraft.getMinecraft().gameSettings.fovSetting;
    }

}
