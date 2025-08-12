package com.bug1312.totoro_top;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.AnimationState;

@Environment(EnvType.CLIENT)
public class TopRenderState extends EntityRenderState {
	public final AnimationState idleAnimationState = new AnimationState();
}
