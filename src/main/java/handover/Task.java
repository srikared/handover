package handover;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Task {
    int id;
    String message_id;
    String task;
    int task_completed;

    public Task(int id, String message_id, String task, int task_completed) {
        this.id = id;
        this.message_id = message_id;
        this.task = task;
        this.task_completed = task_completed;
    }
}
