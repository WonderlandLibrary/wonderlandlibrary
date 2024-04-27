package me.aquavit.liquidsense.ui.client.hud.element;

public class Side {

    private Side.Horizontal horizontal;
    private Side.Vertical vertical;

    public Side(final Side.Horizontal horizontal, final Side.Vertical vertical) {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    public final Side.Horizontal getHorizontal() {
        return this.horizontal;
    }

    public void setHorizontal(final Side.Horizontal horizontal) {
        this.horizontal = horizontal;
    }

    public Side.Vertical getVertical() {
        return this.vertical;
    }

    public void setVertical(final Side.Vertical vertical) {
        this.vertical = vertical;
    }

    public enum Horizontal {
        LEFT("Left"),
        MIDDLE("Middle"),
        RIGHT("Right");

        private final String sideName;

        private Horizontal(String sideName) {
            this.sideName = sideName;
        }

        public final String getSideName() {
            return this.sideName;
        }

        public static Side.Horizontal getByName(String name) {
            return Companion.getByName(name);
        }

        public static final class Companion {
            public static Side.Horizontal getByName(final String name) {
                for (final Horizontal horizontal : values()) {
                    if (horizontal.getSideName().equals(name)) {
                        return horizontal;
                    }
                }
                return null;
            }
        }

    }

    public enum Vertical {
        UP("Up"),
        MIDDLE("Middle"),
        DOWN("Down");

        private final String sideName;

        public final String getSideName() {
            return this.sideName;
        }

        private Vertical(String sideName) {
            this.sideName = sideName;
        }

        public static Side.Vertical getByName(String name) {
            return Companion.getByName(name);
        }

        public static final class Companion {
            public static Vertical getByName(final String name) {
                for (final Vertical vertical : values()) {
                    if (vertical.getSideName().equals(name)) {
                        return vertical;
                    }
                }
                return null;
            }
        }

    }
}
