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
import net.ccbluex.liquidbounce.api.IParticleManager;
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
    public IFramebuffer getFramebuffer() {
        Framebuffer $this$wrap$iv = this.wrapped.func_147110_a();
        boolean $i$f$wrap = false;
        return new FramebufferImpl($this$wrap$iv);
    }

    @Override
    public boolean isFullScreen() {
        return this.wrapped.func_71372_G();
    }

    @Override
    public File getDataDir() {
        return this.wrapped.field_71412_D;
    }

    @Override
    public int getDebugFPS() {
        return Minecraft.func_175610_ah();
    }

    @Override
    public IRenderGlobal getRenderGlobal() {
        RenderGlobal $this$wrap$iv = this.wrapped.field_71438_f;
        boolean $i$f$wrap = false;
        return new RenderGlobalImpl($this$wrap$iv);
    }

    @Override
    public IRenderItem getRenderItem() {
        RenderItem $this$wrap$iv = this.wrapped.func_175599_af();
        boolean $i$f$wrap = false;
        return new RenderItemImpl($this$wrap$iv);
    }

    @Override
    public int getDisplayWidth() {
        return this.wrapped.field_71443_c;
    }

    @Override
    public int getDisplayHeight() {
        return this.wrapped.field_71440_d;
    }

    @Override
    public IEntityRenderer getEntityRenderer() {
        EntityRenderer $this$wrap$iv = this.wrapped.field_71460_t;
        boolean $i$f$wrap = false;
        return new EntityRendererImpl($this$wrap$iv);
    }

    @Override
    public int getRightClickDelayTimer() {
        return this.wrapped.field_71467_ac;
    }

    @Override
    public void setRightClickDelayTimer(int value) {
        this.wrapped.field_71467_ac = value;
    }

    @Override
    public ISession getSession() {
        Session $this$wrap$iv = this.wrapped.field_71449_j;
        boolean $i$f$wrap = false;
        return new SessionImpl($this$wrap$iv);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void setSession(ISession value) {
        void $this$unwrap$iv;
        Session session;
        ISession iSession = value;
        Minecraft minecraft = this.wrapped;
        boolean $i$f$unwrap = false;
        minecraft.field_71449_j = session = ((SessionImpl)$this$unwrap$iv).getWrapped();
    }

    @Override
    public ISoundHandler getSoundHandler() {
        SoundHandler $this$wrap$iv = this.wrapped.func_147118_V();
        boolean $i$f$wrap = false;
        return new SoundHandlerImpl($this$wrap$iv);
    }

    @Override
    public IMovingObjectPosition getObjectMouseOver() {
        IMovingObjectPosition iMovingObjectPosition;
        RayTraceResult rayTraceResult = this.wrapped.field_71476_x;
        if (rayTraceResult != null) {
            RayTraceResult $this$wrap$iv = rayTraceResult;
            boolean $i$f$wrap = false;
            iMovingObjectPosition = new MovingObjectPositionImpl($this$wrap$iv);
        } else {
            iMovingObjectPosition = null;
        }
        return iMovingObjectPosition;
    }

    @Override
    public ITimer getTimer() {
        Timer $this$wrap$iv = this.wrapped.field_71428_T;
        boolean $i$f$wrap = false;
        return new TimerImpl($this$wrap$iv);
    }

    @Override
    public IRenderManager getRenderManager() {
        RenderManager $this$wrap$iv = this.wrapped.func_175598_ae();
        boolean $i$f$wrap = false;
        return new RenderManagerImpl($this$wrap$iv);
    }

    @Override
    public IPlayerControllerMP getPlayerController() {
        PlayerControllerMP $this$wrap$iv = this.wrapped.field_71442_b;
        boolean $i$f$wrap = false;
        return new PlayerControllerMPImpl($this$wrap$iv);
    }

    @Override
    public IGuiScreen getCurrentScreen() {
        IGuiScreen iGuiScreen;
        GuiScreen guiScreen = this.wrapped.field_71462_r;
        if (guiScreen != null) {
            GuiScreen $this$wrap$iv = guiScreen;
            boolean $i$f$wrap = false;
            iGuiScreen = new GuiScreenImpl<GuiScreen>($this$wrap$iv);
        } else {
            iGuiScreen = null;
        }
        return iGuiScreen;
    }

    @Override
    public IEntity getRenderViewEntity() {
        IEntity iEntity;
        Entity entity = this.wrapped.func_175606_aa();
        if (entity != null) {
            Entity $this$wrap$iv = entity;
            boolean $i$f$wrap = false;
            iEntity = new EntityImpl<Entity>($this$wrap$iv);
        } else {
            iEntity = null;
        }
        return iEntity;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void setRenderViewEntity(@Nullable IEntity value) {
        Entity entity;
        Minecraft minecraft = this.wrapped;
        IEntity iEntity = value;
        if (iEntity != null) {
            void $this$unwrap$iv;
            IEntity iEntity2 = iEntity;
            Minecraft minecraft2 = minecraft;
            boolean $i$f$unwrap = false;
            void v2 = $this$unwrap$iv;
            if (v2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.EntityImpl<*>");
            }
            Object t = ((EntityImpl)v2).getWrapped();
            minecraft = minecraft2;
            entity = (Entity)t;
        } else {
            entity = null;
        }
        minecraft.func_175607_a(entity);
    }

    @Override
    public IINetHandlerPlayClient getNetHandler() {
        NetHandlerPlayClient netHandlerPlayClient = this.wrapped.func_147114_u();
        if (netHandlerPlayClient == null) {
            Intrinsics.throwNpe();
        }
        NetHandlerPlayClient $this$wrap$iv = netHandlerPlayClient;
        boolean $i$f$wrap = false;
        return new INetHandlerPlayClientImpl($this$wrap$iv);
    }

    @Override
    public IWorldClient getTheWorld() {
        IWorldClient iWorldClient;
        WorldClient worldClient = this.wrapped.field_71441_e;
        if (worldClient != null) {
            WorldClient $this$wrap$iv = worldClient;
            boolean $i$f$wrap = false;
            iWorldClient = new WorldClientImpl($this$wrap$iv);
        } else {
            iWorldClient = null;
        }
        return iWorldClient;
    }

    @Override
    public IEntityPlayerSP getThePlayer() {
        IEntityPlayerSP iEntityPlayerSP;
        EntityPlayerSP entityPlayerSP = this.wrapped.field_71439_g;
        if (entityPlayerSP != null) {
            EntityPlayerSP $this$wrap$iv = entityPlayerSP;
            boolean $i$f$wrap = false;
            iEntityPlayerSP = new EntityPlayerSPImpl<EntityPlayerSP>($this$wrap$iv);
        } else {
            iEntityPlayerSP = null;
        }
        return iEntityPlayerSP;
    }

    @Override
    public ITextureManager getTextureManager() {
        TextureManager $this$wrap$iv = this.wrapped.func_110434_K();
        boolean $i$f$wrap = false;
        return new TextureManagerImpl($this$wrap$iv);
    }

    @Override
    public boolean isIntegratedServerRunning() {
        return this.wrapped.func_71387_A();
    }

    @Override
    public IServerData getCurrentServerData() {
        IServerData iServerData;
        ServerData serverData = this.wrapped.func_147104_D();
        if (serverData != null) {
            ServerData $this$wrap$iv = serverData;
            boolean $i$f$wrap = false;
            iServerData = new ServerDataImpl($this$wrap$iv);
        } else {
            iServerData = null;
        }
        return iServerData;
    }

    @Override
    public IGameSettings getGameSettings() {
        return new GameSettingsImpl(this.wrapped.field_71474_y);
    }

    @Override
    public IFontRenderer getFontRendererObj() {
        FontRenderer $this$wrap$iv = this.wrapped.field_71466_p;
        boolean $i$f$wrap = false;
        return new FontRendererImpl($this$wrap$iv);
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
        NetHandlerPlayClient it = netHandlerPlayClient2;
        boolean bl3 = false;
        MinecraftImpl $this$unwrap$iv = this;
        boolean $i$f$unwrap = false;
        MinecraftImpl minecraftImpl = $this$unwrap$iv;
        if (minecraftImpl == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.MinecraftImpl");
        }
        minecraftImpl.getWrapped();
        return (INetHandlerPlayClient)netHandlerPlayClient2;
    }

    @Override
    public IParticleManager getEffectRenderer() {
        ParticleManager $this$wrap$iv = this.wrapped.field_71452_i;
        boolean $i$f$wrap = false;
        return new ParticleManagerImpl($this$wrap$iv);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void displayGuiScreen(@Nullable IGuiScreen screen) {
        GuiScreen guiScreen;
        Minecraft minecraft = this.wrapped;
        IGuiScreen iGuiScreen = screen;
        if (iGuiScreen != null) {
            void $this$unwrap$iv;
            IGuiScreen iGuiScreen2 = iGuiScreen;
            Minecraft minecraft2 = minecraft;
            boolean $i$f$unwrap = false;
            void v2 = $this$unwrap$iv;
            if (v2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.GuiScreenImpl<*>");
            }
            GuiScreen guiScreen2 = (GuiScreen)((GuiScreenImpl)v2).getWrapped();
            minecraft = minecraft2;
            guiScreen = guiScreen2;
        } else {
            guiScreen = null;
        }
        minecraft.func_147108_a(guiScreen);
    }

    @Override
    public void rightClickMouse() {
        this.wrapped.func_147121_ag();
    }

    @Override
    public void shutdown() {
        this.wrapped.func_71400_g();
    }

    @Override
    public void toggleFullscreen() {
        this.wrapped.func_71352_k();
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof MinecraftImpl && ((MinecraftImpl)other).wrapped.equals(this.wrapped);
    }

    public final Minecraft getWrapped() {
        return this.wrapped;
    }

    public MinecraftImpl(Minecraft wrapped) {
        this.wrapped = wrapped;
    }
}

