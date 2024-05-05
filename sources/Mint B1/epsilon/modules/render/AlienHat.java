package epsilon.modules.render;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

import epsilon.events.Event;
import epsilon.events.listeners.render.EventRender3d;
import epsilon.modules.Module;
import epsilon.util.EpsilonColorUtils;
import epsilon.util.ShapeUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

public class AlienHat extends Module {

	private final static ShapeUtils render = new ShapeUtils();
	
	public AlienHat() {
		super("AlienHat", Keyboard.KEY_NONE, Category.RENDER, "Draws a silly hat");
	}
	
	
}
