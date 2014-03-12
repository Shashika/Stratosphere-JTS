package Examples;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;


/**
 * Created with IntelliJ IDEA.
 * User: shashika
 * Date: 3/2/14
 * Time: 11:06 PM
 * To change this template use File | Settings | File Templates.
 */


class JTSFunctionalities{

   public void coordinates(){

       try {
           Geometry geometry1 = new WKTReader().read("LINESTRING (0 0, 10 10, 20 20)");
           Geometry geometry2 = new WKTReader().read("LINESTRING (0 20 , 10 10, 20 0)");
           System.out.println(geometry1.toText());
           System.out.println(geometry2.toText());

           Geometry geometry3 = geometry2.intersection(geometry1);
           System.out.println(geometry3.toText());      //POINT (10 10)


       } catch (ParseException e) {
           e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
       }

       Coordinate[] coordinates = new Coordinate[] {
               new Coordinate(0, 0), new Coordinate(10, 10),
               new Coordinate(20,0),new Coordinate(0, 0) };
       int x [] = {12,45,65,-12};
       int y [] = {-59,48,25,45};

       /*Coordinate[] coordinates =
               new Coordinate[] {new Coordinate(4, 0), new Coordinate(2, 2),
                       new Coordinate(4,4), new Coordinate(6, 2), new Coordinate(4, 0) };*/

       GeometryFactory geometryFactory = new GeometryFactory();
       LinearRing linearRing = new GeometryFactory().createLinearRing(coordinates);
       Polygon polygon = new Polygon(linearRing,null,geometryFactory);

       System.out.println(polygon.getArea());
       /*Point point =new Point(new CoordinateSequence(12.01, 54.21) {
       },geometryFactory);
        polygon.intersects();*/

       Coordinate coord = new Coordinate(1, 1);
       Point point = geometryFactory.createPoint(coord);

       System.out.println(point.intersection(polygon));

       /*GeometryFactory geometryFactory = new GeometryFactory();
       LinearRing linearRing = geometryFactory.createLinearRing(coordinates);
       Polygon polygon = geometryFactory.createPolygon(linearRing,null);

       System.out.println(polygon.getArea());*/


   }
}


public class JTSExample {

    public static void main(String[] args) {
        JTSFunctionalities jtsFunctionalities = new JTSFunctionalities();
        jtsFunctionalities.coordinates();
    }
}
