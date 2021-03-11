
//Created: 3/11/2021
//Main Class for animal properties and methods

import java.util.Vector;

class Animal {
    private String name;
    private float speed;
    private int energy;
    private float detectRange;

    //position coordinates
    private float x;
    private float y;

    public Animal() {

    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public float getDetectRange() {
        return detectRange;
    }

    public int getEnergy() {
        return energy;
    }

    public void setDetectRange(float detectRange) {
        this.detectRange = detectRange;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String toString() {
        return getName() + ": Position = (" + getX() + ", " + getY() + ")," +
                " Speed = " + getSpeed() + ", Energy = " + getEnergy() + ", Range = " + getDetectRange();
    }

    private float EuclDist(float ax, float ay, float bx, float by) {
        float dist = (float)Math.sqrt((ax-bx)*(ax-bx)+(ay-by)*(ay-by));
        return dist;
    }

    private float EnergyCost(Vector<Float> velocity) {
        float speed = (float)Math.sqrt(velocity.get(1)*velocity.get(1)+velocity.get(2)*velocity.get(2));
        float energyCost = speed; //Equation to calculate energy cost based on speed
        return energyCost;
    }
}