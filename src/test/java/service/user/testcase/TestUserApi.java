package service.user.testcase;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import service.user.api.UserApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class TestUserApi {
    public UserApi userApi = new UserApi();

    @Test
    public void info() {
        String userId = "test";
        userApi.get(userId);
    }

    @Test
    public void delete() {
        String userId = "test";
        userApi.delete(userId);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "TestUser.csv")
    public void delete(String name, String userId) {
        userApi.delete(userId).then().body("errcode", equalTo(0));
    }

    @ParameterizedTest
    @MethodSource("deleteByParmasFromYaml")
    public void delete(String name, String userId, List<Integer> departs) {
        userApi.delete(userId).then().body("errcode", equalTo(0));
    }

    @Test
    public void create() throws IOException {
        String name = "test_123456";
        String userId = "pwtest_" + System.currentTimeMillis();
        String mobile = String.valueOf(System.currentTimeMillis()).substring(0,11);
        userApi.create(name, mobile, userId).then().body("errcode", equalTo(0));
        userApi.get(userId).then().body(matchesJsonSchemaInClasspath("service/user/testcase/user_schame.json"));
    }

    static Stream<Arguments> deleteByParmasFromYaml() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        TypeReference<List<HashMap<String, Object>>> typeRef = new TypeReference<List<HashMap<String, Object>>>(){};
        List<HashMap<String, Object>> data;
        try {
            data = mapper.readValue(
                    TestUser.class.getResourceAsStream("TestUser.yaml"),
                    typeRef);
            ArrayList<Arguments> results = new ArrayList<>();
            data.forEach(map -> {
                results.add(arguments(
                        map.get("name"),
                        map.get("userId"),
                        map.get("departs")
                ));
            });
            return results.stream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Stream.of();
    }
}
