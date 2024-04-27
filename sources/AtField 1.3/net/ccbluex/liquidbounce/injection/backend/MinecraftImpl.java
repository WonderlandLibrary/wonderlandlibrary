/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.audio.SoundHandler
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.client.multiplayer.ServerData
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.client.particle.ParticleManager
 *  net.minecraft.client.renderer.EntityRenderer
 *  net.minecraft.client.renderer.RenderGlobal
 *  net.minecraft.client.renderer.RenderItem
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.client.shader.Framebuffer
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.play.INetHandlerPlayClient
 *  net.minecraft.util.Session
 *  net.minecraft.util.Timer
 *  net.minecraft.util.math.RayTraceResult
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import java.io.File;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.IParticleManager;
import net.ccbluex.liquidbounce.api.minecraft.client.IMinecraft;
import net.ccbluex.liquidbounce.api.minecraft.client.audio.ISoundHandler;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiScreen;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IPlayerControllerMP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IServerData;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.client.render.entity.IRenderItem;
import net.ccbluex.liquidbounce.api.minecraft.client.render.texture.ITextureManager;
import net.ccbluex.liquidbounce.api.minecraft.client.renderer.IEntityRenderer;
import net.ccbluex.liquidbounce.api.minecraft.client.renderer.IRenderGlobal;
import net.ccbluex.liquidbounce.api.minecraft.client.settings.IGameSettings;
import net.ccbluex.liquidbounce.api.minecraft.client.shader.IFramebuffer;
import net.ccbluex.liquidbounce.api.minecraft.renderer.entity.IRenderManager;
import net.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition;
import net.ccbluex.liquidbounce.api.minecraft.util.ISession;
import net.ccbluex.liquidbounce.api.minecraft.util.ITimer;
import net.ccbluex.liquidbounce.injection.backend.EntityImpl;
import net.ccbluex.liquidbounce.injection.backend.EntityPlayerSPImpl;
import net.ccbluex.liquidbounce.injection.backend.EntityRendererImpl;
import net.ccbluex.liquidbounce.injection.backend.FontRendererImpl;
import net.ccbluex.liquidbounce.injection.backend.FramebufferImpl;
import net.ccbluex.liquidbounce.injection.backend.GameSettingsImpl;
import net.ccbluex.liquidbounce.injection.backend.GuiScreenImpl;
import net.ccbluex.liquidbounce.injection.backend.INetHandlerPlayClientImpl;
import net.ccbluex.liquidbounce.injection.backend.MovingObjectPositionImpl;
import net.ccbluex.liquidbounce.injection.backend.ParticleManagerImpl;
import net.ccbluex.liquidbounce.injection.backend.PlayerControllerMPImpl;
import net.ccbluex.liquidbounce.injection.backend.RenderGlobalImpl;
import net.ccbluex.liquidbounce.injection.backend.RenderItemImpl;
import net.ccbluex.liquidbounce.injection.backend.RenderManagerImpl;
import net.ccbluex.liquidbounce.injection.backend.ServerDataImpl;
import net.ccbluex.liquidbounce.injection.backend.SessionImpl;
import net.ccbluex.liquidbounce.injection.backend.SoundHandlerImpl;
import net.ccbluex.liquidbounce.injection.backend.TextureManagerImpl;
import net.ccbluex.liquidbounce.injection.backend.TimerImpl;
import net.ccbluex.liquidbounce.injection.backend.WorldClientImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;
import net.minecraft.util.math.RayTraceResult;
import org.jetbrains.annotations.Nullable;

