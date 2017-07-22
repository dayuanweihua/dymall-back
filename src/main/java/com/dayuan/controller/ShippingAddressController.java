package com.dayuan.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dayuan.bean.ShippingAddress;
import com.dayuan.bean.UserInfo;
import com.dayuan.constant.ConstantCode;
import com.dayuan.service.GoodsService;
import com.dayuan.service.ShippingAddressService;
import com.dayuan.vo.ResultVo;

/**
 * 用户地址管理controller
 * @author 24725
 *
 */
@Controller
@RequestMapping("/addressManager")
public class ShippingAddressController {
	private static Logger logger = LoggerFactory.getLogger(UserInfoController.class);

	@Resource
	private ShippingAddressService shippingAddressService;

	@Resource
	private GoodsService goodsService;

	/**
	 *增加用户收货地址
	 * @param session
	 * @param shippingAddress
	 * @return
	 */
	@RequestMapping(value = "/addShippingAddress.shtml", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResultVo addShippingAddress(HttpSession session, ShippingAddress shippingAddress) {
		ResultVo resultVo = null;
		try {
			if (shippingAddress == null) {
				return new ResultVo(ConstantCode.PARAM_EMPTY.getCode(), ConstantCode.PARAM_EMPTY.getMsg());
			}
			UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
			List<ShippingAddress> shippingAddressList=shippingAddressService.selectShippingAddress(userInfo.getId());
			if(shippingAddressList.size()>0){
				shippingAddress.setIsDefault(0);
			}else{
				shippingAddress.setIsDefault(1);
			}
			shippingAddress.setUserId(userInfo.getId());
			int code=shippingAddressService.insertShippingAddress(shippingAddress);
			if (code != 1) {
				return new ResultVo(ConstantCode.DBEXCEPTION.getCode(), ConstantCode.DBEXCEPTION.getMsg());
			}
			resultVo = new ResultVo(ConstantCode.SUCCESS.getMsg(), ConstantCode.SUCCESS.getCode());
			return resultVo;
		} catch (Exception e) {
			resultVo = new ResultVo(ConstantCode.FAIL.getCode(), ConstantCode.FAIL.getMsg());
			e.printStackTrace();
			logger.error(ConstantCode.FAIL.printMsg());
			return resultVo;
		}
	}

	/**
	 * 删除用户选择的收货地址
	 * @param session
	 * @param shippingAddressId
	 * @return
	 */
	@RequestMapping(value = "/deleteShippingAddress.shtml", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResultVo deleteShippingAddress(HttpSession session,Long shippingAddressId) {
		ResultVo resultVo = null;
		try {
			if (shippingAddressId == null || "".equals(shippingAddressId)) {
				return new ResultVo(ConstantCode.PARAM_EMPTY.getCode(), ConstantCode.PARAM_EMPTY.getMsg());
			}
			UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
			int code = shippingAddressService.deleteShippingAddress(userInfo.getId(), shippingAddressId);
			if (code != 1) {
				return new ResultVo(ConstantCode.DBEXCEPTION.getCode(), ConstantCode.DBEXCEPTION.getMsg());
			}
			resultVo = new ResultVo(ConstantCode.SUCCESS.getMsg(), null);
			return resultVo;
		} catch (Exception e) {
			resultVo = new ResultVo(ConstantCode.EXCEPTION.getCode(), ConstantCode.EXCEPTION.getMsg());
			logger.error(ConstantCode.EXCEPTION.printMsg());
			e.printStackTrace();
			return resultVo;
		}
	}
	
	/**
	 * 更新用户选择的收货地址
	 * 注：具体的一个收货地址的id，前端必须要传过来，这样服务端才能定位到时那一个具体的收货地址
	 * @param session
	 * @param shippingAddress
	 * @return
	 */
	@RequestMapping(value = "/updateShippingAddress.shtml", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResultVo updateShippingAddress(HttpSession session,ShippingAddress shippingAddress) {
		ResultVo resultVo = null;

		try {
			if (shippingAddress == null) {
				return new ResultVo(ConstantCode.PARAM_EMPTY.getCode(), ConstantCode.PARAM_EMPTY.getMsg());
			}
			UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
			shippingAddress.setUserId(userInfo.getId());
			int code = shippingAddressService.updateShippingAddress(shippingAddress);
			if (code != 1) {
				return new ResultVo(ConstantCode.DBEXCEPTION.getCode(), ConstantCode.DBEXCEPTION.getMsg());
			}
			resultVo = new ResultVo(ConstantCode.SUCCESS.getMsg(), null);
			return resultVo;
		} catch (Exception e) {
			resultVo = new ResultVo(ConstantCode.EXCEPTION.getCode(), ConstantCode.EXCEPTION.getMsg());
			logger.error(ConstantCode.EXCEPTION.printMsg());
			e.printStackTrace();
			return resultVo;
		}
	}

	
	/**
	 * 查询用户的收货地址
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/checkShippingAddress.shtml", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResultVo checkShippingAddress(HttpSession session) {
		ResultVo resultVo = null;
		try {
			UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
			List<ShippingAddress> shippingAddressList=shippingAddressService.selectShippingAddress(userInfo.getId());
			return new ResultVo(ConstantCode.SUCCESS.getMsg(),shippingAddressList);
		} catch (Exception e) {
			logger.error(ConstantCode.EXCEPTION.printMsg());
			resultVo = new ResultVo(ConstantCode.EXCEPTION.getCode(), ConstantCode.EXCEPTION.getMsg());
			e.printStackTrace();
			return resultVo;
		}
	}

	
	

}
