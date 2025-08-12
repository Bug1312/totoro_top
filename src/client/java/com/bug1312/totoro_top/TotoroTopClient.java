package com.bug1312.totoro_top;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class TotoroTopClient implements ClientModInitializer {
	public static final EntityModelLayer MODEL_TOP_LAYER = new EntityModelLayer(Identifier.of(TotoroTop.MOD_ID, "top"), "main");
	
	@Override 
	public void onInitializeClient() {
		EntityRendererRegistry.register(TotoroTop.TOP_ENTITY, TopEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(MODEL_TOP_LAYER, TopEntityModel::getTexturedModelData);
	}
}