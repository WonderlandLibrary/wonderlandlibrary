package Method.Client.utils.Screens.Custom;

import Method.Client.Main;
import Method.Client.managers.FriendManager;
import Method.Client.managers.OtherPlayerManager;
import Method.Client.utils.Screens.ListGui;
import Method.Client.utils.Screens.SubGui;
import Method.Client.utils.visual.RenderUtils;
import Method.Client.utils.visual.SimpleButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class PlayerActivity extends SubGui {

    public PlayerActivity() {
        super("PlayerActivity");
    }

    @Override
    public ArrayList<SelectedThing> getItems() {
        ArrayList<SelectedThing> items = new ArrayList<>();
        for (OtherPlayerManager.EntityofIntrest entity : Main.PlayerManager.Entities) {
            SelectedThing selectedThing = new SelectedThing(entity.getEntity().getName(), false,entity.getEntity());
            items.add(selectedThing);
        }
        return items;
    }

    @Override
    public void open() {
        listGui = new ListGui(this.getItems(), this);
        this.x = 100;
        this.y = 100;
        this.width = 500;
    }

    @Override
    public ArrayList<SimpleButton> getButtons() {
        ArrayList<SimpleButton> buttons = new ArrayList<>();
        buttons.add(new SimpleButton(0, 10, 10, 100, 20, "Delete", "Delete"));
        buttons.add(new SimpleButton(1, 10, 10, 100, 20, "Friend", "Add Friend"));
        buttons.add(new SimpleButton(2, 10, 10, 100, 20, "UnFriend", "Remove Friend"));
        buttons.add(new SimpleButton(3, 10, 10, 100, 20, "seenLocations", "seenLocations"));
        buttons.add(new SimpleButton(4, 10, 10, 100, 20, "Teleport", "Teleported locations"));
        buttons.add(new SimpleButton(5, 10, 10, 100, 20, "Default", "Default"));
        return buttons;
    }

    @Override
    public boolean elementClicked(SelectedThing selectedThing) {
        listGui.list.forEach(thing -> thing.isSelected = false);
        return true;
    }

    @Override
    public void buttonPressed(SimpleButton button) {
        if (button.id == 0) {
            for (SelectedThing item : listGui.list) {
                if (item.isSelected) {
                    OtherPlayerManager.EntityofIntrest remove = null;
                    for (OtherPlayerManager.EntityofIntrest entity : Main.PlayerManager.Entities) {
                        if (entity.getEntity() == item.entity)
                            remove = entity;
                    }
                    if (remove != null)
                        Main.PlayerManager.Entities.remove(remove);
                }
            }
        } else if (button.id == 1) {
            if (listGui.lastSelected != -1 && listGui.list.get(listGui.lastSelected) != null) {
                FriendManager.addFriend(listGui.list.get(listGui.lastSelected).name);
               // ChatUtils.message(listGui.list.get(listGui.lastSelected).name + " has been added to your friends list.");
            }
        } else if (button.id == 2) {
            if (listGui.lastSelected != -1 && listGui.list.get(listGui.lastSelected) != null) {
                FriendManager.removeFriend(listGui.list.get(listGui.lastSelected).name);
             //   ChatUtils.message(listGui.list.get(listGui.lastSelected) + " has been removed from your friends list.");
            }
        } else if (button.id == 3) {
            if (listGui.lastSelected != -1) {
                listGui.temp = new ArrayList<>();
                for (OtherPlayerManager.EntityofIntrest entity : Main.PlayerManager.Entities) {
                    if (entity.getEntity() == listGui.list.get(listGui.lastSelected).entity) {
                        listGui.temp.add(new SelectedThing(entity.getEntity().getName(), false, entity.getEntity()));
                        for (Vec3d vec3d : entity.getLocationsSeen()) {
                            DecimalFormat df = new DecimalFormat("#.##");
                            String pos = "X: " + df.format(vec3d.x) + " Y: " + df.format(vec3d.y) + " Z: " + df.format(vec3d.z);
                            listGui.temp.add(new SelectedThing(pos, false, entity.getEntity()));
                        }
                    }
                }
            }
        } else if (button.id == 4) {
            listGui.temp = new ArrayList<>();
            for (OtherPlayerManager.EntityofIntrest entity : Main.PlayerManager.Entities) {
                if (entity.getEntity() == listGui.list.get(listGui.lastSelected).entity) {
                    listGui.temp.add(new SelectedThing(entity.getEntity().getName(), false, entity.getEntity()));
                    for (Vec3d vec3d : entity.getTeleportedLocation()) {
                        DecimalFormat df = new DecimalFormat("#.##");
                        String pos = "X: " + df.format(vec3d.x) + " Y: " + df.format(vec3d.y) + " Z: " + df.format(vec3d.z);
                        listGui.temp.add(new SelectedThing(pos, false, entity.getEntity()));
                    }
                }
            }
        } else if (button.id == 5) {
            listGui.temp = null;
        }
    }

    @Override
    public void drawSlot(String name, boolean isSelected, int slotIdx, int entryRight, int slotTop,
                         int slotBuffer, Tessellator tess) {
        if (listGui.temp != null)
            RenderUtils.scaledText(name, x + 5, slotTop, new Color(25, 211, 125, 1).getRGB(), true);
        else if (isSelected)
            RenderUtils.scaledText(name, x + 5, slotTop, new Color(25, 211, 25, 1).getRGB(), true);
        else
            RenderUtils.scaledText(name, x + 5, slotTop, new Color(211, 25, 25, 1).getRGB(), true);

    }


}
