package me.robbanrobbin.jigsaw.client.modules;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.modules.target.AuraUtils;
import me.robbanrobbin.jigsaw.client.tools.RenderTools;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.Vec3;
import net.minecraft.world.pathfinder.WalkNodeProcessor;

public class ReachAura extends Module {
	WaitTimer timerInf = new WaitTimer();
	private static EntityLivingBase en = null;
	boolean attack = false;
	public static final double maxTP = 9.9;
	private PathFinder pathFinder = new PathFinder(new WalkNodeProcessor());
	ArrayList<Vec3> positions = new ArrayList<Vec3>();
	ArrayList<Vec3> positionsBack = new ArrayList<Vec3>();

	public ReachAura() {
		super("ReachAura", Keyboard.KEY_NONE, Category.COMBAT, "Experimental stuff");
	}

	@Override
	public void onToggle() {
		this.attack = false;
		this.en = null;
		AuraUtils.targets.clear();
		super.onToggle();
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		AuraUtils.targets.clear();
		if (!AuraUtils.hasEntity(en)) {
			AuraUtils.targets.add(en);
		}
		en = Utils.getClosestEntity(Float.MAX_VALUE);
		if (en == null) {
			return;
		}
		attack = false;
		if (mc.thePlayer.getDistanceToEntity(en) >= 6) {
			if (!timerInf.hasTimeElapsed(1000 / 6, true)) {
				return;
			}
		}
		positions.clear();
		positionsBack.clear();
		if (mc.thePlayer.canEntityBeSeen(en)) {

		}
		attack = true;
		updateStages();
		event.cancel();
		super.onUpdate();
	}

	@Override
	public void onPacketSent(AbstractPacket packet) {
		super.onPacketSent(packet);
	}

	public void updateStages() {
		PathEntity path = pathFinder.createEntityPathTo(mc.theWorld, mc.thePlayer, en, 200);
		int ii = 0;
		for (int i = 0; i < path.getCurrentPathLength(); i++) {
			PathPoint point = path.getPathPointFromIndex(i);
			sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(point.xCoord + 0.5, point.yCoord, point.zCoord + 0.5, true));
			positions.add(new Vec3(point.xCoord + 0.5, point.yCoord, point.zCoord + 0.5));
			ii = i;
			i++;
		}
		PathPoint pointt = path.getPathPointFromIndex(ii);
		mc.thePlayer.swingItem();
		Criticals.disable = true;
		Criticals.crit(pointt.xCoord + 0.5, pointt.yCoord, pointt.zCoord + 0.5);
		sendPacket(new C02PacketUseEntity(en, Action.ATTACK));
		Criticals.disable = false;
		// Go back!
		for (int i = positions.size() - 2; i > -1; i--) {
			{
				double x = positions.get(i).xCoord;
				double y = positions.get(i).yCoord;
				double z = positions.get(i).zCoord;
				sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
				positionsBack.add(new Vec3(x, y, z));
			}

		}
	}

	public static boolean doBlock() {
		return en != null;
	}

	public void doReach(double range, boolean up) {

	}

	@Override
	public void onRender() {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL11.GL_BLEND);
		RenderTools.lineWidth(2);
		RenderTools.color4f(0.3f, 1f, 0.3f, 1f);
		RenderTools.glBegin(3);
		int i = 0;
		for (Vec3 vec : positions) {
			RenderTools.putVertex3d(RenderTools.getRenderPos(vec.xCoord, vec.yCoord, vec.zCoord));
			i++;
		}
		RenderTools.glEnd();
		RenderTools.color4f(0.3f, 0.3f, 1f, 1f);
		RenderTools.glBegin(3);
		i = 0;
		for (Vec3 vec : positionsBack) {
			RenderTools.putVertex3d(RenderTools.getRenderPos(vec.xCoord, vec.yCoord, vec.zCoord));
			i++;
		}
		RenderTools.glEnd();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glPopMatrix();
		RenderTools.lineWidth(3);
		for (Vec3 vec : positions) {
			drawESP(1f, 0.3f, 0.3f, 1f, vec.xCoord, vec.yCoord, vec.zCoord);
		}
		RenderTools.lineWidth(1.5f);
		for (Vec3 vec : positionsBack) {
			drawESP(0.3f, 0.3f, 1f, 1f, vec.xCoord, vec.yCoord, vec.zCoord);
		}
		super.onRender();
	}

	public void drawESP(float red, float green, float blue, float alpha, double x, double y, double z) {
		double xPos = x - mc.getRenderManager().renderPosX;
		double yPos = y - mc.getRenderManager().renderPosY;
		double zPos = z - mc.getRenderManager().renderPosZ;
		RenderTools.drawOutlinedEntityESP(xPos, yPos, zPos, mc.thePlayer.width / 2, mc.thePlayer.height, red, green,
				blue, alpha);
	}
}
