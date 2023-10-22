package cn.hanabi.injection.mixins;

import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({GuiDisconnected.class})
public abstract class MixinGuiDisconnected extends GuiScreen {
   @Shadow
   public abstract void initGui();
}
