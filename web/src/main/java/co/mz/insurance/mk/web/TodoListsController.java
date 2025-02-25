package co.mz.insurance.mk.web;

import co.mz.insurance.mk.model.TodoItem;
import co.mz.insurance.mk.model.TodoList;
import co.mz.insurance.mk.model.TodoState;
import co.mz.insurance.mk.repository.TodoItemRepository;
import co.mz.insurance.mk.repository.TodoListRepository;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class TodoListsController {

  private final TodoListRepository todoListRepository;

  private final TodoItemRepository todoItemRepository;

  public TodoListsController(
    TodoListRepository todoListRepository,
    TodoItemRepository todoItemRepository
  ) {
    this.todoListRepository = todoListRepository;
    this.todoItemRepository = todoItemRepository;
  }

  /**
   * POST /lists/{listId}/items : Creates a new Todo item within a list
   *
   * @param listId   The Todo list unique identifier (required)
   * @param todoItem The Todo Item (optional)
   * @return A Todo item result (status code 201)
   * or Todo list not found (status code 404)
   */
  @PostMapping("/lists/{listId}/items")
  public ResponseEntity<TodoItem> createItem(
    @PathVariable("listId") Long listId,
    @Valid @RequestBody(required = false) TodoItem todoItem
  ) {
    Optional<TodoList> optionalTodoList = todoListRepository.findById(listId);
    if (optionalTodoList.isPresent()) {
      todoItem.setListId(listId);
      TodoItem savedTodoItem = todoItemRepository.save(todoItem);
      URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(savedTodoItem.getId())
        .toUri();
      return ResponseEntity.created(location).body(savedTodoItem);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * POST /lists : Creates a new Todo list
   *
   * @param todoList The Todo List (optional)
   * @return A Todo list result (status code 201)
   * or Invalid request schema (status code 400)
   */
  @PostMapping("/lists")
  public ResponseEntity<TodoList> createList(
    @Valid @RequestBody(required = false) TodoList todoList
  ) {
    TodoList savedTodoList = todoListRepository.save(todoList);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
      .path("/{id}")
      .buildAndExpand(savedTodoList.getId())
      .toUri();
    return ResponseEntity.created(location).body(savedTodoList);
  }

  /**
   * DELETE /lists/{listId}/items/{itemId} : Deletes a Todo item by unique identifier
   *
   * @param listId The Todo list unique identifier (required)
   * @param itemId The Todo list unique identifier (required)
   * @return Todo item deleted successfully (status code 204)
   * or Todo list or item not found (status code 404)
   */
  @DeleteMapping("/lists/{listId}/items/{itemId}")
  public ResponseEntity<Void> deleteItemById(
    @PathVariable("listId") Long listId,
    @PathVariable("itemId") Long itemId
  ) {
    Optional<TodoItem> todoItem = getTodoItem(listId, itemId);
    if (todoItem.isPresent()) {
      todoItemRepository.deleteById(itemId);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * DELETE /lists/{listId} : Deletes a Todo list by unique identifier
   *
   * @param listId The Todo list unique identifier (required)
   * @return Todo list deleted successfully (status code 204)
   * or Todo list not found (status code 404)
   */
  @DeleteMapping("/lists/{listId}")
  public ResponseEntity<Void> deleteListById(
    @PathVariable("listId") Long listId
  ) {
    Optional<TodoList> todoList = todoListRepository.findById(listId);
    if (todoList.isPresent()) {
      todoListRepository.deleteById(listId);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * GET /lists/{listId}/items/{itemId} : Gets a Todo item by unique identifier
   *
   * @param listId The Todo list unique identifier (required)
   * @param itemId The Todo list unique identifier (required)
   * @return A Todo item result (status code 200)
   * or Todo list or item not found (status code 404)
   */
  @GetMapping("/lists/{listId}/items/{itemId}")
  public ResponseEntity<TodoItem> getItemById(
    @PathVariable("listId") Long listId,
    @PathVariable("itemId") Long itemId
  ) {
    return getTodoItem(listId, itemId)
      .map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * GET /lists/{listId}/items : Gets Todo items within the specified list
   *
   * @param listId The Todo list unique identifier (required)
   * @param top    The max number of items to returns in a result (optional)
   * @param skip   The number of items to skip within the results (optional)
   * @return An array of Todo items (status code 200)
   * or Todo list not found (status code 404)
   */

  @GetMapping("/lists/{listId}/items")
  public ResponseEntity<List<TodoItem>> getItemsByListId(
    @PathVariable("listId") Long listId,
    @Valid @RequestParam(
      value = "top",
      required = false,
      defaultValue = "20"
    ) BigDecimal top,
    @Valid @RequestParam(
      value = "skip",
      required = false,
      defaultValue = "0"
    ) BigDecimal skip
  ) {
    Optional<TodoList> todoList = todoListRepository.findById(listId);
    if (todoList.isPresent()) {
      return ResponseEntity.ok(
        todoItemRepository.findByListId(
          listId,
          PageRequest.of(skip.intValue(), top.intValue())
        )
      );
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * GET /lists/{listId}/items/state/{state} : Gets a list of Todo items of a specific state
   *
   * @param listId The Todo list unique identifier (required)
   * @param state  The Todo item state (required)
   * @param top    The max number of items to returns in a result (optional)
   * @param skip   The number of items to skip within the results (optional)
   * @return An array of Todo items (status code 200)
   * or Todo list or item not found (status code 404)
   */
  @GetMapping("/lists/{listId}/items/state/{state}")
  public ResponseEntity<List<TodoItem>> getItemsByListIdAndState(
    @PathVariable("listId") Long listId,
    @PathVariable("state") TodoState state,
    @Valid @RequestParam(
      value = "top",
      required = false,
      defaultValue = "20"
    ) BigDecimal top,
    @Valid @RequestParam(
      value = "skip",
      required = false,
      defaultValue = "0"
    ) BigDecimal skip
  ) {
    return ResponseEntity.ok(
      todoItemRepository.findByListIdAndState(
        listId,
        state.name(),
        PageRequest.of(skip.intValue(), top.intValue())
      )
    );
  }

  /**
   * GET /lists/{listId} : Gets a Todo list by unique identifier
   *
   * @param listId The Todo list unique identifier (required)
   * @return A Todo list result (status code 200)
   * or Todo list not found (status code 404)
   */
  @GetMapping("/lists/{listId}")
  public ResponseEntity<TodoList> getListById(
    @PathVariable("listId") Long listId
  ) {
    return todoListRepository
      .findById(listId)
      .map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * GET /lists : Gets an array of Todo lists
   *
   * @param top  The max number of items to returns in a result (optional)
   * @param skip The number of items to skip within the results (optional)
   * @return An array of Todo lists (status code 200)
   */
  @GetMapping("/lists")
  public ResponseEntity<List<TodoList>> getLists(
    @Valid @RequestParam(value = "top", required = false) BigDecimal top,
    @Valid @RequestParam(value = "skip", required = false) BigDecimal skip
  ) {
    if (top == null) {
      top = new BigDecimal(20);
    }
    if (skip == null) {
      skip = new BigDecimal(0);
    }
    return ResponseEntity.ok(
      todoListRepository
        .findAll(PageRequest.of(skip.intValue(), top.intValue()))
        .getContent()
    );
  }

  /**
   * PUT /lists/{listId}/items/{itemId} : Updates a Todo item by unique identifier
   *
   * @param listId   The Todo list unique identifier (required)
   * @param itemId   The Todo list unique identifier (required)
   * @param todoItem The Todo Item (optional)
   * @return A Todo item result (status code 200)
   * or Todo item is invalid (status code 400)
   * or Todo list or item not found (status code 404)
   */
  @PutMapping("/lists/{listId}/items/{itemId}")
  public ResponseEntity<TodoItem> updateItemById(
    @PathVariable("listId") Long listId,
    @PathVariable("itemId") Long itemId,
    @Valid @RequestBody(required = false) TodoItem todoItem
  ) {
    return getTodoItem(listId, itemId)
      .map(t -> {
        todoItemRepository.save(todoItem);
        return ResponseEntity.ok(todoItem);
      })
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * PUT /lists/{listId}/items/state/{state} : Changes the state of the specified list items
   *
   * @param listId      The Todo list unique identifier (required)
   * @param state       The Todo item state (required)
   * @param requestBody (optional)
   * @return Todo items updated (status code 204)
   * or Update request is invalid (status code 400)
   */
  @PutMapping("/lists/{listId}/items/state/{state}")
  public ResponseEntity<Void> updateItemsStateByListId(
    @PathVariable("listId") Long listId,
    @PathVariable("state") TodoState state,
    @Valid @RequestBody(required = false) List<String> requestBody
  ) {
    for (TodoItem todoItem : todoItemRepository.findByListId(listId)) {
      todoItem.state(state);
      todoItemRepository.save(todoItem);
    }
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * PUT /lists/{listId} : Updates a Todo list by unique identifier
   *
   * @param listId   The Todo list unique identifier (required)
   * @param todoList The Todo List (optional)
   * @return A Todo list result (status code 200)
   * or Todo list is invalid (status code 400)
   */
  @PutMapping("/lists/{listId}")
  public ResponseEntity<TodoList> updateListById(
    @PathVariable("listId") Long listId,
    @Valid @RequestBody(required = false) TodoList todoList
  ) {
    return todoListRepository
      .findById(listId)
      .map(t -> ResponseEntity.ok(todoListRepository.save(todoList)))
      .orElseGet(() -> ResponseEntity.badRequest().build());
  }

  private Optional<TodoItem> getTodoItem(Long listId, Long itemId) {
    Optional<TodoList> optionalTodoList = todoListRepository.findById(listId);
    if (optionalTodoList.isEmpty()) {
      return Optional.empty();
    }
    Optional<TodoItem> optionalTodoItem = todoItemRepository.findById(itemId);
    if (optionalTodoItem.isPresent()) {
      TodoItem todoItem = optionalTodoItem.get();
      if (todoItem.getListId().equals(listId)) {
        return Optional.of(todoItem);
      } else {
        return Optional.empty();
      }
    } else {
      return Optional.empty();
    }
  }
}
