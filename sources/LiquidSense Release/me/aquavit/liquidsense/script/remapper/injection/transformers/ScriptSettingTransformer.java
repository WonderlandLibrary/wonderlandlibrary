package me.aquavit.liquidsense.script.remapper.injection.transformers;

import me.aquavit.liquidsense.script.remapper.injection.utils.ClassUtils;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.tree.ClassNode;

public class ScriptSettingTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (name.equals("me.aquavit.liquidsense.script.api.global.Setting")) {
            final ClassNode classNode = ClassUtils.toClassNode(basicClass);
            classNode.methods.forEach(methodNode -> {
                if(methodNode.name.equals("boolean2")){
                    methodNode.name = "boolean";
                }
                if(methodNode.name.equals("float2")){
                    methodNode.name = "float";
                }
            });
            return ClassUtils.toBytes(classNode);
        }
        return basicClass;
    }
}
