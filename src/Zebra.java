
//Created: 3/11/2021
//Main class for Zebra (prey)
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;
import java.util.*;

class Zebra extends Animal {

    private final String name = "Zebra";

    private int breedEnergy;
    private int state; //State of the zebra, 0 = wander, 1 = food, 2 = mate, 3 = run
    private int passion; //How willing the zebra is to mate than to run. (innate)
    private int excitement;

    private Plant targetPlant;
    private Zebra targetMate;
    private Lion targetLion;

//    public Zebra() {
//
//    }

    public Zebra(int x, int y, int speed, int environmentSize) {
        setName("Zebra");
        setX(x);
        setY(y);
        setSpeed(speed);
        setEnvironmentSize(environmentSize);
    }

    public Zebra(int x, int y, float speed, int energy, float detectRange, int breedEnergy, int maxWanderDirTimer, int attentionSpan) {
        setName("Zebra");
        setEnergy(energy);
        setSpeed(speed);
        setDetectRange(detectRange);
        setBreedEnergy(breedEnergy);
        setX(x);
        setY(y);
        setWanderDirTimer(maxWanderDirTimer);
        setMaxWanderDirTimer(maxWanderDirTimer);
        setAttentionSpan(attentionSpan);
        this.PickNewDir();
    }

    //Searches for plants in detection range, sets target plant to nearest plant
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
            this.state = 1;
        }
        else {
            this.state = 0;
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

    //Searches for nearby enemies within detection range, sets nearest lion to target lion
    private void DetectEnemy(ArrayList<Lion> lionList) {
        HashMap<Float, Lion> lionMap = new HashMap<Float, Lion>();

        //Looking for lions in range
        for (Lion lion:lionList) {
            float distBetween = Equations.EuclDist(lion.getX(),lion.getY(),getX(),getY());
            if (distBetween < getDetectRange()) {
                lionMap.put(distBetween, lion);
            }
        }
        //Enters run phase if lions are found
        if (lionMap.size()>0) {
            Set<Float> distances = lionMap.keySet();
            float minDist = Collections.min(distances);
            this.targetLion = lionMap.get(minDist);
            this.state = 3;
        }
        //Enters wander phase if lions are out of detection range
        else {
            this.targetLion = null;
            this.state = 0;
        }
    }

    //Call this method every time step, controls what the zebra is deciding to do
    public void Update(ArrayList<Plant> plantList, ArrayList<Zebra> zebraList, ArrayList<Lion> lionList, int mapSize) {
        DetectEnemy(lionList);
        DetectPlant(plantList);
        //Search for mate if enough energy and not running from lion
//        if (this.getEnergy() > this.breedEnergy) {
//            this.state = 2;
//        }
        //System.out.println("State: "+this.state);
        //System.out.println("Direction: "+this.getDirection());
        switch (this.state) {
            case 0: //wandering phase
                //System.out.println("NO FOOD");
                Wander(mapSize);
                break;
            case 1: //searching for food
                //System.out.println("FOOD");
                if (this.targetPlant != null) {
                    Move(this.targetPlant.getX(),this.targetPlant.getY(),1,mapSize);
                    Eat(plantList);
                }
                break;
//            case 2: //searching for mate
//                DetectMate(zebraList);
//                Move(this.targetMate.getX(),this.targetMate.getY(),0);
//                Mate();
//            case 3: //running from predator
//                Move(this.targetLion.getX(),this.targetLion.getY(),1);
        }
    }

    //Call every time step during food targeting phase
//    private void GoEat() {
//        Vector<Float> foodDir = new Vector<Float>();
//        foodDir.set(0,this.targetPlant.getX()-this.getX());
//        foodDir.set(1,this.targetPlant.getY()-this.getY());
//
//        this.setDirection(foodDir);
//        Move();
//    }

    //Handles the eating of plants
    private void Eat(ArrayList<Plant> plantList) {
        float distBetween = Equations.EuclDist(this.targetPlant.getX(),this.targetPlant.getY(),getX(),getY());
        if (distBetween < 0.5) {
            // distBetween < speed : eat
            this.setEnergy(this.getEnergy()+this.targetPlant.getFOODVAL());
            plantList.remove(targetPlant); //removes this plant
            this.targetPlant = null; //clear targetPlant variable
            this.PickNewDir();
        }
    }

//   Runs in opposite direction of the target lion
//    private void Run() {
//        Vector<Float> enemyDir = new Vector<Float>();
//        enemyDir.set(0,this.getX()-this.targetLion.getX());
//        enemyDir.set(1,this.getY()-this.targetLion.getY());
//
//        this.setDirection(enemyDir);
//        Move();
//    }

    //Checks for available mate in proximity and mates
    public void Mate() {

    }

    //Call every time step during wander phase
    private void Wander(int mapSize) {
        System.out.println("Wander Timer: "+this.getWanderDirTimer());
        if (this.getWanderDirTimer() == 0) {
            this.setWanderDirTimer(this.getMaxWanderDirTimer());
            this.PickNewDir();
        }
        else {
            this.setWanderDirTimer(this.getWanderDirTimer()-1);
        }
        Advance(mapSize);
    }

    public void setBreedEnergy(int breedEnergy) {
        this.breedEnergy = breedEnergy;
    }
}