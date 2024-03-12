// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.text;

public enum CharacterPredicates implements CharacterPredicate
{
    LETTERS {
        @Override
        public boolean test(final int codePoint) {
            return Character.isLetter(codePoint);
        }
    }, 
    DIGITS {
        @Override
        public boolean test(final int codePoint) {
            return Character.isDigit(codePoint);
        }
    }, 
    ARABIC_NUMERALS {
        @Override
        public boolean test(final int codePoint) {
            return codePoint >= 48 && codePoint <= 57;
        }
    }, 
    ASCII_LOWERCASE_LETTERS {
        @Override
        public boolean test(final int codePoint) {
            return codePoint >= 97 && codePoint <= 122;
        }
    }, 
    ASCII_UPPERCASE_LETTERS {
        @Override
        public boolean test(final int codePoint) {
            return codePoint >= 65 && codePoint <= 90;
        }
    }, 
    ASCII_LETTERS {
        @Override
        public boolean test(final int codePoint) {
            return CharacterPredicates$6.ASCII_LOWERCASE_LETTERS.test(codePoint) || CharacterPredicates$6.ASCII_UPPERCASE_LETTERS.test(codePoint);
        }
    }, 
    ASCII_ALPHA_NUMERALS {
        @Override
        public boolean test(final int codePoint) {
            return CharacterPredicates$7.ASCII_LOWERCASE_LETTERS.test(codePoint) || CharacterPredicates$7.ASCII_UPPERCASE_LETTERS.test(codePoint) || CharacterPredicates$7.ARABIC_NUMERALS.test(codePoint);
        }
    };
}
