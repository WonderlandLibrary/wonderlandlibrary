// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import com.elementars.eclient.util.Helper;
import com.elementars.eclient.event.events.UnloadChunkEvent;
import org.apache.commons.lang3.SystemUtils;
import net.minecraft.client.Minecraft;
import java.io.IOException;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.File;
import java.nio.file.Path;
import com.elementars.eclient.command.Command;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import com.elementars.eclient.event.EventTarget;
import com.elementars.eclient.event.events.ChunkEvent;
import java.util.Iterator;
import com.elementars.eclient.event.events.RenderEvent;
import org.lwjgl.opengl.GL11;
import java.util.Collection;
import java.util.Arrays;
import com.elementars.eclient.module.Category;
import net.minecraft.world.chunk.Chunk;
import java.util.ArrayList;
import java.io.PrintWriter;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class ChunkFinder extends Module
{
    private final Value<Integer> yOffset;
    private final Value<Boolean> relative;
    private final Value<Boolean> saveNewChunks;
    private final Value<String> saveOption;
    private final Value<Boolean> saveInRegionFolder;
    private final Value<Boolean> alsoSaveNormalCoords;
    private LastSetting lastSetting;
    private PrintWriter logWriter;
    static ArrayList<Chunk> chunks;
    private static boolean dirty;
    private int list;
    
    public ChunkFinder() {
        super("ChunkFinder", "Finds new chunks", 0, Category.RENDER, true);
        this.yOffset = this.register(new Value<Integer>("Y Offset", this, 0, 0, 256));
        this.relative = this.register(new Value<Boolean>("Relative", this, true));
        this.saveNewChunks = this.register(new Value<Boolean>("Save New Chunks", this, false));
        this.saveOption = this.register(new Value<String>("Save Option", this, "extraFolder", new ArrayList<String>(Arrays.asList("extraFolder", "liteLoaderWdl", "nhackWdl"))));
        this.saveInRegionFolder = this.register(new Value<Boolean>("In Region", this, false));
        this.alsoSaveNormalCoords = this.register(new Value<Boolean>("Save Normal Coords", this, false));
        this.lastSetting = new LastSetting();
        this.list = GL11.glGenLists(1);
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (ChunkFinder.dirty) {
            GL11.glNewList(this.list, 4864);
            GL11.glPushMatrix();
            GL11.glEnable(2848);
            GL11.glDisable(2929);
            GL11.glDisable(3553);
            GL11.glDepthMask(false);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(3042);
            GL11.glLineWidth(1.0f);
            for (final Chunk chunk : ChunkFinder.chunks) {
                final double posX = chunk.x * 16;
                final double posY = 0.0;
                final double posZ = chunk.z * 16;
                GL11.glColor3f(0.6f, 0.1f, 0.2f);
                GL11.glBegin(2);
                GL11.glVertex3d(posX, posY, posZ);
                GL11.glVertex3d(posX + 16.0, posY, posZ);
                GL11.glVertex3d(posX + 16.0, posY, posZ + 16.0);
                GL11.glVertex3d(posX, posY, posZ + 16.0);
                GL11.glVertex3d(posX, posY, posZ);
                GL11.glEnd();
            }
            GL11.glDisable(3042);
            GL11.glDepthMask(true);
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDisable(2848);
            GL11.glPopMatrix();
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glEndList();
            ChunkFinder.dirty = false;
        }
        final double x = ChunkFinder.mc.getRenderManager().renderPosX;
        final double y = this.relative.getValue() ? 0.0 : (-ChunkFinder.mc.getRenderManager().renderPosY);
        final double z = ChunkFinder.mc.getRenderManager().renderPosZ;
        GL11.glTranslated(-x, y + this.yOffset.getValue(), -z);
        GL11.glCallList(this.list);
        GL11.glTranslated(x, -(y + this.yOffset.getValue()), z);
    }
    
    @Override
    public void onDisable() {
        this.logWriterClose();
        ChunkFinder.chunks.clear();
    }
    
    @EventTarget
    public void onChunk(final ChunkEvent event) {
        if (!event.getPacket().isFullChunk()) {
            ChunkFinder.chunks.add(event.getChunk());
            ChunkFinder.dirty = true;
            if (this.saveNewChunks.getValue()) {
                this.saveNewChunk(event.getChunk());
            }
        }
    }
    
    public void saveNewChunk(final Chunk chunk) {
        this.saveNewChunk(this.testAndGetLogWriter(), this.getNewChunkInfo(chunk));
    }
    
    private String getNewChunkInfo(final Chunk chunk) {
        String rV = String.format("%d,%d,%d", System.currentTimeMillis(), chunk.x, chunk.z);
        if (this.alsoSaveNormalCoords.getValue()) {
            rV += String.format(",%d,%d", chunk.x * 16 + 8, chunk.z * 16 + 8);
        }
        return rV;
    }
    
    private PrintWriter testAndGetLogWriter() {
        if (this.lastSetting.testChangeAndUpdate()) {
            this.logWriterClose();
            this.logWriterOpen();
        }
        return this.logWriter;
    }
    
    private void logWriterOpen() {
        final String filepath = this.getPath().toString();
        try {
            this.logWriter = new PrintWriter(new BufferedWriter(new FileWriter(filepath, true)), true);
            String head = "timestamp,ChunkX,ChunkZ";
            if (this.alsoSaveNormalCoords.getValue()) {
                head += ",x coordinate,z coordinate";
            }
            this.logWriter.println(head);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.print("some exception happened when trying to start the logging -> " + e.getMessage());
            Command.sendChatMessage("onLogStart: " + e.getMessage());
        }
    }
    
    private Path getPath() {
        File file = null;
        final int dimension = ChunkFinder.mc.player.dimension;
        if (ChunkFinder.mc.isSingleplayer()) {
            try {
                file = ChunkFinder.mc.getIntegratedServer().getWorld(dimension).getChunkSaveLocation();
            }
            catch (Exception e) {
                e.printStackTrace();
                System.err.print("some exception happened when getting canonicalFile -> " + e.getMessage());
                Command.sendChatMessage("onGetPath: " + e.getMessage());
            }
            if (file.toPath().relativize(ChunkFinder.mc.gameDir.toPath()).getNameCount() != 2) {
                file = file.getParentFile();
            }
        }
        else {
            file = this.makeMultiplayerDirectory().toFile();
        }
        if (dimension != 0) {
            file = new File(file, "DIM" + dimension);
        }
        if (this.saveInRegionFolder.getValue()) {
            file = new File(file, "region");
        }
        file = new File(file, "newChunkLogs");
        final String date = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        file = new File(file, ChunkFinder.mc.getSession().getUsername() + "_" + date + ".csv");
        final Path rV = file.toPath();
        try {
            if (!Files.exists(rV, new LinkOption[0])) {
                Files.createDirectories(rV.getParent(), (FileAttribute<?>[])new FileAttribute[0]);
                Files.createFile(rV, (FileAttribute<?>[])new FileAttribute[0]);
            }
        }
        catch (IOException e2) {
            e2.printStackTrace();
            System.err.print("some exception happened when trying to make the file -> " + e2.getMessage());
            Command.sendChatMessage("onCreateFile: " + e2.getMessage());
        }
        return rV;
    }
    
    private Path makeMultiplayerDirectory() {
        File rV = Minecraft.getMinecraft().gameDir;
        if (this.saveOption.getValue().equalsIgnoreCase("liteLoaderWdl")) {
            final String folderName = ChunkFinder.mc.getCurrentServerData().serverName;
            rV = new File(rV, "saves");
            rV = new File(rV, folderName);
        }
        else if (this.saveOption.getValue().equalsIgnoreCase("nhackWdl")) {
            final String folderName = this.getNHackInetName();
            rV = new File(rV, "config");
            rV = new File(rV, "wdl-saves");
            rV = new File(rV, folderName);
            if (!rV.exists()) {
                Command.sendChatMessage("nhack wdl directory doesnt exist: " + folderName);
                Command.sendChatMessage("creating the directory now. It is recommended to update the ip");
            }
        }
        else {
            String folderName = ChunkFinder.mc.getCurrentServerData().serverName + "-" + ChunkFinder.mc.getCurrentServerData().serverIP;
            if (SystemUtils.IS_OS_WINDOWS) {
                folderName = folderName.replace(":", "_");
            }
            rV = new File(rV, "KAMI_NewChunks");
            rV = new File(rV, folderName);
        }
        return rV.toPath();
    }
    
    private String getNHackInetName() {
        String folderName = ChunkFinder.mc.getCurrentServerData().serverIP;
        if (SystemUtils.IS_OS_WINDOWS) {
            folderName = folderName.replace(":", "_");
        }
        if (this.hasNoPort(folderName)) {
            folderName += "_25565";
        }
        return folderName;
    }
    
    private boolean hasNoPort(final String ip) {
        if (!ip.contains("_")) {
            return true;
        }
        final String[] sp = ip.split("_");
        final String ending = sp[sp.length - 1];
        return !this.isInteger(ending);
    }
    
    private boolean isInteger(final String s) {
        try {
            Integer.parseInt(s);
        }
        catch (NumberFormatException | NullPointerException ex2) {
            final RuntimeException ex;
            final RuntimeException e = ex;
            return false;
        }
        return true;
    }
    
    private void logWriterClose() {
        if (this.logWriter != null) {
            this.logWriter.close();
            this.logWriter = null;
        }
    }
    
    private void saveNewChunk(final PrintWriter log, final String data) {
        log.println(data);
    }
    
    @EventTarget
    private void onUnload(final UnloadChunkEvent event) {
        ChunkFinder.dirty = ChunkFinder.chunks.remove(event.getChunk());
    }
    
    @Override
    public void destroy() {
        GL11.glDeleteLists(1, 1);
    }
    
    static {
        ChunkFinder.chunks = new ArrayList<Chunk>();
        ChunkFinder.dirty = true;
    }
    
    private enum SaveOption
    {
        extraFolder, 
        liteLoaderWdl, 
        nhackWdl;
    }
    
    private class LastSetting
    {
        String lastSaveOption;
        boolean lastInRegion;
        boolean lastSaveNormal;
        int dimension;
        String ip;
        
        public boolean testChangeAndUpdate() {
            if (this.testChange()) {
                this.update();
                return true;
            }
            return false;
        }
        
        public boolean testChange() {
            return !ChunkFinder.this.saveOption.getValue().equals(this.lastSaveOption) || ChunkFinder.this.saveInRegionFolder.getValue() != this.lastInRegion || ChunkFinder.this.alsoSaveNormalCoords.getValue() != this.lastSaveNormal || this.dimension != Helper.mc.player.dimension || !Helper.mc.getCurrentServerData().serverIP.equals(this.ip);
        }
        
        private void update() {
            this.lastSaveOption = ChunkFinder.this.saveOption.getValue();
            this.lastInRegion = ChunkFinder.this.saveInRegionFolder.getValue();
            this.lastSaveNormal = ChunkFinder.this.alsoSaveNormalCoords.getValue();
            this.dimension = Helper.mc.player.dimension;
            this.ip = Helper.mc.getCurrentServerData().serverIP;
        }
    }
}
