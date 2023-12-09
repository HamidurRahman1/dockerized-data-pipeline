package hrahman.ddp.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NYCViolationsFileInfo {
    private String url;
    private String downloadDir;
    private String fileName;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDownloadDir() {
        return downloadDir;
    }

    public void setDownloadDir(String downloadDir) {
        this.downloadDir = downloadDir;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "NYCViolationsFileInfo{" +
                "url='" + url + '\'' +
                ", downloadDir='" + downloadDir + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
