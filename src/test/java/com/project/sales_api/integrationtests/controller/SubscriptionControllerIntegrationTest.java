package com.project.sales_api.integrationtests.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.sales_api.config.TestConfigs;
import com.project.sales_api.config.TestSecurityConfig;
import com.project.sales_api.dto.CustomerRequestDTO;
import com.project.sales_api.dto.CustomerResponseDTO;
import com.project.sales_api.dto.SubscriptionRequestDTO;
import com.project.sales_api.dto.SubscriptionResponseDTO;
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

import java.math.BigDecimal;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
public class SubscriptionControllerIntegrationTest extends AbstractIntegrationTest {

    private RequestSpecification specification;
    private ObjectMapper objectMapper;
    private Long customerId;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup(){
        specification = new RequestSpecBuilder()
                .setBasePath("/v1/subscriptions")
                .setPort(port)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    private Long createCustomer() throws Exception{
        String unique = UUID.randomUUID().toString();

        CustomerRequestDTO request = new CustomerRequestDTO(
                "João",
                "joao" + unique + "@joao.com",
                unique.substring(0, 11)
        );

        CustomerResponseDTO response = given()
                    .spec(new RequestSpecBuilder()
                            .setBasePath("/v1/customers")
                            .setPort(port)
                            .build())
                    .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .body(objectMapper.writeValueAsString(request))
                .when()
                    .post()
                .then()
                .statusCode(201)
                .extract()
                .as(CustomerResponseDTO.class);

        return response.id();
    }

    private SubscriptionResponseDTO createSubscription(Long customerId) throws Exception{
        SubscriptionRequestDTO request = new SubscriptionRequestDTO(
                "Curso de Programação",
                BigDecimal.valueOf(250.00),
                customerId);

        return given()
                    .spec(specification)
                    .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .body(objectMapper.writeValueAsString(request))
                .when()
                    .post()
                .then()
                    .statusCode(201)
                    .extract()
                    .as(SubscriptionResponseDTO.class);
    }

    @Test
    void testCreateSubscription() throws Exception{
        customerId = createCustomer();
        SubscriptionResponseDTO response = createSubscription(customerId);

        assertNotNull(response);
        assertNotNull(response.id());
        assertEquals("Curso de Programação", response.planName());
    }

    @Test
    void testFindSubscriptionById() throws Exception{
        customerId = createCustomer();
        SubscriptionResponseDTO createdSubscription = createSubscription(customerId);

        var response = given()
                    .spec(specification)
                    .pathParam("subscriptionId", createdSubscription.id())
                .when()
                    .get("/{subscriptionId}")
                .then()
                    .statusCode(200)
                    .extract()
                    .as(SubscriptionResponseDTO.class);

        assertNotNull(response);
        assertEquals(createdSubscription.id(), response.id());
    }

    @Test
    void testFindAllSubscriptions() throws Exception{
        customerId = createCustomer();
        SubscriptionResponseDTO createdSubscription = createSubscription(customerId);

        var response = given()
                    .spec(specification)
                .when()
                    .get()
                .then()
                .statusCode(200)
                .extract()
                .as(SubscriptionResponseDTO[].class);

        assertNotNull(response);
        assertTrue(response.length >= 1);
    }

    @Test
    void testUpdatingSubscriptionById() throws Exception{
        customerId = createCustomer();
        SubscriptionResponseDTO created = createSubscription(customerId);

        SubscriptionRequestDTO updateInfo = new SubscriptionRequestDTO("Curso Novo", BigDecimal.valueOf(300.00), customerId);

        var response = given()
                    .spec(specification)
                    .pathParam("subscriptionId", created.id())
                    .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .body(objectMapper.writeValueAsString(updateInfo))
                .when()
                    .put("/{subscriptionId}")
                .then()
                    .statusCode(200)
                    .extract()
                    .as(SubscriptionResponseDTO.class);

        assertNotNull(response);
        assertEquals("Curso Novo", response.planName());
    }

    @Test
    void testDeletingSubscriptionById() throws Exception{
        customerId = createCustomer();
        SubscriptionResponseDTO created = createSubscription(customerId);

        given()
                    .spec(specification)
                    .pathParam("subscriptionId", created.id())
                .when()
                    .delete("/{subscriptionId}")
                .then()
                    .statusCode(204);
    }

}
