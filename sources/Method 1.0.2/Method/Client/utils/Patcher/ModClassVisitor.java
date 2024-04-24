package Method.Client.utils.Patcher;

import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.HashMap;
import java.util.Map;

public abstract class ModClassVisitor extends ClassVisitor {

    private final Map<String, MethodVisitorFactory> methodVisitorRegistry = new HashMap<>();

    public ModClassVisitor(ClassVisitor cv) {
        super(Opcodes.ASM4, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        return methodVisitorRegistry.getOrDefault(name + desc, MethodVisitorFactory.DEFAULT).createMethodVisitor(mv);
    }

    protected String unmap(String typeName) {
        return FMLDeobfuscatingRemapper.INSTANCE.unmap(typeName);
    }

    protected void registerMethodVisitor(String name, String desc, MethodVisitorFactory factory) {
        methodVisitorRegistry.put(name + desc, factory);
    }

    public interface MethodVisitorFactory {
        MethodVisitor createMethodVisitor(MethodVisitor mv);

        MethodVisitorFactory DEFAULT = mv -> mv;
    }
}


