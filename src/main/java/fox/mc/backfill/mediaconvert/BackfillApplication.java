package fox.mc.backfill.mediaconvert;

import fox.mc.backfill.mediaconvert.application.Main;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.io.IOException;
import java.util.Scanner;

@Slf4j
@SpringBootApplication
@EnableFeignClients
public class BackfillApplication implements CommandLineRunner {

    @Autowired
    private Main main;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BackfillApplication.class);
        application.setWebApplicationType(WebApplicationType.NONE);

        System.out.print("Enter run env supported: ");
        Scanner scanner = new Scanner(System.in);
        String env = scanner.nextLine();

        application.run("--spring.profiles.active=" + env);
    }

    @Override
    public void run(String... args) throws IOException {
        log.info("EXECUTING : command line runner");
        main.execute();
        log.info("APPLICATION FINISHED");
    }
}
