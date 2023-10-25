package fun.rich.client.command.macro;

import java.util.ArrayList;
import java.util.List;

public class MacroManager {

    public List<Macro> macros = new ArrayList<>();

    public List<Macro> getMacros() {
        return this.macros;
    }

    public void addMacro(Macro macro) {
        this.macros.add(macro);
    }

    public void deleteMacroByKey(int key) {
        this.macros.removeIf(macro -> macro.getKey() == key);
    }

}
