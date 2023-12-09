package hrahman.ddp.hibernate.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "nyc_violation_analytics", schema = "ddp_schema")
public class NycViolationAnalytics implements Serializable {

    private Integer id;
    private String inputFile;
    private String countyName;
    private Long countyViolations;
    private String precinct;
    private Double precinctAvgFine;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "input_file")
    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    @Column(name = "county_name")
    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    @Column(name = "county_violations", columnDefinition = "numeric")
    public Long getCountyViolations() {
        return countyViolations;
    }

    public void setCountyViolations(Long countyViolations) {
        this.countyViolations = countyViolations;
    }

    @Column(name = "precinct")
    public String getPrecinct() {
        return precinct;
    }

    public void setPrecinct(String precinct) {
        this.precinct = precinct;
    }

    @Column(name = "precinct_avg_fine", columnDefinition = "numeric")
    public Double getPrecinctAvgFine() {
        return precinctAvgFine;
    }

    public void setPrecinctAvgFine(Double precinctAvgFine) {
        this.precinctAvgFine = precinctAvgFine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NycViolationAnalytics that = (NycViolationAnalytics) o;
        return Objects.equals(id, that.id)
                && Objects.equals(inputFile, that.inputFile)
                && Objects.equals(countyName, that.countyName)
                && Objects.equals(countyViolations, that.countyViolations)
                && Objects.equals(precinct, that.precinct)
                && Objects.equals(precinctAvgFine, that.precinctAvgFine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, inputFile, countyName, countyViolations, precinct, precinctAvgFine);
    }

    @Override
    public String toString() {
        return "NYCViolationAnalytics{" +
                "id=" + id +
                ", inputFile='" + inputFile + '\'' +
                ", countyName='" + countyName + '\'' +
                ", countyViolations='" + countyViolations + '\'' +
                ", precinct='" + precinct + '\'' +
                ", precinctAvgFine='" + precinctAvgFine + '\'' +
                '}';
    }
}
