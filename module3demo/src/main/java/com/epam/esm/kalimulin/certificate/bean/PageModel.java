package com.epam.esm.kalimulin.certificate.bean;

import java.io.Serializable;
import java.util.StringJoiner;

public class PageModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int page;
	private int size;

	public PageModel() {
	}

	public PageModel(int page, int size) {
		this.page = page;
		this.size = size;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PageModel pageModel = (PageModel) o;

        if (page != pageModel.page) return false;
        return size == pageModel.size;
    }

    @Override
    public int hashCode() {
        int result = page;
        result = 31 * result + size;
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PageModel.class.getSimpleName() + "[", "]")
                .add("page=" + page)
                .add("size=" + size)
                .toString();
    }
}
