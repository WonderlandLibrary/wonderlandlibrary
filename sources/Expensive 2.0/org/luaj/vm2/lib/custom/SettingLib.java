package org.luaj.vm2.lib.custom;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.*;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.settings.Setting;
import wtf.expensive.modules.settings.imp.BooleanOption;
import wtf.expensive.modules.settings.imp.ModeSetting;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.util.font.Fonts;
import wtf.expensive.util.render.ColorUtil;
import wtf.expensive.util.render.RenderUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingLib extends TwoArgFunction {
    @Override
    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaValue library = tableOf();
        library.set("addBool", new addBoolean());
        library.set("addNumber", new addSlider());
        library.set("addMode", new addMode());
        library.set("get", new get());
        env.set("setting", library);
        return library;
    }

    static class addBoolean extends ThreeArgFunction {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
            Function function = (Function) arg1.checkuserdata();
            function.addSettings(new BooleanOption(arg2.toString(), arg3.toboolean()));
            return LuaValue.valueOf(0);
        }
    }

    static class addSlider extends SixArgFunction {

        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3, LuaValue arg4, LuaValue arg5, LuaValue arg6) {
            Function function = (Function) arg1.checkuserdata();
            function.addSettings(new SliderSetting(arg2.toString(), arg3.tofloat(), arg4.tofloat(),arg5.tofloat(), arg6.tofloat()));
            return LuaValue.valueOf(0);
        }

    }

    static class addMode extends FourArgFunction {

        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3, LuaValue arg4) {
            Function function = (Function) arg1.checkuserdata();
            System.out.println(arg4);
            LuaTable table = (LuaTable) arg4;
            List<String> val = new ArrayList<>();
            int ind = 1;
            while (true) {
                LuaValue value = table.get(ind);
                if (value != NIL) {
                    val.add(value.toString());
                } else {
                    break;
                }
                ind++;
            }

            function.addSettings(new ModeSetting(arg2.toString(), arg3.toString(), val.toArray(String[]::new)));
            return LuaValue.valueOf(0);
        }

    }

    static class get extends TwoArgFunction {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            Function function = (Function) arg1.checkuserdata();
            for (Setting setting : function.settingList) {
                if (setting.getName().equalsIgnoreCase(arg2.toString())) {
                    if (setting instanceof BooleanOption b) {
                        return LuaValue.valueOf(b.get());
                    }
                    if (setting instanceof SliderSetting b) {
                        return LuaValue.valueOf(b.getValue().floatValue());
                    }
                    if (setting instanceof ModeSetting b) {
                        return LuaValue.valueOf(b.get());
                    }
                }
            }
            return LuaValue.valueOf(0);
        }

    }

}
