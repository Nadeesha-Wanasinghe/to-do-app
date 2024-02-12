package lk.ijse.dep11.todo.api;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lk.ijse.dep11.todo.to.TaskTO;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PreDestroy;
import java.sql.*;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskHttpController {

    private final HikariDataSource pool;

    public TaskHttpController() {
        HikariConfig config = new HikariConfig();
        config.setUsername("postgres");
        config.setPassword("postgres");
        config.setJdbcUrl("jdbc:postgresql://localhost:15000/dep11_todo_app");
        config.setDriverClassName("org.postgresql.Driver");
        config.addDataSourceProperty("maximumPoolSize", 10);
        pool = new HikariDataSource(config);
    }

    @PreDestroy
    public void destroy(){
        pool.close();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = "application/json", consumes = "application/json")
    public TaskTO createTask(@RequestBody @Validated(TaskTO.Create.class) TaskTO task) {
        try (Connection connection = pool.getConnection()){
            PreparedStatement stm = connection
                    .prepareStatement("INSERT INTO task (description, status) VALUES (?, FALSE)",
                            Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, task.getDescription());
            stm.executeUpdate();
            ResultSet generatedKeys = stm.getGeneratedKeys();
            generatedKeys.next();
            int id = generatedKeys.getInt(1);
            task.setId(id);
            task.setStatus(false);
            return task;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{id}", consumes = "application/json")
    public void updateTask(@PathVariable int id,
                           @RequestBody @Validated(TaskTO.Update.class) TaskTO task) {
        try (Connection connection = pool.getConnection()) {
            PreparedStatement stmExist = connection
                    .prepareStatement("SELECT * FROM task WHERE id = ?");
            stmExist.setInt(1, id);
            if (!stmExist.executeQuery().next()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task Not Found");
            }

            PreparedStatement stm = connection
                    .prepareStatement("UPDATE task SET description = ?, status=? WHERE id=?");
            stm.setString(1, task.getDescription());
            stm.setBoolean(2, task.getStatus());
            stm.setInt(3, id);
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable("id") String taskId) {
        System.out.println("deleteTask()");
    }

    @GetMapping(produces = "application/json")
    public List<TaskTO> getAllTasks() {
        System.out.println("getAllTasks()");
        return null;
    }
}
