// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.cmd.impl;

import ru.fluger.client.helpers.misc.ChatHelper;
import ru.fluger.client.cmd.CommandAbstract;

public class ClipCommand extends CommandAbstract
{
    bib mc;
    
    public ClipCommand() {
        super("vclip", "vclip | hclip", "§6.vclip | (hclip) §7<value>", new String[] { "vclip", "hclip" });
        this.mc = bib.z();
    }
    
    @Override
    public void execute(final String... args) {
        if (args.length > 1) {
            if (args[0].equalsIgnoreCase("vclip")) {
                try {
                    ChatHelper.addChatMessage(a.k + "Successfully vclipped " + Double.valueOf(args[1]) + " blocks.");
                    for (int i = 0; i < 10; ++i) {
                        this.mc.h.d.a(new lk.a(this.mc.h.p, this.mc.h.q, this.mc.h.r, false));
                    }
                    for (int i = 0; i < 10; ++i) {
                        this.mc.h.d.a(new lk.a(this.mc.h.p, this.mc.h.q + Double.parseDouble(args[1]), this.mc.h.r, false));
                    }
                    this.mc.h.b(this.mc.h.p, this.mc.h.q + Double.parseDouble(args[1]), this.mc.h.r);
                }
                catch (Exception ex) {}
            }
            if (args[0].equalsIgnoreCase("hclip")) {
                try {
                    ChatHelper.addChatMessage(a.k + "Successfully hclipped " + Double.valueOf(args[1]) + " blocks.");
                    final float f = this.mc.h.v * 0.017453292f;
                    final double speed = Double.valueOf(args[1]);
                    final double x = -(rk.a(f) * speed);
                    final double z = rk.b(f) * speed;
                    this.mc.h.b(this.mc.h.p + x, this.mc.h.q, this.mc.h.r + z);
                }
                catch (Exception ex2) {}
            }
        }
        else {
            ChatHelper.addChatMessage(this.getUsage());
        }
    }
}
