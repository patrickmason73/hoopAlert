package learn.hoopAlert.domain;


import java.util.ArrayList;
import java.util.List;

public class Result<T> {
    private ActionStatus status = ActionStatus.SUCCESS;
    private List<String> messages = new ArrayList<>();
    private T payload;

    public ActionStatus getStatus() {
        return status;
    }

    public T getPayload() {
        return payload;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public void addMessage(ActionStatus status, String message) {
        this.status = status;
        this.messages.add(message);
    }

    public boolean isSuccess() {
        return status == ActionStatus.SUCCESS || messages.isEmpty();
    }

    public void setStatus(ActionStatus status) {
        this.status = status;
    }

    public void addErrorMessage(String message) {
        this.status = ActionStatus.INVALID; // Or other appropriate status for errors
        this.messages.add(message);
    }
}

