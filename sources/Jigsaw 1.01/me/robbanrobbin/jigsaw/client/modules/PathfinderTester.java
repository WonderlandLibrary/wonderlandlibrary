package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import me.robbanrobbin.jigsaw.pathfinding.FlyingNodeProcessor;
import me.robbanrobbin.jigsaw.pathfinding.JigsawPathfinder;

public class PathfinderTester extends Module {
	
	private JigsawPathfinder pathFinder = new JigsawPathfinder(mc, 500, new FlyingNodeProcessor());

	public PathfinderTester() {
		super("PathfinderTester", Keyboard.KEY_NONE, Category.AI, "Debugging. If you found this in the public version of Jigsaw, the developer forgot to remove it!!");
	}
	
	@Override
	public void onUpdate(UpdateEvent event) {
		super.onUpdate(event);
		
		
		
	}
	
//	@Override
//	public void onRender() {
//		super.onRender();
//		
//		GL11.glPushMatrix();
//		GL11.glEnable(GL11.GL_BLEND);
//		GL11.glEnable(GL11.GL_LINE_SMOOTH);
//		GL11.glDisable(GL11.GL_TEXTURE_2D);
//		GL11.glDisable(GL11.GL_DEPTH_TEST);
//		GL11.glBlendFunc(770, 771);
//		GL11.glEnable(GL11.GL_BLEND);
//		RenderTools.lineWidth(2);
//		RenderTools.color4f(0.3f, 1f, 0.3f, 1f);
//		RenderTools.glBegin(3);
//		int i = 0;
//		for (Vec3d vec : miningController_XRAY.getPositions()) {
//			RenderTools.putVertex3d(RenderTools.getRenderPos(vec.x + 0.5, vec.y, vec.z + 0.5));
//			i++;
//		}
//		RenderTools.glEnd();
//		GL11.glDisable(GL11.GL_BLEND);
//		GL11.glEnable(GL11.GL_TEXTURE_2D);
//		GL11.glDisable(GL11.GL_LINE_SMOOTH);
//		GL11.glDisable(GL11.GL_BLEND);
//		GL11.glEnable(GL11.GL_DEPTH_TEST);
//		GL11.glPopMatrix();
//		RenderTools.lineWidth(3);
//		for (Vec3d vec : miningController_XRAY.getPositions()) {
//			drawESP(1f, 0.3f, 0.3f, 1f, vec.x + 0.5, vec.y, vec.z + 0.5);
//		}
//		Vec3d vec = miningController_XRAY.getPositions().get(miningController_XRAY.getPositionIndex());
//		drawESP(1f, 1f, 1f, 1f, vec.x + 0.5, vec.y, vec.z + 0.5);
//		
//	}

}
