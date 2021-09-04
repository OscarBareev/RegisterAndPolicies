package dataClass;

import java.util.Objects;

public class Info {

    private String index;
    private String name;


    public Info(String index, String name) {
        this.index = index;
        this.name = name;
    }


    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Info)) return false;
        Info info = (Info) o;
        return Objects.equals(getIndex(), info.getIndex()) && Objects.equals(getName(), info.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIndex(), getName());
    }

    @Override
    public String toString() {
        return  "index='" + index + '\'' +
                ", name='" + name;
    }
}
