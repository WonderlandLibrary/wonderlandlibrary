/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.material.Material
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.GuiMainMenu
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.particle.EffectRenderer
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.MovingObjectPosition
 *  net.minecraft.util.MovingObjectPosition$MovingObjectType
 *  net.minecraft.util.Util
 *  net.minecraft.util.Util$EnumOS
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.LWJGLException
 *  org.lwjgl.Sys
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.opengl.Display
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.client;

import java.awt.AWTException;
import java.nio.ByteBuffer;
import me.report.liquidware.gui.GuiMainMenu;
import me.report.liquidware.modules.render.SilentView;
import me.report.liquidware.utils.HWID.Login;
import me.report.liquidware.utils.ui.FuckerNMSL;
import me.report.liquidware.utils.verify.WbxMain;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.ClickBlockEvent;
import net.ccbluex.liquidbounce.event.KeyEvent;
import net.ccbluex.liquidbounce.event.ScreenEvent;
import net.ccbluex.liquidbounce.event.TickEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoClicker;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.fun.AbortBreaking;
import net.ccbluex.liquidbounce.features.module.modules.fun.MultiActions;
import net.ccbluex.liquidbounce.features.module.modules.world.FastPlace;
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold;
import net.ccbluex.liquidbounce.utils.CPSCounter;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.render.IconUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Util;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SideOnly(value=Side.CLIENT)
@Mixin(value={Minecraft.class})
public abstract class MixinMinecraft {
    @Shadow
    public GuiScreen field_71462_r;
    @Shadow
    public boolean field_71454_w;
    @Shadow
    private int field_71429_W;
    @Shadow
    public MovingObjectPosition field_71476_x;
    @Shadow
    public WorldClient field_71441_e;
    @Shadow
    private Entity field_175622_Z;
    @Shadow
    public EntityPlayerSP field_71439_g;
    @Shadow
    public EffectRenderer field_71452_i;
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
    private long lastFrame = this.getTime();

    @Shadow
    protected abstract void func_175609_am() throws LWJGLException;

    @Inject(method={"run"}, at={@At(value="HEAD")})
    private void init(CallbackInfo callbackInfo) throws AWTException {
        Login.sendWindowsMessageLogin();
        if (this.field_71443_c < 1067) {
            this.field_71443_c = 1067;
        }
        if (this.field_71440_d < 622) {
            this.field_71440_d = 622;
        }
    }

