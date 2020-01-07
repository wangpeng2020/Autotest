package framework;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.restassured.response.Response;

import java.io.IOException;
import java.util.HashMap;

public class BaseApi {
    //todo: 使用注解优化参数传递，减少重复代码
    public ApiObjectModel model = new ApiObjectModel();
    HashMap<String, Object> params = new HashMap<>();
    public Response parseSteps() {
        String method = Thread.currentThread().getStackTrace()[2].getMethodName();
        System.out.println(method);

        if(model.methods.entrySet().isEmpty()) {
            System.out.println("first load");
            String path = "/" + this.getClass().getCanonicalName().replace('.', '/') + ".yaml";
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            try {
                model = mapper.readValue(
                        BaseApi.class.getResourceAsStream(path), ApiObjectModel.class
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return (Response) model.run(method, params);
    }

    public void setParams(HashMap<String, Object> data){
        params=data;
    }
}