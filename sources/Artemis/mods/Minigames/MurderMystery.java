package artemis.mods.Minigames;

//import java.util.Iterator;

import org.lwjgl.input.Keyboard;

import artemis.Category;
import artemis.module;
import artemis.notification.Notification;
import artemis.notification.NotificationManager;
import artemis.notification.NotificationType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MurderMystery extends module{
		  public MurderMystery() {
		    super("MurderMystery", "Finds the Murderer in MurderMystery", Keyboard.KEY_NONE, 11, Category.OTHER);
		  }
		  
		  public void onDisable() {
		    super.onDisable();
		  }
		  
		  public void onUpdate() {
		    if (isToggled()) {
		    	for(Object e : mc.theWorld.loadedEntityList) {
					if (e instanceof EntityPlayer) {
						EntityPlayer p = (EntityPlayer) e;
						for (int i = 0; i < p.inventoryContainer.getInventory().size(); i++) {
							ItemStack stack = (ItemStack) p.inventoryContainer.getInventory().get(i);
							if (stack != null) {
								if (stack.getItem() == Item.getByNameOrId("267")) {
										// by SuperBlauBeere27
										NotificationManager.show(
												new Notification(NotificationType.INFO, "The Murderer is:", p.getName(), 10));
										this.toggle();
										//this.time.clear();
									}
								}
							}
						}
					}
				}
		      super.onUpdate();
		    } 
		  }
		

