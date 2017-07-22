package com.dayuan.vo;

import com.dayuan.constant.ConstantCode;

/**
 * Ajax传输对象（通用）
 * 
 * @author sam
 *
 */
public class ResultVo {
	/**
	 * 状态码
	 */
	private Integer code;
	private String msg;
	private Object data;
	
	/**
	 * 针对错误情况的构造函数，有状态码和错误信息。
	 * @param code
	 * @param msg
	 */
	public ResultVo(Integer code,String msg){
		this.code=code;
		this.msg=msg;
	}
	/**
	 * 针对成功情况的构造函数，状态码固定为1，有成功信息和前端所需数据。
	 * @param code
	 * @param msg
	 * @param data
	 */
	public ResultVo(String msg,Object data){
		code=ConstantCode.SUCCESS.getCode();
		this.msg=msg;
		this.data=data;
	}
	
	public ResultVo(){}
	
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
