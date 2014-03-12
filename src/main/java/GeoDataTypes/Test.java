package GeoDataTypes;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * Created with IntelliJ IDEA.
 * User: shashika
 * Date: 3/11/14
 * Time: 11:47 PM
 * To change this template use File | Settings | File Templates.
 */

class GeoOperations{

    public void geoOp(){


        GeometryFactory geometryFactory = new GeometryFactory();

        GeoCordinate geoCordinate =  new GeoCordinate();
        geoCordinate.setX(35.265);
        geoCordinate.setY(23.221);

        System.out.println(geoCordinate.toString());    //GeoCordinate[35.265,23.221]

        Coordinate coord = new Coordinate(geoCordinate.getX(), geoCordinate.getY());
        Point point = geometryFactory.createPoint(coord);

        System.out.println(point);                      //POINT (35.265 23.221)
    }
}

public class Test {

    public static void main(String[] args) {
        GeoOperations geoOperations =  new GeoOperations();
        geoOperations.geoOp();
    }
}
