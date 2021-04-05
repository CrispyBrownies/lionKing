
//Created: 3/11/2021
//Main class for Zebra (prey)

import java.util.*;

class Zebra extends Animal {

    private final String name = "Zebra";

    private float breedEnergy; //Energy required to make baby
    private float babyEnergy; //How much energy given to baby
    private int state; //State of the zebra, 0 = wander, 1 = food, 2 = mate, 3 = run, 4 = mating, 10 = invalid
    private final int maxBreedTimer = 500;
    private int breedTimer = maxBreedTimer;
    private boolean targeted;
    private float desirability = 100;
    private float desirabilityThreshold = 10;
    private final int maxBadMate = 10;
    private int badMateCounter = maxBadMate;
    private int generation = 1;
    private float startEnergy;

    private Plant targetPlant;
    private Zebra targetMate;
    private Lion targetLion;

    public ArrayList<Zebra> badMates = new ArrayList<>();

    public Zebra() {
        this.state = 10;
    }

    public Zebra(float x, float y, float speed, float energy, float detectRange, float breedEnergy, float babyEnergy, int maxWanderDirTimer, int attentionSpan, float desirability, float desirabilityThreshold) {
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
        setAge(0);
        if (babyEnergy > breedEnergy) {babyEnergy = breedEnergy;};
        setBabyEnergy(babyEnergy);
        setColor(Equations.toVector(170f / 255f, 170f / 255f, 170f / 255f));
        setTargetedColor(Equations.toVector(170f / 255f, 170f / 255f, 170f / 255f));
        setDesirability(desirability);
        setDesirabilityThreshold(desirabilityThreshold);
        this.PickNewDir();
        this.setDirection(Equations.toVector(0f,1f));
        this.setTargetPos(Equations.toVector(0f,0f));
        this.setMovementState(0);
        this.startEnergy = energy;
    }

    //Searches for plants in detection range, sets target plant to nearest plant
    private void DetectPlant(ArrayList<Plant> plantList) {
        ArrayList<Plant> plantInRange = new ArrayList<Plant>();

        //Clears target plant if not in range, looks for new food
        if (targetPlant != null) {
            targetPlant.setTargeted(true);
            if (Equations.EuclDist(targetPlant.getX(), targetPlant.getY(), getX(), getY()) > getDetectRange()) {
                this.targetPlant.setTargeted(false);
                this.targetPlant = null;
                PickNewPlant(plantList);
            }
        }
        else {
            PickNewPlant(plantList);
        }
    }

    //Picks a random plant in range
    private void PickNewPlant(ArrayList<Plant> plantList) {
        ArrayList<Plant> plantInRange = new ArrayList<Plant>();
        for (Plant plant : plantList) {
            float distBetween = Equations.EuclDist(plant.getX(), plant.getY(), getX(), getY());
            if (distBetween < getDetectRange()) {
                plantInRange.add(plant);
            }
        }
        int pickNum = (int) (Math.random() * plantInRange.size());
        if (plantInRange.size() > 0) {
            this.targetPlant = plantInRange.get(pickNum);
        }
    }

    private void StateManager() {
        if (this.targetLion != null) {
            this.state = 3;
            //this.setTargetPos(Equations.toVector(targetLion.getX(),targetLion.getY()));
        }
        else {
            if (this.targetMate != null && this.getEnergy() > this.getBreedEnergy()) { //
                this.state = 2;
                //this.setTargetPos(Equations.toVector(targetMate.getX(), targetMate.getY()));
            }
            else {
                if (this.targetPlant != null) {
                    this.state = 1;
                    //this.setTargetPos(Equations.toVector(targetPlant.getX(), targetPlant.getY()));
                } else {
                    this.state = 0;
                }
            }
        }
    }

    //Searches for mate in detection range and sets mate target to closest available mate
    private void DetectMate(ArrayList<Zebra> zebraList) {

        if (targetMate == null) {
            //If another zebra targets this one, if that one is desirable, pick them too
            for (Zebra zebra : zebraList) {
                if (zebra.targetMate == this) {
                    boolean inRange = Equations.EuclDist(zebra.getX(), zebra.getY(), getX(), getY()) < getDetectRange();
                    if (inRange) {
                        if (zebra.getDesirability() < this.desirabilityThreshold) {
                            this.targetMate = zebra;
                            RemoveMate();
                        } else {
                            //System.out.println("good mate");

                            this.targetMate = zebra;
                        }
                    }
                    else {
                        if (zebra.breedTimer != 0) {
                            zebra.breedTimer--;
                        }
                        else {
                            zebra.breedTimer = zebra.maxBreedTimer;
                            zebra.badMates.add(this);
                            zebra.targetMate=null;
                            this.targeted = false;
                        }
                    }
                }
            }
            if (targetMate == null) {
                PickNewMate(zebraList);
            }
        }
        else if (targetMate.targetMate == this){
            MateColor();
            targetMate.setTargeted(true);

            boolean outOfRange = Equations.EuclDist(targetMate.getX(), targetMate.getY(), getX(), getY()) > getDetectRange();

            if (Equations.EuclDist(targetMate.getX(), targetMate.getY(), getX(), getY()) > getDetectRange()) {
                RemoveMate();
                PickNewMate(zebraList);
            }
            else if (this.getEnergy() < this.getBreedEnergy() || this.targetMate.getEnergy() < this.targetMate.getBreedEnergy()) {
                RemoveMate();
            }
        }
//        else {
//            System.out.println("rejected!");
//            RemoveMate();
//        }
    }

