
import java.util.*;

class Equations {

    public static float EuclDist(float ax, float ay, float bx, float by) {
        float dist = (float)Math.sqrt((ax-bx)*(ax-bx)+(ay-by)*(ay-by));
        return dist;
    }

    public static float EnergyCost(Vector<Float> velocity) {
        float speed = (float)Math.sqrt(velocity.get(1)*velocity.get(1)+velocity.get(2)*velocity.get(2));
        float energyCost = speed; //Equation to calculate energy cost based on speed
        return energyCost;
    }

}
