package fox.mc.backfill.mediaconvert.service;

import fox.mc.backfill.mediaconvert.config.ConfigProperties;
import fox.mc.backfill.mediaconvert.config.VidispineConfiguration;
import fox.mc2.vidispine.model.ContentEnum;
import fox.mc2.vidispine.model.xmlschema.ItemSearchType;
import fox.mc2.vidispine.model.xmlschema.ItemSearchValueType;
import fox.mc2.vidispine.model.xmlschema.SearchFieldType;
import fox.mc2.vidispine.model.xmlschema.SearchResultType;
import fox.mc2.vidispine.requests.*;
import fox.mc2.vidispine.service.VidispineClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class VidispineServiceImpl implements VidispineService {
    @Autowired
    private VidispineConfiguration vidispineConfiguration;
    @Autowired
    private VidispineClient vidispineClient;
    @Autowired
    private ConfigProperties configProperties;

    @Override
    @Cacheable(value = "vidispine_token_cache")
    public String getToken() {
        String user = vidispineConfiguration.getUser();
        String password = configProperties.getAdminPassword();

        String token = vidispineClient.getAuthenticationToken(GetAuthenticationTokenRequest.builder()
                .userId(user).password(password).seconds(360).build());
        return token;
    }

    @Override
    public String getBasicAuthHeader() {
        String user = vidispineConfiguration.getUser();
        String password = configProperties.getAdminPassword();

        Map<String, Header> basicAuthMap = vidispineClient.getBasicAuthHeaderMap(user, password);
        BasicHeader basicHeader = (BasicHeader) basicAuthMap.get("Authorization");
        String basicAuth = basicHeader.getValue();

        return basicAuth;
    }

    @Override
    public void removeShapeByTag(String itemId, String shapeTag) {
        log.info("Processing delete tag: " + shapeTag + " item: " + itemId);
        int responseCode = vidispineClient.deleteShapeByTag(itemId, shapeTag);
        if (responseCode == 200) {
            log.info("Delete response code: " + responseCode + ", Successful shape deleted: " + shapeTag + " itemId: " + itemId);
        } else if (responseCode == 0) {
            log.warn("Delete response code: " + responseCode + ", not existing shape: " + shapeTag + " itemId: " + itemId);
        } else {
            log.warn("Delete response code: " + responseCode + ", please check itemId: " + itemId + " shape: " + shapeTag);
        }
    }

    @Override
    public String retrieveItemByFileName(String fileName) {
        log.info("Searching for item with file name: " + fileName);

        ItemSearchType itemSearchType = new ItemSearchType();
        
        SearchFieldType itemIdField = new SearchFieldType();
        itemIdField.setName("originalFilename");
        itemIdField.getValue().add(new ItemSearchValueType());
        itemIdField.getValue().get(0).setValue(fileName);
        
        itemSearchType.getField().add(itemIdField);

        SearchRequest searchRequest = SearchRequest.builder()
                .contentEnum(ContentEnum.METADATA)
                .terse(true)
                .first(1)
                .number(1)
                .field("originalFilename")
                .authToken(this.getToken())
                .itemSearchType(itemSearchType)
                .build();

        SearchResultType resultType;
        String itemIdFound = "";
        try {
            resultType = vidispineClient.search(searchRequest);
            itemIdFound = resultType.getEntry().get(0).getId();
            log.info("ItemId found: " + itemIdFound + " fileName: " + fileName);
        } catch (Exception e) {
            log.error("ERROR: ItemId not found for file: " + fileName);
        }
        return itemIdFound;
    }
}
