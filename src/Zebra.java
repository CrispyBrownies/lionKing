
//Created: 3/11/2021
//Main class for Zebra (prey)

import java.util.*;

class Zebra extends Animal {

    private static int breedEnergy;
    private int state; //State of the zebra, 0 = wander, 1 = food, 2 = mate
    private int passion; //How willing the zebra is to mate than to run. (innate)
    private int excitement;
    private Plant targetPlant;
    private Zebra targetMate;
    private Lion targetLion;
    private final int WANDERDIRTIMER = 1000;

    public Zebra(float x, float y, float speed, int energy, float detectRange, int breedEnergy, int attentionSpan) {
        setEnergy(energy);
        setSpeed(speed);
        setDetectRange(detectRange);
        this.breedEnergy = breedEnergy;
        setX(x);
        setY(y);
        setWanderDirTimer(WANDERDIRTIMER);
        setAttentionSpan(attentionSpan);
    }

    private void DetectPlant(ArrayList<Plant> plantList) {
        HashMap<Float, Plant> plantMap = new HashMap<Float, Plant>();

        //Looking for food in range
        for (Plant plant:plantList) {
            float distBetween = Equations.EuclDist(plant.getX(),plant.getY(),getX(),getY());
            if (distBetween < getDetectRange()) {
                plantMap.put(distBetween, plant);
            }
        }
        if (plantMap.size()>0) {
            Set<Float> distances = plantMap.keySet();
            float minDist = Collections.min(distances);
            this.targetPlant = plantMap.get(minDist);
        }
    }

    //Searches for mate in detection range and sets mate target to closest available mate
    private void DetectMate(ArrayList<Zebra> zebraList) {
        HashMap<Float, Zebra> mateMap = new HashMap<Float, Zebra>();
        for (Zebra zebra:zebraList) {
            float distBetween = Equations.EuclDist(zebra.getX(),zebra.getY(),getX(),getY());

            //Checks if the target zebra is in range and also available to mate
            if ((distBetween < getDetectRange()) && (zebra.state == 2)) {
                mateMap.put(distBetween, zebra);
            }
        }
        if (mateMap.size()>0) {
            Set<Float> distances = mateMap.keySet();
            float minDist = Collections.min(distances);
            this.targetMate = mateMap.get(minDist);
        }
        else {
            this.state = 0;
        }
    }

    private void DetectEnemy(ArrayList<Lion> lionList) {
        HashMap<Float, Lion> lionMap = new HashMap<Float, Lion>();

        //Looking for lions in range
        for (Lion lion:lionList) {
            float distBetween = Equations.EuclDist(lion.getX(),lion.getY(),getX(),getY());
            if (distBetween < getDetectRange()) {
                lionMap.put(distBetween, lion);
            }
        }
        if (lionMap.size()>0) {
            Set<Float> distances = lionMap.keySet();
            float minDist = Collections.min(distances);
            this.targetLion = lionMap.get(minDist);
            this.state = 3;
        }
    }

    //Controls what the zebra is deciding to do
    private void Behavior(ArrayList<Plant> plantList, ArrayList<Zebra> zebraList, ArrayList<Lion> lionList) {
        DetectEnemy(lionList);

        //Search for mate if enough energy and not running from lion
        if (this.getEnergy() > this.breedEnergy) {
            this.state = 2;
        }
        switch (this.state) {
            case 0: //wandering phase
                Wander();
            case 1: //searching for food
                DetectPlant(plantList);
            case 2: //searching for mate
                DetectMate(zebraList);
                Mate();
            case 3: //running from predator

        }
    }

    //Call every time step during food targeting phase
    private void GoEat() {
        Vector<Float> foodDir = new Vector<Float>();
        foodDir.set(0,this.targetPlant.getX()-this.getX());
        foodDir.set(1,this.targetPlant.getY()-this.getY());

        this.setDirection(foodDir);
        Move();
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

    //Call every time step during wander phase
    private void Wander() {
        if (this.getWanderDirTimer() == 0) {
            this.setWanderDirTimer(WANDERDIRTIMER);
            this.PickNewDir();
        }
        else {
            this.setWanderDirTimer(this.getWanderDirTimer()-1);
        }
        Move();
    }



}