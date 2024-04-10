package info.sigmaclient.sigma.minimap.minimap;

import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.modules.gui.MiniMap;
import info.sigmaclient.sigma.minimap.*;
import info.sigmaclient.sigma.minimap.animation.*;
import info.sigmaclient.sigma.minimap.interfaces.*;
import info.sigmaclient.sigma.minimap.settings.*;
import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import static net.minecraft.world.biome.BiomeColors.getGrassColor;


public class Minimap
{
    public Interface screen;
    public static final int frame = 9;
    public static int loadingSide;
    public static int loadedSide;
    public static int updateRadius;
    public static boolean enlargedMap;
    public static Minecraft mc;
    private int loadedCaving;
    private int loadingCaving;
    public static final Color radarPlayers;
    public static final Color radarShadow;
    public static ArrayList<Entity> loadedPlayers;
    public static ArrayList<Entity> loadedLiving;
    public static ArrayList<Entity> loadedHostile;
    public static ArrayList<Entity> loadedItems;
    public static ArrayList<Entity> loadedEntities;
    public static int blocksLoaded;
    public int loadingMapX;
    public int loadingMapZ;
    public int loadedMapX;
    public int loadedMapZ;
    public double minimapZoom;
    public int minimapWidth;
    public int minimapHeight;
    public MapLoader loader;
    private static String worldID;
    public static String customWorldID;
    public MinimapChunk[][] currentBlocks;
    public MinimapChunk[][] loadingBlocks;
    public static boolean frameIsUpdating;
    public static boolean frameUpdateNeeded;
    public static int bufferSizeToUpdate;
    public static float frameUpdatePartialTicks;
    public static boolean updatePause;
    Integer previousTransparentBlock;
    int underRed;
    int underGreen;
    int underBlue;
    float divider;
    int sun;
    public float postBrightness;
    int blockY;
    int blockColor;
    public static HashMap<String, Integer> textureColours;
    public static HashMap<Integer, Integer> blockColours;
    public static boolean clearBlockColours;
    boolean isglowing;
    public int[] lastBlockY;
    public static float sunBrightness;
    private byte[] bytes;
    private byte drawYState;
    private static boolean toResetImage;
    public static double zoom;
    public static int[] minimapSizes = new int[0];
    public static int[] bufferSizes = new int[0];
    public static int[] FBOMinimapSizes = new int[0];
    public static int[] FBOBufferSizes = new int[0];
    public static boolean triedFBO;
    public static boolean loadedFBO;
    public static DynamicTexture mapTexture;
    public static Framebuffer scalingFrameBuffer;
    public static Framebuffer rotationFrameBuffer;
    public static int mapUpdateX;
    public static int mapUpdateZ;

    static {
        Minimap.loadingSide = 16;
        Minimap.loadedSide = 16;
        Minimap.updateRadius = 16;
        Minimap.enlargedMap = false;
        Minimap.mc = Minecraft.getInstance();
        radarPlayers = new Color(255, 255, 255);
        radarShadow = new Color(0, 0, 0);
        Minimap.loadedPlayers = new ArrayList<Entity>();
        Minimap.loadedLiving = new ArrayList<Entity>();
        Minimap.loadedHostile = new ArrayList<Entity>();
        Minimap.loadedItems = new ArrayList<Entity>();
        Minimap.loadedEntities = new ArrayList<Entity>();
        Minimap.blocksLoaded = 0;
        Minimap.worldID = null;
        Minimap.customWorldID = null;
        Minimap.frameIsUpdating = false;
        Minimap.frameUpdateNeeded = false;
        Minimap.bufferSizeToUpdate = -1;
        Minimap.frameUpdatePartialTicks = 1.0f;
        Minimap.updatePause = false;
        Minimap.textureColours = new HashMap<String, Integer>();
        Minimap.blockColours = new HashMap<Integer, Integer>();
        Minimap.clearBlockColours = false;
        Minimap.toResetImage = true;
        Minimap.zoom = 1.0;
        minimapSizes = new int[] { 112, 168, 224, 336 };
        bufferSizes = new int[] { 128, 256, 256, 512 };
        FBOMinimapSizes = new int[] { 11, 17, 21, 31 };
        FBOBufferSizes = new int[] { 256, 512, 512, 512 };
        Minimap.triedFBO = false;
        Minimap.loadedFBO = false;
        Minimap.mapTexture = new DynamicTexture(InterfaceHandler.mapTextures);
    }
    public static int getLoadSide() {
        return Minimap.enlargedMap ? 31 : Minimap.FBOMinimapSizes[XaeroMinimap.getSettings().getMinimapSize()];
    }
    
    public static int getUpdateRadius() {
        return (int)Math.ceil(Minimap.loadingSide / 2.0 / XaeroMinimap.getSettings().zooms[XaeroMinimap.getSettings().zoom]);
    }
    
    /*private static String getWorld() {
        if (Minimap.mc.getIntegratedServer() != null) {
            return Minimap.mc.getIntegratedServer().getFolderName() + "_" + Minimap.mc.theWorld.provider.getSaveFolder();
        }
        if (Minimap.mc.getCurrentServerData() != null) {
            String serverIP = Minimap.mc.getCurrentServerData().serverIP;
            if (serverIP.contains(":")) {
                serverIP = serverIP.substring(0, serverIP.indexOf(":"));
            }
            return "Multiplayer_" + serverIP.replaceAll(":", "�") + "_" + Minimap.mc.theWorld.provider.getSaveFolder();
        }
        return "Unknown_null";
    }*/
    
