package fox.mc.backfill.mediaconvert.service;

import feign.FeignException;
import feign.Response;
import fox.mc.backfill.mediaconvert.client.MediaConvertClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class MediaConvertServiceImpl implements MediaConvertService {
    @Autowired
    private VidispineService vidispineService;
    @Autowired
    private MediaConvertClient mediaConvertClient;

    @Override
    public Response sendMediaConvertRequest(String itemId, String tag, String sourceTag) {
        Response response;
        try {
            response = mediaConvertClient.transcodeItem(vidispineService.getToken(), itemId, null, tag, sourceTag);
        } catch (FeignException e) {
            response = Response.builder().request(e.request()).status(e.status()).body(e.contentUTF8().getBytes()).build();
        }

        if (response.status() != 200) {
            log.error("Please try again next item: " + itemId + ", shape: " + tag);
        }

        log.info("MediaConvert response status: " + response.status() + " - " + response.body());
        return response;
    }
}
