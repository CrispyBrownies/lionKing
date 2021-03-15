
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

}
