/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreenResourcePacks
 *  net.minecraftforge.fml.common.Loader
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraftforge.fml.common.Loader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value={GuiScreenResourcePacks.class})
public class GuiScreenResourcePacksMixin_ChangeTextPosition {
    @ModifyConstant(method={"drawScreen"}, constant={@Constant(intValue=77)})
    private int patcher$moveInformationText(int original) {
        return !Loader.isModLoaded((String)"ResourcePackOrganizer") ? 102 : original;
    }
}

