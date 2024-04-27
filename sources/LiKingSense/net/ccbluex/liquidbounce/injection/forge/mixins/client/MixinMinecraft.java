/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.particle.ParticleManager
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.Util
 *  net.minecraft.util.Util$EnumOS
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.Sys
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.opengl.Display
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Overwrite
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.At$Shift
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.client;

import Verify1.GuiLogin;
import java.nio.ByteBuffer;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.ClickBlockEvent;
import net.ccbluex.liquidbounce.event.KeyEvent;
import net.ccbluex.liquidbounce.event.ScreenEvent;
import net.ccbluex.liquidbounce.event.TickEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoClicker;
import net.ccbluex.liquidbounce.features.module.modules.exploit.AbortBreaking;
import net.ccbluex.liquidbounce.features.module.modules.misc.FakeFPS;
import net.ccbluex.liquidbounce.features.module.modules.world.FastPlace;
import net.ccbluex.liquidbounce.injection.backend.EnumFacingImplKt;
import net.ccbluex.liquidbounce.injection.backend.GuiScreenImplKt;
import net.ccbluex.liquidbounce.injection.backend.WorldClientImplKt;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;
import net.ccbluex.liquidbounce.injection.backend.utils.BackendExtentionsKt;
import net.ccbluex.liquidbounce.utils.CPSCounter;
import net.ccbluex.liquidbounce.utils.render.IconUtils;
import net.ccbluex.liquidbounce.utils.render.MiniMapRegister;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ui.GuiMainMenu;

@SideOnly(value=Side.CLIENT)
@Mixin(value={Minecraft.class})
public abstract class MixinMinecraft {
    @Shadow
    public GuiScreen field_71462_r;
    @Shadow
    public boolean field_71454_w;
    @Shadow
    public RayTraceResult field_71476_x;
    @Shadow
    public WorldClient field_71441_e;
    @Shadow
    public EntityPlayerSP field_71439_g;
    @Shadow
    public ParticleManager field_71452_i;
    @Shadow
    public PlayerControllerMP field_71442_b;
    @Shadow
    public int field_71443_c;
    @Shadow
    public int field_71440_d;
    @Shadow
    public int field_71467_ac;
    @Shadow
    public GameSettings field_71474_y;
    @Shadow
    private int field_71429_W;
    private long lastFrame = this.getTime();

    @Inject(method={"run"}, at={@At(value="HEAD")})
    private void init(CallbackInfo callbackInfo) {
        if (this.field_71443_c < 1067) {
            this.field_71443_c = 1067;
        }
        if (this.field_71440_d < 622) {
            this.field_71440_d = 622;
        }
    }

    @Inject(method={"<init>"}, at={@At(value="RETURN")})
    private void injectWrapperInitializer(CallbackInfo ci) {
        LiquidBounce.wrapper = WrapperImpl.INSTANCE;
    }

