package best.azura.client.impl.module.impl.render;

import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.impl.events.EventRender2D;
import best.azura.client.impl.events.EventRender3D;
import best.azura.client.impl.events.EventRender3DPost;
import best.azura.client.impl.module.impl.render.esp.ESP2D;
import best.azura.client.impl.module.impl.render.esp.ShaderESP;
import best.azura.client.impl.value.*;
import best.azura.client.util.color.HSBColor;
import best.azura.client.util.render.RenderUtil;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

@ModuleInfo(name = "ESP", description = "Indicate entities through walls.", category = Category.RENDER)
public class ESP extends Module {


	final ESP2D esp2D = new ESP2D();
	private final ShaderESP shaderESP = new ShaderESP();

	public final BooleanValue checkPit = new BooleanValue("Pit check", "Checks for pit height.", true);

	public static final ComboValue espCombo = new ComboValue("Player ESP", "Define what esp should be rendered.",
			new ComboSelection("Chams", true),
			new ComboSelection("Boxy", false),
			new ComboSelection("Hands (Chams)", false),
			new ComboSelection("Tracers", false),
			new ComboSelection("2D", true),
			new ComboSelection("Shader", false));

	public final ComboValue targetsCombo = new ComboValue("2D ESP Targets", "Define targets for the 2D esp.", () -> espCombo.isSelected("2D"),
			new ComboSelection("Players", true),
			new ComboSelection("Monsters", false),
			new ComboSelection("Animals", false),
			new ComboSelection("Items", false),
			new ComboSelection("Chests", false));

	public final ComboValue boxyTargets = new ComboValue("Box ESP Targets", "Define targets for the Boxy esp.", () -> espCombo.isSelected("Boxy"),
			new ComboSelection("Players", true),
			new ComboSelection("Monsters", false),
			new ComboSelection("Animals", false),
			new ComboSelection("Items", false),
			new ComboSelection("Chests", false));

	public static final ComboValue shaderTargets = new ComboValue("Shader ESP Targets", "Define targets for the shader esp.", () -> espCombo.isSelected("Shader"),
			new ComboSelection("Players", true),
			new ComboSelection("Monsters", false),
			new ComboSelection("Animals", false),
			new ComboSelection("Items", false),
			new ComboSelection("Chests", false));

	public final ComboValue tracerTargets = new ComboValue("Tracer Targets", "Define targets for the tracers.", () -> espCombo.isSelected("Tracers"),
			new ComboSelection("Players", true),
			new ComboSelection("Monsters", false),
			new ComboSelection("Animals", false),
			new ComboSelection("Items", false),
			new ComboSelection("Chests", false));


	public final ModeValue esp2DBoxMode = new ModeValue("2D Box Mode", "Mode for 2d box rendering", () -> espCombo.isSelected("2D"), "Full", "Full", "Side", "Corner");
	public final ColorValue espColor = new ColorValue("2D Color", "Change the color of the 2d esp", () -> espCombo.isSelected("2D"), HSBColor.fromColor(new Color(0x5B5BAB)));
	public final ColorValue espBoxColor = new ColorValue("2D Box Color", "Change the color of the 2d esp box", () -> this.esp2DCombo.isSelected("Filled"), HSBColor.fromColor(new Color(0xFF5B5BAB)));
	public final ComboValue esp2DCombo = new ComboValue("2D ESP Settings", "Define what things should be rendered on the 2D ESP.", () -> espCombo.isSelected("2D"),
			new ComboSelection("Box", true),
			new ComboSelection("Filled", false),
			new ComboSelection("Health bar", true),
			new ComboSelection("Health text", false),
			new ComboSelection("Armor bar", true),
			new ComboSelection("Held item", false),
			new ComboSelection("Name tags", false),
			new ComboSelection("Image", false));

	public static final ComboValue shaderCombo = new ComboValue("Shader ESP Settings", "Define what things should be rendered on the shader ESP.", () -> espCombo.isSelected("Shader"),
			new ComboSelection("Outline", true),
			new ComboSelection("Fill", false),
			new ComboSelection("Chams", true));

	public static final ColorValue shaderOutlineColor = new ColorValue("Shader outline color", "Change the color of the shader outline",
			() -> espCombo.isSelected("Shader") && shaderCombo.isSelected("Outline"), HSBColor.fromColor(new Color(0x5B5BAB)));

	public static final NumberValue<Float> shaderOutlineAlpha = new NumberValue<>("Shader outline alpha", "Shader outline alpha change",
			() -> espCombo.isSelected("Shader") && shaderOutlineColor.collapsed && shaderOutlineColor.checkDependency(), 1.0F, 0.01F, 0.2F, 1F);

