/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.CharacterIteration;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.DictionaryBreakEngine;
import com.ibm.icu.text.LanguageBreakEngine;
import com.ibm.icu.text.UnicodeSet;
import java.text.CharacterIterator;

final class UnhandledBreakEngine
implements LanguageBreakEngine {
    volatile UnicodeSet fHandled = new UnicodeSet();

    @Override
    public boolean handles(int c2) {
        return this.fHandled.contains(c2);
    }

    @Override
    public int findBreaks(CharacterIterator text, int startPos, int endPos, DictionaryBreakEngine.DequeI foundBreaks) {
        UnicodeSet uniset = this.fHandled;
        int c2 = CharacterIteration.current32(text);
        while (text.getIndex() < endPos && uniset.contains(c2)) {
            CharacterIteration.next32(text);
            c2 = CharacterIteration.current32(text);
        }
        return 0;
    }

    public void handleChar(int c2) {
        UnicodeSet originalSet = this.fHandled;
        if (!originalSet.contains(c2)) {
            int script = UCharacter.getIntPropertyValue(c2, 4106);
            UnicodeSet newSet = new UnicodeSet();
            newSet.applyIntPropertyValue(4106, script);
            newSet.addAll(originalSet);
            this.fHandled = newSet;
        }
    }
}

