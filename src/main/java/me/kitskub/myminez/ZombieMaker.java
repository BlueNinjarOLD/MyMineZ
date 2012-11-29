package me.kitskub.myminez;

import java.lang.reflect.Field;
import java.util.Random;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityVillager;
import net.minecraft.server.EntityZombie;
import net.minecraft.server.PathfinderGoalBreakDoor;
import net.minecraft.server.PathfinderGoalFloat;
import net.minecraft.server.PathfinderGoalHurtByTarget;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalMeleeAttack;
import net.minecraft.server.PathfinderGoalMoveThroughVillage;
import net.minecraft.server.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.PathfinderGoalRandomLookaround;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.PathfinderGoalSelector;

import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftZombie;

import org.bukkit.Location;
import org.bukkit.entity.Zombie;

public class ZombieMaker {
	public static void mineZombie(Zombie z){
		Location loc = z.getLocation();
		EntityZombie zombie = ((CraftZombie) z).getHandle();
		Random gen = new Random();
		try {
			int chance = gen.nextInt(22);
			if (gen.nextBoolean()) zombie.setBaby(true);
			if (!zombie.isBaby()) {
				float speedGoal = 0.42F;
				//42 and they are REALLY FAST
				Field fGoalSelector = EntityLiving.class.getDeclaredField("goalSelector");
				fGoalSelector.setAccessible(true);
				PathfinderGoalSelector gs = new PathfinderGoalSelector(((CraftWorld) loc.getWorld()).getHandle() != null && ((CraftWorld) loc.getWorld()).getHandle().methodProfiler != null ? ((CraftWorld) loc.getWorld()).getHandle().methodProfiler : null);
				gs.a(0, new PathfinderGoalFloat(zombie));
				gs.a(1, new PathfinderGoalBreakDoor(zombie));
				gs.a(2, new PathfinderGoalMeleeAttack(zombie, EntityHuman.class, speedGoal, false));
				gs.a(3, new PathfinderGoalMeleeAttack(zombie, EntityVillager.class, speedGoal, true));
				gs.a(4, new PathfinderGoalMoveTowardsRestriction(zombie, speedGoal));
				gs.a(5, new PathfinderGoalMoveThroughVillage(zombie, speedGoal, false));
				gs.a(6, new PathfinderGoalRandomStroll(zombie, speedGoal));
				gs.a(7, new PathfinderGoalLookAtPlayer(zombie, EntityHuman.class, 15.0F));
				gs.a(7, new PathfinderGoalRandomLookaround(zombie));
				fGoalSelector.set(zombie,gs);

				float range = 32f;
				Field fTargetSelector = EntityLiving.class.getDeclaredField("targetSelector");
				fTargetSelector.setAccessible(true);
				PathfinderGoalSelector ts = new PathfinderGoalSelector(((CraftWorld) loc.getWorld()).getHandle() != null && ((CraftWorld) loc.getWorld()).getHandle().methodProfiler != null ? ((CraftWorld) loc.getWorld()).getHandle().methodProfiler : null);
				ts.a(1, new PathfinderGoalHurtByTarget(zombie, false));
				ts.a(2, new PathfinderGoalNearestAttackableTarget(zombie, EntityHuman.class, range, 0, true));
				ts.a(2, new PathfinderGoalNearestAttackableTarget(zombie, EntityVillager.class, range, 0, false));
				fTargetSelector.set(zombie, ts);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
