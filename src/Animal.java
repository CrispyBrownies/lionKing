
//Created: 3/11/2021
//Main Class for animal properties and methods

import java.util.Vector;

class Animal {
    private String name;
    private float speed;
    private int energy;
    private float detectRange;

    private double xdirection;
    private double ydirection;

    private Vector<Float> direction;
    private int wanderDirTimer; //How long until animal picks new direction during wander
    private int maxWanderDirTimer;
    private int attentionSpan; //How long until animal gives up on target seeking and enter wander

    //position coordinates
    private float x;
    private float y;

    public Animal() {

    }

    public void newWanderAngle() {
        double angle = Math.random()*2*Math.PI;
        setXdirection(Math.sin(angle));
        setYdirection(Math.cos(angle));
    }

    public void move() {
        setX((int) ((int) (getSpeed()*getXdirection()) + getX()));
        setY((int) ((int) (getSpeed()*getYdirection()) + getY()));
    }

    //Sets animal's direction to new random direction
    public void PickNewDir() {
        double newAngle = Math.random()*2*Math.PI;
        Vector<Float> moveDir = new Vector<Float>();
        System.out.println(getSpeed());
        moveDir.add((float)Math.cos(newAngle));
        moveDir.add((float)Math.sin(newAngle));
        this.direction = moveDir;
    }

    //Handles movement of animal, dir = 1: towards, else: away
    public void Move(float targetx, float targety, int dir) {
        Vector<Float> moveDir = new Vector<Float>();
        float magnitude = Equations.EuclDist(targetx,targety,this.x,this.y);
        if (dir == 0) {
            moveDir.add((this.x-targetx)/magnitude);
            moveDir.add((this.y-targety)/magnitude);
        }
        else {
            moveDir.add((targetx-this.x)/magnitude);
            moveDir.add((targety-this.y)/magnitude);
        }
        this.direction = moveDir;
        Advance();
    }

    public void Advance() {
        System.out.println("Direction: "+this.direction);
        System.out.println("Position: "+this.x+" "+this.y);
        System.out.println("Speed: "+this.speed);
        //this.energy -= (int)Equations.EnergyCost(this.direction);
        this.x += this.direction.get(0)*this.speed;
        this.y += this.direction.get(1)*this.speed;
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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
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
                " Speed = " + (getSpeed()-1) + ", Energy = " + getEnergy() + ", Range = " + getDetectRange();
    }

    public double getXdirection() {
        return xdirection;
    }

    public double getYdirection() {
        return ydirection;
    }

    public void setXdirection(double xdirection) {
        this.xdirection = xdirection;
    }

    public void setYdirection(double ydirection) {
        this.ydirection = ydirection;
    }

    public void setMaxWanderDirTimer(int maxWanderDirTimer) {
        this.maxWanderDirTimer = maxWanderDirTimer;
    }

    public int getMaxWanderDirTimer() {
        return maxWanderDirTimer;
    }
}