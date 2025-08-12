package com.bug1312.totoro_top.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.bug1312.totoro_top.TopEntity;
import com.bug1312.totoro_top.TopEntityLoopSound;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
	@Inject(at = @At("HEAD"), method = "playSpawnSound")
	public void totoroTop$playSpawnSound(Entity entity, CallbackInfo ci) {		
		if (entity instanceof TopEntity topEntity) MinecraftClient.getInstance().getSoundManager().play(new TopEntityLoopSound(topEntity));
	}
}
