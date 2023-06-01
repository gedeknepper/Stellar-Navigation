package io.github.carrotbane.stellarnav;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class StarHandler {
	
	static int starGridSize = 60; //grid is actually (2n+1) x (2n+1) big
	static double skyHeight = 10.0; //this is in units of chunks
	static int skySize = 512;
	
	// does the chunk in question have a star?
	public static boolean hasStar(double x, double z) {
//		return x/(z*z+1) % 3 == 0;
		return x*z % 7 == 3 && x*z % 5 == 3;
	}
	

	@OnlyIn(Dist.CLIENT)
	public static void makePics(int x, int z) throws IOException {
		
		NativeImage img = new NativeImage(8*skySize, skySize, true); // the bool here is something about memory allocation??
		// I'm making it 8x1 instead of 6x1 because it makes the arithmetic easier; in particular, 1/6 is a repeating decimal
		// The subsets of img are like this:
		// DNSUEW--
		img.fillRect(0, 0, 8*skySize, skySize, 255 << 24 | 0 << 16 | 0 << 8 | 0); // make a black background

		for(double j = -starGridSize; j<=starGridSize; j++) {
			for(double k = -starGridSize; k<=starGridSize; k++) {
				if(hasStar(x+j, z+k)) {
					System.out.println("j: " + j + ", k: " + k);
					double alt = Math.atan(skyHeight/Math.sqrt(Math.pow(j, 2)+Math.pow(k, 2)));
//					double alt = Math.PI/3;
					double az = j >= 0 ? Math.atan(k/j) + Math.PI/2 : Math.atan(k/j)+ 1.5*Math.PI;  
//					double az = Math.PI;

					char face = face(alt, az);
					Vector2f texLoc = texLoc(alt, az, face);
					System.out.println("texLoc: " + texLoc.x + ", " + texLoc.y);
					int starX = (int) Math.floor(texLoc.x*(8*skySize-1));
					int starY = (int) Math.floor(texLoc.y*(skySize-1));
					if(starX == 3*skySize - 1 && face == 'u') {
						starX++;
					}
					System.out.println("alt: " + alt + ", az: " + az);
					System.out.println("x = " + starX + ", y = " + starY);
					img.setPixelRGBA(starX, starY, 255 << 24 | 255 << 16 | 255 << 8 | 255);
					//the above method is called "setPixelRGBA", but actually it's not RGBA, it's ABGR.
					// It takes a 32-bit number representing color + opacity.
				}
			}
		}
		
		DynamicTexture tex = new DynamicTexture(img);
//		tex.bind();
		
//		System.out.println("texture id: " + tex.getId()); // This crashes with "Rendersystem called from wrong thread"
		
		ResourceLocation rl = new ResourceLocation(StellarNavMod.MOD_ID, "gen_sky.png");
		Minecraft.getInstance().textureManager.register(rl, tex);
	}
	
	public static char face(double alt, double az) {
		char face = 'n';
		double pi = Math.PI;
		double az2 = az;
		
		if(az > pi/4 && az <= 3*pi/4) {
			face = 'e';
			az2 = az-pi/2;
		}
		else if(az > 3*pi/4 && az <= 5*pi/4) {
			face = 's';
			az2 = az-pi;
		}
		else if(az > 5*pi/4 && az <= 7*pi/4) {
			face = 'w';
			az2 = az-3*pi/2;
		}
		else if(az > 7*pi/4) {
			az2 = az-2*pi;
		}
		
		if(Math.tan(alt) > Math.cos(az2) || alt > 0.9*Math.PI/2) {
			face = 'u';
		}
		if(face == 'u' && alt<0) {
			face = 'd';
		}
		return face;
	}
	
	//return the location on the texture, as a float between 0 and 1
	public static Vector2f texLoc(double alt, double az, char face) {
		float offset = 0;
		float x = 0;
		float y = 0;
		if(face == 'u') {
//			int s = (az < Math.PI) ? 1 : 3;
			if(az < Math.PI) {
				y = (float) (0.5*Math.abs(Math.tan(Math.PI/2-alt)*Math.cos(az-Math.PI/2)) + 0.5);
				x = (float) ((y-0.5)*Math.tan(az-Math.PI/2)+0.5);
			}
			else {
				y = (float) (-0.5*Math.abs(Math.tan(Math.PI/2-alt)*Math.cos(az-3*Math.PI/2)) + 0.5);
				x = (float) ((y-0.5)*Math.tan(az-Math.PI/2)+0.5);
			}
//			y = (float) (0.5*Math.abs(Math.tan(Math.PI/2-alt)*Math.cos(az-s*Math.PI/2)) + 0.5);
//			x = (float) ((y-0.5)*Math.tan(az-s*Math.PI/2)+0.5);
			if(az == 0) {
				x = (float) (-0.5*Math.tan(Math.PI/2-alt)+0.5);
				y = 0.5f;
			}
			if(az == Math.PI) {
				x = (float) (0.5*Math.tan(Math.PI/2-alt)+0.5);
				y = 0.5f;
			}
			if(alt == Math.PI/2) {
				x = 0.5f;
				y = 0.5f;
			}
			offset = 3*0.125f;
		}
		
		if(face == 'n') {
			offset = 0.125f;
		}
		else if(face == 'e') {
			az = az-Math.PI/2;;
			offset = 0.5f;
		}
		else if(face == 's') {
			az = az-Math.PI;
			offset = 0.25f;
		}
		else if(face == 'w') {
			az = az-3*Math.PI/2;
			offset = 5*0.125f;
		}

		if(face == 'n' || face == 'e' || face == 's' || face == 'w') {
			y = (float) (0.5*(-Math.tan(alt)/Math.cos(az)+1)); // I know this is hacky. Sorry.
			if(y<0)
				y = 0;
			x = (float) (0.5*(Math.tan(az)+1));
		}
		
		Vector2f result = new Vector2f(0.125f*x + offset, y);
		System.out.println("face: " + face + ", x: " + x + ", y: " + y + ", offset: " + offset);
		return result;
		
		
	}

}
