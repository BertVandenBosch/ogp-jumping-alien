package jumpingalien.model;

import java.util.Random;

import jumpingalien.model.World.TerrainType;
import jumpingalien.util.Sprite;

public class Slime extends GameObject {

	public Slime(World world, double x, double y, Sprite[] sprites, School school) {
		super(world, x, y, 1, 100, 100, sprites, 100, 0, 250, 250, 70, 1000);
		assert school != null;
		this.setSchool(school);
		generator = new Random();
		startNewMovement();
	}
	
	
	public School getSchool() {
		return school;
	}
	
	private void setSchool(School school) {
		this.school = school;
	}

	public void transferToSchool(School school) {
		for(Slime colleague: getSchool().getSlimes()) {
			if (colleague != this) {
				colleague.addHitPoints(1);
				this.substractHitPoints(1);
			}
		}
		this.getSchool().removeAsSlime(this);
		this.setSchool(school);
		school.addAsSlime(this);
		for(Slime colleague: getSchool().getSlimes()) {
			if (colleague != this) {
				this.addHitPoints(1);
				colleague.substractHitPoints(1);
			}
		}
	}
	
	@Override
	protected void substractHitPoints(int hitPoints) {
		super.substractHitPoints(hitPoints);
		for(Slime colleague: getSchool().getSlimes())
			if (colleague != this)
				colleague.substractHitPoints(1);
	}
	
	private School school;
	
	private Random generator;
	
	
	public double getGoal() {
		return goal;
	}


	private void setGoal(double goal) {
		this.goal = goal;
	}
	
	private double goal;


	public void advanceTime(double duration) {
		super.advanceTime(duration);
		setTimer(getTimer()+duration);
		//Goals of 5.8 - 6 sec are less likely, but implementation is easier
		if(getTimer() > getGoal())
			startNewMovement();
	}
	
	private void startNewMovement() {
		endMove();
		setTimer(0);
		if(generator.nextDouble() < 0.5) //nextDouble() returns random double out of [0,1]
			startMove(Direction.LEFT);
		else
			startMove(Direction.RIGHT);
		setGoal(2+(5.8-2)*generator.nextDouble());
	}


	@Override
	public Sprite getCurrentSprite() {
		//Staat niet in opgave
		if (getXDirection() == Direction.LEFT)
			return getSprites()[0];
		else
			return getSprites()[1];
	}

	@Override
	public void handleInteraction(double duration) {
		
		
		Slime object0 = (Slime) this.touches(Slime.class);
		if (object0 != null && getTimeToBeImmune() == 0) {
			if(this.getSchool() != object0.getSchool()) {
				if(this.getSchool().getNbSlimes() > object0.getSchool().getNbSlimes())
					object0.transferToSchool(this.getSchool());
				else if(this.getSchool().getNbSlimes() < object0.getSchool().getNbSlimes())
					this.transferToSchool(object0.getSchool());
			}
				
		}
		
		Mazub object1 = (Mazub) this.touches(Mazub.class);
		if (object1 != null && getTimeToBeImmune() == 0) {
			this.substractHitPoints(50);
			this.setTimeToBeImmune(0.6);
		}
		
		
		
		Shark object2 = (Shark) this.touches(Shark.class);
		if (object2 != null && getTimeToBeImmune() == 0) {
			this.substractHitPoints(50);
			this.setTimeToBeImmune(0.6);
		}
		
		//Exacte kopie van klasse Mazub, niet echt rendabel om een extra niveau in de klassenhierarchie
		//op te nemen.
		if (this.touches(TerrainType.WATER)) {
			this.setWaterTimer(getWaterTimer() + duration);
			if (getWaterTimer() >= 0.2) {
				this.substractHitPoints(2);
				setWaterTimer(getWaterTimer() - 0.2);
			}
		}
		
		else
			setWaterTimer(0);
		
		if (this.touches(TerrainType.MAGMA)) {
			this.setMagmaTimer(getMagmaTimer() + duration);
			if (getMagmaTimer() >= 0.2) {
				this.substractHitPoints(50);
				setMagmaTimer(getMagmaTimer() - 0.2);
			}
		}

		else
			setMagmaTimer(0.2);
		
	}

}