/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package codes.som.anthony.koffee.sugar;

import codes.som.anthony.koffee.modifiers.ModifiersKt;
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
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000\u00d2\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001R\u0014\u0010\u0002\u001a\u00020\u00038VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0014\u0010\u0006\u001a\u00020\u00078VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0014\u0010\n\u001a\u00020\u000b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\rR\u0014\u0010\u000e\u001a\u00020\u000f8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u0014\u0010\u0012\u001a\u00020\u00138VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015R\u0014\u0010\u0016\u001a\u00020\u00178VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0018\u0010\u0019R\u0014\u0010\u001a\u001a\u00020\u001b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001c\u0010\u001dR\u0014\u0010\u001e\u001a\u00020\u001f8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b \u0010!R\u0014\u0010\"\u001a\u00020\u00038VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b#\u0010\u0005R\u0014\u0010$\u001a\u00020%8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b&\u0010'R\u0014\u0010(\u001a\u00020)8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b*\u0010+R\u0014\u0010,\u001a\u00020-8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b.\u0010/R\u0014\u00100\u001a\u0002018VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b2\u00103R\u0014\u00104\u001a\u0002058VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b6\u00107R\u0014\u00108\u001a\u0002098VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b:\u0010;R\u0014\u0010<\u001a\u00020=8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b>\u0010?R\u0014\u0010@\u001a\u00020A8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bB\u0010CR\u0014\u0010D\u001a\u00020E8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bF\u0010GR\u0014\u0010H\u001a\u00020I8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bJ\u0010KR\u0014\u0010L\u001a\u00020M8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bN\u0010OR\u0014\u0010P\u001a\u00020\u00078VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bQ\u0010\tR\u0014\u0010R\u001a\u00020S8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bT\u0010UR\u0014\u0010V\u001a\u00020W8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bX\u0010YR\u0014\u0010Z\u001a\u00020[8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\\\u0010]R\u0014\u0010^\u001a\u00020_8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b`\u0010aR\u0014\u0010b\u001a\u00020c8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bd\u0010eR\u0014\u0010f\u001a\u00020g8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bh\u0010i\u00a8\u0006j"}, d2={"Lcodes/som/anthony/koffee/sugar/ModifiersAccess;", "", "_interface", "Lcodes/som/anthony/koffee/modifiers/interface;", "get_interface", "()Lcodes/som/anthony/koffee/modifiers/interface;", "_super", "Lcodes/som/anthony/koffee/modifiers/super;", "get_super", "()Lcodes/som/anthony/koffee/modifiers/super;", "abstract", "Lcodes/som/anthony/koffee/modifiers/abstract;", "getAbstract", "()Lcodes/som/anthony/koffee/modifiers/abstract;", "annotation", "Lcodes/som/anthony/koffee/modifiers/annotation;", "getAnnotation", "()Lcodes/som/anthony/koffee/modifiers/annotation;", "bridge", "Lcodes/som/anthony/koffee/modifiers/bridge;", "getBridge", "()Lcodes/som/anthony/koffee/modifiers/bridge;", "deprecated", "Lcodes/som/anthony/koffee/modifiers/deprecated;", "getDeprecated", "()Lcodes/som/anthony/koffee/modifiers/deprecated;", "enum", "Lcodes/som/anthony/koffee/modifiers/enum;", "getEnum", "()Lcodes/som/anthony/koffee/modifiers/enum;", "final", "Lcodes/som/anthony/koffee/modifiers/final;", "getFinal", "()Lcodes/som/anthony/koffee/modifiers/final;", "interface", "getInterface", "mandated", "Lcodes/som/anthony/koffee/modifiers/mandated;", "getMandated", "()Lcodes/som/anthony/koffee/modifiers/mandated;", "module", "Lcodes/som/anthony/koffee/modifiers/module;", "getModule", "()Lcodes/som/anthony/koffee/modifiers/module;", "native", "Lcodes/som/anthony/koffee/modifiers/native;", "getNative", "()Lcodes/som/anthony/koffee/modifiers/native;", "open", "Lcodes/som/anthony/koffee/modifiers/open;", "getOpen", "()Lcodes/som/anthony/koffee/modifiers/open;", "package_private", "Lcodes/som/anthony/koffee/modifiers/package_private;", "getPackage_private", "()Lcodes/som/anthony/koffee/modifiers/package_private;", "private", "Lcodes/som/anthony/koffee/modifiers/private;", "getPrivate", "()Lcodes/som/anthony/koffee/modifiers/private;", "protected", "Lcodes/som/anthony/koffee/modifiers/protected;", "getProtected", "()Lcodes/som/anthony/koffee/modifiers/protected;", "public", "Lcodes/som/anthony/koffee/modifiers/public;", "getPublic", "()Lcodes/som/anthony/koffee/modifiers/public;", "static", "Lcodes/som/anthony/koffee/modifiers/static;", "getStatic", "()Lcodes/som/anthony/koffee/modifiers/static;", "static_phase", "Lcodes/som/anthony/koffee/modifiers/static_phase;", "getStatic_phase", "()Lcodes/som/anthony/koffee/modifiers/static_phase;", "strict", "Lcodes/som/anthony/koffee/modifiers/strict;", "getStrict", "()Lcodes/som/anthony/koffee/modifiers/strict;", "super", "getSuper", "synchronized", "Lcodes/som/anthony/koffee/modifiers/synchronized;", "getSynchronized", "()Lcodes/som/anthony/koffee/modifiers/synchronized;", "synthetic", "Lcodes/som/anthony/koffee/modifiers/synthetic;", "getSynthetic", "()Lcodes/som/anthony/koffee/modifiers/synthetic;", "transient", "Lcodes/som/anthony/koffee/modifiers/transient;", "getTransient", "()Lcodes/som/anthony/koffee/modifiers/transient;", "transitive", "Lcodes/som/anthony/koffee/modifiers/transitive;", "getTransitive", "()Lcodes/som/anthony/koffee/modifiers/transitive;", "varargs", "Lcodes/som/anthony/koffee/modifiers/varargs;", "getVarargs", "()Lcodes/som/anthony/koffee/modifiers/varargs;", "volatile", "Lcodes/som/anthony/koffee/modifiers/volatile;", "getVolatile", "()Lcodes/som/anthony/koffee/modifiers/volatile;", "koffee"})
public interface ModifiersAccess {
    @NotNull
    public package_private getPackage_private();

