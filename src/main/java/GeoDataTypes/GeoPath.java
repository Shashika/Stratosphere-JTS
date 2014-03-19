package GeoDataTypes;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import eu.stratosphere.types.Value;

import java.io.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: shashika
 * Date: 3/12/14
 * Time: 9:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class GeoPath implements Value {

    private ArrayList<Coordinate> cordinates = null;
    private ByteArrayOutputStream b;

    public GeoPath(){
         cordinates = new ArrayList<Coordinate>();
    }

    public void add(Coordinate coordinate){
         cordinates.add(coordinate);

     }

    public void print(){
        System.out.println(cordinates);
    }

    public void clear() {
        cordinates = null;
    }

    public Coordinate[] getCordinatesArray(){

       return (Coordinate[]) cordinates.toArray();
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(cordinates.size());

        b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);

        for(Coordinate coordinate : cordinates){
              o.writeObject(coordinate);
        }

        out.write(b.toByteArray());
    }

    @Override
    public void read(DataInput in) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
        int cordinatesSize = in.readInt();

        ByteArrayInputStream bi = new ByteArrayInputStream(b.toByteArray());
        ObjectInputStream o = new ObjectInputStream(bi);

        for(Coordinate coordinate : cordinates){
            try {
                coordinate = (Coordinate)o.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


    }

    public String toString(){
        return cordinates.toString();
    }


}

class Test2{
    public static void main(String[] args) {
        GeoPath geoPath = new GeoPath();
        geoPath.add(new Coordinate(12.35,56.23));
        geoPath.add(new Coordinate(33.26,89.65));
        geoPath.add(new Coordinate(65.11,45.36));

        GeometryFactory geometryFactory = new GeometryFactory();

        LineString lineString = geometryFactory.createLineString(geoPath.getCordinatesArray());
        System.out.println(lineString);

        geoPath.print();
    }

}


