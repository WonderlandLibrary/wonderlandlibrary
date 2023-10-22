package cn.hanabi.injection.mixins;

import cn.hanabi.Client;
import cn.hanabi.command.commands.SkinChangeCommand;
import cn.hanabi.modules.ModManager;
import cn.hanabi.modules.modules.render.ClickGUIModule;
import java.util.Objects;
import me.yarukon.Yarukon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SideOnly(Side.CLIENT)
@Mixin({AbstractClientPlayer.class})
public abstract class MixinAbstractClientPlayer extends MixinEntityPlayer {
   @Inject(
      method = {"getLocationCape"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void getCape(CallbackInfoReturnable callbackInfoReturnable) {
      ResourceLocation location;
      if (Client.onDebug) {
         ClickGUIModule var10000 = (ClickGUIModule)ModManager.getModule(ClickGUIModule.class);
         if (ClickGUIModule.theme.isCurrentMode("Dark")) {
            location = new ResourceLocation("Client/capes/darkhanabi.png");
         } else {
            location = new ResourceLocation("Client/capes/lighthanabi.png");
         }
      } else {
         location = new ResourceLocation("Client/capes/darkhanabi.png");
      }

      callbackInfoReturnable.setReturnValue(this.getUniqueID().equals(Minecraft.getMinecraft().thePlayer.getUniqueID()) ? location : null);
   }

   @Inject(
      method = {"getLocationSkin()Lnet/minecraft/util/ResourceLocation;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void getSkin(CallbackInfoReturnable callbackInfoReturnable) {
      if (Objects.equals(this.getGameProfile().getName(), Minecraft.getMinecraft().thePlayer.getGameProfile().getName()) && !SkinChangeCommand.targetSkin.isEmpty()) {
         callbackInfoReturnable.setReturnValue(Yarukon.INSTANCE.getSkin(SkinChangeCommand.targetSkin));
      }
   }

   @Inject(
      method = {"getSkinType"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void getSkinType(CallbackInfoReturnable callbackInfoReturnable) {
      if (Objects.equals(this.getGameProfile().getName(), Minecraft.getMinecraft().thePlayer.getGameProfile().getName()) && !SkinChangeCommand.targetSkin.isEmpty()) {
         callbackInfoReturnable.setReturnValue(SkinChangeCommand.slim ? "slim" : "default");
      }
   }
}
