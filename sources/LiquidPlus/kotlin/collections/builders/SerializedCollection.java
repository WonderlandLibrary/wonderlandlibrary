/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.collections.builders;

import java.io.Externalizable;
import java.io.InvalidObjectException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u001e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0000\u0018\u0000 \u00112\u00020\u0001:\u0001\u0011B\u0007\b\u0016\u00a2\u0006\u0002\u0010\u0002B\u0019\u0012\n\u0010\u0003\u001a\u0006\u0012\u0002\b\u00030\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016J\b\u0010\f\u001a\u00020\rH\u0002J\u0010\u0010\u000e\u001a\u00020\t2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016R\u0012\u0010\u0003\u001a\u0006\u0012\u0002\b\u00030\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2={"Lkotlin/collections/builders/SerializedCollection;", "Ljava/io/Externalizable;", "()V", "collection", "", "tag", "", "(Ljava/util/Collection;I)V", "readExternal", "", "input", "Ljava/io/ObjectInput;", "readResolve", "", "writeExternal", "output", "Ljava/io/ObjectOutput;", "Companion", "kotlin-stdlib"})
public final class SerializedCollection
implements Externalizable {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private Collection<?> collection;
    private final int tag;
    private static final long serialVersionUID = 0L;
    public static final int tagList = 0;
    public static final int tagSet = 1;

    public SerializedCollection(@NotNull Collection<?> collection, int tag) {
        Intrinsics.checkNotNullParameter(collection, "collection");
        this.collection = collection;
        this.tag = tag;
    }

    public SerializedCollection() {
        this(CollectionsKt.emptyList(), 0);
    }

    @Override
    public void writeExternal(@NotNull ObjectOutput output) {
        Intrinsics.checkNotNullParameter(output, "output");
        output.writeByte(this.tag);
        output.writeInt(this.collection.size());
        for (Object element : this.collection) {
            output.writeObject(element);
        }
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void readExternal(@NotNull ObjectInput input) {
        Collection collection;
        Intrinsics.checkNotNullParameter(input, "input");
        byte flags = input.readByte();
        int tag = flags & 1;
        int other = flags & 0xFFFFFFFE;
        if (other != 0) {
            throw new InvalidObjectException("Unsupported flags value: " + flags + '.');
        }
        int size = input.readInt();
        if (size < 0) {
            throw new InvalidObjectException("Illegal size value: " + size + '.');
        }
        SerializedCollection serializedCollection = this;
        int n = tag;
        switch (n) {
            case 0: {
                List list;
                List list2 = list = CollectionsKt.createListBuilder(size);
                SerializedCollection serializedCollection2 = serializedCollection;
                boolean bl = false;
                int n2 = 0;
                while (n2 < size) {
                    void $this$readExternal_u24lambda_u2d1;
                    int n3;
                    int it = n3 = n2++;
                    boolean bl2 = false;
                    $this$readExternal_u24lambda_u2d1.add(input.readObject());
                }
                Unit unit = Unit.INSTANCE;
                SerializedCollection serializedCollection3 = serializedCollection2;
                collection = CollectionsKt.build(list);
                break;
            }
            case 1: {
                Set set;
                Set $this$readExternal_u24lambda_u2d1 = set = SetsKt.createSetBuilder(size);
                SerializedCollection serializedCollection4 = serializedCollection;
                boolean bl = false;
                int n4 = 0;
                while (n4 < size) {
                    void $this$readExternal_u24lambda_u2d3;
                    int n5;
                    int it = n5 = n4++;
                    boolean bl3 = false;
                    $this$readExternal_u24lambda_u2d3.add(input.readObject());
                }
                Unit unit = Unit.INSTANCE;
                SerializedCollection serializedCollection3 = serializedCollection4;
                collection = SetsKt.build(set);
                break;
            }
            default: {
                throw new InvalidObjectException("Unsupported collection type tag: " + tag + '.');
            }
        }
        serializedCollection3.collection = collection;
    }

    private final Object readResolve() {
        return this.collection;
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2={"Lkotlin/collections/builders/SerializedCollection$Companion;", "", "()V", "serialVersionUID", "", "tagList", "", "tagSet", "kotlin-stdlib"})
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