    @NotNull
    public public getPublic();

    @NotNull
    public private getPrivate();

    @NotNull
    public protected getProtected();

    @NotNull
    public static getStatic();

    @NotNull
    public final getFinal();

    @NotNull
    public super getSuper();

    @NotNull
    public super get_super();

    @NotNull
    public synchronized getSynchronized();

    @NotNull
    public open getOpen();

    @NotNull
    public transitive getTransitive();

    @NotNull
    public volatile getVolatile();

    @NotNull
    public bridge getBridge();

    @NotNull
    public static_phase getStatic_phase();

    @NotNull
    public varargs getVarargs();

    @NotNull
    public transient getTransient();

    @NotNull
    public native getNative();

    @NotNull
    public interface getInterface();

    @NotNull
    public interface get_interface();

    @NotNull
    public abstract getAbstract();

    @NotNull
    public strict getStrict();

    @NotNull
    public synthetic getSynthetic();

    @NotNull
    public annotation getAnnotation();

    @NotNull
    public enum getEnum();

    @NotNull
    public mandated getMandated();

    @NotNull
    public module getModule();

    @NotNull
    public deprecated getDeprecated();

    @Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=3)
    public static final class DefaultImpls {
        @NotNull
        public static package_private getPackage_private(ModifiersAccess $this) {
            return package_private.INSTANCE;
        }

        @NotNull
        public static public getPublic(ModifiersAccess $this) {
            return public.INSTANCE;
        }

        @NotNull
        public static private getPrivate(ModifiersAccess $this) {
            return private.INSTANCE;
        }

        @NotNull
        public static protected getProtected(ModifiersAccess $this) {
            return protected.INSTANCE;
        }

        @NotNull
        public static static getStatic(ModifiersAccess $this) {
            return static.INSTANCE;
        }

        @NotNull
        public static final getFinal(ModifiersAccess $this) {
            return final.INSTANCE;
        }

        @NotNull
        public static super getSuper(ModifiersAccess $this) {
            return super.INSTANCE;
        }

        @NotNull
        public static super get_super(ModifiersAccess $this) {
            return ModifiersKt.get_super();
        }

        @NotNull
        public static synchronized getSynchronized(ModifiersAccess $this) {
            return synchronized.INSTANCE;
        }

        @NotNull
        public static open getOpen(ModifiersAccess $this) {
            return open.INSTANCE;
        }

        @NotNull
        public static transitive getTransitive(ModifiersAccess $this) {
            return transitive.INSTANCE;
        }

        @NotNull
        public static volatile getVolatile(ModifiersAccess $this) {
            return volatile.INSTANCE;
        }

        @NotNull
        public static bridge getBridge(ModifiersAccess $this) {
            return bridge.INSTANCE;
        }

        @NotNull
        public static static_phase getStatic_phase(ModifiersAccess $this) {
            return static_phase.INSTANCE;
        }

        @NotNull
        public static varargs getVarargs(ModifiersAccess $this) {
            return varargs.INSTANCE;
        }

        @NotNull
        public static transient getTransient(ModifiersAccess $this) {
            return transient.INSTANCE;
        }

        @NotNull
        public static native getNative(ModifiersAccess $this) {
            return native.INSTANCE;
        }

        @NotNull
        public static interface getInterface(ModifiersAccess $this) {
            return interface.INSTANCE;
        }

        @NotNull
        public static interface get_interface(ModifiersAccess $this) {
            return ModifiersKt.get_interface();
        }

        @NotNull
        public static abstract getAbstract(ModifiersAccess $this) {
            return abstract.INSTANCE;
        }

        @NotNull
        public static strict getStrict(ModifiersAccess $this) {
            return strict.INSTANCE;
        }

        @NotNull
        public static synthetic getSynthetic(ModifiersAccess $this) {
            return synthetic.INSTANCE;
        }

        @NotNull
        public static annotation getAnnotation(ModifiersAccess $this) {
            return annotation.INSTANCE;
        }

        @NotNull
        public static enum getEnum(ModifiersAccess $this) {
            return enum.INSTANCE;
        }

        @NotNull
        public static mandated getMandated(ModifiersAccess $this) {
            return mandated.INSTANCE;
        }

        @NotNull
        public static module getModule(ModifiersAccess $this) {
            return module.INSTANCE;
        }

        @NotNull
        public static deprecated getDeprecated(ModifiersAccess $this) {
            return deprecated.INSTANCE;
        }
    }
}

