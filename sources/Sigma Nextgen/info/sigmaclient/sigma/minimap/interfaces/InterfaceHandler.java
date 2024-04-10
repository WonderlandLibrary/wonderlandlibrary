package info.sigmaclient.sigma.minimap.interfaces;

import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import info.sigmaclient.sigma.utils.render.rendermanagers.ScaledResolution;
import info.sigmaclient.sigma.modules.gui.MiniMap;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import info.sigmaclient.sigma.minimap.*;
import info.sigmaclient.sigma.minimap.minimap.*;
import info.sigmaclient.sigma.minimap.settings.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

public class InterfaceHandler
{
    public static int actionTimer;
    public static ArrayList<Preset> presets;
    public static ArrayList<Interface> list;
    public static final ResourceLocation invtextures;
    public static Minecraft mc;
    public static int selectedId;
    public static int draggingId;
    public static int draggingOffX;
    public static int draggingOffY;
    public static Minimap minimap;
    public static final ResourceLocation guiTextures;
    public static final ResourceLocation mapTextures;
    public static long lastFlip;
    public static final Color disabled;
    public static final Color enabled;
    public static final Color selected;
    
    public static void loadPresets() {
        new Preset("gui.xaero_preset_topleft", new int[][] { { 0, 0 }, { 0, 10000 }, { 0, 0 }, { 0, 36 }, { 0, 0 } }, new boolean[][] { { true, false }, { true, false }, { false, true }, { true, false }, { false, false } });
        new Preset("gui.xaero_preset_topright", new int[][] { { 0, 0 }, { 0, 135 }, { 120, 0 }, { 0, 50 }, { 0, 0 } }, new boolean[][] { { false, true }, { false, false }, { true, false }, { true, false }, { false, true } });
        new Preset("gui.xaero_preset_bottom_left", new int[][] { { 0, 0 }, { 0, 135 }, { 120, 0 }, { 0, 50 }, { 0, 10000 } }, new boolean[][] { { false, true }, { false, false }, { true, false }, { true, false }, { false, false } });
        new Preset("gui.xaero_preset_bottom_right", new int[][] { { 0, 0 }, { 0, 135 }, { 120, 0 }, { 0, 50 }, { 0, 10000 } }, new boolean[][] { { false, true }, { false, false }, { true, false }, { true, false }, { false, true } });
    }
    
