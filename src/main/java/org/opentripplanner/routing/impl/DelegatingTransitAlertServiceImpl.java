package org.opentripplanner.routing.impl;

import org.opentripplanner.model.FeedScopedId;
import org.opentripplanner.routing.alertpatch.TransitAlert;
import org.opentripplanner.routing.graph.Graph;
import org.opentripplanner.routing.services.TransitAlertService;
import org.opentripplanner.updater.alerts.GtfsRealtimeAlertsUpdater;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class DelegatingTransitAlertServiceImpl implements TransitAlertService {

  private ArrayList<TransitAlertService> transitAlertServices = new ArrayList<>();

  public DelegatingTransitAlertServiceImpl(Graph graph) {
    if (graph.updaterManager != null) {
      graph.updaterManager.getUpdaterList().stream()
          .filter(GtfsRealtimeAlertsUpdater.class::isInstance)
          .map(GtfsRealtimeAlertsUpdater.class::cast)
          .map(GtfsRealtimeAlertsUpdater::getTransitAlertService)
          .forEach(transitAlertServices::add);
    }
  }

  @Override
  public void setAlerts(
      Collection<TransitAlert> alerts
  ) {
    throw new UnsupportedOperationException("Not supported");
  }

  @Override
  public Collection<TransitAlert> getStopAlerts(FeedScopedId stop) {
    return transitAlertServices
        .stream()
        .map(transitAlertService -> transitAlertService.getStopAlerts(stop))
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  @Override
  public Collection<TransitAlert> getRouteAlerts(FeedScopedId route) {
    return transitAlertServices
        .stream()
        .map(transitAlertService -> transitAlertService.getRouteAlerts(route))
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  @Override
  public Collection<TransitAlert> getTripAlerts(FeedScopedId trip) {
    return transitAlertServices
        .stream()
        .map(transitAlertService -> transitAlertService.getTripAlerts(trip))
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  @Override
  public Collection<TransitAlert> getAgencyAlerts(FeedScopedId agency) {
    return transitAlertServices
        .stream()
        .map(transitAlertService -> transitAlertService.getAgencyAlerts(agency))
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  @Override
  public Collection<TransitAlert> getStopAndRouteAlerts(
      FeedScopedId stop, FeedScopedId route
  ) {
    return transitAlertServices
        .stream()
        .map(transitAlertService -> transitAlertService.getStopAndRouteAlerts(stop, route))
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  @Override
  public Collection<TransitAlert> getStopAndTripAlerts(
      FeedScopedId stop, FeedScopedId trip
  ) {
    return transitAlertServices
        .stream()
        .map(transitAlertService -> transitAlertService.getStopAndTripAlerts(stop, trip))
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }
}
