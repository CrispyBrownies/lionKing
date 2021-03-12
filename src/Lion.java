
//Created: 3/11/2021
//Main class for Lion (predator)

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

class Lion extends Animal {

    private Zebra targetZebra;

    public Lion() { }

    public Lion(float x, float y) {
        setName("Lion");
        setEnergy(10000);
        setSpeed(10);
        setDetectRange(7);
    }

    //Checks for nearby zebras and selects closest one as target
    private void DetectZebra(ArrayList<Zebra> zebraList) {
        HashMap<Float, Zebra> zebraMap = new HashMap<Float, Zebra>();
        //Looking for food in range
        for (Zebra zebra:zebraList) { //for each zebra in zebraList
            float distBetween = Equations.EuclDist(zebra.getX(),zebra.getY(),getX(),getY());
            if (distBetween < getDetectRange()) {
                zebraMap.put(distBetween, zebra);
            }
        }
        Set<Float> distances = zebraMap.keySet();
        float minDist = Collections.min(distances);
        this.targetZebra = zebraMap.get(minDist);
    }

    public void setX(float x) {
        super.setX(x);
    }

    public void setEnergy(int energy) {
        super.setEnergy(energy);
    }

    public void setName(String name) {
        super.setName(name);
    }

    public void setDetectRange(float detectRange) {
        super.setDetectRange(detectRange);
    }

    public void setSpeed(float speed) {
        super.setSpeed(speed);
    }

    public void setY(float y) {
        super.setY(y);
    }

    public float getSpeed() {
        return super.getSpeed();
    }

    public String getName() {
        return super.getName();
    }

}