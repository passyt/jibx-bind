package org.jibx.binding.test4;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Passyt
 *
 */
public class TestMapping extends Mapping {

	private String value;
	private String name;
	private String age;
	private List<String> list1 = new ArrayList<String>();
	private List<String> list2 = new ArrayList<String>();
	private List<Item> list3 = new ArrayList<Item>();

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public List<String> getList1() {
		return list1;
	}

	public void setList1(List<String> list1) {
		this.list1 = list1;
	}

	public List<String> getList2() {
		return list2;
	}

	public void setList2(List<String> list2) {
		this.list2 = list2;
	}

	public List<Item> getList3() {
		return list3;
	}

	public void setList3(List<Item> list3) {
		this.list3 = list3;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((age == null) ? 0 : age.hashCode());
		result = prime * result + ((list1 == null) ? 0 : list1.hashCode());
		result = prime * result + ((list2 == null) ? 0 : list2.hashCode());
		result = prime * result + ((list3 == null) ? 0 : list3.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestMapping other = (TestMapping) obj;
		if (age == null) {
			if (other.age != null)
				return false;
		} else if (!age.equals(other.age))
			return false;
		if (list1 == null) {
			if (other.list1 != null)
				return false;
		} else if (!list1.equals(other.list1))
			return false;
		if (list2 == null) {
			if (other.list2 != null)
				return false;
		} else if (!list2.equals(other.list2))
			return false;
		if (list3 == null) {
			if (other.list3 != null)
				return false;
		} else if (!list3.equals(other.list3))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TestMapping [value=" + value + ", name=" + name + ", age=" + age + ", list1=" + list1 + ", list2=" + list2 + ", list3=" + list3 + "]";
	}

}