    @Inject(method={"startGame"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/Minecraft;checkGLError(Ljava/lang/String;)V", ordinal=2, shift=At.Shift.AFTER)})
    private void startGame(CallbackInfo callbackInfo) throws Exception {
        LiquidBounce.INSTANCE.startClient();
    }

    @Inject(method={"startGame"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V", shift=At.Shift.AFTER)})
    private void afterMainScreen(CallbackInfo callbackInfo) {
        Minecraft.func_71410_x().func_147108_a((GuiScreen)new FuckerNMSL());
    }

    @Inject(method={"createDisplay"}, at={@At(value="INVOKE", target="Lorg/lwjgl/opengl/Display;setTitle(Ljava/lang/String;)V", shift=At.Shift.AFTER)})
    private void createDisplay(CallbackInfo callbackInfo) {
        WbxMain.LoadingTitle();
    }

    @Inject(method={"displayGuiScreen"}, at={@At(value="FIELD", target="Lnet/minecraft/client/Minecraft;currentScreen:Lnet/minecraft/client/gui/GuiScreen;", shift=At.Shift.AFTER)})
    private void displayGuiScreen(CallbackInfo callbackInfo) {
        if (this.field_71462_r instanceof net.minecraft.client.gui.GuiMainMenu || this.field_71462_r != null && this.field_71462_r.getClass().getName().startsWith("net.labymod") && this.field_71462_r.getClass().getSimpleName().equals("ModGuiMainMenu")) {
            this.field_71462_r = new GuiMainMenu();
            ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
            this.field_71462_r.func_146280_a(Minecraft.func_71410_x(), scaledResolution.func_78326_a(), scaledResolution.func_78328_b());
            this.field_71454_w = false;
        }
        LiquidBounce.eventManager.callEvent(new ScreenEvent(this.field_71462_r));
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

    @Inject(method={"runTick"}, at={@At(value="FIELD", target="Lnet/minecraft/client/Minecraft;joinPlayerCounter:I", shift=At.Shift.BEFORE)})
    private void onTick(CallbackInfo callbackInfo) {
        LiquidBounce.eventManager.callEvent(new TickEvent());
    }

    @Inject(method={"runTick"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/Minecraft;dispatchKeypresses()V", shift=At.Shift.AFTER)})
    private void onKey(CallbackInfo callbackInfo) {
        if (Keyboard.getEventKeyState() && this.field_71462_r == null) {
            LiquidBounce.eventManager.callEvent(new KeyEvent(Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey()));
        }
    }

    @Inject(method={"sendClickBlockToController"}, at={@At(value="INVOKE", target="Lnet/minecraft/util/MovingObjectPosition;getBlockPos()Lnet/minecraft/util/BlockPos;")})
    private void onClickBlock(CallbackInfo callbackInfo) {
        if (this.field_71429_W == 0 && this.field_71441_e.func_180495_p(this.field_71476_x.func_178782_a()).func_177230_c().func_149688_o() != Material.field_151579_a) {
            LiquidBounce.eventManager.callEvent(new ClickBlockEvent(this.field_71476_x.func_178782_a(), this.field_71476_x.field_178784_b));
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
        LiquidBounce.INSTANCE.stopClient();
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
        LiquidBounce.eventManager.callEvent(new WorldEvent(p_loadWorld_1_));
    }

    @Inject(method={"getRenderViewEntity"}, at={@At(value="HEAD")})
    public void getRenderViewEntity(CallbackInfoReturnable<Entity> cir) {
        if (this.field_175622_Z instanceof EntityLivingBase && RotationUtils.serverRotation != null && this.field_71439_g != null) {
            SilentView silentView = (SilentView)LiquidBounce.moduleManager.getModule(SilentView.class);
            KillAura killAura = (KillAura)LiquidBounce.moduleManager.getModule(KillAura.class);
            Scaffold scaffold = (Scaffold)LiquidBounce.moduleManager.getModule(Scaffold.class);
            EntityLivingBase entityLivingBase = (EntityLivingBase)this.field_175622_Z;
            float yaw = RotationUtils.serverRotation.getYaw();
            if (silentView.getState() && ((String)silentView.getMode().get()).equals("Normal") && killAura.getTarget() != null) {
                entityLivingBase.field_70759_as = yaw;
                entityLivingBase.field_70758_at = yaw;
                entityLivingBase.field_70761_aq = yaw;
                entityLivingBase.field_70760_ar = yaw;
            }
            if (silentView.getState() && ((String)silentView.getMode().get()).equals("Normal") && scaffold.getState()) {
                entityLivingBase.field_70759_as = yaw;
                entityLivingBase.field_70758_at = yaw;
                entityLivingBase.field_70761_aq = yaw;
                entityLivingBase.field_70760_ar = yaw;
            }
        }
    }

    @Overwrite
    private void func_147115_a(boolean leftClick) {
        if (!leftClick) {
            this.field_71429_W = 0;
        }
        if (this.field_71429_W <= 0 && (!this.field_71439_g.func_71039_bw() || LiquidBounce.moduleManager.getModule(MultiActions.class).getState())) {
            if (leftClick && this.field_71476_x != null && this.field_71476_x.field_72313_a == MovingObjectPosition.MovingObjectType.BLOCK) {
                BlockPos blockPos = this.field_71476_x.func_178782_a();
                if (this.field_71429_W == 0) {
                    LiquidBounce.eventManager.callEvent(new ClickBlockEvent(blockPos, this.field_71476_x.field_178784_b));
                }
                if (this.field_71441_e.func_180495_p(blockPos).func_177230_c().func_149688_o() != Material.field_151579_a && this.field_71442_b.func_180512_c(blockPos, this.field_71476_x.field_178784_b)) {
                    this.field_71452_i.func_180532_a(blockPos, this.field_71476_x.field_178784_b);
                    this.field_71439_g.func_71038_i();
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

