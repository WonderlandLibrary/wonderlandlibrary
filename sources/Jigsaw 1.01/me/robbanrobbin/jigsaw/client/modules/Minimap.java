package me.robbanrobbin.jigsaw.client.modules;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.minimap.MinimapInfo;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.Chunk;

public class Minimap extends Module {
	
	private WaitTimer updateTimer = new WaitTimer();
	
	public static int width = 10 * 16;
	public static int height = 10 * 16;
	public static int renderWidth = width - 32;
	public static int renderHeight = height - 32;
	
	public static MinimapInfo mapInfo;
	
	int preChunkX;
	int preChunkZ;
	
	public static double mouseX;
	public static double mouseY;
	
	public static int inWorldMouseX;
	public static int inWorldMouseY;
	public static int inWorldMouseZ;
	
	public static String coordStringToDraw = "";
	
	public static Framebuffer frameBuffer;
	
	public Minimap() {
		super("Minimap", Keyboard.KEY_NONE, Category.HIDDEN, "Shows a minimap");
	}
	
	@Override
	public void onClientLoad() {
		
		frameBuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
		frameBuffer.setFramebufferColor(0f, 0f, 0f, 0f);
		
		super.onClientLoad();
	}
	
	@Override
	public void onResize(int width, int height) {
		
		frameBuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
		frameBuffer.setFramebufferColor(0f, 0f, 0f, 0f);
		frameBuffer.framebufferClear();
		
		super.onResize(width, height);
	}
	
	@Override
	public void onUpdate(UpdateEvent event) {
		
		renderWidth = width - 32;
		renderHeight = height - 32 + 1;
		
		if(Jigsaw.getClickGuiManager().lastOverlayWindow == null) {
			if(preChunkX != mc.player.chunkCoordX || preChunkZ != mc.player.chunkCoordZ) {
				updateMapInfo();
				updateTimer.reset();
			}
			
			if(updateTimer.hasTimeElapsed(200, true)) {
				updateMapInfo();
			}
		}
		
		preChunkX = mc.player.chunkCoordX;
		preChunkZ = mc.player.chunkCoordZ;
		
		super.onUpdate(event);
	}
	
	public void updateMapInfo() {
		
		MinimapInfo mapInfo = new MinimapInfo();
		
		boolean foundMouseThing = false;
		
		/*https://hastebin.com/azivavinoz.java //TODO GLOWESP*/
		
		double playerPosX = mc.player.posX;
		double playerPosY = mc.player.posY;
		double playerPosZ = mc.player.posZ;
		
		for(int x = 0; x < width / 16; x++) {
			for(int z = 0; z < height / 16; z++) {
				
				if(x == 0 && z == 0) {
					if(mouseX != -1 && mouseY != -1) {
						double mouseX = mc.player.posX - renderWidth / 2 + this.mouseX;
						double mouseY = mc.player.posZ - renderHeight / 2 + this.mouseY;
						this.inWorldMouseX = (int)mouseX;
						this.inWorldMouseZ = (int)mouseY;
					}
				}
				
				int playerChunkPosX = mc.player.chunkCoordX;
				int playerChunkPosZ = mc.player.chunkCoordZ;
				
				Chunk chunk = mc.world.getChunkFromChunkCoords(playerChunkPosX + x - width / 32, playerChunkPosZ + z - height / 32);
				
				if(!chunk.isLoaded()) {
					continue;
				}
				
				for(int blockX = 0; blockX < 16; blockX++) {
					for(int blockZ = 0; blockZ < 16; blockZ++) {
						
						int worldBlockY = chunk.getHeightValue(blockX, blockZ) - 1;
						int worldBlockX = blockX + chunk.x * 16;
						int worldBlockZ = blockZ + chunk.z * 16;
						
						if(!foundMouseThing && mouseX != -1 && mouseY != -1 && Minimap.inWorldMouseX == worldBlockX && Minimap.inWorldMouseZ == worldBlockZ) {
							this.inWorldMouseY = worldBlockY + 1;
							foundMouseThing = true;
							coordStringToDraw = "(" + Minimap.inWorldMouseX + ", " + Minimap.inWorldMouseY + ", " + Minimap.inWorldMouseZ + ")";
						}
						
//						if(worldBlockY <= 1) {
//							mapInfo.addColorAndLightData(blockX + chunk.xPosition * 16, blockZ + chunk.zPosition * 16, 0.521568627f, 0.65098039215f, 1f);
//							continue;
//						}
						
						BlockPos worldBlockPos = new BlockPos(worldBlockX, worldBlockY, worldBlockZ);
						BlockPos localBlockPos = new BlockPos(blockX, worldBlockY, blockZ);
						
						IBlockState blockState = chunk.getBlockState(localBlockPos);
						Block block = blockState.getBlock();
						
						int color = blockState.getMapColor(mc.world, worldBlockPos).colorValue;
						
						if(color == 0) {
							color = 0x85A6FF;
						}
						
//						System.out.println(color);
						
						float light = -((float)mc.player.posY - (float)worldBlockY) / 100f;
						
						
//						light = chunk.getLightFor(EnumSkyBlock.SKY, blockPos);
//						light += chunk.getLightFor(EnumSkyBlock.BLOCK, blockPos);
						
						float max = 0.2f;
						
						if(color != 0x85A6FF) {
							if(light > max) {
								light = max;
							}
							
							if(light < -max) {
								light = -max;
							}
							
							light += 0.6f;
						}
						else {
							light = 1f;
						}
						
//						float light = 1f;
						
						float smoothness = (float)ClientSettings.minimapTerrainSmoothness;
						smoothness = (1f - smoothness) / 2f;
						
						if(!Utils.getBlockState(worldBlockPos.north()).isFullBlock()) {
							light = light + smoothness;
						}
						else if(!Utils.getBlockState(worldBlockPos.south()).isFullBlock()) {
							light = light - smoothness;
						}
						
						Color clr = Minimap.toColor(color);
						
						mapInfo.addColorAndLightData(worldBlockX + 1, worldBlockZ, (((float)clr.getRed()) / 255f) * light, (((float)clr.getGreen()) / 255f) * light, (((float)clr.getBlue()) / 255f) * light);
						
					}
				}
				
			}
		}
		
		for(Entity en : mc.world.loadedEntityList) {
			
			if(en instanceof EntityPlayerSP) {
				continue;
			}
			
			if(en instanceof EntityArrow) {
				Vec3d lastPosVec = new Vec3d(en.lastTickPosX - en.posX, 0, en.lastTickPosZ - en.posZ);
				
				mapInfo.addArrowData((int)en.posX, (int)en.posZ, (EntityArrow) en);
			}
			else {
				mapInfo.addEntityData((int)en.posX, (int)en.posZ, en);
			}
			
			
		}
		
		this.mapInfo = mapInfo;
		
	}
	
	@Override
	public boolean getEnableAtStartup() {
		return true;
	}
	
	@Override
	public boolean dontToggleOnLoadModules() {
		return true;
	}
	
	public static Color toColor(int rgb) {
		int r = (rgb & 0xFF0000) >> 16;
	    int g = (rgb & 0xFF00) >> 8;
	    int b = (rgb & 0xFF);
		return new Color(r, g, b);
	}
	
	public static Color toColor2(int rgb) {
		int r = rgb & 0xFF, g = rgb >> 8 & 0xFF, b = rgb >> 16 & 0xFF;
		return new Color(r, g, b);
	}
	
	@Override
	public void onGui() {
		
		
		
		super.onGui();
	}

}
