/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.objectweb.asm.Label
 *  org.objectweb.asm.tree.LabelNode
 */
package codes.som.anthony.koffee.labels;

import codes.som.anthony.koffee.labels.KoffeeLabel;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Label;
import org.objectweb.asm.tree.LabelNode;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=2, d1={"\u0000\u0014\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0012\u0010\u0000\u001a\u00020\u00012\n\u0010\u0002\u001a\u00060\u0003j\u0002`\u0004*\n\u0010\u0005\"\u00020\u00032\u00020\u0003\u00a8\u0006\u0006"}, d2={"coerceLabel", "Lorg/objectweb/asm/tree/LabelNode;", "value", "", "Lcodes/som/anthony/koffee/labels/LabelLike;", "LabelLike", "koffee"})
public final class LabelsKt {
    @NotNull
    public static final LabelNode coerceLabel(@NotNull Object value) {
        LabelNode labelNode;
        Intrinsics.checkParameterIsNotNull(value, "value");
        Object object = value;
        if (object instanceof LabelNode) {
            labelNode = (LabelNode)value;
        } else if (object instanceof Label) {
            labelNode = new LabelNode((Label)value);
        } else if (object instanceof KoffeeLabel) {
            labelNode = ((KoffeeLabel)value).getLabel$koffee();
        } else {
            String string = "Non label-like object passed to coerceLabel()";
            boolean bl = false;
            throw (Throwable)new IllegalStateException(string.toString());
        }
        return labelNode;
    }
}

