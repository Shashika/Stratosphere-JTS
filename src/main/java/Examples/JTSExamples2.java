package Examples;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.*;
/**
 * Created with IntelliJ IDEA.
 * User: shashika
 * Date: 3/19/14
 * Time: 7:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class JTSExamples2 {

    public void doJTSOperations(){

         Coordinate[] coordinates =
               new Coordinate[] {new Coordinate(4, 0), new Coordinate(2, 2),
                       new Coordinate(4,4), new Coordinate(6, 2), new Coordinate(4, 0) };

       GeometryFactory geometryFactory = new GeometryFactory();

       LinearRing linearRing = geometryFactory.createLinearRing(coordinates);
       Polygon polygon = geometryFactory.createPolygon(linearRing,null);
       System.out.println(polygon.getArea());          //Area of the polygon

    }

    public static void main(String[] args) {
        JTSExamples2 jtsExamples2 = new JTSExamples2();
        jtsExamples2.doJTSOperations();
    }
}
