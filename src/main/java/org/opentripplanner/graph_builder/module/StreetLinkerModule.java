package org.opentripplanner.graph_builder.module;

import org.opentripplanner.graph_builder.DataImportIssueStore;
import org.opentripplanner.graph_builder.linking.SimpleStreetSplitter;
import org.opentripplanner.graph_builder.services.GraphBuilderModule;
import org.opentripplanner.routing.graph.Graph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * {@link org.opentripplanner.graph_builder.services.GraphBuilderModule} plugin that links various objects
 * in the graph to the street network. It should be run after both the transit network and street network are loaded.
 * It links three things: transit stops, bike rental stations, and park-and-ride lots. Therefore it should be run
 * even when there's no GTFS data present to make bike rental services and parking lots usable.
 */
public class StreetLinkerModule implements GraphBuilderModule {

    private static final Logger LOG = LoggerFactory.getLogger(StreetLinkerModule.class);

    private Boolean addExtraEdgesToAreas = true;

    @Override
    public void buildGraph(
            Graph graph,
            HashMap<Class<?>, Object> extra,
            DataImportIssueStore issueStore
    ) {
        if(graph.hasStreets) {
            LOG.info("Linking transit stops, bike rental stations, bike parking areas, and park-and-rides to graph . . .");
            SimpleStreetSplitter linker = new SimpleStreetSplitter(graph, issueStore);
            linker.setAddExtraEdgesToAreas(this.addExtraEdgesToAreas);
            linker.link();
        }
        //Calculates convex hull of a graph which is shown in routerInfo API point
        graph.calculateConvexHull();
    }

    @Override
    public void checkInputs() {
        //no inputs
    }
}
