package com.example.moais.controller;


import com.example.moais.UserClaims;
import com.example.moais.dto.MsgDto;
import com.example.moais.dto.TodoCreateDto;
import com.example.moais.service.TodoService;
import com.example.moais.vo.TodoVo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping("")
    public ResponseEntity<TodoVo> createDodo(HttpServletRequest request, @RequestBody TodoCreateDto createDto) {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.createTodo((UserClaims) request.getAttribute("claims"), createDto));
    }

    @GetMapping("")
    public ResponseEntity<List<TodoVo>> getAll(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.getAll((UserClaims) request.getAttribute("claims")));
    }

    @GetMapping("/peek")
    public ResponseEntity<TodoVo> peek(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.peek((UserClaims) request.getAttribute("claims")));
    }

    @PatchMapping("/{todoId}/begin")
    public ResponseEntity<TodoVo> updateTodoRun(@PathVariable Long todoId) {

        return ResponseEntity.status(HttpStatus.OK).body(todoService.statusToRun(todoId));
    }

    @PatchMapping("/{todoId}/end")
    public ResponseEntity<TodoVo> updateTodoEnd(@PathVariable Long todoId) {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.statusToEnd(todoId));
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<MsgDto> deleteTodo(@PathVariable Long todoId) {
        todoService.delete(todoId);
        return ResponseEntity.status(HttpStatus.OK).body(MsgDto.builder().message("OK").build());
    }
}
