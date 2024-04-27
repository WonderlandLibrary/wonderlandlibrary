/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.gui.client.hud.element;

import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\u0018\u0000 \u000f2\u00020\u0001:\u0003\u000f\u0010\u0011B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e\u00a8\u0006\u0012"}, d2={"Lnet/dev/important/gui/client/hud/element/Side;", "", "horizontal", "Lnet/dev/important/gui/client/hud/element/Side$Horizontal;", "vertical", "Lnet/dev/important/gui/client/hud/element/Side$Vertical;", "(Lnet/dev/important/gui/client/hud/element/Side$Horizontal;Lnet/dev/important/gui/client/hud/element/Side$Vertical;)V", "getHorizontal", "()Lnet/dev/important/gui/client/hud/element/Side$Horizontal;", "setHorizontal", "(Lnet/dev/important/gui/client/hud/element/Side$Horizontal;)V", "getVertical", "()Lnet/dev/important/gui/client/hud/element/Side$Vertical;", "setVertical", "(Lnet/dev/important/gui/client/hud/element/Side$Vertical;)V", "Companion", "Horizontal", "Vertical", "LiquidBounce"})
public final class Side {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private Horizontal horizontal;
    @NotNull
    private Vertical vertical;

    public Side(@NotNull Horizontal horizontal, @NotNull Vertical vertical) {
        Intrinsics.checkNotNullParameter((Object)horizontal, "horizontal");
        Intrinsics.checkNotNullParameter((Object)vertical, "vertical");
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    @NotNull
    public final Horizontal getHorizontal() {
        return this.horizontal;
    }

    public final void setHorizontal(@NotNull Horizontal horizontal) {
        Intrinsics.checkNotNullParameter((Object)horizontal, "<set-?>");
        this.horizontal = horizontal;
    }

    @NotNull
    public final Vertical getVertical() {
        return this.vertical;
    }

    public final void setVertical(@NotNull Vertical vertical) {
        Intrinsics.checkNotNullParameter((Object)vertical, "<set-?>");
        this.vertical = vertical;
    }

    /*
     * Illegal identifiers - consider using --renameillegalidents true
     */
    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004\u00a8\u0006\u0005"}, d2={"Lnet/dev/important/gui/client/hud/element/Side$Companion;", "", "()V", "default", "Lnet/dev/important/gui/client/hud/element/Side;", "LiquidBounce"})
    public static final class Companion {
        private Companion() {
        }

        @NotNull
        public final Side default() {
            return new Side(Horizontal.LEFT, Vertical.UP);
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\b\u0086\u0001\u0018\u0000 \n2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\nB\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\t\u00a8\u0006\u000b"}, d2={"Lnet/dev/important/gui/client/hud/element/Side$Horizontal;", "", "sideName", "", "(Ljava/lang/String;ILjava/lang/String;)V", "getSideName", "()Ljava/lang/String;", "LEFT", "MIDDLE", "RIGHT", "Companion", "LiquidBounce"})
    public static final class Horizontal
    extends Enum<Horizontal> {
        @NotNull
        public static final Companion Companion;
        @NotNull
        private final String sideName;
        public static final /* enum */ Horizontal LEFT;
        public static final /* enum */ Horizontal MIDDLE;
        public static final /* enum */ Horizontal RIGHT;
        private static final /* synthetic */ Horizontal[] $VALUES;

        private Horizontal(String sideName) {
            this.sideName = sideName;
        }

        @NotNull
        public final String getSideName() {
            return this.sideName;
        }

        public static Horizontal[] values() {
            return (Horizontal[])$VALUES.clone();
        }

        public static Horizontal valueOf(String value) {
            return Enum.valueOf(Horizontal.class, value);
        }

        @JvmStatic
        @Nullable
        public static final Horizontal getByName(@NotNull String name) {
            return Companion.getByName(name);
        }

        static {
            LEFT = new Horizontal("Left");
            MIDDLE = new Horizontal("Middle");
            RIGHT = new Horizontal("Right");
            $VALUES = horizontalArray = new Horizontal[]{Horizontal.LEFT, Horizontal.MIDDLE, Horizontal.RIGHT};
            Companion = new Companion(null);
        }

        @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0007"}, d2={"Lnet/dev/important/gui/client/hud/element/Side$Horizontal$Companion;", "", "()V", "getByName", "Lnet/dev/important/gui/client/hud/element/Side$Horizontal;", "name", "", "LiquidBounce"})
        public static final class Companion {
            private Companion() {
            }

            @JvmStatic
            @Nullable
            public final Horizontal getByName(@NotNull String name) {
                Horizontal horizontal;
                block1: {
                    Intrinsics.checkNotNullParameter(name, "name");
                    for (Horizontal horizontal2 : Horizontal.values()) {
                        Horizontal it = horizontal2;
                        boolean bl = false;
                        if (!Intrinsics.areEqual(it.getSideName(), name)) continue;
                        horizontal = horizontal2;
                        break block1;
                    }
                    horizontal = null;
                }
                return horizontal;
            }

            public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
                this();
            }
        }
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\b\u0086\u0001\u0018\u0000 \n2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\nB\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\t\u00a8\u0006\u000b"}, d2={"Lnet/dev/important/gui/client/hud/element/Side$Vertical;", "", "sideName", "", "(Ljava/lang/String;ILjava/lang/String;)V", "getSideName", "()Ljava/lang/String;", "UP", "MIDDLE", "DOWN", "Companion", "LiquidBounce"})
    public static final class Vertical
    extends Enum<Vertical> {
        @NotNull
        public static final Companion Companion;
        @NotNull
        private final String sideName;
        public static final /* enum */ Vertical UP;
        public static final /* enum */ Vertical MIDDLE;
        public static final /* enum */ Vertical DOWN;
        private static final /* synthetic */ Vertical[] $VALUES;

        private Vertical(String sideName) {
            this.sideName = sideName;
        }

        @NotNull
        public final String getSideName() {
            return this.sideName;
        }

        public static Vertical[] values() {
            return (Vertical[])$VALUES.clone();
        }

        public static Vertical valueOf(String value) {
            return Enum.valueOf(Vertical.class, value);
        }

        @JvmStatic
        @Nullable
        public static final Vertical getByName(@NotNull String name) {
            return Companion.getByName(name);
        }

        static {
            UP = new Vertical("Up");
            MIDDLE = new Vertical("Middle");
            DOWN = new Vertical("Down");
            $VALUES = verticalArray = new Vertical[]{Vertical.UP, Vertical.MIDDLE, Vertical.DOWN};
            Companion = new Companion(null);
        }

        @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0007"}, d2={"Lnet/dev/important/gui/client/hud/element/Side$Vertical$Companion;", "", "()V", "getByName", "Lnet/dev/important/gui/client/hud/element/Side$Vertical;", "name", "", "LiquidBounce"})
        public static final class Companion {
            private Companion() {
            }

            @JvmStatic
            @Nullable
            public final Vertical getByName(@NotNull String name) {
                Vertical vertical;
                block1: {
                    Intrinsics.checkNotNullParameter(name, "name");
                    for (Vertical vertical2 : Vertical.values()) {
                        Vertical it = vertical2;
                        boolean bl = false;
                        if (!Intrinsics.areEqual(it.getSideName(), name)) continue;
                        vertical = vertical2;
                        break block1;
                    }
                    vertical = null;
                }
                return vertical;
            }

            public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
                this();
            }
        }
    }
}

