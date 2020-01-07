package service.user.api;

import framework.BaseApi;
import io.restassured.response.Response;
import service.Work;
import utils.Template;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class UserApi extends BaseApi {

    public Response get(String userId) {
        HashMap<String, Object> params=new HashMap<>();
        params.put("userId", userId);
        setParams(params);
        return parseSteps();
    }

    public Response delete(String userId) {
        HashMap<String, Object> params=new HashMap<>();
        params.put("userId", userId);
        setParams(params);
        return parseSteps();
    }

    public Response create(String postBody) {
        HashMap<String, Object> params=new HashMap<>();
        params.put("postBody", postBody);
        setParams(params);
        return parseSteps();
    }

    public Response create(String name, String mobile, String userId) throws IOException {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("name", name);
        data.put("mobile", mobile);
        data.put("userid", userId);
        String body = new Template().template("/service/user/api/user.json", data);
        return create(body);
    }
}
