
//Created: 3/11/2021
//Plant class for managing plant properties

class Plant {

    //Plant properties
    private float x;
    private float y;
    private final int FOODVAL = 100;

    public Plant() { }

    public Plant(int x, int y) {
        setX(x);
        setY(y);
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

    public int getFOODVAL() {
        return FOODVAL;
    }
}