package service.user.api;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import service.Work;
import utils.Template;

public class User {

    public Response get(String userId) {
        return given()
                .queryParam("access_token", Work.getInstance().getToken())
                .queryParam("userid", userId)
                .when().log().all()
                .get("https://qyapi.weixin.qq.com/cgi-bin/user/get")
                .then().log().all()
                .extract().response();
    }

    public Response update(String userId, Map<String, Object> data) {
        data.put("userid", userId);
        return given()
                .queryParam("access_token", Work.getInstance().getToken())
                .body(data)
                .when().log().all()
                .post("https://qyapi.weixin.qq.com/cgi-bin/user/update")
                .then().log().all()
                .extract().response();
    }

    public Response create(Map<String, Object> data) {
        return given()
                .queryParam("access_token", Work.getInstance().getToken())
                .body(data)
                .when().log().all()
                .post("https://qyapi.weixin.qq.com/cgi-bin/user/create")
                .then().log().all()
                .extract().response();
    }

    public Response cloneUser() throws IOException {
            HashMap<String, Object> data = new HashMap<String, Object>();
            data.put("name", System.currentTimeMillis());
            data.put("mobile", String.valueOf(System.currentTimeMillis()).substring(0,11));
            data.put("userid", System.currentTimeMillis());
            String body = new Template().template("/service/user/api/user.json", data);
            return create(body);
    }

    public Response create(String body) {
        return given()
                .queryParam("access_token", Work.getInstance().getToken())
                .body(body)
                .when().log().all()
                .post("https://qyapi.weixin.qq.com/cgi-bin/user/create")
                .then().log().all()
                .extract().response();
    }

    public Response delete(String userId) {
        return given()
                .queryParam("access_token", Work.getInstance().getToken())
                .queryParam("userid", userId)
                .when().log().all()
                .post("https://qyapi.weixin.qq.com/cgi-bin/user/delete")
                .then().log().all()
                .extract().response();
    }
}
