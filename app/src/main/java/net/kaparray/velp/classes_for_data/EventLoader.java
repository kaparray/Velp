package net.kaparray.velp.classes_for_data;



public class EventLoader{

    private String nameEvent;
    private String valueEvent;
    private String organizerEvent;

    public EventLoader(String nameEvent, String valueEvent, String organizerEvent) {
        this.nameEvent = nameEvent;
        this.valueEvent = valueEvent;
        this.organizerEvent = organizerEvent;
    }

    public EventLoader() {
    }

    public String getNameEvent() {
        return nameEvent;
    }

    public void setNameEvent(String nameEvent) {
        this.nameEvent = nameEvent;
    }

    public String getValueEvent() {
        return valueEvent;
    }

    public void setValueEvent(String valueEvent) {
        this.valueEvent = valueEvent;
    }

    public String getOrganizerEvent() {
        return organizerEvent;
    }

    public void setOrganizerEvent(String organizerEvent) {
        this.organizerEvent = organizerEvent;
    }
}
