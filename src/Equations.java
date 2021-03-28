
import java.util.*;

class Equations {

    public static float EuclDist(float ax, float ay, float bx, float by) {
        float dist = (float)Math.sqrt((ax-bx)*(ax-bx)+(ay-by)*(ay-by));
        return dist;
    }

    public static float EnergyCost(float speed) {
        float energyCost = 0.001f*speed; //Equation to calculate energy cost based on speed
        return energyCost;
    }

    public static float AngleBTVector(Vector<Float> dir1, Vector<Float> dir2) {
        //float magProd = EuclDist(0,0,dir1.get(0),dir1.get(0))*EuclDist(0,0,dir2.get(0),dir2.get(1));
        //float dotProd = dir1.get(0)*dir2.get(0)+dir1.get(1)*dir2.get(1);

        float angle = (float)Math.acos(1-(Math.pow(EuclDist(dir1.get(0),dir1.get(1),dir2.get(0),dir2.get(1)),2)/2));
        //float angle = (float)Math.acos(dotProd/magProd);
        return angle;
    }
}