    public static void load() {
        new Interface("", 0, 0, ModOptions.DEFAULT) {};
        InterfaceHandler.minimap = new Minimap(new Interface("gui.xaero_minimap", 128, 128, ModOptions.MINIMAP) {
            long lastFBOTry = 0L;
            int lastMinimapSize = 0;
            private ArrayList<String> underText = new ArrayList<String>();
            
            @Override
            public int getW(final int scale) {
                return this.getSize() / scale;
            }
            
            @Override
            public int getH(final int scale) {
                return this.getW(scale);
            }
            
            @Override
            public int getWC(final int scale) {
                return this.getW(scale);
            }
            
            @Override
            public int getHC(final int scale) {
                return this.getH(scale);
            }
            
            @Override
            public int getW0(final int scale) {
                return this.getW(scale);
            }
            
            @Override
            public int getH0(final int scale) {
                return this.getH(scale);
            }
            
            @Override
            public int getSize() {
                return InterfaceHandler.minimap.getMinimapWidth() + 36 + 2;
            }
            
            public void translatePosition(final int specW, final int specH, final double ps, final double pc, final double offx, final double offy) {
                final double Y = (pc * offx + ps * offy) * Minimap.zoom;
                double borderedX;
                final double X = borderedX = (ps * offx - pc * offy) * Minimap.zoom;
                double borderedY = Y;
                if (borderedX > specW) {
                    borderedX = specW;
                    borderedY = Y * specW / X;
                }
                else if (borderedX < -specW) {
                    borderedX = -specW;
                    borderedY = -Y * specW / X;
                }
                if (borderedY > specH) {
                    borderedY = specH;
                    borderedX = X * specH / Y;
                }
                else if (borderedY < -specH) {
                    borderedY = -specH;
                    borderedX = -X * specH / Y;
                }
                GL11.glPushMatrix();
                GlStateManager.translate(borderedX, borderedY, 0.0);
            }
            
            @Override
            public void drawInterface(final int width, final int height, final int scale, final float partial) {
//                if (Minimap.loadedFBO && !OpenGlHelper.isFramebufferEnabled()) {
//                    Minimap.loadedFBO = false;
//                    Minimap.scalingFrameBuffer.deleteFramebuffer();
//                    Minimap.rotationFrameBuffer.deleteFramebuffer();
//                    Minimap.resetImage();
//                }
                if (!Minimap.loadedFBO && !XaeroMinimap.getSettings().mapSafeMode && System.currentTimeMillis() - this.lastFBOTry > 1000L) {
                    this.lastFBOTry = System.currentTimeMillis();
                    Minimap.loadFrameBuffer();
                }
                if (XaeroMinimap.getSettings().getMinimapSize() != this.lastMinimapSize) {
                    this.lastMinimapSize = XaeroMinimap.getSettings().getMinimapSize();
                    Minimap.resetImage();
                    Minimap.frameUpdateNeeded = Minimap.usingFBO();
                }
                final long before = System.currentTimeMillis();
                int bufferSize;
                if (Minimap.usingFBO()) {
                    bufferSize = InterfaceHandler.minimap.getFBOBufferSize();
                }
                else {
                    bufferSize = InterfaceHandler.minimap.getBufferSize();
                }
                final float mapScale = scale / 2.0f;
                final Minimap minimap = InterfaceHandler.minimap;
                final int minimapWidth = InterfaceHandler.minimap.getMinimapWidth();
                minimap.minimapWidth = minimapWidth;
                final int mapW = minimapWidth;
                final Minimap minimap2 = InterfaceHandler.minimap;
                final int minimapHeight = mapW;
                minimap2.minimapHeight = minimapHeight;
                final int mapH = minimapHeight;
                Minimap.frameUpdatePartialTicks = partial;
                InterfaceHandler.minimap.updateZoom();
                //Minimap.zoom = InterfaceHandler.minimap.getZoom();
                RenderHelper.disableStandardItemLighting();
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                float sizeFix = bufferSize / 512.0f;
                if (Minimap.usingFBO()) {
                    InterfaceHandler.minimap.renderFrameToFBO(bufferSize, mapW, sizeFix, partial, true);
                    Minecraft.getInstance().getFramebuffer().bindFramebuffer(true);
                    Minimap.rotationFrameBuffer.bindFramebufferTexture();
                    sizeFix = 1.0f;
                }
                else {
                    InterfaceHandler.minimap.updateMapFrame(bufferSize, partial);
                    GL11.glScalef(sizeFix, sizeFix, 1.0f);
                    Minimap.bindTextureBuffer(Minimap.mapTexture.getBuffer(bufferSize), bufferSize, bufferSize, Minimap.mapTexture.getGlTextureId());
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, XaeroMinimap.getSettings().minimapOpacity / 100.0f);
                }
                final int scaledX = (int)(this.x * mapScale);
                final int scaledY = (int)(this.y * mapScale);
                GL11.glEnable(3008);
                GlStateManager.enableAlpha();
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(770, 771);
                GL11.glScalef(1.0f / mapScale, 1.0f / mapScale, 1.0f);

                AbstractGui.drawModalRectWithCustomSizedTexture((int)((scaledX + 5) / sizeFix), (int)((scaledY + 9) / sizeFix) + MiniMap.offsetY, 0, 0, (int)((mapW / 2 + 1) / sizeFix), (int)((mapH / 2 + 1) / sizeFix), (int)((mapW / 2 + 1) / sizeFix), (int)((mapH / 2 + 1) / sizeFix));
                RenderUtils.drawShadow((int)((scaledX + 5) / sizeFix), (int)((scaledY + 9) / sizeFix) + MiniMap.offsetY, (int)((scaledX + 5) / sizeFix) + (int)((mapW / 2 + 1) / sizeFix), (int)((scaledY + 9) / sizeFix) + MiniMap.offsetY + (int)((mapH / 2 + 1) / sizeFix), 0.5f);
                RenderUtils.drawOutinShadow((int)((scaledX + 5) / sizeFix), (int)((scaledY + 9) / sizeFix) + MiniMap.offsetY, (int)((scaledX + 5) / sizeFix) + (int)((mapW / 2 + 1) / sizeFix), (int)((scaledY + 9) / sizeFix) + MiniMap.offsetY + (int)((mapH / 2 + 1) / sizeFix), 0.5f);
                if (!Minimap.usingFBO()) {
                    GL11.glScalef(1.0f / sizeFix, 1.0f / sizeFix, 1.0f);
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                }
//                GL11.glTexParameteri(3553, 10240, 9729);
//                GL11.glTexParameteri(3553, 10241, 9729);
//                final double arrowX = 2 * scaledX + 18 + (mapW + 1) / 2 + 0.5;
//                final double arrowY = 2 * scaledY + 18 + (mapH + 1) / 2 + 0.5;
//                GL11.glPushMatrix();
//                GL11.glScalef(0.5f, 0.5f, 1.0f);
//                this.drawArrow(arrowX, arrowY + 1.0, new float[] { 0.0f, 0.0f, 0.0f, 0.5f });
//                this.drawArrow(arrowX, arrowY, XaeroMinimap.getSettings().arrowColours[XaeroMinimap.getSettings().arrowColour]);
//                GL11.glPopMatrix();
                GL11.glTexParameteri(3553, 10240, 9728);
                GL11.glTexParameteri(3553, 10241, 9728);
                final int specH;
                final int specW = specH = mapW / 2 + 6;
                GL11.glPushMatrix();
                GlStateManager.scale(0.5f, 0.5f, 1.0f);
                GlStateManager.translate((float)(2 * scaledX + specW - 6 + 18), (float)(2 * scaledY + specH - 6 + 18), 0.0f);

                GL11.glPopMatrix();
                this.underText.clear();
                if (XaeroMinimap.getSettings().getShowCoords()) {
                    final int interfaceSize = this.getSize() / 2;
                    String coords = Minimap.myFloor(InterfaceHandler.mc.player.getPosX()) + ", " + Minimap.myFloor(InterfaceHandler.mc.player.getPosY()) + ", " + Minimap.myFloor(InterfaceHandler.mc.player.getPosZ());
                    if (InterfaceHandler.mc.fontRenderer.getStringWidth(coords) >= interfaceSize) {
                        final String stringLevel = "" + Minimap.myFloor(InterfaceHandler.mc.player.getPosY());
                        coords = Minimap.myFloor(InterfaceHandler.mc.player.getPosX()) + ", " + Minimap.myFloor(InterfaceHandler.mc.player.getPosZ());
                        this.underText.add(coords);
                        this.underText.add(stringLevel);
                    }
                    else {
                        this.underText.add(coords);
                    }
                }
//                if (XaeroMinimap.getSettings().showBiome) {
//                    final BlockPos pos = new BlockPos(InterfaceHandler.mc.getRenderViewEntity().posX, InterfaceHandler.mc.getRenderViewEntity().getEntityBoundingBox().minY, InterfaceHandler.mc.getRenderViewEntity().posZ);
//                    this.underText.add(InterfaceHandler.mc.world.getChunkFromBlockCoords(pos).getBiome(pos, InterfaceHandler.mc.world.provider.getBiomeProvider()).getBiomeName());
//                }
//                this.drawTextUnderMinimap(scaledX, scaledY, height, mapScale);
                GL11.glScalef(mapScale, mapScale, 1.0f);
                super.drawInterface(width, height, scale, partial);
            }
        });
    }
    
    public static void drawInterfaces(final float partial) {
        final ScaledResolution scaledresolution = new ScaledResolution(XaeroMinimap.mc);
        final int width = //427;//
        scaledresolution.getScaledWidth();
        final int height = //240;//
        		scaledresolution.getScaledHeight();
        final int scale = //2;//
        		scaledresolution.getScaleFactor();
       // updateInterfaces(mouseX, mouseY, width, height, scale);
        for (final Interface l : InterfaceHandler.list) {
            if (XaeroMinimap.settings.getBooleanValue(l.option)) {
            	l.drawInterface(width, height, scale, partial);
            }
        }
    }
    
   /* public static boolean overAButton(final int mouseX, final int mouseY) {
        if (InterfaceHandler.mc.currentScreen instanceof GuiEditMode) {
            for (int k = 0; k < ((GuiEditMode)InterfaceHandler.mc.currentScreen).getButtons().size(); ++k) {
                final GuiButton b = ((GuiEditMode)InterfaceHandler.mc.currentScreen).getButtons().get(k);
                if (mouseX >= b.xPosition && mouseY >= b.yPosition && mouseX < b.xPosition + 150 && mouseY < b.yPosition + 20) {
                    return true;
                }
            }
        }
        return false;
    }*/
    
    public static void confirm() {
        for (final Interface l : InterfaceHandler.list) {
            l.bx = l.actualx;
            l.by = l.actualy;
            l.bcentered = l.centered;
            l.bflipped = l.flipped;
            l.bfromRight = l.fromRight;
        }
    }
    
    public static void cancel() {
        for (final Interface l : InterfaceHandler.list) {
            l.actualx = l.bx;
            l.actualy = l.by;
            l.centered = l.bcentered;
            l.flipped = l.bflipped;
            l.fromRight = l.bfromRight;
        }
    }
    
    public static void applyPreset(final int id) {
        for (final Interface l : InterfaceHandler.list) {
            InterfaceHandler.actionTimer = 10;
            l.actualx = InterfaceHandler.presets.get(id).coords[l.id][0];
            l.actualy = InterfaceHandler.presets.get(id).coords[l.id][1];
            l.centered = InterfaceHandler.presets.get(id).types[l.id][0];
            l.flipped = l.cflipped;
            l.fromRight = InterfaceHandler.presets.get(id).types[l.id][1];
        }
    }
    
    /*public static void updateInterfaces(final int mouseX, final int mouseY, final int width, final int height, final int scale) {
        if (InterfaceHandler.actionTimer <= 0) {
            if (XaeroMinimap.events.lastGuiOpen instanceof GuiEditMode) {
                if (Mouse.isButtonDown(1)) {
                    InterfaceHandler.selectedId = -1;
                }
                int i = getInterfaceId(mouseX, mouseY, width, height, scale);
                if (i == -1) {
                    i = InterfaceHandler.selectedId;
                }
                if (i != -1) {
                    if (Mouse.isButtonDown(0) && InterfaceHandler.draggingId == -1) {
                        InterfaceHandler.draggingId = i;
                        InterfaceHandler.selectedId = i;
                        if (InterfaceHandler.list.get(i).fromRight) {
                            InterfaceHandler.list.get(i).x = width - InterfaceHandler.list.get(i).x;
                        }
                        InterfaceHandler.draggingOffX = InterfaceHandler.list.get(i).x - mouseX;
                        InterfaceHandler.draggingOffY = InterfaceHandler.list.get(i).y - mouseY;
                        if (InterfaceHandler.list.get(i).fromRight) {
                            InterfaceHandler.list.get(i).x = width - InterfaceHandler.list.get(i).x;
                        }
                    }
                    else if (!Mouse.isButtonDown(0) && InterfaceHandler.draggingId != -1) {
                        InterfaceHandler.draggingId = -1;
                        InterfaceHandler.draggingOffX = 0;
                        InterfaceHandler.draggingOffY = 0;
                    }
                    if (InterfaceHandler.selectedId != -1) {
                        i = InterfaceHandler.selectedId;
                    }
                    if (Keyboard.isKeyDown(33) && System.currentTimeMillis() - InterfaceHandler.lastFlip > 300L) {
                        InterfaceHandler.lastFlip = System.currentTimeMillis();
                        InterfaceHandler.list.get(i).flipped = !InterfaceHandler.list.get(i).flipped;
                    }
                    if (Keyboard.isKeyDown(46) && System.currentTimeMillis() - InterfaceHandler.lastFlip > 300L) {
                        InterfaceHandler.lastFlip = System.currentTimeMillis();
                        InterfaceHandler.list.get(i).centered = !InterfaceHandler.list.get(i).centered;
                    }
                    if (Keyboard.isKeyDown(31)) {
                        InterfaceHandler.selectedId = -1;
                        InterfaceHandler.draggingId = -1;
                        switch (i) {
                            case 4: {
                                InterfaceHandler.mc.currentScreen.mc.displayGuiScreen((GuiScreen)new GuiMinimap(InterfaceHandler.mc.currentScreen, XaeroMinimap.getSettings()));
                                break;
                            }
                        }
                    }
                }
                if (InterfaceHandler.draggingId != -1) {
                    if (!InterfaceHandler.list.get(InterfaceHandler.draggingId).centered) {
                        InterfaceHandler.list.get(InterfaceHandler.draggingId).actualx = mouseX + InterfaceHandler.draggingOffX;
                        if (InterfaceHandler.list.get(InterfaceHandler.draggingId).fromRight) {
                            InterfaceHandler.list.get(InterfaceHandler.draggingId).actualx = width - InterfaceHandler.list.get(InterfaceHandler.draggingId).actualx;
                        }
                    }
                    if (InterfaceHandler.list.get(InterfaceHandler.draggingId).actualx > width / 2) {
                        InterfaceHandler.list.get(InterfaceHandler.draggingId).fromRight = !InterfaceHandler.list.get(InterfaceHandler.draggingId).fromRight;
                        InterfaceHandler.list.get(InterfaceHandler.draggingId).actualx = width - InterfaceHandler.list.get(InterfaceHandler.draggingId).actualx;
                    }
                    InterfaceHandler.list.get(InterfaceHandler.draggingId).actualy = mouseY + InterfaceHandler.draggingOffY;
                }
            }
        }
        else {
            --InterfaceHandler.actionTimer;
        }
        for (final Interface j : InterfaceHandler.list) {
            j.x = j.actualx;
            j.y = j.actualy;
            if (j.fromRight) {
                j.x = width - j.x;
            }
            if (j.centered) {
                if (j.multi) {
                    j.w = j.getWC(scale);
                    j.h = j.getHC(scale);
                }
                j.x = width / 2 - j.getW(scale) / 2;
            }
            else if (j.multi) {
                j.w = j.getW0(scale);
                j.h = j.getH0(scale);
            }
            if (j.x < 5) {
                j.x = 0;
            }
            if (j.y < 5) {
                j.y = 0;
            }
            if (j.x + j.getW(scale) > width - 5) {
                j.x = width - j.getW(scale);
            }
            if (j.y + j.getH(scale) > height - 5) {
                j.y = height - j.getH(scale);
            }
        }
    }*/
    
   /* public static void drawBoxes(final int mouseX, final int mouseY, final int width, final int height, final int scale) {
        if (XaeroMinimap.events.lastGuiOpen instanceof GuiEditMode) {
            final int mouseOverId = getInterfaceId(mouseX, mouseY, width, height, scale);
            for (int i = 0; i < InterfaceHandler.list.size(); ++i) {
                if (XaeroMinimap.settings.getBooleanValue(InterfaceHandler.list.get(i).option)) {
                    int x = InterfaceHandler.list.get(i).x;
                    if (InterfaceHandler.list.get(i).fromRight) {
                        x = width - x;
                    }
                    final int y = InterfaceHandler.list.get(i).y;
                    final int w = InterfaceHandler.list.get(i).getW(scale);
                    final int h = InterfaceHandler.list.get(i).getH(scale);
                    final int x2 = x + w;
                    final int y2 = y + h;
                    if (InterfaceHandler.selectedId == i || (!overAButton(mouseX, mouseY) && mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2) || i == InterfaceHandler.draggingId) {
                        Gui.drawRect(x, y, x2, y2, (InterfaceHandler.selectedId == i) ? InterfaceHandler.selected.hashCode() : InterfaceHandler.enable.hashCode());
                        if (InterfaceHandler.draggingId == -1 && i == mouseOverId) {
                            InterfaceHandler.list.get(i).cBox.drawBox(mouseX, mouseY, width, height);
                        }
                    }
                    else {
                        Gui.drawRect(x, y, x2, y2, InterfaceHandler.disabled.hashCode());
                    }
                }
            }
        }
    }*/
    
    /*public static int getInterfaceId(final int mouseX, final int mouseY, final int width, final int height, final int scale) {
        int toReturn = -1;
        int size = 0;
        for (int i = 0; i < InterfaceHandler.list.size(); ++i) {
            int x = InterfaceHandler.list.get(i).x;
            if (InterfaceHandler.list.get(i).fromRight) {
                x = width - x;
            }
            final int y = InterfaceHandler.list.get(i).y;
            final int x2 = x + InterfaceHandler.list.get(i).getW(scale);
            final int y2 = y + InterfaceHandler.list.get(i).getH(scale);
            final int isize = InterfaceHandler.list.get(i).getSize();
            if ((size == 0 || isize < size) && !overAButton(mouseX, mouseY) && mouseX >= x && mouseX < x2 && mouseY >= y && mouseY < y2) {
                size = isize;
                toReturn = i;
            }
        }
        return toReturn;
    }*/
    
    static {
        InterfaceHandler.actionTimer = 0;
        InterfaceHandler.presets = new ArrayList<>();
        InterfaceHandler.list = new ArrayList<>();
        invtextures = new ResourceLocation("textures/gui/container/inventory.png");
        InterfaceHandler.mc = XaeroMinimap.mc;
        InterfaceHandler.selectedId = -1;
        InterfaceHandler.draggingId = -1;
        InterfaceHandler.draggingOffX = 0;
        InterfaceHandler.draggingOffY = 0;
        guiTextures = new ResourceLocation("xaerobetterpvp", "gui/guis.png");
        mapTextures = new ResourceLocation("xaeromaptexture");
        InterfaceHandler.lastFlip = 0L;
        disabled = new Color(189, 189, 189, 80);
        enabled = new Color(255, 255, 255, 100);
        selected = new Color(255, 255, 255, 130);
    }
}
