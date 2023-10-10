package rosegoldclient.features;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rosegoldclient.Main;
import rosegoldclient.commands.SpellAuraFilter;
import rosegoldclient.events.*;
import rosegoldclient.utils.RenderUtils;
import rosegoldclient.utils.RotationUtils;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class SpellAura {

    public static EntityLivingBase target;
    public static ArrayList<Spell> spellCycle = new ArrayList<>();
    public static Spell currentSpell;
    public static boolean spellWasCast = true;
    public static int delay = 0;
    public static int index = 0;

    public static class Spell {
        public int spell;
        public int delay;

        public Spell(int spell, int delay) {
            this.spell = spell;
            this.delay = delay;
        }

        private static String intToSpellType(int num) {
            switch (num) {
                case 0:
                    return "None";
                case 1:
                    return "RRR";
                case 2:
                    return "RLR";
                case 3:
                    return "RLL";
                case 4:
                    return "RRL";
                case 5:
                    return "L";
                default:
                    return "Unrecognized Spell "+num;
            }
        }

        @Override
        public String toString() {
            return intToSpellType(this.spell) + " with delay of " + this.delay;
        }
    }

    @SubscribeEvent
    public void onEnabled(KeybindEvent event) {
        currentSpell = null;
        spellWasCast = false;
        index = 0;
        spellCycle.clear();
        if (Main.configFile.spellOneType != 0) {
            spellCycle.add(new Spell(Main.configFile.spellOneType, Main.configFile.spellOneDelay));
        }
        if(Main.configFile.spellTwoType != 0) {
            spellCycle.add(new Spell(Main.configFile.spellTwoType, Main.configFile.spellTwoDelay));
        }
        if(Main.configFile.spellThreeType != 0) {
            spellCycle.add(new Spell(Main.configFile.spellThreeType, Main.configFile.spellThreeDelay));
        }
        if(Main.configFile.spellFourType != 0) {
            spellCycle.add(new Spell(Main.configFile.spellFourType, Main.configFile.spellFourDelay));
        }
        if(Main.configFile.spellFiveType != 0) {
            spellCycle.add(new Spell(Main.configFile.spellFiveType, Main.configFile.spellFiveDelay));
        }
        if (Main.configFile.spellSixType != 0) {
            spellCycle.add(new Spell(Main.configFile.spellSixType, Main.configFile.spellSixDelay));
        }
        if(Main.configFile.spellSevenType != 0) {
            spellCycle.add(new Spell(Main.configFile.spellSevenType, Main.configFile.spellSevenDelay));
        }
        if(Main.configFile.spellEightType != 0) {
            spellCycle.add(new Spell(Main.configFile.spellEightType, Main.configFile.spellEightDelay));
        }
        if(Main.configFile.spellNineType != 0) {
            spellCycle.add(new Spell(Main.configFile.spellNineType, Main.configFile.spellNineDelay));
        }
        if(Main.configFile.spellTenType != 0) {
            spellCycle.add(new Spell(Main.configFile.spellTenType, Main.configFile.spellTenDelay));
        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if(!Main.spellAura) {
            target = null;
            return;
        }
        if(!Main.configFile.spellAura || Main.mc.player == null || Main.mc.world == null) return;
        if(!spellWasCast) return;
        if(spellCycle.size() == 0) return;
        target = getEntity();
        if(target == null) return;
        if(delay > 0) {
            delay--;
        } else {
            if(currentSpell == null) {
                currentSpell = spellCycle.get(0);
            } else {
                currentSpell = getNextSpell();
            }
            spellWasCast = false;
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onUpdatePre(PlayerMoveEvent.Pre pre) {
        if(!Main.spellAura || !Main.configFile.spellAura || Main.mc.player == null || Main.mc.world == null) return;
        if (target != null && currentSpell != null) {
            switch (Main.configFile.spellAuraType) {
                case 1: //archer
                    RotationUtils.smoothLook(RotationUtils.getBowRotationToEntity(target), 0, () -> {});
                    break;
                case 2: //mage
                    if (currentSpell.spell == 3) { //meteor
                        RotationUtils.Rotation rotation = RotationUtils.getRotationToBlockUnderEntity(target);
                        if (rotation != null) {
                            RotationUtils.smoothLook(rotation, 0, () -> {});
                        } else {
                            RotationUtils.smoothLook(RotationUtils.getRotationToEntity(target), 0, () -> {});
                        }
                    } else {
                        RotationUtils.smoothLook(RotationUtils.getRotationToEntity(target), 0, () -> {});
                    }
                    break;
                case 3: //assassin
                    switch (currentSpell.spell) {
                        case 1: //vanish
                            RotationUtils.smoothLook(new RotationUtils.Rotation(90, Main.mc.player.rotationYaw), 0, () -> {});
                            break;
                        case 4: //smoke bomb
                            RotationUtils.smoothLook(RotationUtils.getRotationToEntityFeet(target), 0, () -> {});
                            break;
                        default:
                            RotationUtils.smoothLook(RotationUtils.getRotationToEntity(target), 0, () -> {});
                            break;
                    }
                    break;
                case 4: //shaman
                    if (currentSpell.spell == 2) { //totem
                        RotationUtils.smoothLook(new RotationUtils.Rotation(90, Main.mc.player.rotationYaw), 0, () -> {});
                    } else {
                        RotationUtils.smoothLook(RotationUtils.getRotationToEntity(target), 0, () -> {});
                    }
                    break;
                default:
                    RotationUtils.smoothLook(RotationUtils.getRotationToEntity(target), 0, () -> {});
                    break;
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onUpdatePost(PlayerMoveEvent.Post post) {
        if(!Main.spellAura || !Main.configFile.spellAura || Main.mc.player == null || Main.mc.world == null) return;
        if(target == null) return;
        if(spellWasCast) return;
        spellWasCast = true;
        if(currentSpell == null) return;
        castSpell(currentSpell.spell);
        delay = currentSpell.delay;
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if(!Main.configFile.highlightSA) return;
        if(target == null) return;
        RenderUtils.drawEntityESP(target, Color.RED, event.getPartialTicks());
    }

    private static Spell getNextSpell() {
        index++;
        return spellCycle.get((index) % spellCycle.size());
    }

    private static EntityLivingBase getEntity() {
        if(Main.mc.world == null) return null;
        float range = Main.configFile.spellAuraRange;

        List<Entity> entityList = Main.mc.world.getLoadedEntityList().stream().filter(
                entity -> entity instanceof EntityLivingBase
        ).filter(
                entity -> isValid(entity, range, true)
        ).sorted(
                Comparator.comparingDouble(e -> e.getDistance(Main.mc.player))
        ).collect(Collectors.toList());
        Iterator<Entity> iterator = entityList.iterator();
        if(iterator.hasNext()) {
            Entity en = iterator.next();
            return (EntityLivingBase) en;
        }
        return null;
    }

    private static boolean isValid(Entity entity, float range, boolean throughWalls) {
        if(Main.configFile.spellAuraCustomNames && !entity.hasCustomName()) {
            return false;
        }
        if(entity.getCustomNameTag().contains(" By ")) return false;
        if(Main.configFile.spellAuraFilter) {
            if(Main.configFile.spellAuraFilterBlacklist) {
                for (String search : SpellAuraFilter.SASettings) {
                    if (removeFormatting(entity.getCustomNameTag()).contains(search)) {
                        return false;
                    }
                }
            } else {
                b1:
                {
                    for (String search : SpellAuraFilter.SASettings) {
                        if (removeFormatting(entity.getCustomNameTag()).contains(search)) {
                            break b1;
                        }
                    }
                    return false;
                }
            }
        }
        b2: {
            b3: {
                if (entity == Main.mc.player) {
                    break b3;
                }
                if((entity instanceof EntityPlayer || entity instanceof EntityWither || entity instanceof EntityBat) && entity.isInvisible()) {
                    break b3;
                }
                if(entity instanceof EntityArrow) {
                    break b3;
                }
                if(!Main.mc.player.canEntityBeSeen(entity) && !throughWalls) {
                    break b3;
                }
                if(entity.isDead) {
                    break b3;
                }
                double dist = entity.getDistance(Main.mc.player);
                if(range > dist) break b2;
            }
            return false;
        }
        if (entity instanceof EntityPlayer) {
            return true;
        }
        return !(entity instanceof EntityVillager);
    }

    private static String removeFormatting(String input) {
        return input.replaceAll("§[0-9a-fk-or]", "");
    }

    private static void castSpell(int spell) {
        switch (spell) {
            case 1:
                SpellCaster.RRR();
                break;
            case 2:
                SpellCaster.RLR();
                break;
            case 3:
                SpellCaster.RLL();
                break;
            case 4:
                SpellCaster.RRL();
                break;
            case 5:
                SpellCaster.L();
                break;
        }
    }
}
