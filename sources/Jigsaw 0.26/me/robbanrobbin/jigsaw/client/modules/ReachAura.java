package me.robbanrobbin.jigsaw.client.modules;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.modules.target.AuraUtils;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.RenderTools;
import me.robbanrobbin.jigsaw.client.tools.TeleportResult;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ModSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.SliderSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ValueFormat;
import me.robbanrobbin.jigsaw.module.Module;
import me.robbanrobbin.jigsaw.pathfinding.Node;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.util.Vec3;
import net.minecraft.world.pathfinder.WalkNodeProcessor;

public class ReachAura extends Module {
	WaitTimer timerInf = new WaitTimer();
	private static EntityLivingBase en = null;
	boolean attack = false;
	public static final double maxTP = 9.9;
	ArrayList<Vec3> positions = new ArrayList<Vec3>();
	ArrayList<Vec3> positionsBack = new ArrayList<Vec3>();
	ArrayList<Node> triedPaths = new ArrayList<Node>();
	
	@Override
	public ModSetting[] getModSettings() {
		SliderSetting slider2 = new SliderSetting("Hit Range", "NewTpAuraRange", 6.0, 300.0, 0.0, ValueFormat.DECIMAL);
		SliderSetting slider3 = new SliderSetting("Hits Per Second", "NewTpAuraAPS", 1.0, 20.0, 0.0, ValueFormat.DECIMAL);
		
		return new ModSetting[]{slider2, slider3};
	}

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
		en = Utils.getClosestEntity((float) ClientSettings.NewTpAuraRange);
		if (en == null) {
			return;
		}
		attack = false;
		if (!timerInf.hasTimeElapsed((long) (1000 / ClientSettings.NewTpAuraAPS), true)) {
			return;
		}
		positions.clear();
		positionsBack.clear();
		attack = true;
		updateStages();
		//event.cancel();
		super.onUpdate();
	}

	@Override
	public void onPacketSent(AbstractPacket packet) {
		super.onPacketSent(packet);
	}

	public void updateStages() {
		
		for(EntityLivingBase en : Utils.getClosestEntities((float) ClientSettings.NewTpAuraRange)) {
			positions.clear();
			positionsBack.clear();
			triedPaths.clear();
//			Jigsaw.chatMessage(en.posX);
//			Jigsaw.chatMessage(en.posY);
//			Jigsaw.chatMessage(en.posZ);
//			System.out.println(en.posX + "," + en.posY + "," + en.posZ);
			TeleportResult result = Utils.pathFinderTeleportTo(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), new Vec3(en.posX, en.posY, en.posZ));
			
			triedPaths = result.triedPaths;
			
			if(!result.foundPath) {
				continue;
			}
			
			Vec3 lastPos = result.lastPos;

			mc.thePlayer.swingItem();
			Criticals.disable = true;
			Criticals.crit(lastPos.xCoord, lastPos.yCoord, lastPos.zCoord);
			sendPacket(new C02PacketUseEntity(en, Action.ATTACK));
			Criticals.disable = false;
			
			positions = result.positions;
			
			TeleportResult resultBack = Utils.pathFinderTeleportBack(positions);
			
			positionsBack = resultBack.positionsBack;
		}
		
		
		
	}

	public static boolean doBlock() {
		return en != null;
	}

	@Override
	public void onRender() {
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
//		for (Vec3 vec : positions) {
//			RenderTools.putVertex3d(RenderTools.getRenderPos(vec.xCoord, vec.yCoord, vec.zCoord));
//			i++;
//		}
//		RenderTools.glEnd();
//		RenderTools.color4f(0.3f, 1f, 1f, 1f);
//		RenderTools.lineWidth(4);
//		for (Node n : triedPaths) {
//			Vec3 vec = RenderTools.getRenderPos(n.getBlockpos().getX(), n.getBlockpos().getY(), n.getBlockpos().getZ());
//			RenderTools.drawBlockESP(vec.xCoord, vec.yCoord, vec.zCoord, 1f, 1f, 0.3f, 0.15f, 1f, 1f, 1f, 0.15f, 1f);
//			i++;
//		}
//		RenderTools.color4f(0.3f, 0.3f, 1f, 1f);
//		RenderTools.lineWidth(2);
//		RenderTools.glBegin(3);
//		i = 0;
//		for (Vec3 vec : positionsBack) {
//			RenderTools.putVertex3d(RenderTools.getRenderPos(vec.xCoord, vec.yCoord, vec.zCoord));
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
//		for (Vec3 vec : positions) {
//			drawESP(1f, 0.3f, 0.3f, 1f, vec.xCoord, vec.yCoord, vec.zCoord);
//		}
//		RenderTools.lineWidth(1.5f);
//		for (Vec3 vec : positionsBack) {
//			drawESP(0.3f, 0.3f, 1f, 1f, vec.xCoord, vec.yCoord, vec.zCoord);
//		}
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
