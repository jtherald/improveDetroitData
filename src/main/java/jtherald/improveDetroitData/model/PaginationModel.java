package jtherald.improveDetroitData.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaginationModel {

    Integer entries;

    Integer page;

    Integer per_page;

    Integer pages;

    Integer next_page;

    String next_page_url;

    Integer previous_page;

    String previous_page_url;

}
