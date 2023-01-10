package fox.mc.backfill.mediaconvert.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Slf4j
@Data
@Configuration
@PropertySource(value = "classpath:config.properties")
public class ConfigProperties {
    @Value("${location}")
    private String location;
    @Value("${sheetNumber}")
    private int sheetNumber;
    @Value("${columnNumber}")
    private int columnNumber;
    @Value("${startInRowNumber}")
    private int startInRowNumber;
    @Value("${adminPassword}")
    private String adminPassword;
    @Value("${shapeListGenerate}")
    private String shapeListGenerate;

    @Override
    public String toString() {
        return "Location: " + location + ", SheetNumber: " + sheetNumber + ", ColumnNumber: " + columnNumber
                + ", StartInRowNumber: " + startInRowNumber + ", ShapeListGenerate: " + shapeListGenerate;
    }
}
