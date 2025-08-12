package com.bug1312.totoro_top.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.bug1312.totoro_top.BipedEntityRenderStateDuck;
import com.bug1312.totoro_top.TopEntity;

import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.entity.LivingEntity;

@Mixin(BipedEntityRenderer.class)
public class BipedEntityRendererMixin {
	@Inject(at = @At("TAIL"), method = "updateBipedRenderState")
	private static void totoroTop$updateBipedRenderState(LivingEntity entity, BipedEntityRenderState state, float tickProgress, ItemModelManager itemModelResolver, CallbackInfo ci) {
		BipedEntityRenderStateDuck duckState = (BipedEntityRenderStateDuck) state;
		duckState.setRidingTop(entity.hasVehicle() && entity.getVehicle() instanceof TopEntity);
	}
}
