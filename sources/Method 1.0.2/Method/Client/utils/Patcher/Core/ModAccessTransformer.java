package Method.Client.utils.Patcher.Core;


import java.io.IOException;

public final class ModAccessTransformer extends net.minecraftforge.fml.common.asm.transformers.AccessTransformer {
    public ModAccessTransformer() throws IOException {
        super("META-INF/accesstransformer.cfg");
    }
}