package com.bug1312.totoro_top.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.bug1312.totoro_top.BipedEntityRenderStateDuck;

import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;

@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin<T extends BipedEntityRenderState> {
	@Inject(at = @At("TAIL"), method = "setAngles")
	void totoroTop$setAngles(T state, CallbackInfo ci) {
		BipedEntityModel<?> _this = (BipedEntityModel<?>) ((Object) this);
		BipedEntityRenderStateDuck duckState = (BipedEntityRenderStateDuck) state;
		
		if (duckState.isRidingTop()) {
			_this.rightArm.pitch -= (float) (-Math.PI / 5);
			_this.leftArm.pitch -= (float) (-Math.PI / 5);
			_this.rightLeg.pitch = 0;
			_this.rightLeg.yaw = 0;
			_this.rightLeg.roll = 0;
			_this.leftLeg.pitch = 0;
			_this.leftLeg.yaw = 0;
			_this.leftLeg.roll = 0;
		}
	}
}
