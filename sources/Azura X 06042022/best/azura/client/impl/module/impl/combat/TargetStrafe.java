package best.azura.client.impl.module.impl.combat;


import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMove;
import best.azura.client.impl.events.EventRender3D;
import best.azura.client.impl.events.EventUpdate;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.impl.module.impl.movement.Flight;
import best.azura.client.impl.module.impl.movement.LongJump;
import best.azura.client.impl.module.impl.movement.Speed;
import best.azura.client.util.color.HSBColor;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.player.RotationUtil;
import best.azura.client.util.render.RenderUtil;
import best.azura.client.impl.value.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

@ModuleInfo(name = "Target Strafe", description = "Strafe around the KillAura target.", category = Category.COMBAT)
public class TargetStrafe extends Module {

	private final static BooleanValue renderLine = new BooleanValue("Render Circle", "Render a circle to where you strafe", true);
	private final static NumberValue<Float> smoothVal = new NumberValue<>("Polygons", "Amount of polygons", () -> renderLine.getObject() || TargetStrafe.shape.getObject(), 14F, 1F, 3F, 25F);
	private final static ColorValue colorVal = new ColorValue("Color", "Color of the circle", renderLine::getObject, HSBColor.fromColor(new Color(187, 255, 119, 255)));
	public final static NumberValue<Float> lineWidth = new NumberValue<>("Line width", "Line width", renderLine::getObject, 3.0F, 0.25F, 2F, 10F);
	private final static BooleanValue outline = new BooleanValue("Outline Circle", "Render a outline of the circle", renderLine::getObject, false);
	public final static NumberValue<Float> rangeVal = new NumberValue<>("Range", "Range of the target strafe", 3.0F, 0.1F, 0.0F, 6.0F);
	public final static BooleanValue onJumpKey = new BooleanValue("On Jump", "Only strafe while jumping", false);
	public final static BooleanValue behindValue = new BooleanValue("Behind", "Stay behind the target", false);
	public final static BooleanValue shape = new BooleanValue("Shape", "Strafe in the configured rendered shape", true);
	private final static ComboValue moduleCombo = new ComboValue("Modules", "Modules to target strafe with",
			new ComboSelection("Speed", true), new ComboSelection("Flight", true),
			new ComboSelection("Long Jump", true), new ComboSelection("Space", false));
	private static boolean strafeDirection = false, isStrafing = false;
	public static float strafeYaw;
	private static KillAura killAura;
	private static int currentStrafeIndex;
	
	public TargetStrafe() {
		super();
		killAura = null;
	}
	
	@EventHandler
	public Listener<Event> eventListener = this::handle;
	
	public void handle(Event event) {
		if (killAura == null)
			killAura = (KillAura) Client.INSTANCE.getModuleManager().getModule(KillAura.class);
		if (event instanceof EventMove) {
			final EventMove e = (EventMove) event;
			final double currSpeed = Math.sqrt(e.getX() * e.getX() + e.getZ() * e.getZ());
			setSpeedForStrafe(e, currSpeed);
		}
		if (event instanceof EventRender3D) {
			if (!renderLine.getObject()) return;
			double range = rangeVal.getObject();
			if (!killAura.targets.isEmpty() && killAura.isEnabled()) {
				int color = colorVal.getObject().getRGB();
				final float smooth = smoothVal.getObject();
				final float width = lineWidth.getObject();
				final float pt = mc.timer.renderPartialTicks;
				final double x = killAura.targets.get(0).lastTickPosX + (killAura.targets.get(0).posX - killAura.targets.get(0).prevPosX) * pt - RenderManager.renderPosX;
				final double y = killAura.targets.get(0).lastTickPosY + (killAura.targets.get(0).posY - killAura.targets.get(0).prevPosY) * pt - RenderManager.renderPosY;
				final double z = killAura.targets.get(0).lastTickPosZ + (killAura.targets.get(0).posZ - killAura.targets.get(0).prevPosZ) * pt - RenderManager.renderPosZ;
				
				final float[] rgba = RenderUtil.INSTANCE.convertToRGBA(isStrafingDo() ? color : -1);
				glEnable(GL_BLEND);
				
				//Line Anti-alias
				glEnable(GL_LINE_SMOOTH);
				glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
				glDisable(GL_TEXTURE_2D);
				glDisable(GL_DEPTH_TEST);
				if (outline.getObject()) {
					glLineWidth(width + 1f);
					glColor4f(0, 0, 0, 1);
					glBegin(GL_LINE_LOOP);
					for (int i = 0; i <= smooth; i++) {
						glVertex3d(x + Math.sin(i * Math.PI / ((smooth) * 0.5)) * range,
								y + 0.1, z + Math.cos(i * Math.PI / (smooth * 0.5)) * range);
					}
					glEnd();
				}
				glColor4f(rgba[0], rgba[1], rgba[2], rgba[3]);
				glLineWidth(width);
				glBegin(GL_LINE_LOOP);
				for (int i = 0; i <= smooth; i++) {
					glVertex3d(x + Math.sin(i * Math.PI / ((smooth) * 0.5)) * range,
							y + 0.1, z + Math.cos(i * Math.PI / (smooth * 0.5)) * range);
				}
				glEnd();
				glDisable(GL_BLEND);
				glDisable(GL_LINE_SMOOTH);
				
				//Disable the Line Anti-alias
				glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
				glEnable(GL_TEXTURE_2D);
				glEnable(GL_DEPTH_TEST);
				GlStateManager.resetColor();
			}
		}
		if (event instanceof EventUpdate)
			if (mc.thePlayer.isCollidedHorizontally) strafeDirection = !strafeDirection;
	}
	
