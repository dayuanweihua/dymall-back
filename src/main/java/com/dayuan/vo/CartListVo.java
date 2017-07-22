package com.dayuan.vo;

import java.util.ArrayList;
import java.util.List;

public class CartListVo {

	private Long id;
	private Long userId;
	private Long cartId;
	private List<CartInfoVo> cartInfoVos=new ArrayList<CartInfoVo>();;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getCartId() {
		return cartId;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}

	public List<CartInfoVo> getCartInfoVos() {
		return cartInfoVos;
	}

	public void setCartInfoVos(List<CartInfoVo> cartInfoVos) {
		this.cartInfoVos = cartInfoVos;
	}

	public class CartInfoVo {
		private Long goodsId;
		private Integer buyNumber;
		private Integer goodsOriginalPrice;
		private Integer goodsPromotionPrice;
		private String pictureUrl;
		private String goodsName;

		public String getGoodsName() {
			return goodsName;
		}

		public void setGoodsName(String goodsName) {
			this.goodsName = goodsName;
		}

		public Long getGoodsId() {
			return goodsId;
		}

		public void setGoodsId(Long goodsId) {
			this.goodsId = goodsId;
		}

		public Integer getBuyNumber() {
			return buyNumber;
		}

		public void setBuyNumber(Integer buyNumber) {
			this.buyNumber = buyNumber;
		}

		public Integer getGoodsOriginalPrice() {
			return goodsOriginalPrice;
		}

		public void setGoodsOriginalPrice(Integer goodsOriginalPrice) {
			this.goodsOriginalPrice = goodsOriginalPrice;
		}

		public Integer getGoodsPromotionPrice() {
			return goodsPromotionPrice;
		}

		public void setGoodsPromotionPrice(Integer goodsPromotionPrice) {
			this.goodsPromotionPrice = goodsPromotionPrice;
		}

		public String getPictureUrl() {
			return pictureUrl;
		}

		public void setPictureUrl(String pictureUrl) {
			this.pictureUrl = pictureUrl;
		}

	}

}
