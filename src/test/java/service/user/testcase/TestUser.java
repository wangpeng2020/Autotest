package service.user.testcase;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import service.user.api.User;
import utils.Template;

public class TestUser {
    User user = new User();
    @Test
    public void info() {
        String userId = "pwtest001_123456";
        user.get(userId).then().body("name", equalTo("wangpeng"));
    }

    @Test
    public void update() {
        String userId = "pwtest_1578106831335";
        String newName = "test_update";
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", newName);
        user.update(userId, data).then().body("errmsg", equalTo("updated"));
        user.get(userId).then().body("name", equalTo(newName));
    }

    @Test
    public void create() {
        String newName = "test";
        String userId = "pwtest_" + System.currentTimeMillis();
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", newName);
        data.put("mobile", String.valueOf(System.currentTimeMillis()).substring(0,11));
        data.put("department", new int[]{1});
        data.put("userid", userId);
        user.create(data).then().body("errcode", equalTo(0));
        user.get(userId).then().body("name", equalTo(newName));
    }

    @Test
    public void deleteUser() throws IOException {
        String userId = user.cloneUser().body().path("userid");
        user.delete(userId).then().body("errcode", equalTo(0));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "TestUser.csv")
    public void delete(String name, String userId) {
        user.delete(userId).then().body("errcode", equalTo(0));
    }

    @ParameterizedTest
    @MethodSource("deleteByParmasFromYaml")
    public void delete(String name, String userId, List<Integer> departs) {
        user.delete(userId).then().body("errcode", equalTo(0));
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
