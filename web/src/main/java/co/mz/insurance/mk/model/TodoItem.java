package co.mz.insurance.mk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.time.OffsetDateTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * A task that needs to be completed
 */

@Entity
public class TodoItem {

  @JsonProperty("id")
  @JsonSerialize(using = ToStringSerializer.class)
  @Id
  @GeneratedValue
  private Long id;

  @JsonProperty("listId")
  private Long listId;

  @JsonProperty("name")
  private String name;

  @JsonProperty("description")
  private String description;

  @JsonProperty("state")
  private TodoState state;

  @JsonProperty("dueDate")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime dueDate;

  @JsonProperty("completedDate")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime completedDate;

  public TodoItem() {
    this.dueDate = OffsetDateTime.now();
  }

  public TodoItem id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public TodoItem listId(Long listId) {
    this.listId = listId;
    return this;
  }

  /**
   * Get listId
   * @return listId
   */
  @NotNull
  public Long getListId() {
    return listId;
  }

  public void setListId(Long listId) {
    this.listId = listId;
  }

  public TodoItem name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public TodoItem description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  @NotNull
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public TodoItem state(TodoState state) {
    this.state = state;
    return this;
  }

  /**
   * Get state
   * @return state
   */
  @Valid
  public TodoState getState() {
    return state;
  }

  public void setState(TodoState state) {
    this.state = state;
  }

  public TodoItem dueDate(OffsetDateTime dueDate) {
    this.dueDate = dueDate;
    return this;
  }

  /**
   * Get dueDate
   * @return dueDate
   */
  @Valid
  public OffsetDateTime getDueDate() {
    return dueDate;
  }

  public void setDueDate(OffsetDateTime dueDate) {
    this.dueDate = dueDate;
  }

  public TodoItem completedDate(OffsetDateTime completedDate) {
    this.completedDate = completedDate;
    return this;
  }

  /**
   * Get completedDate
   * @return completedDate
   */
  @Valid
  public OffsetDateTime getCompletedDate() {
    return completedDate;
  }

  public void setCompletedDate(OffsetDateTime completedDate) {
    this.completedDate = completedDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TodoItem todoItem = (TodoItem) o;
    return (
      Objects.equals(this.id, todoItem.id) &&
      Objects.equals(this.listId, todoItem.listId) &&
      Objects.equals(this.name, todoItem.name) &&
      Objects.equals(this.description, todoItem.description) &&
      Objects.equals(this.state, todoItem.state) &&
      Objects.equals(this.dueDate, todoItem.dueDate) &&
      Objects.equals(this.completedDate, todoItem.completedDate)
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(
      id,
      listId,
      name,
      description,
      state,
      dueDate,
      completedDate
    );
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TodoItem {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    listId: ").append(toIndentedString(listId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb
      .append("    description: ")
      .append(toIndentedString(description))
      .append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    dueDate: ").append(toIndentedString(dueDate)).append("\n");
    sb
      .append("    completedDate: ")
      .append(toIndentedString(completedDate))
      .append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
