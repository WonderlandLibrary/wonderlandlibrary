/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.JvmStatic
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.ui.client.hud.element;

import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\u0018\u0000 \u000f2\u00020\u0001:\u0003\u000f\u0010\u0011B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e\u00a8\u0006\u0012"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/Side;", "", "horizontal", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Side$Horizontal;", "vertical", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Side$Vertical;", "(Lnet/ccbluex/liquidbounce/ui/client/hud/element/Side$Horizontal;Lnet/ccbluex/liquidbounce/ui/client/hud/element/Side$Vertical;)V", "getHorizontal", "()Lnet/ccbluex/liquidbounce/ui/client/hud/element/Side$Horizontal;", "setHorizontal", "(Lnet/ccbluex/liquidbounce/ui/client/hud/element/Side$Horizontal;)V", "getVertical", "()Lnet/ccbluex/liquidbounce/ui/client/hud/element/Side$Vertical;", "setVertical", "(Lnet/ccbluex/liquidbounce/ui/client/hud/element/Side$Vertical;)V", "Companion", "Horizontal", "Vertical", "LiKingSense"})
public final class Side {
    @NotNull
    private Horizontal horizontal;
    @NotNull
    private Vertical vertical;
    public static final Companion Companion = new Companion(null);

    @NotNull
    public final Horizontal getHorizontal() {
        return this.horizontal;
    }

    public final void setHorizontal(@NotNull Horizontal horizontal) {
        Intrinsics.checkParameterIsNotNull((Object)((Object)horizontal), (String)"<set-?>");
        this.horizontal = horizontal;
    }

    @NotNull
    public final Vertical getVertical() {
        return this.vertical;
    }

    public final void setVertical(@NotNull Vertical vertical) {
        Intrinsics.checkParameterIsNotNull((Object)((Object)vertical), (String)"<set-?>");
        this.vertical = vertical;
    }

