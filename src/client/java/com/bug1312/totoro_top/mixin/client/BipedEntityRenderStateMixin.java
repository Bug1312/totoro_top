package com.bug1312.totoro_top.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.bug1312.totoro_top.BipedEntityRenderStateDuck;

import net.minecraft.client.render.entity.state.BipedEntityRenderState;

@Mixin(BipedEntityRenderState.class)
public abstract class BipedEntityRenderStateMixin implements BipedEntityRenderStateDuck {
	@Unique private boolean isRidingTop = false;
	@Unique @Override public boolean isRidingTop() { return this.isRidingTop; }
	@Unique @Override public void setRidingTop(boolean bool) { this.isRidingTop = bool; }
}
