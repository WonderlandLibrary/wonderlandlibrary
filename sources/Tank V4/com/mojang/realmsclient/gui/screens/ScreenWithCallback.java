package com.mojang.realmsclient.gui.screens;

import net.minecraft.realms.RealmsScreen;

public abstract class ScreenWithCallback extends RealmsScreen {
   abstract void callback(Object var1);
}
