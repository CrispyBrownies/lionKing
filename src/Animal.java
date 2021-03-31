
//Created: 3/11/2021
//Main Class for animal properties and methods

import org.lwjgl.opengl.GL11;

import java.nio.channels.ScatteringByteChannel;
import java.util.Vector;

class Animal {
    private String name;
    private float speed;
    private float energy;
    private float detectRange;
    private int age;
    private int movementState;
    private float turnAngle;
    private int turnSteps;

    public Vector<Float> targetDir = new Vector<Float>(2);
    public Vector<Float> targetPos = new Vector<Float>(2);

    private double xdirection;
    private double ydirection;

    private Vector<Float> direction;
    private int wanderDirTimer; //How long until animal picks new direction during wander
    private int maxWanderDirTimer;
    private final int MAXATTENTIONSPAN = 500;
    private int attentionSpan = 500; //How long until animal gives up on target seeking and enter wander

    //position coordinates
    private float x;
    private float y;

    //private int environmentSize;

    public Animal() {
    }

    //Calculates if next move will be outside the map
    public boolean CheckCollision(int mapSize) {
        float nextX = this.x + this.direction.get(0) * 1 * this.speed;
        float nextY = this.y + this.direction.get(1) * 1 * this.speed;

        return nextX > 2 * mapSize || nextX < 0 || nextY > 2 * mapSize || nextY < 0;
    }

    //Sets the target direction towards object
    public void SetTargetDir(Object object) {
        float mag;
        if (object instanceof Zebra) {
            mag = Equations.EuclDist(this.x,this.y,((Zebra) object).getX(),((Zebra) object).getY());
            this.targetDir = toVector((((Zebra) object).getX()-this.x)/mag,(((Zebra) object).getY()-this.y)/mag);
        }
        else if (object instanceof Lion) {
            mag = Equations.EuclDist(this.x,this.y,((Lion) object).getX(),((Lion) object).getY());
            this.targetDir = toVector((this.x-((Lion) object).getX())/mag,(this.y-((Lion) object).getY())/mag);
        }
        else if (object instanceof Plant) {
            mag = Equations.EuclDist(this.x,this.y,((Plant) object).getX(),((Plant) object).getY());
            this.targetDir = toVector((((Plant) object).getX()-this.x)/mag,(((Plant) object).getY()-this.y)/mag);
        }
        else {
            mag = Equations.EuclDist(this.x,this.y,((Vector<Float>) object).get(0),((Vector<Float>) object).get(1));
            this.targetDir = toVector((((Vector<Float>) object).get(0)-this.x)/mag,(((Vector<Float>) object).get(1)-this.y)/mag);
        }
    }

    //Sets animal's direction to new random direction
    public void PickNewDir() {
        double newAngle = Math.random() * 2 * Math.PI;
        Vector<Float> moveDir = new Vector<Float>();
        moveDir.add((float) Math.cos(newAngle));
        moveDir.add((float) Math.sin(newAngle));
        this.targetDir = moveDir;
        //return moveDir;
    }

    //Call every time step during wander phase
    public void Wander(int mapSize) {
        if (this.getWanderDirTimer() == 0) {
            this.setWanderDirTimer(this.getMaxWanderDirTimer());
            this.PickNewDir();
            //this.SmoothTurn(this.PickNewDir());
            //this.movementState = 1;
        }
        else {
            this.setWanderDirTimer(this.getWanderDirTimer()-1);
        }
        float nextX = this.getX()+this.direction.get(0)*this.getSpeed()*100f;
        float nextY = this.getY()+this.direction.get(1)*this.getSpeed()*100f;
        this.setTargetPos(toVector(nextX,nextY));
        //Advance(mapSize);
    }

