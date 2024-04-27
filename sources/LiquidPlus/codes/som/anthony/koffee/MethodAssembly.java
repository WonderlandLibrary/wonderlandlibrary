/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.objectweb.asm.Type
 *  org.objectweb.asm.tree.InsnList
 *  org.objectweb.asm.tree.MethodNode
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
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TryCatchBlockNode;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u00042\u00020\u0005B\r\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bR\u0014\u0010\t\u001a\u00020\nX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0014\u0010\r\u001a\u00020\u000e8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R$\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0011\u001a\u00020\u00128F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R$\u0010\u0018\u001a\u00020\u00122\u0006\u0010\u0011\u001a\u00020\u00128F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u0019\u0010\u0015\"\u0004\b\u001a\u0010\u0017R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u001a\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u001f0\u001e8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b \u0010!\u00a8\u0006\""}, d2={"Lcodes/som/anthony/koffee/MethodAssembly;", "Lcodes/som/anthony/koffee/insns/InstructionAssembly;", "Lcodes/som/anthony/koffee/TryCatchContainer;", "Lcodes/som/anthony/koffee/labels/LabelScope;", "Lcodes/som/anthony/koffee/sugar/ModifiersAccess;", "Lcodes/som/anthony/koffee/sugar/TypesAccess;", "node", "Lorg/objectweb/asm/tree/MethodNode;", "(Lorg/objectweb/asm/tree/MethodNode;)V", "L", "Lcodes/som/anthony/koffee/labels/LabelRegistry;", "getL", "()Lcodes/som/anthony/koffee/labels/LabelRegistry;", "instructions", "Lorg/objectweb/asm/tree/InsnList;", "getInstructions", "()Lorg/objectweb/asm/tree/InsnList;", "value", "", "maxLocals", "getMaxLocals", "()I", "setMaxLocals", "(I)V", "maxStack", "getMaxStack", "setMaxStack", "getNode", "()Lorg/objectweb/asm/tree/MethodNode;", "tryCatchBlocks", "", "Lorg/objectweb/asm/tree/TryCatchBlockNode;", "getTryCatchBlocks", "()Ljava/util/List;", "koffee"})
public final class MethodAssembly
implements InstructionAssembly,
TryCatchContainer,
LabelScope,
ModifiersAccess,
TypesAccess {
    @NotNull
    private final LabelRegistry L;
    @NotNull
    private final MethodNode node;

    @Override
    @NotNull
    public InsnList getInstructions() {
        InsnList insnList = this.node.instructions;
        Intrinsics.checkExpressionValueIsNotNull(insnList, "node.instructions");
        return insnList;
    }

    @Override
    @NotNull
    public List<TryCatchBlockNode> getTryCatchBlocks() {
        List list = this.node.tryCatchBlocks;
        Intrinsics.checkExpressionValueIsNotNull(list, "node.tryCatchBlocks");
        return list;
    }

    @Override
    @NotNull
    public LabelRegistry getL() {
        return this.L;
    }

    public final int getMaxStack() {
        return this.node.maxStack;
    }

    public final void setMaxStack(int value) {
        this.node.maxStack = value;
    }

    public final int getMaxLocals() {
        return this.node.maxLocals;
    }

    public final void setMaxLocals(int value) {
        this.node.maxLocals = value;
    }

    @NotNull
    public final MethodNode getNode() {
        return this.node;
    }

    public MethodAssembly(@NotNull MethodNode node) {
        Intrinsics.checkParameterIsNotNull(node, "node");
        this.node = node;
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

