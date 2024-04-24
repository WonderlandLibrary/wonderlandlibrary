package me.crimp.claudius.mod.modules.misc;

import me.crimp.claudius.claudius;
import me.crimp.claudius.mod.modules.Module;
import me.crimp.claudius.mod.setting.Bind;
import me.crimp.claudius.mod.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.input.Keyboard;

public class ClickEnemy extends Module {
    private boolean clicked = false;
    public Setting<Bind> Keybind = register(new Setting<>("EnemyBind", new Bind(Keyboard.KEY_B)));

    public ClickEnemy() {
        super("ClickEnemy", "Like Mcf But With A Different Bind", Category.Misc, false, false);
    }

    @Override
    public void onUpdate() {
        if (Keyboard.isKeyDown(Keybind.getValue().getKey())) {
            if (!clicked && McfSync.mc.currentScreen == null) {
                onClick();
            }
            clicked = true;
        } else {
            clicked = false;
        }
    }

    private void onClick() {
        Entity entity;
        RayTraceResult result = McfSync.mc.objectMouseOver;
        if (result != null && result.typeOfHit == RayTraceResult.Type.ENTITY && (entity = result.entityHit) instanceof EntityPlayer) {
            if (claudius.enemyManager.isEnemy(entity.getName())) {
                claudius.enemyManager.removeEnemy(entity.getName());
            } else if (!claudius.enemyManager.isEnemy(entity.getName())) {
                claudius.enemyManager.addEnemy(entity.getName());
            }
        }
    }
}
