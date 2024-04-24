package me.crimp.claudius.mod.modules.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.crimp.claudius.claudius;
import me.crimp.claudius.event.events.Render2DEvent;
import me.crimp.claudius.mod.modules.Module;
import me.crimp.claudius.mod.modules.text.PopCounter;
import me.crimp.claudius.mod.setting.Setting;
import me.crimp.claudius.utils.ColorUtil;
import net.minecraft.entity.player.EntityPlayer;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class PlayerRadar extends Module {
    private final Setting<Integer> amount = register(new Setting<>("PlayerCount", 10, 1, 100));
    private final Setting<Boolean> totemPops = register(new Setting<>("totempops", true));
    public Setting<Integer> X = register(new Setting<>("X", 10, 0, 950));
    public Setting<Integer> Y = register(new Setting<>("Y", 10, 0, 550));

    public PlayerRadar() {
        super("PlayerRadar", "Shows players in render distance on hud", Category.Render, false, false);
    }

    public static PlayerRadar INSTANCE = new PlayerRadar();

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, RoundingMode.HALF_UP);
        return bd.floatValue();
    }


    @Override
    public void onRender2D(Render2DEvent event) {
        int i = 0;
        for (Object o : mc.world.loadedEntityList) {
            if (o instanceof EntityPlayer && o != mc.player) {
                i++;
                if (i > amount.getValue()) return;
                EntityPlayer entity = (EntityPlayer) o;
                float health = round(entity.getHealth() + entity.getAbsorptionAmount(), 1);

                String heal;
                String health_str;

                health_str = health + "";

                health_str = health_str.replace(".0", "");

                if (health >= 12.0) {
                    heal = " " + ChatFormatting.GREEN + health_str + "";
                } else if (health >= 4.0) {
                    heal = " " + ChatFormatting.YELLOW + health_str + "";
                } else {
                    heal = " " + ChatFormatting.RED + health_str + "";
                }


                String name = entity.getGameProfile().getName();
                String popStr = "";
                String str = heal + " " + ChatFormatting.RESET;

                if (totemPops.getValue()) {
                    popStr = " " + PopCounter.INSTANCE.getTotemPopString(entity);
                }

                if (claudius.friendManager.isFriend(entity.getName())) {
                    claudius.textManager.drawString( str + ChatFormatting.AQUA + name + ChatFormatting.YELLOW + popStr, X.getValue() + 5, Y.getValue() + i * 10, ColorUtil.toRGBA(255, 255, 255, 255), false);
                } else if (claudius.enemyManager.isEnemy(entity.getName())) {
                    claudius.textManager.drawString(str + ChatFormatting.RED + name + ChatFormatting.YELLOW + popStr, X.getValue() + 5, Y.getValue() + i * 10, ColorUtil.toRGBA(255, 255, 255, 255), false);
                } else {
                    claudius.textManager.drawString(str + name + ChatFormatting.YELLOW + popStr, X.getValue() + 5, Y.getValue() + i * 10, ColorUtil.toRGBA(255, 255, 255, 255), false);
                }
            }
        }
    }
}


