package com.bug1312.totoro_top;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Leashable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.PositionInterpolator;
import net.minecraft.entity.mob.CreakingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Colors;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TopEntity extends VehicleEntity implements Leashable {
	@Nullable private Leashable.LeashData leashData;
	public AnimationState idleAnimationState = new AnimationState();
	
	private final PositionInterpolator interpolator = new PositionInterpolator(this, 3);
	private boolean pressingLeft;
	private long pressingLeftStart;
	private boolean pressingRight;
	private long pressingRightStart;
	private boolean pressingForward;
	private long pressingForwardStart;
	private boolean pressingBack;
	private long pressingBackStart;
	private boolean pressingJump;
	private long pressingJumpStart;
	private static final double SPEED = 0.20;
	private static final int EASE_TICKS = 80;
	
	public TopEntity(EntityType<?> entityType, World world) {
		super(entityType, world);
	}
	
	@Override public boolean isFlyingVehicle() { return true; }
	@Override protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) { }
	@Override public boolean hasNoGravity() { return true; }
	@Override public boolean isOnGround() { return false; }
	
	@Override public boolean isCollidable(Entity entity) { return true; }
	@Override public boolean isPushable() { return true; }
	
	@Override public boolean canHit() { return true; }
	
	@Override public PositionInterpolator getInterpolator() { return this.interpolator; }
	@Override protected float getVelocityMultiplier() { return 0.9F; }
	
	@Nullable @Override public Leashable.LeashData getLeashData() { return this.leashData; }
	@Override public void setLeashData(@Nullable Leashable.LeashData leashData) { this.leashData = leashData; }

	public void initPosition(double x, double y, double z) {
		this.setPosition(x, y, z);
		this.lastX = x;
		this.lastY = y;
		this.lastZ = z;
	}
	
	@Override public LivingEntity getControllingPassenger() { 
		Entity passenger = this.getFirstPassenger();
		if (passenger instanceof LivingEntity) return (LivingEntity) passenger;
		return null;
	}
	
	@Override
	public void pushAwayFrom(Entity entity) {
		if (entity.getBoundingBox().minY <= this.getBoundingBox().minY) {
			super.pushAwayFrom(entity);
		}
	}
	
	public void setInputs(boolean pressingLeft, boolean pressingRight, boolean pressingForward, boolean pressingBack, boolean pressingJump) {
		if (pressingLeft && !this.pressingLeft) this.pressingLeftStart = this.getWorld().getTime();
		this.pressingLeft = pressingLeft;
		
		if (pressingRight && !this.pressingRight) this.pressingRightStart = this.getWorld().getTime();
		this.pressingRight = pressingRight;
		
		if (pressingForward && !this.pressingForward) this.pressingForwardStart = this.getWorld().getTime();
		this.pressingForward = pressingForward;
		
		if (pressingBack && !this.pressingBack) this.pressingBackStart = this.getWorld().getTime();
		this.pressingBack = pressingBack;
		
		if (pressingJump && !this.pressingJump) this.pressingJumpStart = this.getWorld().getTime();
		this.pressingJump = pressingJump;
	}
	
	private double easeOut(float start) {
		double progress = (this.getWorld().getTime() - start) / (EASE_TICKS);
		progress = Math.clamp(progress, 0, 1);

		// Cubic ease out: f(t) = 1 - (1 - t)^3
		double easedProgress = 1 - Math.pow(1 - progress, 3);
		
		return SPEED * easedProgress;
	}
	
	@Override
	public void tick() {
		super.tick();

		LivingEntity rider = this.getControllingPassenger();

		if (FabricLoader.getInstance().isModLoaded("area_lib")) {
			if (!this.getWorld().isClient() && this.getWorld() instanceof ServerWorld serverWorld) { 
				dev.doublekekse.area_lib.data.AreaSavedData areaData = dev.doublekekse.area_lib.AreaLib.getSavedData(this.getWorld());
				dev.doublekekse.area_lib.Area area = areaData.get(Identifier.of(TotoroTop.MOD_ID, "only_fly_zone"));
				if (area != null && !area.contains(this)) {
					this.kill(serverWorld);
					if (rider != null && rider instanceof ServerPlayerEntity player) {
						player.sendMessage(Text.translatable("totoro_top.area_lib.killed").withColor(Colors.LIGHT_RED));
					}
				}
			}
		}

		this.idleAnimationState.startIfNotRunning(this.age);
		this.interpolator.tick();
		
		if (rider == null) {
			this.setBoundingBox(calculateDefaultBoundingBox(this.getPos()));
			this.addVelocity(0, -0.05, 0);
		} else {
			// Make sure y'all don't suffocate
			Box riderBoundingBox = rider.getBoundingBox();
			Box myBoundingBox = calculateDefaultBoundingBox(this.getPos());
			Vec3d riderSize = new Vec3d(
				Math.max(0, riderBoundingBox.getLengthX() - myBoundingBox.getLengthX()), 
				riderBoundingBox.getLengthY(), 
				Math.max(0, riderBoundingBox.getLengthZ() - myBoundingBox.getLengthZ())
			);
			this.setBoundingBox(myBoundingBox.stretch(riderSize).offset(-riderSize.getX()/2D, 0, -riderSize.getZ()/2D));
			
			// Fun factoid: Happy ghasts determine not only direction, but the SPEED, from the client
			if (this.getWorld().isClient()) {
				double forward = 0;
				if (this.pressingForward) forward += easeOut(this.pressingForwardStart);
				if (this.pressingBack) forward -= easeOut(this.pressingBackStart);
				
				double right = 0;
				if (this.pressingRight) right += easeOut(this.pressingRightStart);
				if (this.pressingLeft) right -= easeOut(this.pressingLeftStart);
				
				double yawRad = Math.toRadians(rider.getHeadYaw());
				double pitchRad = Math.toRadians(rider.getPitch());
				
				// Forward + / Backward -
				double xForward = -Math.sin(yawRad) * Math.cos(pitchRad);
				double yForward = -Math.sin(pitchRad);
				double zForward = Math.cos(yawRad) * Math.cos(pitchRad);
				
				// Right + / Left -
				double xRight = -Math.cos(yawRad);
				double zRight = -Math.sin(yawRad);
				
				double x = forward * xForward + right * xRight;
				double y = forward * yForward; // Pitch up/down
				double z = forward * zForward + right * zRight;
				
				if (this.pressingJump) y += easeOut(this.pressingJumpStart);

				x = Math.clamp(x, -SPEED, SPEED);
				y = Math.clamp(y, -SPEED, SPEED);
				z = Math.clamp(z, -SPEED, SPEED);
				
				double currentMaxSpeed = Math.max(Math.max(Math.abs(x), Math.abs(y)), Math.abs(z));

				this.addVelocity(new Vec3d(x, y, z).normalize().multiply(currentMaxSpeed));
			}
		}
		
		this.setVelocity(this.getVelocity().multiply(this.getVelocityMultiplier()));
		
		if (!this.getWorld().isClient() || this.isLogicalSideForUpdatingMovement()) {
			this.move(MovementType.SELF, this.getVelocity());

			this.tickBlockCollision();
			
			List<Entity> list = this.getWorld().getOtherEntities(this, this.getBoundingBox().expand(0.2F, -0.01F, 0.2F), EntityPredicates.canBePushedBy(this));
			if (!list.isEmpty()) {
				for (Entity entity : list) {
					if (!entity.hasPassenger(this)) {
						if (
							this.getPassengerList().isEmpty()
							&& !entity.hasVehicle()
							&& entity instanceof LivingEntity
							&& !(entity instanceof PlayerEntity)
							&& !(entity instanceof CreakingEntity)
						) entity.startRiding(this);
						else this.pushAwayFrom(entity);
					}
				}
			}
		} else this.setVelocity(Vec3d.ZERO);
	}	

	@Override
	public ActionResult interact(PlayerEntity player, Hand hand) {
		if (!player.shouldCancelInteraction() && !this.hasPassengers() && (this.getWorld().isClient || player.startRiding(this))) {
			if (!this.getWorld().isClient) return player.startRiding(this) ? ActionResult.CONSUME : ActionResult.PASS;
			return ActionResult.SUCCESS;
		}
		
		return super.interact(player, hand);
	}

	@Override
	protected Item asItem() {
		return TotoroTop.TOP_ITEM;
	}

	@Override
	protected void writeCustomData(WriteView view) {
		this.writeLeashData(view, this.leashData);
	}

	@Override
	protected void readCustomData(ReadView view) {
		this.readLeashData(view);
	}
	
	@Override
	public ItemStack getPickBlockStack() {
		return new ItemStack(this.asItem());
	}
	
}
