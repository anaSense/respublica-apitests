package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PaginationModel {
    int current, previous, next;
    @JsonProperty("per_page")
    int perPage;
    int pages, count;
}
