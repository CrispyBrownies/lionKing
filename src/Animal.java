
//Created: 3/11/2021
//Main Class for animal properties and methods

import java.util.Vector;

class Animal {
    private String name;
    private int speed;
    private int energy;
    private float detectRange;

    private double xdirection;
    private double ydirection;

    private Vector<Float> direction;
    private int wanderDirTimer; //How long until animal picks new direction during wander
    private int maxWanderDirTimer;
    private int attentionSpan; //How long until animal gives up on target seeking and enter wander

    //position coordinates
    private int x;
    private int y;

    private int environmentSize;

    public Animal() {

    }

    public void newWanderAngle(int a) {
        double angle = Math.random()*2*Math.PI;
        setXdirection(Math.sin(angle));
        setYdirection(Math.cos(angle));
    }

    public void move() {

        // walls
        // left wall
        int newXPosition = (int) (getSpeed()*getXdirection()) + getX();
        int newYPosition = (int) (getSpeed()*getYdirection()) + getY();
        int d;

        if ((getX() == 0 && getXdirection()<0) || (getX()==getEnvironmentSize()-1 && getXdirection()>0)) {
            setXdirection(-1*getXdirection());
            setX((int) (getSpeed()*getXdirection()) + getX());

        }
        if ((getY() == 0 && getYdirection()<0) || (getY()==getEnvironmentSize()-1 && getYdirection()>0)) {
            setYdirection(-1*getYdirection());
            setY((int) (getSpeed()*getYdirection()) + getY());

        }

        if (newXPosition < 0 && newYPosition < 0) {
            setX(0);
            setY(0);
            return;
        } else if (newXPosition > getEnvironmentSize()-1 && newYPosition > getEnvironmentSize()-1) {
            setX(getEnvironmentSize()-1);
            setY(getEnvironmentSize()-1);
            return;
        } else if (newXPosition < 0) {
            d = getX();
            setX(0);
            setY((int) (d*getYdirection() + getY()));
            return;
        } else if (newXPosition > getEnvironmentSize()-1) {
            d = getEnvironmentSize()-1-getX();
            setX(getEnvironmentSize()-1);
            setY((int) (d*getYdirection()) + getY());
            return;
        } else if (newYPosition < 0) {
            d = getY();
            setY(0);
            setX((int) (d*getXdirection()) + getX());
            return;
        } else if (newYPosition > getEnvironmentSize()-1) {
            d = getEnvironmentSize()-1-getY();
            setY(getEnvironmentSize()-1);
            setX((int) (d*getXdirection()) + getX());
            return;
        }
        setX(newXPosition);
        setY(newYPosition);
    }



    //Sets animal's direction to new random direction
    public void PickNewDir() {
        double newAngle = Math.random()*2*Math.PI;
        this.direction.set(0,getSpeed() * (float)Math.cos(newAngle));
        this.direction.set(1,getSpeed() * (float)Math.sin(newAngle));
    }

    //Handles movement of animal, dir = 1: towards, else: away
    public void Move(float x, float y, int dir) {
        Vector<Float> moveDir = new Vector<Float>();
        if (dir == 0) {
            moveDir.set(0,x-this.x);
            moveDir.set(1,y-this.y);
        }
        else {
            moveDir.set(0,this.x-x);
            moveDir.set(1,this.y-y);
        }
        this.direction = moveDir;
        Advance();
    }

    public void Advance() {
        this.energy -= Equations.EnergyCost(this.direction);
        this.x += this.direction.get(0);
        this.y += this.direction.get(1);
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
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

    public int getX() {
        return x;
    }

    public int getY() {
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

    public int getEnvironmentSize() {
        return environmentSize;
    }

    public void setEnvironmentSize(int environmentSize) {
        this.environmentSize = environmentSize;
    }
}