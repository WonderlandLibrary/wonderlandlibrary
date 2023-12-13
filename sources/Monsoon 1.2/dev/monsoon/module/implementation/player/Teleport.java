package dev.monsoon.module.implementation.player;


import org.lwjgl.input.Keyboard;

import dev.monsoon.Monsoon;
import dev.monsoon.event.Event;
import dev.monsoon.event.listeners.EventRender3D;
import dev.monsoon.module.base.Module;
import dev.monsoon.module.setting.impl.ModeSetting;
import dev.monsoon.util.render.RenderUtil;
import dev.monsoon.util.misc.Timer;
import net.minecraft.util.BlockPos;
import dev.monsoon.module.enums.Category;

public class Teleport extends Module
{
    public ModeSetting mode;
    
    public Teleport() {
        super("Teleport", Keyboard.KEY_NONE, Category.MOVEMENT);
        this.mode = new ModeSetting("Mode", this, "Vanilla", new String[] { "Vanilla" });
        this.addSettings(this.mode);
    }
    
    private int x, y, z;
    
    private Timer timer = new Timer();
    
    @Override
    public void onEnable() {
    	timer.reset();
        if (this.mode.is("Vanilla")) {
        	timer.reset();
            this.setSuffix(mode.getValueName());
            x = mc.objectMouseOver.func_178782_a().getX();
            y = mc.objectMouseOver.func_178782_a().getY() + 1;
            z = mc.objectMouseOver.func_178782_a().getZ();
            BlockPos pos = new BlockPos(x, y, z);
            Monsoon.sendMessage("Teleporting.");
            mc.thePlayer.setPosition(x, y + 1, z);
        }
    }
    
    public void onEvent(Event e) {
		if(e instanceof EventRender3D) {
			BlockPos pos = new BlockPos(x, y, z);
			  RenderUtil.drawBoxFromBlockpos(pos, 1, 0, 0, 1);
		}
    }
}
