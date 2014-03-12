package Examples;


import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;


public class GeometryExample {


    public void createPoint(){
        GeometryFactory gf = new GeometryFactory();

        Coordinate coord = new Coordinate( 1, 1 );
        Point point = gf.createPoint( coord );


    }

    public void wktWriter(){
        GeometryFactory gf = new GeometryFactory();

        Coordinate coord = new Coordinate( 1, 1 );
        Point point = gf.createPoint( coord );

        StringWriter writer = new StringWriter();
        WKTWriter wktWriter = new WKTWriter(2);

        try {
            wktWriter.write( point, writer );
        } catch (IOException e) {
        }

        String wkt = writer.toString();

        System.out.println( wkt );
    }
    public void parseWKT(){
        GeometryFactory geometryFactory = new GeometryFactory();

        WKTReader reader = new WKTReader( geometryFactory );
        Point point = null;
        try {
            point = (Point) reader.read("POINT (1 1)");
        } catch (ParseException e) {
        }
        System.out.println( point );
    }

    /**
     * @param args
     */
    public static void main( String[] args ) {
        List<String> examples = new ArrayList<String>();
        if( args.length == 0 || "-all".equalsIgnoreCase(args[0]) ){
            for( Method method : getExamples() ){
                examples.add(method.getName());
            }
        }
        else if( "-help".equalsIgnoreCase(args[0]) ){
            System.out.println("Usage: java org.geotools.demo.jts.Examples.GeometryExample <example>");
            System.out.println("Where <example> is one of the following:");
            System.out.println("-help");
            System.out.println("-all");
            for( Method method : getExamples()){
                System.out.println( method.getName() );
            }
            System.exit(0);
        }
        else {
            for( String arg : args ){
                examples.add( arg );
            }
        }
        GeometryExample example = new GeometryExample();
        for( Method method : getExamples() ){
            if( !examples.contains( method.getName() )){
                continue;
            }
            System.out.println( "Running example "+method.getName() );
            try {
                method.invoke( example, new Object[0] );
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }

    }

    public static List<Method> getExamples(){
        List<Method> examples = new ArrayList<Method>();
        for( Method method : GeometryExample.class.getDeclaredMethods() ){
            if( !Modifier.isPublic( method.getModifiers() ) ) {
                continue;
            }
            if( method.getReturnType() != Void.TYPE ){
                continue;
            }
            if( method.getParameterAnnotations().length != 0){
                continue;
            }
            examples.add( method );
        }
        return examples;
    }

}
