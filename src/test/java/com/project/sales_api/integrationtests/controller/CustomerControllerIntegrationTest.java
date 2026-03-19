package com.project.sales_api.integrationtests.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.sales_api.config.TestConfigs;
import com.project.sales_api.config.TestSecurityConfig;
import com.project.sales_api.dto.CustomerRequestDTO;
import com.project.sales_api.dto.CustomerResponseDTO;
import com.project.sales_api.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
public class CustomerControllerIntegrationTest extends AbstractIntegrationTest {

        private static RequestSpecification specification;
        private static ObjectMapper objectMapper;
        private static CustomerRequestDTO customerRequest;
        private static CustomerResponseDTO customerResponse;

        @BeforeAll
        public static void setup(){
            specification = new RequestSpecBuilder()
                    .setBasePath("/v1/customers")
                    .setPort(TestConfigs.SERVER_PORT)
                        .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                        .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                    .build();

            objectMapper = new ObjectMapper();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            customerRequest = new CustomerRequestDTO(
                    "João", "joao@joao.com", "12345678910"
            );
        }

        @Test
        @Order(1)
        void testCreateCustomer() throws Exception{
            customerResponse = given()
                        .spec(specification)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .body(objectMapper.writeValueAsString(customerRequest))
                    .when()
                        .post()
                    .then()
                        .statusCode(201)
                        .extract()
                        .as(CustomerResponseDTO.class);

            assertNotNull(customerResponse);
            assertNotNull(customerResponse.id());
            assertEquals("João", customerResponse.name());
        }

        @Test
        @Order(2)
        void testFindCustomerById(){
            var response = given()
                        .spec(specification)
                        .pathParam("customerId", customerResponse.id())
                    .when()
                        .get("/{customerId}")
                    .then()
                        .statusCode(200)
                        .extract()
                        .as(CustomerResponseDTO.class);

            assertNotNull(customerResponse);
            assertEquals(customerResponse.id(), response.id());
            assertEquals("João", response.name());
        }

        @Test
        @Order(3)
        void testFindAllCustomer(){
            var response = given()
                        .spec(specification)
                    .when()
                        .get()
                    .then()
                        .statusCode(200)
                        .extract()
                        .as(CustomerResponseDTO[].class);

            assertNotNull(response);
            assertTrue(response.length > 0);
        }

        @Test
        @Order(4)
        void testUpdatingCustomerById() throws Exception{

            CustomerRequestDTO requestUpdating = new CustomerRequestDTO("João Novo", "updated@email.com", "12345678911");

            var response = given()
                        .spec(specification)
                        .pathParam("customerId", customerResponse.id())
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .body(objectMapper.writeValueAsString(requestUpdating))
                    .when()
                        .put("/{customerId}")
                    .then()
                        .statusCode(200)
                        .extract()
                        .as(CustomerResponseDTO.class);

            assertNotNull(response);
            assertEquals("João Novo", response.name());
            assertEquals("updated@email.com", response.email());
        }

        @Test
        @Order(5)
        void testDeletingCustomerById() {
            given()
                    .spec(specification)
                    .pathParam("customerId", customerResponse.id())
                    .when()
                    .delete("/{customerId}")
                    .then()
                    .statusCode(204);
        }
}

