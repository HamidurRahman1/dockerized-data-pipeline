package hrahman.ddp.hibernate.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "test_table")
public class TestEntity {

    @Id
    @Column(name = "test_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer testId;

    @Column(name = "value")
    private String value;

    public Integer getTestId() {
        return testId;
    }

    public void setTestId(Integer testId) {
        this.testId = testId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestEntity that = (TestEntity) o;
        return Objects.equals(testId, that.testId) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testId, value);
    }

    @Override
    public String toString() {
        return "TestEntity{" +
                "testId=" + testId +
                ", value='" + value + '\'' +
                '}';
    }
}
