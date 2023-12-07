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
import net.minecraft.util.BlockPos;

public class ESP extends Module {

	@Override
	public ModSetting[] getModSettings() {
		
		CheckBtnSetting box1 = new CheckBtnSetting("Players", "playerESP");
		CheckBtnSetting box2 = new CheckBtnSetting("Mobs", "mobsESP");
		CheckBtnSetting box3 = new CheckBtnSetting("Animals", "animalESP");
		CheckBtnSetting box4 = new CheckBtnSetting("BlockHunt", "blockHuntESP");
		CheckBtnSetting box5 = new CheckBtnSetting("Storage", "storageESP");
		CheckBtnSetting box6 = new CheckBtnSetting("ESP Fade", "espFade");
		CheckBtnSetting box7 = new CheckBtnSetting("Color Based on Accuracy", "ESPaccuracyColor");
		
		return new ModSetting[] { box1, box2, box3, box4, box5, box6, box7 };
	}

	public ESP() {
		super("ESP", Keyboard.KEY_NONE, Category.RENDER, "Renders a box on entities.");
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

		for (Object o : mc.theWorld.loadedEntityList) {
			if (ClientSettings.blockHuntESP) {
				Entity en = (Entity) o;
				if (en.isInvisible() && en instanceof EntityLiving && en.height < 0.5) {
					drawProphuntESP(0.4f, 0.4f, 1f, 1f, en);
				}
			}
			if (!this.currentMode.equals("Box")) {
				break;
			}
			if (ClientSettings.playerESP) {
				if (!(o instanceof EntityPlayerSP)) {
					if (o instanceof EntityPlayer) {
						EntityPlayer en = (EntityPlayer) o;
						if (Jigsaw.getFriendsMananger().isFriend(en)) {
							drawESP(0.0f, 1f, 1f, 1f, en);
							continue;
						}
						if(Utils.isBlacklisted(en)) {
//							drawESP(0f, 0f, 0f, 0.0f, en);
						}
						else {
							if(ClientSettings.ESPaccuracyColor) {
								if(HackerDetect.getHackerByName(en.getName()) != null && HackerDetect.getHackerByName(en.getName()).isAccuracyListUsable()) {
									float accuracy = (HackerDetect.getHackerByName(en.getName()).accuracyValue / 360f);
									accuracy -= 0.3;
									accuracy *= 6;
									accuracy += 0.3;
//									System.out.println(HackerDetect.getHackerByName(en.getName()).accuracyValue / 360f + " - " + accuracy);
//									if(en.getName().equals("SENFI_LOX")) {
//										System.out.println(HackerDetect.getHackerByName(en.getName()).accuracyValue / 360f + " - " + accuracy);
//									}
									drawESP(accuracy, 1f - accuracy / 2f, 1f - accuracy, 1f, en);
								}
								else {
									drawESP(1f, 1f, 1f, 1f, en);
								}
							}
							else {
								if (Team.isOnTeam(en)) {
//									System.out.println(HackerDetect.getHackerByName(en.getName()).accuracyValue);
//									System.out.println(HackerDetect.getHackerByName(en.getName()).accuracyValue / 360f);
									
									drawESP(0.5f, 1f, 0.5f, 1f, en);
								} else {
									drawESP(1f, 1f, 1f, 1f, en);
								}
							}
						}
					}
				}
			}
			if (ClientSettings.mobsESP) {
				if (o instanceof IMob) {
					Entity en = (Entity) o;
					drawMob(1, 0.1f, 0.5f, 1f, en);
				}

			}
			if (ClientSettings.animalESP) {
				if (o instanceof EntityAnimal) {
					Entity en = (Entity) o;
					drawMob(1, 1f, 0.5f, 1f, en);
				}
			}
		}
		if (ClientSettings.storageESP) {
			for (Object o : mc.theWorld.loadedTileEntityList) {
				if (!(o instanceof TileEntity)) {
					continue;
				}
				BlockPos blockPos = new BlockPos(((TileEntity) o).getPos());

				double x = blockPos.getX() - mc.getRenderManager().renderPosX;
				double y = blockPos.getY() - mc.getRenderManager().renderPosY;
				double z = blockPos.getZ() - mc.getRenderManager().renderPosZ;
				if(o instanceof TileEntityFurnace) {
					RenderTools.drawOutlinedBlockESP(x, y, z, 0.5f, 1f, 1f, 1f, 1f);
					RenderTools.drawSolidBlockESP(x, y, z, 0.5f, 1f, 1f, 0.2f);
				}
				if(o instanceof TileEntityBrewingStand) {
					RenderTools.drawOutlinedBlockESP(x, y, z, 0.5f, 0.5f, 1f, 1f, 1f);
					RenderTools.drawSolidBlockESP(x, y, z, 0.5f, 0.5f, 1f, 0.2f);
				}
				if (o instanceof TileEntityChest) {
					BlockChest blockChest = (BlockChest) ((TileEntityChest)o).getBlockType();
					if(blockChest.chestType == 0) {
						RenderTools.drawOutlinedBlockESP(x, y, z, 1f, 0.5f, 0.5f, 1f, 1f);
						RenderTools.drawSolidBlockESP(x, y, z, 1f, 0.5f, 0.5f, 0.2f);
					}
					else {
						RenderTools.drawOutlinedBlockESP(x, y, z, 1f, 0.2f, 0.2f, 1f, 1f);
						RenderTools.drawSolidBlockESP(x, y, z, 1f, 0.2f, 0.2f, 0.2f);
					}
					
				}
				if (o instanceof TileEntityEnderChest) {
					RenderTools.drawOutlinedBlockESP(x, y, z, 1f, 0.3f, 1f, 1f, 1f);
					RenderTools.drawSolidBlockESP(x, y, z, 1f, 0.3f, 1f, 0.2f);
				}
			}
		}

		super.onRender();
	}

