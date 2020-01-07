package service.tag.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import service.Work;

public class Tag {

    public Response list() {
        return given()
                .queryParam("access_token", Work.getInstance().getToken())
        .when().log().all()
                .get("https://qyapi.weixin.qq.com/cgi-bin/tag/list")
        .then().log().all()
                .extract().response();
    }

    public Response create(String tagName) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tagname", tagName);
        return given()
                .queryParam("access_token", Work.getInstance().getToken())
                .body(map)
                .contentType(ContentType.JSON)
        .when().log().all()
                .post("https://qyapi.weixin.qq.com/cgi-bin/tag/create")
        .then().log().all()
                .extract().response();
    }

    public Response delete(int tagId) {
        return given()
                .queryParam("access_token", Work.getInstance().getToken())
                .queryParam("tagid", tagId)
        .when().log().all()
                .get("https://qyapi.weixin.qq.com/cgi-bin/tag/delete")
        .then().log().all()
                .extract().response();
    }
}
