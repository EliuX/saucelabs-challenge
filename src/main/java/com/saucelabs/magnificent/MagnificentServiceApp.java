package com.saucelabs.magnificent;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@SpringBootApplication
@Configuration
@EnableScheduling
public class MagnificentServiceApp implements ApplicationRunner {

    private static final String DEFAULT_LOG_FILE_NAME = "report.log";

    public static void main(String[] args) {
        if (args.length >= 0 && Arrays.asList("-h", "help", "?").contains(args[0])) {
            printHelp();
            System.exit(0);
        }
        SpringApplication.run(MagnificentServiceApp.class, args);
    }

    private static void printHelp() {
        System.out.println("Welcome to Magnificient Log Service");
        System.out.println("Please specify the --file option for the log file");
        System.out.println("java -jar magnificient.jar --file=mylogfile.log");
        System.out.printf("Otherwise '%s' will be used%n", DEFAULT_LOG_FILE_NAME);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        String logFileName = Optional.ofNullable(args.getOptionValues("file"))
                .orElseGet(Collections::emptyList).stream()
                .findFirst()
                .orElse(DEFAULT_LOG_FILE_NAME);

        System.setProperty("logging.file", logFileName);
    }
}

