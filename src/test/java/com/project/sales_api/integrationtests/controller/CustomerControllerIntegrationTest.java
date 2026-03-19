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
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
public class CustomerControllerIntegrationTest extends AbstractIntegrationTest {

        private RequestSpecification specification;
        private ObjectMapper objectMapper;

        @LocalServerPort
        private int port;

        @BeforeEach
        public void setup(){
            specification = new RequestSpecBuilder()
                    .setBasePath("/v1/customers")
                    .setPort(port)
                        .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                        .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                    .build();

            objectMapper = new ObjectMapper();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        }

        private String generateCpf(){
            long min = 10000000000L;
            long max = 99999999999L;

            return String.valueOf(ThreadLocalRandom.current().nextLong(min, max));
        }

        private CustomerResponseDTO createCustomer() throws Exception{
            CustomerRequestDTO request = new CustomerRequestDTO("João", "joao" + UUID.randomUUID() + "@joao.com", generateCpf());

            return given()
                        .spec(specification)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .body(objectMapper.writeValueAsString(request))
                    .when()
                        .post()
                    .then()
                        .statusCode(201)
                        .extract()
                        .as(CustomerResponseDTO.class);
        }

        @Test
        void testCreateCustomer() throws Exception{
            var response = createCustomer();

            assertNotNull(response);
            assertNotNull(response.id());
            assertEquals("João", response.name());
        }

        @Test
        void testFindCustomerById() throws Exception {
            var created = createCustomer();

            var response = given()
                        .spec(specification)
                        .pathParam("customerId", created.id())
                    .when()
                        .get("/{customerId}")
                    .then()
                        .statusCode(200)
                        .extract()
                        .as(CustomerResponseDTO.class);


            assertNotNull(response);
            assertEquals(created.id(), response.id());
            assertEquals("João", response.name());
        }

        @Test
        void testFindAllCustomer() throws Exception{
            createCustomer();

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
        void testUpdatingCustomerById() throws Exception{

            var created = createCustomer();

            CustomerRequestDTO requestUpdating = new CustomerRequestDTO("João Novo", "updated@email.com", "12345678911");

            var response = given()
                        .spec(specification)
                        .pathParam("customerId", created.id())
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
        void testDeletingCustomerById() throws Exception {
            var created = createCustomer();
            given()
                    .spec(specification)
                    .pathParam("customerId", created.id())
                    .when()
                    .delete("/{customerId}")
                    .then()
                    .statusCode(204);
        }
}

