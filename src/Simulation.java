import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.lang.reflect.Array;
import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.Math;
import java.util.Iterator;

class Simulation {

    private final int PLANTCOUNT = 10;
    private final int ZEBRACOUNT = 10;
    private final int LIONCOUNT = 0;

    private final int MAPSIZE = 100;
    private final float MAXSPEED = 0.5f;
    private final float MAXENERGY = 200f;
    private final int MAXDETECT = 100;
    private final int MAXBREEDENERGY = 100;
    private final int MAXATTENTION = 3000;
    private final int MINATTENTION = 100;
    private final int MAXWANDERDIRTIME = 1000;
    private final float MAXBABYENERGY = 50;
    private final float DESIRABILITY = 100;
    private final float DESIRABILITYTHRESHOLD = 10;
    private final int MAXLIFESPAN = 15000;
    private static boolean RunSim = true;
    private final int MAXWFOODTIMER = 25;
    private int spawnFoodTimer = MAXWFOODTIMER;
    private int plantID = 0;

    private ArrayList<Plant> PlantList = new ArrayList<Plant>(PLANTCOUNT);
    private ArrayList<Zebra> ZebraList = new ArrayList<Zebra>(ZEBRACOUNT);
    private ArrayList<Lion> LionList = new ArrayList<Lion>(LIONCOUNT);

    public static void main(String[] args) throws IOException {

        Simulation sim = new Simulation();
        Graphics graphics = new Graphics(sim.getMAPSIZE());
        //System.out.println("hi");

        sim.CreateSim();
        Data.CreateFiles();
        Data.CreateHeaders();

        while (RunSim) {
            GL.createCapabilities();

            // Set the clear color
            glClearColor(113f / 255f, 245f / 255f, 40f / 255f, 0.0f);

            // Run the rendering loop until the user has attempted to close
            // the window or has pressed the ESCAPE key.
            while (!glfwWindowShouldClose(graphics.getWindow())) {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

                Zebra newBaby = new Zebra();

                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //System.out.println(sim.spawnFoodTimer);
                if (sim.spawnFoodTimer == 0) {
                    sim.spawnFoodTimer = sim.MAXWFOODTIMER;
                    sim.plantID++;
                    sim.PlantList.add(new Plant((int) (Math.random() * 2 * sim.getMAPSIZE()), (int) (Math.random() * 2 * sim.getMAPSIZE()), sim.plantID));
                }

                sim.CheckDeath();
                ArrayList<Zebra> addZebra = new ArrayList<>();

                for (Plant plant : sim.PlantList) {
                    Graphics.DrawObject(plant);
                    //graphics.DrawPlant(plant);
                }
                for (Zebra zebra : sim.ZebraList) {
                    zebra.Update(sim.getPlantList(), sim.getZebraList(), sim.getLionList(), sim.getMAPSIZE());
                    newBaby = sim.Mate(zebra);
                    if (newBaby.getState() != 10) {
                        addZebra.add(newBaby);
                    }
                    Graphics.DrawObject(zebra);
                }
                sim.ZebraList.addAll(addZebra);
                addZebra.clear();
                for (Lion lion : sim.LionList) {
                    lion.Update(sim.getZebraList(), sim.getMAPSIZE());
//                    graphics.DrawLion(lion);
                    Graphics.DrawObject(lion);
                }

                sim.spawnFoodTimer -= 1;
                Graphics.Update();
            }
            RunSim = false;
        }

        graphics.term();
    }

    //Checks if the animal is alive, if dead, remove from list
    private void CheckDeath() {

        ArrayList<Zebra> removeList = new ArrayList<>();

        for (Zebra zebra:ZebraList) {
            if (!zebra.getAlive()) {
                Data.RecordData(zebra,"Zebra");
                removeList.add(zebra);
            }
        }
        ZebraList.removeAll(removeList);
        //ZebraList.removeIf(zebra -> !zebra.getAlive());
        LionList.removeIf(lion -> !lion.getAlive());
    }

    public Simulation() {

    }