    public void Turn(int dir) {

        Vector<Float> turnDir = this.direction;
        if ((Equations.Truncate(this.direction.get(0),2) != Equations.Truncate(this.targetDir.get(0),2)) && (Equations.Truncate(this.direction.get(1),3) != Equations.Truncate(this.targetDir.get(1),3))) {
            float angleBtwn = Equations.AngleBTVector(this.direction, this.targetDir);
            float currentAngle = Equations.AngleBTVector(toVector(1,0),this.direction);
            float scale = 1;

//            System.out.println("Direction: "+this.direction);
//            System.out.println("Position: "+this.x+" "+this.y);
//            System.out.println("Target Dir: "+this.targetDir);
//            System.out.println("Angle Btwn: "+Math.toDegrees(angleBtwn));
//            System.out.println("Current Angle: "+Math.signum(currentAngle)*Math.toDegrees(currentAngle));

            float signedAngle = Math.signum(this.direction.get(1))*currentAngle + Equations.SignCP(this.direction,this.targetDir)*this.speed*scale*angleBtwn;

            if (signedAngle > Math.PI) {
                signedAngle = -(2*(float)Math.PI-signedAngle);
            } else if (signedAngle < -Math.PI) {
                signedAngle = (2*(float)Math.PI+signedAngle);
            }

            System.out.println("Signed Angle: "+Math.toDegrees(signedAngle));
            if (!Float.isNaN(angleBtwn)) {
                switch (dir) {
                    case 0: {
                        turnDir = toVector((float) Math.cos(signedAngle), (float) Math.sin(signedAngle));
                        break;
                    }
                    case 1: {
                        turnDir = toVector((float) (-1 * Math.cos(angleBtwn * this.speed)), (float) (-1 * Math.sin(angleBtwn * this.speed)));
                        break;
                    }
                    default:
                        throw new IllegalStateException("Unexpected value: " + dir);
                }
            }
            else {
                turnDir = this.targetDir;
            }
        }
//        System.out.println("Turn Dir: "+turnDir);
        this.direction = turnDir;
    }

    //Moves the animal forward in whichever direction they want to travel in
    public void Advance(int mapSize) {
//        System.out.println("Direction: "+this.direction);
//        System.out.println("Position: "+this.x+" "+this.y);
//        System.out.println("Target Dir: "+this.targetDir);
//        System.out.println("Speed: "+this.speed);
//        System.out.println("Energy: "+this.energy);
        this.energy -= Equations.EnergyCost(this.speed);

        while (CheckCollision(mapSize)) {
//            System.out.println("OUT");
//            System.out.println("Direction: "+this.direction);
//            System.out.println("Target Dir: "+this.targetDir);
            this.PickNewDir();
            Turn(0);
        }
        this.x += this.direction.get(0) * this.speed;
        this.y += this.direction.get(1) * this.speed;
    }

    public Vector<Float> toVector(float x, float y) {
        Vector<Float> newDirVect = new Vector<Float>();
        newDirVect.add(x);
        newDirVect.add(y);
        return newDirVect;
    }

    public int getMovementState() {
        return movementState;
    }

    public Vector<Float> getTargetPos() {
        return targetPos;
    }

    public int getAge() {
        return age;
    }

    public int getMAXATTENTIONSPAN() {
        return MAXATTENTIONSPAN;
    }

    public Vector<Float> getTargetDir() {
        return targetDir;
    }

    public float getSpeed() {
        return speed;
    }

    public float getDetectRange() {
        return detectRange;
    }

    public float getEnergy() {
        return energy;
    }

