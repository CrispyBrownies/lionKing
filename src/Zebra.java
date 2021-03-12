
//Created: 3/11/2021
//Main class for Zebra (prey)

import java.util.*;

class Zebra extends Animal {

    private static int breedEnergy;
    private int state; //State of the zebra, 0 = wander, 1 = food, 2 = mate
    private Plant targetPlant;
    private Zebra targetMate;

    public Zebra(float x, float y, float speed, int energy, float detectRange, int breedEnergy) {
        setEnergy(energy);
        setSpeed(speed);
        setDetectRange(detectRange);
        this.breedEnergy = breedEnergy;
        setX(x);
        setY(y);
    }

    private void DetectPlant(ArrayList<Plant> plantList) {
        HashMap<Float, Plant> plantMap = new HashMap<Float, Plant>();
        //Looking for food in range
        for (Plant plant:plantList) { //for each plant in plantList
            float distBetween = Equations.EuclDist(plant.getX(),plant.getY(),getX(),getY());
            if (distBetween < getDetectRange()) {
                plantMap.put(distBetween, plant);
            }
        }
        Set<Float> distances = plantMap.keySet();
        float minDist = Collections.min(distances);
        this.targetPlant = plantMap.get(minDist);
    }

    //Searches for mate in detection range and sets mate target to closest available mate
    private void DetectMate(ArrayList<Zebra> zebraList) {
        HashMap<Float, Zebra> mateMap = new HashMap<Float, Zebra>();
        for (Zebra zebra:zebraList) { //for each zebra in zebraList
            float distBetween = Equations.EuclDist(zebra.getX(),zebra.getY(),getX(),getY());
            //Checks if the target zebra is in range and also available to mate
            if ((distBetween < getDetectRange()) && (zebra.state == 2)) {
                mateMap.put(distBetween, zebra);
            }
        }
        Set<Float> distances = mateMap.keySet();
        float minDist = Collections.min(distances);
        this.targetMate = mateMap.get(minDist);
    }

    //Controls what the zebra is deciding to do
    private void Behavior() {
        switch (this.state) {
            case 0: //wandering phase

            case 1: //searching for food

            case 2: //searching for mate

        }
    }

    //Handles the eating of plants
    private void Eat(ArrayList<Plant> plantList) {
        float distBetween = Equations.EuclDist(this.targetPlant.getX(),this.targetPlant.getY(),getX(),getY());
        if (distBetween < 0.05) {
            plantList.remove(targetPlant); //removes this plant
            this.targetPlant = null; //clear targetPlant variable
        }
    }

    public void Mate() {

    }

}