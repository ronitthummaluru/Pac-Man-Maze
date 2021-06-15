import java.awt.Polygon;

public class Wall{

	private int num;
	int newWall = 70;

	public Wall(int num){
		this.num= num;
    }

    public Polygon getPoly(){
        Polygon poly = new Polygon();
        if (num == 1){
            poly.addPoint(350, 350);
            poly.addPoint(420, 420);
            poly.addPoint(420, 770);
            poly.addPoint(350, 840);
        }
        else if (num == 2){
            poly.addPoint(420, 420);
            poly.addPoint(490, 490);
            poly.addPoint(490, 700);
            poly.addPoint(420, 770);
        }
        else if (num == 3){
            poly.addPoint(490, 490);
            poly.addPoint(560, 560);
            poly.addPoint(560, 630);
            poly.addPoint(490, 700);
        }
        else if (num == 4){
            poly.addPoint(840, 350);
            poly.addPoint(770, 420);
            poly.addPoint(770, 770);
            poly.addPoint(840, 840);
        }
        else if (num == 5){
            poly.addPoint(770, 420);
            poly.addPoint(700, 490);
            poly.addPoint(700, 700);
            poly.addPoint(770, 770);
        }
        else if (num == 6){
            poly.addPoint(700, 490);
            poly.addPoint(630, 560);
            poly.addPoint(630, 630);
            poly.addPoint(700, 700);
        }
        else if (num == 7){
            poly.addPoint(490, 490);
            poly.addPoint(700, 490);
            poly.addPoint(700, 700);
            poly.addPoint(490, 700);
        }
        else if (num == 8){
            poly.addPoint(420, 420);
            poly.addPoint(770, 420);
            poly.addPoint(770, 770);
            poly.addPoint(420, 770);
        }
        else if (num == 9){
            poly.addPoint(350, 350);
            poly.addPoint(840, 350);
            poly.addPoint(840, 840);
            poly.addPoint(350, 840);
        }

        return poly;
    }

}