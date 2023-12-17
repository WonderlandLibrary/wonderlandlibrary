// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.spectator;

import net.minecraft.util.text.ITextComponent;
import java.util.List;

public interface ISpectatorMenuView
{
    List<ISpectatorMenuObject> getItems();
    
    ITextComponent getPrompt();
}
