package com.hncis.businessCard.dao.impl;

import java.util.List;

import com.hncis.businessCard.dao.BusinessCardDao;
import com.hncis.businessCard.vo.BgabGascba01;
import com.hncis.businessCard.vo.BgabGascba02;
import com.hncis.businessCard.vo.BgabGascba03;
import com.hncis.common.dao.CommonDao;

public class BusinessCardDaoImplByIBatis extends CommonDao implements BusinessCardDao{
	private final String SELECT_INFO_BC_TO_REQUEST                 = "selectInfoBcToRequest";
	private final String SELECT_INFO_BC_TO_DEFAULT_REQUEST         = "selectInfoBcToDefaulRequest";
	private final String SELECT_INFO_BC_TO_BPM_REQUEST		       = "selectInfoBcToBpmRequest";
	private final String INSERT_INFO_BC_TO_REQUEST_1               = "insertInfoBcToReqeust_1";
	private final String INSERT_INFO_BC_TO_REQUEST_2               = "insertInfoBcToReqeust_2";
	private final String DELETE_INFO_BC_TO_REQUEST                 = "deleteInfoBcToRequest";
	private final String UPDATE_INFO_BC_TO_REQUEST                 = "updateInfoBcToRequest";
	
	private final String SELECT_COUNT_BC_TO_ACCEPT                 = "selectCountBcToAccept";
	private final String SELECT_LIST_BC_TO_ACCEPT                  = "selectListBcToAccept";
	private final String DELETE_LIST_BC_TO_ACCEPT                  = "deleteListBcToAccept";
	
	private final String UPDATE_LIST_BC_TO_ACCEPT_BY_REJECT        = "updateListBcToAcceptByReject";
	private final String UPDATE_LIST_BC_TO_ACCEPT_BY_CONFIRM1      = "updateListBcToAcceptByConfirm1";
	private final String UPDATE_LIST_BC_TO_ACCEPT_BY_CONFIRMCANCEL = "updateListBcToAcceptByConfirmCancel";
	private final String UPDATE_LIST_BC_TO_ACCEPT_BY_CONFIRM2      = "updateListBcToAcceptByConfirm2";
	private final String UPDATE_LIST_BC_TO_ACCEPT_BY_ISSUE         = "updateListBcToAcceptByIssue";
	private final String SELECT_BC_TO_SUBMIT                       = "selectBcToSubmit";
	private final String SELECT_APPROVAL_INFO_BY_BC                = "selectApprovalInfoByBc";
	private final String SELECT_COUNT_BC_TO_CARD_TYPE_MANAGEMENT   = "selectCountBcToCardTypeManagement";
	private final String SELECT_LIST_BC_TO_CARD_TYPE_MANAGEMENT    = "selectListBcToCardTypeManagement";
	private final String INSERT_LIST_BC_TO_CARD_TYPE_MANAGEMENT    = "insertListBcToCardTypeManagement";
	private final String UPDATE_LIST_BC_TO_CARD_TYPE_MANAGEMENT    = "updateListBcToCardTypeManagement";
	private final String DELETE_LIST_BC_TO_CARD_TYPE_MANAGEMENT    = "deleteListBcToCardTypeManagement";
	private final String UPDATE_BUSINESS_CARD_PO_INFO 			   = "updateBusunessCardPoInfo";
	private final String SELECT_BUSINESS_CARD_REJECT_SUBMIT_PO_SEARCH = "selectBusinessCardRejectSubmitPoSearch";
	private final String UPDATE_INFO_BC_TO_REJECT                  = "updateInfoBcToReject";
	
	private final String SELECT_COUNT_BC_TO_CONFIRM                 = "selectCountBcToConfirm";
	private final String SELECT_LIST_BC_TO_CONFIRM                  = "selectListBcToConfirm";
	
	/**
	 * request search
	 * @param BgabGascba01
	 * @return
	 */
	public BgabGascba01 getSelectInfoBCToRequest(BgabGascba02 keyParamDto){
		return (BgabGascba01)getSqlMapClientTemplate().queryForObject(SELECT_INFO_BC_TO_REQUEST, keyParamDto);
	}
	public BgabGascba01 getSelectInfoBCToDefaultRequest(BgabGascba02 keyParamDto){
		return (BgabGascba01)getSqlMapClientTemplate().queryForObject(SELECT_INFO_BC_TO_DEFAULT_REQUEST, keyParamDto);
	}
	public BgabGascba01 getSelectInfoBCToBpmRequest(BgabGascba02 keyParamDto){
		return (BgabGascba01)getSqlMapClientTemplate().queryForObject(SELECT_INFO_BC_TO_BPM_REQUEST, keyParamDto);
	}
	
	/**
	 * request apply
	 * @param BgabGascba01
	 * @return
	 */
	public Object insertInfoBCToRequest_1(BgabGascba01 cgabGascba01){
		return super.insert(INSERT_INFO_BC_TO_REQUEST_1, cgabGascba01);
	}
	public Object insertInfoBCToRequest_2(BgabGascba01 cgabGascba01){
		return super.insert(INSERT_INFO_BC_TO_REQUEST_2, cgabGascba01);
	}
	
	/**
	 * request delete
	 * @param cgabGascba01
	 * @return
	 */
	public Object deleteInfoBCToRequest(BgabGascba02 keyParamDto){
		return super.delete(DELETE_INFO_BC_TO_REQUEST, keyParamDto);
	}
	
