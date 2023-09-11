package com.example.moais.repo;

import com.example.moais.vo.TodoVo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepo extends JpaRepository<TodoVo, Long> {

    TodoVo findTopByUser_IdOrderByIdDesc(Long userId);
    List<TodoVo> findAllByUser_Id(Long userId);
}
