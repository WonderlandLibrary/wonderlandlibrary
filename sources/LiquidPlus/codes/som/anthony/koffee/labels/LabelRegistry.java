/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.objectweb.asm.tree.LabelNode
 */
package codes.som.anthony.koffee.labels;

import codes.som.anthony.koffee.insns.InstructionAssembly;
import codes.som.anthony.koffee.labels.KoffeeLabel;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.LabelNode;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\t\u001a\u00020\u00002\u0006\u0010\u0002\u001a\u00020\u0003J\u0011\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0086\u0002J\u0011\u0010\n\u001a\u00020\u000b2\u0006\u0010\u000e\u001a\u00020\u0007H\u0086\u0002J\u000e\u0010\u000f\u001a\u00020\u00002\u0006\u0010\u0002\u001a\u00020\u0003R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2={"Lcodes/som/anthony/koffee/labels/LabelRegistry;", "", "insns", "Lcodes/som/anthony/koffee/insns/InstructionAssembly;", "(Lcodes/som/anthony/koffee/insns/InstructionAssembly;)V", "labels", "", "", "Lorg/objectweb/asm/tree/LabelNode;", "copy", "get", "Lcodes/som/anthony/koffee/labels/KoffeeLabel;", "index", "", "name", "scope", "koffee"})
public final class LabelRegistry {
    private Map<String, LabelNode> labels;
    private final InstructionAssembly insns;

    @NotNull
    public final LabelRegistry copy(@NotNull InstructionAssembly insns) {
        Intrinsics.checkParameterIsNotNull(insns, "insns");
        LabelRegistry labelRegistry = new LabelRegistry(insns);
        boolean bl = false;
        boolean bl2 = false;
        LabelRegistry it = labelRegistry;
        boolean bl3 = false;
        it.labels = this.labels;
        return labelRegistry;
    }

    @NotNull
    public final LabelRegistry scope(@NotNull InstructionAssembly insns) {
        Intrinsics.checkParameterIsNotNull(insns, "insns");
        LabelRegistry labelRegistry = new LabelRegistry(insns);
        boolean bl = false;
        boolean bl2 = false;
        LabelRegistry it = labelRegistry;
        boolean bl3 = false;
        it.labels.putAll(this.labels);
        return labelRegistry;
    }

    @NotNull
    public final KoffeeLabel get(int index) {
        return this.get("label_" + index);
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public final KoffeeLabel get(@NotNull String name) {
        Object object;
        void $this$getOrPut$iv;
        Intrinsics.checkParameterIsNotNull(name, "name");
        Map<String, LabelNode> map = this.labels;
        InstructionAssembly instructionAssembly = this.insns;
        boolean $i$f$getOrPut = false;
        Object value$iv = $this$getOrPut$iv.get(name);
        if (value$iv == null) {
            boolean bl = false;
            LabelNode answer$iv = new LabelNode();
            $this$getOrPut$iv.put(name, answer$iv);
            object = answer$iv;
        } else {
            object = value$iv;
        }
        Object v = object;
        LabelNode labelNode = (LabelNode)v;
        InstructionAssembly instructionAssembly2 = instructionAssembly;
        return new KoffeeLabel(instructionAssembly2, labelNode);
    }

    public LabelRegistry(@NotNull InstructionAssembly insns) {
        Map map;
        Intrinsics.checkParameterIsNotNull(insns, "insns");
        this.insns = insns;
        LabelRegistry labelRegistry = this;
        boolean bl = false;
        labelRegistry.labels = map = (Map)new LinkedHashMap();
    }
}

