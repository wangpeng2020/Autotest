package service.department.testcase;

import framework.BaseApi;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.department.api.Department;
import service.department.api.DepartmentApi;

import java.util.ArrayList;
import java.util.HashMap;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public class TestDepartmentApi {
    static DepartmentApi departmentApi = new DepartmentApi();
    public static HashMap<String, String> groupEnvs = new HashMap<>();

    @BeforeAll
    public static void clearData() {
        //清理数据
        ArrayList<Integer> departmentIds = departmentApi.list(departmentApi.parentDepartmentId)
                .then().extract().body().path("department.findAll {d->d.parentid=="+departmentApi.parentDepartmentId+"}.id");
        System.out.println(departmentIds);
        departmentIds.forEach(departmentId -> departmentApi.delete(departmentId));
    }

    @Test
    public void list() {
        departmentApi.list(departmentApi.parentDepartmentId).then().log().all()
                .body("errmsg", equalTo("ok"));
    }

    @Test
    public void creat() {
        String departmentName = "forCreate";
        departmentApi.create("forCreate", departmentApi.parentDepartmentId)
                .then().body("errmsg", equalTo("created"));
        departmentApi.list(departmentApi.parentDepartmentId)
                .then().body("department.findAll {d->d.name=='"+departmentName+"'}", hasSize(1));
    }

    @Test
    public void delete() {
        Integer departmentId = departmentApi.create("forDelete").then().body("errmsg", equalTo("created"))
                .extract().body().path("id");
        departmentApi.delete(departmentId).then().body("errmsg", equalTo("deleted"));
    }
}
