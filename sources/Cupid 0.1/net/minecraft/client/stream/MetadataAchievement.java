package net.minecraft.client.stream;

import net.minecraft.stats.Achievement;

public class MetadataAchievement extends Metadata {
  public MetadataAchievement(Achievement p_i1032_1_) {
    super("achievement");
    func_152808_a("achievement_id", p_i1032_1_.statId);
    func_152808_a("achievement_name", p_i1032_1_.getStatName().getUnformattedText());
    func_152808_a("achievement_description", p_i1032_1_.getDescription());
    func_152807_a("Achievement '" + p_i1032_1_.getStatName().getUnformattedText() + "' obtained!");
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\stream\MetadataAchievement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */