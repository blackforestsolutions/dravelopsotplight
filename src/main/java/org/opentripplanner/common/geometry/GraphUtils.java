package org.opentripplanner.common.geometry;

import org.locationtech.jts.algorithm.ConvexHull;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.opentripplanner.routing.graph.Graph;
import org.opentripplanner.routing.graph.Vertex;

import java.util.Collection;

public class GraphUtils {

    public static Geometry makeConvexHull(Graph graph) {
        return new ConvexHull(geometryCollectionFromVertices(graph)).getConvexHull();
    }

    private static GeometryCollection geometryCollectionFromVertices(Graph graph) {
        GeometryFactory gf = GeometryUtils.getGeometryFactory();
        Collection<Vertex> vertices = graph.getVertices();
        Geometry[] points = new Geometry[vertices.size()];
        int i = 0;
        for (Vertex v : vertices) {
            points[i++] = gf.createPoint(v.getCoordinate());
        }

        GeometryCollection geometries = new GeometryCollection(points, gf);
        return geometries;
    }
}
