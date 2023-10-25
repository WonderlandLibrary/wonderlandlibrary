package tech.dort.dortware.impl.modules.misc;

import com.google.common.eventbus.Subscribe;
import org.apache.commons.lang3.RandomStringUtils;
import tech.dort.dortware.api.module.Module;
import tech.dort.dortware.api.module.ModuleData;
import tech.dort.dortware.api.property.impl.EnumValue;
import tech.dort.dortware.api.property.impl.NumberValue;
import tech.dort.dortware.api.property.impl.interfaces.INameable;
import tech.dort.dortware.impl.events.UpdateEvent;
import tech.dort.dortware.impl.utils.player.ChatUtil;
import tech.dort.dortware.impl.utils.time.Stopwatch;

public class Spammer extends Module {

    private int index;
    private final EnumValue<Mode> enumValue = new EnumValue<>("Mode", this, Spammer.Mode.values());
    private final NumberValue delay = new NumberValue("Delay", this, 3, 1, 10, true);
    private final Stopwatch stopwatch = new Stopwatch();
    private final String[] jakePaul = {
            "Y'all can't handle this\n",
            "Y'all don't know what's about to happen baby\n",
            "Team 10\n",
            "Los Angeles, Cali boy\n",
            "But I'm from Ohio though, white boy\n",
            "It's everyday bro, with the Disney Channel flow\n",
            "5 mill on YouTube in 6 months, never done before\n",
            "Passed all the competition man, PewDiePie is next\n",
            "Man I'm poppin' all these checks, got a brand new Rolex\n",
            "And I met a Lambo too and I'm coming with the crew\n",
            "This is Team 10, bitch, who the hell are flippin' you?\n",
            "And you know I kick them out if they ain't with the crew\n",
            "Yeah, I'm talking about you, you beggin' for attention\n",
            "Talking shit on Twitter too but you still hit my phone last night\n",
            "It was 4:52 and I got the text to prove\n",
            "And all the recordings too, don't make me tell them the truth\n",
            "And I just dropped some new merch and it's selling like a god, church\n",
            "Ohio's where I'm from, we chew 'em like it's gum\n",
            "We shooting with a gun, the tattoo just for fun\n",
            "I Usain Bolt and run, catch me at game one\n",
            "I cannot be outdone, Jake Paul is number one\n",
            "It's everyday bro\n",
            "It's everyday bro\n",
            "It's everyday bro\n",
            "I said it is everyday bro!\n",
            "You know it's Nick Crompton and my collar stay poppin'\n",
            "Yes, I can rap and no, I am not from Compton\n",
            "England is my city\n",
            "And if it weren't for Team 10, then the US would be shitty\n",
            "I'll pass it to Chance 'cause you know he stay litty\n",
            "Two months ago you didn't know my name\n",
            "And now you want my fame? Bitch I'm blowin' up\n",
            "I'm only going up, now I'm going off, I'm never fallin' off\n",
            "Like Mag, who? Digi who? Who are you?\n",
            "All these beefs I just ran through, hit a milli in a month\n",
            "Where were you? Hatin' on me back in West Fake\n",
            "You need to get your shit straight\n",
            "Jakey brought me to the top, now we're really poppin' off\n",
            "Number one and number four, that's why these fans all at our door\n",
            "It's lonely at the top so we all going\n",
            "We left Ohio, now the trio is all rollin'\n",
            "It's Team 10, bitch\n",
            "We back again, always first, never last\n",
            "We the future, we'll see you in the past\n",
            "It's everyday bro\n",
            "It's everyday bro\n",
            "It's everyday bro\n",
            "I said it is everyday bro!\n",
            "Hold on, hold on, hold on, hold on (espera)\n",
            "Can we switch the language? (Ha, ya tú sabes)\n",
            "We 'bout to hit it (dale)\n",
            "Sí, lo unico que quiero es dinero\n",
            "Trabajando en YouTube todo el día entero\n",
            "Viviendo en U.S.A, el sueño de cualquiera\n",
            "Enviando dólares a mi familia entera\n",
            "Tenemos una persona por encima\n",
            "Se llama Donald Trump y está en la cima\n",
            "Desde aquí te cantamos can I get my VISA?\n",
            "Martinez Twins, representando España\n",
            "Desde la pobreza a la fama\n",
            "It's everyday bro\n",
            "It's everyday bro\n",
            "It's everyday bro\n",
            "I said it is everyday bro!\n",
            "Yo, it's Tessa Brooks\n",
            "The competition shook\n",
            "These guys up on me\n",
            "I got 'em with the hook\n",
            "Lemme educate ya'\n",
            "And I ain't talking book\n",
            "Panera is your home?\n",
            "So, stop calling my phone\n",
            "I'm flyin' like a drone\n",
            "They buying like a loan\n",
            "Yeah, I smell good\n",
            "Is that your boy's cologne?\n",
            "Is that your boy's cologne?\n",
            "Started balling', quicken Loans\n",
            "Now I'm in my flippin' zone\n",
            "Yes, they all copy me\n",
            "But, that's some shitty clones\n",
            "Stay in all designer clothes\n",
            "And they ask me what I make\n",
            "I said is 10 with six zeros\n",
            "Always plug, merch link in bio\n",
            "And I will see you tomorrow 'cause it's everyday bro\n",
            "Peace\n",
            "Ya tu sabes baby, Jake Paul"
    };

    public static String spamString = "Spam!";

    public Spammer(ModuleData moduleData) {
        super(moduleData);
        register(enumValue, delay);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (stopwatch.timeElapsed((long) (delay.getValue() * 1000L))) {
            switch (enumValue.getValue()) {
                case JAKE_PAUL:
                    try {
                        mc.thePlayer.sendChatMessage(jakePaul[index++]);
                    } catch (ArrayIndexOutOfBoundsException exc) {
                        index = 0;
                    }
                    stopwatch.resetTime();
                    break;
                case CUSTOM:
                    mc.thePlayer.sendChatMessage(spamString + " - " + RandomStringUtils.randomAlphabetic(32));
                    stopwatch.resetTime();
                    break;
            }
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        stopwatch.resetTime();
        index = 0;
        ChatUtil.displayChatMessage("Use .spam [message] to edit the spam message.");
    }

    public enum Mode implements INameable {

        CUSTOM("Custom"), JAKE_PAUL("Jake Paul");

        private final String name;

        Mode(String name) {
            this.name = name;
        }

        @Override
        public String getDisplayName() {
            return name;
        }
    }
}
