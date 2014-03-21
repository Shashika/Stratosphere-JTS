package GeoDataTypes;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: shashika
 * Date: 3/21/14
 * Time: 6:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class PactGeometryTest extends PactGeometry {


    public PactGeometryTest(Geometry geo) {
        super(geo);
    }

    public static void main(String[] args) {
        Coordinate[] coordinate = {new Coordinate(0,0),new Coordinate(1,1),new Coordinate(2,0),new Coordinate(0,0)};

        CoordinateSequence coordinateSequence =new CoordinateArraySequence(coordinate);
        GeometryFactory geometryFactory = new GeometryFactory();

        //create Linear Ring - 1 (with coordinateSequence)
        Geometry geometry = new LinearRing(coordinateSequence, geometryFactory);
        PactGeometry pactGeometry = new PactGeometry(geometry);
        System.out.println(pactGeometry.getGeometry());

        //create Linear Ring - 2  (with coordinateArray)
        LinearRing linearRing1 = pactGeometry.geoFactory.createLinearRing(coordinate);
        System.out.println(linearRing1);

        //create Linear Ring - 3  (with coordinateSequence)
        LinearRing linearRing2 = pactGeometry.geoFactory.createLinearRing(coordinateSequence);
        System.out.println(linearRing2);

        //create Point
        Coordinate coordinate1 = new Coordinate(23.54,789.33);
        Point point = pactGeometry.geoFactory.createPoint(coordinate1);
        System.out.println(point);

        //create Multipoint
        MultiPoint multiPoint = pactGeometry.geoFactory.createMultiPoint(coordinate);
        System.out.println(multiPoint);

        //create Polygon
        Polygon polygon = pactGeometry.geoFactory.createPolygon(linearRing1,null);
        System.out.println(polygon);
        System.out.println(polygon.getArea());  //area of the polygon

    }
}
