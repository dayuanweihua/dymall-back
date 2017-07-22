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

import com.dayuan.bean.Cart;
import com.dayuan.bean.CartInfo;
import com.dayuan.bean.Goods;
import com.dayuan.bean.UserInfo;
import com.dayuan.constant.ConstantCode;
import com.dayuan.service.CartService;
import com.dayuan.service.GoodsService;
import com.dayuan.vo.CartListVo;
import com.dayuan.vo.ResultVo;

@Controller
@RequestMapping("/cart")
public class CartController {
	private static Logger logger = LoggerFactory.getLogger(UserInfoController.class);

	@Resource
	private CartService cartService;

	@Resource
	private GoodsService goodsService;

	@RequestMapping(value = "/checkCartInfos.shtml", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	/**
	 * 
	 * @param goodId
	 * @return
	 */
	public ResultVo checkCartInfos(HttpSession session) {
		ResultVo resultVo = null;
		try {
			UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
			Cart cart = cartService.selectCart(userInfo.getId());
			CartListVo cartListVo = new CartListVo();
			cartListVo.setCartId(cart.getId());
			cartListVo.setUserId(userInfo.getId());
			List<CartInfo> cartInfos = cartService.selectCartInfos(cart.getId());
			for (CartInfo cartInfo : cartInfos) {
				CartListVo.CartInfoVo cartInfoVo = cartListVo.new CartInfoVo();
				cartInfoVo.setGoodsId(cartInfo.getGoodsId());
				cartInfoVo.setBuyNumber(cartInfo.getBuyNumber());
				cartInfoVo.setGoodsOriginalPrice(cartInfo.getGoodsOriginalPrice());
				cartInfoVo.setGoodsPromotionPrice(cartInfo.getGoodsPromotionPrice());
				cartInfoVo.setPictureUrl(cartInfo.getPictureUrl());
				cartInfoVo.setGoodsName(cartInfo.getGoodsName());
				cartListVo.getCartInfoVos().add(cartInfoVo);

			}

			return new ResultVo(ConstantCode.SUCCESS.getMsg(), cartListVo);
		} catch (Exception e) {
			logger.error(ConstantCode.EXCEPTION.printMsg());
			resultVo = new ResultVo(ConstantCode.EXCEPTION.getCode(), ConstantCode.EXCEPTION.getMsg());
			e.printStackTrace();
			return resultVo;
		}
	}

	@RequestMapping(value = "/addToCartInfo.shtml", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	/**
	 * 
	 * @param goodsName
	 * @return
	 */
	public ResultVo addToCartInfo(HttpSession session, Long goodsId, Integer buyNumber) {
		ResultVo resultVo = null;
		try {
			if (goodsId == null || "".equals(goodsId) || buyNumber == null || "".equals(buyNumber)) {
				return new ResultVo(ConstantCode.PARAM_EMPTY.getCode(), ConstantCode.PARAM_EMPTY.getMsg());
			}
			UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
			Cart cart = cartService.selectCart(userInfo.getId());
			if (cart == null) {
				cartService.insertCart(userInfo.getId());
			}
//			List<CartInfo> cartInfos = cartService.selectCartInfos(cart.getId());
			
//			List<Long> goodsIds=new ArrayList<Long>();
//			for (CartInfo cartI: cartInfos) {
//				goodsIds.add(cartI.getGoodsId());				
//				if (cartI.getGoodsId().intValue() == goodsId.intValue()) {
//					cartService.updateCartInfo(cartI.getBuyNumber() + buyNumber, cart.getId(), goodsId);
//				} else {
					Goods goods = goodsService.selectGoodByGoodId(goodsId);
					CartInfo cartInfo = new CartInfo();
					cartInfo.setCartId(cart.getId());
					cartInfo.setGoodsId(goodsId);
					cartInfo.setGoodsName(goods.getGoodsName());
					cartInfo.setPictureUrl(goods.getPictureUrl());
					cartInfo.setBuyNumber(buyNumber);
					cartInfo.setGoodsOriginalPrice(goods.getOriginalPrice());
					cartInfo.setGoodsPromotionPrice(goods.getPromotionPrice());
					int code = cartService.insertCartInfo(cartInfo);
					if (code != 1) {
						return new ResultVo(ConstantCode.DBEXCEPTION.getCode(), ConstantCode.DBEXCEPTION.getMsg());
					}
//				}
//			}

			resultVo = new ResultVo(ConstantCode.SUCCESS.getMsg(), ConstantCode.SUCCESS.getCode());
			return resultVo;
		} catch (Exception e) {
			resultVo = new ResultVo(ConstantCode.FAIL.getCode(), ConstantCode.FAIL.getMsg());
			e.printStackTrace();
			logger.error(ConstantCode.FAIL.printMsg());
			return resultVo;
		}
	}

	@RequestMapping(value = "/updateCartInfo.shtml", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	/**
	 * 
	 * 
	 * @param
	 * @return
	 */
	public ResultVo updateCartInfo(HttpSession session, Integer changeNumber, Long goodsId) {
		ResultVo resultVo = null;

		try {
			if (changeNumber == null || "".equals(changeNumber) || goodsId == null || "".equals(goodsId)) {
				return new ResultVo(ConstantCode.PARAM_EMPTY.getCode(), ConstantCode.PARAM_EMPTY.getMsg());
			}
			UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
			Cart cart = cartService.selectCart(userInfo.getId());
			int code = cartService.updateCartInfo(changeNumber, cart.getId(), goodsId);
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

	@RequestMapping(value = "/deleteCartInfo.shtml", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	/**
	 * 
	 * 
	 * @param
	 * @return
	 */
	public ResultVo deleteCartInfo(HttpSession session, Long goodsId) {
		ResultVo resultVo = null;

		try {
			if (goodsId == null || "".equals(goodsId)) {
				return new ResultVo(ConstantCode.PARAM_EMPTY.getCode(), ConstantCode.PARAM_EMPTY.getMsg());
			}
			UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");			
			Cart cart = cartService.selectCart(userInfo.getId());
			int code = cartService.deleteCartInfoById(cart.getId(), goodsId);
			if (code < 1) {
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

}
