package com.kns.shipshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.kns.shipshop.file.properties.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({
    FileStorageProperties.class
})
public class ShipShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShipShopApplication.class, args);
	}

}
