import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.Math;

class Simulation {
    
    private final int PLANTCOUNT = 0;
    private final int ZEBRACOUNT = 1;
    private final int LIONCOUNT = 1;

    private final int MAPSIZE = 100;
    private final float MAXSPEED = 0.5f;
    private final float MAXENERGY = 1000f;
    private final int MAXDETECT = 10;
    private final int MAXBREEDENERGY = 1000;
    private final int MAXATTENTION = 3000;
    private final int MINATTENTION = 100;
    private final int MAXWANDERDIRTIME = 1000;
    private final int MAXWFOODTIMER = 500;
    private static boolean RunSim = true;
    private int spawnFoodTimer = 500;

    private ArrayList<Plant> PlantList = new ArrayList<Plant>(PLANTCOUNT);
    private ArrayList<Zebra> ZebraList = new ArrayList<Zebra>(ZEBRACOUNT);
    private ArrayList<Lion> LionList = new ArrayList<Lion>(LIONCOUNT);

    public static void main(String[] args) throws IOException {

        Graphics graphics = new Graphics();
        Simulation sim = new Simulation();

        sim.CreateSim();

        while(RunSim) {
            // This line is critical for LWJGL's interoperation with GLFW's
            // OpenGL context, or any context that is managed externally.
            // LWJGL detects the context that is current in the current thread,
            // creates the GLCapabilities instance and makes the OpenGL
            // bindings available for use.
            GL.createCapabilities();

            // Set the clear color
            glClearColor(113f/255f, 245f/255f, 40f/255f, 0.0f);

            // Run the rendering loop until the user has attempted to close
            // the window or has pressed the ESCAPE key.
            while ( !glfwWindowShouldClose(graphics.getWindow()) ) {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
                //System.out.println(sim.spawnFoodTimer);
                if (sim.spawnFoodTimer == 0) {
                    sim.spawnFoodTimer = sim.MAXWFOODTIMER;
                    sim.PlantList.add(new Plant((int) (Math.random()*sim.getMAPSIZE()), (int) (Math.random()*sim.getMAPSIZE())));
                }

                sim.CheckDeath();
                for (Plant plant: sim.PlantList) {
                    graphics.DrawPlant(plant);
                }
                for (Zebra zebra: sim.ZebraList) {
                    zebra.Update(sim.getPlantList(),sim.getZebraList(),sim.getLionList(),sim.getMAPSIZE());
                    graphics.DrawZebra(zebra);
                }
                for (Lion lion: sim.LionList) {
                    lion.Update(sim.getZebraList(),sim.getMAPSIZE());
                    graphics.DrawLion(lion);
                }
                sim.spawnFoodTimer -= 1;
                graphics.Update();
            }
            RunSim = false;
        }

        graphics.term();
    }

    //Checks if the animal is alive, if dead, remove from list
    private void CheckDeath() {
        ZebraList.removeIf(zebra -> zebra.getEnergy() <= 0);
        LionList.removeIf(lion -> lion.getEnergy() <= 0);
    }

    public void simulate(Simulation simulation) {

    }

    public Simulation() {

    }

    private void CreateSim() {
        for (int i = PLANTCOUNT; i > 0; i--) {
            Plant newPlant = new Plant((int) (Math.random()*2*MAPSIZE), (int) (Math.random()*2*MAPSIZE));
            PlantList.add(newPlant);
        }
        for (int i = ZEBRACOUNT; i > 0; i--) {
            Zebra newZebra = new Zebra((int)(Math.random()*2*MAPSIZE), (int)(Math.random()*2*MAPSIZE),
                    (float)(Math.random()*MAXSPEED), (float)Math.random()*MAXENERGY, (float)Math.random()*MAXDETECT, (int)Math.round(Math.random()*MAXBREEDENERGY), (int)Math.round(Math.random()*MAXWANDERDIRTIME), (int)(Math.random()*MAXATTENTION+MINATTENTION));
            ZebraList.add(newZebra);
        }
        for (int i = LIONCOUNT; i > 0; i--) {
            Lion newLion = new Lion((float)Math.random()*MAPSIZE, (float)Math.random()*MAPSIZE);
            LionList.add(newLion);
        }
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

}
