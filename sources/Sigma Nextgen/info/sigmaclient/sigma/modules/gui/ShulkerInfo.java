package info.sigmaclient.sigma.modules.gui;

import info.sigmaclient.sigma.event.render.RenderEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.*;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.List;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class ShulkerInfo extends Module {
    public ShulkerInfo() {
        super("ShulkerInfo", Category.Gui, "Show full shulker's info");
    }
    public static void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        CompoundNBT compoundnbt = stack.getChildTag("BlockEntityTag");

        if (compoundnbt != null)
        {
            if (compoundnbt.contains("LootTable", 8))
            {
                tooltip.add(new StringTextComponent("???????"));
            }

            if (compoundnbt.contains("Items", 9))
            {
                NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
                ItemStackHelper.loadAllItems(compoundnbt, nonnulllist);
                int i = 0;
                int j = 0;

                for (ItemStack itemstack : nonnulllist)
                {
                    if (!itemstack.isEmpty())
                    {
                        ++j;

//                        if (i <= 4)
//                        {
                            ++i;
                            IFormattableTextComponent iformattabletextcomponent = itemstack.getDisplayName().deepCopy();
                            iformattabletextcomponent.appendString(" x").appendString(String.valueOf(itemstack.getCount())).appendString(" - " + itemstack.getTranslationKey());
                            tooltip.add(iformattabletextcomponent);
//                        }
                    }
                }

//                if (j - i > 0)
//                {
//                    tooltip.add((new TranslationTextComponent("container.shulkerBox.more", j - i)).mergeStyle(TextFormatting.ITALIC));
//                }
            }
        }
    }
    @Override
    public void onRenderEvent(RenderEvent event) {
        super.onRenderEvent(event);
    }
}