    public String getName() {
        return name;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getWanderDirTimer() {
        return wanderDirTimer;
    }

    public int getAttentionSpan() {
        return attentionSpan;
    }

    public Vector<Float> getDirection() {
        return direction;
    }

    public double getXdirection() {
        return xdirection;
    }

    public double getYdirection() {
        return ydirection;
    }

    public int getMaxWanderDirTimer() {
        return maxWanderDirTimer;
    }

//    public int getEnvironmentSize() {
//        return environmentSize;
//    }

    public void setMovementState(int movementState) {
        this.movementState = movementState;
    }

    public void setTargetPos(Vector<Float> targetPos) {
        this.targetPos = targetPos;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setEnergy(float energy) {
        this.energy = energy;
    }

    public void setDirection(Vector<Float> direction) {
        this.direction = direction;
    }

    public void setDetectRange(float detectRange) {
        this.detectRange = detectRange;
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

    public void setAttentionSpan(int attentionSpan) {
        this.attentionSpan = attentionSpan;
    }

    public void setName(String name) {
        this.name = name;
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

//    public void setEnvironmentSize(int environmentSize) {
//        this.environmentSize = environmentSize;
//    }

    public void setTargetDir(Vector<Float> targetDir) {
        this.targetDir = targetDir;
    }


    public String toString() {
        return getName() + ": Position = (" + getX() + ", " + getY() + ")," +
                " Speed = " + (getSpeed()) + ", Energy = " + getEnergy() + ", Range = " + getDetectRange();
    }

}
//    CODE GRAVEYARD
//=================================================================================================

//    //Handles movement of animal, dir = 1: towards, else: away
//    public void Move(float targetX, float targetY, int dir, int mapSize) {
//        Vector<Float> moveDir = new Vector<Float>();
//        float magnitude = Equations.EuclDist(targetX, targetY, this.x, this.y);
//        if (dir == 0) {
//            moveDir.add((this.x - targetX) / magnitude);
//            moveDir.add((this.y - targetY) / magnitude);
//        } else {
//            moveDir.add((targetX - this.x) / magnitude);
//            moveDir.add((targetY - this.y) / magnitude);
//        }
//        this.direction = moveDir;
//        Advance(mapSize);
//    }
//

//    public void MovementController(int mapSize) {
//        switch (this.movementState) {
//            case 0: { //moving
//                Advance(mapSize);
//                break;
//            }
//            case 1: { //turning
//                SmoothTurning();
//                break;
//            }
//        }
//    }

//    public void SmoothTurning() {
//
//        Vector<Float> temp = toVector(1,0);
//        float currentAngle = Equations.AngleBTVector(this.direction,temp);
//
//        if (this.turnSteps != 0) {
//            currentAngle += this.turnAngle;
//            this.direction = toVector(this.x+(float)Math.cos(currentAngle),this.y+(float)Math.sin(currentAngle));
//            this.targetPos = this.direction;
//            this.turnSteps--;
//        }
//        else {
//            this.movementState = 0;
//        }
//    }

//    public void SmoothTurn(Vector<Float> newDir) {
//        this.turnSteps = Math.round(10/this.speed);
//
//        System.out.println("TurnSteps: "+this.turnSteps);
////        Vector<Float> temp = toVector(1,0);
//
//        this.turnAngle = Equations.AngleBTVector(this.direction,newDir)/this.turnSteps;
////        float currentAngle = Equations.AngleBTVector(this.direction,temp);
//
////        while (turnSteps != 0) {
////            currentAngle += turnAngle;
////
////            System.out.println("Current Angle: "+currentAngle);
////            System.out.println("Turn Angle: "+turnAngle);
////            this.direction = toVector((float) Math.cos(currentAngle),(float) Math.sin(currentAngle));
////            //this.targetDir = this.direction;
////            float nextX = this.getX()+this.getDirection().get(0)*this.getSpeed()*100f;
////            float nextY = this.getY()+this.getDirection().get(1)*this.getSpeed()*100f;
////            this.setTargetDir(toVector(nextX,nextY));
////            Graphics.DrawDir(this);
////            try {
////                Thread.sleep(100);
////            }catch (InterruptedException e) {
////                e.printStackTrace();
////            }
////            turnSteps--;
////        }
//
//    }


//    public void newWanderAngle(int a) {
//        double angle = Math.random()*2*Math.PI;
//        setXdirection(Math.sin(angle));
//        setYdirection(Math.cos(angle));
//    }

//    public void move() {
//
//        // walls
//        // left wall
//        int newXPosition = (int) (getSpeed()*getXdirection()) + getX();
//        int newYPosition = (int) (getSpeed()*getYdirection()) + getY();
//        int d;
//
//        if ((getX() == 0 && getXdirection()<0) || (getX()==getEnvironmentSize()-1 && getXdirection()>0)) {
//            setXdirection(-1*getXdirection());
//            setX((int) (getSpeed()*getXdirection()) + getX());
//
//        }
//        if ((getY() == 0 && getYdirection()<0) || (getY()==getEnvironmentSize()-1 && getYdirection()>0)) {
//            setYdirection(-1*getYdirection());
//            setY((int) (getSpeed()*getYdirection()) + getY());
//
//        }
//
//        if (newXPosition < 0 && newYPosition < 0) {
//            setX(0);
//            setY(0);
//            return;
//        } else if (newXPosition > getEnvironmentSize()-1 && newYPosition > getEnvironmentSize()-1) {
//            setX(getEnvironmentSize()-1);
//            setY(getEnvironmentSize()-1);
//            return;
//        } else if (newXPosition < 0) {
//            d = getX();
//            setX(0);
//            setY((int) (d*getYdirection() + getY()));
//            return;
//        } else if (newXPosition > getEnvironmentSize()-1) {
//            d = getEnvironmentSize()-1-getX();
//            setX(getEnvironmentSize()-1);
//            setY((int) (d*getYdirection()) + getY());
//            return;
//        } else if (newYPosition < 0) {
//            d = getY();
//            setY(0);
//            setX((int) (d*getXdirection()) + getX());
//            return;
//        } else if (newYPosition > getEnvironmentSize()-1) {
//            d = getEnvironmentSize()-1-getY();
//            setY(getEnvironmentSize()-1);
//            setX((int) (d*getXdirection()) + getX());
//            return;
//        }
//        setX(newXPosition);
//        setY(newYPosition);
//    }