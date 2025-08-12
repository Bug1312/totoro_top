package com.bug1312.totoro_top.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.bug1312.totoro_top.TopEntity;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

@Mixin(Entity.class)
public class EntityMixin {
	// Gorsh, I sure hope nobody messes with this method and instead uses getVehicleAttachmentPos :)))
	@Inject(
		at = @At("HEAD"), 
		method = "Lnet/minecraft/entity/Entity;updatePassengerPosition(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity$PositionUpdater;)V", 
		cancellable = true
	)
	protected void totoroTop$updatePassengerPosition(Entity passenger, Entity.PositionUpdater positionUpdater, CallbackInfo ci) {
		if (passenger.hasVehicle() && passenger.getVehicle() instanceof TopEntity) {
			Entity _this = (Entity) ((Object) this);
			Vec3d vec3d = _this.getPassengerRidingPos(passenger);
			positionUpdater.accept(passenger, vec3d.x, vec3d.y, vec3d.z);
			
			ci.cancel();
		}
	}
}
