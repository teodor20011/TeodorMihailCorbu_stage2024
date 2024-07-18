package com.teo.springbootrest;

import com.teo.springbootrest.repo.UserRepo;
import com.teo.springbootrest.repo.MessageRepository;

import com.teo.springbootrest.service.MessageService;
import com.teo.springbootrest.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import com.teo.springbootrest.WebConfig;

@SpringBootApplication
public class SpringBootRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRestApplication.class, args);
	}
}