    public Side(@NotNull Horizontal horizontal, @NotNull Vertical vertical) {
        Intrinsics.checkParameterIsNotNull((Object)((Object)horizontal), (String)"horizontal");
        Intrinsics.checkParameterIsNotNull((Object)((Object)vertical), (String)"vertical");
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\b\u0086\u0001\u0018\u0000 \n2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\nB\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\t\u00a8\u0006\u000b"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/Side$Horizontal;", "", "sideName", "", "(Ljava/lang/String;ILjava/lang/String;)V", "getSideName", "()Ljava/lang/String;", "LEFT", "MIDDLE", "RIGHT", "Companion", "LiKingSense"})
    public static final class Horizontal
    extends Enum<Horizontal> {
        public static final /* enum */ Horizontal LEFT;
        public static final /* enum */ Horizontal MIDDLE;
        public static final /* enum */ Horizontal RIGHT;
        private static final /* synthetic */ Horizontal[] $VALUES;
        @NotNull
        private final String sideName;
        public static final Companion Companion;

        static {
            Horizontal[] horizontalArray = new Horizontal[3];
            Horizontal[] horizontalArray2 = horizontalArray;
            horizontalArray[0] = LEFT = new Horizontal("Left");
            horizontalArray[1] = MIDDLE = new Horizontal("Middle");
            horizontalArray[2] = RIGHT = new Horizontal("Right");
            $VALUES = horizontalArray;
            Companion = new Companion(null);
        }

        @NotNull
        public final String getSideName() {
            return this.sideName;
        }

        private Horizontal(String sideName) {
            this.sideName = sideName;
        }

        public static Horizontal[] values() {
            return (Horizontal[])$VALUES.clone();
        }

        public static Horizontal valueOf(String string) {
            return Enum.valueOf(Horizontal.class, string);
        }

        @JvmStatic
        @Nullable
        public static final Horizontal getByName(@NotNull String name) {
            return Companion.getByName(name);
        }

        @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0007"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/Side$Horizontal$Companion;", "", "()V", "getByName", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Side$Horizontal;", "name", "", "LiKingSense"})
        public static final class Companion {
            @JvmStatic
            @Nullable
            public final Horizontal getByName(@NotNull String name) {
                Horizontal horizontal;
                block1: {
                    Intrinsics.checkParameterIsNotNull((Object)name, (String)"name");
                    Horizontal[] horizontalArray = Horizontal.values();
                    boolean bl = false;
                    Horizontal[] horizontalArray2 = horizontalArray;
                    boolean bl2 = false;
                    Horizontal[] horizontalArray3 = horizontalArray2;
                    int n = horizontalArray3.length;
                    for (int i = 0; i < n; ++i) {
                        Horizontal horizontal2;
                        Horizontal it = horizontal2 = horizontalArray3[i];
                        boolean bl3 = false;
                        if (!Intrinsics.areEqual((Object)it.getSideName(), (Object)name)) continue;
                        horizontal = horizontal2;
                        break block1;
                    }
                    horizontal = null;
                }
                return horizontal;
            }

            private Companion() {
            }

            public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
                this();
            }
        }
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\b\u0086\u0001\u0018\u0000 \n2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\nB\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\t\u00a8\u0006\u000b"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/Side$Vertical;", "", "sideName", "", "(Ljava/lang/String;ILjava/lang/String;)V", "getSideName", "()Ljava/lang/String;", "UP", "MIDDLE", "DOWN", "Companion", "LiKingSense"})
    public static final class Vertical
    extends Enum<Vertical> {
        public static final /* enum */ Vertical UP;
        public static final /* enum */ Vertical MIDDLE;
        public static final /* enum */ Vertical DOWN;
        private static final /* synthetic */ Vertical[] $VALUES;
        @NotNull
        private final String sideName;
        public static final Companion Companion;

        static {
            Vertical[] verticalArray = new Vertical[3];
            Vertical[] verticalArray2 = verticalArray;
            verticalArray[0] = UP = new Vertical("Up");
            verticalArray[1] = MIDDLE = new Vertical("Middle");
            verticalArray[2] = DOWN = new Vertical("Down");
            $VALUES = verticalArray;
            Companion = new Companion(null);
        }

        @NotNull
        public final String getSideName() {
            return this.sideName;
        }

        private Vertical(String sideName) {
            this.sideName = sideName;
        }

        public static Vertical[] values() {
            return (Vertical[])$VALUES.clone();
        }

        public static Vertical valueOf(String string) {
            return Enum.valueOf(Vertical.class, string);
        }

        @JvmStatic
        @Nullable
        public static final Vertical getByName(@NotNull String name) {
            return Companion.getByName(name);
        }

        @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0007"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/Side$Vertical$Companion;", "", "()V", "getByName", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Side$Vertical;", "name", "", "LiKingSense"})
        public static final class Companion {
            @JvmStatic
            @Nullable
            public final Vertical getByName(@NotNull String name) {
                Vertical vertical;
                block1: {
                    Intrinsics.checkParameterIsNotNull((Object)name, (String)"name");
                    Vertical[] verticalArray = Vertical.values();
                    boolean bl = false;
                    Vertical[] verticalArray2 = verticalArray;
                    boolean bl2 = false;
                    Vertical[] verticalArray3 = verticalArray2;
                    int n = verticalArray3.length;
                    for (int i = 0; i < n; ++i) {
                        Vertical vertical2;
                        Vertical it = vertical2 = verticalArray3[i];
                        boolean bl3 = false;
                        if (!Intrinsics.areEqual((Object)it.getSideName(), (Object)name)) continue;
                        vertical = vertical2;
                        break block1;
                    }
                    vertical = null;
                }
                return vertical;
            }

            private Companion() {
            }

            public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
                this();
            }
        }
    }

    /*
     * Illegal identifiers - consider using --renameillegalidents true
     */
    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004\u00a8\u0006\u0005"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/Side$Companion;", "", "()V", "default", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Side;", "LiKingSense"})
    public static final class Companion {
        @NotNull
        public final Side default() {
            return new Side(Horizontal.LEFT, Vertical.UP);
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

