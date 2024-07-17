package com.example.ExchangeProject;

import com.example.ExchangeProject.service.DataLoaderService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExchangeProjectApplication implements CommandLineRunner {

	private final DataLoaderService dataLoaderService;

    public ExchangeProjectApplication(DataLoaderService dataLoaderService) {
        this.dataLoaderService = dataLoaderService;
    }

    public static void main(String[] args) {
		SpringApplication.run(ExchangeProjectApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		long startTime = System.currentTimeMillis();

		dataLoaderService.loadDataIfNecessary();

		long endTime = System.currentTimeMillis();
		System.out.println("Parallel processing time: " + (endTime - startTime) + "ms");
	}
}
