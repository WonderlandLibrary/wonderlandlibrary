package Method.Client.utils.Patcher.Core;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;
import java.util.Map;

@IFMLLoadingPlugin.MCVersion(value = "1.12.2")
@IFMLLoadingPlugin.TransformerExclusions(value = "Method.Client.utils.Patcher.Core")
public final class CoreModPatcher implements IFMLLoadingPlugin {
    public static boolean IN_MCP = false;

    public CoreModPatcher(){
    }


    @Override
    public String[] getASMTransformerClass() {
        return new String[]{ClassTransformer.class.getName()};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map data) {
        IN_MCP = !(Boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
         return ModAccessTransformer.class.getName();
    }


}
