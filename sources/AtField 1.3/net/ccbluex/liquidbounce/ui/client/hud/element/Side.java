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

    public final void setVertical(Vertical vertical) {
        this.vertical = vertical;
    }

    public final Vertical getVertical() {
        return this.vertical;
    }

    public final void setHorizontal(Horizontal horizontal) {
        this.horizontal = horizontal;
    }

    public Side(Horizontal horizontal, Vertical vertical) {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    public final Horizontal getHorizontal() {
        return this.horizontal;
    }

    public static final class Vertical
    extends Enum {
        private final String sideName;
        public static final Companion Companion;
        public static final /* enum */ Vertical MIDDLE;
        private static final Vertical[] $VALUES;
        public static final /* enum */ Vertical UP;
        public static final /* enum */ Vertical DOWN;

        public static Vertical[] values() {
            return (Vertical[])$VALUES.clone();
        }

        static {
            Vertical[] verticalArray = new Vertical[3];
            Vertical[] verticalArray2 = verticalArray;
            verticalArray[0] = UP = new Vertical("UP", 0, "Up");
            verticalArray[1] = MIDDLE = new Vertical("MIDDLE", 1, "Middle");
            verticalArray[2] = DOWN = new Vertical("DOWN", 2, "Down");
            $VALUES = verticalArray;
            Companion = new Companion(null);
        }

        /*
         * WARNING - Possible parameter corruption
         * WARNING - void declaration
         */
        private Vertical() {
            void var3_1;
            void var2_-1;
            void var1_-1;
            this.sideName = var3_1;
        }

        public static Vertical valueOf(String string) {
            return Enum.valueOf(Vertical.class, string);
        }

        @JvmStatic
        public static final Vertical getByName(String string) {
            return Companion.getByName(string);
        }

        public final String getSideName() {
            return this.sideName;
        }

        public static final class Companion {
            @JvmStatic
            public final Vertical getByName(String string) {
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
                        Vertical vertical3 = vertical2 = verticalArray3[i];
                        boolean bl3 = false;
                        if (!vertical3.getSideName().equals(string)) continue;
                        vertical = vertical2;
                        break block1;
                    }
                    vertical = null;
                }
                return vertical;
            }

            private Companion() {
            }

            public Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }
        }
    }

    /*
     * Illegal identifiers - consider using --renameillegalidents true
     */
    public static final class Companion {
        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final Side default() {
            return new Side(Horizontal.LEFT, Vertical.UP);
        }

        private Companion() {
        }
    }

    public static final class Horizontal
    extends Enum {
        public static final /* enum */ Horizontal MIDDLE;
        public static final /* enum */ Horizontal LEFT;
        public static final /* enum */ Horizontal RIGHT;
        private static final Horizontal[] $VALUES;
        private final String sideName;
        public static final Companion Companion;

        public final String getSideName() {
            return this.sideName;
        }

        public static Horizontal[] values() {
            return (Horizontal[])$VALUES.clone();
        }

        static {
            Horizontal[] horizontalArray = new Horizontal[3];
            Horizontal[] horizontalArray2 = horizontalArray;
            horizontalArray[0] = LEFT = new Horizontal("LEFT", 0, "Left");
            horizontalArray[1] = MIDDLE = new Horizontal("MIDDLE", 1, "Middle");
            horizontalArray[2] = RIGHT = new Horizontal("RIGHT", 2, "Right");
            $VALUES = horizontalArray;
            Companion = new Companion(null);
        }

        /*
         * WARNING - Possible parameter corruption
         * WARNING - void declaration
         */
        private Horizontal() {
            void var3_1;
            void var2_-1;
            void var1_-1;
            this.sideName = var3_1;
        }

        @JvmStatic
        public static final Horizontal getByName(String string) {
            return Companion.getByName(string);
        }

        public static Horizontal valueOf(String string) {
            return Enum.valueOf(Horizontal.class, string);
        }

        public static final class Companion {
            @JvmStatic
            public final Horizontal getByName(String string) {
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
                        Horizontal horizontal3 = horizontal2 = horizontalArray3[i];
                        boolean bl3 = false;
                        if (!horizontal3.getSideName().equals(string)) continue;
                        horizontal = horizontal2;
                        break block1;
                    }
                    horizontal = null;
                }
                return horizontal;
            }

            public Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            private Companion() {
            }
        }
    }
}

