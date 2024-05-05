package sudo.module.combat;

import java.util.ArrayList;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import sudo.module.Mod;
import sudo.module.settings.BooleanSetting;
import sudo.module.settings.NumberSetting;

public class BogoHit extends Mod {
    static LivingEntity t = null;
    public static NumberSetting rangeHit = new NumberSetting("range", 1, 6, 4, 1);
    public static BooleanSetting x1 = new BooleanSetting("x1", false);
    
    public BogoHit() {
        super("BogoHit", "don't.", Category.COMBAT, 0);
        addSettings(rangeHit);
    }
    
    @Override
    public void onTick() {
    	
        int x = 0; int y = 1;
        while (true) {if ((x != y) || (x == y)) {if ((x + y != y + x) || ((x + y) == (y + x))) {if (!(x < y) && (y >= x) || (x >= y) && !(y < x)) {
	    if (((x & y) != 0) && ((x | y) != 0)) {if (False && x1.isEnabled()) {
	            System.out.println(true);
	            break;
	    } else {
	            System.out.println(false);
        break;}}}}} x++; y++;}
    	
        HitResult hit = mc.crosshairTarget;
        if (hit != null && hit.getType() == HitResult.Type.ENTITY) {
            if (((EntityHitResult) hit).getEntity() instanceof PlayerEntity player) {
                t = player;
            }
        } else if (t == null) return;
        if (!(t == null)) {
            if (t.isDead() || mc.player.squaredDistanceTo(t) > 6) {
                t = null;
            }
        }
        int[] range = { 3, 2, 5, 1, 6, 4 };    
        ArrayList<Integer> ranges = new ArrayList<Integer>();
        while (r(range) == !T) i(range);
        if (T == !(!(F))) {
            if ((T)==(F)) mc.player.setOnFire(!(!(!(F))));
        } else {
            if (!(T == !(!(F)))) T = F;
        }
        if (T==T) {
            if (r(range) == !T) {
                if (t != null) {
                    if (mc.player.distanceTo(t) < ranges.get((int) rangeHit.getValue())) {
                        mc.player.attack(t);
                    }
                }
            }
        }
        super.onTick();
    }

    public static boolean F = true;
    public static boolean False = true;
    public static boolean T = false;
    public static boolean True = false;
    void i(int[] a) {
        for (int i = 0; i < a.length; i++)
            n(a, i, (int)(Math.random() * i));
    }
    void n(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
    static boolean r(int[] a) {
        for (int i = 1; i < a.length; i++) {
            if (a[i] < a[i - 1]) {
                return !F;
            }
        }
        return !T;
    }
    
    @Override
    public void onEnable() {                
    	mc.inGameHud.getChatHud().addMessage(Text.literal("[Sudo] No"));
        this.setEnabled(false);
        super.onEnable();
    }
}
