package service.department.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.util.HashMap;
import java.util.Map;

import service.Work;

public class Department {
    public final Integer parentDepartmentId = 1;

    public Response create(String departmentName, int parentDepartmentId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", departmentName);
        map.put("parentid", parentDepartmentId);
        return given()
                .queryParam("access_token", Work.getInstance().getToken())
                .body(map)
                .contentType(ContentType.JSON)
        .when().log().all()
                .post("https://qyapi.weixin.qq.com/cgi-bin/department/create")
        .then().log().all()
                .body("errcode", equalTo(0))
                .extract().response();
    }

    public Response create(String departmentName) {
        return create(departmentName, parentDepartmentId);
    }

    public Response list(int departmentId) {
        return given()
                .queryParam("access_token", Work.getInstance().getToken())
                .queryParam("id", departmentId)
        .when().log().all()
                .get("https://qyapi.weixin.qq.com/cgi-bin/department/list");
    }

    public Response delete(int departmentId) {
        return given()
                .queryParam("access_token", Work.getInstance().getToken())
                .queryParam("id", departmentId)
        .when().log().all()
                .get("https://qyapi.weixin.qq.com/cgi-bin/department/delete");
    }
}
