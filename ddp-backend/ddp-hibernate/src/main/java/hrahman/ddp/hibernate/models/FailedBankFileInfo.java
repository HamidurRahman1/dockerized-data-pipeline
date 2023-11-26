package hrahman.ddp.hibernate.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "failed_bank_file_info", schema = "ddp_schema")
public class FailedBankFileInfo {

    private Integer id;
    private String url;
    private String downloadDir;
    private String fileName;
    private Integer httpStatusCode;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(name = "download_dir")
    public String getDownloadDir() {
        return downloadDir;
    }

    public void setDownloadDir(String downloadDir) {
        this.downloadDir = downloadDir;
    }

    @Column(name = "filename")
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Column(name = "http_code")
    public Integer getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(Integer httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FailedBankFileInfo that = (FailedBankFileInfo) o;
        return Objects.equals(id, that.id)
                && Objects.equals(url, that.url)
                && Objects.equals(downloadDir, that.downloadDir)
                && Objects.equals(fileName, that.fileName)
                && Objects.equals(httpStatusCode, that.httpStatusCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, downloadDir, fileName, httpStatusCode);
    }

    @Override
    public String toString() {
        return "FailedBankFileInfo{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", downloadDir='" + downloadDir + '\'' +
                ", fileName='" + fileName + '\'' +
                ", httpStatusCode=" + httpStatusCode +
                '}';
    }
}
