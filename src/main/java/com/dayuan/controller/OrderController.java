package com.dayuan.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dayuan.bean.GoodsStore;
import com.dayuan.bean.Order;
import com.dayuan.bean.OrderInfo;
import com.dayuan.bean.UserInfo;
import com.dayuan.constant.ConstantCode;
import com.dayuan.exception.StoreException;
import com.dayuan.service.GoodsStoreService;
import com.dayuan.service.OrderService;
import com.dayuan.vo.ResultVo;

@Controller
@RequestMapping("/order")
public class OrderController {
	private static Logger logger = LoggerFactory.getLogger(UserInfoController.class);

	@Resource
	private OrderService orderService;

	@RequestMapping(value = "/getOrdersByParams.shtml", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	/**
	 * 商品详情接口
	 * 
	 * @param goodId
	 * @return
	 */
	public ResultVo getOrdersByParams(HttpSession session, Integer orderStatus) {
		ResultVo resultVo = null;
		/*
		 * if(orderStatus==null||"".equals(orderStatus)){ resultVo = new
		 * ResultVo(ConstantCode.FAIL.getCode(),"订单号不能为空"); return resultVo; }
		 */
		try {
			System.out.println(orderStatus);
			UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
			if (userInfo != null) {
				List<Order> orders = orderService.selectOrdersByParams(orderStatus, userInfo.getId());
				resultVo = new ResultVo("获取商品详情成功", orders);
				return resultVo;
			} else {
				resultVo = new ResultVo(ConstantCode.FAIL.getCode(), "用户未登录");
				return resultVo;
			}

		} catch (Exception e) {
			if (resultVo != null) {
				resultVo.setCode(ConstantCode.FAIL.getCode());
				resultVo.setMsg("获取商品详情失败");
			}
			logger.error("前端获取商品类目失败" + e.getMessage());
			e.printStackTrace();
			return resultVo;
		}
	}

	@RequestMapping(value = "/getOrderInfo.shtml", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResultVo getOrderInfo(Long orderId) {
		ResultVo resultVo = null;
		try {
			if (orderId == null || "".equals(orderId)) {
				resultVo = new ResultVo(ConstantCode.PARAM_EMPTY.getCode(), "订单号不能为空");
				return resultVo;
			}
			List<OrderInfo> orderInfos = orderService.selectOrderInfoByOrderId(orderId);
			if (orderInfos == null) {
				resultVo = new ResultVo(ConstantCode.PARAM_ERROR.getCode(), "订单号错误");
				return resultVo;
			}
			resultVo = new ResultVo("获取订单详情成功", orderInfos);
			return resultVo;
		} catch (Exception e) {
			resultVo = new ResultVo(ConstantCode.FAIL.getCode(), "出现异常");
			logger.error("前端获取商品类目失败" + e.getMessage());
			e.printStackTrace();
			return resultVo;
		}
	}

	@Resource
	private GoodsStoreService goodsStoreService;

	/**
	 * 下单接口
	 * 
	 * @param session
	 * @param orderInfos
	 * @param order
	 * @return
	 */
	@RequestMapping(value = "/placeOrder.shtml", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResultVo placeOrder(HttpSession session,List<OrderInfo> orderInfos) {
		ResultVo resultVo = null;
		List<Object> list = new ArrayList<Object>();
		try {

			for (OrderInfo orderInfo : orderInfos) {
				GoodsStore goodsStore=goodsStoreService.selectGoodsStoreById(orderInfo.getGoodsId());
				if(goodsStore==null){
					list.add(orderInfo.getGoodsId()+"商品不存在");
				}
				if(orderInfo.getGoodsNumber()>goodsStore.getGoodsStore()){
					list.add(orderInfo.getGoodsId()+"商品库存不足");
				}
				list.add(orderInfo.getGoodsId()+"");
			}
			resultVo = new ResultVo("用户下单成功", list);
			return resultVo;
		} catch (Exception e) {
			resultVo = new ResultVo(ConstantCode.FAIL.getCode(), "出现异常");
			logger.error("前端获取商品类目失败" + e.getMessage());
			e.printStackTrace();
			return resultVo;
		}
	}

	/**
	 * 付款接口
	 * 
	 * @param session
	 * @param orderInfos
	 * @param order
	 * @return
	 */
	
	@RequestMapping(value = "/submitOrder.shtml", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResultVo submitOrder(HttpSession session,Long goodsId,Integer goodsNumber,Order order) {
		ResultVo resultVo = null;
		try {
			if(goodsId==null||goodsNumber==null||order==null){
				return new ResultVo(ConstantCode.PARAM_EMPTY.getCode(),"参数不能为空");
			}
			
//			GoodsStore goodsStore=goodsStoreService.selectGoodsStoreById(goodsId);
//			if(goodsStore==null){
//				return new ResultVo(ConstantCode.FAIL.getCode(),"商品不存在");
//			}
//			if(goodsStore.getGoodsStore()<goodsNumber){
//				return new ResultVo(ConstantCode.FAIL.getCode(),"商品库存不足");
//			}
			UserInfo userInfo=(UserInfo)session.getAttribute("userInfo");
			if(userInfo==null){
				return new ResultVo(ConstantCode.FAIL.getCode(),"请先登陆");
			}
			Long userId=userInfo.getId();
			
			orderService.submitOrder(goodsId,goodsNumber,order,userId);
			
			resultVo = new ResultVo("用户下单成功", null);
			return resultVo;
		} catch (StoreException e) {
			resultVo = new ResultVo(e.getConstantCode().getCode(), e.getConstantCode().getMsg());
			logger.error(e.getConstantCode().printMsg());
			e.printStackTrace();
			return resultVo;
		}
		catch (Exception e) {
			resultVo = new ResultVo(ConstantCode.FAIL.getCode(), "出现异常");
			logger.error("前端获取商品类目失败" + e.getMessage());
			e.printStackTrace();
			return resultVo;
		}
	}

}
