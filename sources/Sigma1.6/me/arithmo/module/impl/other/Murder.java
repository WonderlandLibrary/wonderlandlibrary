/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.other;

import java.util.List;
import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventTick;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import me.arithmo.util.Timer;
import me.arithmo.util.misc.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class Murder
extends Module {
    private String MESSAGE = "MESSAGE";
    private Timer timer = new Timer();

    public Murder(ModuleData data) {
        super(data);
        this.settings.put(this.MESSAGE, new Setting<String>(this.MESSAGE, "{P} is trying to kill me!", "Sends a mesage in chat when murderer is found. {P} = Name"));
    }

    @RegisterEvent(events={EventTick.class})
    public void onEvent(Event event) {
        for (Object o : Murder.mc.theWorld.loadedEntityList) {
            EntityPlayer ent;
            if (!(o instanceof EntityPlayer) || !this.timer.delay(15000.0f) || (ent = (EntityPlayer)o) == Murder.mc.thePlayer || ent.getCurrentEquippedItem() == null || !(ent.getCurrentEquippedItem().getItem() instanceof ItemSword) || ent.isMurderer) continue;
            ent.isMurderer = true;
            String customChat = (String)((Setting)this.settings.get(this.MESSAGE)).getValue();
            customChat = customChat.replace("{P}", "%s");
            ChatUtil.sendChat(String.format(customChat, ent.getName()));
            break;
        }
    }
}

