package com.project.sales_api.integrationtests.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.sales_api.Enums.Roles;
import com.project.sales_api.config.TestConfigs;
import com.project.sales_api.config.TestSecurityConfig;
import com.project.sales_api.dto.UserRequestDTO;
import com.project.sales_api.dto.UserResponseDTO;
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

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
public class UserControllerIntegrationTest extends AbstractIntegrationTest {

        private static RequestSpecification specification;
        private static ObjectMapper objectMapper;
        private static UserRequestDTO userRequestDTO;
        private static UserResponseDTO userResponseDTO;

        @BeforeAll
        public static void setup(){
            specification = new RequestSpecBuilder()
                    .setBasePath("/v1/users")
                    .setPort(TestConfigs.SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                    .build();

            objectMapper = new ObjectMapper();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            userRequestDTO = new UserRequestDTO("João", "joao@joao.com", "joao123", Roles.ADMIN);
        }

        @Test
        @Order(1)
        void testCreatingUser() throws Exception{
            userResponseDTO = given()
                        .spec(specification)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .body(objectMapper.writeValueAsString(userRequestDTO))
                    .when()
                        .post()
                    .then()
                        .statusCode(201)
                        .extract()
                        .as(UserResponseDTO.class);

            assertNotNull(userResponseDTO);
            assertNotNull(userResponseDTO.id());
            assertEquals("João", userResponseDTO.name());
        }

        @Test
        @Order(2)
        void testFindUserById(){
            var response = given()
                        .spec(specification)
                        .pathParam("userId", userResponseDTO.id())
                    .when()
                        .get("/{userId}")
                    .then()
                        .statusCode(200)
                        .extract()
                        .as(UserResponseDTO.class);

            assertNotNull(response);
            assertEquals(userResponseDTO.id(), response.id());
            assertEquals("João", response.name());
        }

        @Test
        @Order(3)
        void testFindAllUsers(){
            var response = given()
                        .spec(specification)
                    .when()
                        .get()
                    .then()
                    .statusCode(200)
                    .extract()
                    .as(UserResponseDTO[].class);

            assertNotNull(response);
            assertTrue(response.length >= 1);
            assertEquals(userResponseDTO.id(), response[0].id());
        }

        @Test
        @Order(4)
        void testUpdatingUserById() throws Exception{
            UserRequestDTO updatedInfo = new UserRequestDTO("João Novo", "updated@email.com", "joao1234", Roles.ADMIN);

            var response = given()
                        .spec(specification)
                        .pathParam("userId", userResponseDTO.id())
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .body(objectMapper.writeValueAsString(updatedInfo))
                    .when()
                        .put("/{userId}")
                    .then()
                        .statusCode(200)
                        .extract()
                        .as(UserResponseDTO.class);

            assertNotNull(response);
            assertEquals("João Novo", response.name());
            assertEquals("updated@email.com", response.email());
        }

        @Test
        @Order(5)
        void deletingUserById(){
            var response = given()
                        .spec(specification)
                        .pathParam("userId", userResponseDTO.id())
                    .when()
                        .delete("/{userId}")
                    .then()
                        .statusCode(204);
        }

}
