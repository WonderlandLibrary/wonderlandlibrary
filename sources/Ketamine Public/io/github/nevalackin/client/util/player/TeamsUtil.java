package io.github.nevalackin.client.util.player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public final class TeamsUtil {

    private static final int[] COLOR_CODE_TABLE = new int[32];

    static {
        for (int i = 0; i < 32; ++i) {
            int j = (i >> 3 & 1) * 85;
            int k = (i >> 2 & 1) * 170 + j;
            int l = (i >> 1 & 1) * 170 + j;
            int i1 = (i & 1) * 170 + j;

            if (i == 6) {
                k += 85;
            }

            if (i >= 16) {
                k /= 4;
                l /= 4;
                i1 /= 4;
            }

            COLOR_CODE_TABLE[i] = (k & 255) << 16 | (l & 255) << 8 | i1 & 255;
        }
    }

    private TeamsUtil() {
    }

    private static char getColourChar(final EntityPlayer player) {
        final String name = player.getDisplayName().getFormattedText();
        if (name.length() < 2) return '?';
        final int index = name.indexOf('\247');
        if (index == -1 || index + 1 >= name.length()) return '?';
        return name.charAt(index + 1);
    }

    public enum TeamsMode {
        NAME("Name", player -> {
            final char character = getColourChar(player);
            if (character == '?') return 0;
            final int colourIndex = "0123456789abcdef".indexOf(character);
            if (colourIndex == -1) return 0;
            return COLOR_CODE_TABLE[colourIndex] | 0xFF000000;
        }),
        ARMOUR("Armour", player -> {
            final ItemStack helmet = player.getCurrentArmor(3);
            final ItemStack chestPlate = player.getCurrentArmor(2);

            if (helmet == null || chestPlate == null || !(helmet.getItem() instanceof ItemArmor) || !(chestPlate.getItem() instanceof ItemArmor))
                return 0;

            final ItemArmor hArmor = (ItemArmor) helmet.getItem();
            final ItemArmor cpArmor = (ItemArmor) chestPlate.getItem();

            if (hArmor.getArmorMaterial() != ItemArmor.ArmorMaterial.LEATHER || cpArmor.getArmorMaterial() != ItemArmor.ArmorMaterial.LEATHER)
                return 0;

            final int hColour = hArmor.getColor(helmet);
            final int cpColour = cpArmor.getColor(chestPlate);

            if (hColour != cpColour) return 0;

            return hColour | 0xFF000000;
        });

        private final String name;
        private final TeamsColourSupplier colourSupplier;
        private final TeamComparator comparator;

        TeamsMode(String name, TeamsColourSupplier colourSupplier) {
            this.name = name;
            this.colourSupplier = colourSupplier;
            this.comparator = (player1, player2) -> {
                final int colour1 = getColourSupplier().getTeamColour(player1);
                if (colour1 == 0) return false;
                final int colour2 = getColourSupplier().getTeamColour(player2);
                if (colour2 == 0) return false;

                return colour1 == colour2;
            };
        }

        @Override
        public String toString() {
            return this.name;
        }

        public TeamsColourSupplier getColourSupplier() {
            return colourSupplier;
        }

        public TeamComparator getComparator() {
            return comparator;
        }
    }

    public interface TeamsColourSupplier {
        int getTeamColour(final EntityPlayer player);
    }

    public interface TeamComparator {
        boolean isOnSameTeam(final EntityPlayer player1,
                             final EntityPlayer player2);
    }
}