    @Inject(method={"init"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/Minecraft;checkGLError(Ljava/lang/String;)V", ordinal=2, shift=At.Shift.AFTER)})
    private void startGame(CallbackInfo callbackInfo) {
        LiquidBounce.INSTANCE.initClient();
    }

    @Inject(method={"init"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V", shift=At.Shift.AFTER)})
    private void afterMainScreen(CallbackInfo callbackInfo) {
        LiquidBounce.wrapper.getMinecraft().displayGuiScreen(LiquidBounce.wrapper.getClassProvider().wrapGuiScreen(new GuiLogin()));
    }

    @Inject(method={"createDisplay"}, at={@At(value="INVOKE", target="Lorg/lwjgl/opengl/Display;setTitle(Ljava/lang/String;)V", shift=At.Shift.AFTER)})
    private void createDisplay(CallbackInfo callbackInfo) {
        Display.setTitle((String)"LiKingSense");
    }

    @Inject(method={"displayGuiScreen"}, at={@At(value="FIELD", target="Lnet/minecraft/client/Minecraft;currentScreen:Lnet/minecraft/client/gui/GuiScreen;", shift=At.Shift.AFTER)})
    private void displayGuiScreen(CallbackInfo callbackInfo) {
        if (this.field_71462_r instanceof GuiMainMenu || this.field_71462_r != null && this.field_71462_r.getClass().getName().startsWith("net.labymod") && this.field_71462_r.getClass().getSimpleName().equals("ModGuiMainMenu")) {
            this.field_71462_r = new GuiMainMenu();
            ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
            this.field_71462_r.func_146280_a(Minecraft.func_71410_x(), scaledResolution.func_78326_a(), scaledResolution.func_78328_b());
            this.field_71454_w = false;
        }
        LiquidBounce.eventManager.callEvent(new ScreenEvent(this.field_71462_r == null ? null : GuiScreenImplKt.wrap(this.field_71462_r)));
    }

    @Inject(method={"runGameLoop"}, at={@At(value="HEAD")})
    private void runGameLoop(CallbackInfo callbackInfo) {
        long currentTime = this.getTime();
        int deltaTime = (int)(currentTime - this.lastFrame);
        this.lastFrame = currentTime;
        RenderUtils.deltaTime = deltaTime;
    }

    public long getTime() {
        return Sys.getTime() * 1000L / Sys.getTimerResolution();
    }

    @Overwrite
    public static int func_175610_ah() {
        FakeFPS fakeFPS = (FakeFPS)LiquidBounce.moduleManager.getModule(FakeFPS.class);
        if (fakeFPS.getState()) {
            return fakeFPS.getFakeFPS();
        }
        return Minecraft.field_71470_ab;
    }

    @Inject(method={"runTick"}, at={@At(value="FIELD", target="Lnet/minecraft/client/Minecraft;joinPlayerCounter:I", shift=At.Shift.BEFORE)})
    private void onTick(CallbackInfo callbackInfo) {
        LiquidBounce.eventManager.callEvent(new TickEvent());
    }

    @Inject(method={"runTickKeyboard"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/Minecraft;dispatchKeypresses()V", shift=At.Shift.AFTER)})
    private void onKey(CallbackInfo callbackInfo) {
        if (Keyboard.getEventKeyState() && this.field_71462_r == null) {
            LiquidBounce.eventManager.callEvent(new KeyEvent(Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey()));
        }
    }

    @Inject(method={"sendClickBlockToController"}, at={@At(value="INVOKE", target="Lnet/minecraft/util/math/RayTraceResult;getBlockPos()Lnet/minecraft/util/math/BlockPos;")})
    private void onClickBlock(CallbackInfo callbackInfo) {
        IBlockState blockState = this.field_71441_e.func_180495_p(this.field_71476_x.func_178782_a());
        if (this.field_71429_W == 0 && blockState.func_177230_c().func_149688_o(blockState) != Material.field_151579_a) {
            LiquidBounce.eventManager.callEvent(new ClickBlockEvent(BackendExtentionsKt.wrap(this.field_71476_x.func_178782_a()), EnumFacingImplKt.wrap(this.field_71476_x.field_178784_b)));
        }
    }

    @Inject(method={"setWindowIcon"}, at={@At(value="HEAD")}, cancellable=true)
    private void setWindowIcon(CallbackInfo callbackInfo) {
        ByteBuffer[] liquidBounceFavicon;
        if (Util.func_110647_a() != Util.EnumOS.OSX && (liquidBounceFavicon = IconUtils.getFavicon()) != null) {
            Display.setIcon((ByteBuffer[])liquidBounceFavicon);
            callbackInfo.cancel();
        }
    }

    @Inject(method={"shutdown"}, at={@At(value="HEAD")})
    private void shutdown(CallbackInfo callbackInfo) {
        try {
            LiquidBounce.INSTANCE.stopClient();
        }
        catch (Exception e) {
            System.exit(0);
        }
    }

    @Inject(method={"clickMouse"}, at={@At(value="HEAD")})
    private void clickMouse(CallbackInfo callbackInfo) {
        CPSCounter.registerClick(CPSCounter.MouseButton.LEFT);
        if (LiquidBounce.moduleManager.getModule(AutoClicker.class).getState()) {
            this.field_71429_W = 0;
        }
    }

    @Inject(method={"middleClickMouse"}, at={@At(value="HEAD")})
    private void middleClickMouse(CallbackInfo ci) {
        CPSCounter.registerClick(CPSCounter.MouseButton.MIDDLE);
    }

    @Inject(method={"rightClickMouse"}, at={@At(value="FIELD", target="Lnet/minecraft/client/Minecraft;rightClickDelayTimer:I", shift=At.Shift.AFTER)})
    private void rightClickMouse(CallbackInfo callbackInfo) {
        CPSCounter.registerClick(CPSCounter.MouseButton.RIGHT);
        FastPlace fastPlace = (FastPlace)LiquidBounce.moduleManager.getModule(FastPlace.class);
        if (fastPlace.getState()) {
            this.field_71467_ac = (Integer)fastPlace.getSpeedValue().get();
        }
    }

    @Inject(method={"loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;Ljava/lang/String;)V"}, at={@At(value="HEAD")})
    private void loadWorld(WorldClient p_loadWorld_1_, String p_loadWorld_2_, CallbackInfo callbackInfo) {
        if (this.field_71441_e != null) {
            MiniMapRegister.INSTANCE.unloadAllChunks();
        }
        LiquidBounce.eventManager.callEvent(new WorldEvent(p_loadWorld_1_ == null ? null : WorldClientImplKt.wrap(p_loadWorld_1_)));
    }

    @Overwrite
    private void func_147115_a(boolean leftClick) {
        if (!leftClick) {
            this.field_71429_W = 0;
        }
        if (this.field_71429_W <= 0 && !this.field_71439_g.func_184587_cr()) {
            if (leftClick && this.field_71476_x != null && this.field_71476_x.field_72313_a == RayTraceResult.Type.BLOCK) {
                IBlockState bs;
                BlockPos blockPos = this.field_71476_x.func_178782_a();
                if (this.field_71429_W == 0) {
                    LiquidBounce.eventManager.callEvent(new ClickBlockEvent(BackendExtentionsKt.wrap(blockPos), EnumFacingImplKt.wrap(this.field_71476_x.field_178784_b)));
                }
                if ((bs = this.field_71441_e.func_180495_p(blockPos)).func_177230_c().func_149688_o(bs) != Material.field_151579_a && this.field_71442_b.func_180512_c(blockPos, this.field_71476_x.field_178784_b)) {
                    this.field_71452_i.func_180532_a(blockPos, this.field_71476_x.field_178784_b);
                    this.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
                }
            } else if (!LiquidBounce.moduleManager.getModule(AbortBreaking.class).getState()) {
                this.field_71442_b.func_78767_c();
            }
        }
    }

    @Overwrite
    public int func_90020_K() {
        return this.field_71441_e == null && this.field_71462_r != null ? 60 : this.field_71474_y.field_74350_i;
    }
}

