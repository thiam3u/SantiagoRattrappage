package plateau;


public class Intersection {
    boolean irrigue= false;
    int i;
    int j;

    public Intersection(int i, int j) {
        this.j = j;
        this.i = i;
    }


    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public boolean isirrigue() {
        return irrigue;
    }

    public void setirrigue(boolean irrigue) {
        this.irrigue = irrigue;
    }

    @Override
    public String toString() {
        return "Intersection{" +
                "source=" + irrigue +
                ", i=" + i +
                ", j=" + j +
                '}';
    }
}
