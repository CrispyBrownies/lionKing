import java.*;

class Equations {

    private float EuclDist(float a, float b) {
        float dist = Math.sqrt(a*a+b*b);
        return dist;
    }

    private float EnergyCost(Vector<float> velocity) {
        float speed = EuclDist(velocity.get(1),velocity.get(2));
        float energyCost = speed; //Equation to calculate energy cost based on speed
        return energyCost;
    }

}
