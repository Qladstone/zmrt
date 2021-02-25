package com.zendesk.zmrt;

import com.zendesk.zmrt.raildata.StationsWithSchedule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ZmrtApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZmrtApplication.class, args);
		StationsWithSchedule.getInstance();
	}

}
