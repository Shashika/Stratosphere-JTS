package GeoDataTypes;

import eu.stratosphere.types.Value;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: shashika
 * Date: 3/11/14
 * Time: 11:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class GeoCordinate implements Value {

    private double x,y;

    public GeoCordinate(){
         this.x = 0;
         this.y = 0;
    }

    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }

    public double getX(){
        return this.x;
    }

    public double getY(){
        return this.y;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeDouble(this.x);
        out.writeDouble(this.y);
    }

    @Override
    public void read(DataInput in) throws IOException {
        this.x = in.readDouble();
        this.y = in.readDouble();
    }

    @Override
    public String toString() {
        return "GeoCordinate["+this.x+","+this.y+"]";
    }


}
