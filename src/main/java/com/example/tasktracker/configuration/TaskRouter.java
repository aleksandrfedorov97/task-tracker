package com.example.tasktracker.configuration;

import com.example.tasktracker.web.handler.TaskHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class TaskRouter {
    @Bean
    public RouterFunction<ServerResponse> itemRouters(TaskHandler taskHandler) {
        return RouterFunctions.route()
                .GET("/api/v2/task", taskHandler::getAllTask)
                .GET("/api/v2/task/{id}", taskHandler::findById)
                .POST("/api/v2/task", taskHandler::create)
                .PUT("/api/v2/task/{id}", taskHandler::update)
                .PUT("/api/v2/task/observer/{id}", taskHandler::addObserver)
                .DELETE("/api/v2/task/{id}", taskHandler::delete)
                .build();
    }
}