    private void CreateSim() {
        for (int i = ZEBRACOUNT; i > 0; i--) {
            int life = (int) (Math.random() * MAXLIFESPAN);
            System.out.println("life: "+life);
            Zebra newZebra = new Zebra((int) (Math.random() * 2 * MAPSIZE), (int) (Math.random() * 2 * MAPSIZE),
                    (float) (Math.random() * MAXSPEED), (float) Math.random() * MAXENERGY,
                    (float) Math.random() * MAXDETECT, (int) Math.round(Math.random() * MAXBREEDENERGY),
                    (float) Math.random() * MAXBABYENERGY, (int) Math.round(Math.random() * MAXWANDERDIRTIME),
                    (int) (Math.random() * MAXATTENTION + MINATTENTION),(float) Math.random() * DESIRABILITY, (float) Math.random() * DESIRABILITYTHRESHOLD, life);
            newZebra.CheckCollision(getMAPSIZE());
            ZebraList.add(newZebra);
        }
        for (int i = LIONCOUNT; i > 0; i--) {
            Lion newLion = new Lion((int) (Math.random() * 2 * MAPSIZE), (int) (Math.random() * 2 * MAPSIZE) * MAPSIZE);
            newLion.CheckCollision(getMAPSIZE());
            LionList.add(newLion);
        }
        for (int i = PLANTCOUNT; i > 0; i--) {
            plantID++;
            Plant newPlant = new Plant((int) (Math.random() * 2 * MAPSIZE), (int) (Math.random() * 2 * MAPSIZE), plantID);
            PlantList.add(newPlant);
        }
    }

    private Zebra Mate(Zebra zebra) {

        boolean inRange = false;
        boolean breedState = false;
        boolean matchMate = false;
        Zebra baby = new Zebra();

        if (zebra.getTargetMate() != null){
            matchMate = (zebra.getTargetMate().getTargetMate() == zebra);
            inRange = (Equations.EuclDist(zebra.getTargetMate().getX(),zebra.getTargetMate().getY(),zebra.getX(),zebra.getY()) < 1);
            breedState = ((zebra.getState() == 2) && (zebra.getTargetMate().getState() == 2));
        }

//        System.out.println("matchmate: "+matchMate);
//        System.out.println("inrange: "+inRange);
//        System.out.println("breedstate: "+breedState);

        //if can breed,
        if (matchMate && inRange && breedState) {
            //System.out.println("New Zebra!");

            float newX = zebra.getX();
            float newY = zebra.getY();
            float newEnergy = (zebra.getBabyEnergy()+zebra.getTargetMate().getBabyEnergy()) + (float)Math.random()*0f;
            zebra.setEnergy(zebra.getEnergy()-zebra.getBabyEnergy());
            zebra.getTargetMate().setEnergy(zebra.getTargetMate().getEnergy()-zebra.getTargetMate().getBabyEnergy());
            float newSpeed = ((zebra.getSpeed()+zebra.getTargetMate().getSpeed())/2) + (float)Math.random()*Equations.GetRandomSign()*0.1f*Math.max(zebra.getSpeed(),zebra.getTargetMate().getSpeed());
            float newBreedEnergy = ((zebra.getBreedEnergy()+zebra.getTargetMate().getBreedEnergy())/2) + (float)Math.random()*Equations.GetRandomSign()*0.1f*Math.max(zebra.getBreedEnergy(),zebra.getTargetMate().getBreedEnergy());
            float newRange = ((zebra.getDetectRange()+zebra.getTargetMate().getDetectRange())/2) + (float)Math.random()*Equations.GetRandomSign()*0.1f*Math.max(zebra.getDetectRange(),zebra.getTargetMate().getDetectRange());
            float newBabyEnergy = ((zebra.getBabyEnergy()+zebra.getTargetMate().getBabyEnergy())/2) + (float)Math.random()*Equations.GetRandomSign()*0.1f*Math.max(zebra.getBabyEnergy(),zebra.getTargetMate().getBabyEnergy());
            int newWanderDirTimer = ((zebra.getMaxWanderDirTimer()+zebra.getTargetMate().getMaxWanderDirTimer())/2) + (int)(Math.random()*0);
            int newAttentionSpan = ((zebra.getMAXATTENTIONSPAN()+zebra.getTargetMate().getMAXATTENTIONSPAN())/2) + (int)Math.round(Math.random()*Equations.GetRandomSign()*0.1f*Math.max(zebra.getMAXATTENTIONSPAN(),zebra.getTargetMate().getMAXATTENTIONSPAN()));
            float newDesirability = ((zebra.getDesirability()+zebra.getTargetMate().getDesirability())/2) + (float)Math.random()*Equations.GetRandomSign()*0.1f*Math.max(zebra.getDesirability(),zebra.getTargetMate().getDesirability());
            float newDesirabilityThreshold = ((zebra.getDesirabilityThreshold()+zebra.getTargetMate().getDesirabilityThreshold())/2) + (float)Math.random()*Equations.GetRandomSign()*0.1f*Math.max(zebra.getDesirabilityThreshold(),zebra.getTargetMate().getDesirabilityThreshold());
            int newGen = (Math.max(zebra.getGeneration(),zebra.getTargetMate().getGeneration()))+1;
            int newLifespan = ((zebra.getLifespan()+zebra.getTargetMate().getLifespan())/2) + (int)Math.round(Math.random()*Equations.GetRandomSign()*0.1f*Math.max(zebra.getLifespan(),zebra.getTargetMate().getLifespan()));


            baby = new Zebra(newX,newY,newSpeed,newEnergy,newRange,newBreedEnergy,newBabyEnergy,newWanderDirTimer,newAttentionSpan,newDesirability,newDesirabilityThreshold,newLifespan);
            baby.setGeneration(newGen);

            //add bad mates so they find new mates
            zebra.badMates.add(zebra.getTargetMate());
            zebra.getTargetMate().badMates.add(zebra);
            zebra.badMates.add(baby);
            zebra.getTargetMate().badMates.add(baby);
            baby.badMates.add(zebra);
            baby.badMates.add(zebra.getTargetMate());

            zebra.getTargetMate().setTargeted(false);
            zebra.setTargeted(false);

            baby.setTargetMate(null);
            zebra.getTargetMate().setTargetMate(null);
            zebra.setTargetMate(null);

            return baby;
        }
        return baby;
    }

