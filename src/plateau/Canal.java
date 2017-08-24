package plateau;

import java.util.ArrayList;


public class Canal {

    boolean irrigue=false;
    int xdeb;
    int ydeb;
    int xfin;
    int yfin;


    public int getXdeb() {
        return xdeb;
    }

    public int getYdeb() {
        return ydeb;
    }

    public int getYfin() {
        return yfin;
    }

    public int getXfin() {
        return xfin;
    }

    public Canal(boolean irrigue, int xdeb, int ydeb, int xfin, int yfin) {
        this.irrigue = irrigue;
        this.xdeb = xdeb;
        this.ydeb = ydeb;
        this.xfin = xfin;
        this.yfin = yfin;
    }

    public boolean isIrrigue() {
        return irrigue;
    }

    public void setIrrigue(boolean irrigue) {
        this.irrigue = irrigue;
    }


    @Override
    public String toString() {
        return "Canal{" +
                "irrigue=" + irrigue +
                ", debut = (" + xdeb +
                "," + ydeb +
                "), fin = (" + xfin +
                "," + yfin +
                ")}";
    }

    //vérifie que le canal est en position horizontale
    public boolean estHorizontale(){
        return (this.ydeb==this.yfin);
    }
    //vérifie que le canal est en position verticale
    public boolean estVerticale(){
        return (this.xdeb==this.xfin);
    }



}
