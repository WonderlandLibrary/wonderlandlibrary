package rip.helium.cheat;

import rip.helium.cheat.commands.Command;
import rip.helium.cheat.commands.cmds.*;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    public List<Command> cmds = new ArrayList<>();


    public CommandManager() {
        this.cmds.add(new VClip());
        this.cmds.add(new Friend());
        this.cmds.add(new Username());
        this.cmds.add(new Clientname());
        this.cmds.add(new StaffDec());
        this.cmds.add(new Teleport());
        this.cmds.add(new Bind());
        this.cmds.add(new Toggle());
        this.cmds.add(new Help());
        this.cmds.add(new Focus());
        this.cmds.add(new ChangeColor());

    }
}
