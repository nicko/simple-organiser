package com.cremaconsulting.simpleorganiser.controller;

import com.cremaconsulting.simpleorganiser.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommentStatusGridController {

    private static final Logger log = LoggerFactory.getLogger(CommentStatusGridController.class);

    @Autowired
    private TaskRepository tasks;


    @GetMapping("/grid/")
    public String index(Model model) {

//        final var columns = StreamSupport.stream(groups.findAll().spliterator(), false)
//                .map(group -> {
//                    var col = new TaskGroupController.Column();
//                    col.setName(group.getName());
//                    col.setStatus(group.getStatus());
//                    var columnTasks = tasks.findByStatus(group.getStatus());
//                    columnTasks.sort(Comparator.comparingInt(Task::getPosition));
//                    col.setTasks(columnTasks);
//                    return col;
//                }).collect(Collectors.toList());


//        model.addAttribute("taskForm", new TaskGroupController.TaskForm());
//        model.addAttribute("columns", columns);
        model.addAttribute("tasks", tasks.findAll());
        return "commentStatusGridIndex";
    }


}