	public boolean isStrafingDo() {
		return mc.thePlayer.isMoving() && isStrafing;
	}
	
	private static boolean shouldStrafe() {
		Speed speedModule = (Speed) Client.INSTANCE.getModuleManager().getModule(Speed.class);
		return (speedModule.isEnabled() && moduleCombo.isSelected("Speed")) || isFlight() ||
				(Minecraft.getMinecraft().gameSettings.keyBindJump.pressed && moduleCombo.isSelected("Space"));
	}
	
	private static boolean isFlight() {
		final Flight flight = (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
		final LongJump longJump = (LongJump) Client.INSTANCE.getModuleManager().getModule(LongJump.class);
		return (flight.isEnabled() && moduleCombo.isSelected("Flight")) || (longJump.isEnabled() && moduleCombo.isSelected("Long Jump"));
	}
	
	public static void setSpeedForStrafe(EventMove move, double speed) {
		Minecraft mc = Minecraft.getMinecraft();
		double range = rangeVal.getObject();
		strafeYaw = mc.thePlayer.rotationYaw;
		isStrafing = false;
		if (onJumpKey.getObject() && !mc.gameSettings.keyBindJump.isKeyDown()) isStrafing = false;
		if (onJumpKey.getObject() && !mc.gameSettings.keyBindJump.isKeyDown()) return;
		boolean isFlight = isFlight();
		if (!(!killAura.targets.isEmpty() && killAura.isEnabled() && shouldStrafe()))
			isStrafing = false;
		if (!killAura.targets.isEmpty() && killAura.isEnabled() && shouldStrafe()) {
			isStrafing = true;
			Vec3 playerVector = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
			AxisAlignedBB aa = killAura.targets.get(0).getEntityBoundingBox();
			Vec3 entityVector = new Vec3(aa.minX + (aa.maxX - aa.minX) * 0.5, aa.minY + (aa.maxY - aa.minY) * 0.5, aa.minZ + (aa.maxZ - aa.minZ) * 0.5);
			double y = entityVector.yCoord - playerVector.yCoord;
			double x = entityVector.xCoord - playerVector.xCoord;
			double z = entityVector.zCoord - playerVector.zCoord;
			double dff = Math.sqrt(x * x + z * z);
			float yaw = (float) Math.toDegrees(Math.atan2(z, x)) - 90.0f;
			float pitch = (float) (-Math.toDegrees(Math.atan2(y, dff)));
			float newYaw;
			double sX = MathUtil.getDifference(mc.thePlayer.posX, mc.thePlayer.lastTickPosX);
			double sZ = MathUtil.getDifference(mc.thePlayer.posZ, mc.thePlayer.lastTickPosZ);
			double sp = Math.sqrt(sX * sX + sZ * sZ);
			double dffX = MathUtil.getDifference(mc.thePlayer.posX, killAura.targets.get(0).posX);
			double dffZ = MathUtil.getDifference(mc.thePlayer.posZ, killAura.targets.get(0).posZ);
			double distanceXZ = Math.sqrt(dffX * dffX + dffZ * dffZ);
			float strafeVal = (float) (90 - ((sp * 0.6)));
			strafeVal += Math.min(90, Math.toDegrees(rangeVal.getObject() - distanceXZ));
			if (distanceXZ > rangeVal.getObject() + 2) strafeVal = 0;
			if (strafeDirection) newYaw = strafeVal;
			else newYaw = -strafeVal;
			if (mc.thePlayer.isMovingLeft()) strafeDirection = true;
			else if (mc.thePlayer.isMovingRight()) strafeDirection = false;
			boolean foundBlocks = false;
			BlockPos playerPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY, mc.thePlayer.posZ);
			for (int i = 0; i <= 20; i++) if (!mc.theWorld.isAirBlock(playerPos.add(0, -i, 0))) foundBlocks = true;
			if (distanceXZ >= range + 1 && !isFlight)
				newYaw = 0;
			if (!foundBlocks && !isFlight) {
				if (strafeDirection) newYaw = -30;
				else newYaw = 30;
				strafeDirection = !strafeDirection;
			}
			boolean foundBlocks1 = false;
			BlockPos playerPos1 = new BlockPos(killAura.targets.get(0).posX, killAura.targets.get(0).getEntityBoundingBox().minY, killAura.targets.get(0).posZ);
			for (int i = 0; i <= 20; i++) if (!mc.theWorld.isAirBlock(playerPos1.add(0, -i, 0))) foundBlocks1 = true;
			if (!foundBlocks1 && !isFlight) return;
			if (!foundBlocks && newYaw == 0) return;
			if (range == 0. && foundBlocks && foundBlocks1 && !behindValue.getObject()) {
				strafeYaw = yaw;
				double speed1 = Math.min(Math.max(range, distanceXZ * 0.6), speed);
				move.setX(-(Math.sin(Math.toRadians(yaw)) * speed1));
				move.setZ(Math.cos(Math.toRadians(yaw)) * speed1);
				return;
			}
			float[] rotations = new float[]{MathHelper.wrapAngleTo180_float(yaw), MathHelper.wrapAngleTo180_float(pitch)};
			float dir = (float) Math.toRadians(rotations[0] - newYaw);
			if (behindValue.getObject()) {
				double tempYaw = Math.toRadians(MathHelper.wrapAngleTo180_float(killAura.targets.get(0).rotationYaw + 180));
				double targetX = -Math.sin(tempYaw) * range;
				double targetZ = Math.cos(tempYaw) * range;
				Vec3 vec = killAura.targets.get(0).getPositionVector().addVector(targetX, 0, targetZ);
				double tempDirYaw = RotationUtil.faceVector(vec)[0];
				float tempStrafeVal = (float) (((sp * 0.6)));
				if (foundBlocks1 && foundBlocks) dir = (float) Math.toRadians(tempDirYaw - tempStrafeVal);
				if (foundBlocks && foundBlocks1) {
					strafeYaw = (float) Math.toDegrees(dir);
					double dffX1 = MathUtil.getDifference(mc.thePlayer.posX, vec.xCoord);
					double dffZ1 = MathUtil.getDifference(mc.thePlayer.posZ, vec.zCoord);
					double distanceXZ1 = Math.sqrt(dffX1 * dffX1 + dffZ1 * dffZ1);
					double speed1 = Math.min(distanceXZ1 * 0.6, speed);
					move.setX(-(Math.sin(dir) * speed1));
					move.setZ(Math.cos(dir) * speed1);
					return;
				}
			}
			strafeYaw = (float) Math.toDegrees(dir);
			move.setX(-(Math.sin(dir) * speed));
			move.setZ(Math.cos(dir) * speed);
			if (shape.getObject() && !behindValue.getObject() && rangeVal.getObject() > 0.5) {
				range += 0.25;
				Vec3 vec = entityVector.addVector(Math.sin(currentStrafeIndex * (strafeDirection ? -1 : 1) * Math.PI / (smoothVal.getObject() * 0.5)) * range, 0,
						Math.cos(currentStrafeIndex * (strafeDirection ? -1 : 1) * Math.PI / (smoothVal.getObject() * 0.5)) * range);
				double dffX1 = MathUtil.getDifference(mc.thePlayer.posX, vec.xCoord);
				double dffZ1 = MathUtil.getDifference(mc.thePlayer.posZ, vec.zCoord);
				double distanceXZ1 = Math.sqrt(dffX1 * dffX1 + dffZ1 * dffZ1);
				double speed1 = Math.min(Math.max(distanceXZ1, 1), speed);
				dir = (float) Math.toRadians(RotationUtil.faceVector(vec)[0]);
				strafeYaw = (float) Math.toDegrees(dir);
				move.setX(-(Math.sin(dir) * speed1));
				move.setZ(Math.cos(dir) * speed1);
				if (distanceXZ1 < 0.5) currentStrafeIndex++;
				if (currentStrafeIndex > smoothVal.getObject()) currentStrafeIndex = 0;
			}
		}
	}
}