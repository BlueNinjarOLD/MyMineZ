package me.kitskub.myminez.entity;

import net.minecraft.server.EntityZombie;
import net.minecraft.server.World;

public class Zombie extends EntityZombie {

	public Zombie(World world) {
		super(world);
		System.out.println("Zombie created");
		this.bI = 1f;
		//this.targetSelector
	}

	@Override
	public void c() {
		//super.c();
	}
	
}