    private void RemoveMate() {
        if (true) {
            targetMate.setTargeted(false);
            this.setTargeted(false);
            this.badMates.add(targetMate);
            targetMate.badMates.add(this);
            targetMate.targetMate = null;
            targetMate = null;
        }
    }

    private void MateColor() {
        if (this.targetMate.targetMate == this && !this.targeted) {
            float redValue = (float)Math.random();
            float blueValue = (float)Math.random();
            float greenValue = (float)Math.random();

            this.targetMate.setTargetedColor(Equations.toVector(redValue,blueValue,greenValue));
            this.setTargetedColor(Equations.toVector(redValue,blueValue,greenValue));
        }
    }

    private void MateAvoider() {
        if (this.badMateCounter == 0) {
            if (this.badMates.size() > 0) {
                this.badMates.remove(0);
            }
            this.badMateCounter = this.maxBadMate;
        }
        else {
            this.badMateCounter -= 1;
        }
    }

    private void PickNewMate(ArrayList<Zebra> zebraList) {
        HashMap<Float, Zebra> mateMap = new HashMap<Float, Zebra>();
        for (Zebra zebra:zebraList) {
            if (zebra != this && !this.badMates.contains(zebra)) {
                float distBetween = Equations.EuclDist(zebra.getX(), zebra.getY(), getX(), getY());
                if ((distBetween < getDetectRange()) && (zebra.getEnergy()>zebra.getBreedEnergy())) {
                    mateMap.put(zebra.getDesirability(), zebra);
                }
            }
        }
        if (mateMap.size()>0) {
            Set<Float> desirability = mateMap.keySet();
            float maxDesirability = Collections.max(desirability);
            this.targetMate = mateMap.get(maxDesirability);
        }
    }

    private void PickNewPredator(ArrayList<Lion> lionList) {
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
        }
    }

    //Searches for nearby enemies within detection range, sets nearest lion to target lion
    private void DetectEnemy(ArrayList<Lion> lionList) {

        if (targetLion != null) {
            targetLion.setTargeted(true);
            if (Equations.EuclDist(targetLion.getX(), targetLion.getY(), getX(), getY()) > getDetectRange()) {
                this.targetLion.setTargeted(false);
                this.targetLion = null;
                PickNewPredator(lionList);
            }
        }
        else {
            PickNewPredator(lionList);
        }
    }

    //Call this method every time step, controls what the zebra is deciding to do
    public void Update(ArrayList<Plant> plantList, ArrayList<Zebra> zebraList, ArrayList<Lion> lionList, int mapSize) {

        this.setAge(this.getAge()+1);

        //ArrayList<Zebra> addZebras = new ArrayList<Zebra>();

        DetectEnemy(lionList);
        DetectMate(zebraList);

        //System.out.println("breedtimer: "+this.breedTimer);
//        if (this.breedTimer == 0) {
//
//        } else {
//            this.breedTimer-=1;
//        }
        DetectPlant(plantList);
        StateManager();

        System.out.println("State: "+this.state);
        System.out.println("Energy: "+this.getEnergy());
        System.out.println("can breed: "+(this.getBreedEnergy()<this.getEnergy()));
        switch (this.state) {
            case 0: //wandering phase
                Wander(mapSize);
                break;
            case 1: //searching for food
                //System.out.println("FOOD");
                if (this.targetPlant != null) {
                    SetTargetDir(this.targetPlant);
                    //Move(this.targetPlant.getX(),this.targetPlant.getY(),1,mapSize);
                    Eat(plantList);
                }
                break;
            case 2: //searching for mate
                SetTargetDir(this.targetMate);
                break;
            case 3: //running from predator
                SetTargetDir(this.targetLion);
                //Move(this.targetLion.getX(),this.targetLion.getY(),0,mapSize);
                break;
        }
        Turn(0);
        Advance(mapSize);
        MateAvoider();
        CheckAlive();
        //return addZebras;
    }

    //Handles the eating of plants
    private void Eat(ArrayList<Plant> plantList) {
        float distBetween = Equations.EuclDist(this.targetPlant.getX(),this.targetPlant.getY(),getX(),getY());
        if (distBetween < 0.5) {
            this.setEnergy(this.getEnergy()+this.targetPlant.getFOODVAL());
            plantList.remove(targetPlant); //removes this plant
            this.targetPlant = null; //clear targetPlant variable
            //this.PickNewDir();
        }
    }

    public float getStartEnergy() {
        return startEnergy;
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public Zebra getTargetMate() {
        return targetMate;
    }

    public void setTargetMate(Zebra targetMate) {
        this.targetMate = targetMate;
    }

    public float getDesirability() {
        return desirability;
    }

    public float getDesirabilityThreshold() {
        return desirabilityThreshold;
    }

    public int getState() {
        return state;
    }

    public float getBreedEnergy() {
        return breedEnergy;
    }

    public float getBabyEnergy() {
        return babyEnergy;
    }

    public Plant getTargetPlant() {
        return targetPlant;
    }

    public void setDesirability(float desirability) {
        this.desirability = desirability;
    }

    public void setDesirabilityThreshold(float desirabilityThreshold) {
        this.desirabilityThreshold = desirabilityThreshold;
    }

    public void setTargetPlant(Plant targetPlant) {
        this.targetPlant = targetPlant;
    }

    public void setTargeted(boolean targeted) {
        this.targeted = targeted;
    }

    public boolean isTargeted() {
        return targeted;
    }

    public void setBreedEnergy(float breedEnergy) {
        this.breedEnergy = breedEnergy;
    }

    public void setBabyEnergy(float babyEnergy) {
        this.babyEnergy = babyEnergy;
    }


}

