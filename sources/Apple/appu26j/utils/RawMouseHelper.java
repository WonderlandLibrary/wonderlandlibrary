package appu26j.utils;

import appu26j.Apple;
import appu26j.mods.general.RawInput;
import net.minecraft.util.MouseHelper;

public class RawMouseHelper extends MouseHelper
{
    private RawInput rawInput;
    
    public RawMouseHelper()
    {
        this.rawInput = (RawInput) Apple.CLIENT.getModsManager().getMod("Raw Input");
    }
    
    @Override
    public void mouseXYChange()
    {
        this.deltaX = this.rawInput.getDeltaX();
        this.rawInput.setDeltaX(0);
        this.deltaY = -this.rawInput.getDeltaY();
        this.rawInput.setDeltaY(0);
    }
}
