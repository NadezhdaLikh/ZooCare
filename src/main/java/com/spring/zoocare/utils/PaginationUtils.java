package com.spring.zoocare.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@NoArgsConstructor(access = AccessLevel.PRIVATE) // generates a private no-argument constructor for the class to prevent instantiation of the utility class
/* Utility classes are meant to provide reusable static methods that don't depend on instance-level state.
The lack of instance variables or static variables aligns with the design principle of utility classes:
to provide stateless, reusable static methods that operate only on the parameters passed to them */
public class PaginationUtils {
    public static Pageable makePageRequest(Integer page, Integer pageSize, String sortParam, Sort.Direction sortDirect) {
        if (page == null) {
            page = 0;
        } else if (page > 0) {
            page = page - 1;
        }

        if (pageSize == null) {
            pageSize = 10;
        }

        if (sortParam == null || sortDirect == null) {
            return PageRequest.of(page, pageSize);
        } else if (sortDirect.isDescending()) {
            return PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, sortParam));
        } else return PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, sortParam));
    }
}