	public void drawESP(float red, float green, float blue, float alpha, Entity e) {
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
		red -= (double)e.hurtResistantTime / 30d;
		green -= (double)e.hurtResistantTime / 30d;
		blue -= (double)e.hurtResistantTime / 30d;
		if(false) {
			if(ClientSettings.espFade) {
				RenderTools.drawSolidEntityESP(xPos, yPos, zPos, e.width / 2, e.height, 1f, 0.5f, 0.5f, Math.min(0.2f, mc.thePlayer.getDistanceToEntity(e) / 30f));
				RenderTools.drawOutlinedEntityESP(xPos, yPos, zPos, e.width / 2, e.height, red, green, blue, (mc.thePlayer.getDistanceToEntity(e) / 30f));
				
			}
			else {
				RenderTools.drawSolidEntityESP(xPos, yPos, zPos, e.width / 2, e.height, 1f, 0.5f, 0.5f, 0.2f);
				RenderTools.drawOutlinedEntityESP(xPos, yPos, zPos, e.width / 2, e.height, red, green, blue, alpha);
				
			}
		}
		else {
			if(ClientSettings.espFade) {
				RenderTools.drawSolidEntityESP(xPos, yPos, zPos, e.width / 2, e.height, red, green, blue, Math.min(0.2f, mc.thePlayer.getDistanceToEntity(e) / 30f));
				RenderTools.drawOutlinedEntityESP(xPos, yPos, zPos, e.width / 2, e.height, red, green, blue, (mc.thePlayer.getDistanceToEntity(e) / 30f));
				
			}
			else {
				RenderTools.drawSolidEntityESP(xPos, yPos, zPos, e.width / 2, e.height, red, green, blue, 0.2f);
				RenderTools.drawOutlinedEntityESP(xPos, yPos, zPos, e.width / 2, e.height, red, green, blue, alpha);
				
			}
		}
		
		
	}

	public void drawProphuntESP(float red, float green, float blue, float alpha, Entity e) {
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

		RenderTools.drawOutlinedBlockESP(xPos - 0.5, yPos, zPos - 0.5f, red, green, blue, alpha, 2);
		RenderTools.drawSolidBlockESP(xPos - 0.5, yPos, zPos - 0.5f, red, green, blue, 0.3f);
	}

	public void drawMob(float red, float green, float blue, float alpha, Entity e) {
		drawESP(red, green, blue, alpha, e);
	}

	@Override
	public String[] getModes() {
		return new String[] { "Box" };
	}

	@Override
	public String getAddonText() {
		if(ClientSettings.espFade) {
			return this.currentMode + ", " + "Fade";
		}
		else {
			return this.currentMode;
		}
		
	}

}