    public static String getCurrentWorldID() {
        if (Minimap.customWorldID == null) {
            return Minimap.worldID;
        }
        return Minimap.customWorldID;
    }
    
    
    public static String getAutoWorldID() {
        return Minimap.worldID;
    }
    
    
    public static void updateWaypoints() {
       // addWorld(Minimap.worldID = getWorld());
      //  Minimap.waypoints = getWaypoints();
    }
    
    public Minimap(final Interface i) {
        this.loadedCaving = -1;
        this.loadingCaving = -1;
        this.loadingMapX = 0;
        this.loadingMapZ = 0;
        this.loadedMapX = 0;
        this.loadedMapZ = 0;
        this.minimapZoom = 1.0;
        this.minimapWidth = 0;
        this.minimapHeight = 0;
        this.loader = new MapLoader();
        this.currentBlocks = new MinimapChunk[16][16];
        this.loadingBlocks = new MinimapChunk[16][16];
        this.previousTransparentBlock = null;
        this.divider = 1.0f;
        this.blockY = 0;
        this.blockColor = 0;
        this.isglowing = false;
        this.lastBlockY = new int[16];
        this.drawYState = 0;
        this.screen = i;
        new Thread(this.loader).start();
    }
    
    public static int loadBlockColourFromTexture(final BlockState state, final Block b, final BlockPos pos, final boolean convert) {
        final int stateId = state.toString().hashCode();
        Integer c = Minimap.blockColours.get(stateId);
        int red = 0;
        int green = 0;
        int blue = 0;
        if (c == null) {
            final TextureAtlasSprite texture = Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state);
            String name = null;
            try {
                name = texture.getName() + ".png";
                if (b instanceof GrassBlock) {
                    name = "minecraft:blocks/grass_top.png";
                } else if (b == Blocks.RED_MUSHROOM_BLOCK) {
                    name = "minecraft:blocks/mushroom_block_skin_red.png";
                } else if (b == Blocks.BROWN_MUSHROOM_BLOCK) {
                    name = "minecraft:blocks/mushroom_block_skin_brown.png";
                } else if (b instanceof OreBlock && b != Blocks.QUARTZ_BLOCK) {
                    name = "minecraft:blocks/stone.png";
                }
                if (convert) {
                    name = name.replaceAll("_side", "_top").replaceAll("_front.png", "_top.png");
                }
                String[] args = name.split(":");
                if (args.length < 2) {
                    args = new String[]{"minecraft", args[0]};
                }
                final Integer cachedColour = Minimap.textureColours.get(name);
                if (cachedColour == null) {
                    final ResourceLocation location = new ResourceLocation(args[0], "textures/" + args[1]);
                    final IResource resource = Minecraft.getInstance().getResourceManager().getResource(location);
                    final InputStream input = resource.getInputStream();
                    final BufferedImage img = TextureUtil.readBufferedImage(input);
                    int total = 64;
                    final int tw = img.getWidth();
                    final int diff = tw / 8;
                    for (int i = 0; i < 8; ++i) {
                        for (int j = 0; j < 8; ++j) {
                            final int rgb = img.getRGB(i * diff, j * diff);
                            if (rgb == 0) {
                                --total;
                            } else {
                                red += (rgb >> 16 & 0xFF);
                                green += (rgb >> 8 & 0xFF);
                                blue += (rgb & 0xFF);
                            }
                        }
                    }
                    input.close();
                    if (total == 0) {
                        total = 1;
                    }
                    red /= total;
                    green /= total;
                    blue /= total;
                    c = (0xFF000000 | red << 16 | green << 8 | blue);
                    Minimap.textureColours.put(name, c);
                } else {
                    c = cachedColour;
                }
            } catch (FileNotFoundException e) {
                if (convert) {
                    return loadBlockColourFromTexture(state, b, pos, false);
                }
                c = b.getMaterialColor().colorValue;
                Minimap.textureColours.put(name, c);
                System.out.println("Block file not found: ");
            } catch (Exception e2) {
                c = b.getMaterialColor().colorValue;
                if (name != null) {
                    Minimap.textureColours.put(name, c);
                }
                System.out.println("Block " + " has no texture, using material colour.");
            }
            Minimap.blockColours.put(stateId, c);
        }
        final int grassColor = getGrassColor(Minecraft.getInstance().world, pos);
        if (grassColor != 16777215) {
            final float rMultiplier = (c >> 16 & 0xFF) / 255.0f;
            final float gMultiplier = (c >> 8 & 0xFF) / 255.0f;
            final float bMultiplier = (c & 0xFF) / 255.0f;
            red = (int)((grassColor >> 16 & 0xFF) * rMultiplier);
            green = (int)((grassColor >> 8 & 0xFF) * gMultiplier);
            blue = (int)((grassColor & 0xFF) * bMultiplier);
            c = (0xFF000000 | red << 16 | green << 8 | blue);
        }
        return c;
    }
    
    public boolean applyTransparentBlock(final Chunk bchunk, final Block b, final BlockState state, final BlockPos globalPos, final BlockPos pos) {
        int red = 0;
        int green = 0;
        int blue = 0;
        int intensity = 1;
        boolean skip = false;
        if (b instanceof FlowingFluidBlock) {
            switch (XaeroMinimap.getSettings().blockColours) {
                case 1: {
                    red = 0;
                    green = 100;
                    blue = 225;
                    break;
                }
                case 0: {
                    final int waterColor = loadBlockColourFromTexture(state, b, globalPos, true);
                    red = (waterColor >> 16 & 0xFF);
                    green = (waterColor >> 8 & 0xFF);
                    blue = (waterColor & 0xFF);
                    break;
                }
            }
            intensity = 2;
            skip = true;
        }
        else if (XaeroMinimap.getSettings().blockColours == 0) {
            final int glassColor = loadBlockColourFromTexture(state, b, globalPos, true);
            red = (glassColor >> 16 & 0xFF);
            green = (glassColor >> 8 & 0xFF);
            blue = (glassColor & 0xFF);
            skip = true;
        }
        else if (XaeroMinimap.getSettings().blockColours == 1) {
            final int color = bchunk.getBlockState(pos).getMaterialColor(null, null).colorValue;
            red = (color >> 16 & 0xFF);
            green = (color >> 8 & 0xFF);
            blue = (color & 0xFF);
            skip = true;
        }
        if (skip) {
            if (this.previousTransparentBlock == null) {
                this.postBrightness = this.getBlockBrightness(bchunk, new BlockPos(pos.getX(), Math.min(pos.getY() + 1, 255), pos.getZ()), 5.0f, this.sun, true);
            }
            final int rgb = red << 16 | green << 8 | blue;
            if (this.previousTransparentBlock == null || this.previousTransparentBlock != rgb) {
                this.previousTransparentBlock = rgb;
                final float overlayIntensity = intensity * this.getBlockBrightness(bchunk, new BlockPos(pos.getX(), Math.min(pos.getY() + 1, 255), pos.getZ()), 5.0f, this.sun, false);
                if (this.isGlowing(b)) {
                    final int[] colours = this.getBrightestColour(red, green, blue);
                    red = colours[0];
                    green = colours[1];
                    blue = colours[2];
                }
                this.divider += overlayIntensity;
                this.underRed += (int)(red * overlayIntensity);
                this.underGreen += (int)(green * overlayIntensity);
                this.underBlue += (int)(blue * overlayIntensity);
            }
            this.sun -= b.getAmbientOcclusionLightValue(b.getDefaultState(),null, null);
            if (this.sun < 0) {
                this.sun = 0;
            }
        }
        return skip;
    }
    
    public Block findBlock(final Chunk bchunk, final int insideX, final int insideZ, final int highY, final int lowY) {
        boolean underair = false;
        for (int i = highY; i >= lowY; --i) {
            final Block got = bchunk.getBlockState(new BlockPos(insideX, i, insideZ)).getBlock();
            if (!(got instanceof AirBlock) && (underair || this.loadingCaving == -1)) {
                if (got != Blocks.TORCH) {
                    if (got != Blocks.TALL_GRASS) {
                        if (!XaeroMinimap.getSettings().showFlowers) {
                            if (got instanceof FlowerBlock || got instanceof DoublePlantBlock) {
                                continue;
                            }
                            if (got instanceof DeadBushBlock) {
                                continue;
                            }
                        }
                        if (!XaeroMinimap.getSettings().displayRedstone) {
                            continue;
                        }
                        this.blockY = i;
                        int color = 0;
                        final BlockPos pos = new BlockPos(insideX, this.blockY, insideZ);
                        final BlockPos globalPos = this.getGlobalBlockPos(bchunk.getPos().x, bchunk.getPos().z, insideX, this.blockY, insideZ);
                        BlockState state = bchunk.getBlockState(pos);
                        if (!this.applyTransparentBlock(bchunk, got, state, globalPos, pos)) {
                            if (XaeroMinimap.getSettings().blockColours == 1) {
                                color = got.getMaterialColor().colorValue;
                            } else {
                                color = loadBlockColourFromTexture(state, got, globalPos, true);
                            }
                            if (color != 0) {
                                this.blockColor = color;
                                return got;
                            }
                        }
                    }
                }

            } else if (got instanceof AirBlock) {
                underair = true;
            }
        }
        return null;
    }
    
    public BlockPos getGlobalBlockPos(final int chunkX, final int chunkZ, final int x, final int y, final int z) {
        return new BlockPos(chunkX * 16 + x, y, chunkZ * 16 + z);
    }
    
    public float getBlockBrightness(final Chunk c, final BlockPos pos, final float min, final int sun, final boolean dayLight) {
        if (!XaeroMinimap.getSettings().lighting) {
            return (min + sun) / (15.0f + min);
        }
        return (min + Math.max((dayLight ? Minimap.sunBrightness : 1.0f), 1)) / (15.0f + min);
    }
    
    public int[] getBrightestColour(int r, int g, int b) {
        final int max = Math.max(r, Math.max(g, b));
        if (max == 0) {
            return new int[] { r, g, b };
        }
        r = 255 * r / max;
        g = 255 * g / max;
        b = 255 * b / max;
        return new int[] { r, g, b };
    }
    
    public boolean isGlowing(final Block b) {
        return false;
    }
    
    public void loadBlockColor(final int par1, final int par2, final Chunk bchunk, final int chunkX, final int chunkZ) {
        final int insideX = par1 & 0xF;
        final int insideZ = par2 & 0xF;
        final int playerY = (int)Minimap.mc.player.getPosY();
        final int height = bchunk.getHeight();
        final int highY = (this.loadingCaving != -1) ? this.loadingCaving : (height + 3);
        int lowY = (this.loadingCaving != -1) ? (playerY - 30) : 0;
        if (lowY < 0) {
            lowY = 0;
        }
        this.blockY = 0;
        this.blockColor = 0;
        this.underRed = 0;
        this.underGreen = 0;
        this.underBlue = 0;
        this.divider = 1.0f;
        this.sun = 15;
        this.previousTransparentBlock = null;
        this.postBrightness = 1.0f;
        this.isglowing = false;
        final Block block = this.findBlock(bchunk, insideX, insideZ, highY, lowY);
        this.isglowing = (block != null && !(block instanceof OreBlock) && this.isGlowing(block));
        float brightness = 1.0f;
        if (!XaeroMinimap.getSettings().lighting && this.loadingCaving != -1) {
            brightness *= (float)Math.min(this.blockY / height, 1.0);
        }
        else {
            final BlockPos pos = new BlockPos(insideX, Math.min(this.blockY + 1, 255), insideZ);
            brightness = this.getBlockBrightness(bchunk, pos, 5.0f, this.sun, this.previousTransparentBlock == null);
        }
        double secondaryB = 1.0;
        if (this.lastBlockY[insideX] <= 0) {
            this.lastBlockY[insideX] = this.blockY;
            final Chunk prevChunk = Minimap.mc.world.getChunk(this.loadingMapX + chunkX, this.loadingMapZ + chunkZ - 1);
            if (prevChunk.isEmpty()) {
                this.lastBlockY[insideX] = prevChunk.getHeight() - 1;
            }
        }
        if (this.blockY < this.lastBlockY[insideX]) {
            secondaryB -= 0.15;
        }
        if (this.blockY > this.lastBlockY[insideX]) {
            secondaryB += 0.15;
        }
        brightness *= (float)secondaryB;
        this.lastBlockY[insideX] = this.blockY;
        if (this.blockColor == 0) {
            this.blockColor = 1;
        }
        int l = this.blockColor >> 16 & 0xFF;
        int i1 = this.blockColor >> 8 & 0xFF;
        int j1 = this.blockColor & 0xFF;
        if (this.isglowing) {
            final int[] colours = this.getBrightestColour(l, i1, j1);
            l = colours[0];
            i1 = colours[1];
            j1 = colours[2];
        }
        l = (int)((l * brightness + this.underRed) / this.divider * this.postBrightness);
        if (l > 255) {
            l = 255;
        }
        i1 = (int)((i1 * brightness + this.underGreen) / this.divider * this.postBrightness);
        if (i1 > 255) {
            i1 = 255;
        }
        j1 = (int)((j1 * brightness + this.underBlue) / this.divider * this.postBrightness);
        if (j1 > 255) {
            j1 = 255;
        }
        this.blockColor = (0xFF000000 | l << 16 | i1 << 8 | j1);
        if (chunkX < 0 || chunkX >= Minimap.loadingSide || chunkZ < 0 || chunkZ >= Minimap.loadingSide) {
            return;
        }
        MinimapChunk[] chunkmap = this.loadingBlocks[chunkX];
        if (chunkmap == null) {
            chunkmap = new MinimapChunk[Minimap.loadingSide];
            this.loadingBlocks[chunkX] = chunkmap;
        }
        MinimapChunk chunk = chunkmap[chunkZ];
        if (chunk == null) {
            chunk = new MinimapChunk(bchunk.getPos().x, bchunk.getPos().z);
            chunkmap[chunkZ] = chunk;
        }
        chunk.colors[insideX][insideZ] = this.blockColor;
    }
    
    public int getMapCoord(final int side, final double coord) {
        return (myFloor(coord) >> 4) - side / 2;
    }
    
    public int getLoadedBlockColor(final int par1, final int par2) {
        final int cX = (par1 >> 4) - this.loadedMapX;
        final int cZ = (par2 >> 4) - this.loadedMapZ;
        if (cX < 0 || cX >= Minimap.loadedSide || cZ < 0 || cZ >= Minimap.loadedSide) {
            return 1;
        }
        final MinimapChunk current = this.currentBlocks[cX][cZ];
        if (current != null) {
            return this.chunkOverlay(current.colors[par1 & 0xF][par2 & 0xF], current);
        }
        return 1;
    }
    
    public MinimapChunk[] getLoadedYChunks(final int par1) {
        final int cX = (par1 >> 4) - this.loadedMapX;
        if (cX < 0 || cX >= Minimap.loadedSide) {
            return null;
        }
        return this.currentBlocks[cX];
    }
    
    public int getLoadedBlockColor(final MinimapChunk[] yChunks, final int par1, final int par2) {
        final int cZ = (par2 >> 4) - this.loadedMapZ;
        if (cZ < 0 || cZ >= Minimap.loadedSide) {
            return 1;
        }
        final MinimapChunk current = yChunks[cZ];
        if (current != null) {
            return this.chunkOverlay(current.colors[par1 & 0xF][par2 & 0xF], current);
        }
        return 1;
    }
    
    public int gridOverlay(final int color) {
        final int grid = ModSettings.COLORS[XaeroMinimap.getSettings().chunkGrid];
        final int r = ((color >> 16 & 0xFF) * 3 + (grid >> 16 & 0xFF)) / 4;
        final int g = ((color >> 8 & 0xFF) * 3 + (grid >> 8 & 0xFF)) / 4;
        final int b = ((color & 0xFF) * 3 + (grid & 0xFF)) / 4;
        return 0xFF000000 | r << 16 | g << 8 | b;
    }
    
    public int slimeOverlay(final int color) {
        final int r = ((color >> 16 & 0xFF) + 82) / 2;
        final int g = ((color >> 8 & 0xFF) + 241) / 2;
        final int b = ((color & 0xFF) + 64) / 2;
        return 0xFF000000 | r << 16 | g << 8 | b;
    }
    
    public int chunkOverlay(final int color, final MinimapChunk c) {
        if (XaeroMinimap.getSettings().getSlimeChunks() && c.slimeChunk) {
            return this.slimeOverlay(color);
        }
        if (XaeroMinimap.getSettings().chunkGrid > -1 && c.chunkGrid) {
            return this.gridOverlay(color);
        }
        return color;
    }
    
    public static double getRenderAngle() {
        if (XaeroMinimap.getSettings().getLockNorth()) {
            return 90.0;
        }
        return getActualAngle();
    }
    
    public static double getActualAngle() {
        double rotation = Minimap.mc.player.rotationYaw;
        if (rotation < 0.0 || rotation > 360.0) {
            rotation %= 360.0;
        }
        double angle = 270.0 - rotation;
        if (angle < 0.0 || angle > 360.0) {
            angle %= 360.0;
        }
        return angle;
    }
    
    public double getZoom() {
        return this.minimapZoom;
    }
    
    public void updateZoom() {
        double target = XaeroMinimap.getSettings().zooms[XaeroMinimap.getSettings().zoom] * ((this.loadedCaving != -1) ? 3.0f : 1.0f);
        if (target > XaeroMinimap.getSettings().zooms[XaeroMinimap.getSettings().zooms.length - 1]) {
            target = XaeroMinimap.getSettings().zooms[XaeroMinimap.getSettings().zooms.length - 1];
        }
        double off = target - this.minimapZoom;
        if (off > 0.01 || off < -0.01) {
            off = (float)Animation.animate(off, 0.8);
        }
        else {
            off = 0.0;
        }
        this.minimapZoom = target - off;
    }
    
    public static void resetImage() {
        Minimap.toResetImage = true;
    }
    
    public static int myFloor(double d) {
        if (d < 0.0) {
            --d;
        }
        return (int)d;
    }
    
    public int getMinimapWidth() {
    	//Jello.addChatMessage(""+(Minimap.enlargedMap ? 448 : Minimap.minimapSizes[XaeroMinimap.getSettings().getMinimapSize()]));
//    	if(true)
    		return 149;
    	//System.out.println(Minimap.minimapSizes[XaeroMinimap.getSettings().getMinimapSize()]);
//        return Minimap.enlargedMap ? 448 : Minimap.minimapSizes[XaeroMinimap.getSettings().getMinimapSize()];
    }
    
    public int getBufferSize() {
        return Minimap.enlargedMap ? 512 : Minimap.bufferSizes[XaeroMinimap.getSettings().getMinimapSize()];
    }
    
    public int getFBOBufferSize() {
        return Minimap.enlargedMap ? 512 : Minimap.FBOBufferSizes[XaeroMinimap.getSettings().getMinimapSize()];
    }
    
    public static boolean usingFBO() {
        return false;
    }
    
    public void updateMapFrame(final int bufferSize, final float partial) {
        if (Minimap.toResetImage || usingFBO()) {
            this.bytes = new byte[bufferSize * bufferSize * 3];
            Minimap.toResetImage = false;
        }
        final boolean motionBlur = Minecraft.debugFPS >= 35;
        final int increaseY = motionBlur ? 2 : 1;
        int mapW = 0;
        int mapH = 0;
        int halfW = 0;
        int halfH = 0;
        if (!usingFBO()) {
            mapW = (mapH = this.getMinimapWidth());
            halfW = (mapW + 1) / 2;
            halfH = (mapH + 1) / 2;
        }
        final double halfWZoomed = halfW / Minimap.zoom;
        final double halfHZoomed = halfH / Minimap.zoom;
        byte currentState = this.drawYState;
        final double angle = Math.toRadians(getRenderAngle());
        final double ps = Math.sin(3.141592653589793 - angle);
        final double pc = Math.cos(3.141592653589793 - angle);
        final double playerX = getEntityX(Minimap.mc.player, partial);
        final double playerZ = getEntityZ(Minimap.mc.player, partial);
        for (int currentX = 0; currentX <= mapW + 1; ++currentX) {
            final double currentXZoomed = currentX / Minimap.zoom;
            final double offx = currentXZoomed - halfWZoomed;
            final double psx = ps * offx;
            final double pcx = pc * offx;
            for (int currentY = motionBlur ? currentState : 0; currentY <= mapH + 1; currentY += increaseY) {
                final double offy = currentY / Minimap.zoom - halfHZoomed;
                int c = this.getLoadedBlockColor(myFloor(playerX + psx + pc * offy), myFloor(playerZ + ps * offy - pcx));
                this.putColor(this.bytes, currentX, currentY, (c == -16777216 || c == 1) ? 0xff8cafff : c, bufferSize);
            }
            currentState = (byte) ((currentState != 1) ? 1 : 0);
        }
        this.drawYState = (byte) ((this.drawYState != 1) ? 1 : 0);
        final ByteBuffer buffer = Minimap.mapTexture.getBuffer(bufferSize);
        buffer.put(this.bytes);
        buffer.flip();
    }

    private void putColor(final byte[] bytes, final int x, final int y, final int color, final int size) {
        int pixel = (y * size + x) * 3;
        bytes[pixel] = (byte)(color >> 16 & 0xFF);
        bytes[++pixel] = (byte)(color >> 8 & 0xFF);
        bytes[++pixel] = (byte)(color & 0xFF);
    }
    
    public static void loadFrameBuffer() {
        Minimap.scalingFrameBuffer = new Framebuffer(512, 512, false);
        Minimap.rotationFrameBuffer = new Framebuffer(512, 512, false);
        Minimap.loadedFBO = (Minimap.scalingFrameBuffer.framebufferObject != -1);

        Minimap.triedFBO = true;
    }
    public static double getEntityX(final Entity e, final float partial) {
        return e.lastTickPosX + (e.getPosX() - e.lastTickPosX) * partial;
    }

    public static double getEntityZ(final Entity e, final float partial) {
        return e.lastTickPosZ + (e.getPosZ() - e.lastTickPosZ) * partial;
    }
    
    public void renderFrameToFBO(final int bufferSize, final int viewW, final float sizeFix, final float partial, final boolean retryIfError) {
        Minimap.updatePause = true;
        final int chunkAmount = getLoadSide();
        final int mapW = chunkAmount * 16;
        final double playerX = getEntityX((Entity)Minimap.mc.player, partial);
        final double playerZ = getEntityZ((Entity)Minimap.mc.player, partial);
        final int xFloored = myFloor(playerX);
        final int zFloored = myFloor(playerZ);
        int offsetX = xFloored & 0xF;
        int offsetZ = zFloored & 0xF;
        final int mapX = this.getMapCoord(chunkAmount, playerX);
        final int mapZ = this.getMapCoord(chunkAmount, playerZ);
        final boolean zooming = (int)Minimap.zoom != Minimap.zoom;
        final ByteBuffer buffer = Minimap.mapTexture.getBuffer(bufferSize);
        if (mapX != Minimap.mapUpdateX || mapZ != Minimap.mapUpdateZ || zooming || !retryIfError) {
            if (!Minimap.frameIsUpdating) {
                Minimap.frameIsUpdating = true;
                this.updateMapFrame(bufferSize, partial);
                Minimap.frameIsUpdating = false;
                buffer.put(this.bytes);
                buffer.flip();
                Minimap.bufferSizeToUpdate = -1;
                Minimap.frameUpdateNeeded = false;
            }
            else {
                offsetX += 16 * (mapX - Minimap.mapUpdateX);
                offsetZ += 16 * (mapZ - Minimap.mapUpdateZ);
            }
        }
        Minimap.scalingFrameBuffer.bindFramebuffer(true);
        GL11.glClear(16640);
        GL11.glEnable(3553);
        RenderHelper.disableStandardItemLighting();
        try {
            bindTextureBuffer(buffer, bufferSize, bufferSize, Minimap.mapTexture.getGlTextureId());
        }
        catch (Exception e) {
            if (retryIfError) {
                System.out.println("Error when binding texture buffer. Retrying...");
                this.renderFrameToFBO(bufferSize, viewW, sizeFix, partial, false);
            }
            else {
                System.out.println("Error after retrying... :( Please report to Xaero96 on MinecraftForum of PlanetMinecraft!");
            }
        }
        long before = System.currentTimeMillis();
        if (!zooming) {
            GL11.glTexParameteri(3553, 10240, 9728);
        }
        else {
            GL11.glTexParameteri(3553, 10240, 9729);
        }
        GlStateManager.clear(256);
        GlStateManager.matrixMode(5889);
        GL11.glPushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0, 512.0, 512.0, 0.0, 1000.0, 3000.0);
        GlStateManager.matrixMode(5888);
        GL11.glPushMatrix();
        GlStateManager.loadIdentity();
        before = System.currentTimeMillis();
        double xInsidePixel = getEntityX((Entity)Minimap.mc.player, partial) - xFloored;
        if (xInsidePixel < 0.0) {
            ++xInsidePixel;
        }
        double zInsidePixel = getEntityZ((Entity)Minimap.mc.player, partial) - zFloored;
        if (zInsidePixel < 0.0) {
            ++zInsidePixel;
        }
        zInsidePixel = 1.0 - zInsidePixel;
        final float halfW = mapW / 2.0f;
        final float halfWView = viewW / 2.0f;
        final float angle = (float)(90.0 - getRenderAngle());
        GlStateManager.translate(256.0f, 256.0f, -2000.0f);
        //Jello.addChatMessage(Minimap.zoom+"");
        //Minimap.zoom = 1;
        GlStateManager.scale(Minimap.zoom, Minimap.zoom, 1.0);
        drawMyTexturedModalRect(-halfW - offsetX + 8.0f, -halfW - offsetZ + 7.0f, 0, 0, mapW + offsetX, mapW + offsetZ, bufferSize);
        Minimap.scalingFrameBuffer.unbindFramebuffer();
        Minimap.rotationFrameBuffer.bindFramebuffer(false);
        GL11.glClear(16640);
        Minimap.scalingFrameBuffer.bindFramebufferTexture();
        GlStateManager.loadIdentity();
        if (XaeroMinimap.getSettings().getAntiAliasing()) {
            GL11.glTexParameteri(3553, 10240, 9729);
            GL11.glTexParameteri(3553, 10241, 9729);
        }
        else {
            GL11.glTexParameteri(3553, 10240, 9728);
            GL11.glTexParameteri(3553, 10241, 9728);
        }
        GlStateManager.translate(halfWView + 0.5f, 511.5f - halfWView, -2000.0f);
        if (!XaeroMinimap.getSettings().getLockNorth()) {
            GL11.glRotatef(angle, 0.0f, 0.0f, 1.0f);
        }
        GL11.glPushMatrix();
        GlStateManager.translate(-xInsidePixel * Minimap.zoom, -zInsidePixel * Minimap.zoom, 0.0);
        GlStateManager.disableBlend();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, XaeroMinimap.getSettings().minimapOpacity / 100.0f);
        drawMyTexturedModalRect(-256.0f, -256.0f, 0, 0, 512.0f, 512.0f, 512.0f);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
        before = System.currentTimeMillis();
        //Minimap.mc.getTextureManager().bindTexture(InterfaceHandler.guiTextures);
        if (XaeroMinimap.getSettings().getSmoothDots()) {
            GL11.glTexParameteri(3553, 10240, 9729);
            GL11.glTexParameteri(3553, 10241, 9729);
        }
        else {
            GL11.glTexParameteri(3553, 10240, 9728);
            GL11.glTexParameteri(3553, 10241, 9728);
        }
        GlStateManager.enableBlend();
        GL14.glBlendFuncSeparate(770, 771, 1, 771);
        GL11.glTexParameteri(3553, 10240, 9728);
        GL11.glTexParameteri(3553, 10241, 9728);
        Minimap.rotationFrameBuffer.unbindFramebuffer();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableBlend();
        Minimap.updatePause = false;
        GlStateManager.matrixMode(5889);
        GL11.glPopMatrix();
        GlStateManager.matrixMode(5888);
        GL11.glPopMatrix();
    }

    private static void drawMyTexturedModalRect(final float x, final float y, final int textureX, final int textureY, final float width, final float height, final float factor) {
        final float f2;
        final float f = f2 = 1.0f / factor;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos((x + 0.0f), (y + height), 0.0).tex(((textureX) * f), ((textureY + height) * f2));
        worldrenderer.pos((x + width), (y + height), 0.0).tex(((textureX + width) * f), ((textureY + height) * f2));
        worldrenderer.pos((x + width), (y + 0.0f), 0.0).tex(((textureX + width) * f), ((textureY) * f2));
        worldrenderer.pos((x + 0.0f), (y + 0.0f), 0.0).tex(((textureX) * f), ((textureY) * f2));
        tessellator.draw();
    }
    
    public static void bindTextureBuffer(final ByteBuffer image, final int width, final int height, final int par0) {
        GL11.glBindTexture(3553, par0);
        GL11.glTexImage2D(3553, 0, 6407, width, height, 0, 6407, 5121, image);
    }
    
    public static boolean shouldRenderEntity(final Entity e) {
        return !e.isSneaking() && !e.isInvisible();
    }
    public class MapLoader implements Runnable
    {
        @Override
        public void run() {
            int updateChunkX = 0;
            int updateChunkZ = 0;
            while (true) {
                final long before = System.currentTimeMillis();
                boolean sleep = true;
                try {
                    if (XaeroMinimap.getSettings() == null || !XaeroMinimap.getSettings().getMinimap() || Minimap.mc.player == null || Minimap.mc.world == null
                    || SigmaNG.SigmaNG == null || !SigmaNG.SigmaNG.moduleManager.getModule(MiniMap.class).enabled) {
                        Thread.sleep(100L);
                    }
                    else {
                        final long before2 = System.currentTimeMillis();
                        if (updateChunkX == 0 && updateChunkZ == 0) {
                            if (Minimap.clearBlockColours) {
                                Minimap.clearBlockColours = false;
                                if (!Minimap.blockColours.isEmpty()) {
                                    Minimap.blockColours.clear();
                                    Minimap.textureColours.clear();
                                    System.out.println("Minimap block colour cache cleaned.");
                                }
                            }
                            Minimap.sunBrightness = Minimap.mc.world.getSunBrightness(1.0f);
                            Minimap.loadingSide = Minimap.getLoadSide();
                            Minimap.updateRadius = Minimap.getUpdateRadius();
                            Minimap.this.loadingMapX = Minimap.this.getMapCoord(Minimap.loadingSide, Minimap.mc.player.getPosX());
                            Minimap.this.loadingMapZ = Minimap.this.getMapCoord(Minimap.loadingSide, Minimap.mc.player.getPosZ());
                            Minimap.this.loadingCaving = 0;
                            Minimap.this.loadingBlocks = new MinimapChunk[Minimap.loadingSide][Minimap.loadingSide];
                        }
                        sleep = this.updateChunk(updateChunkX, updateChunkZ);
                        if (updateChunkX == Minimap.loadingSide - 1 && updateChunkZ == Minimap.loadingSide - 1) {
                            Minimap.this.currentBlocks = Minimap.this.loadingBlocks;
                            Minimap.loadedSide = Minimap.loadingSide;
                            Minimap.this.loadedMapX = Minimap.this.loadingMapX;
                            Minimap.this.loadedMapZ = Minimap.this.loadingMapZ;
                            Minimap.this.loadedCaving = Minimap.this.loadingCaving;
                            Minimap.frameUpdateNeeded = Minimap.usingFBO();
                        }
                        updateChunkZ = (updateChunkZ + 1) % Minimap.loadingSide;
                        if (updateChunkZ == 0) {
                            updateChunkX = (updateChunkX + 1) % Minimap.loadingSide;
                            Minimap.this.lastBlockY = new int[16];
                            final ArrayList<Entity> loadingPlayers = new ArrayList<Entity>();
                            final ArrayList<Entity> loadingHostile = new ArrayList<Entity>();
                            final ArrayList<Entity> loadingLiving = new ArrayList<Entity>();
                            final ArrayList<Entity> loadingItems = new ArrayList<Entity>();
                            final ArrayList<Entity> loadingEntities = new ArrayList<Entity>();
                            Minimap.loadedPlayers = loadingPlayers;
                            Minimap.loadedHostile = loadingHostile;
                            Minimap.loadedLiving = loadingLiving;
                            Minimap.loadedItems = loadingItems;
                            Minimap.loadedEntities = loadingEntities;
                        }
                        if (!Minimap.frameIsUpdating && Minimap.frameUpdateNeeded) {
                            Minimap.frameUpdateNeeded = false;
                            Minimap.frameIsUpdating = true;
                            Minimap.bufferSizeToUpdate = Minimap.this.getFBOBufferSize();
                            Minimap.this.updateMapFrame(Minimap.bufferSizeToUpdate, Minimap.frameUpdatePartialTicks);
                            Minimap.frameIsUpdating = false;
                        }
                        if (!Minimap.updatePause && Minimap.bufferSizeToUpdate != -1) {
                            final ByteBuffer buffer = Minimap.mapTexture.getBuffer(Minimap.bufferSizeToUpdate);
                            buffer.put(Minimap.this.bytes);
                            buffer.flip();
                            Minimap.bufferSizeToUpdate = -1;
                        }
                    }
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                    Minimap.frameIsUpdating = false;
                }
                final int passed = (int)(System.currentTimeMillis() - before);
                try {
                    if (sleep && passed <= 5) {
                        Thread.sleep(5 - passed);
                    }
                    else {
                        Thread.sleep(1L);
                    }
                }
                catch (InterruptedException e3) {
                    e3.printStackTrace();
                }
            }
        }
        
        public boolean updateChunk(final int x, final int z) {
            final int chunkX = Minimap.this.loadingMapX + x;
            final int chunkZ = Minimap.this.loadingMapZ + z;
            final int halfSide = Minimap.loadingSide / 2;
            final int fromCenterX = x - halfSide;
            final int fromCenterZ = z - halfSide;
            final int xOld = chunkX - Minimap.this.loadedMapX;
            final int zOld = chunkZ - Minimap.this.loadedMapZ;
            MinimapChunk current = null;
            if (Minimap.this.currentBlocks != null && xOld > -1 && xOld < Minimap.this.currentBlocks.length && zOld > -1 && zOld < Minimap.this.currentBlocks.length) {
                current = Minimap.this.currentBlocks[xOld][zOld];
            }
            final Chunk bchunk = Minimap.mc.world.getChunk(chunkX, chunkZ);
            if ((int)Minimap.zoom == Minimap.zoom && (!bchunk.isEmpty() || ((fromCenterX > Minimap.updateRadius || fromCenterZ > Minimap.updateRadius || fromCenterX < -Minimap.updateRadius || fromCenterZ < -Minimap.updateRadius) && current != null))) {
                if (current != null) {
                    Minimap.this.loadingBlocks[x][z] = current;
                    for (int i = 0; i < 16; ++i) {
                        Minimap.this.lastBlockY[i] = current.lastHeights[i];
                    }
                }
                else {
                    Minimap.this.lastBlockY = new int[16];
                }
                return false;
            }
            final int x2 = chunkX * 16;
            final int z2 = chunkZ * 16;
            for (int blockX = x2; blockX < x2 + 16; ++blockX) {
                for (int blockZ = z2; blockZ < z2 + 16; ++blockZ) {
                    Minimap.this.loadBlockColor(blockX, blockZ, bchunk, x, z);
                    if ((blockZ & 0xF) == 0xF) {
                        Minimap.this.loadingBlocks[x][z].lastHeights[blockX & 0xF] = Minimap.this.lastBlockY[blockX & 0xF];
                    }
                }
            }
            return true;
        }
    }
}
