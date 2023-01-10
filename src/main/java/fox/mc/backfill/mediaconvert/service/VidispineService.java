package fox.mc.backfill.mediaconvert.service;

public interface VidispineService {
    String getToken();
    String getBasicAuthHeader();
    void removeShapeByTag(String itemId, String shapeTag);
    String retrieveItemByFileName(String fileName);
}
