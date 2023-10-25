package net.minecraft.client.gui;

import com.google.common.collect.Maps;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Map;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.BossInfo;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.event.EventManager;
import ru.smertnix.celestial.event.events.EventBossBar;
import ru.smertnix.celestial.feature.impl.visual.NoRender;


public class GuiBossOverlay extends Gui
{
	public static boolean pot = false;
    private static final ResourceLocation GUI_BARS_TEXTURES = new ResourceLocation("textures/gui/bars.png");
    private final Minecraft client;
    private final Map<UUID, BossInfoClient> mapBossInfos = Maps.<UUID, BossInfoClient>newLinkedHashMap();
    public static int x,z;
    public GuiBossOverlay(Minecraft clientIn)
    {
        this.client = clientIn;
    }
   
    public static boolean contains(String str, String substr){
        return str.contains(substr);
    }

    public void renderBossHealth()
    {
        if (!this.mapBossInfos.isEmpty())
        {
            ScaledResolution scaledresolution = new ScaledResolution(this.client);
            int i = scaledresolution.getScaledWidth();
            int j = 12;

            for (BossInfoClient bossinfoclient : this.mapBossInfos.values())
            {
                int k = i / 2 - 91;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.client.getTextureManager().bindTexture(GUI_BARS_TEXTURES);
                this.render(k, j, bossinfoclient);
                String s = bossinfoclient.getName().getFormattedText();
                if (contains(s, "PVP")) {
                	pot = true;
                } else {
                	pot = false;
                }
                EventBossBar event = new EventBossBar(s);
                String bossBar = event.bossName;
                EventManager.call(event);
                if (bossBar.equalsIgnoreCase("airdrop")) {
                	 String string = bossBar;
                	 String[] parts = string.split("z");
                	 String part1 = parts[0]; // 004
                	 String part2 = parts[1]; // 034556
                  	 x = (part1.contains("-") ? -1 : 1) * Integer.parseInt(part1.replaceAll("§4", "").replaceAll("\\D+","").substring(1).substring(1).substring(1).substring(1));
                	 z = (part2.contains("-") ? -1 : 1) * Integer.parseInt(part2.replaceAll("§4", "").replaceAll("\\D+","").substring(1));
                }
                this.client.fontRendererObj.drawStringWithShadow(s, (float)(i / 2 - this.client.fontRendererObj.getStringWidth(s) / 2), (float)(j - 9), 16777215);
                j += 10 + this.client.fontRendererObj.FONT_HEIGHT;

                if (j >= scaledresolution.getScaledHeight() / 3)
                {
                    break;
                }
            }
        } else {
        	x = 0;
        	z = 0;
        	pot = false;
        }
    }

    private void render(int x, int y, BossInfo info)
    {
        this.drawTexturedModalRect(x, y, 0, info.getColor().ordinal() * 5 * 2, 182, 5);

        if (info.getOverlay() != BossInfo.Overlay.PROGRESS)
        {
            this.drawTexturedModalRect(x, y, 0, 80 + (info.getOverlay().ordinal() - 1) * 5 * 2, 182, 5);
        }

        int i = (int)(info.getPercent() * 183.0F);

        if (i > 0)
        {
            this.drawTexturedModalRect(x, y, 0, info.getColor().ordinal() * 5 * 2 + 5, i, 5);

            if (info.getOverlay() != BossInfo.Overlay.PROGRESS)
            {
            	
                this.drawTexturedModalRect(x, y, 0, 80 + (info.getOverlay().ordinal() - 1) * 5 * 2 + 5, i, 5);
            }
        }
    }

    public void read(SPacketUpdateBossInfo packetIn)
    {
        if (packetIn.getOperation() == SPacketUpdateBossInfo.Operation.ADD)
        {
            this.mapBossInfos.put(packetIn.getUniqueId(), new BossInfoClient(packetIn));
        }
        else if (packetIn.getOperation() == SPacketUpdateBossInfo.Operation.REMOVE)
        {
            this.mapBossInfos.remove(packetIn.getUniqueId());
        }
        else
        {
            ((BossInfoClient)this.mapBossInfos.get(packetIn.getUniqueId())).updateFromPacket(packetIn);
        }
    }

    public void clearBossInfos()
    {
        this.mapBossInfos.clear();
        pot = false;
    }

    public boolean shouldPlayEndBossMusic()
    {
        if (!this.mapBossInfos.isEmpty())
        {
            for (BossInfo bossinfo : this.mapBossInfos.values())
            {
                if (bossinfo.shouldPlayEndBossMusic())
                {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean shouldDarkenSky()
    {
        if (!this.mapBossInfos.isEmpty())
        {
            for (BossInfo bossinfo : this.mapBossInfos.values())
            {
                if (bossinfo.shouldDarkenSky())
                {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean shouldCreateFog()
    {
        if (!this.mapBossInfos.isEmpty())
        {
            for (BossInfo bossinfo : this.mapBossInfos.values())
            {
                if (bossinfo.shouldCreateFog())
                {
                    return true;
                }
            }
        }
        return false;
    }
}
