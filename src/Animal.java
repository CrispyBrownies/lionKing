
//Created: 3/11/2021
//Main Class for animal properties and methods

import java.util.Vector;

class Animal {
    private String name;
    private float speed;
    private int energy;
    private float detectRange;
    private Vector<Float> direction;
    private int wanderDirTimer; //How long until animal picks new direction during wander
    private int attentionSpan; //How long until animal gives up on target seeking and enter wander

    //position coordinates
    private float x;
    private float y;

    public Animal() {

    }

    public void PickNewDir() {
        double newAngle = Math.random()*2*Math.PI;
        this.direction.set(0,this.speed * (float)Math.cos(newAngle));
        this.direction.set(1,this.speed * (float)Math.sin(newAngle));
    }

    public void Move() {
        this.x += this.direction.get(0);
        this.y += this.direction.get(1);
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

    public void setWanderDirTimer(int wanderDirTimer) {
        this.wanderDirTimer = wanderDirTimer;
    }

    public int getWanderDirTimer() {
        return wanderDirTimer;
    }

    public void setAttentionSpan(int attentionSpan) {
        this.attentionSpan = attentionSpan;
    }

    public int getAttentionSpan() {
        return attentionSpan;
    }

    public Vector<Float> getDirection() {
        return direction;
    }

    public void setDirection(Vector<Float> direction) {
        this.direction = direction;
    }

    public String toString() {
        return getName() + ": Position = (" + getX() + ", " + getY() + ")," +
                " Speed = " + getSpeed() + ", Energy = " + getEnergy() + ", Range = " + getDetectRange();
    }
}