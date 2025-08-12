package com.bug1312.totoro_top;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class TopEntityRenderer extends EntityRenderer<TopEntity, TopRenderState> {
	private final Identifier texture = Identifier.of(TotoroTop.MOD_ID, "textures/entity/top.png");
	private EntityModel<TopRenderState> model;
	
	protected TopEntityRenderer(Context context) {
		super(context);
		this.model = new TopEntityModel(context.getPart(TotoroTopClient.MODEL_TOP_LAYER));
	}

	@Override
	public TopRenderState createRenderState() {
		return new TopRenderState();
	}
	
	@Override
	public void updateRenderState(TopEntity entity, TopRenderState state, float tickProgress) {
		super.updateRenderState(entity, state, tickProgress);
		state.idleAnimationState.copyFrom(entity.idleAnimationState);
	}
	
	@Override
	public void render(TopRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {	
		this.model.setAngles(state);
		this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(this.texture)), light, OverlayTexture.DEFAULT_UV);
		super.render(state, matrices, vertexConsumers, light);
	}

}
