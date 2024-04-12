package co.mz.insurance.mk.repository;

import co.mz.insurance.mk.model.TodoList;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoListRepository
  extends PagingAndSortingRepository<TodoList, Long> {}
