import java.io.IOException;
import java.util.ArrayList;
import java.lang.Math;

class Simulation {
    
    private final int PLANTCOUNT = 10;
    private final int ZEBRACOUNT = 5;
    private final int LIONCOUNT = 1;

    private final int MAPSIZE = 25;
    private final int MAXSPEED = 5;
    private final int MAXENERGY = 1000;
    private final int MAXDETECT = 5;
    private final int MAXBREEDENERGY = 1000;
    private final int MAXATTENTION = 3000;
    private final int MINATTENTION = 100;

    private ArrayList<Plant> PlantList = new ArrayList<Plant>(PLANTCOUNT);
    private ArrayList<Zebra> ZebraList = new ArrayList<Zebra>(ZEBRACOUNT);
    private ArrayList<Lion> LionList = new ArrayList<Lion>(LIONCOUNT);

    public static void main(String[] args) throws IOException {

        Simulation sim = new Simulation();
        int randomSpeed = (int) (Math.random()*sim.getMAXSPEED());

        Lion testLion = new Lion((int) (Math.random()* sim.getMAPSIZE()), (int) (Math.random()* sim.getMAPSIZE()), 2);
        Zebra testZebra = new Zebra((int) (Math.random()* sim.getMAPSIZE()), (int) (Math.random()* sim.getMAPSIZE()), 2);
        Plant plant1 = new Plant((int) (Math.random()* sim.getMAPSIZE()), (int) (Math.random()* sim.getMAPSIZE()));

        String[][] map = new String[sim.getMAPSIZE()][sim.getMAPSIZE()];


        // Zebra movement
        testZebra.newWanderAngle();


        // Lion movement
        testLion.newWanderAngle();

        for (int w=0; w<10; w++) {

            for (int i=0; i<sim.getMAPSIZE(); i++) {
                for (int j=0; j<sim.getMAPSIZE(); j++) {
                    map[i][j] = "| | ";
                }
            }

            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    if (testLion.getX() == i && testLion.getY() == j) {
                        map[i][j] = "[L] ";
                    }
                    if (testZebra.getX() == i && testZebra.getY() == j) {
                        map[i][j] = "[Z] ";
                    }
                    if (plant1.getX() == i && plant1.getY() == j) {
                        map[i][j] = "[P] ";
                    }
                }
            }

            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    System.out.print(map[i][j]);
                }
                System.out.println();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            testZebra.move();
            System.out.println(testZebra);
            System.out.println(testLion);
//            System.out.println(testZebra.getXdirection());
//            System.out.println(testZebra.getYdirection());
            testLion.move();

        }

    }

    public void simulate(Simulation simulation) {

    }

    public Simulation() {

    }




    private void CreateSim() {
        for (int i = PLANTCOUNT; i > 0; i--) {
            Plant newPlant = new Plant((int) (Math.random()*getMAPSIZE()), (int) (Math.random()*getMAPSIZE()));
            PlantList.add(newPlant);
        }
        for (int i = ZEBRACOUNT; i > 0; i--) {
            Zebra newZebra = new Zebra((int)(Math.random()*MAPSIZE), (int)(Math.random()*MAPSIZE),
                    (int)(Math.random()*MAXSPEED), (int)Math.round(Math.random()*MAXENERGY), (float)Math.random()*MAXDETECT, (int)Math.round(Math.random()*MAXBREEDENERGY), (int)(Math.random()*MAXATTENTION+MINATTENTION));
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
}
