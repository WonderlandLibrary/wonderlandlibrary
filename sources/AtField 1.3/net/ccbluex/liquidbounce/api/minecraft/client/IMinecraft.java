/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  net.minecraft.network.play.INetHandlerPlayClient
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.api.minecraft.client;

import java.io.File;
import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.IParticleManager;
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
import net.minecraft.network.play.INetHandlerPlayClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u00d2\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0005\bf\u0018\u00002\u00020\u0001J\u0012\u0010n\u001a\u00020o2\b\u0010p\u001a\u0004\u0018\u00010\u0003H&J\b\u0010q\u001a\u00020oH&J\b\u0010r\u001a\u00020oH&J\b\u0010s\u001a\u00020oH&R\u0014\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0014\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0012\u0010\n\u001a\u00020\u000bX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\rR\u0012\u0010\u000e\u001a\u00020\u000fX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u0012\u0010\u0012\u001a\u00020\u000fX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0013\u0010\u0011R\u0012\u0010\u0014\u001a\u00020\u000fX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0015\u0010\u0011R\u0012\u0010\u0016\u001a\u00020\u0017X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0018\u0010\u0019R\u0012\u0010\u001a\u001a\u00020\u001bX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001c\u0010\u001dR\u0012\u0010\u001e\u001a\u00020\u001fX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b \u0010!R\u0012\u0010\"\u001a\u00020#X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b$\u0010%R\u0012\u0010&\u001a\u00020'X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b(\u0010)R\u0012\u0010*\u001a\u00020+X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b*\u0010,R\u0012\u0010-\u001a\u00020+X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b-\u0010,R\u0012\u0010.\u001a\u00020/X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b0\u00101R\u0012\u00102\u001a\u000203X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b4\u00105R\u0014\u00106\u001a\u0004\u0018\u000107X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b8\u00109R\u0012\u0010:\u001a\u00020;X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b<\u0010=R\u0012\u0010>\u001a\u00020?X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b@\u0010AR\u0012\u0010B\u001a\u00020CX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\bD\u0010ER\u0012\u0010F\u001a\u00020GX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\bH\u0010IR\u001a\u0010J\u001a\u0004\u0018\u00010KX\u00a6\u000e\u00a2\u0006\f\u001a\u0004\bL\u0010M\"\u0004\bN\u0010OR\u0018\u0010P\u001a\u00020\u000fX\u00a6\u000e\u00a2\u0006\f\u001a\u0004\bQ\u0010\u0011\"\u0004\bR\u0010SR\u0018\u0010T\u001a\u00020UX\u00a6\u000e\u00a2\u0006\f\u001a\u0004\bV\u0010W\"\u0004\bX\u0010YR\u0012\u0010Z\u001a\u00020[X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\\\u0010]R\u0012\u0010^\u001a\u00020_X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b`\u0010aR\u0014\u0010b\u001a\u0004\u0018\u00010cX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\bd\u0010eR\u0014\u0010f\u001a\u0004\u0018\u00010gX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\bh\u0010iR\u0012\u0010j\u001a\u00020kX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\bl\u0010m\u00a8\u0006t"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/IMinecraft;", "", "currentScreen", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiScreen;", "getCurrentScreen", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiScreen;", "currentServerData", "Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IServerData;", "getCurrentServerData", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IServerData;", "dataDir", "Ljava/io/File;", "getDataDir", "()Ljava/io/File;", "debugFPS", "", "getDebugFPS", "()I", "displayHeight", "getDisplayHeight", "displayWidth", "getDisplayWidth", "effectRenderer", "Lnet/ccbluex/liquidbounce/api/minecraft/IParticleManager;", "getEffectRenderer", "()Lnet/ccbluex/liquidbounce/api/minecraft/IParticleManager;", "entityRenderer", "Lnet/ccbluex/liquidbounce/api/minecraft/client/renderer/IEntityRenderer;", "getEntityRenderer", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/renderer/IEntityRenderer;", "fontRendererObj", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IFontRenderer;", "getFontRendererObj", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IFontRenderer;", "framebuffer", "Lnet/ccbluex/liquidbounce/api/minecraft/client/shader/IFramebuffer;", "getFramebuffer", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/shader/IFramebuffer;", "gameSettings", "Lnet/ccbluex/liquidbounce/api/minecraft/client/settings/IGameSettings;", "getGameSettings", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/settings/IGameSettings;", "isFullScreen", "", "()Z", "isIntegratedServerRunning", "netHandler", "Lnet/ccbluex/liquidbounce/api/minecraft/client/network/IINetHandlerPlayClient;", "getNetHandler", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/network/IINetHandlerPlayClient;", "netHandler2", "Lnet/minecraft/network/play/INetHandlerPlayClient;", "getNetHandler2", "()Lnet/minecraft/network/play/INetHandlerPlayClient;", "objectMouseOver", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IMovingObjectPosition;", "getObjectMouseOver", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IMovingObjectPosition;", "playerController", "Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IPlayerControllerMP;", "getPlayerController", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IPlayerControllerMP;", "renderGlobal", "Lnet/ccbluex/liquidbounce/api/minecraft/client/renderer/IRenderGlobal;", "getRenderGlobal", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/renderer/IRenderGlobal;", "renderItem", "Lnet/ccbluex/liquidbounce/api/minecraft/client/render/entity/IRenderItem;", "getRenderItem", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/render/entity/IRenderItem;", "renderManager", "Lnet/ccbluex/liquidbounce/api/minecraft/renderer/entity/IRenderManager;", "getRenderManager", "()Lnet/ccbluex/liquidbounce/api/minecraft/renderer/entity/IRenderManager;", "renderViewEntity", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "getRenderViewEntity", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "setRenderViewEntity", "(Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;)V", "rightClickDelayTimer", "getRightClickDelayTimer", "setRightClickDelayTimer", "(I)V", "session", "Lnet/ccbluex/liquidbounce/api/minecraft/util/ISession;", "getSession", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/ISession;", "setSession", "(Lnet/ccbluex/liquidbounce/api/minecraft/util/ISession;)V", "soundHandler", "Lnet/ccbluex/liquidbounce/api/minecraft/client/audio/ISoundHandler;", "getSoundHandler", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/audio/ISoundHandler;", "textureManager", "Lnet/ccbluex/liquidbounce/api/minecraft/client/render/texture/ITextureManager;", "getTextureManager", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/render/texture/ITextureManager;", "thePlayer", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityPlayerSP;", "getThePlayer", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityPlayerSP;", "theWorld", "Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IWorldClient;", "getTheWorld", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IWorldClient;", "timer", "Lnet/ccbluex/liquidbounce/api/minecraft/util/ITimer;", "getTimer", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/ITimer;", "displayGuiScreen", "", "screen", "rightClickMouse", "shutdown", "toggleFullscreen", "AtField"})
public interface IMinecraft {
    public void setRenderViewEntity(@Nullable IEntity var1);

