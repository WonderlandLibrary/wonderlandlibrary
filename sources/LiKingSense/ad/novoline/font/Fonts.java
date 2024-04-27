/*
 * Decompiled with CFR 0.152.
 */
package ad.novoline.font;

import ad.novoline.font.Client;
import ad.novoline.font.FontFamily;
import ad.novoline.font.FontManager;
import ad.novoline.font.FontRenderer;
import ad.novoline.font.FontType;

public interface Fonts {
    public static final String BUG = "a";
    public static final String LIST = "b";
    public static final String BOMB = "c";
    public static final String EYE = "d";
    public static final String PERSON = "e";
    public static final String WHEELCHAIR = "f";
    public static final String SCRIPT = "g";
    public static final String SKIP_LEFT = "h";
    public static final String PAUSE = "i";
    public static final String PLAY = "j";
    public static final String SKIP_RIGHT = "k";
    public static final String SHUFFLE = "l";
    public static final String INFO = "m";
    public static final String SETTINGS = "n";
    public static final String CHECKMARK = "o";
    public static final String XMARK = "p";
    public static final String TRASH = "q";
    public static final String WARNING = "r";
    public static final String FOLDER = "s";
    public static final String LOAD = "t";
    public static final String SAVE = "u";
    public static final FontManager FONT_MANAGER = Client.getFontManager();

    public static interface posterama {
        public static final FontFamily posterama = FONT_MANAGER.fontFamily(FontType.posterama);

        public static final class posterama18 {
            public static final FontRenderer posterama18 = posterama.ofSize(18);

            private posterama18() {
            }
        }

        public static final class posterama16 {
            public static final FontRenderer posterama16 = posterama.ofSize(16);

            private posterama16() {
            }
        }

        public static final class posterama20 {
            public static final FontRenderer posterama20 = posterama.ofSize(20);

            private posterama20() {
            }
        }

        public static final class posterama13 {
            public static final FontRenderer posterama13 = posterama.ofSize(13);

            private posterama13() {
            }
        }
    }

    public static interface tenacityCheck {
        public static final FontFamily tenacitycheck = FONT_MANAGER.fontFamily(FontType.tenacityCheck);

        public static final class tenacitycheck35 {
            public static final FontRenderer tenacitycheck35 = tenacitycheck.ofSize(35);

            private tenacitycheck35() {
            }
        }
    }

    public static interface CsgoIcon {
        public static final FontFamily csgoicon = FONT_MANAGER.fontFamily(FontType.csgoicon);

        public static final class csgoicon_55 {
            public static final FontRenderer csgoicon_55 = csgoicon.ofSize(55);

            private csgoicon_55() {
            }
        }

        public static final class csgoicon_40 {
            public static final FontRenderer csgoicon_40 = csgoicon.ofSize(40);

            private csgoicon_40() {
            }
        }

        public static final class csgoicon_35 {
            public static final FontRenderer csgoicon_35 = csgoicon.ofSize(35);

            private csgoicon_35() {
            }
        }

        public static final class csgoicon_32 {
            public static final FontRenderer csgoicon_32 = csgoicon.ofSize(32);

            private csgoicon_32() {
            }
        }

        public static final class csgoicon_24 {
            public static final FontRenderer csgoicon_24 = csgoicon.ofSize(24);

            private csgoicon_24() {
            }
        }

        public static final class csgoicon_20 {
            public static final FontRenderer csgoicon_20 = csgoicon.ofSize(20);

            private csgoicon_20() {
            }
        }

        public static final class csgoicon_18 {
            public static final FontRenderer csgoicon_18 = csgoicon.ofSize(18);

            private csgoicon_18() {
            }
        }
    }

    public static interface tenacityblod {
        public static final FontFamily tenacityblod = FONT_MANAGER.fontFamily(FontType.tenacityBlod);

        public static final class tenacityblod22 {
            public static final FontRenderer tenacityblod22 = tenacityblod.ofSize(22);

            private tenacityblod22() {
            }
        }
    }

    public static interface tenacity {
        public static final FontFamily tenacity = FONT_MANAGER.fontFamily(FontType.tenacity);

        public static final class tenacity22 {
            public static final FontRenderer tenacity22 = tenacity.ofSize(22);

            private tenacity22() {
            }
        }

        public static final class tenacity18 {
            public static final FontRenderer tenacity18 = tenacity.ofSize(18);

            private tenacity18() {
            }
        }
    }

    public static interface notification {
        public static final FontFamily notification = FONT_MANAGER.fontFamily(FontType.Notification);

        public static final class notification35 {
            public static final FontRenderer notification35 = notification.ofSize(35);

            private notification35() {
            }
        }
    }

    public static interface NovolineIcon {
        public static final FontFamily NovolineIcon = FONT_MANAGER.fontFamily(FontType.Novoicon);

        public static final class NovolineIcon45 {
            public static final FontRenderer NovolineIcon45 = NovolineIcon.ofSize(35);

            private NovolineIcon45() {
            }
        }

        public static final class NovolineIcon75 {
            public static final FontRenderer NovolineIcon75 = NovolineIcon.ofSize(75);

            private NovolineIcon75() {
            }
        }
    }
}

