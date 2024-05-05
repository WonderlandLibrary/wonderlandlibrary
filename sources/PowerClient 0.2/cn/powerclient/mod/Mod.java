/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.mod;

import com.darkmagician6.eventapi.EventManager;
import java.util.List;
import me.AveReborn.Client;
import me.AveReborn.Value;
import me.AveReborn.mod.Category;
import me.AveReborn.ui.ClientNotification;
import me.AveReborn.util.ClientUtil;
import me.AveReborn.util.FileUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;

public class Mod {
	public Minecraft mc = Minecraft.getMinecraft();
    public Value showValue;
    private String name;
    private int key;
    private Category category;
    private boolean isEnabled;
    private String desc;
    public boolean openValues;
    public double arrowAnlge = 0.0;
    public double animateX = 0.0;
    public double hoverOpacity = 0.0;
    public float circleValue;
    public boolean canSeeCircle;
    public int[] circleCoords;
    public boolean clickedCircle;
    public String displayName = "";

    public Mod(String name, Category category) {
        this.name = name;
        this.key = -1;
        this.category = category;
        this.circleCoords = new int[2];
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onToggle() {
    }

    public void disableValues() {
    }

    public String getValue() {
        if (this.showValue == null) {
            return "";
        }
        return this.showValue.isValueMode ? this.showValue.getModeAt(this.showValue.getCurrentMode()) : String.valueOf(this.showValue.getValueState());
    }

    public void set(boolean state) {
        this.set(state, false);
        Client.instance.fileMgr.saveMods();
    }

    public void set(boolean state, boolean safe) {
        this.isEnabled = state;
        this.onToggle();
        if (state) {
            if (this.mc.theWorld != null) {
                if (!Client.isClientLoading) {
                    ClientUtil.sendClientMessage(String.valueOf(this.getName()) + " Enabled", ClientNotification.Type.SUCCESS);
                }
                this.onEnable();
            }
            EventManager.register(this);
        } else {
            if (this.mc.theWorld != null) {
                if (!Client.isClientLoading) {
                    ClientUtil.sendClientMessage(String.valueOf(this.getName()) + " Disabled", ClientNotification.Type.ERROR);
                }
                this.onDisable();
            }
            EventManager.unregister(this);
        }
        if (safe) {
            Client.instance.fileMgr.saveMods();
        }
    }

    public String getName() {
        return this.name;
    }

    public int getKey() {
        return this.key;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Category getCategory() {
        return this.category;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public boolean hasValues() {
        for (Value value : Value.list) {
            String name = value.getValueName().split("_")[0];
            if (!name.equalsIgnoreCase(this.getName())) continue;
            return true;
        }
        return false;
    }

    public void portMove(float yaw, float multiplyer, float up2) {
        double moveX = (- Math.sin(Math.toRadians(yaw))) * (double)multiplyer;
        double moveZ = Math.cos(Math.toRadians(yaw)) * (double)multiplyer;
        double moveY = up2;
        Minecraft.thePlayer.setPosition(moveX + Minecraft.thePlayer.posX, moveY + Minecraft.thePlayer.posY, moveZ + Minecraft.thePlayer.posZ);
    }

    public void move(float yaw, float multiplyer, float up2) {
        double moveX = (- Math.sin(Math.toRadians(yaw))) * (double)multiplyer;
        double moveZ = Math.cos(Math.toRadians(yaw)) * (double)multiplyer;
        Minecraft.thePlayer.motionX = moveX;
        Minecraft.thePlayer.motionY = up2;
        Minecraft.thePlayer.motionZ = moveZ;
    }

    public void move(float yaw, float multiplyer) {
        double moveX = (- Math.sin(Math.toRadians(yaw))) * (double)multiplyer;
        double moveZ = Math.cos(Math.toRadians(yaw)) * (double)multiplyer;
        Minecraft.thePlayer.motionX = moveX;
        Minecraft.thePlayer.motionZ = moveZ;
    }
}

