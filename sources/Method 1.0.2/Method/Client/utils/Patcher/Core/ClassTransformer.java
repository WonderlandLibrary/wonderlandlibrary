package Method.Client.utils.Patcher.Core;


import Method.Client.utils.Patcher.ModClassVisitor;
import Method.Client.utils.Patcher.Patches.*;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.util.HashMap;

public final class ClassTransformer implements IClassTransformer {

    private final HashMap<String, Class<? extends ModClassVisitor>> visitors = new HashMap<>();

    public static boolean obfuscated = !FMLDeobfuscatingRemapper.INSTANCE.unmap("net/minecraft/client/Minecraft").equals("net/minecraft/client/Minecraft");

    public ClassTransformer() {
        visitors.put("net.minecraft.client.entity.EntityPlayerSP", EntityPlayerSPVisitor.class);
        visitors.put("net.minecraft.entity.player.EntityPlayer", EntityPlayerVisitor.class);
        visitors.put("net.minecraft.block.state.BlockStateContainer$StateImplementation", StateImplementationVisitor.class);
        visitors.put("net.minecraft.block.BlockLiquid", BlockLiquidVisitor.class);
        visitors.put("net.minecraft.client.multiplayer.PlayerControllerMP", PlayerControllerMPVisitor.class);
        visitors.put("net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher", TileEntityRendererDispatcherVisitor.class);
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {

        if (!visitors.containsKey(transformedName))
            return basicClass;

        System.out.println("Transforming " + transformedName + ", obfuscated=" + obfuscated);
        if (basicClass == null)
            return null;
        try {

            ClassReader reader = new ClassReader(basicClass);
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

            // Contained you are debuging this is the line you want to look at, We create a new Class Visitor, get the visitor with the correct name, find that classes
            // constructor. THEN! we pass into that new constructor the writer and bool obf. Note that the writer is of type 3 and is only used to pass back
            // to the visitor!

            reader.accept(visitors.get(transformedName).getConstructor(ClassVisitor.class, Boolean.TYPE).newInstance(writer, obfuscated), 0);
            return writer.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return basicClass;
        }

    }


}
