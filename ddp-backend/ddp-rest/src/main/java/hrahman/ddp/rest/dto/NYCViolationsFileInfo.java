package hrahman.ddp.rest.dto;

public class NYCViolationsFileInfo {
    private String url;
    private String downloadDir;
    private String fileName;
    private String saveMode;

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

    public String getSaveMode() {
        return saveMode;
    }

    public void setSaveMode(String saveMode) {
        this.saveMode = saveMode;
    }

    @Override
    public String toString() {
        return "NYCViolationsFileInfo{" +
                "url='" + url + '\'' +
                ", downloadDir='" + downloadDir + '\'' +
                ", fileName='" + fileName + '\'' +
                ", saveMode='" + saveMode + '\'' +
                '}';
    }
}
