
//Created: 3/11/2021
//Plant class for managing plant properties

class Plant {

    //Plant properties
    private float x;
    private float y;
    private final float FOODVAL = 10;

    public class Plant(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}