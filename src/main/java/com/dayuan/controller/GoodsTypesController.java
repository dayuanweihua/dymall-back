package com.dayuan.controller;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dayuan.constant.ConstantCode;
import com.dayuan.service.GoodsTypesService;
import com.dayuan.vo.GoodsTypesVo;
import com.dayuan.vo.ResultVo;

@Controller
@RequestMapping("/goods")
public class GoodsTypesController {
	private static Logger logger = LoggerFactory.getLogger(UserInfoController.class);

	@Resource
	private GoodsTypesService goodsTypesService;

	@RequestMapping(value = "/getGoodsTypes.shtml", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResultVo getGoodsTypes() {
		ResultVo resultVo = null;
		try {
//			List<GoodsTypesVo> goodsTypeVos =null;
//			Object object=redisTemplate
//			RedisTemplate<K, V>
			List<GoodsTypesVo> goodsTypeVos = goodsTypesService.getTypes(0);
			resultVo = new ResultVo(ConstantCode.SUCCESS.getMsg(),goodsTypeVos);		
			return resultVo;
		} catch (Exception e) {
			resultVo = new ResultVo(ConstantCode.FAIL.getCode(),ConstantCode.FAIL.getMsg());
			logger.error("前端获取商品类目失败" + e.getMessage());
			return resultVo;			
		}
	}	
}
