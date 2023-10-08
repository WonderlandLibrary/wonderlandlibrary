/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.other;

import dev.rise.Rise;
import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.other.AttackEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.util.player.PlayerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.lang3.RandomUtils;

@ModuleInfo(name = "Insults", description = "Insults people when you kill them", category = Category.OTHER)
public final class Insults extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", this, "Normal", "Normal", "Fag", "Sigma");

    private EntityPlayer target;

    @Override
    public void onAttackEvent(final AttackEvent event) {
        final Entity entity = event.getTarget();

        if (entity instanceof EntityPlayer)
            target = (EntityPlayer) entity;
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (target != null && !(PlayerUtil.isOnServer("minemenclub") || PlayerUtil.isOnServer("minemanclub"))) {
            if (!mc.theWorld.playerEntities.contains(target) && mc.thePlayer.getDistanceSq(target.posX, mc.thePlayer.posY, target.posZ) < 900) {
                if (mc.thePlayer.ticksExisted > 20) {
                    switch (mode.getMode()) {
                        case "Fag":
                            mc.thePlayer.sendChatMessage(fags[RandomUtils.nextInt(0, fags.length)].replaceAll(":user:", target.getName()));
                            break;

                        case "Normal":
                            mc.thePlayer.sendChatMessage(insults[RandomUtils.nextInt(0, insults.length)].replaceAll(":user:", target.getName()));
                            break;

                        case "Sigma":
                            mc.thePlayer.sendChatMessage(sigma[RandomUtils.nextInt(0, sigma.length)].replaceAll(":user:", target.getName()));
                            break;
                    }
                }
                target = null;
            }
        }
    }

    private final String[] insults = {
            "Did :user: pay for that loss?",
            "Get Rise from intent.store",
            "Did :user:'s dad not come back after he wanted to buy some milk?",
            "RISE against other cheaters by getting Rise from intent.store",
            "Are you afraid of me",
            "Why not use Rise?",
            "#SwitchToRise5",
            "Did :user: forget to left click?",
            ":user: takes up 2 seats on the bus",
            "It is impossible to miss :user: with their man boobs",
            "Come on :user:, report me to the obese staff",
            ":user: is the type to overdose on Benadryl for a Tiktok video",
            "No wonder :user: dropped out of college",
            "Here's your ticket to spectator",
            ":user: said they would never give me up and never let me down, I am sad",
            "The latest update to Rise fps booster client gave me 1000 fps and regedits for better velocity",
            ":user: became Transgender just to join the 50% a day later",
            "Drink hand sanitizer so we can get rid of :user:",
            "Even the MC Virgins are less virgin than :user:",
            ":user:'s free trial of life has expired",
            ":user: is socially awkward",
            "I bet :user: believes in the flat earth",
            ":user: is the reason why society is failing",
            "Pay to lose",
            "Get good, get Rise @ intent.store",
            "Why would I be cheating when I am recording?",
            ":user: is such a that degenerate :user: believes EQ has more value than IQ",
            "The air could've took :user: away because of how weak :user: is",
            "Even Kurt Cobain is more alive than :user: with his wounds from a shotgun and heroin in his veins",
            ":user: is breaking down more than Nirvana after Kurt Cobain's death",
            "Does :user: buy their groceries at the dollar store?",
            "Does :user: need some pvp advice?",
            "I'd smack :user:, but that would be animal abuse",
            "I don't cheat, :user: just needs to click faster",
            "Welcome to my rape dungeon! population: :user:!",
            ":user: pressed the wrong button when they installed Minecraft?",
            "If the body is 70% water than how is :user:'s body 100% salt?",
            "Rise " + Rise.CLIENT_VERSION + " is sexier than :user:",
            "Oh, :user: is recording? Well I am too",
            ":user: is the type of person who would brute force interpolation",
            ":user: go drown in your own salt",
            ":user: is literally heavier than Overflow",
            "Excuse me :user:, I don't speak retard",
            "Hmm, the problem :user:'s having looks like a skin color issue",
            ":user: I swear I'm on Lunar Client",
            "Hey! Wise up :user:! Don't waste your time without Rise",
            ":user: didn't even stand a chance",
            "If opposites attract I hope :user: finds someone who is intelligent, honest and cultured",
            "If laughter is the best medicine, :user:'s face must be curing the world",
            ":user: is the type of person to climb over a glass wall to see what's on the other side",
            "What does :user:'s IQ and their girlfriend have in common? They're both below 5."
    };

    private final String[] fags = {
            "fag",
            "f4g",
            "f a g",
            "fa g",
            "f ag",
            "f 4 g"
    };

    private final String[] sigma = {
            "Learn your alphabet with the Rise client: Alan31, Rise, Tecnio, Auth!",
            "Download Rise to kick ass while listening to some badass music!",
            "Why Rise? Cause it is the addition of pure skill and incredible intellectual abilities",
            "Want some skills? Check out Rise at intent.store!",
            "You have been oofed by Rise oof oof",
            "I am not racist, but I only like Rise users. so git gud noobs",
            "Quick Quiz: I am Alan31's son, who am I? RISE",
            "Wow! My combo is Rise'n!",
            "What should I choose? Rise or Rise?",
            "Bigmama and Risemama",
            "I don't hack I just Rise",
            "Rise client is your new home",
            "Look a divinity! He definitely must use Rise!",
            "In need of a cute present for Christmas? Rise is all you need!",
            "I have a good Rise config, don't blame me",
            "Don't piss me off or you will discover the true power of Rise's inf reach",
            "Rise never dies",
            "Maybe I will be Rise, I am already Rise",
            "Rise will help you! Oops, i killed you instead.",
            "#NoHaxJustRise",
            "Do like Tenebrous, subscribe to 8Sence!",
            "Did I really just forget that melody? Ri ris ris ris Rise",
            "Rise. The only client run by speakers of Breton",
            "Order free baguettes with Rise client",
            "Another Rise user? Awww man",
            "Rise utility client no hax 100%",
            "Hypixel wants to know Rise owner's location [Accept] [Deny]",
            "I am a Rise-magician, thats how I am able to do all those block game tricks",
            "Stop it, get some help! Get Rise",
            "Rise users belike: Hit or miss I guess I never miss!",
            "I dont hack i just have Rise Gaming Chair",
            "Stop Hackustation me cuz im just Rise",
            "R. I. S. E. Hack with me today!",
            "Subscribe to 8Sence and Tecnio on youtube and discover Rise!",
            "Beauty is not in the face; beauty is in Rise",
            "Imagine using anything but Rise",
            "No hax just beta testing the anti-cheat with Rise",
            "Don't forget to report me for Rise on the forums!",
            "Get Rise hack client at intent.store to get the best mineman skills!",
            "don't use Rise? ok boomer",
            "Rise is better than Optifine",
            "It's not BlockFly it's Scaffold in Rise!",
            "How come a noob like you not use Rise?",
            "A mother becomes a true grandmother the day she gets Rise " + Rise.CLIENT_VERSION,
            "Fly faster than light, only available in Rise™",
            "Behind every Rise user, is an incredibly cool human being. Trust me, cooler than you.",
            "Hello Rise my old friend...",
            "#SwitchToRise5",
            "What? You've never downloaded Rise? You know it's the best right?",
            "Your client sucks, just get Rise",
            "Rise made this world a better place, killing you with it even more"
    };
}
