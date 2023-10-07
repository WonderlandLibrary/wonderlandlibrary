/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.launchwrapper.IClassTransformer
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.ClassNode
 *  org.objectweb.asm.tree.FieldInsnNode
 *  org.objectweb.asm.tree.MethodInsnNode
 *  org.objectweb.asm.tree.VarInsnNode
 */
package net.ccbluex.liquidbounce.script.remapper.injection.transformers;

import net.ccbluex.liquidbounce.script.remapper.injection.utils.ClassUtils;
import net.ccbluex.liquidbounce.script.remapper.injection.utils.NodeUtils;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class AbstractJavaLinkerTransformer
implements IClassTransformer {
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (name.equals("jdk.internal.dynalink.beans.AbstractJavaLinker")) {
            try {
                ClassNode classNode = ClassUtils.INSTANCE.toClassNode(basicClass);
                classNode.methods.forEach(methodNode -> {
                    switch (methodNode.name + methodNode.desc) {
                        case "addMember(Ljava/lang/String;Ljava/lang/reflect/AccessibleObject;Ljava/util/Map;)V": {
                            methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), NodeUtils.INSTANCE.toNodes(new AbstractInsnNode[]{new VarInsnNode(25, 0), new FieldInsnNode(180, "jdk/internal/dynalink/beans/AbstractJavaLinker", "clazz", "Ljava/lang/Class;"), new VarInsnNode(25, 1), new VarInsnNode(25, 2), new MethodInsnNode(184, "net/ccbluex/liquidbounce/script/remapper/injection/transformers/handlers/AbstractJavaLinkerHandler", "addMember", "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/reflect/AccessibleObject;)Ljava/lang/String;", false), new VarInsnNode(58, 1)}));
                            break;
                        }
                        case "addMember(Ljava/lang/String;Ljdk/internal/dynalink/beans/SingleDynamicMethod;Ljava/util/Map;)V": {
                            methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), NodeUtils.INSTANCE.toNodes(new AbstractInsnNode[]{new VarInsnNode(25, 0), new FieldInsnNode(180, "jdk/internal/dynalink/beans/AbstractJavaLinker", "clazz", "Ljava/lang/Class;"), new VarInsnNode(25, 1), new MethodInsnNode(184, "net/ccbluex/liquidbounce/script/remapper/injection/transformers/handlers/AbstractJavaLinkerHandler", "addMember", "(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/String;", false), new VarInsnNode(58, 1)}));
                            break;
                        }
                        case "setPropertyGetter(Ljava/lang/String;Ljdk/internal/dynalink/beans/SingleDynamicMethod;Ljdk/internal/dynalink/beans/GuardedInvocationComponent$ValidationType;)V": {
                            methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), NodeUtils.INSTANCE.toNodes(new AbstractInsnNode[]{new VarInsnNode(25, 0), new FieldInsnNode(180, "jdk/internal/dynalink/beans/AbstractJavaLinker", "clazz", "Ljava/lang/Class;"), new VarInsnNode(25, 1), new MethodInsnNode(184, "net/ccbluex/liquidbounce/script/remapper/injection/transformers/handlers/AbstractJavaLinkerHandler", "setPropertyGetter", "(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/String;", false), new VarInsnNode(58, 1)}));
                        }
                    }
                });
                return ClassUtils.INSTANCE.toBytes(classNode);
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return basicClass;
    }
}

