
//Created: 3/11/2021
//Plant class for managing plant properties

class Plant {

    //Plant properties
    private float x;
    private float y;
    private final int FOODVAL = 100;
    private int id = 0;
    private boolean targeted;

    public Plant() { }

    public Plant(int x, int y, int id) {
        setId(id);
        setX(x);
        setY(y);
    }


    public boolean isTargeted() {
        return targeted;
    }

    public int getId() {
        return id;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getFOODVAL() {
        return FOODVAL;
    }


    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setTargeted(boolean targeted) {
        this.targeted = targeted;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString() {
        return "Plant{" +
                "x=" + x +
                ", y=" + y +
                ", FOODVAL=" + FOODVAL +
                ", id=" + id +
                '}';
    }
}