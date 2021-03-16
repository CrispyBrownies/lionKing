
//Created: 3/11/2021
//Main class for Lion (predator)

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

class Lion extends Animal {

    private final String name = "Lion";
    private Zebra targetZebra;
    private final int WANDERDIRTIMER = 1000;
    private final int MAXATTENTIONSPAN = 5000;

    public Lion() { }

    public Lion(float x, float y) {
        setName("Lion");
        setEnergy(10000);
        setSpeed(0.2f);
        setDetectRange(100);
        setWanderDirTimer(WANDERDIRTIMER);
        this.PickNewDir();
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
        if (zebraMap.size()>0) {
            Set<Float> distances = zebraMap.keySet();
            float minDist = Collections.min(distances);
            this.targetZebra = zebraMap.get(minDist);
        }
    }

    //Call this method every time step
    public void Update(ArrayList<Zebra> zebraList, int mapSize) {
        DetectZebra(zebraList);
        if (this.targetZebra != null) {
            if (this.getAttentionSpan() == 0) {
                this.targetZebra = null;
                this.setAttentionSpan(MAXATTENTIONSPAN);
            }
            else {
                this.setAttentionSpan(this.getAttentionSpan()-1);
                Move(targetZebra.getX(), targetZebra.getY(), 1, mapSize);
                EatZebra(zebraList);
            }
        }
        else {
            Wander(mapSize);
        }
    }

    //Call every time step during wander phase
    private void Wander(int mapSize) {
        //System.out.println("Wander Timer: "+this.getWanderDirTimer());
        if (this.getWanderDirTimer() == 0) {
            this.setWanderDirTimer(this.WANDERDIRTIMER);
            this.PickNewDir();
        }
        else {
            this.setWanderDirTimer(this.getWanderDirTimer()-1);
        }
        Advance(mapSize);
    }

    //Checks whether targeted zebra is in eating range.
    private void EatZebra(ArrayList<Zebra> zebraList) {
        if (Equations.EuclDist(targetZebra.getX(),targetZebra.getY(),getX(),getY()) < 0.5) {
            zebraList.remove(this.targetZebra);
            this.setEnergy(this.getEnergy()+100);
            this.targetZebra = null;
        }
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

    public void setSpeed(int speed) {
        super.setSpeed(speed);
    }

    public float getSpeed() {
        return super.getSpeed();
    }

    public String getName() {
        return super.getName();
    }

    //    public Lion(int x, int y, int speed, int environmentSize) {
//        setName("Lion");
//        setX(x);
//        setY(y);
//        setSpeed(speed);
//        setEnvironmentSize(environmentSize);
//    }
}