public final class MinecraftImpl
implements IMinecraft {
    private final Minecraft wrapped;

    @Override
    public ISoundHandler getSoundHandler() {
        SoundHandler soundHandler = this.wrapped.func_147118_V();
        boolean bl = false;
        return new SoundHandlerImpl(soundHandler);
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof MinecraftImpl && ((MinecraftImpl)object).wrapped.equals(this.wrapped);
    }

    @Override
    public void setRenderViewEntity(@Nullable IEntity iEntity) {
        Entity entity;
        Minecraft minecraft = this.wrapped;
        IEntity iEntity2 = iEntity;
        if (iEntity2 != null) {
            IEntity iEntity3 = iEntity2;
            Minecraft minecraft2 = minecraft;
            boolean bl = false;
            IEntity iEntity4 = iEntity3;
            if (iEntity4 == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.EntityImpl<*>");
            }
            Entity entity2 = ((EntityImpl)iEntity4).getWrapped();
            minecraft = minecraft2;
            entity = entity2;
        } else {
            entity = null;
        }
        minecraft.func_175607_a(entity);
    }

    @Override
    public IParticleManager getEffectRenderer() {
        ParticleManager particleManager = this.wrapped.field_71452_i;
        boolean bl = false;
        return new ParticleManagerImpl(particleManager);
    }

    @Override
    public IRenderGlobal getRenderGlobal() {
        RenderGlobal renderGlobal = this.wrapped.field_71438_f;
        boolean bl = false;
        return new RenderGlobalImpl(renderGlobal);
    }

    @Override
    public IPlayerControllerMP getPlayerController() {
        PlayerControllerMP playerControllerMP = this.wrapped.field_71442_b;
        boolean bl = false;
        return new PlayerControllerMPImpl(playerControllerMP);
    }

    @Override
    public ITextureManager getTextureManager() {
        TextureManager textureManager = this.wrapped.func_110434_K();
        boolean bl = false;
        return new TextureManagerImpl(textureManager);
    }

    @Override
    public IEntity getRenderViewEntity() {
        IEntity iEntity;
        Entity entity = this.wrapped.func_175606_aa();
        if (entity != null) {
            Entity entity2 = entity;
            boolean bl = false;
            iEntity = new EntityImpl(entity2);
        } else {
            iEntity = null;
        }
        return iEntity;
    }

    @Override
    public void setRightClickDelayTimer(int n) {
        this.wrapped.field_71467_ac = n;
    }

    @Override
    public File getDataDir() {
        return this.wrapped.field_71412_D;
    }

    public MinecraftImpl(Minecraft minecraft) {
        this.wrapped = minecraft;
    }

    @Override
    public IEntityPlayerSP getThePlayer() {
        IEntityPlayerSP iEntityPlayerSP;
        EntityPlayerSP entityPlayerSP = this.wrapped.field_71439_g;
        if (entityPlayerSP != null) {
            EntityPlayerSP entityPlayerSP2 = entityPlayerSP;
            boolean bl = false;
            iEntityPlayerSP = new EntityPlayerSPImpl(entityPlayerSP2);
        } else {
            iEntityPlayerSP = null;
        }
        return iEntityPlayerSP;
    }

    @Override
    public void toggleFullscreen() {
        this.wrapped.func_71352_k();
    }

    @Override
    public IRenderItem getRenderItem() {
        RenderItem renderItem = this.wrapped.func_175599_af();
        boolean bl = false;
        return new RenderItemImpl(renderItem);
    }

    @Override
    public int getRightClickDelayTimer() {
        return this.wrapped.field_71467_ac;
    }

    @Override
    public IMovingObjectPosition getObjectMouseOver() {
        IMovingObjectPosition iMovingObjectPosition;
        RayTraceResult rayTraceResult = this.wrapped.field_71476_x;
        if (rayTraceResult != null) {
            RayTraceResult rayTraceResult2 = rayTraceResult;
            boolean bl = false;
            iMovingObjectPosition = new MovingObjectPositionImpl(rayTraceResult2);
        } else {
            iMovingObjectPosition = null;
        }
        return iMovingObjectPosition;
    }

    @Override
    public ISession getSession() {
        Session session = this.wrapped.field_71449_j;
        boolean bl = false;
        return new SessionImpl(session);
    }

    @Override
    public IWorldClient getTheWorld() {
        IWorldClient iWorldClient;
        WorldClient worldClient = this.wrapped.field_71441_e;
        if (worldClient != null) {
            WorldClient worldClient2 = worldClient;
            boolean bl = false;
            iWorldClient = new WorldClientImpl(worldClient2);
        } else {
            iWorldClient = null;
        }
        return iWorldClient;
    }

    @Override
    public int getDisplayWidth() {
        return this.wrapped.field_71443_c;
    }

    @Override
    public void setSession(ISession iSession) {
        Session session;
        ISession iSession2 = iSession;
        Minecraft minecraft = this.wrapped;
        boolean bl = false;
        minecraft.field_71449_j = session = ((SessionImpl)iSession2).getWrapped();
    }

    @Override
    public boolean isIntegratedServerRunning() {
        return this.wrapped.func_71387_A();
    }

    @Override
    public IFramebuffer getFramebuffer() {
        Framebuffer framebuffer = this.wrapped.func_147110_a();
        boolean bl = false;
        return new FramebufferImpl(framebuffer);
    }

    @Override
    public void rightClickMouse() {
        this.wrapped.func_147121_ag();
    }

    @Override
    public int getDisplayHeight() {
        return this.wrapped.field_71440_d;
    }

    @Override
    public void displayGuiScreen(@Nullable IGuiScreen iGuiScreen) {
        GuiScreen guiScreen;
        Minecraft minecraft = this.wrapped;
        IGuiScreen iGuiScreen2 = iGuiScreen;
        if (iGuiScreen2 != null) {
            IGuiScreen iGuiScreen3 = iGuiScreen2;
            Minecraft minecraft2 = minecraft;
            boolean bl = false;
            IGuiScreen iGuiScreen4 = iGuiScreen3;
            if (iGuiScreen4 == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.GuiScreenImpl<*>");
            }
            GuiScreen guiScreen2 = (GuiScreen)((GuiScreenImpl)iGuiScreen4).getWrapped();
            minecraft = minecraft2;
            guiScreen = guiScreen2;
        } else {
            guiScreen = null;
        }
        minecraft.func_147108_a(guiScreen);
    }

    @Override
    public ITimer getTimer() {
        Timer timer = this.wrapped.field_71428_T;
        boolean bl = false;
        return new TimerImpl(timer);
    }

    @Override
    public IRenderManager getRenderManager() {
        RenderManager renderManager = this.wrapped.func_175598_ae();
        boolean bl = false;
        return new RenderManagerImpl(renderManager);
    }

    @Override
    public int getDebugFPS() {
        return Minecraft.func_175610_ah();
    }

    @Override
    public INetHandlerPlayClient getNetHandler2() {
        NetHandlerPlayClient netHandlerPlayClient = this.wrapped.func_147114_u();
        if (netHandlerPlayClient == null) {
            Intrinsics.throwNpe();
        }
        NetHandlerPlayClient netHandlerPlayClient2 = netHandlerPlayClient;
        boolean bl = false;
        boolean bl2 = false;
        NetHandlerPlayClient netHandlerPlayClient3 = netHandlerPlayClient2;
        boolean bl3 = false;
        MinecraftImpl minecraftImpl = this;
        boolean bl4 = false;
        MinecraftImpl minecraftImpl2 = minecraftImpl;
        if (minecraftImpl2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.MinecraftImpl");
        }
        minecraftImpl2.getWrapped();
        return (INetHandlerPlayClient)netHandlerPlayClient2;
    }

    @Override
    public IGuiScreen getCurrentScreen() {
        IGuiScreen iGuiScreen;
        GuiScreen guiScreen = this.wrapped.field_71462_r;
        if (guiScreen != null) {
            GuiScreen guiScreen2 = guiScreen;
            boolean bl = false;
            iGuiScreen = new GuiScreenImpl(guiScreen2);
        } else {
            iGuiScreen = null;
        }
        return iGuiScreen;
    }

    @Override
    public IServerData getCurrentServerData() {
        IServerData iServerData;
        ServerData serverData = this.wrapped.func_147104_D();
        if (serverData != null) {
            ServerData serverData2 = serverData;
            boolean bl = false;
            iServerData = new ServerDataImpl(serverData2);
        } else {
            iServerData = null;
        }
        return iServerData;
    }

    @Override
    public IINetHandlerPlayClient getNetHandler() {
        NetHandlerPlayClient netHandlerPlayClient = this.wrapped.func_147114_u();
        if (netHandlerPlayClient == null) {
            Intrinsics.throwNpe();
        }
        NetHandlerPlayClient netHandlerPlayClient2 = netHandlerPlayClient;
        boolean bl = false;
        return new INetHandlerPlayClientImpl(netHandlerPlayClient2);
    }

    public final Minecraft getWrapped() {
        return this.wrapped;
    }

    @Override
    public void shutdown() {
        this.wrapped.func_71400_g();
    }

    @Override
    public boolean isFullScreen() {
        return this.wrapped.func_71372_G();
    }

    @Override
    public IGameSettings getGameSettings() {
        return new GameSettingsImpl(this.wrapped.field_71474_y);
    }

    @Override
    public IEntityRenderer getEntityRenderer() {
        EntityRenderer entityRenderer = this.wrapped.field_71460_t;
        boolean bl = false;
        return new EntityRendererImpl(entityRenderer);
    }

    @Override
    public IFontRenderer getFontRendererObj() {
        FontRenderer fontRenderer = this.wrapped.field_71466_p;
        boolean bl = false;
        return new FontRendererImpl(fontRenderer);
    }
}

