/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.objectweb.asm.Type
 *  org.objectweb.asm.tree.InsnList
 *  org.objectweb.asm.tree.TryCatchBlockNode
 */
package codes.som.anthony.koffee;

import codes.som.anthony.koffee.TryCatchContainer;
import codes.som.anthony.koffee.insns.InstructionAssembly;
import codes.som.anthony.koffee.labels.LabelRegistry;
import codes.som.anthony.koffee.labels.LabelScope;
import codes.som.anthony.koffee.modifiers.abstract;
import codes.som.anthony.koffee.modifiers.annotation;
import codes.som.anthony.koffee.modifiers.bridge;
import codes.som.anthony.koffee.modifiers.deprecated;
import codes.som.anthony.koffee.modifiers.enum;
import codes.som.anthony.koffee.modifiers.final;
import codes.som.anthony.koffee.modifiers.interface;
import codes.som.anthony.koffee.modifiers.mandated;
import codes.som.anthony.koffee.modifiers.module;
import codes.som.anthony.koffee.modifiers.native;
import codes.som.anthony.koffee.modifiers.open;
import codes.som.anthony.koffee.modifiers.package_private;
import codes.som.anthony.koffee.modifiers.private;
import codes.som.anthony.koffee.modifiers.protected;
import codes.som.anthony.koffee.modifiers.public;
import codes.som.anthony.koffee.modifiers.static;
import codes.som.anthony.koffee.modifiers.static_phase;
import codes.som.anthony.koffee.modifiers.strict;
import codes.som.anthony.koffee.modifiers.super;
import codes.som.anthony.koffee.modifiers.synchronized;
import codes.som.anthony.koffee.modifiers.synthetic;
import codes.som.anthony.koffee.modifiers.transient;
import codes.som.anthony.koffee.modifiers.transitive;
import codes.som.anthony.koffee.modifiers.varargs;
import codes.som.anthony.koffee.modifiers.volatile;
import codes.som.anthony.koffee.sugar.ModifiersAccess;
import codes.som.anthony.koffee.sugar.TypesAccess;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.TryCatchBlockNode;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u00042\u00020\u0005B\u001b\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t\u00a2\u0006\u0002\u0010\u000bR\u0014\u0010\f\u001a\u00020\rX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0014\u0010\u0006\u001a\u00020\u0007X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u001a\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013\u00a8\u0006\u0014"}, d2={"Lcodes/som/anthony/koffee/BlockAssembly;", "Lcodes/som/anthony/koffee/insns/InstructionAssembly;", "Lcodes/som/anthony/koffee/TryCatchContainer;", "Lcodes/som/anthony/koffee/labels/LabelScope;", "Lcodes/som/anthony/koffee/sugar/ModifiersAccess;", "Lcodes/som/anthony/koffee/sugar/TypesAccess;", "instructions", "Lorg/objectweb/asm/tree/InsnList;", "tryCatchBlocks", "", "Lorg/objectweb/asm/tree/TryCatchBlockNode;", "(Lorg/objectweb/asm/tree/InsnList;Ljava/util/List;)V", "L", "Lcodes/som/anthony/koffee/labels/LabelRegistry;", "getL", "()Lcodes/som/anthony/koffee/labels/LabelRegistry;", "getInstructions", "()Lorg/objectweb/asm/tree/InsnList;", "getTryCatchBlocks", "()Ljava/util/List;", "koffee"})
public final class BlockAssembly
implements InstructionAssembly,
TryCatchContainer,
LabelScope,
ModifiersAccess,
TypesAccess {
    @NotNull
    private final LabelRegistry L;
    @NotNull
    private final InsnList instructions;
    @NotNull
    private final List<TryCatchBlockNode> tryCatchBlocks;

    @Override
    @NotNull
    public LabelRegistry getL() {
        return this.L;
    }

    @Override
    @NotNull
    public InsnList getInstructions() {
        return this.instructions;
    }

    @Override
    @NotNull
    public List<TryCatchBlockNode> getTryCatchBlocks() {
        return this.tryCatchBlocks;
    }

    public BlockAssembly(@NotNull InsnList instructions, @NotNull List<TryCatchBlockNode> tryCatchBlocks) {
        Intrinsics.checkParameterIsNotNull(instructions, "instructions");
        Intrinsics.checkParameterIsNotNull(tryCatchBlocks, "tryCatchBlocks");
        this.instructions = instructions;
        this.tryCatchBlocks = tryCatchBlocks;
        this.L = new LabelRegistry(this);
    }

    @Override
    @NotNull
    public package_private getPackage_private() {
        return ModifiersAccess.DefaultImpls.getPackage_private(this);
    }

    @Override
    @NotNull
    public public getPublic() {
        return ModifiersAccess.DefaultImpls.getPublic(this);
    }

    @Override
    @NotNull
    public private getPrivate() {
        return ModifiersAccess.DefaultImpls.getPrivate(this);
    }

    @Override
    @NotNull
    public protected getProtected() {
        return ModifiersAccess.DefaultImpls.getProtected(this);
    }

    @Override
    @NotNull
    public static getStatic() {
        return ModifiersAccess.DefaultImpls.getStatic(this);
    }

    @Override
    @NotNull
    public final getFinal() {
        return ModifiersAccess.DefaultImpls.getFinal(this);
    }

    @Override
    @NotNull
    public super getSuper() {
        return ModifiersAccess.DefaultImpls.getSuper(this);
    }

    @Override
    @NotNull
    public super get_super() {
        return ModifiersAccess.DefaultImpls.get_super(this);
    }

    @Override
    @NotNull
    public synchronized getSynchronized() {
        return ModifiersAccess.DefaultImpls.getSynchronized(this);
    }

    @Override
    @NotNull
    public open getOpen() {
        return ModifiersAccess.DefaultImpls.getOpen(this);
    }

    @Override
    @NotNull
    public transitive getTransitive() {
        return ModifiersAccess.DefaultImpls.getTransitive(this);
    }

    @Override
    @NotNull
    public volatile getVolatile() {
        return ModifiersAccess.DefaultImpls.getVolatile(this);
    }

    @Override
    @NotNull
    public bridge getBridge() {
        return ModifiersAccess.DefaultImpls.getBridge(this);
    }

    @Override
    @NotNull
    public static_phase getStatic_phase() {
        return ModifiersAccess.DefaultImpls.getStatic_phase(this);
    }

    @Override
    @NotNull
    public varargs getVarargs() {
        return ModifiersAccess.DefaultImpls.getVarargs(this);
    }

    @Override
    @NotNull
    public transient getTransient() {
        return ModifiersAccess.DefaultImpls.getTransient(this);
    }

    @Override
    @NotNull
    public native getNative() {
        return ModifiersAccess.DefaultImpls.getNative(this);
    }

    @Override
    @NotNull
    public interface getInterface() {
        return ModifiersAccess.DefaultImpls.getInterface(this);
    }

    @Override
    @NotNull
    public interface get_interface() {
        return ModifiersAccess.DefaultImpls.get_interface(this);
    }

    @Override
    @NotNull
    public abstract getAbstract() {
        return ModifiersAccess.DefaultImpls.getAbstract(this);
    }

    @Override
    @NotNull
    public strict getStrict() {
        return ModifiersAccess.DefaultImpls.getStrict(this);
    }

    @Override
    @NotNull
    public synthetic getSynthetic() {
        return ModifiersAccess.DefaultImpls.getSynthetic(this);
    }

    @Override
    @NotNull
    public annotation getAnnotation() {
        return ModifiersAccess.DefaultImpls.getAnnotation(this);
    }

    @Override
    @NotNull
    public enum getEnum() {
        return ModifiersAccess.DefaultImpls.getEnum(this);
    }

    @Override
    @NotNull
    public mandated getMandated() {
        return ModifiersAccess.DefaultImpls.getMandated(this);
    }

    @Override
    @NotNull
    public module getModule() {
        return ModifiersAccess.DefaultImpls.getModule(this);
    }

    @Override
    @NotNull
    public deprecated getDeprecated() {
        return ModifiersAccess.DefaultImpls.getDeprecated(this);
    }

    @Override
    public Type getVoid() {
        return TypesAccess.DefaultImpls.getVoid(this);
    }

    @Override
    public Type getChar() {
        return TypesAccess.DefaultImpls.getChar(this);
    }

    @Override
    public Type getByte() {
        return TypesAccess.DefaultImpls.getByte(this);
    }

    @Override
    public Type getShort() {
        return TypesAccess.DefaultImpls.getShort(this);
    }

    @Override
    public Type getInt() {
        return TypesAccess.DefaultImpls.getInt(this);
    }

    @Override
    public Type getFloat() {
        return TypesAccess.DefaultImpls.getFloat(this);
    }

    @Override
    public Type getLong() {
        return TypesAccess.DefaultImpls.getLong(this);
    }

    @Override
    public Type getDouble() {
        return TypesAccess.DefaultImpls.getDouble(this);
    }

    @Override
    public Type getBoolean() {
        return TypesAccess.DefaultImpls.getBoolean(this);
    }

    @Override
    @NotNull
    public Type coerceType(@NotNull Object value) {
        Intrinsics.checkParameterIsNotNull(value, "value");
        return TypesAccess.DefaultImpls.coerceType(this, value);
    }
}

