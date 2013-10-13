package gp;

public class EventsCounter {
	private int bulletHit = 0;
	private boolean bulletHitThisRound = false;
	
	private int bulletMissed = 0;
	private boolean bulletMissedThisRound = false;
	
	private int hitByBullet = 0;
	private boolean hitByBulletThisRound = false;
	
	private int hitRobot = 0;
	private boolean hitRobotThisRound = false;
	
	private boolean scannedRobotThisRound = false;
	
	public int getBulletHit() {
		return bulletHit;
	}

	public void incBulletHit() {
		this.bulletHit += 1;
		this.bulletHitThisRound = true;
	}

	public int getBulletMissed() {
		return bulletMissed;
	}

	public void incBulletMissed() {
		this.bulletMissed += 1;
		this.bulletMissedThisRound = true;
	}

	public int getHitByBullet() {
		return hitByBullet;
	}

	public void incHitByBullet() {
		this.hitByBullet += 1;
		this.hitByBulletThisRound = true;
	}

	public int getHitRobot() {
		return hitRobot;
	}

	public void incHitRobot() {
		this.hitRobot += 1;
		this.hitRobotThisRound = true;
	}
	
	public void newRound() {
		this.bulletHitThisRound = false;
		this.bulletMissedThisRound = false;
		this.hitByBulletThisRound = false;
		this.hitRobotThisRound = false;
		this.scannedRobotThisRound = false;
	}
	
	public boolean isBulletHitThisRound() {
		return bulletHitThisRound;
	}

	public boolean isBulletMissedThisRound() {
		return bulletMissedThisRound;
	}

	public boolean isHitByBulletThisRound() {
		return hitByBulletThisRound;
	}

	public boolean isHitRobotThisRound() {
		return hitRobotThisRound;
	}

	public boolean isScannedRobotThisRound() {
		return scannedRobotThisRound;
	}

	public void haveScannedRobotThisRound() {
		this.scannedRobotThisRound = true;
	}
}
