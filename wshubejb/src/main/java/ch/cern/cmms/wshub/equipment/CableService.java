package ch.cern.cmms.wshub.equipment;

public class CableService {
    /*public List<Cable> getCables(
            List<GridRequestFilter> gridRequestFilters,
            String username
    ) {
        EAMContext eamContext = eamContextI.getUserContext(username);
        try {
            GridRequest gridRequest = new GridRequest(CABLES_GRID_NAME);

            gridRequest.setGridRequestFilters(gridRequestFilters);
            GridRequestResult queryResult = gridsService.executeQuery(eamContext, gridRequest);

            List<Cable> cablesList = GridTools.convertGridResultToObject(Cable.class, null, queryResult);
            return cablesList;
        } catch (EAMException e) {
            log.error("Error fetching equipment", e);
            throw new NotFoundException(e.getMessage());
        }
    }*/
}