    public ArrayList<Lion> getLionList() {
        return LionList;
    }

    public ArrayList<Plant> getPlantList() {
        return PlantList;
    }

    public ArrayList<Zebra> getZebraList() {
        return ZebraList;
    }

    public float getMAXBREEDENERGY() {
        return MAXBREEDENERGY;
    }

    public float getMAXDETECT() {
        return MAXDETECT;
    }

    public float getMAXENERGY() {
        return MAXENERGY;
    }

    public float getMAXSPEED() {
        return MAXSPEED;
    }

    public int getLIONCOUNT() {
        return LIONCOUNT;
    }

    public int getMAPSIZE() {
        return MAPSIZE;
    }

    public int getMAXATTENTION() {
        return MAXATTENTION;
    }

    public int getMINATTENTION() {
        return MINATTENTION;
    }

    public int getPLANTCOUNT() {
        return PLANTCOUNT;
    }

    public int getZEBRACOUNT() {
        return ZEBRACOUNT;
    }

    public void setLionList(ArrayList<Lion> lionList) {
        LionList = lionList;
    }

    public void setPlantList(ArrayList<Plant> plantList) {
        PlantList = plantList;
    }

    public void setZebraList(ArrayList<Zebra> zebraList) {
        ZebraList = zebraList;
    }
}

//    CODE GRAVEYARD
//=================================================================================================
//    public void simulate(Simulation simulation) {
//
//    }

//        int randomSpeed = (int) (Math.random()*sim.getMAXSPEED());
//
//        Lion testLion = new Lion((int) (Math.random()* sim.getMAPSIZE()), (int) (Math.random()* sim.getMAPSIZE()), 2);
//        Zebra testZebra = new Zebra((int) (Math.random()* sim.getMAPSIZE()), (int) (Math.random()* sim.getMAPSIZE()), 2);
//        Plant plant1 = new Plant((int) (Math.random()* sim.getMAPSIZE()), (int) (Math.random()* sim.getMAPSIZE()));
//
//        String[][] map = new String[sim.getMAPSIZE()][sim.getMAPSIZE()];
//
//
//        // Zebra movement
//        testZebra.newWanderAngle();
//
//
//        // Lion movement
//        testLion.newWanderAngle();

//        for (int w=0; w<10; w++) {
//
//            for (int i=0; i<sim.getMAPSIZE(); i++) {
//                for (int j=0; j<sim.getMAPSIZE(); j++) {
//                    map[i][j] = "| | ";
//                }
//            }
//
//            for (int i = 0; i < map.length; i++) {
//                for (int j = 0; j < map[i].length; j++) {
//                    if (testLion.getX() == i && testLion.getY() == j) {
//                        map[i][j] = "[L] ";
//                    }
//                    if (testZebra.getX() == i && testZebra.getY() == j) {
//                        map[i][j] = "[Z] ";
//                    }
//                    if (plant1.getX() == i && plant1.getY() == j) {
//                        map[i][j] = "[P] ";
//                    }
//                }
//            }
//
//            for (int i = 0; i < map.length; i++) {
//                for (int j = 0; j < map[i].length; j++) {
//                    System.out.print(map[i][j]);
//                }
//                System.out.println();
//            }
//
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            testZebra.move();
//            System.out.println(testZebra);
//            System.out.println(testLion);
////            System.out.println(testZebra.getXdirection());
////            System.out.println(testZebra.getYdirection());
//            testLion.move();
//
//        }

