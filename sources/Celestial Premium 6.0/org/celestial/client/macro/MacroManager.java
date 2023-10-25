/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.macro;

import java.util.ArrayList;
import java.util.List;
import org.celestial.client.macro.Macro;

public class MacroManager {
    public List<Macro> macros = new ArrayList<Macro>();

    public Macro getMacroByKey(int key) {
        return this.getMacros().stream().filter(macro -> macro.getKey() == key).findFirst().orElse(null);
    }

    public List<Macro> getMacros() {
        return this.macros;
    }

    public void addMacro(Macro macro) {
        this.macros.add(macro);
    }

    public void deleteMacroByKey(int key) {
        this.macros.removeIf(macro -> macro.getKey() == key);
    }

    public void deleteMacro(Macro macro) {
        this.macros.remove(macro);
    }
}

