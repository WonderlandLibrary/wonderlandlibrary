package net.minecraft.client.stream;

import net.minecraft.stats.Achievement;
import net.minecraft.util.IChatComponent;

public class MetadataAchievement
  extends Metadata
{
  private static final String __OBFID = "CL_00001824";
  
  public MetadataAchievement(Achievement p_i1032_1_)
  {
    super("achievement");
    func_152808_a("achievement_id", p_i1032_1_.statId);
    func_152808_a("achievement_name", p_i1032_1_.getStatName().getUnformattedText());
    func_152808_a("achievement_description", p_i1032_1_.getDescription());
    func_152807_a("Achievement '" + p_i1032_1_.getStatName().getUnformattedText() + "' obtained!");
  }
}
