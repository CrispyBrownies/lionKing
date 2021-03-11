import java.util.ArrayList;
import java.lang.Math;

class Simulation {
    
    private static int initPlant = 10;
    private static int initZebra = 5;
    private static int initLion = 1;

    private static float mapSize = 100;

    private static float maxSpeed = 10;
    private static float maxEnergy = 1000;
    private static float maxDetect = 5;
    private static float maxBreedEnergy = 1000;

    private ArrayList<Plant> PlantList = new ArrayList<Plant>(initPlant);
    private ArrayList<Zebra> ZebraList = new ArrayList<Zebra>(initZebra);
    private ArrayList<Lion> LionList = new ArrayList<Lion>(initLion);

    public static void main(String[] args) {



        System.out.println("run");

    }

    private void CreateSim() {
        for (int i = initPlant; i > 0; i--) {
            Plant newPlant = new Plant((float)Math.random()*mapSize, (float)Math.random()*mapSize);
            PlantList.add(newPlant);
        }
        for (int i = initZebra; i > 0; i--) {
            Zebra newZebra = new Zebra((float)Math.random()*mapSize, (float)Math.random()*mapSize,
                    (float)Math.random()*maxSpeed, (int)Math.round(Math.random()*maxEnergy), (float)Math.random()*maxDetect, (int)Math.round(Math.random()*maxBreedEnergy));
            ZebraList.add(newZebra);
        }
    }

}
