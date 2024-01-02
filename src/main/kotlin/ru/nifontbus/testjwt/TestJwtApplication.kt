package ru.nifontbus.testjwt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

// https://github.com/Example-uPagge/jwt-server-spring

@SpringBootApplication
class TestJwtApplication

fun main(args: Array<String>) {
	runApplication<TestJwtApplication>(*args)
}
