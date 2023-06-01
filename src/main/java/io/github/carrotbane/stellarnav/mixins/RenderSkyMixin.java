package io.github.carrotbane.stellarnav.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import io.github.carrotbane.stellarnav.StellarNavMod;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

@Mixin(WorldRenderer.class)
public class RenderSkyMixin {

	@Inject(method = "renderSky", at = @At("TAIL"), cancellable = true)
	protected void starPlane(MatrixStack matrices, float what, CallbackInfo ci) {
		
//		System.out.println("Started trying to add blue square.");
		
        RenderSystem.disableAlphaTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        		
		WorldRendererMixin rendererMixin = (WorldRendererMixin) this;
		
		Tessellator tessellator = Tessellator.getInstance();
	    BufferBuilder bufferBuilder = tessellator.getBuilder();
	    TextureManager textureManager = rendererMixin.getTextureManager();
	    
//        matrices.mulPose(Vector3f.XP.rotationDegrees(90.0F)); // this line transforms bottom face to north face, I think
//	    Textures textures = new Textures();

	    
		for (int i = 0; i < 6; i++) {
            // 0 = bottom
            // 1 = north
            // 2 = south
            // 3 = top
            // 4 = east
            // 5 = west
			
			matrices.pushPose();
			ResourceLocation rl = new ResourceLocation(StellarNavMod.MOD_ID, "gen_sky.png");
			textureManager.bind(rl);
			
            if (i == 1) {
                matrices.mulPose(Vector3f.XP.rotationDegrees(90.0F));
            } else if (i == 2) {
                matrices.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
                matrices.mulPose(Vector3f.YP.rotationDegrees(180.0F));
            } else if (i == 3) {
                matrices.mulPose(Vector3f.XP.rotationDegrees(180.0F));
                matrices.mulPose(Vector3f.YP.rotationDegrees(90.0F));
            } else if (i == 4) {
                matrices.mulPose(Vector3f.ZP.rotationDegrees(90.0F));
                matrices.mulPose(Vector3f.YP.rotationDegrees(-90.0F));
            } else if (i == 5) {
                matrices.mulPose(Vector3f.ZP.rotationDegrees(-90.0F));
                matrices.mulPose(Vector3f.YP.rotationDegrees(90.0F));
            }
//        Texture tex = this.textures.byId(i);


			Matrix4f matrix4f = matrices.last().pose();
			bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
			makeVertices(bufferBuilder, matrix4f, i);

			tessellator.end();
			matrices.popPose();
			RenderSystem.depthMask(true);
			RenderSystem.enableTexture();
			RenderSystem.disableBlend();
			RenderSystem.enableAlphaTest();
//	    System.out.println("Finished trying to add blue square");
			// ci.cancel();

		}
        
	}
	
	public void makeVertices(BufferBuilder bufferBuilder, Matrix4f matrix4f, int i) {

		bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, -100.0F).uv(i*0.125f, 0).color(1f, 1f, 1f, 1f).endVertex();
		bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, 100.0F).uv(i*0.125f, 1f).color(1f, 1f, 1f, 1f).endVertex();
		bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, 100.0F).uv(0.125f+i*0.125f, 1f).color(1f, 1f, 1f, 1f).endVertex();
		bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, -100.0F).uv(0.125f+i*0.125f, 0).color(1f, 1f, 1f, 1f).endVertex();
		
//		if(i==1) {
//			bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, -100.0F).uv(0, 0).color(1f, 1f, 1f, 1f).endVertex();
//			bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, 100.0F).uv(0, 0.25f).color(1f, 1f, 1f, 1f).endVertex();
//			bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, 100.0F).uv(0.25f, 0.25f).color(1f, 1f, 1f, 1f).endVertex();
//			bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, -100.0F).uv(0.25f, 0).color(1f, 1f, 1f, 1f).endVertex();
//		}
//		else if(i==4) {
//			bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, -100.0F).uv(0.25f, 0).color(1f, 1f, 1f, 1f).endVertex();
//			bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, 100.0F).uv(0.25f, 0.25f).color(1f, 1f, 1f, 1f).endVertex();
//			bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, 100.0F).uv(0.5f, 0.25f).color(1f, 1f, 1f, 1f).endVertex();
//			bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, -100.0F).uv(0.5f, 0).color(1f, 1f, 1f, 1f).endVertex();
//		}
//		else if(i==2) {
//			bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, -100.0F).uv(0.5f, 0).color(1f, 1f, 1f, 1f).endVertex();
//			bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, 100.0F).uv(0.5f, 0.25f).color(1f, 1f, 1f, 1f).endVertex();
//			bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, 100.0F).uv(0.75f, 0.25f).color(1f, 1f, 1f, 1f).endVertex();
//			bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, -100.0F).uv(0.75f, 0).color(1f, 1f, 1f, 1f).endVertex();
//		}
//		else if(i==5) {
//			bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, -100.0F).uv(0.75f, 0).color(1f, 1f, 1f, 1f).endVertex();
//			bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, 100.0F).uv(0.75f, 0.25f).color(1f, 1f, 1f, 1f).endVertex();
//			bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, 100.0F).uv(1f, 0.25f).color(1f, 1f, 1f, 1f).endVertex();
//			bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, -100.0F).uv(1f, 0).color(1f, 1f, 1f, 1f).endVertex();
//		}
//		else if(i==3) { //top
//			bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, -100.0F).uv(0, 0.25f).color(1f, 1f, 1f, 1f).endVertex();
//			bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, 100.0F).uv(0, 0.5f).color(1f, 1f, 1f, 1f).endVertex();
//			bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, 100.0F).uv(0.25f, 0.5f).color(1f, 1f, 1f, 1f).endVertex();
//			bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, -100.0F).uv(0.25f, 0.25f).color(1f, 1f, 1f, 1f).endVertex();
//		}
//		else if(i==0) { //bottom
//			bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, -100.0F).uv(0.25f, 0.25f).color(1f, 1f, 1f, 1f).endVertex();
//			bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, 100.0F).uv(0.25f, 0.5f).color(1f, 1f, 1f, 1f).endVertex();
//			bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, 100.0F).uv(0.5f, 0.5f).color(1f, 1f, 1f, 1f).endVertex();
//			bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, -100.0F).uv(0.5f, 0.25f).color(1f, 1f, 1f, 1f).endVertex();
//		}

	}
}