    @NotNull
    public IParticleManager getEffectRenderer();

    public int getDisplayHeight();

    @NotNull
    public IFontRenderer getFontRendererObj();

    public void setSession(@NotNull ISession var1);

    @Nullable
    public IMovingObjectPosition getObjectMouseOver();

    @NotNull
    public IRenderItem getRenderItem();

    public void displayGuiScreen(@Nullable IGuiScreen var1);

    @NotNull
    public ISoundHandler getSoundHandler();

    @NotNull
    public ITextureManager getTextureManager();

    @NotNull
    public IRenderGlobal getRenderGlobal();

    @Nullable
    public IServerData getCurrentServerData();

    public int getDebugFPS();

    @NotNull
    public IINetHandlerPlayClient getNetHandler();

    @NotNull
    public INetHandlerPlayClient getNetHandler2();

    public void shutdown();

    public void setRightClickDelayTimer(int var1);

    @NotNull
    public IEntityRenderer getEntityRenderer();

    @Nullable
    public IWorldClient getTheWorld();

    @NotNull
    public IPlayerControllerMP getPlayerController();

    public void rightClickMouse();

    @NotNull
    public IRenderManager getRenderManager();

    @NotNull
    public IGameSettings getGameSettings();

    public void toggleFullscreen();

    @NotNull
    public IFramebuffer getFramebuffer();

    @Nullable
    public IEntityPlayerSP getThePlayer();

    @Nullable
    public IEntity getRenderViewEntity();

    @Nullable
    public IGuiScreen getCurrentScreen();

    public int getRightClickDelayTimer();

    public boolean isFullScreen();

    public boolean isIntegratedServerRunning();

    @NotNull
    public ISession getSession();

    @NotNull
    public File getDataDir();

    @NotNull
    public ITimer getTimer();

    public int getDisplayWidth();
}

