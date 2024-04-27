/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
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
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.client.shader.Framebuffer
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.Session
 *  net.minecraft.util.Timer
 *  net.minecraft.util.math.RayTraceResult
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import java.io.File;
import kotlin.Metadata;
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
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;
import net.minecraft.util.math.RayTraceResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u00da\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0012\u0010p\u001a\u00020q2\b\u0010r\u001a\u0004\u0018\u00010\u0006H\u0016J\u0013\u0010s\u001a\u00020.2\b\u0010t\u001a\u0004\u0018\u00010uH\u0096\u0002J\b\u0010v\u001a\u00020qH\u0016J\b\u0010w\u001a\u00020qH\u0016J\b\u0010x\u001a\u00020qH\u0016R\u0016\u0010\u0005\u001a\u0004\u0018\u00010\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0016\u0010\t\u001a\u0004\u0018\u00010\n8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u0014\u0010\r\u001a\u00020\u000e8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u0014\u0010\u0011\u001a\u00020\u00128VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0013\u0010\u0014R\u0014\u0010\u0015\u001a\u00020\u00128VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0016\u0010\u0014R\u0014\u0010\u0017\u001a\u00020\u00128VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0018\u0010\u0014R\u0014\u0010\u0019\u001a\u00020\u001a8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001b\u0010\u001cR\u0014\u0010\u001d\u001a\u00020\u001e8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001f\u0010 R\u0014\u0010!\u001a\u00020\"8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b#\u0010$R\u0014\u0010%\u001a\u00020&8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b'\u0010(R\u0014\u0010)\u001a\u00020*8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b+\u0010,R\u0014\u0010-\u001a\u00020.8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b-\u0010/R\u0014\u00100\u001a\u00020.8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b0\u0010/R\u0014\u00101\u001a\u0002028VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b3\u00104R\u0016\u00105\u001a\u0004\u0018\u0001068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b7\u00108R\u0014\u00109\u001a\u00020:8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b;\u0010<R\u0014\u0010=\u001a\u00020>8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b?\u0010@R\u0014\u0010A\u001a\u00020B8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bC\u0010DR\u0014\u0010E\u001a\u00020F8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bG\u0010HR(\u0010K\u001a\u0004\u0018\u00010J2\b\u0010I\u001a\u0004\u0018\u00010J8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\bL\u0010M\"\u0004\bN\u0010OR$\u0010P\u001a\u00020\u00122\u0006\u0010I\u001a\u00020\u00128V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\bQ\u0010\u0014\"\u0004\bR\u0010SR$\u0010U\u001a\u00020T2\u0006\u0010I\u001a\u00020T8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\bV\u0010W\"\u0004\bX\u0010YR\u0014\u0010Z\u001a\u00020[8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\\\u0010]R\u0014\u0010^\u001a\u00020_8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b`\u0010aR\u0016\u0010b\u001a\u0004\u0018\u00010c8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bd\u0010eR\u0016\u0010f\u001a\u0004\u0018\u00010g8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bh\u0010iR\u0014\u0010j\u001a\u00020k8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bl\u0010mR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bn\u0010o\u00a8\u0006y"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/MinecraftImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/IMinecraft;", "wrapped", "Lnet/minecraft/client/Minecraft;", "(Lnet/minecraft/client/Minecraft;)V", "currentScreen", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiScreen;", "getCurrentScreen", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiScreen;", "currentServerData", "Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IServerData;", "getCurrentServerData", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IServerData;", "dataDir", "Ljava/io/File;", "getDataDir", "()Ljava/io/File;", "debugFPS", "", "getDebugFPS", "()I", "displayHeight", "getDisplayHeight", "displayWidth", "getDisplayWidth", "effectRenderer", "Lnet/ccbluex/liquidbounce/api/minecraft/IParticleManager;", "getEffectRenderer", "()Lnet/ccbluex/liquidbounce/api/minecraft/IParticleManager;", "entityRenderer", "Lnet/ccbluex/liquidbounce/api/minecraft/client/renderer/IEntityRenderer;", "getEntityRenderer", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/renderer/IEntityRenderer;", "fontRendererObj", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IFontRenderer;", "getFontRendererObj", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IFontRenderer;", "framebuffer", "Lnet/ccbluex/liquidbounce/api/minecraft/client/shader/IFramebuffer;", "getFramebuffer", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/shader/IFramebuffer;", "gameSettings", "Lnet/ccbluex/liquidbounce/api/minecraft/client/settings/IGameSettings;", "getGameSettings", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/settings/IGameSettings;", "isFullScreen", "", "()Z", "isIntegratedServerRunning", "netHandler", "Lnet/ccbluex/liquidbounce/api/minecraft/client/network/IINetHandlerPlayClient;", "getNetHandler", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/network/IINetHandlerPlayClient;", "objectMouseOver", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IMovingObjectPosition;", "getObjectMouseOver", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IMovingObjectPosition;", "playerController", "Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IPlayerControllerMP;", "getPlayerController", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IPlayerControllerMP;", "renderGlobal", "Lnet/ccbluex/liquidbounce/api/minecraft/client/renderer/IRenderGlobal;", "getRenderGlobal", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/renderer/IRenderGlobal;", "renderItem", "Lnet/ccbluex/liquidbounce/api/minecraft/client/render/entity/IRenderItem;", "getRenderItem", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/render/entity/IRenderItem;", "renderManager", "Lnet/ccbluex/liquidbounce/api/minecraft/renderer/entity/IRenderManager;", "getRenderManager", "()Lnet/ccbluex/liquidbounce/api/minecraft/renderer/entity/IRenderManager;", "value", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "renderViewEntity", "getRenderViewEntity", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "setRenderViewEntity", "(Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;)V", "rightClickDelayTimer", "getRightClickDelayTimer", "setRightClickDelayTimer", "(I)V", "Lnet/ccbluex/liquidbounce/api/minecraft/util/ISession;", "session", "getSession", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/ISession;", "setSession", "(Lnet/ccbluex/liquidbounce/api/minecraft/util/ISession;)V", "soundHandler", "Lnet/ccbluex/liquidbounce/api/minecraft/client/audio/ISoundHandler;", "getSoundHandler", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/audio/ISoundHandler;", "textureManager", "Lnet/ccbluex/liquidbounce/api/minecraft/client/render/texture/ITextureManager;", "getTextureManager", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/render/texture/ITextureManager;", "thePlayer", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityPlayerSP;", "getThePlayer", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityPlayerSP;", "theWorld", "Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IWorldClient;", "getTheWorld", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IWorldClient;", "timer", "Lnet/ccbluex/liquidbounce/api/minecraft/util/ITimer;", "getTimer", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/ITimer;", "getWrapped", "()Lnet/minecraft/client/Minecraft;", "displayGuiScreen", "", "screen", "equals", "other", "", "rightClickMouse", "shutdown", "toggleFullscreen", "LiKingSense"})
public final class MinecraftImpl
implements IMinecraft {
    @NotNull
    private final Minecraft wrapped;

    @Override
    @NotNull
    public IFramebuffer getFramebuffer() {
        Framebuffer framebuffer = this.wrapped.func_147110_a();
        Intrinsics.checkExpressionValueIsNotNull((Object)framebuffer, (String)"wrapped.framebuffer");
        Framebuffer $this$wrap$iv = framebuffer;
        boolean $i$f$wrap = false;
        return new FramebufferImpl($this$wrap$iv);
    }

    @Override
    public boolean isFullScreen() {
        return this.wrapped.func_71372_G();
    }

    @Override
    @NotNull
    public File getDataDir() {
        File file = this.wrapped.field_71412_D;
        Intrinsics.checkExpressionValueIsNotNull((Object)file, (String)"wrapped.mcDataDir");
        return file;
    }

    @Override
    public int getDebugFPS() {
        return Minecraft.func_175610_ah();
    }

    @Override
    @NotNull
    public IRenderGlobal getRenderGlobal() {
        RenderGlobal renderGlobal = this.wrapped.field_71438_f;
        Intrinsics.checkExpressionValueIsNotNull((Object)renderGlobal, (String)"wrapped.renderGlobal");
        RenderGlobal $this$wrap$iv = renderGlobal;
        boolean $i$f$wrap = false;
        return new RenderGlobalImpl($this$wrap$iv);
    }

    @Override
    @NotNull
    public IRenderItem getRenderItem() {
        RenderItem renderItem = this.wrapped.func_175599_af();
        Intrinsics.checkExpressionValueIsNotNull((Object)renderItem, (String)"wrapped.renderItem");
        RenderItem $this$wrap$iv = renderItem;
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
    @NotNull
    public IEntityRenderer getEntityRenderer() {
        EntityRenderer entityRenderer = this.wrapped.field_71460_t;
        Intrinsics.checkExpressionValueIsNotNull((Object)entityRenderer, (String)"wrapped.entityRenderer");
        EntityRenderer $this$wrap$iv = entityRenderer;
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
    @NotNull
    public ISession getSession() {
        Session session = this.wrapped.field_71449_j;
        Intrinsics.checkExpressionValueIsNotNull((Object)session, (String)"wrapped.session");
        Session $this$wrap$iv = session;
        boolean $i$f$wrap = false;
        return new SessionImpl($this$wrap$iv);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void setSession(@NotNull ISession value) {
        void $this$unwrap$iv;
        Session session;
        Intrinsics.checkParameterIsNotNull((Object)value, (String)"value");
        ISession iSession = value;
        Minecraft minecraft = this.wrapped;
        boolean $i$f$unwrap = false;
        minecraft.field_71449_j = session = ((SessionImpl)$this$unwrap$iv).getWrapped();
    }

    @Override
    @NotNull
    public ISoundHandler getSoundHandler() {
        SoundHandler soundHandler = this.wrapped.func_147118_V();
        Intrinsics.checkExpressionValueIsNotNull((Object)soundHandler, (String)"wrapped.soundHandler");
        SoundHandler $this$wrap$iv = soundHandler;
        boolean $i$f$wrap = false;
        return new SoundHandlerImpl($this$wrap$iv);
    }

    @Override
    @Nullable
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
    @NotNull
    public ITimer getTimer() {
        Timer timer = this.wrapped.field_71428_T;
        Intrinsics.checkExpressionValueIsNotNull((Object)timer, (String)"wrapped.timer");
        Timer $this$wrap$iv = timer;
        boolean $i$f$wrap = false;
        return new TimerImpl($this$wrap$iv);
    }

    @Override
    @NotNull
    public IRenderManager getRenderManager() {
        RenderManager renderManager = this.wrapped.func_175598_ae();
        Intrinsics.checkExpressionValueIsNotNull((Object)renderManager, (String)"wrapped.renderManager");
        RenderManager $this$wrap$iv = renderManager;
        boolean $i$f$wrap = false;
        return new RenderManagerImpl($this$wrap$iv);
    }

    @Override
    @NotNull
    public IPlayerControllerMP getPlayerController() {
        PlayerControllerMP playerControllerMP = this.wrapped.field_71442_b;
        Intrinsics.checkExpressionValueIsNotNull((Object)playerControllerMP, (String)"wrapped.playerController");
        PlayerControllerMP $this$wrap$iv = playerControllerMP;
        boolean $i$f$wrap = false;
        return new PlayerControllerMPImpl($this$wrap$iv);
    }

    @Override
    @Nullable
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
    @Nullable
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
            Object t2 = ((EntityImpl)v2).getWrapped();
            minecraft = minecraft2;
            entity = (Entity)t2;
        } else {
            entity = null;
        }
        minecraft.func_175607_a(entity);
    }

    @Override
    @NotNull
    public IINetHandlerPlayClient getNetHandler() {
        NetHandlerPlayClient netHandlerPlayClient = this.wrapped.func_147114_u();
        Intrinsics.checkExpressionValueIsNotNull((Object)netHandlerPlayClient, (String)"wrapped.connection!!");
        NetHandlerPlayClient $this$wrap$iv = netHandlerPlayClient;
        boolean $i$f$wrap = false;
        return new INetHandlerPlayClientImpl($this$wrap$iv);
    }

    @Override
    @Nullable
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
    @Nullable
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
    @NotNull
    public ITextureManager getTextureManager() {
        TextureManager textureManager = this.wrapped.func_110434_K();
        Intrinsics.checkExpressionValueIsNotNull((Object)textureManager, (String)"wrapped.textureManager");
        TextureManager $this$wrap$iv = textureManager;
        boolean $i$f$wrap = false;
        return new TextureManagerImpl($this$wrap$iv);
    }

    @Override
    public boolean isIntegratedServerRunning() {
        return this.wrapped.func_71387_A();
    }

    @Override
    @Nullable
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
    @NotNull
    public IGameSettings getGameSettings() {
        GameSettings gameSettings = this.wrapped.field_71474_y;
        Intrinsics.checkExpressionValueIsNotNull((Object)gameSettings, (String)"wrapped.gameSettings");
        return new GameSettingsImpl(gameSettings);
    }

    @Override
    @NotNull
    public IFontRenderer getFontRendererObj() {
        FontRenderer fontRenderer = this.wrapped.field_71466_p;
        Intrinsics.checkExpressionValueIsNotNull((Object)fontRenderer, (String)"wrapped.fontRenderer");
        FontRenderer $this$wrap$iv = fontRenderer;
        boolean $i$f$wrap = false;
        return new FontRendererImpl($this$wrap$iv);
    }

    @Override
    @NotNull
    public IParticleManager getEffectRenderer() {
        ParticleManager particleManager = this.wrapped.field_71452_i;
        Intrinsics.checkExpressionValueIsNotNull((Object)particleManager, (String)"wrapped.effectRenderer");
        ParticleManager $this$wrap$iv = particleManager;
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
        return other instanceof MinecraftImpl && Intrinsics.areEqual((Object)((MinecraftImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final Minecraft getWrapped() {
        return this.wrapped;
    }

    public MinecraftImpl(@NotNull Minecraft wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

