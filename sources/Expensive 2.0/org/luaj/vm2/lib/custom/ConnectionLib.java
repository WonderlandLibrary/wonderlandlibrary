package org.luaj.vm2.lib.custom;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.network.play.client.CPlayerTryUseItemPacket;
import net.minecraft.util.Hand;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.*;
import wtf.expensive.util.misc.HudUtil;
import wtf.expensive.util.render.ColorUtil;

import java.util.List;
import java.util.stream.Collectors;

public class ConnectionLib extends TwoArgFunction {
    @Override
    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaValue library = tableOf();
        library.set("sendPosition", new position());
        library.set("sendPositionWithRotation", new positionRotation());
        library.set("sendTryUseItem", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                if (arg.toint() == 341)
                    mc.player.connection.sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
                if (arg.toint() == 351)
                    mc.player.connection.sendPacket(new CPlayerTryUseItemPacket(Hand.OFF_HAND));
                return LuaValue.valueOf(0);
            }
        });

        library.set("sendAction", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                mc.player.connection.sendPacket(new CEntityActionPacket(mc.player, CEntityActionPacket.Action.valueOf(arg.toString())));
                return LuaValue.valueOf(0);
            }
        });
        library.set("getOnlinePlayers", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                List<LuaValue> online = mc.player.connection.getPlayerInfoMap().stream()
                        .map(NetworkPlayerInfo::getGameProfile)
                        .map(GameProfile::getName)
                        .map(l -> LuaValue.valueOf(l))
                        .collect(Collectors.toList());
                return LuaValue.listOf(online.toArray(LuaValue[]::new));
            }
        });
        env.set("network", library);
        return library;
    }

    static class position extends FourArgFunction {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3, LuaValue arg4) {
            if (mc.player != null) {
                mc.player.connection.sendPacket(new CPlayerPacket.PositionPacket(arg1.todouble(), arg2.todouble(),arg3.todouble(), arg4.toboolean()));
            }
            return LuaValue.valueOf(0);
        }
    }

    static class positionRotation extends SixArgFunction {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3, LuaValue arg4, LuaValue arg5, LuaValue arg6) {
            if (mc.player != null) {
                mc.player.connection.sendPacket(new CPlayerPacket.PositionRotationPacket(arg1.todouble(), arg2.todouble(),arg3.todouble(), arg4.tofloat(), arg5.tofloat(), arg6.toboolean()));
            }
            return LuaValue.valueOf(0);
        }
    }


}
