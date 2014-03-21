package GeoDataTypes;

import com.vividsolutions.jts.geom.*;
import eu.stratosphere.types.Value;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;


/**
 * This is a native Pact Type for Geometry objects of the JTS framework.
 * Objects are natively written and read from PACT data streams.
 *
 * The serialization and deserialization code was copied from com.vividsolutions.jts.io.WKBWriter and
 * com.vividsolutions.jts.io.WKBReader and slightly adapted.
 *
 * @author fhueske
 *
 */


public class PactGeometry implements Value {

    private static enum GeoType {
        point, lineString, polygon, multiPoint, multiLineString, multiPolygon, geoCollection
    };

    protected final GeometryFactory geoFactory = new GeometryFactory();
    private final int numDimensions = 2;
    private Geometry geo;

    public PactGeometry(Geometry geo) {
        this.geo = geo;
    }

    public Geometry getGeometry() {
        return this.geo;
    }

    public void write(DataOutput out) throws IOException {
        writeGeometry(out, this.geo);
    }

    public void read(DataInput in) throws IOException {
        this.geo = readGeometry(in);
    }

    private Geometry readGeometry(DataInput in) throws IOException {

        int typeOrd = in.readInt();

        if (typeOrd == GeoType.point.ordinal()) {
            return readPoint(in);

        } else if (typeOrd == GeoType.lineString.ordinal()) {
            return readLineString(in);

        } else if (typeOrd == GeoType.polygon.ordinal()) {
            return readPolygon(in);

        } else if (typeOrd == GeoType.multiPoint.ordinal()) {
            return readMultiPoint(in);

        } else if (typeOrd == GeoType.multiLineString.ordinal()) {
            return readMultiLineString(in);

        } else if (typeOrd == GeoType.multiPolygon.ordinal()) {
            return readMultiPolygon(in);

        } else if (typeOrd == GeoType.geoCollection.ordinal()) {
            return readGeoCollection(in);

        } else {
            throw new RuntimeException("Invalid Geometry Type");
        }
    }


    private Point readPoint(DataInput in) throws IOException {

        CoordinateSequence coordSeq = readCoordinate(in);
        return new Point(coordSeq, this.geoFactory);
    }


    private LineString readLineString(DataInput in) throws IOException {

        CoordinateSequence coordSeq = readCoordinateSequence(in);

        return new LineString(coordSeq, this.geoFactory);
    }


    private Polygon readPolygon(DataInput in) throws IOException {

        int numRings = in.readInt();

        LinearRing[] holes = null;

        if (numRings > 1)
            holes = new LinearRing[numRings - 1];

        LinearRing shell = readLinearRing(in);

        for (int i = 0; i < numRings - 1; i++) {
            holes[i] = readLinearRing(in);
        }

        return new Polygon(shell, holes, this.geoFactory);
    }



    private MultiPoint readMultiPoint(DataInput in) throws IOException {

        int numGeom = in.readInt();
        Point[] geoms = new Point[numGeom];

        for (int i = 0; i < numGeom; i++) {

            Geometry g = readGeometry(in);

            if (! (g instanceof Point))

                throw new RuntimeException("Invalid Geometry Type in MultiPoint");

            geoms[i] = (Point) g;
        }
        return this.geoFactory.createMultiPoint(geoms);
    }


    private MultiLineString readMultiLineString(DataInput in) throws IOException {

        int numGeom = in.readInt();

        LineString[] geoms = new LineString[numGeom];

        for (int i = 0; i < numGeom; i++) {
            Geometry g = readGeometry(in);

            if (! (g instanceof LineString))

                throw new RuntimeException("Invalid Geometry Type in MultiLineString");

            geoms[i] = (LineString) g;

        }

        return this.geoFactory.createMultiLineString(geoms);
    }



    private MultiPolygon readMultiPolygon(DataInput in) throws IOException {

        int numGeom = in.readInt();

        Polygon[] geoms = new Polygon[numGeom];

        for (int i = 0; i < numGeom; i++) {
            Geometry g = readGeometry(in);

            if (! (g instanceof Polygon))

                throw new RuntimeException("Invalid Geometry Type in MultiPolygon");

            geoms[i] = (Polygon) g;

        }
        return this.geoFactory.createMultiPolygon(geoms);
    }


