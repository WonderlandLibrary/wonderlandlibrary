/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.JvmStatic
 *  kotlin.jvm.internal.DefaultConstructorMarker
 */
package net.ccbluex.liquidbounce.ui.client.hud.element;

import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;

public final class Side {
    private Horizontal horizontal;
    private Vertical vertical;
    public static final Companion Companion = new Companion(null);

    public final Horizontal getHorizontal() {
        return this.horizontal;
    }

    public final void setHorizontal(Horizontal horizontal) {
        this.horizontal = horizontal;
    }

    public final Vertical getVertical() {
        return this.vertical;
    }

    public final void setVertical(Vertical vertical) {
        this.vertical = vertical;
    }

    public Side(Horizontal horizontal, Vertical vertical) {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    public static final class Horizontal
    extends Enum<Horizontal> {
        public static final /* enum */ Horizontal LEFT;
        public static final /* enum */ Horizontal MIDDLE;
        public static final /* enum */ Horizontal RIGHT;
        private static final /* synthetic */ Horizontal[] $VALUES;
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
        public static final Horizontal getByName(String name) {
            return Companion.getByName(name);
        }

        public static final class Companion {
            @JvmStatic
            public final Horizontal getByName(String name) {
                Horizontal horizontal;
                block1: {
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
                        if (!it.getSideName().equals(name)) continue;
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

    public static final class Vertical
    extends Enum<Vertical> {
        public static final /* enum */ Vertical UP;
        public static final /* enum */ Vertical MIDDLE;
        public static final /* enum */ Vertical DOWN;
        private static final /* synthetic */ Vertical[] $VALUES;
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
        public static final Vertical getByName(String name) {
            return Companion.getByName(name);
        }

        public static final class Companion {
            @JvmStatic
            public final Vertical getByName(String name) {
                Vertical vertical;
                block1: {
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
                        if (!it.getSideName().equals(name)) continue;
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
    public static final class Companion {
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

