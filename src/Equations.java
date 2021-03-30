
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
        double magProd = EuclDist(0,0,dir1.get(0),dir1.get(1))*EuclDist(0,0,dir2.get(0),dir2.get(1));
        double dotProd = dir1.get(0)*dir2.get(0)+dir1.get(1)*dir2.get(1);
//        System.out.println("dir1: "+dir1);
//        System.out.println("dir2: "+dir2);
        float c = EuclDist(dir1.get(0),dir1.get(1),dir2.get(0),dir2.get(1));
//        System.out.println("c: "+c);
        //float angle = (float)(Math.acos(Math.pow(c,2)/-2));
        //float
        //System.out.println("angle 1: "+angle);
        float angle = (float)Math.acos(dotProd/magProd);
//        System.out.println("angle: "+angle);
        return angle;
    }
}
