package com.project.sales_api.integrationtests.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
public class UserControllerIntegrationTest extends AbstractIntegrationTest {

        private static RequestSpecification specification;
        private static ObjectMapper objectMapper;

        @LocalServerPort
        private int port;

        @BeforeEach
        public void setup(){
            specification = new RequestSpecBuilder()
                    .setBasePath("/v1/users")
                    .setPort(port)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                    .build();

            objectMapper = new ObjectMapper();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        }

        private UserResponseDTO createUser() throws Exception {
            String unique = UUID.randomUUID().toString();
            UserRequestDTO request = new UserRequestDTO("João", "joao" + UUID.randomUUID() + "@gmail.com", unique.substring(0,8), Roles.ADMIN);

            return given()
                        .spec(specification)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .body(objectMapper.writeValueAsString(request))
                    .when()
                        .post()
                    .then()
                        .statusCode(201)
                        .extract()
                        .as(UserResponseDTO.class);
        }

        @Test
        void testCreatingUser() throws Exception{
            var created = createUser();

            assertNotNull(created);
            assertNotNull(created.id());
            assertEquals("João", created.name());
        }

        @Test
        void testFindUserById() throws Exception{

            var created = createUser();

            var response = given()
                        .spec(specification)
                        .pathParam("userId", created.id())
                    .when()
                        .get("/{userId}")
                    .then()
                        .statusCode(200)
                        .extract()
                        .as(UserResponseDTO.class);

            assertNotNull(response);
            assertEquals(created.id(), response.id());
            assertEquals("João", response.name());
        }

        @Test
        void testFindAllUsers() throws Exception{
            createUser();

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
        }

        @Test
        void testUpdatingUserById() throws Exception{
            var created = createUser();
            UserRequestDTO updatedInfo = new UserRequestDTO("João Novo", "updated@email.com", "joao1234", Roles.ADMIN);

            var response = given()
                        .spec(specification)
                        .pathParam("userId", created.id())
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
        void deletingUserById() throws Exception{
            var created = createUser();
            given()
                        .spec(specification)
                        .pathParam("userId", created.id())
                    .when()
                        .delete("/{userId}")
                    .then()
                        .statusCode(204);
        }

}
