package org.luaj.vm2.lib.custom;

import net.minecraft.item.Item;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import wtf.expensive.util.render.ColorUtil;
import wtf.expensive.util.world.InventoryUtil;

public class InventoryLib extends TwoArgFunction {
    @Override
    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaValue library = tableOf();
        library.set("getItemSlot", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                return LuaValue.valueOf(InventoryUtil.getItemSlot(Item.getItemById(arg.toint())));
            }
        });
        library.set("moveItem", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue arg, LuaValue arg1) {
                InventoryUtil.moveItem(arg.toint(), arg1.toint(), false);
                return LuaValue.valueOf(0);
            }
        });
        library.set("dropItem", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                InventoryUtil.dropItem(arg.toint());
                return LuaValue.valueOf(0F);
            }
        });
        library.set("setSlot", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                if (mc.player != null) {
                    mc.player.inventory.currentItem = arg.toint();
                }
                return LuaValue.valueOf(0F);
            }
        });
        library.set("pickupItem", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue arg, LuaValue arg2) {
                if (mc.player != null) {
                    InventoryUtil.pickupItem(arg.toint(), arg2.toint());
                }
                return LuaValue.valueOf(0F);
            }
        });
        env.set("inventory", library);
        return library;
    }

}
