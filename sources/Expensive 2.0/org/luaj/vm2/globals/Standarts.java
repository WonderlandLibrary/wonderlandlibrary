package org.luaj.vm2.globals;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.*;
import org.luaj.vm2.lib.custom.*;

public class Standarts {
    public static Globals standardGlobals() {
        Globals globals = new Globals();
        globals.load(new BaseLib()); //ok
        globals.load(new Bit32Lib()); //ok
        globals.load(new MathLib()); //ok
        globals.load(new TableLib()); //ok
        globals.load(new StringLib()); //ok
        globals.load(new PlayerLib());
        globals.load(new DisplayLib());
        globals.load(new GLLib());
        globals.load(new DragLib());
        globals.load(new AuraTargetLib());
        globals.load(new ColorLib());
        globals.load(new ProjectionLib());
        globals.load(new WorldLib());
        globals.load(new MinecraftLib());
        globals.load(new ConnectionLib());
        globals.load(new SettingLib());
        globals.load(new AnimationLib());
        globals.load(new UtilsLib());
        globals.load(new InventoryLib());
        LoadState.install(globals);
        LuaC.install(globals);
        return globals;
    }
}
