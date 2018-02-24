package net.kaparray.velp.classes;


@SuppressWarnings("unused")
public class TaskLoader {

    public String TaskName, TaskValue, key;

    public TaskLoader (String taskName, String taskValue,String key) {
        this.TaskName = taskName;
        this.TaskValue = taskValue;
        this.key = key;
    }

    public TaskLoader() {
    }
}
