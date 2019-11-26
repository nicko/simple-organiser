/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cremaconsulting.simpleorganiser.controller;

import com.cremaconsulting.simpleorganiser.Application;
import com.cremaconsulting.simpleorganiser.model.Task;
import com.cremaconsulting.simpleorganiser.model.TaskGroup;
import com.cremaconsulting.simpleorganiser.model.TaskGroupRepository;
import com.cremaconsulting.simpleorganiser.model.TaskRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = TaskGroupController.class)
public class TaskGroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskGroupRepository taskGroupRepository;

    @MockBean
    private TaskRepository taskRepository;

    @Test
    public void homePage() throws Exception {
        given(taskGroupRepository.findAll()).willReturn(Arrays.asList(
                new TaskGroup("To Do", 1, "NEW"),
                new TaskGroup("Finished", 2, "DERP")
        ));
        given(taskRepository.findByStatus("NEW")).willReturn(Arrays.asList(
                new Task("Wash the car", "NEW", 2),
                new Task("Eat the chocolate cake", "NEW", 0)
        ));
        given(taskRepository.findByStatus("DERP")).willReturn(Arrays.asList(
                new Task("Patch the bathtub", "DERP", 1)
        ));

        mockMvc.perform(get("/"))
                .andExpect(content().string(containsString("Simple Task Manager")))
                .andExpect(content().string(stringContainsInOrder(List.of(
                        "To Do",
                        "Wash the car",
                        "Finished",
                        "Patch the bathtub"
                ))));
    }

    @Test
    public void addTask() throws Exception {
        Task expected = new Task("A new task!", "SOME_STATUS", 100);

        given(taskRepository.count()).willReturn(0L);
        given(taskRepository.save(ArgumentMatchers.any())).willReturn(expected);

        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(buildUrlEncodedFormEntity(
                        "title", "A new task!",
                        "status", "SOME_STATUS",
                        "position", "100"
                )))
                .andExpect(redirectedUrl("/"));

        verify(taskRepository).save(ArgumentMatchers.refEq(expected));
    }

    @Test
    public void addTaskOver100() throws Exception {
        given(taskRepository.count()).willReturn(101L);

        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(buildUrlEncodedFormEntity(
                        "title", "A new task!",
                        "status", "SOME_STATUS",
                        "position", "100"
                )))
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("errorMessage", containsString("deleted")));
    }

    @Test
    public void addTaskGroup() throws Exception {
        TaskGroup expected = new TaskGroup("A new group!", 100, "SOME_STATUS");

        given(taskGroupRepository.count()).willReturn(0L);
        given(taskGroupRepository.save(ArgumentMatchers.any())).willReturn(expected);

        mockMvc.perform(post("/taskGroups")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(buildUrlEncodedFormEntity(
                        "name", "A new group!",
                        "status", "SOME_STATUS",
                        "position", "100"
                )))
                .andExpect(redirectedUrl("/"));

        verify(taskGroupRepository).save(ArgumentMatchers.refEq(expected));
    }

    @Test
    public void addGroupOver100() throws Exception {
        given(taskGroupRepository.count()).willReturn(101L);

        mockMvc.perform(post("/taskGroups")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(buildUrlEncodedFormEntity(
                        "name", "A new group!",
                        "status", "SOME_STATUS",
                        "position", "100"
                )))
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("errorMessage", containsString("deleted")));
    }

    private String buildUrlEncodedFormEntity(String... params) {
        if ((params.length % 2) > 0) {
            throw new IllegalArgumentException("Need to give an even number of parameters");
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < params.length; i += 2) {
            if (i > 0) {
                result.append('&');
            }
            try {
                result.
                        append(URLEncoder.encode(params[i], StandardCharsets.UTF_8.name())).
                        append('=').
                        append(URLEncoder.encode(params[i + 1], StandardCharsets.UTF_8.name()));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        return result.toString();
    }

}
