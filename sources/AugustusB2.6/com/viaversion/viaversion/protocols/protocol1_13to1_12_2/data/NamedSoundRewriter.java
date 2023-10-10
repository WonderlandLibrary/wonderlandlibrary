// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NamedSoundRewriter
{
    private static final Map<String, String> oldToNew;
    
    public static String getNewId(final String old) {
        final String newId = NamedSoundRewriter.oldToNew.get(old);
        return (newId != null) ? newId : old.toLowerCase(Locale.ROOT);
    }
    
    static {
        (oldToNew = new HashMap<String, String>()).put("block.cloth.break", "block.wool.break");
        NamedSoundRewriter.oldToNew.put("block.cloth.fall", "block.wool.fall");
        NamedSoundRewriter.oldToNew.put("block.cloth.hit", "block.wool.hit");
        NamedSoundRewriter.oldToNew.put("block.cloth.place", "block.wool.place");
        NamedSoundRewriter.oldToNew.put("block.cloth.step", "block.wool.step");
        NamedSoundRewriter.oldToNew.put("block.enderchest.close", "block.ender_chest.close");
        NamedSoundRewriter.oldToNew.put("block.enderchest.open", "block.ender_chest.open");
        NamedSoundRewriter.oldToNew.put("block.metal_pressureplate.click_off", "block.metal_pressure_plate.click_off");
        NamedSoundRewriter.oldToNew.put("block.metal_pressureplate.click_on", "block.metal_pressure_plate.click_on");
        NamedSoundRewriter.oldToNew.put("block.note.basedrum", "block.note_block.basedrum");
        NamedSoundRewriter.oldToNew.put("block.note.bass", "block.note_block.bass");
        NamedSoundRewriter.oldToNew.put("block.note.bell", "block.note_block.bell");
        NamedSoundRewriter.oldToNew.put("block.note.chime", "block.note_block.chime");
        NamedSoundRewriter.oldToNew.put("block.note.flute", "block.note_block.flute");
        NamedSoundRewriter.oldToNew.put("block.note.guitar", "block.note_block.guitar");
        NamedSoundRewriter.oldToNew.put("block.note.harp", "block.note_block.harp");
        NamedSoundRewriter.oldToNew.put("block.note.hat", "block.note_block.hat");
        NamedSoundRewriter.oldToNew.put("block.note.pling", "block.note_block.pling");
        NamedSoundRewriter.oldToNew.put("block.note.snare", "block.note_block.snare");
        NamedSoundRewriter.oldToNew.put("block.note.xylophone", "block.note_block.xylophone");
        NamedSoundRewriter.oldToNew.put("block.slime.break", "block.slime_block.break");
        NamedSoundRewriter.oldToNew.put("block.slime.fall", "block.slime_block.fall");
        NamedSoundRewriter.oldToNew.put("block.slime.hit", "block.slime_block.hit");
        NamedSoundRewriter.oldToNew.put("block.slime.place", "block.slime_block.place");
        NamedSoundRewriter.oldToNew.put("block.slime.step", "block.slime_block.step");
        NamedSoundRewriter.oldToNew.put("block.stone_pressureplate.click_off", "block.stone_pressure_plate.click_off");
        NamedSoundRewriter.oldToNew.put("block.stone_pressureplate.click_on", "block.stone_pressure_plate.click_on");
        NamedSoundRewriter.oldToNew.put("block.waterlily.place", "block.lily_pad.place");
        NamedSoundRewriter.oldToNew.put("block.wood_pressureplate.click_off", "block.wooden_pressure_plate.click_off");
        NamedSoundRewriter.oldToNew.put("block.wood_button.click_on", "block.wooden_button.click_on");
        NamedSoundRewriter.oldToNew.put("block.wood_button.click_off", "block.wooden_button.click_off");
        NamedSoundRewriter.oldToNew.put("block.wood_pressureplate.click_on", "block.wooden_pressure_plate.click_on");
        NamedSoundRewriter.oldToNew.put("entity.armorstand.break", "entity.armor_stand.break");
        NamedSoundRewriter.oldToNew.put("entity.armorstand.fall", "entity.armor_stand.fall");
        NamedSoundRewriter.oldToNew.put("entity.armorstand.hit", "entity.armor_stand.hit");
        NamedSoundRewriter.oldToNew.put("entity.armorstand.place", "entity.armor_stand.place");
        NamedSoundRewriter.oldToNew.put("entity.bobber.retrieve", "entity.fishing_bobber.retrieve");
        NamedSoundRewriter.oldToNew.put("entity.bobber.splash", "entity.fishing_bobber.splash");
        NamedSoundRewriter.oldToNew.put("entity.bobber.throw", "entity.fishing_bobber.throw");
        NamedSoundRewriter.oldToNew.put("entity.enderdragon.ambient", "entity.ender_dragon.ambient");
        NamedSoundRewriter.oldToNew.put("entity.enderdragon.death", "entity.ender_dragon.death");
        NamedSoundRewriter.oldToNew.put("entity.enderdragon.flap", "entity.ender_dragon.flap");
        NamedSoundRewriter.oldToNew.put("entity.enderdragon.growl", "entity.ender_dragon.growl");
        NamedSoundRewriter.oldToNew.put("entity.enderdragon.hurt", "entity.ender_dragon.hurt");
        NamedSoundRewriter.oldToNew.put("entity.enderdragon.shoot", "entity.ender_dragon.shoot");
        NamedSoundRewriter.oldToNew.put("entity.enderdragon_fireball.explode", "entity.dragon_fireball.explode");
        NamedSoundRewriter.oldToNew.put("entity.endereye.death", "entity.ender_eye.death");
        NamedSoundRewriter.oldToNew.put("entity.endereye.launch", "entity.ender_eye.launch");
        NamedSoundRewriter.oldToNew.put("entity.endermen.ambient", "entity.enderman.ambient");
        NamedSoundRewriter.oldToNew.put("entity.endermen.death", "entity.enderman.death");
        NamedSoundRewriter.oldToNew.put("entity.endermen.hurt", "entity.enderman.hurt");
        NamedSoundRewriter.oldToNew.put("entity.endermen.scream", "entity.enderman.scream");
        NamedSoundRewriter.oldToNew.put("entity.endermen.stare", "entity.enderman.stare");
        NamedSoundRewriter.oldToNew.put("entity.endermen.teleport", "entity.enderman.teleport");
        NamedSoundRewriter.oldToNew.put("entity.enderpearl.throw", "entity.ender_pearl.throw");
        NamedSoundRewriter.oldToNew.put("entity.evocation_illager.ambient", "entity.evoker.ambient");
        NamedSoundRewriter.oldToNew.put("entity.evocation_illager.cast_spell", "entity.evoker.cast_spell");
        NamedSoundRewriter.oldToNew.put("entity.evocation_illager.death", "entity.evoker.death");
        NamedSoundRewriter.oldToNew.put("entity.evocation_illager.hurt", "entity.evoker.hurt");
        NamedSoundRewriter.oldToNew.put("entity.evocation_illager.prepare_attack", "entity.evoker.prepare_attack");
        NamedSoundRewriter.oldToNew.put("entity.evocation_illager.prepare_summon", "entity.evoker.prepare_summon");
        NamedSoundRewriter.oldToNew.put("entity.evocation_illager.prepare_wololo", "entity.evoker.prepare_wololo");
        NamedSoundRewriter.oldToNew.put("entity.firework.blast", "entity.firework_rocket.blast");
        NamedSoundRewriter.oldToNew.put("entity.firework.blast_far", "entity.firework_rocket.blast_far");
        NamedSoundRewriter.oldToNew.put("entity.firework.large_blast", "entity.firework_rocket.large_blast");
        NamedSoundRewriter.oldToNew.put("entity.firework.large_blast_far", "entity.firework_rocket.large_blast_far");
        NamedSoundRewriter.oldToNew.put("entity.firework.launch", "entity.firework_rocket.launch");
        NamedSoundRewriter.oldToNew.put("entity.firework.shoot", "entity.firework_rocket.shoot");
        NamedSoundRewriter.oldToNew.put("entity.firework.twinkle", "entity.firework_rocket.twinkle");
        NamedSoundRewriter.oldToNew.put("entity.firework.twinkle_far", "entity.firework_rocket.twinkle_far");
        NamedSoundRewriter.oldToNew.put("entity.illusion_illager.ambient", "entity.illusioner.ambient");
        NamedSoundRewriter.oldToNew.put("entity.illusion_illager.cast_spell", "entity.illusioner.cast_spell");
        NamedSoundRewriter.oldToNew.put("entity.illusion_illager.death", "entity.illusioner.death");
        NamedSoundRewriter.oldToNew.put("entity.illusion_illager.hurt", "entity.illusioner.hurt");
        NamedSoundRewriter.oldToNew.put("entity.illusion_illager.mirror_move", "entity.illusioner.mirror_move");
        NamedSoundRewriter.oldToNew.put("entity.illusion_illager.prepare_blindness", "entity.illusioner.prepare_blindness");
        NamedSoundRewriter.oldToNew.put("entity.illusion_illager.prepare_mirror", "entity.illusioner.prepare_mirror");
        NamedSoundRewriter.oldToNew.put("entity.irongolem.attack", "entity.iron_golem.attack");
        NamedSoundRewriter.oldToNew.put("entity.irongolem.death", "entity.iron_golem.death");
        NamedSoundRewriter.oldToNew.put("entity.irongolem.hurt", "entity.iron_golem.hurt");
        NamedSoundRewriter.oldToNew.put("entity.irongolem.step", "entity.iron_golem.step");
        NamedSoundRewriter.oldToNew.put("entity.itemframe.add_item", "entity.item_frame.add_item");
        NamedSoundRewriter.oldToNew.put("entity.itemframe.break", "entity.item_frame.break");
        NamedSoundRewriter.oldToNew.put("entity.itemframe.place", "entity.item_frame.place");
        NamedSoundRewriter.oldToNew.put("entity.itemframe.remove_item", "entity.item_frame.remove_item");
        NamedSoundRewriter.oldToNew.put("entity.itemframe.rotate_item", "entity.item_frame.rotate_item");
        NamedSoundRewriter.oldToNew.put("entity.leashknot.break", "entity.leash_knot.break");
        NamedSoundRewriter.oldToNew.put("entity.leashknot.place", "entity.leash_knot.place");
        NamedSoundRewriter.oldToNew.put("entity.lightning.impact", "entity.lightning_bolt.impact");
        NamedSoundRewriter.oldToNew.put("entity.lightning.thunder", "entity.lightning_bolt.thunder");
        NamedSoundRewriter.oldToNew.put("entity.lingeringpotion.throw", "entity.lingering_potion.throw");
        NamedSoundRewriter.oldToNew.put("entity.magmacube.death", "entity.magma_cube.death");
        NamedSoundRewriter.oldToNew.put("entity.magmacube.hurt", "entity.magma_cube.hurt");
        NamedSoundRewriter.oldToNew.put("entity.magmacube.jump", "entity.magma_cube.jump");
        NamedSoundRewriter.oldToNew.put("entity.magmacube.squish", "entity.magma_cube.squish");
        NamedSoundRewriter.oldToNew.put("entity.parrot.imitate.enderdragon", "entity.parrot.imitate.ender_dragon");
        NamedSoundRewriter.oldToNew.put("entity.parrot.imitate.evocation_illager", "entity.parrot.imitate.evoker");
        NamedSoundRewriter.oldToNew.put("entity.parrot.imitate.illusion_illager", "entity.parrot.imitate.illusioner");
        NamedSoundRewriter.oldToNew.put("entity.parrot.imitate.magmacube", "entity.parrot.imitate.magma_cube");
        NamedSoundRewriter.oldToNew.put("entity.parrot.imitate.vindication_illager", "entity.parrot.imitate.vindicator");
        NamedSoundRewriter.oldToNew.put("entity.player.splash.highspeed", "entity.player.splash.high_speed");
        NamedSoundRewriter.oldToNew.put("entity.polar_bear.baby_ambient", "entity.polar_bear.ambient_baby");
        NamedSoundRewriter.oldToNew.put("entity.small_magmacube.death", "entity.magma_cube.death_small");
        NamedSoundRewriter.oldToNew.put("entity.small_magmacube.hurt", "entity.magma_cube.hurt_small");
        NamedSoundRewriter.oldToNew.put("entity.small_magmacube.squish", "entity.magma_cube.squish_small");
        NamedSoundRewriter.oldToNew.put("entity.small_slime.death", "entity.slime.death_small");
        NamedSoundRewriter.oldToNew.put("entity.small_slime.hurt", "entity.slime.hurt_small");
        NamedSoundRewriter.oldToNew.put("entity.small_slime.jump", "entity.slime.jump_small");
        NamedSoundRewriter.oldToNew.put("entity.small_slime.squish", "entity.slime.squish_small");
        NamedSoundRewriter.oldToNew.put("entity.snowman.ambient", "entity.snow_golem.ambient");
        NamedSoundRewriter.oldToNew.put("entity.snowman.death", "entity.snow_golem.death");
        NamedSoundRewriter.oldToNew.put("entity.snowman.hurt", "entity.snow_golem.hurt");
        NamedSoundRewriter.oldToNew.put("entity.snowman.shoot", "entity.snow_golem.shoot");
        NamedSoundRewriter.oldToNew.put("entity.villager.trading", "entity.villager.trade");
        NamedSoundRewriter.oldToNew.put("entity.vindication_illager.ambient", "entity.vindicator.ambient");
        NamedSoundRewriter.oldToNew.put("entity.vindication_illager.death", "entity.vindicator.death");
        NamedSoundRewriter.oldToNew.put("entity.vindication_illager.hurt", "entity.vindicator.hurt");
        NamedSoundRewriter.oldToNew.put("entity.zombie.attack_door_wood", "entity.zombie.attack_wooden_door");
        NamedSoundRewriter.oldToNew.put("entity.zombie.break_door_wood", "entity.zombie.break_wooden_door");
        NamedSoundRewriter.oldToNew.put("entity.zombie_pig.ambient", "entity.zombie_pigman.ambient");
        NamedSoundRewriter.oldToNew.put("entity.zombie_pig.angry", "entity.zombie_pigman.angry");
        NamedSoundRewriter.oldToNew.put("entity.zombie_pig.death", "entity.zombie_pigman.death");
        NamedSoundRewriter.oldToNew.put("entity.zombie_pig.hurt", "entity.zombie_pigman.hurt");
        NamedSoundRewriter.oldToNew.put("record.11", "music_disc.11");
        NamedSoundRewriter.oldToNew.put("record.13", "music_disc.13");
        NamedSoundRewriter.oldToNew.put("record.blocks", "music_disc.blocks");
        NamedSoundRewriter.oldToNew.put("record.cat", "music_disc.cat");
        NamedSoundRewriter.oldToNew.put("record.chirp", "music_disc.chirp");
        NamedSoundRewriter.oldToNew.put("record.far", "music_disc.far");
        NamedSoundRewriter.oldToNew.put("record.mall", "music_disc.mall");
        NamedSoundRewriter.oldToNew.put("record.mellohi", "music_disc.mellohi");
        NamedSoundRewriter.oldToNew.put("record.stal", "music_disc.stal");
        NamedSoundRewriter.oldToNew.put("record.strad", "music_disc.strad");
        NamedSoundRewriter.oldToNew.put("record.wait", "music_disc.wait");
        NamedSoundRewriter.oldToNew.put("record.ward", "music_disc.ward");
    }
}
