package net.minecraft.util;

import org.lwjgl.input.Keyboard;

import com.enjoytheban.Client;
import com.enjoytheban.module.modules.player.Invplus;
import com.enjoytheban.utils.Helper;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.GameSettings;

public class MovementInputFromOptions extends MovementInput
{
    private final GameSettings gameSettings;
    private static final String __OBFID = "CL_00000937";

    public MovementInputFromOptions(GameSettings p_i1237_1_)
    {
        this.gameSettings = p_i1237_1_;
    }

    public void updatePlayerMoveState()
    {

        //Don't hardcode
    	//if invwalk is enabled and isnt chatting
    	Invplus inv = (Invplus)Client.instance.getModuleManager().getModuleByClass(Invplus.class);
    	if(inv.isEnabled() && inv.sw.getValue() && !(Helper.mc.currentScreen instanceof GuiChat)) {

        this.moveStrafe = 0.0F;
        this.moveForward = 0.0F;

        if (Keyboard.isKeyDown(this.gameSettings.keyBindForward.getKeyCode()))
        {
            ++this.moveForward;
        }

        if (Keyboard.isKeyDown(this.gameSettings.keyBindBack.getKeyCode()))
        {
            --this.moveForward;
        }

        if (Keyboard.isKeyDown(this.gameSettings.keyBindLeft.getKeyCode()))
        {
            ++this.moveStrafe;
        }

        if (Keyboard.isKeyDown(this.gameSettings.keyBindRight.getKeyCode()))
        {
            --this.moveStrafe;
        }

        this.jump = Keyboard.isKeyDown(this.gameSettings.keyBindJump.getKeyCode());
        this.sneak = this.gameSettings.keyBindSneak.getIsKeyPressed();

        if (this.sneak)
        {
            this.moveStrafe = (float)((double)this.moveStrafe * 0.3D);
            this.moveForward = (float)((double)this.moveForward * 0.3D);
        }

    } else {

    	this.moveStrafe = 0.0F;
        this.moveForward = 0.0F;

        if (this.gameSettings.keyBindForward.getIsKeyPressed()) {
          this.moveForward += 1.0F;
        }

        if (this.gameSettings.keyBindBack.getIsKeyPressed()) {
          this.moveForward -= 1.0F;
        }

        if (this.gameSettings.keyBindLeft.getIsKeyPressed()) {
          this.moveStrafe += 1.0F;
        }

        if (this.gameSettings.keyBindRight.getIsKeyPressed()) {
          this.moveStrafe -= 1.0F;
        }

        this.jump = this.gameSettings.keyBindJump.getIsKeyPressed();
        this.sneak = this.gameSettings.keyBindSneak.getIsKeyPressed();

        if (this.sneak)
        {
          this.moveStrafe = ((float)(this.moveStrafe * 0.3D));
          this.moveForward = ((float)(this.moveForward * 0.3D));
        }
     }
  }
}