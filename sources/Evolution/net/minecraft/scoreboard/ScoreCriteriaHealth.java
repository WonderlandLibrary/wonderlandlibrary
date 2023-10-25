package net.minecraft.scoreboard;

public class ScoreCriteriaHealth extends ScoreCriteria
{
    public ScoreCriteriaHealth(String name)
    {
        super(name);
    }

    public boolean isReadOnly()
    {
        return true;
    }

    public EnumRenderType getRenderType()
    {
        return EnumRenderType.HEARTS;
    }
}
