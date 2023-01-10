package fox.mc.backfill.mediaconvert.application;

import feign.Response;
import fox.mc.backfill.mediaconvert.config.ConfigProperties;
import fox.mc.backfill.mediaconvert.service.MediaConvertService;
import fox.mc.backfill.mediaconvert.service.VidispineService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Slf4j
@Service
public class Main {
    @Autowired
    private ConfigProperties configProperties;
    @Autowired
    private MediaConvertService mediaConvertService;
    @Autowired
    private VidispineService vidispineService;

    public void execute() throws IOException {
        log.info("Running with the next parameters: " + configProperties.toString());

        FileInputStream file = new FileInputStream(configProperties.getLocation());
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(configProperties.getSheetNumber());

        Map<String, String> itemList = new HashMap();

        for (int i = configProperties.getStartInRowNumber(); i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Cell cell = row.getCell(configProperties.getColumnNumber());
            String fileName = cell.getStringCellValue();

            String itemId = vidispineService.retrieveItemByFileName(fileName);

            itemList.put(itemId, fileName);
        }

        // split shapes requested in a list of string
        List<String> shapes = Arrays.asList(configProperties.getShapeListGenerate().split(","));

        // remove shapes
        for (String itemId : itemList.keySet()) {
            for (String shape: shapes) {
                vidispineService.removeShapeByTag(itemId, shape);
            }
        }

        // create shapes
        Map<String, String> itemFailedMap = new HashMap<>();
        for (String itemId : itemList.keySet()) {
            for (String shape: shapes) {
                Response response = mediaConvertService.sendMediaConvertRequest(itemId, shape, "original");
                if (response.status() != 200) {
                    itemFailedMap.put(itemId, shape);
                }
            }
        }

        log.info("DONE");

        if (!itemFailedMap.isEmpty()) {
            log.error("Please try again generate shape for next items: ");
            for (String item : itemFailedMap.keySet()) {
                log.error("item: " + item + " shape: " + itemFailedMap.get(item));
            }
        }

    }
}