	public static final NumberValue<Float> shaderOutlineWidth = new NumberValue<>("Shader outline width", "Shader outline alpha change",
			() -> espCombo.isSelected("Shader") && shaderCombo.isSelected("Outline"), 1.0F, 0.5F, 1F, 10F);

	public static final ColorValue shaderFilledColor = new ColorValue("Shader filled color", "Change the color of the shader fill",
			() -> espCombo.isSelected("Shader") && shaderCombo.isSelected("Fill"), HSBColor.fromColor(new Color(0x5B5BAB)));

	public static final NumberValue<Float> shaderFilledAlpha = new NumberValue<>("Shader filled alpha", "Shader fill alpha change",
			() -> espCombo.isSelected("Shader") && shaderFilledColor.collapsed && shaderFilledColor.checkDependency(), 1.0F, 0.01F, 0.2F, 1F);

	public static final NumberValue<Float> shaderChamsAlpha = new NumberValue<>("Shader chams alpha", "Shader fill alpha change",
			() -> espCombo.isSelected("Shader") && shaderCombo.isSelected("Chams"), 1.0F, 0.01F, 0.2F, 1F);


	public static final ColorValue colorValue = new ColorValue("Chams color", "Change the color of the chams",
			() -> espCombo.isSelected("Chams"), HSBColor.fromColor(new Color(0x5B5BAB)));
	public static final NumberValue<Float> shownAlpha = new NumberValue<>("Chams alpha shown", "Chams alpha change",
			() -> espCombo.isSelected("Chams") && colorValue.collapsed && colorValue.checkDependency(), 255F, 1F, 1F, 255F);
	public static final ColorValue hiddenChams = new ColorValue("Hidden Chams color", "Change the color of the chams",
			() -> espCombo.isSelected("Chams"), HSBColor.fromColor(new Color(0x5B5BAB)));
	public static final NumberValue<Float> hiddenAlpha = new NumberValue<>("Chams alpha hidden", "Chams alpha change",
			() -> espCombo.isSelected("Chams") && hiddenChams.collapsed && hiddenChams.checkDependency(), 255F, 1F, 1F, 255F);

	public final NumberValue<Integer> alphaValue = new NumberValue<Integer>("2D Box Color Alpha", "Change the alpha of the 2d esp box",
			() -> espBoxColor.collapsed && espBoxColor.checkDependency(), 255, 0, 255);
	public static final ColorValue boxColor = new ColorValue("Box Color", "Change to Color of the BOX ESP",
			() -> espCombo.isSelected("Boxy"), HSBColor.fromColor(new Color(0x5B5BAB)));

	public final ColorValue chestBox = new ColorValue("Chest Box Color", "Change to Color of the Chest BOX ESP",
			() -> espCombo.isSelected("Boxy") && boxyTargets.isSelected("Chests"), HSBColor.fromColor(new Color(0x5B5BAB)));

	public static final BooleanValue handChamsRenderTwice = new BooleanValue("Render hands twice", "Render the hands twice.",
			() -> espCombo.isSelected("Hands (Chams)"), true);

	@EventHandler
	public Listener<Event> eventListener = this::handle;