//   CODE GRAVEYARD
//=========================================================
//   Runs in opposite direction of the target lion
//    private void Run() {
//        Vector<Float> enemyDir = new Vector<Float>();
//        enemyDir.set(0,this.getX()-this.targetLion.getX());
//        enemyDir.set(1,this.getY()-this.targetLion.getY());
//
//        this.setDirection(enemyDir);
//        Move();
//    }

//Call every time step during food targeting phase
//    private void GoEat() {
//        Vector<Float> foodDir = new Vector<Float>();
//        foodDir.set(0,this.targetPlant.getX()-this.getX());
//        foodDir.set(1,this.targetPlant.getY()-this.getY());
//
//        this.setDirection(foodDir);
//        Move();
//    }

//        System.out.println(targetPlant);
//
//        if (this.state != 3) {
//            if (this.breedTimer == 0) {
//                DetectMate(zebraList);
//            }
//            else {
//                this.breedTimer-=1;
//            }
//            if (this.state != 2) {
//                //System.out.printf("run");
//                DetectPlant(plantList);
//            }
//        }
//Search for mate if enough energy and not running from lion
//        if (this.getEnergy() > this.breedEnergy && this.targetMate != null) {
//            this.state = 2;
//        }
//
//    public Zebra(int x, int y, int speed, int environmentSize) {
//        setName("Zebra");
//        setX(x);
//        setY(y);
//        setSpeed(speed);
//        setEnvironmentSize(environmentSize);
//    }

//Checks for available mate in proximity and mates
//    private Zebra Mate() {
//        Zebra baby = new Zebra();
//        float distBetween = Equations.EuclDist(this.targetMate.getX(),this.targetMate.getY(),getX(),getY());
//        float prevSpeed = this.getSpeed();
//        if (distBetween < 0.5) {
//            if (this.targetMate.state == 4 && this.state == 2) {
//                baby = new Zebra(this.getX(),this.getY(),(this.getSpeed()+this.targetMate.getSpeed())/2f,(this.getBabyEnergy()+this.targetMate.getBabyEnergy())/2f,(this.getDetectRange()+this.targetMate.getDetectRange())/2f,(this.getBreedEnergy()+this.targetMate.getBreedEnergy())/2f,(this.getBabyEnergy()+this.targetMate.getBabyEnergy())/2f,(this.getMaxWanderDirTimer()+this.targetMate.getMaxWanderDirTimer())/2,(this.getAttentionSpan()+this.targetMate.getAttentionSpan())/2);
//                this.setEnergy(this.getEnergy()-this.babyEnergy);
//                this.targetMate.setEnergy(this.targetMate.getEnergy()-this.targetMate.babyEnergy);
//
//            }
//            this.state = 4;
//            if (this.targetMate.state == 2) {
//                this.badMates.add(this.targetMate);
//                this.badMates.add(baby);
//                this.targetMate.badMates.add(this);
//                this.targetMate.badMates.add(baby);
//                baby.badMates.add(this);
//                baby.badMates.add(this.targetMate);
//                this.state = 0;
//                this.targetMate.state = 0;
//                this.PickNewDir();
//                this.targetMate.PickNewDir();
//                baby.breedTimer = baby.maxBreedTimer;
//                this.targetMate.breedTimer = this.targetMate.maxBreedTimer;
//                this.breedTimer = this.maxBreedTimer;
//                this.setTargeted(false);
//                this.targetMate.setTargeted(false);
//                baby.setTargeted(false);
//                this.targetMate.targetMate = null;
//                this.targetMate = null;
//            }
//        }
//        return baby;
//    }