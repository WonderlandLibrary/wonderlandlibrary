/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.objectweb.asm.tree.TryCatchBlockNode
 */
package codes.som.anthony.koffee;

import java.util.List;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.TryCatchBlockNode;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001R\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2={"Lcodes/som/anthony/koffee/TryCatchContainer;", "", "tryCatchBlocks", "", "Lorg/objectweb/asm/tree/TryCatchBlockNode;", "getTryCatchBlocks", "()Ljava/util/List;", "koffee"})
public interface TryCatchContainer {
    @NotNull
    public List<TryCatchBlockNode> getTryCatchBlocks();
}

