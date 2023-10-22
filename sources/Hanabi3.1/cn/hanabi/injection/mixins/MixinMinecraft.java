package cn.hanabi.injection.mixins;

import cn.hanabi.Client;
import cn.hanabi.Hanabi;
import cn.hanabi.events.EventGui;
import cn.hanabi.events.EventKey;
import cn.hanabi.events.EventMouse;
import cn.hanabi.events.EventTick;
import cn.hanabi.injection.interfaces.IMinecraft;
import cn.hanabi.utils.IconUtils;
import cn.hanabi.utils.OSUtils;
import cn.hanabi.utils.PacketHelper;
import cn.hanabi.utils.RenderUtil;
import com.darkmagician6.eventapi.EventManager;
import java.nio.ByteBuffer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SideOnly(Side.CLIENT)
@Mixin({Minecraft.class})
public abstract class MixinMinecraft implements IMinecraft {
   @Shadow
   private int rightClickDelayTimer;
   @Shadow
   public GuiScreen currentScreen;
   @Shadow
   public int displayWidth;
   @Shadow
   public int displayHeight;
   long lastFrame;
   @Shadow
   @Mutable
   @Final
   private Session session;
   @Shadow
   private LanguageManager mcLanguageManager;
   @Shadow
   private Timer timer;
   @Shadow
   private int leftClickCounter;
   @Shadow
   private boolean fullscreen;
   private long lasteFrame = this.getTime();

   @Shadow
   protected abstract void clickMouse();

   @Shadow
   protected abstract void rightClickMouse();

   public void runCrinkMouse() {
      this.clickMouse();
   }

   public void runRightMouse() {
      this.rightClickMouse();
   }

   public void setClickCounter(int a) {
      this.leftClickCounter = a;
   }

   @Inject(
      method = {"run"},
      at = {@At("HEAD")}
   )
   private void init(CallbackInfo callbackInfo) {
      if (this.displayWidth < 1100) {
         this.displayWidth = 1100;
      }

      if (this.displayHeight < 630) {
         this.displayHeight = 630;
      }

   }

   @Inject(
      method = {"startGame"},
      at = {@At(
   value = "FIELD",
   target = "Lnet/minecraft/client/Minecraft;ingameGUI:Lnet/minecraft/client/gui/GuiIngame;",
   shift = At.Shift.AFTER
)}
   )
   private void startGame(CallbackInfo ci) {
      Client.Load();
   }

   @Inject(
      method = {"createDisplay"},
      at = {@At(
   value = "INVOKE",
   target = "Lorg/lwjgl/opengl/Display;setTitle(Ljava/lang/String;)V",
   shift = At.Shift.AFTER
)}
   )
   private void createDisplay(CallbackInfo callbackInfo) {
      Client.doLogin();
   }

   public long getTime() {
      return Sys.getTime() * 1000L / Sys.getTimerResolution();
   }

   @Inject(
      method = {"runGameLoop"},
      at = {@At("HEAD")}
   )
   private void runGameLoop(CallbackInfo ci) {
      PacketHelper.onUpdate();
      long currentTime = this.getTime();
      int deltaTime = (int)(currentTime - this.lasteFrame);
      this.lasteFrame = currentTime;
      RenderUtil.delta = (float)deltaTime;
      Client.onGameLoop();
   }

   @Inject(
      method = {"clickMouse"},
      at = {@At("HEAD")}
   )
   private void clickMouse(CallbackInfo callbackInfo) {
      EventManager.call(new EventMouse(EventMouse.Button.Left));
   }

   @Inject(
      method = {"rightClickMouse"},
      at = {@At("HEAD")}
   )
   private void rightClickMouse(CallbackInfo callbackInfo) {
      EventManager.call(new EventMouse(EventMouse.Button.Right));
   }

   @Inject(
      method = {"middleClickMouse"},
      at = {@At("HEAD")}
   )
   private void middleClickMouse(CallbackInfo callbackInfo) {
      EventManager.call(new EventMouse(EventMouse.Button.Middle));
   }

   @Inject(
      method = {"runTick"},
      at = {@At("HEAD")}
   )
   private void tick(CallbackInfo ci) {
      EventManager.call(new EventTick());
   }

   @Inject(
      method = {"runTick"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/Minecraft;dispatchKeypresses()V",
   shift = At.Shift.AFTER
)}
   )
   private void onKey(CallbackInfo ci) {
      if (Keyboard.getEventKeyState() && this.currentScreen == null) {
         EventManager.call(new EventKey(Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey()));
      }

   }

   @Inject(
      method = {"shutdown"},
      at = {@At("HEAD")}
   )
   private void onShutdown(CallbackInfo ci) {
      Hanabi.INSTANCE.stopClient();
   }

   @Inject(
      method = {"displayGuiScreen"},
      at = {@At(
   value = "FIELD",
   target = "Lnet/minecraft/client/Minecraft;currentScreen:Lnet/minecraft/client/gui/GuiScreen;",
   shift = At.Shift.AFTER
)}
   )
   private void displayGuiScreen(CallbackInfo callbackInfo) {
      EventManager.call(new EventGui(this.currentScreen));
   }

   @Inject(
      method = {"setWindowIcon"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void setWindowIcon(CallbackInfo callbackInfo) {
      if (OSUtils.getOperatingSystemType() != OSUtils.OSType.MacOS) {
         ByteBuffer[] favicon = IconUtils.getFavicon();
         if (favicon != null) {
            Display.setIcon(favicon);
            callbackInfo.cancel();
         }
      }

   }

   @Inject(
      method = {"toggleFullscreen"},
      at = {@At("RETURN")}
   )
   public void toggleFullscreen(CallbackInfo info) {
      if (!this.fullscreen) {
         Display.setResizable(false);
         Display.setResizable(true);
      }

   }

   @Overwrite
   private void sendClickBlockToController(boolean leftClick) {
      if (!leftClick) {
         this.leftClickCounter = 0;
      }

      if (this.leftClickCounter <= 0) {
         if (leftClick && Minecraft.getMinecraft().objectMouseOver != null && Minecraft.getMinecraft().objectMouseOver.typeOfHit == MovingObjectType.BLOCK) {
            BlockPos blockpos = Minecraft.getMinecraft().objectMouseOver.getBlockPos();
            if (Minecraft.getMinecraft().theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air && Minecraft.getMinecraft().playerController.onPlayerDamageBlock(blockpos, Minecraft.getMinecraft().objectMouseOver.sideHit)) {
               Minecraft.getMinecraft().effectRenderer.addBlockHitEffects(blockpos, Minecraft.getMinecraft().objectMouseOver.sideHit);
               Minecraft.getMinecraft().thePlayer.swingItem();
            }
         } else {
            Minecraft.getMinecraft().playerController.resetBlockRemoving();
         }
      }

   }

   public Session getSession() {
      return this.session;
   }

   public void setSession(Session session) {
      this.session = session;
   }

   public Timer getTimer() {
      return this.timer;
   }

   public LanguageManager getLanguageManager() {
      return this.mcLanguageManager;
   }

   public void setRightClickDelayTimer(int i) {
      this.rightClickDelayTimer = i;
   }
}
