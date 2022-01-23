package com.bananna.ninja;

import com.bananna.ninja.boostrap.NettyBoostrap;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class NinjaApplication implements CommandLineRunner {

	@Resource
	private NettyBoostrap nettyBoostrap;

	public static void main(String[] args) {
		SpringApplication.run(NinjaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		nettyBoostrap.run();
	}
}
