package fox.mc.backfill.mediaconvert.client;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "media-convert", url = "${mediaconvert.url}")
public interface MediaConvertClient {

    @RequestMapping(method = RequestMethod.POST, value = "/item/{itemId}/transcode", produces = MediaType.APPLICATION_JSON_VALUE)
    Response transcodeItem(@RequestHeader("Authorization") String authorizationHeader,
                           @PathVariable("itemId") String itemId,
                           @RequestParam("outputFilename") String outputFilename,
                           @RequestParam("tag") String tag,
                           @RequestParam("sourceTag") String sourceTag);
}