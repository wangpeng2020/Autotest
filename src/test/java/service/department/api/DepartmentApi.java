package service.department.api;

import framework.BaseApi;
import io.restassured.response.Response;

import java.awt.print.PrinterGraphics;
import java.util.HashMap;

public class DepartmentApi extends BaseApi {

    public final Integer parentDepartmentId = 1;

    public Response delete(Integer departmentId) {
        HashMap<String, Object> params=new HashMap<>();
        params.put("departmentId", departmentId);
        setParams(params);
        return parseSteps();
    }

    public Response create(String departmentName, Integer parentDepartmentId) {
        HashMap<String, Object> params=new HashMap<>();
        params.put("departmentName", departmentName);
        params.put("parentDepartmentId", parentDepartmentId);
        setParams(params);
        return parseSteps();
    }

    public Response create(String departmentName) {
        return create(departmentName, parentDepartmentId);
    }

    public Response list(Integer departmentId) {
        HashMap<String, Object> params=new HashMap<>();
        params.put("departmentId", departmentId);
        setParams(params);
        return parseSteps();
    }
}
