
//Created: 3/11/2021
//Main class for Zebra (prey)

import java.util.ArrayList;

class Zebra extends Animal {

    private ArrayList<Plant> inRange;
    private ArrayList<Zebra> mateList;
    private static int breedEnergy;
    private boolean state; //State of the zebra, 0 = food, 1 = mate

    public Zebra(float x, float y, float speed, int energy, float detectRange, int breedEnergy) {
        setEnergy(energy);
        setSpeed(speed);
        setDetectRange(detectRange);
        this.breedEnergy = breedEnergy;
        setX(x);
        setY(y);
    }

    private void detectPrey(ArrayList<Plant> plantList) {
        //Looking for food in range
        for (Plant plant:plantList) { //for each plant in plantlist
            if (Equations.EuclDist(plant.getX(),plant.getY(),getX(),getY()) < getDetectRange()) {
                inRange.add(plant);
            }
        }
    }

    private void detectMate(ArrayList<Zebra> zebraList) {
        for (Zebra zebra:zebraList) { //for each zebra in zebralist
            if ((Equations.EuclDist(zebra.getX(),zebra.getY(),getX(),getY()) < getDetectRange()) && (zebra.state)) {
                mateList.add(zebra);
            }
        }
    }


}