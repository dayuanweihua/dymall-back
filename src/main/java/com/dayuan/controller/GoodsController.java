package com.dayuan.controller;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dayuan.bean.Goods;
import com.dayuan.constant.ConstantCode;
import com.dayuan.service.GoodsService;
import com.dayuan.vo.ResultVo;

@Controller
@RequestMapping("/goods")
public class GoodsController {
	private static Logger logger = LoggerFactory.getLogger(UserInfoController.class);

	@Resource
	private GoodsService goodsService;

	@RequestMapping(value = "/getGoodsList.shtml", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	/**
	 * 商品列表接口
	 * @param goodsName
	 * @return
	 */
	public ResultVo getGoodsList(String goodsName) {
		ResultVo resultVo = null;
		
		try {
			List<Goods> goodsTypeVos = goodsService.selectGoodByParam(goodsName);
			resultVo = new ResultVo("获取商品清单成功",goodsTypeVos);
			return resultVo;
		} catch (Exception e) {
			if (resultVo != null) {
				resultVo = new ResultVo(ConstantCode.FAIL.getCode(),"获取商品类目失败");
			}
			logger.error("前端获取商品类目失败" + e.getMessage());
			return resultVo;
		}
	}

	@RequestMapping(value = "/getGoodsById.shtml", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	/**
	 * 商品详情接口
	 * @param goodId
	 * @return
	 */
	public ResultVo getGoodsById(Long goodsId){
		ResultVo resultVo = null;		
		if(goodsId==null||"".equals(goodsId)){
			resultVo = new ResultVo(ConstantCode.FAIL.getCode(),"商品id不能为空");
			return resultVo;
		}		
		try {
			Goods goods = goodsService.selectGoodByGoodId(goodsId);
				
			resultVo = new ResultVo("获取商品详情成功",goods);
			return resultVo;
		} catch (Exception e) {
			if (resultVo != null) {
				resultVo.setCode(ConstantCode.FAIL.getCode());
				resultVo.setMsg("获取商品详情失败");
			}
			logger.error("前端获取商品类目失败" + e.getMessage());
			System.out.println(e.getMessage());
			return resultVo;
		}
	}

}
