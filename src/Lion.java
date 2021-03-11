
//Created: 3/11/2021
//Main class for Lion (predator)

import java.util.ArrayList;

class Lion extends Animal {

    private ArrayList<Zebra> inRange;

    public Lion() { }

    public Lion(float x, float y) {
        setName("Lion");
        setEnergy(10000);
        setSpeed(10);
        setDetectRange(7);
    }

    private void detectPrey(ArrayList<Zebra> zebraList) {
//        for (x:zebraList.size()); { //for each thing in list
//
//        }
    }

    public void setX(float x) {
        super.setX(x);
    }


    public void setEnergy(int energy) {
        super.setEnergy(energy);
    }

    public void setName(String name) {
        super.setName(name);
    }


    public void setDetectRange(float detectRange) {
        super.setDetectRange(detectRange);
    }

    public void setSpeed(float speed) {
        super.setSpeed(speed);
    }

    public void setY(float y) {
        super.setY(y);
    }

    public void setInRange(ArrayList<Zebra> inRange) {
        this.inRange = inRange;
    }

    public float getSpeed() {
        return super.getSpeed();
    }

    public String getName() {
        return super.getName();
    }

    public ArrayList<Zebra> getInRange() {
        return inRange;
    }

}