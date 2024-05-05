package artemis.mods.Other;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import artemis.Category;
import artemis.module;
import net.minecraft.client.Minecraft;

public class Jargon extends module{

	
	public Jargon() {
		super("Jargon", "Makes you smart", 0, 6876, Category.OTHER);
	
	}

	
@Override
public void onUpdate() {
	if(this.isToggled()) {
	givenList_whenNumberElementsChosen_shouldReturnRandomElementsRepeat();
}
}
public void givenList_whenNumberElementsChosen_shouldReturnRandomElementsRepeat() {
    Random rand = new Random();
    List<String> givenList = Arrays.asList("I'll hack the back-end PPPoE system, that should index the HTTP firewall.", 
    		"Generating the card won't do anything, we need to transcode the analog RX program!",
    		"They're inside the hard drive, use the optical SMTP interface to compress their system!",
    		"Send the solid state form factor into the port, it will back up the firewall by generating its COM transmitter!");
    int numberOfElements = 2;
 
    for (int i = 0; i < numberOfElements; i++) {
        int randomIndex = rand.nextInt(givenList.size());
        String randomElement = givenList.get(randomIndex);
        Minecraft.getMinecraft().thePlayer.sendChatMessage(randomElement);
       this.onDisable();
    }
    
}
}