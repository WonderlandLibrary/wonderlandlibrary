package best.azura.client.util.crypt;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    public static String getPasswordHash(String passwordToHash){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update("X!L72xeMUczsd7TKhjrJp^BGwcK+hrGeYMYA@@g_!eBUkG&bM7d-X^XfuG$y2eH8uLXX6hwKz79bc2=x6XWMCG2uCy6mcBGTUd4ghHUsjZK9_$N53C+^CcB^AbLCFh-6y#dwycnY+v!fzy??Nq@XyQh8SnsJ--M!7Sm*y9!?@2HvG2HX_3CxWMTH&=ubadvP=mWXeUzHmUuDag^5gUh8KErK^V!#U*P%fZ9PzTrA+KPKpkb=Gtn6#hhC-EMb^G75S*EUSb6zf-ahsVTwn8rMys?6T3VCMJ46DqEGM5c4UaTRq5PM%k49+V+#e^Mef!**N$Ez$%8P!wQDT&Z!jpf%Nz$wt2r_MSx3v!CTS_x2EEXh^mK*=m^^yXK!a?2@6!g8gpR-MXWUw?KN7Zf^sP4e@u@x6S+j#!9!mJ6??5-4h?*tNR+sMdZg9TC4@ut-uzqw&Tx-vyYyLF-ka$e2kp%28FB^jqgjM4b$$geBcEbb7$UtBvy8Lfmm6h&Dtja^k+EcatS!u3+fd4bM362wmBK&S!j8ejNbENVLzmAxETJ++K=A?JYtATg*7kBu4_?Y3Q^qEwQ#7&W=pZ^fuzCgGR+TQWfpYhXHn#xc#9?-vYQqz2F3wGbbzMB?C9?sTNgqP#DfvBb%K7!7La?Mp=e2#&ZAkH%ZaLcmXd4*Ae+dyQFK8pEga?EywQQv94er8-er^Xeb?L?59cV-P?qS6!BRVtYX!6dT=N9BNheaNxsQyhnq!gF$n_+5*q6W!qDC!p&GD5rT!X9d?GMeZVv4Q@8&KH%tqJMTt5qE3pstghPP6P4FNsGEp3Pp9K6MwzUQSWWws3Cb%76KAK$Mqba^5jv2d85*?xrFYpCh3WBX2B?Y66A7-?5C=K&xTG6uwafZ@JjZ%fEq3Mhz*T=%*FMJV849c2!z#RVs%nqWTu@R=WPTY6&uEh^6rz*-*=3v+jKg&T_nz6vXG*nh-+xhv8nYqz&&5sg+rSQ+8D5YAUvhGDdpGRBK2t3EPffAUP9@xW67*sDJ^rukZ6WjK7?q%LdsML+TwFcMh=Gn!_2pK&xE98@JCwSrLLG_-%byK-xp#P8?-jxPawx2-J6SU8yF4NPsuHBYcf_WKQpSu756J4kg*L?jx9-E*Prws_&h6*k@cDjM9d$B&gZQxQ9Q!dw%bH7GM@83*n-&5PXEJenGTdSv?*XCU%-K7JMVg*D33h2*_!emuj-HJKcUm@XNdf2QgBUkmv#a584?QGNV?Zw4qa*4G^dfqJT_QWJXYHhmjH=xBkbF^te9TcdGM75xXU3E@W@VpbyRZa!!NR*7a@5LuAnpkABGT9RvZNNjYR#F?*F4EF5NQb5L+jNZ@n@!_bB+rPP&deLBBGpp6z2tNQZJ%W!vL?JERp$tS9X#mmgsaBU7ReQHpp@QAY-Avwhbu@m$vL?Hf9WU6yZNQk9zmEXNMC7dwn2-&3MEgDqJb9Nrmz82Hk$Vdy9Dy@$E+pvrbe_%zFKs!XF_DL@LPDauq#kEsjf#QcCtrVXG6nmYGM6_QG#6W2JET_3qBjsBR&hULzwrp-9h!mH__ZVRk!Nz4sX#k!f7K$ABn5gHkER*RYe_ebAmmKXeCg8S_EYR*#*tqRX#erF^kC*BG%wqwQC=w*J@FKY*Ms@rYq_qSP3Wy?U=6AJC^c&J$+QJ3Gy837#qUs2J5kH8?8eKEhSRCCEvgGe@+!tJeV@B$73=Wz8Gfb3!e4F^g*2q962X*KqAVm&+!cvSsb2v_sXMya#KTjLDtgCejwKwvu*rHFagprr?q_uSBu!JQ4s*NvwGZPf+rfwg*tNFHG53Q84cnj^693NwQtcWpLTzgTPufJnNs_&F5Qh68r*F*&apKdSE+kCHFQyX#HSV^%-&CU4HQmppBvfW%4*_&$$LXnt57trxVr3E4^748c3z@eb2zbk_*b@7J4&8m*!pMv+y=S^bL7*%sw5Be=qZR&p_xtx8#UBr4F7+F8#pd_wwdg7fV^+H&Pcg_GGqJ3mN4^k$p_zZyG!bC?T=sRzvf565CFm3S*^?_JMwV#UML?4UW23ney2M!g$k".getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public static String getHash(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] endBytes = md.digest(bytes);
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (Exception ignored) {}

        return null;
    }

}
