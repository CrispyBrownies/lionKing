import java.util.ArrayList;
import java.lang.Math;

class Simulation {
    
    private final int PLANTCOUNT = 10;
    private final int ZEBRACOUNT = 5;
    private final int LIONCOUNT = 1;

    private final float MAPSIZE = 100;
    private final float MAXSPEED = 10;
    private final float MAXENERGY = 1000;
    private final float maxDetect = 5;
    private final float maxBreedEnergy = 1000;

    private ArrayList<Plant> PlantList = new ArrayList<Plant>(PLANTCOUNT);
    private ArrayList<Zebra> ZebraList = new ArrayList<Zebra>(ZEBRACOUNT);
    private ArrayList<Lion> LionList = new ArrayList<Lion>(LIONCOUNT);

    public static void main(String[] args) {

        System.out.println("run");

    }

    private void CreateSim() {
        for (int i = PLANTCOUNT; i > 0; i--) {
            Plant newPlant = new Plant((float)Math.random()*MAPSIZE, (float)Math.random()*MAPSIZE);
            PlantList.add(newPlant);
        }
        for (int i = ZEBRACOUNT; i > 0; i--) {
            Zebra newZebra = new Zebra((float)Math.random()*MAPSIZE, (float)Math.random()*MAPSIZE,
                    (float)Math.random()*MAXSPEED, (int)Math.round(Math.random()*MAXENERGY), (float)Math.random()*maxDetect, (int)Math.round(Math.random()*maxBreedEnergy));
            ZebraList.add(newZebra);
        }
        for (int i = LIONCOUNT; i > 0; i--) {
            Lion newLion = new Lion((float)Math.random()*MAPSIZE, (float)Math.random()*MAPSIZE);
            LionList.add(newLion);
        }
    }

}
