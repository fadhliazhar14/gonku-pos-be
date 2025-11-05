package com.gonku.pos_be.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponseDto<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
    private boolean empty;
    private String sort;
    private String direction;

    public static <T> PageResponseDto<T> of(List<T> content, int page, int size, long totalElements,
                                            int totalPages, boolean first, boolean last, boolean empty,
                                            String sort, String direction) {
        PageResponseDto<T> response = new PageResponseDto<>();
        response.setContent(content);
        response.setPage(page);
        response.setSize(size);
        response.setTotalElements(totalElements);
        response.setTotalPages(totalPages);
        response.setFirst(first);
        response.setLast(last);
        response.setEmpty(empty);
        response.setSort(sort);
        response.setDirection(direction);
        return response;
    }
}
