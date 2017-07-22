package com.dayuan.vo;

import java.util.List;

public class GoodsTypesVo {
	private int id;
	private String name;
	private int pid;
	private List<GoodsTypesVo> subList;
	
	public GoodsTypesVo(int id,String name,int pid,List<GoodsTypesVo> subList){
		this.id=id;
		this.name=name;
		this.pid=pid;
		this.subList=subList;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public List<GoodsTypesVo> getSubList() {
		return subList;
	}
	public void setSubList(List<GoodsTypesVo> subList) {
		this.subList = subList;
	}
		
}
