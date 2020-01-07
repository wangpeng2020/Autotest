package service.department.testcase;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import java.util.ArrayList;

import service.department.api.Department;


public class TestDepartment {
    static Department department = new Department();

    @BeforeAll
    public static void clearData() {
        //清理数据
        ArrayList<Integer> departmentIds = department.list(department.parentDepartmentId)
                .then().extract().body().path("department.findAll {d->d.parentid=="+department.parentDepartmentId+"}.id");
        System.out.println(departmentIds);
        departmentIds.forEach(departmentId -> department.delete(departmentId));
    }

    @Test
    public void list() {
        department.list(department.parentDepartmentId).then().log().all()
                .body("errmsg", equalTo("ok"));
    }

    @Test
    public void creat() {
        String departmentName = "forCreate";
        department.create("forCreate", department.parentDepartmentId)
                .then().body("errmsg", equalTo("created"));
        department.list(department.parentDepartmentId)
                .then().body("department.findAll {d->d.name=='"+departmentName+"'}", hasSize(1));
    }

    @Test
    public void delete() {
        int departmentId = department.create("forDelete").then().body("errmsg", equalTo("created"))
                .extract().body().path("id");
        department.delete(departmentId).then().body("errmsg", equalTo("deleted"));
    }
}
