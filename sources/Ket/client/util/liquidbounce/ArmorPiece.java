package client.util.liquidbounce;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

@Getter
@AllArgsConstructor
public final class ArmorPiece {
    private final ItemStack itemStack;
    private final int slot;
    public int getArmorType() {
        return ((ItemArmor) itemStack.getItem()).armorType;
    }
}
