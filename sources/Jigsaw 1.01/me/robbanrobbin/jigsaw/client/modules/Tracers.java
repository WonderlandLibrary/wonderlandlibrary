package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.modules.target.AuraUtils;
import me.robbanrobbin.jigsaw.client.modules.target.Team;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.RenderTools;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.CheckBtnSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ModSetting;
import me.robbanrobbin.jigsaw.hackerdetect.Hacker;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.block.BlockChest;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.BlockPos;

public class Tracers extends Module {

	@Override
	public ModSetting[] getModSettings() {
		CheckBtnSetting box1 = new CheckBtnSetting("Players", "playerTracers");
		CheckBtnSetting box2 = new CheckBtnSetting("Mobs", "mobsTracers");
		CheckBtnSetting box3 = new CheckBtnSetting("Animals", "animalTracers");
		CheckBtnSetting box4 = new CheckBtnSetting("BlockHunt", "blockHuntTracers");
		CheckBtnSetting box5 = new CheckBtnSetting("Storage", "storageTracers");
		return new ModSetting[] { box1, box2, box3, box4, box5 };
	}

	public Tracers() {
		super("Tracers", Keyboard.KEY_NONE, Category.RENDER, "Draws a line to all entities");
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@Override
	public void onRender() {

		for (Object o : mc.world.loadedEntityList) {
			if (ClientSettings.blockHuntTracers) {
				Entity en = (Entity) o;
				if (en.isInvisible() && en instanceof EntityLiving && en.height < 0.5) {
					drawProphuntTracers(0.4f, 0.4f, 1f, 1f, en);
				}
			}
			if (ClientSettings.playerTracers) {
				if (!(o instanceof EntityPlayerSP)) {
					if (o instanceof EntityPlayer) {
						EntityPlayer en = (EntityPlayer) o;
						if(Utils.isBlacklisted(en)) {
							continue;
						}
						if (Jigsaw.getFriendsMananger().isFriend(en)) {
							drawTracers(0.0f, 1f, 1f, 1f, en);
							continue;
						}
						if (Team.isOnTeam(en)) {
							drawTracers(0.5f, 1f, 0.5f, 1f, en);
						} else {
							drawTracers(1f, 1f, 1f, 1f, en);
						}
					}
				}
			}
			if (ClientSettings.mobsTracers) {
				if (o instanceof IMob) {
					Entity en = (Entity) o;
					drawMob(1, 0.1f, 0.5f, 1f, en);
				}

			}
			if (ClientSettings.animalTracers) {
				if (o instanceof EntityAnimal) {
					Entity en = (Entity) o;
					drawMob(1, 1f, 0.5f, 1f, en);
				}
			}
		}
		if (ClientSettings.storageTracers) {
			for (Object o : mc.world.loadedTileEntityList) {
				if (!(o instanceof TileEntity)) {
					continue;
				}
				BlockPos blockPos = new BlockPos(((TileEntity) o).getPos());

				double x = blockPos.getX() - mc.getRenderManager().renderPosX;
				double y = blockPos.getY() - mc.getRenderManager().renderPosY;
				double z = blockPos.getZ() - mc.getRenderManager().renderPosZ;
				if(o instanceof TileEntityFurnace) {
					RenderTools.drawTracerLine(x + 0.5f, y + 0.5f, z + 0.5f, 0.5f, 1f, 1f, 1f, 1.2f);
				}
				if(o instanceof TileEntityBrewingStand) {
					RenderTools.drawTracerLine(x + 0.5f, y + 0.5f, z + 0.5f, 0.5f, 0.5f, 1f, 1f, 1.2f);
				}
				if (o instanceof TileEntityChest) {
					BlockChest blockChest = (BlockChest) ((TileEntityChest)o).getBlockType();
					if(blockChest.chestType == BlockChest.Type.BASIC) {
						RenderTools.drawTracerLine(x + 0.5f, y + 0.5f, z + 0.5f, 1f, 0.5f, 0.5f, 1f, 1.2f);
					}
					else {
						RenderTools.drawTracerLine(x + 0.5f, y + 0.5f, z + 0.5f, 1f, 0.2f, 0.2f, 1f, 1.2f);
					}
				}
				if (o instanceof TileEntityEnderChest) {
					RenderTools.drawTracerLine(x + 0.5f, y + 0.5f, z + 0.5f, 1f, 0.3f, 1f, 1f, 1.2f);
				}
			}
		}

		super.onRender();
	}

	public void drawTracers(float red, float green, float blue, float alpha, Entity e) {
		if (e instanceof EntityPlayer && !Jigsaw.getFriendsMananger().isFriend((EntityPlayer) e)) {
			EntityPlayer player = (EntityPlayer) e;
			Hacker hacker = HackerDetect.getHackerByName(player.getName());
			if (hacker != null && hacker.getViolations() > 0) {
				red = 1f;
				green = 0.5f;
				blue = 0.5f;
			}
		}
		if (AuraUtils.hasEntity(e)) {
			red = 1;
			green = 0;
			blue = 0;
		}

		double xPos = (e.lastTickPosX + (e.posX - e.lastTickPosX) * mc.timer.renderPartialTicks)
				- mc.getRenderManager().renderPosX;
		double yPos = (e.lastTickPosY + (e.posY - e.lastTickPosY) * mc.timer.renderPartialTicks)
				- mc.getRenderManager().renderPosY;
		double zPos = (e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * mc.timer.renderPartialTicks)
				- mc.getRenderManager().renderPosZ;
		
		RenderTools.drawTracerLine(xPos, yPos, zPos, red, green, blue, alpha, 0.5f);
	}

	public void drawProphuntTracers(float red, float green, float blue, float alpha, Entity e) {
		if (AuraUtils.hasEntity(e)) {
			red = 1;
			green = 0;
			blue = 0;
		}

		double xPos = (e.lastTickPosX + (e.posX - e.lastTickPosX) * mc.timer.renderPartialTicks)
				- mc.getRenderManager().renderPosX;
		double yPos = (e.lastTickPosY + (e.posY - e.lastTickPosY) * mc.timer.renderPartialTicks)
				- mc.getRenderManager().renderPosY;
		double zPos = (e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * mc.timer.renderPartialTicks)
				- mc.getRenderManager().renderPosZ;

		RenderTools.drawTracerLine(xPos, yPos, zPos, red, green, blue, alpha, 0.5f);
	}

	public void drawMob(float red, float green, float blue, float alpha, Entity e) {
		drawTracers(red, green, blue, alpha, e);
	}

}
