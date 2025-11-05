package com.gonku.pos_be.util;

import com.gonku.pos_be.dto.common.PageRequestDto;
import com.gonku.pos_be.dto.common.PageResponseDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PageUtil {
    public static Pageable createPageable(PageRequestDto pageRequestDto) {
        Sort.Direction direction = "desc".equalsIgnoreCase(pageRequestDto.getDirection())
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Sort sort = Sort.by(direction, pageRequestDto.getSort());

        return PageRequest.of(pageRequestDto.getPage(), pageRequestDto.getSize(), sort);
    }

    public static <T> PageResponseDto<T> createPageResponse(List<T> content, Pageable pageable, long totalElements) {
        int totalPages = (int) Math.ceil((double) totalElements / pageable.getPageSize());
        boolean isFirst = pageable.getPageNumber() == 0;
        boolean isLast = pageable.getPageNumber() >= totalPages - 1;
        boolean isEmpty = content.isEmpty();

        String sort = pageable.getSort().isSorted()
                ? pageable.getSort().iterator().next().getProperty()
                : "id";
        String direction = pageable.getSort().isSorted()
                ? pageable.getSort().iterator().next().getDirection().name().toLowerCase()
                : "asc";

        return PageResponseDto.of(
                content,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                totalElements,
                totalPages,
                isFirst,
                isLast,
                isEmpty,
                sort,
                direction
        );
    }
}
