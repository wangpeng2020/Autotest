package framework;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;

import java.util.HashMap;
import java.util.Map;

import service.Work;

public class ApiObjectMethodModel {
    //todo 支持环境变量
    //todo 支持组变量
    private HashMap<String, Object> params;
    public HashMap<String, Object> query;
    public HashMap<String, Object> header;
    public HashMap<String, Object> postBodyMap;
    public String postBodyString;
    public String postBodyRaw;
    public String method = "";
    public String url = "";


    public Response run() {
        RequestSpecification request = given();
        request.queryParam("access_token", Work.getInstance().getToken());
        System.out.println(Work.getInstance().getProjectEnvs());
        if (query != null) {
            query.entrySet().forEach(entry ->
            {
                request.queryParam(entry.getKey(), repalce(entry.getValue().toString()));
            });
        }
        if (header !=null) {
            header.entrySet().forEach(entry ->
            {
                request.header(entry.getKey(), repalce(entry.getValue().toString()));
            });
        }
        if (postBodyMap != null) {
            //todo: support none-json postBody
            HashMap<String, Object> newPostBody = new HashMap<String, Object>();
            postBodyMap.entrySet().forEach(entry ->
            {
                newPostBody.put(entry.getKey(), repalce(entry.getValue().toString()));
            });
            request.body(newPostBody);
        }
        if (postBodyString != null)
        {
            request.body(repalce(postBodyString));
        }
        if (postBodyRaw != null) {
            request.body(postBodyRaw);
        }
        //todo: support binary
        return request
                .when().log().all().request(method, url)
                .then().log().all().extract().response();
    }

    private String repalce(String raw){
        for (Map.Entry<String, Object> kv : params.entrySet()) {
            String matcher = "${" + kv.getKey() + "}";
            if (raw.contains(matcher)) {
                System.out.println(kv);
                raw = raw.replace(matcher, kv.getValue().toString());
            }
        }
        return raw;
    }

    public Response run(HashMap<String, Object> params) {
        this.params = params;
        return run();
    }
}
