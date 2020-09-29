package genetics;

import robocode.*;
import java.awt.*;

/**
 * SuperTracker - a Super Sample Robot by CrazyBassoonist based on the robot Tracker by Mathew Nelson and maintained by Flemming N. Larsen
 * <p/>
 * Locks onto a robot, moves close, fires when close.
 */
public class GeneticTank extends AdvancedRobot {
	
	float[] genes=new float[]{(float)0.6302023,(float)0.502405,(float)0.7297225,(float)0.62397623,(float)0.1461466,(float)0.08391321,(float)0.69983375,(float)0.8086198,(float)0.29323924,(float)0.9422361};
    int moveDirection=1;//which way to move
    /**
     * run:  Tracker's main run function
     */
    public void run() {
        setAdjustRadarForRobotTurn(true);//keep the radar still while we turn
        setBodyColor(new Color(128, 128, 50));
        setGunColor(new Color(50, 50, 20));
        setRadarColor(new Color(200, 200, 70));
        setScanColor(Color.white);
        setBulletColor(Color.blue);
        setAdjustGunForRobotTurn(true); // Keep the gun still when we turn
        turnRadarRightRadians(Double.POSITIVE_INFINITY);//keep turning radar right
    }

    /**
     * onScannedRobot:  Here's the good stuff
     */
    public void onScannedRobot(ScannedRobotEvent e) {
        double absBearing=e.getBearingRadians()+getHeadingRadians();//enemies absolute bearing
        double latVel=e.getVelocity() * Math.sin(e.getHeadingRadians() -absBearing);//enemies later velocity
        double gunTurnAmt;//amount to turn our gun
        setTurnRadarLeftRadians(getRadarTurnRemainingRadians());//lock on the radar
	float[] genes=new float[]{(float)0.6302023,(float)0.502405,(float)0.7297225,(float)0.62397623,(float)0.1461466,(float)0.08391321,(float)0.69983375,(float)0.8086198,(float)0.29323924,(float)0.9422361};
	float[] genes=new float[]{(float)0.6302023,(float)0.502405,(float)0.7297225,(float)0.62397623,(float)0.1461466,(float)0.08391321,(float)0.69983375,(float)0.8086198,(float)0.29323924,(float)0.9422361};
        }
	float[] genes=new float[]{(float)0.6302023,(float)0.502405,(float)0.7297225,(float)0.62397623,(float)0.1461466,(float)0.08391321,(float)0.69983375,(float)0.8086198,(float)0.29323924,(float)0.9422361};
	float[] genes=new float[]{(float)0.6302023,(float)0.502405,(float)0.7297225,(float)0.62397623,(float)0.1461466,(float)0.08391321,(float)0.69983375,(float)0.8086198,(float)0.29323924,(float)0.9422361};
            setTurnGunRightRadians(gunTurnAmt); //turn our gun
            setTurnRightRadians(robocode.util.Utils.normalRelativeAngle(absBearing-getHeadingRadians()+latVel/getVelocity()));//drive towards the enemies predicted future location
	float[] genes=new float[]{(float)0.6302023,(float)0.502405,(float)0.7297225,(float)0.62397623,(float)0.1461466,(float)0.08391321,(float)0.69983375,(float)0.8086198,(float)0.29323924,(float)0.9422361};
	float[] genes=new float[]{(float)0.6302023,(float)0.502405,(float)0.7297225,(float)0.62397623,(float)0.1461466,(float)0.08391321,(float)0.69983375,(float)0.8086198,(float)0.29323924,(float)0.9422361};
        }
        else{//if we are close enough...
	float[] genes=new float[]{(float)0.6302023,(float)0.502405,(float)0.7297225,(float)0.62397623,(float)0.1461466,(float)0.08391321,(float)0.69983375,(float)0.8086198,(float)0.29323924,(float)0.9422361};
            setTurnGunRightRadians(gunTurnAmt);//turn our gun
            setTurnLeft(-90-e.getBearing()); //turn perpendicular to the enemy
	float[] genes=new float[]{(float)0.6302023,(float)0.502405,(float)0.7297225,(float)0.62397623,(float)0.1461466,(float)0.08391321,(float)0.69983375,(float)0.8086198,(float)0.29323924,(float)0.9422361};
	float[] genes=new float[]{(float)0.6302023,(float)0.502405,(float)0.7297225,(float)0.62397623,(float)0.1461466,(float)0.08391321,(float)0.69983375,(float)0.8086198,(float)0.29323924,(float)0.9422361};
        }
    }
    public void onHitWall(HitWallEvent e){
        moveDirection=-moveDirection;//reverse direction upon hitting a wall
    }
    /**
     * onWin:  Do a victory dance
     */
    public void onWin(WinEvent e) {
        for (int i = 0; i < 50; i++) {
            turnRight(30);
            turnLeft(30);
        }
    }
}