	private void handle(Event event) {
		if (espCombo.isSelected("Shader") && mc.theWorld != null && mc.thePlayer != null) {
			shaderESP.onEvent(event);
		}
		if (event instanceof EventRender3DPost) {
			if (!espCombo.isSelected("Tracers")) return;
			glEnable(GL_BLEND);
			glEnable(GL_LINE_SMOOTH);
			glDisable(GL_TEXTURE_2D);
			glDisable(GL_DEPTH_TEST);
			glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
			for (Entity entity : mc.theWorld.loadedEntityList) {
				if (entity.isInvisible()) continue;
				if (entity == mc.thePlayer) continue;
				if (entity.getName().contains("[NPC]")) continue;
				if (!(tracerTargets.isSelected("Players") && entity instanceof EntityPlayer) &&
						!(tracerTargets.isSelected("Monsters") && entity instanceof EntityCreature) &&
						!(tracerTargets.isSelected("Items") && entity instanceof EntityItem) &&
						!(tracerTargets.isSelected("Animals") && entity instanceof EntityAnimal)
				) continue;
				final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks - RenderManager.renderPosX,
						y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks - RenderManager.renderPosY,
						z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - RenderManager.renderPosZ;
				glLineWidth(2.0F);
				glColor4d(1, 1, 1, 1);
				glBegin(GL_LINE_LOOP);
				glVertex3d(0, mc.thePlayer.getEyeHeight(), 0);
				glVertex3d(x, y, z);
				glEnd();

			}
			for (TileEntity entity : mc.theWorld.loadedTileEntityList) {
				if (!(targetsCombo.isSelected("Chests") && entity instanceof TileEntityChest)) continue;
				glColor4d(1, 1, 1, 1);
				final double x = entity.getPos().getX() + 0.5 - RenderManager.renderPosX,
						y = entity.getPos().getY() + 0.5 - RenderManager.renderPosY,
						z = entity.getPos().getZ() + 0.5 - RenderManager.renderPosZ;
				glBegin(GL_LINE_LOOP);
				glVertex3d(0, mc.thePlayer.getEyeHeight(), 0);
				glVertex3d(x, y, z);
				glEnd();
			}
			glDisable(GL_BLEND);
			glDisable(GL_LINE_SMOOTH);
			glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
			glEnable(GL_TEXTURE_2D);
			glEnable(GL_DEPTH_TEST);
			GlStateManager.resetColor();
		}

		if (event instanceof EventRender2D) {
			if (espCombo.isSelected("2D") && mc.theWorld != null && mc.thePlayer != null) {
				esp2D.eventRender2DListener.call((EventRender2D) event);
			}
		}

		if (event instanceof EventRender3D) {
			if (espCombo.isSelected("2D") && mc.theWorld != null && mc.thePlayer != null) {
				esp2D.eventRender3DListener.call((EventRender3D) event);
			}
			if (!espCombo.isSelected("Boxy")) return;
			for (Entity entity : mc.theWorld.loadedEntityList) {
				if (checkPit.getObject() && entity.posY > 100
						&& entity.posX < 25 && entity.posX > -25 && entity.posZ < 25 && entity.posZ > -25) continue;
				if (entity.isInvisible()) continue;
				if (entity == mc.thePlayer) continue;
				if (entity.getName().contains("[NPC]")) continue;
				if (!(boxyTargets.isSelected("Players") && entity instanceof EntityPlayer) &&
						!(boxyTargets.isSelected("Monsters") && entity instanceof EntityCreature) &&
						!(boxyTargets.isSelected("Items") && entity instanceof EntityItem) &&
						!(boxyTargets.isSelected("Animals") && entity instanceof EntityAnimal)
				) continue;
				final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX)
						* mc.timer.renderPartialTicks - RenderManager.renderPosX,
						y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) *
								mc.timer.renderPartialTicks - RenderManager.renderPosY,
						z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) *
								mc.timer.renderPartialTicks - RenderManager.renderPosZ;
				if (entity instanceof EntityLivingBase) {
					EntityLivingBase b = (EntityLivingBase) entity;
					float interpolated = b.prevRotationYawHead +
							(b.rotationYawHead - b.prevRotationYawHead)
									*
									mc.timer.renderPartialTicks;
					GlStateManager.translate(x, y, z);
					GlStateManager.rotate(-interpolated, 0, 1, 0);
					GlStateManager.translate(-x, -y, -z);
				}
				RenderUtil.INSTANCE.renderBox(x, y, z, entity.width / 2,
						entity.height, new Color(boxColor.getObject().getColor().getRed(), boxColor.getObject().getColor().getGreen(), boxColor.getObject().getColor().getBlue(), 50), true);
				if (entity instanceof EntityLivingBase) {
					EntityLivingBase b = (EntityLivingBase) entity;
					float interpolated = b.prevRotationYawHead +
							(b.rotationYawHead - b.prevRotationYawHead) *
									mc.timer.renderPartialTicks;
					GlStateManager.translate(x, y, z);
					GlStateManager.rotate(interpolated, 0, 1, 0);
					GlStateManager.translate(-x, -y, -z);
				}
				glColor4d(1, 1, 1, 1);
			}

			for (TileEntity entity : mc.theWorld.loadedTileEntityList) {
				if (!(boxyTargets.isSelected("Chests") && entity instanceof TileEntityChest)) continue;
				glColor4d(1, 1, 1, 1);
				final double x = entity.getPos().getX() + 0.5 - RenderManager.renderPosX,
						y = entity.getPos().getY() + 0.5 - RenderManager.renderPosY,
						z = entity.getPos().getZ() + 0.5 - RenderManager.renderPosZ;
				RenderUtil.INSTANCE.renderBox(x, y - 0.5, z, 0.44,
						0.875, ((TileEntityChest) entity).getChestType() == 0 ?
								new Color(chestBox.getObject().getColor().getRed(), chestBox.getObject().getColor().getGreen(), chestBox.getObject().getColor().getBlue(), 50)
								: new Color(255, 0, 0, 50), false);


			}
		}
		GlStateManager.resetColor();
	}

}