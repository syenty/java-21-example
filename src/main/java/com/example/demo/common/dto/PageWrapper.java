package com.example.demo.common.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.data.domain.Page;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PageWrapper<T> {

  private final List<T> content;
  private final int page;
  private final int size;
  private final int totalPages;
  private final long totalElements;
  private final boolean first;
  private final boolean last;

  private PageWrapper(final Page<T> page) {
    this.content = page.getContent();
    this.page = page.getNumber();
    this.size = page.getSize();
    this.totalPages = page.getTotalPages();
    this.totalElements = page.getTotalElements();
    this.first = page.isFirst();
    this.last = page.isLast();
  }

  public static <T> PageWrapper of(final Page<T> page) {
    return new PageWrapper<>(page);
  }

  public static <T> PageWrapper of(final List<T> content,
      final int page,
      final int size,
      final long count) {
    int pageCount = (int) Math.ceil((double) count / size);
    return new PageWrapper(
        content,
        page,
        size,
        pageCount,
        count,
        page == 0,
        page == pageCount - 1);
  }

}
