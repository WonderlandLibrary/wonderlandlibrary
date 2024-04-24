package me.offeex.bloomware.mixin.screen;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.util.RenderUtil;
import me.offeex.bloomware.client.module.visuals.ToolTips;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;



@Mixin(HandledScreen.class)
public abstract class MixinHandledScreen extends Screen {
    @Shadow protected Slot focusedSlot;

    protected MixinHandledScreen(Text text) {
        super(text);
    }

    @Inject(method = "drawMouseoverTooltip", at = @At(value = "HEAD"), cancellable = true)
    private void drawMouseOverTooltip(MatrixStack matrixStack, int i, int j, CallbackInfo ci) {
        ToolTips module = ToolTips.INSTANCE;
        if (module.getEnabled()) {
            if (focusedSlot == null) return;
            if (focusedSlot.getStack() != null && !focusedSlot.getStack().isOf(Items.AIR)) {
                if (ToolTips.INSTANCE.getItemInfo().getToggled()) {
                    List<Text> list2 = getTooltipData(focusedSlot.getStack());
                    this.renderTooltip(matrixStack, list2, i, j);
                }
                if (focusedSlot.getStack().getItem() == Items.FILLED_MAP && ToolTips.INSTANCE.getMaps().getToggled())
                    RenderUtil.INSTANCE.drawMap(matrixStack, i + 8, j - 165, focusedSlot.getStack());
                ci.cancel();
            }
        }
    }

    private List<Text> getTooltipData(ItemStack stack) {
        List<Text> info = stack.getTooltip(Bloomware.INSTANCE.getMc().player, Bloomware.INSTANCE.getMc().options.advancedItemTooltips ? TooltipContext.Default.ADVANCED : TooltipContext.Default.BASIC);
        info.addAll(List.of(
                Text.of("Durability: " + (stack.getMaxDamage() - stack.getDamage()) + "/" + stack.getMaxDamage()),
                Text.of("NBT Size: " + (stack.getNbt() == null ? 0 : stack.getNbt().getSize()) + "B")));
        return info;
    }
}
