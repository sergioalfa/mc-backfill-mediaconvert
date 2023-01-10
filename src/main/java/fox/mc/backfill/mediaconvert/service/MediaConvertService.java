package fox.mc.backfill.mediaconvert.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import feign.Response;

public interface MediaConvertService {
    Response sendMediaConvertRequest(String itemId, String tag, String sourceTag) throws JsonProcessingException;
}
