package com.ohare.client.module.modules.player;

import com.ohare.client.Client;
import com.ohare.client.module.Module;
import com.ohare.client.utils.TimerUtil;
import com.ohare.client.utils.value.impl.EnumValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;

import java.awt.*;

/**
 * @author Xen for OhareWare
 * @since 7/31/2019
 **/
public class Emote extends Module {
    public int heilY;
    public EnumValue<Mode> mode = new EnumValue("Mode",Mode.DAB);
    private TimerUtil timer = new TimerUtil();

    public Emote() {
        super("Emote", Category.PLAYER, new Color(255, 0, 0).getRGB());
        setDescription("Cool emotes");
        addValues(mode);
    }

    @Override
    public void onEnable() {
        heilY = 0;
    }

    @Override
    public boolean hasSubscribers() {
        return false;
    }

    public void setBiped(Entity entityIn, ModelBiped biped) {
        if (mode.getValue() == Emote.Mode.NAZISALUTE) {
            if (timer.sleep(1) && heilY > -190) {
                heilY -= 1;
            }
            biped.bipedRightArm.rotateAngleX = heilY * 0.01F;
            biped.bipedHead.rotateAngleX = -0.025F;
            biped.bipedRightArm.rotateAngleY = 0.15f;
            biped.bipedLeftArm.rotateAngleX = 0;
        } else if (heilY < 0) {
            if (timer.sleep(1)) heilY += 0.1;
            biped.bipedRightArm.rotateAngleX = heilY * 0.01F;
            biped.bipedHead.rotateAngleX = -0.025F;
            biped.bipedRightArm.rotateAngleY = 0.15f;
            biped.bipedLeftArm.rotateAngleX = 0;
        }
        if (mode.getValue() == Emote.Mode.DAB) {
            if ((entityIn.equals(Minecraft.getMinecraft().thePlayer) && Minecraft.getMinecraft().gameSettings.thirdPersonView > 0) || Client.INSTANCE.getFriendManager().isFriend(entityIn.getName())) {
                biped.bipedHead.rotateAngleX = 0.5F;
                biped.bipedHead.rotateAngleY = 1;
                biped.bipedRightArm.rotateAngleX = 4.5F;
                biped.bipedRightArm.rotateAngleY = -1.25F;
                biped.bipedLeftArm.rotateAngleX = 4.5F;
                biped.bipedLeftArm.rotateAngleY = -1.25F;
            }
        }
    }

    public enum Mode {
        DAB,NAZISALUTE
    }
}
