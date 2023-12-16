package net.minecraft.world;

import java.util.UUID;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public abstract class BossInfo
{
    private final UUID uniqueId;
    protected ITextComponent name;
    protected float percent;
    protected Color color;
    protected Overlay overlay;
    protected boolean darkenSky;
    protected boolean playEndBossMusic;
    protected boolean createFog;

    public BossInfo(UUID uniqueIdIn, ITextComponent nameIn, Color colorIn, Overlay overlayIn)
    {
        this.uniqueId = uniqueIdIn;
        this.name = nameIn;
        this.color = colorIn;
        this.overlay = overlayIn;
        this.percent = 1.0F;
    }

    public UUID getUniqueId()
    {
        return this.uniqueId;
    }

    public ITextComponent getName()
    {
        return this.name;
    }

    public void setName(ITextComponent nameIn)
    {
        this.name = nameIn;
    }

    public float getPercent()
    {
        return this.percent;
    }

    public void setPercent(float percentIn)
    {
        this.percent = percentIn;
    }

    public Color getColor()
    {
        return this.color;
    }

    public void setColor(Color colorIn)
    {
        this.color = colorIn;
    }

    public Overlay getOverlay()
    {
        return this.overlay;
    }

    public void setOverlay(Overlay overlayIn)
    {
        this.overlay = overlayIn;
    }

    public boolean shouldDarkenSky()
    {
        return this.darkenSky;
    }

    public BossInfo setDarkenSky(boolean darkenSkyIn)
    {
        this.darkenSky = darkenSkyIn;
        return this;
    }

    public boolean shouldPlayEndBossMusic()
    {
        return this.playEndBossMusic;
    }

    public BossInfo setPlayEndBossMusic(boolean playEndBossMusicIn)
    {
        this.playEndBossMusic = playEndBossMusicIn;
        return this;
    }

    public BossInfo setCreateFog(boolean createFogIn)
    {
        this.createFog = createFogIn;
        return this;
    }

    public boolean shouldCreateFog()
    {
        return this.createFog;
    }

    public static enum Color
    {
        PINK("pink", TextFormatting.RED),
        BLUE("blue", TextFormatting.BLUE),
        RED("red", TextFormatting.DARK_RED),
        GREEN("green", TextFormatting.GREEN),
        YELLOW("yellow", TextFormatting.YELLOW),
        PURPLE("purple", TextFormatting.DARK_BLUE),
        WHITE("white", TextFormatting.WHITE);

        private final String name;
        private final TextFormatting formatting;

        private Color(String name, TextFormatting formatting)
        {
            this.name = name;
            this.formatting = formatting;
        }

        public TextFormatting getFormatting()
        {
            return this.formatting;
        }

        public String getName()
        {
            return this.name;
        }

        public static Color byName(String name)
        {
            for (Color bossinfo$color : values())
            {
                if (bossinfo$color.name.equals(name))
                {
                    return bossinfo$color;
                }
            }

            return WHITE;
        }
    }

    public static enum Overlay
    {
        PROGRESS("progress"),
        NOTCHED_6("notched_6"),
        NOTCHED_10("notched_10"),
        NOTCHED_12("notched_12"),
        NOTCHED_20("notched_20");

        private final String name;

        private Overlay(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return this.name;
        }

        public static Overlay byName(String name)
        {
            for (Overlay bossinfo$overlay : values())
            {
                if (bossinfo$overlay.name.equals(name))
                {
                    return bossinfo$overlay;
                }
            }

            return PROGRESS;
        }
    }
}
