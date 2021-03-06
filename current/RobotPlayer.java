/**********************************************************************************************
 *  Current Robot Player (competition ready)                                                  *
 *  Version: 1/27/17                                                                          *
 **********************************************************************************************/

package current;
import battlecode.common.*;

public strictfp class RobotPlayer {
    static RobotController rc;

    /**
     * run() is the method that is called when a robot is instantiated in the Battlecode world.
     * If this method returns, the robot dies!
    **/
    @SuppressWarnings("unused")
    public static void run(RobotController rc) throws GameActionException {

        // This is the RobotController object. You use it to perform actions from this robot,
        // and to get information on its current status.
        RobotPlayer.rc = rc;

        // Here, we've separated the controls into a different method for each RobotType.
        // You can add the missing ones or rewrite this into your own control structure.
        switch (rc.getType()) {
            case ARCHON:
                runArchon();
                break;
            case GARDENER:
                runGardener();
                break;
            case SOLDIER:
                runSoldier();
                break;
            case LUMBERJACK:
                runLumberjack();
                break;
            default:
            	break;
        }
	}

    static void runArchon() throws GameActionException {
        // The code you want your robot to perform every round should be in this loop
        while (true) {
        	// Try/catch blocks stop unhandled exceptions, which cause your robot to explode
            try {

                // Generate a random direction
                Direction dir = randomDirection();

                // Randomly attempt to build a gardener in this direction
                if (rc.canHireGardener(dir)) {
                    rc.hireGardener(dir);
                }

                // Broadcast archon's location for other robots on the team to know
                MapLocation myLocation = rc.getLocation();
                rc.broadcast(0,(int)myLocation.x);
                rc.broadcast(1,(int)myLocation.y);

                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                Clock.yield();

            } catch (Exception e) {
                System.out.println("Archon Exception");
                e.printStackTrace();
            }
        }
    }

	static void runGardener() throws GameActionException {
        // The code you want your robot to perform every round should be in this loop
        while (true) {
        	try {

                // Listen for home archon's location
                int xPos = rc.readBroadcast(0);
                int yPos = rc.readBroadcast(1);
                MapLocation archonLoc = new MapLocation(xPos,yPos);

                // Generate a random direction
                Direction dir = randomDirection();

                if (rc.canBuildRobot(RobotType.SOLDIER, dir)) {
                    rc.buildRobot(RobotType.SOLDIER, dir);
                }
                dir = randomDirection();
                if (rc.canBuildRobot(RobotType.LUMBERJACK, dir)) {
                	rc.buildRobot(RobotType.LUMBERJACK, dir);
                }
                else {
                	// START FARMING HERE
                }
                
                Clock.yield(); // wait until next turn

            } catch (Exception e) {
                System.out.println("Gardener Exception");
                e.printStackTrace();
            }
        }
    }

    static void runSoldier() throws GameActionException {
    	MapLocation center = new MapLocation(250, 250);
    	Team enemy = rc.getTeam().opponent();
    	// The code you want your robot to perform every round should be in this loop
        while (true) {
        	Direction towardsCenter = rc.getLocation().directionTo(center);
        	if (rc.canMove(towardsCenter)) {
        		rc.move(towardsCenter);
        	}
        	RobotInfo[] enemies = rc.senseNearbyRobots(-1, enemy);

            // If there are some...
            if (enemies.length > 0) {
                // And we have enough bullets, and haven't attacked yet this turn...
                if (rc.canFireSingleShot()) {
                    // ...Then fire a bullet in the direction of the enemy.
                    rc.fireSingleShot(rc.getLocation().directionTo(enemies[0].location));
                }
            }
        	Clock.yield();
        }
    }

    static void runLumberjack() throws GameActionException {
        // The code you want your robot to perform every round should be in this loop

        while (true) {
        	Direction a = randomDirection();
        	if (rc.canMove(a))
        		rc.move(a);
        	Clock.yield();
        }
    }
    
    static void movetoLocation(MapLocation l) {
    	// write a method to move the robot to location l, avoiding obstacles
    	// if he encounters opponents, attack
    }
    
    static Direction randomDirection() {
        return new Direction((float)Math.random() * 2 * (float)Math.PI);
    }
    
    // Allows robots to fan out and make space for more robots
    static void fanOut() {
    	
    }
}