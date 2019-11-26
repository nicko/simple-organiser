package com.cremaconsulting.simpleorganiser.controller;

import com.cremaconsulting.simpleorganiser.model.Task;
import com.cremaconsulting.simpleorganiser.model.TaskGroup;
import com.cremaconsulting.simpleorganiser.model.TaskGroupRepository;
import com.cremaconsulting.simpleorganiser.model.TaskRepository;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class TaskGroupController {

    private static final Logger log = LoggerFactory.getLogger(TaskGroupController.class);

    @Autowired
    private TaskRepository tasks;

    @Autowired
    private TaskGroupRepository groups;

    @GetMapping("/")
    public String index(Model model) {
        final var columns = StreamSupport.stream(groups.findAll().spliterator(), false)
                .map(group -> {
                    var col = new Column();
                    col.setName(group.getName());
                    col.setStatus(group.getStatus());
                    var columnTasks = tasks.findByStatus(group.getStatus());
                    columnTasks.sort(Comparator.comparingInt(Task::getPosition));
                    col.setTasks(columnTasks);
                    return col;
                }).collect(Collectors.toList());

        model.addAttribute("taskForm", new TaskForm());
        model.addAttribute("columns", columns);
        return "taskGroupsIndex";
    }

    @PostMapping("/tasks")
    public String createTask(@Valid @ModelAttribute TaskForm taskForm, BindingResult bindingResult, Model model, RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            for (var o : bindingResult.getAllErrors()) {
                log.error(o.toString());
                // Log error and return. The UI should catch this before we send it so no need to report the errors from here.
                model.addAttribute("errorMessage", "Unable to add task");
                return index(model);
            }
        }

        if (tasks.count() > 100) {
            tasks.deleteAll();
            attributes.addFlashAttribute("errorMessage", "Too many tasks, I've deleted them all");
            return "redirect:/";
        }

        Task task = tasks.save(new Task(taskForm.getTitle(), taskForm.getStatus(), taskForm.getPosition()));
        log.info("Saved new instance of task " + task.getId() + ": " + task.getTitle());
        attributes.addFlashAttribute("message", "Task added");
        return "redirect:/";
    }

    @PostMapping("/taskGroups")
    public String createTaskGroup(@Valid @ModelAttribute TaskGroupForm taskGroupForm, BindingResult bindingResult, Model model, RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            for (var o : bindingResult.getAllErrors()) {
                log.error(o.toString());
                // Log error and return. The UI should catch this before we send it so no need to report the errors from here.
                model.addAttribute("errorMessage", "Unable to add group");
                return index(model);
            }
        }

        if (groups.count() > 100) {
            groups.deleteAll();
            attributes.addFlashAttribute("errorMessage", "Too many groups, I've deleted them all");
            return "redirect:/";
        }

        TaskGroup taskGroup = groups.save(new TaskGroup(taskGroupForm.getName(), taskGroupForm.getPosition(), taskGroupForm.getStatus()));
        log.info("Saved new instance of group " + taskGroup.getId() + ": " + taskGroup.getName());
        attributes.addFlashAttribute("message", "Group added");
        return "redirect:/";
    }

    public static class Column {
        private String name;
        private List<Task> tasks;
        private String status;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Task> getTasks() {
            return tasks;
        }

        public void setTasks(List<Task> tasks) {
            this.tasks = tasks;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public static class TaskForm {
        @NotNull
        @Length(max = 127, message = "A title cannot be more than 127 characters")
        private String title;

        @NotNull
        @Length(max = 20, message = "A status cannot be longer than 10 characters")
        private String status;

        @NotNull
        @Max(value = 1024, message = "Position must be a value between -1024 and 1024")
        @Min(value = -1024, message = "Position must be a value between -1024 and 1024")
        private Integer position;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Integer getPosition() {
            return position;
        }

        public void setPosition(Integer position) {
            this.position = position;
        }
    }

    public static class TaskGroupForm {
        @NotNull
        @Length(max = 127, message = "A name cannot be more than 127 characters")
        private String name;

        @NotNull
        @Length(max = 20, message = "A status cannot be longer than 10 characters")
        private String status;

        @NotNull
        @Max(value = 1024, message = "Position must be a value between -1024 and 1024")
        @Min(value = -1024, message = "Position must be a value between -1024 and 1024")
        private Integer position;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Integer getPosition() {
            return position;
        }

        public void setPosition(Integer position) {
            this.position = position;
        }
    }
}