	/**
	 * request approve/confirm/confirm cancel
	 * @param keyParamDto
	 * @return
	 */
	public Object updateInfoBCToRequest(BgabGascba02 keyParamDto){
		return super.update(UPDATE_INFO_BC_TO_REQUEST, keyParamDto);
	}
	
	/**
	 * accept count
	 * accept search
	 * @param cgabGascba01
	 * @return
	 */
	public String getSelectCountBCToAccept(BgabGascba02 keyParamDto){
		return (String)getSqlMapClientTemplate().queryForObject(SELECT_COUNT_BC_TO_ACCEPT, keyParamDto);
	}
	@SuppressWarnings("unchecked")
	public List<BgabGascba01> getSelectListBCToAccept(BgabGascba02 keyParamDto){
		return getSqlMapClientTemplate().queryForList(SELECT_LIST_BC_TO_ACCEPT, keyParamDto);
	}
	
	/**
	 * accept delete
	 * @param cgabGascba01
	 * @return
	 */
	public Object deleteListBCToAccept(List<BgabGascba02> keyParamDto){
		return super.delete(DELETE_LIST_BC_TO_ACCEPT, keyParamDto);
	}
	
	/**
	 * accept return
	 * @param cgabGascba01
	 * @return
	 */
	public Object updateListBCToAcceptByReject(List<BgabGascba02> keyParamDto){
		return super.update(UPDATE_LIST_BC_TO_ACCEPT_BY_REJECT, keyParamDto);
	}
	
	/**
	 * accept confirm
	 * @param cgabGascba01
	 * @return
	 */
	public Object updateListBCToAcceptByConfirm1(List<BgabGascba02> keyParamDto){
		return super.update(UPDATE_LIST_BC_TO_ACCEPT_BY_CONFIRM1 , keyParamDto);
	}
	
	/**
	 * accept confirmCancel
	 * @param cgabGascba01
	 * @return
	 */
	public Object updateListBCToAcceptByConfirmCancel(List<BgabGascba02> keyParamDto){
		return super.update(UPDATE_LIST_BC_TO_ACCEPT_BY_CONFIRMCANCEL  , keyParamDto);
	}
	
	/**
	 * accept confirmAll
	 * @param cgabGascba01
	 * @return
	 */
	public Object updateListBCToAcceptByConfirm2(List<BgabGascba02> keyParamDto){
		return super.update(UPDATE_LIST_BC_TO_ACCEPT_BY_CONFIRM2  , keyParamDto);
	}
	
	/**
	 * accept issue
	 * @param cgabGascba01
	 * @return
	 */
	public Object updateListBCToAcceptByIssue(List<BgabGascba02> keyParamDto){
		return super.update(UPDATE_LIST_BC_TO_ACCEPT_BY_ISSUE  , keyParamDto);
	}
	
	/**
	 * submit search
	 * @param keyParamDto
	 * @return
	 */
	public BgabGascba01 getSelectInfoBCToSubmit(BgabGascba02 keyParamDto){
		return (BgabGascba01)getSqlMapClientTemplate().queryForObject(SELECT_BC_TO_SUBMIT, keyParamDto);
	}
	
	public String getSelectApprovalInfo(BgabGascba02 keyParamDto){
		return (String)getSqlMapClientTemplate().queryForObject(SELECT_APPROVAL_INFO_BY_BC, keyParamDto);
	}
	public String getSelectCountBcToCardTypeManagement(BgabGascba03 vo){
		return (String)getSqlMapClientTemplate().queryForObject(SELECT_COUNT_BC_TO_CARD_TYPE_MANAGEMENT, vo);
	}
	@SuppressWarnings("unchecked")
	public List<BgabGascba03> getSelectListBcToCardTypeManagement(BgabGascba03 vo){
		return getSqlMapClientTemplate().queryForList(SELECT_LIST_BC_TO_CARD_TYPE_MANAGEMENT, vo);
	}
	public int insertListBcToCardTypeManagement(List<BgabGascba03> list){		
		return super.insert(INSERT_LIST_BC_TO_CARD_TYPE_MANAGEMENT, list);
	}
	public int updateListBcToCardTypeManagement(List<BgabGascba03> list){		
		return super.update(UPDATE_LIST_BC_TO_CARD_TYPE_MANAGEMENT, list);
	}
	public int deleteListBcToCardTypeManagement(List<BgabGascba03> list){		
		return super.delete(DELETE_LIST_BC_TO_CARD_TYPE_MANAGEMENT, list);
	}
	public int updateBusunessCardPoInfo(BgabGascba02 dto){
		return update(UPDATE_BUSINESS_CARD_PO_INFO, dto);
	}
	public BgabGascba02 getSelectBusinessCardRejectSubmitPoSearch(BgabGascba02 dto){
		return (BgabGascba02)getSqlMapClientTemplate().queryForObject(SELECT_BUSINESS_CARD_REJECT_SUBMIT_PO_SEARCH, dto);
	}
	
	public int updateInfoBcToReject(BgabGascba02 dto){
		return update(UPDATE_INFO_BC_TO_REJECT, dto);
	}
	
	public String getSelectCountBCToConfirm(BgabGascba02 keyParamDto){
		return (String)getSqlMapClientTemplate().queryForObject(SELECT_COUNT_BC_TO_CONFIRM, keyParamDto);
	}
	@SuppressWarnings("unchecked")
	public List<BgabGascba01> getSelectListBCToConfirm(BgabGascba02 keyParamDto){
		return getSqlMapClientTemplate().queryForList(SELECT_LIST_BC_TO_CONFIRM, keyParamDto);
	}
}



