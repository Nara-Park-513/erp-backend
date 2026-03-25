package port.sm.erp.dto;

public class SearchResultDto {

    private String type;
    private Long id;
    private String title;
    private String path;

    public SearchResultDto() {
    }

    public SearchResultDto(String type, Long id, String title, String path) {
        this.type = type;
        this.id = id;
        this.title = title;
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPath(String path) {
        this.path = path;
    }
}