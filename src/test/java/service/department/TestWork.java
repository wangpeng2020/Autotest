package service.department;

import io.restassured.builder.ResponseBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

import java.util.HashMap;
import java.util.Map;

public class TestWork {
    private static final String corId = "wwa664a53b85c09ea0";
    private static final String corpSecret = "iEOYzbJ48K-7XgCTuj9WdFT6RSOjEgsi2UMiBgZUFmU";
    private static String access_token = null;
    private static final Integer departmentId = 1;

    @BeforeAll
    public static void testGetToken() {
        access_token = given()
                .param("corpid", corId)
                .param("corpsecret",corpSecret)
        .when()
                .get("https://qyapi.weixin.qq.com/cgi-bin/gettoken")
        .then()
                .log().all()
                .body("errcode", equalTo(0))
        .extract()
                .body()
                .path("access_token");
        System.out.println(access_token);
    }

    @Disabled
    @Test
    public void creatDepartment() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name","汪鹏");
        map.put("name_en","wangpeng2020");
        map.put("parentid", departmentId);
        given()
                .queryParam("access_token", access_token)
                .body(map)
                .contentType(ContentType.JSON)
        .when().log().all()
                .post("https://qyapi.weixin.qq.com/cgi-bin/department/create")
        .then().log().all()
                .body("errcode", equalTo(0));
    }

    @Test
    public void getDepartList() {
        given().filter((req, res, ctx) ->{
            req.removeQueryParam("id");
            req.queryParam("id", 2);
            Response oriRes  = ctx.next(req, res);
            System.out.println(oriRes.prettyPeek());
            ResponseBuilder newRes = new ResponseBuilder().clone(oriRes);
            newRes.setBody("{\"code\":0}");
            return newRes.build();
            })
                .queryParam("access_token", access_token)
                .queryParam("id", departmentId)
        .when().log().all()
                .get("https://qyapi.weixin.qq.com/cgi-bin/department/list")
        .then().log().all()
                .body("errcode", equalTo(0));
    }
}
