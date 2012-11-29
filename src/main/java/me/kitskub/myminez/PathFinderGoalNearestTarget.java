package me.kitskub.myminez;

import net.minecraft.server.EntityLiving;
import net.minecraft.server.PathfinderGoalNearestAttackableTarget;

public class PathFinderGoalNearestTarget extends PathfinderGoalNearestAttackableTarget {

	public PathFinderGoalNearestTarget(EntityLiving entityliving, Class oclass, float f, int i, boolean flag) {
		super(entityliving, oclass, f, i, flag);
	}

}