    private GeometryCollection readGeoCollection(DataInput in) throws IOException {

        int numGeom = in.readInt();
        Geometry[] geoms = new Geometry[numGeom];

        for (int i = 0; i < numGeom; i++) {

            geoms[i] = readGeometry(in);
        }

        return this.geoFactory.createGeometryCollection(geoms);
    }


    private CoordinateSequence readCoordinate(DataInput in) throws IOException {
        return readCoordinateSequence(in, 1);
    }

    private CoordinateSequence readCoordinateSequence(DataInput in) throws IOException {

        int numCoords = in.readInt();

        return readCoordinateSequence(in, numCoords);
    }

    private CoordinateSequence readCoordinateSequence(DataInput in,int numCoords) throws IOException {

        CoordinateSequence seq = geoFactory.getCoordinateSequenceFactory().create(numCoords, numDimensions);

        for (int i = 0; i < numCoords; i++) {
            // read coordinate
            for (int j = 0; j < numDimensions; j++) {
                // read ordinate
                double ordinate;

                if (j <= 1) {

                    ordinate = geoFactory.getPrecisionModel().makePrecise(
                    in.readDouble());
                } else {
                    ordinate = in.readDouble();
                }

                // set ordinate in sequence

                seq.setOrdinate(i, j, ordinate);
            }
        }
        return seq;
    }



    private LinearRing readLinearRing(DataInput in) throws IOException {

        int size = in.readInt();

        CoordinateSequence pts = readCoordinateSequenceRing(in, size);
        return this.geoFactory.createLinearRing(pts);
    }



    private CoordinateSequence readCoordinateSequenceRing(DataInput in, int size) throws IOException {

        CoordinateSequence seq = readCoordinateSequence(in, size);
        return seq;
    }



    private void writeGeometry(DataOutput out, Geometry geo) throws IOException {

        if (geo instanceof Point)
            writePoint(out, (Point) geo);

        else if (geo instanceof LineString)
            writeLineString(out, (LineString) geo);

        else if (geo instanceof Polygon)
            writePolygon(out, (Polygon) geo);

        else if (geo instanceof MultiPoint)
            writeGeometryCollection(out, GeoType.multiPoint, (MultiPoint) geo);

        else if (geo instanceof MultiLineString)
            writeGeometryCollection(out, GeoType.multiLineString,(MultiLineString) geo);

        else if (geo instanceof MultiPolygon)
            writeGeometryCollection(out, GeoType.multiPolygon,(MultiPolygon) geo);

        else if (geo instanceof GeometryCollection)
            writeGeometryCollection(out, GeoType.geoCollection,(GeometryCollection) geo);
    }


    private void writePoint(DataOutput out, Point p) throws IOException {

        out.writeInt(GeoType.point.ordinal());
        writeCoordinate(out, p.getCoordinate());
    }

    private void writeLineString(DataOutput out, LineString lineString) throws IOException {

        out.writeInt(GeoType.lineString.ordinal());
        writeCoordinateSequence(out, lineString.getCoordinateSequence());
    }

    private void writePolygon(DataOutput out, Polygon polygon) throws IOException {

        out.writeInt(GeoType.polygon.ordinal());
        out.writeInt(polygon.getNumInteriorRing() + 1);
        writeCoordinateSequence(out, polygon.getExteriorRing().getCoordinateSequence());

        for (int i = 0; i < polygon.getNumInteriorRing(); i++) {
            writeCoordinateSequence(out, polygon.getInteriorRingN(i) .getCoordinateSequence());

        }
    }

    private void writeGeometryCollection(DataOutput out, GeoType type, GeometryCollection geoCollection) throws IOException {
        out.writeInt(type.ordinal());
        out.writeInt(geoCollection.getNumGeometries());

        for (int i = 0; i < geoCollection.getNumGeometries(); i++) {
            writeGeometry(out, geoCollection.getGeometryN(i));
        }
    }

    private void writeCoordinate(DataOutput out, Coordinate coord) throws IOException {

        out.writeDouble(coord.x);
        out.writeDouble(coord.y);

    }

    private void writeCoordinateSequence(DataOutput out,CoordinateSequence coordSeq) throws IOException {
        int seqLength = coordSeq.size();

        out.writeInt(seqLength);

        for (int i = 0; i < seqLength; i++) {
            out.writeDouble(coordSeq.getX(i));
            out.writeDouble(coordSeq.getY(i));
        }
    }

}