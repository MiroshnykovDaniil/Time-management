package com.service.subjectservice.service;

import feign.Param;
import feign.RequestLine;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.hystrix.FallbackFactory;
import feign.hystrix.HystrixFeign;
import org.springframework.cloud.openfeign.FeignClient;
import com.service.taskservice.model.Task;

import java.util.List;

@FeignClient(name = "tasks-service", url = "http://127.0.0.1:8081/", fallback = Fallback.class)
public interface ServiceFeignClient {

    @RequestLine("GET /subject/{id}")
    List<Task> getTasksListBySubjectId(@Param(value = "id") String id);

    class FeignHolder {
        public static ServiceFeignClient create() {
            return HystrixFeign.builder().encoder(new GsonEncoder()).decoder(new GsonDecoder()).target(ServiceFeignClient.class, "http://localhost:8081/", new FallbackFactory<ServiceFeignClient>() {
                @Override
                public ServiceFeignClient create(Throwable throwable) {
                    return id -> {
                        System.out.println(throwable.getMessage());
                        return null;
                    };
                }
            });
        }
    }
}
