package simonxyzjz.phdfms.mongo.repository;

import java.io.Serializable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class MyPageable implements Serializable, Pageable {
	private static final long serialVersionUID = 1;
	private Integer number = 1;
	private Integer size = 10;
	private Sort sort;

	@Override
	public int getPageNumber() {
		return getNumber();
	}

	@Override
	public int getPageSize() {
		return getSize();
	}

	@Override
	public int getOffset() {
		return (getNumber() - 1) * getSize();
	}

	@Override
	public Sort getSort() {
		return sort;
	}

	public void setSort(Sort sort) {
		this.sort = sort;
	}

	@Override
	public Pageable first() {
		return null;
	}

	@Override
	public boolean hasPrevious() {
		return false;
	}

	@Override
	public Pageable next() {
		return null;
	}

	@Override
	public Pageable previousOrFirst() {
		return null;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}
}