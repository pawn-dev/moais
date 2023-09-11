package com.example.moais.service;

import com.example.moais.UserClaims;
import com.example.moais.dto.TodoCreateDto;
import com.example.moais.repo.TodoRepo;
import com.example.moais.repo.UserRepo;
import com.example.moais.vo.TodoVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    private final TodoRepo todoRepo;
    private final UserRepo userRepo;

    public TodoService(TodoRepo todoRepo, UserRepo userRepo) {
        this.todoRepo = todoRepo;
        this.userRepo = userRepo;
    }

    public TodoVo createTodo(UserClaims userClaims, TodoCreateDto createDto) {
        return todoRepo.save(TodoVo.builder()
                .user(userRepo.findById(userClaims.getUserId()).orElseThrow())
                .status("todo")
                .todoContent(createDto.getContent())
                .build());
    }

    public TodoVo peek(UserClaims userClaims) {
        return todoRepo.findTopByUser_IdOrderByIdDesc(userClaims.getUserId());
    }

    public List<TodoVo> getAll(UserClaims userClaims) {
        return todoRepo.findAllByUser_Id(userClaims.getUserId());
    }

    public TodoVo statusToRun(Long todoId) {
        TodoVo vo = todoRepo.findById(todoId).orElseThrow();
        vo.setStatus("run");
        return todoRepo.save(vo);
    }

    public TodoVo statusToEnd(Long todoId) {
        TodoVo vo = todoRepo.findById(todoId).orElseThrow();
        vo.setStatus("end");
        return todoRepo.save(vo);
    }

    public void delete(Long todoId) {
        todoRepo.deleteById(todoId);
    }
}
