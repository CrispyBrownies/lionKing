import java.util.ArrayList;

class Simulation {
    
    private static int initPlant = 10;
    private static int initZebra = 5;
    private static int initLion = 1;

    private static float mapSize = 100;

    private static float maxSpeed = 10;
    private static float maxEnergy = 1000;
    private static float maxDetect = 5;
    private static float maxBreedEnergy = 1000;

    public static void main(String[] args) {

        ArrayList<Plant> PlantList = new ArrayList<Plant>(initPlant);
        ArrayList<Zebra> ZebraList = new ArrayList<Zebra>(initZebra);
        ArrayList<Lion> LionList = new ArrayList<Lion>(initLion);

        System.out.println("run");

    }

    private void CreateSim() {
        for (int i = initPlant; i > 0; i--) {
            Plant newPlant = new Plant(Math.rand()*mapSize, Math.rand()*mapSize);
            PlantList.add(newPlant);
        }
        for (int i = initZebra; i > 0; i--) {
            Zebra newZebra = new Zebra(Math.rand()*mapSize, Math.rand()*mapSize,
                    Math.rand()*maxSpeed, Math.rand()*maxEnergy, Math.rand()*maxDetect, Math.rand()*maxBreedEnergy);
            ZebraList.add(newZebra);
        }
    }

}