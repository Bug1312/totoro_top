package com.bug1312.totoro_top.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.bug1312.totoro_top.TopEntity;

import net.minecraft.client.network.ClientPlayerEntity;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {
	@Inject(at = @At("TAIL"), method = "tickRiding")
	public void totoroTop$tickRiding(CallbackInfo ci) {
		ClientPlayerEntity _this = (ClientPlayerEntity) ((Object) this);
		if (_this.getControllingVehicle() instanceof TopEntity topEntity) {
			topEntity.setInputs(
				_this.input.playerInput.left(), 
				_this.input.playerInput.right(), 
				_this.input.playerInput.forward(), 
				_this.input.playerInput.backward(),
				_this.input.playerInput.jump()				
			);
		}
	}
}
