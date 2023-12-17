// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.MISC;

import ru.tuskevich.event.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ContainerChest;
import ru.tuskevich.event.events.impl.EventMotion;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "MenuClose", type = Type.MISC)
public class MenuClose extends Module
{
    @EventTarget
    public void onUpdate(final EventMotion event) {
        final Minecraft mc = MenuClose.mc;
        if (Minecraft.player.openContainer instanceof ContainerChest) {
            final Minecraft mc2 = MenuClose.mc;
            final ContainerChest chest = (ContainerChest)Minecraft.player.openContainer;
            final String chestName = chest.getLowerChestInventory().getName();
            if (chestName.contains("\u041c\u0435\u043d\u044e")) {
                final Minecraft mc3 = MenuClose.mc;
                Minecraft.player.closeScreen();
            }
        }
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
}
