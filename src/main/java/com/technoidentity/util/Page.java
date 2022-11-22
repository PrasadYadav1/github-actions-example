package com.technoidentity.util;

public class Page {
  private static final int maxLimit = 100;
  private int pageIndex = 0;
  private int pageSize = 20;

  public Page(int pageIndex, int pageSize) {
    setPageIndex(pageIndex);
    setPageSize(pageSize);
  }

  public int getPageIndex() {
    return pageIndex;
  }

  public void setPageIndex(int pageIndex) {
    if (pageIndex > 0) {
      this.pageIndex = pageIndex;
    }
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    if (pageSize > maxLimit) {
      this.pageSize = maxLimit;
    } else if (pageSize > 0) {
      this.pageSize = pageSize;
    }
  }
}
