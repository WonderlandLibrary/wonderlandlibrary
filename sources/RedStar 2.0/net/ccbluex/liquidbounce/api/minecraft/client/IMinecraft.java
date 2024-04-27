package net.ccbluex.liquidbounce.api.minecraft.client;

import java.io.File;
import kotlin.Metadata;
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

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000Ê\n\n\u0000\n\u0000\n\n\b\n\n\b\n\n\b\n\b\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\t\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\bf\u000020Jj0k2\bl0H&J\bm0kH&J\bn0kH&J\bo0kH&R0X¦¢\bR0X¦¢\b\b\tR\n0X¦¢\b\f\rR0X¦¢\bR0X¦¢\bR0X¦¢\bR0X¦¢\bR0X¦¢\bR0X¦¢\b !R\"0#X¦¢\b$%R&0'X¦¢\b&(R)0'X¦¢\b)(R*0+X¦¢\b,-R.0/X¦¢\b01R203X¦¢\b45R607X¦¢\b89R:0;X¦¢\b<=R>0?X¦¢\b@ARB0CX¦¢\bDERF0GX¦¢\f\bHI\"\bJKRL0X¦¢\f\bM\"\bNORP0QX¦¢\f\bRS\"\bTURV0WX¦¢\bXYRZ0[X¦¢\b\\\u0010]R^0_X¦¢\b`aRb0cX¦¢\bdeRf0gX¦¢\bhi¨p"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/IMinecraft;", "", "currentScreen", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiScreen;", "getCurrentScreen", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiScreen;", "currentServerData", "Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IServerData;", "getCurrentServerData", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IServerData;", "dataDir", "Ljava/io/File;", "getDataDir", "()Ljava/io/File;", "debugFPS", "", "getDebugFPS", "()I", "displayHeight", "getDisplayHeight", "displayWidth", "getDisplayWidth", "entityRenderer", "Lnet/ccbluex/liquidbounce/api/minecraft/client/renderer/IEntityRenderer;", "getEntityRenderer", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/renderer/IEntityRenderer;", "fontRendererObj", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IFontRenderer;", "getFontRendererObj", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IFontRenderer;", "framebuffer", "Lnet/ccbluex/liquidbounce/api/minecraft/client/shader/IFramebuffer;", "getFramebuffer", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/shader/IFramebuffer;", "gameSettings", "Lnet/ccbluex/liquidbounce/api/minecraft/client/settings/IGameSettings;", "getGameSettings", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/settings/IGameSettings;", "isFullScreen", "", "()Z", "isIntegratedServerRunning", "netHandler", "Lnet/ccbluex/liquidbounce/api/minecraft/client/network/IINetHandlerPlayClient;", "getNetHandler", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/network/IINetHandlerPlayClient;", "netHandler2", "Lnet/minecraft/network/play/INetHandlerPlayClient;", "getNetHandler2", "()Lnet/minecraft/network/play/INetHandlerPlayClient;", "objectMouseOver", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IMovingObjectPosition;", "getObjectMouseOver", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IMovingObjectPosition;", "playerController", "Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IPlayerControllerMP;", "getPlayerController", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IPlayerControllerMP;", "renderGlobal", "Lnet/ccbluex/liquidbounce/api/minecraft/client/renderer/IRenderGlobal;", "getRenderGlobal", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/renderer/IRenderGlobal;", "renderItem", "Lnet/ccbluex/liquidbounce/api/minecraft/client/render/entity/IRenderItem;", "getRenderItem", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/render/entity/IRenderItem;", "renderManager", "Lnet/ccbluex/liquidbounce/api/minecraft/renderer/entity/IRenderManager;", "getRenderManager", "()Lnet/ccbluex/liquidbounce/api/minecraft/renderer/entity/IRenderManager;", "renderViewEntity", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "getRenderViewEntity", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "setRenderViewEntity", "(Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;)V", "rightClickDelayTimer", "getRightClickDelayTimer", "setRightClickDelayTimer", "(I)V", "session", "Lnet/ccbluex/liquidbounce/api/minecraft/util/ISession;", "getSession", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/ISession;", "setSession", "(Lnet/ccbluex/liquidbounce/api/minecraft/util/ISession;)V", "soundHandler", "Lnet/ccbluex/liquidbounce/api/minecraft/client/audio/ISoundHandler;", "getSoundHandler", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/audio/ISoundHandler;", "textureManager", "Lnet/ccbluex/liquidbounce/api/minecraft/client/render/texture/ITextureManager;", "getTextureManager", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/render/texture/ITextureManager;", "thePlayer", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityPlayerSP;", "getThePlayer", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityPlayerSP;", "theWorld", "Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IWorldClient;", "getTheWorld", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IWorldClient;", "timer", "Lnet/ccbluex/liquidbounce/api/minecraft/util/ITimer;", "getTimer", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/ITimer;", "displayGuiScreen", "", "screen", "rightClickMouse", "shutdown", "toggleFullscreen", "Pride"})
public interface IMinecraft {
    @NotNull
    public IFramebuffer getFramebuffer();

    public boolean isFullScreen();

    @NotNull
    public File getDataDir();

    public int getDebugFPS();

    @NotNull
    public IRenderGlobal getRenderGlobal();

    @NotNull
    public IRenderItem getRenderItem();

    public int getDisplayWidth();

    public int getDisplayHeight();

    @NotNull
    public IEntityRenderer getEntityRenderer();

    public int getRightClickDelayTimer();

    public void setRightClickDelayTimer(int var1);

    @NotNull
    public ISession getSession();

    public void setSession(@NotNull ISession var1);

    @NotNull
    public ISoundHandler getSoundHandler();

    @Nullable
    public IMovingObjectPosition getObjectMouseOver();

    @NotNull
    public ITimer getTimer();

    @NotNull
    public IRenderManager getRenderManager();

    @NotNull
    public IPlayerControllerMP getPlayerController();

    @Nullable
    public IGuiScreen getCurrentScreen();

    @Nullable
    public IEntity getRenderViewEntity();

    public void setRenderViewEntity(@Nullable IEntity var1);

    @NotNull
    public IINetHandlerPlayClient getNetHandler();

    @NotNull
    public INetHandlerPlayClient getNetHandler2();

    @Nullable
    public IWorldClient getTheWorld();

    @Nullable
    public IEntityPlayerSP getThePlayer();

    @NotNull
    public ITextureManager getTextureManager();

    public boolean isIntegratedServerRunning();

    @Nullable
    public IServerData getCurrentServerData();

    @NotNull
    public IGameSettings getGameSettings();

    @NotNull
    public IFontRenderer getFontRendererObj();

    public void displayGuiScreen(@Nullable IGuiScreen var1);

    public void rightClickMouse();

    public void shutdown();

    public void toggleFullscreen();
}
