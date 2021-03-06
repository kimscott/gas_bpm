package com.hncis.controller.gift;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.hncis.common.Constant;
import com.hncis.common.application.SessionInfo;
import com.hncis.common.base.JSONBaseArray;
import com.hncis.common.base.JSONBaseVO;
import com.hncis.common.exception.impl.HncisException;
import com.hncis.common.exception.impl.SessionException;
import com.hncis.common.manager.CommonManager;
import com.hncis.common.message.HncisMessageSource;
import com.hncis.common.util.BpmApiUtil;
import com.hncis.common.util.StringUtil;
import com.hncis.common.vo.BgabGascZ011Dto;
import com.hncis.common.vo.CommonApproval;
import com.hncis.common.vo.CommonList;
import com.hncis.common.vo.CommonMessage;
import com.hncis.controller.AbstractController;
import com.hncis.gift.manager.GiftManager;
import com.hncis.gift.vo.BgabGascgf01Dto;
import com.hncis.gift.vo.BgabGascgf02Dto;
import com.hncis.gift.vo.BgabGascgf03Dto;
import com.hncis.gift.vo.BgabGascgf04Dto;
import com.hncis.gift.vo.BgabGascgf05Dto;
import com.hncis.pickupService.vo.BgabGascps01Dto;
import com.hncis.training.vo.BgabGasctr01;

@Controller
public class GiftController extends AbstractController{

	private static final String strStart = "Start time : ";
	private static final String strEnd = "End time : ";
	private static final String strDateFormat = "yyyy-MM-dd HH:mm:ss.SSS";

	@Autowired
	@Qualifier("giftManagerImpl")
	private GiftManager giftManager;

	@Autowired
	@Qualifier("commonManagerImpl")
	private CommonManager commonManager;


	/**
	 *  대분류 콤보 데이터 조회
	 *
	 * @param req the req
	 * @param res the res
	 * @return the model and view
	 * @throws Exception the exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/hncis/gift/doSearchGfToLrgCombo.do")
	public ModelAndView doSearchGfToLrgCombo(HttpServletRequest req, HttpServletResponse res,
			@RequestParam(value="paramJson", required=true) String paramJson) throws Exception{

		BgabGascgf03Dto vo = (BgabGascgf03Dto)getJsonToBean(paramJson, BgabGascgf03Dto.class);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(DATA_JSON_PAGE);
		modelAndView.addObject("uiType", "ajax");

		try{
	 		JSONBaseVO jso = new JSONBaseVO();
	 		JSONBaseVO json = null;
			JSONBaseArray  jsonArr = null;

			List<BgabGascgf03Dto> codeList = null;

			jsonArr = new JSONBaseArray();

			if(StringUtil.isNullToStrTrm(vo.getS_type()).equals("A")){
				json = new JSONBaseVO();
				json.put("name", HncisMessageSource.getMessage("total"));
				json.put("value", "");
				jsonArr.add(json);
			}else if(StringUtil.isNullToStrTrm(vo.getS_type()).equals("S")){
				json = new JSONBaseVO();
				json.put("name", HncisMessageSource.getMessage("select"));
				json.put("value", "");
				jsonArr.add(json);
			}

			codeList = giftManager.selectGfToLrgCombo(vo);

			for(BgabGascgf03Dto targetBean : codeList){
				json = new JSONBaseVO();
				json.put("value", StringUtil.isNullToStrTrm(targetBean.getLrg_cd()));
				json.put("name", StringUtil.isNullToStrTrm(targetBean.getLrg_nm()));

				jsonArr.add(json);
			}

			jso.put("LRG_COMB", jsonArr);

			modelAndView.addObject(JSON_DATA_KEY, jso.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		return modelAndView;
	}

	/**
	 *  대분류 콤보 데이터 조회
	 *
	 * @param req the req
	 * @param res the res
	 * @return the model and view
	 * @throws Exception the exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/hncis/gift/doSearchGfToMrgCombo.do")
	public ModelAndView doSearchGfToMrgCombo(HttpServletRequest req, HttpServletResponse res,
			@RequestParam(value="paramJson", required=true) String paramJson) throws Exception{

		BgabGascgf04Dto vo = (BgabGascgf04Dto)getJsonToBean(paramJson, BgabGascgf04Dto.class);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(DATA_JSON_PAGE);
		modelAndView.addObject("uiType", "ajax");

		try{
	 		JSONBaseVO jso = new JSONBaseVO();
	 		JSONBaseVO json = null;
			JSONBaseArray  jsonArr = null;

			List<BgabGascgf04Dto> codeList = null;

			jsonArr = new JSONBaseArray();

			if(StringUtil.isNullToStrTrm(vo.getS_type()).equals("A")){
				json = new JSONBaseVO();
				json.put("name", HncisMessageSource.getMessage("total"));
				json.put("value", "");
				jsonArr.add(json);
			}else if(StringUtil.isNullToStrTrm(vo.getS_type()).equals("S")){
				json = new JSONBaseVO();
				json.put("name", HncisMessageSource.getMessage("select"));
				json.put("value", "");
				jsonArr.add(json);
			}

			codeList = giftManager.selectGfToMrgCombo(vo);

			for(BgabGascgf04Dto targetBean : codeList){
				json = new JSONBaseVO();
				//json.put("key", StringUtil.isNullToStrTrm(targetBean.getLrg_cd()));
				json.put("value", StringUtil.isNullToStrTrm(targetBean.getMrg_cd()));
				json.put("name", StringUtil.isNullToStrTrm(targetBean.getMrg_nm()));

				jsonArr.add(json);
			}

			jso.put("MRG_COMB", jsonArr);

			modelAndView.addObject(JSON_DATA_KEY, jso.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		return modelAndView;
	}


	@SuppressWarnings("unchecked")
	@RequestMapping(value="/hncis/gift/doSearchGfToGiftList.do")
	public ModelAndView doSearchGfToGiftList(HttpServletRequest req, HttpServletResponse res,
			@RequestParam(value="paramJson", required=true) String paramJson) throws Exception{

		BgabGascgf01Dto vo = (BgabGascgf01Dto)getJsonToBean(paramJson, BgabGascgf01Dto.class);

		int totCnt = giftManager.selectGfToGiftCount(vo);
		List<BgabGascgf01Dto> rstList = giftManager.selectGfToGiftList(vo);

		JSONBaseVO     jso     = new JSONBaseVO();
 		JSONBaseVO     json    = null;
		JSONBaseArray  jsonArr = new JSONBaseArray();

		if(totCnt > 0){
			for(BgabGascgf01Dto data : rstList){
				json = new JSONBaseVO();
				json.put("file_info"     , data.getFile_info());
				json.put("item_ttl"      , data.getItem_ttl());
				json.put("item_seq"      , data.getItem_seq());
				json.put("item_qty"      , data.getItem_qty());
				jsonArr.add(json);
			}
		}


		//if(rstList.size() > 0){
		//	totCnt = Integer.parseInt(rstList.get(0).getTot_cnt());
		//}

		jso.put("GIFT" , jsonArr);
		jso.put("TOT_CNT" , totCnt);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(DATA_JSON_PAGE);
		modelAndView.addObject(JSON_DATA_KEY, jso.toString());
		modelAndView.addObject("uiType", "ajax");

		return modelAndView;
	}

	@RequestMapping(value="/hncis/gift/doInsertGfToGift.do")
	public ModelAndView doInsertGfToGift(HttpServletRequest req, HttpServletResponse res,
			@RequestParam(value="paramJson" , required=true) String paramJson
			) throws Exception{
		long time = System.currentTimeMillis(); 
		SimpleDateFormat dayTime = new SimpleDateFormat(strDateFormat); 
		String strDT = dayTime.format(new Date(time)); 
		logger.info(strStart + strDT);
		
		BgabGascgf02Dto dto = (BgabGascgf02Dto) getJsonToBean(paramJson, BgabGascgf02Dto.class);
		dto.setIpe_eeno(SessionInfo.getSess_empno(req));
		dto.setUpdr_eeno(SessionInfo.getSess_empno(req));

		CommonMessage message = giftManager.insertGfToGiftRequest(dto);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(DATA_JSON_PAGE);
		modelAndView.addObject(JSON_DATA_KEY, JSONObject.fromObject(message).toString());
		modelAndView.addObject("uiType", "ajax");
		
		time = System.currentTimeMillis(); 
		dayTime = new SimpleDateFormat(strDateFormat); 
		strDT = dayTime.format(new Date(time)); 
		logger.info(strEnd + strDT);

		return modelAndView;
	}
	
	@RequestMapping(value="/hncis/gift/doDeleteGfToRequest.do")
	public ModelAndView doDeleteGfToRequest(HttpServletRequest req, HttpServletResponse res,
			@RequestParam(value="paramJson" , required=true) String paramJson
			) throws Exception{
		BgabGascgf02Dto dto = (BgabGascgf02Dto) getJsonToBean(paramJson, BgabGascgf02Dto.class);

		int rs = giftManager.deleteGfToRequestCancel(dto);
		if(rs>0){
			// BPM API호출
			String processCode = "P-B-003"; 	//프로세스 코드 (선물 프로세스) - 프로세스 정의서 참조
			String bizKey = dto.getDoc_no();	//신청서 번호
			String statusCode = "GASBZ01250010";	//액티비티 코드 (선물신청서작성) - 프로세스 정의서 참조
			String loginUserId = dto.getUpdr_eeno();	//로그인 사용자 아이디
			
			BpmApiUtil.sendDeleteAndRejectTask(processCode, bizKey, statusCode, loginUserId);
				
		}
		CommonMessage message = new CommonMessage();
		
		message.setMessage(HncisMessageSource.getMessage("DELETE.0000"));

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(DATA_JSON_PAGE);
		modelAndView.addObject(JSON_DATA_KEY, JSONObject.fromObject(message).toString());
		modelAndView.addObject("uiType", "ajax");

		return modelAndView;
	}

	@RequestMapping(value="/hncis/gift/doRequestGfToGift.do")
	public ModelAndView doRequestGfToGift(HttpServletRequest req, HttpServletResponse res,
			@RequestParam(value="paramJson" , required=true) String paramJson
			) throws Exception{
		long time = System.currentTimeMillis(); 
		SimpleDateFormat dayTime = new SimpleDateFormat(strDateFormat); 
		String strDT = dayTime.format(new Date(time)); 
		logger.info(strStart + strDT);
		
		BgabGascgf02Dto dto = (BgabGascgf02Dto) getJsonToBean(paramJson, BgabGascgf02Dto.class);
		dto.setIpe_eeno(SessionInfo.getSess_empno(req));
		dto.setUpdr_eeno(SessionInfo.getSess_empno(req));
		
		CommonMessage message = new CommonMessage();
		CommonApproval appInfo = new CommonApproval();

		message = giftManager.approveGfToGiftRequest(dto, appInfo, message, req);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(DATA_JSON_PAGE);
		modelAndView.addObject(JSON_DATA_KEY, JSONObject.fromObject(message).toString());
		modelAndView.addObject("uiType", "ajax");
		
		time = System.currentTimeMillis(); 
		dayTime = new SimpleDateFormat(strDateFormat); 
		strDT = dayTime.format(new Date(time)); 
		logger.info(strEnd + strDT);

		return modelAndView;
	}

	@RequestMapping(value="/hncis/gift/doUpdateGfToRequestCancel.do")
	public ModelAndView doUpdateGfToRequestCancel(HttpServletRequest req, HttpServletResponse res,
			@RequestParam(value="paramJson" , required=true) String paramJson) throws Exception{
		long time = System.currentTimeMillis(); 
		SimpleDateFormat dayTime = new SimpleDateFormat(strDateFormat); 
		String strDT = dayTime.format(new Date(time)); 
		logger.info(strStart + strDT);
		
		BgabGascgf02Dto dto = (BgabGascgf02Dto) getJsonToBean(paramJson, BgabGascgf02Dto.class);
		CommonMessage message = new CommonMessage();
		CommonApproval appInfo = new CommonApproval();
		dto.setCorp_cd(dto.getCorp_cd());

		message = giftManager.approveCancelGfToGiftRequest(dto, appInfo, message, req);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(DATA_JSON_PAGE);
		modelAndView.addObject(JSON_DATA_KEY, JSONObject.fromObject(message).toString());
		modelAndView.addObject("uiType", "ajax");
		
		time = System.currentTimeMillis(); 
		dayTime = new SimpleDateFormat(strDateFormat); 
		strDT = dayTime.format(new Date(time)); 
		logger.info(strEnd + strDT);

		return modelAndView;
	}
	
	/**
	 * training confirm
	 * @param req
	 * @param res
	 * @param paramJson
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/hncis/gift/doConfirmGFToRequest.do")
	public ModelAndView doConfirmGFToRequest(HttpServletRequest req, HttpServletResponse res, 
			@RequestParam(value="uParams", required=true) String uParams)throws HncisException{
		long time = System.currentTimeMillis(); 
		SimpleDateFormat dayTime = new SimpleDateFormat(strDateFormat); 
		String strDT = dayTime.format(new Date(time)); 
		logger.info(strStart + strDT);
		
		ModelAndView modelAndView = null;
		
		List<BgabGascgf02Dto> dtoList = (List<BgabGascgf02Dto>) getJsonToList(uParams, BgabGascgf02Dto.class);
		CommonMessage message = new CommonMessage();
		
		for(BgabGascgf02Dto dto : dtoList){
			message = giftManager.confirmGFToRequest(dto);
		}

		modelAndView = new ModelAndView();
		modelAndView.setViewName(DATA_JSON_PAGE);
		modelAndView.addObject(JSON_DATA_KEY, JSONObject.fromObject(message).toString());
		modelAndView.addObject("uiType", "ajax");
		
		time = System.currentTimeMillis(); 
		dayTime = new SimpleDateFormat(strDateFormat); 
		strDT = dayTime.format(new Date(time)); 
		logger.info(strEnd + strDT);
		
		return modelAndView;
	}
	
	/**
	 * business Travel request reject
	 * @param req
	 * @param res
	 * @param paramJson
	 * @return
	 * @throws SessionException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/hncis/gift/doRejectGFToRequest.do")
	public ModelAndView doRejectGFToRequest(HttpServletRequest req, HttpServletResponse res,
			@RequestParam(value="uParams", required=true) String uParams)throws HncisException, SessionException{
		long time = System.currentTimeMillis(); 
		SimpleDateFormat dayTime = new SimpleDateFormat(strDateFormat); 
		String strDT = dayTime.format(new Date(time)); 
		logger.info(strStart + strDT);
		
		ModelAndView modelAndView = null;

		List<BgabGascgf02Dto> dtoList = (List<BgabGascgf02Dto>) getJsonToList(uParams, BgabGascgf02Dto.class);
		CommonMessage message = new CommonMessage();

		for(BgabGascgf02Dto dto : dtoList){
			message = giftManager.rejectGFToRequest(dto);
		}

		modelAndView = new ModelAndView();
		modelAndView.setViewName(DATA_JSON_PAGE);
		modelAndView.addObject(JSON_DATA_KEY, JSONObject.fromObject(message).toString());
		modelAndView.addObject("uiType", "ajax");
		
		time = System.currentTimeMillis(); 
		dayTime = new SimpleDateFormat(strDateFormat); 
		strDT = dayTime.format(new Date(time)); 
		logger.info(strEnd + strDT);

		return modelAndView;
	}

	@RequestMapping(value="/hncis/gift/doUpdateGfToStatus.do")
	public ModelAndView doUpdateGfToStatus(HttpServletRequest req, HttpServletResponse res,
			@RequestParam(value="paramJson", required=true) String paramJson,
			@RequestParam(value="uParams"  , required=true) String uParams) throws Exception{

		BgabGascgf02Dto       dto   = (BgabGascgf02Dto)      getJsonToBean(paramJson, BgabGascgf02Dto.class);
		List<BgabGascgf02Dto> uList = (List<BgabGascgf02Dto>)getJsonToList(uParams  , BgabGascgf02Dto.class);

		giftManager.updateGfToStatus(dto, uList);
		CommonMessage message = new CommonMessage();
		if("B".equals(dto.getPgs_st_cd())){
			message.setMessage(HncisMessageSource.getMessage("CONFIRM.0000"));
		}else{
			message.setMessage(HncisMessageSource.getMessage("REJECT.0000"));
		}

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(DATA_JSON_PAGE);
		modelAndView.addObject(JSON_DATA_KEY, JSONObject.fromObject(message).toString());
		modelAndView.addObject("uiType", "ajax");

		return modelAndView;
	}


	@RequestMapping(value="/hncis/gift/doSearchGfToReqList.do")
	public ModelAndView doSearchGfToReqList(HttpServletRequest req, HttpServletResponse res,
		@RequestParam(value="page", required = false) String pageNumber,
		@RequestParam(value="rows", required = false) String pageSize,
		@RequestParam(value="paramJson", required=true) String paramJson) throws HncisException{
		ModelAndView modelAndView = null;
		BgabGascgf02Dto dto = (BgabGascgf02Dto) getJsonToBean(paramJson, BgabGascgf02Dto.class);

		if(StringUtil.isNullToString(pageNumber).equals("")){ pageNumber = "1"; }
		if(StringUtil.isNullToString(pageSize).equals("")){   pageSize = Constant.pageSize; }

		int currentPage = Integer.parseInt(pageNumber);
		int startRow    = (currentPage - 1)* Integer.parseInt(pageSize) +1;
		int endRow      = currentPage * Integer.parseInt(pageSize);
		int count       = giftManager.selectCountGfToReqList(dto);

		CommonList list = new CommonList();
		list.setPage(pageNumber);
		list.setTotal(Math.ceil((float)count / (float)Integer.parseInt(pageSize))+"");
		list.setRecords(Integer.toString(count));

		dto.setStartRow(startRow);
		dto.setEndRow(endRow);
		list.setRows(giftManager.selectGfToReqList(dto));

		modelAndView = new ModelAndView();
		modelAndView.setViewName(DATA_JSON_PAGE);
		modelAndView.addObject(JSON_DATA_KEY, JSONObject.fromObject(list).toString());
		return modelAndView;
	}


	/**
	 * 선물 대분류 저장
	 *
	 * @param req the req
	 * @param res the res
	 * @param iParams, uParams the param json array - 조건
	 * @return ModelAndView
	 * @throws hncisException the hncis exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/hncis/gift/doSaveGfToLrgList.do")
	public ModelAndView doSaveGfToLrgList(HttpServletRequest req, HttpServletResponse res,
			@RequestParam(value="iParams", required=true) String iParams,
			@RequestParam(value="uParams", required=true) String uParams) throws HncisException{
		//조회조건 설정
		List<BgabGascgf03Dto> iList = (List<BgabGascgf03Dto>) getJsonToList(iParams, BgabGascgf03Dto.class);
		List<BgabGascgf03Dto> uList = (List<BgabGascgf03Dto>) getJsonToList(uParams, BgabGascgf03Dto.class);

		//수정
		giftManager.saveGfToLrgList(iList, uList);

		CommonMessage message = new CommonMessage();
		//화면의 하단 메시지 설정
		message.setMessage(HncisMessageSource.getMessage("SAVE.0000"));

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(DATA_JSON_PAGE);
		//조회한 데이터를 string으로 해서 넣어줌.
		modelAndView.addObject(JSON_DATA_KEY, JSONObject.fromObject(message).toString());
		modelAndView.addObject("uiType", "ajax");

		return modelAndView;
	}

	@RequestMapping(value="/hncis/gift/doSearchGfListToLrgInfo.do")
	public ModelAndView doSearchGfListToLrgInfo(HttpServletRequest req, HttpServletResponse res,
			@RequestParam(value="page", required = false) String pageNumber,
			@RequestParam(value="rows", required = false) String pageSize,
			@RequestParam(value="paramJson", required=true) String paramJson) throws HncisException{

		BgabGascgf03Dto vo = (BgabGascgf03Dto)getJsonToBean(paramJson, BgabGascgf03Dto.class);

		CommonList list = new CommonList();
		//검색
		list.setRows(giftManager.selectGfListToLrgInfo(vo));

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(DATA_JSON_PAGE);
		//조회한 데이터를 string으로 해서 넣어줌.
		modelAndView.addObject(JSON_DATA_KEY, JSONObject.fromObject(list).toString());

		return modelAndView;
	}


	@SuppressWarnings("unchecked")
	@RequestMapping(value="/hncis/gift/doSaveGfToMrgList.do")
	public ModelAndView doSaveGfToMrgList(HttpServletRequest req, HttpServletResponse res,
			@RequestParam(value="iParams", required=true) String iParams,
			@RequestParam(value="uParams", required=true) String uParams) throws HncisException{
		//조회조건 설정
		List<BgabGascgf04Dto> iList = (List<BgabGascgf04Dto>) getJsonToList(iParams, BgabGascgf04Dto.class);
		List<BgabGascgf04Dto> uList = (List<BgabGascgf04Dto>) getJsonToList(uParams, BgabGascgf04Dto.class);

		//수정
		giftManager.saveGfToMrgList(iList, uList);

		CommonMessage message = new CommonMessage();
		//화면의 하단 메시지 설정
		message.setMessage(HncisMessageSource.getMessage("SAVE.0000"));

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(DATA_JSON_PAGE);
		//조회한 데이터를 string으로 해서 넣어줌.
		modelAndView.addObject(JSON_DATA_KEY, JSONObject.fromObject(message).toString());
		modelAndView.addObject("uiType", "ajax");

		return modelAndView;
	}

	@RequestMapping(value="/hncis/gift/doSearchGfListToMrgInfo.do")
	public ModelAndView doSearchGfListToMrgInfo(HttpServletRequest req, HttpServletResponse res,
			@RequestParam(value="page", required = false) String pageNumber,
			@RequestParam(value="rows", required = false) String pageSize,
			@RequestParam(value="paramJson", required=true) String paramJson) throws HncisException{

		BgabGascgf04Dto vo = (BgabGascgf04Dto)getJsonToBean(paramJson, BgabGascgf04Dto.class);

		CommonList list = new CommonList();
		//검색
		list.setRows(giftManager.selectGfListToMrgInfo(vo));

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(DATA_JSON_PAGE);
		//조회한 데이터를 string으로 해서 넣어줌.
		modelAndView.addObject(JSON_DATA_KEY, JSONObject.fromObject(list).toString());

		return modelAndView;
	}

	/**
	 * 도서 대분류 삭제
	 *
	 * @param req the req
	 * @param res the res
	 * @param iParams, uParams the param json array - 조건
	 * @return ModelAndView
	 * @throws hncisException the hncis exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/hncis/gift/doDeleteGfToLrgList.do")
	public ModelAndView doDeleteGfToLrgList(HttpServletRequest req, HttpServletResponse res,
			@RequestParam(value="dParams", required=true) String dParams) throws HncisException{
		//조회조건 설정
		List<BgabGascgf03Dto> dList = (List<BgabGascgf03Dto>) getJsonToList(dParams, BgabGascgf03Dto.class);

		//수정
		giftManager.deleteGfToLrgList(dList);

		CommonMessage message = new CommonMessage();
		//화면의 하단 메시지 설정
		message.setMessage(HncisMessageSource.getMessage("DELETE.0000"));

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(DATA_JSON_PAGE);
		//조회한 데이터를 string으로 해서 넣어줌.
		modelAndView.addObject(JSON_DATA_KEY, JSONObject.fromObject(message).toString());
		modelAndView.addObject("uiType", "ajax");

		return modelAndView;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/hncis/gift/doDeleteGfToMrgList.do")
	public ModelAndView doDeleteGfToMrgList(HttpServletRequest req, HttpServletResponse res,
			@RequestParam(value="dParams", required=true) String dParams) throws HncisException{
		//조회조건 설정
		List<BgabGascgf04Dto> dList = (List<BgabGascgf04Dto>) getJsonToList(dParams, BgabGascgf04Dto.class);

		//수정
		giftManager.deleteGfToMrgList(dList);

		CommonMessage message = new CommonMessage();
		//화면의 하단 메시지 설정
		message.setMessage(HncisMessageSource.getMessage("DELETE.0000"));

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(DATA_JSON_PAGE);
		//조회한 데이터를 string으로 해서 넣어줌.
		modelAndView.addObject(JSON_DATA_KEY, JSONObject.fromObject(message).toString());
		modelAndView.addObject("uiType", "ajax");

		return modelAndView;
	}

	@RequestMapping(value="/hncis/gift/doSearchGfToGiftMgmtList.do")
	public ModelAndView doSearchGfToGiftMgmtList(HttpServletRequest req, HttpServletResponse res,
		@RequestParam(value="page", required = false) String pageNumber,
		@RequestParam(value="rows", required = false) String pageSize,
		@RequestParam(value="paramJson", required=true) String paramJson) throws HncisException{
		ModelAndView modelAndView = null;
		BgabGascgf01Dto dto = (BgabGascgf01Dto) getJsonToBean(paramJson, BgabGascgf01Dto.class);

		if(StringUtil.isNullToString(pageNumber).equals("")){ pageNumber = "1"; }
		if(StringUtil.isNullToString(pageSize).equals("")){   pageSize = Constant.pageSize; }

		int currentPage = Integer.parseInt(pageNumber);
		int startRow    = (currentPage - 1)* Integer.parseInt(pageSize) +1;
		int endRow      = currentPage * Integer.parseInt(pageSize);
		int count       = giftManager.selectCountGfToGiftMgmtList(dto);

		CommonList list = new CommonList();
		list.setPage(pageNumber);
		list.setTotal(Math.ceil((float)count / (float)Integer.parseInt(pageSize))+"");
		list.setRecords(Integer.toString(count));

		dto.setStartRow(startRow);
		dto.setEndRow(endRow);
		list.setRows(giftManager.selectGfToGiftMgmtList(dto));

		modelAndView = new ModelAndView();
		modelAndView.setViewName(DATA_JSON_PAGE);
		modelAndView.addObject(JSON_DATA_KEY, JSONObject.fromObject(list).toString());

		return modelAndView;
	}

	@RequestMapping(value="/hncis/gift/doSaveGfToGiftInfo.do")
	public ModelAndView doSaveGfToGiftInfo(HttpServletRequest req, HttpServletResponse res,
			@RequestParam(value="paramJson" , required=true) String paramJson
			) throws Exception{
		BgabGascgf01Dto dto = (BgabGascgf01Dto) getJsonToBean(paramJson, BgabGascgf01Dto.class);

		CommonMessage message = giftManager.isnertGfToGiftInfo(dto);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(DATA_JSON_PAGE);
		modelAndView.addObject(JSON_DATA_KEY, JSONObject.fromObject(message).toString());
		modelAndView.addObject("uiType", "ajax");

		return modelAndView;
	}
	@RequestMapping(value="/hncis/gift/doDeleteGfToGiftInfo.do")
	public ModelAndView doDeleteGfToGiftInfo(HttpServletRequest req, HttpServletResponse res,
			@RequestParam(value="paramJson" , required=true) String paramJson
			) throws Exception{
		BgabGascgf01Dto dto = (BgabGascgf01Dto) getJsonToBean(paramJson, BgabGascgf01Dto.class);

		CommonMessage message = giftManager.deleteGfToGiftInfo(dto);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(DATA_JSON_PAGE);
		modelAndView.addObject(JSON_DATA_KEY, JSONObject.fromObject(message).toString());
		modelAndView.addObject("uiType", "ajax");

		return modelAndView;
	}

	@RequestMapping(value="/hncis/gift/doSearchInfoGfToGiftInfo.do")
	public ModelAndView doSearchInfoGfToGiftInfo(HttpServletRequest req, HttpServletResponse res,
			@RequestParam(value="paramJson", required=true) String paramJson) throws HncisException{
		BgabGascgf01Dto dto = (BgabGascgf01Dto) getJsonToBean(paramJson, BgabGascgf01Dto.class);

		BgabGascgf01Dto rsReqDto = new BgabGascgf01Dto();
		rsReqDto = giftManager.selectInfoGfToGiftInfo(dto);

		rsReqDto.setMessage(HncisMessageSource.getMessage("SEARCH.0000"));

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(DATA_JSON_PAGE);
		modelAndView.addObject(JSON_DATA_KEY, JSONObject.fromObject(rsReqDto).toString());
		modelAndView.addObject("uiType", "ajax");

		return modelAndView;
	}
	
	@RequestMapping(value="/hncis/gift/doSearchInfoGfToGiftInfoByIfId.do")
	public ModelAndView doSearchInfoGfToGiftInfoByIfId(HttpServletRequest req, HttpServletResponse res,
			@RequestParam(value="paramJson", required=true) String paramJson) throws HncisException{
		BgabGascgf01Dto dto = (BgabGascgf01Dto) getJsonToBean(paramJson, BgabGascgf01Dto.class);

		BgabGascgf01Dto rsReqDto = new BgabGascgf01Dto();
		rsReqDto = giftManager.selectInfoGfToGiftInfoByIfId(dto);

		rsReqDto.setMessage(HncisMessageSource.getMessage("SEARCH.0000"));

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(DATA_JSON_PAGE);
		modelAndView.addObject(JSON_DATA_KEY, JSONObject.fromObject(rsReqDto).toString());
		modelAndView.addObject("uiType", "ajax");

		return modelAndView;
	}

	@RequestMapping(value="/hncis/gift/doSearchInfoGfToGiftRequstInfo.do")
	public ModelAndView selectInfoGfToGiftRequstInfo(HttpServletRequest req, HttpServletResponse res,
			@RequestParam(value="paramJson", required=true) String paramJson) throws HncisException{
		BgabGascgf02Dto dto = (BgabGascgf02Dto) getJsonToBean(paramJson, BgabGascgf02Dto.class);

		BgabGascgf02Dto rsReqDto = new BgabGascgf02Dto();
		rsReqDto = giftManager.selectInfoGfToGiftRequstInfo(dto);
		
		if(rsReqDto != null){
			if(!StringUtil.isNullToString(rsReqDto.getIf_id()).equals("")){
				
				CommonApproval commonApproval = new CommonApproval();
				commonApproval.setIf_id(rsReqDto.getIf_id());
				commonApproval.setSystem_code("GF");
				commonApproval.setCorp_cd(dto.getCorp_cd());
				
				rsReqDto.setCode(StringUtil.isNullToString(commonManager.getSelectApprovalInfo(commonApproval)));
			}
		}

		rsReqDto.setMessage(HncisMessageSource.getMessage("SEARCH.0000"));

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(DATA_JSON_PAGE);
		modelAndView.addObject(JSON_DATA_KEY, JSONObject.fromObject(rsReqDto).toString());
		modelAndView.addObject("uiType", "ajax");

		return modelAndView;
	}

	/**
	 * business Travel report save
	 * @param fileInfo
	 * @throws HncisException, IOException
	 */
	@RequestMapping(value="/hncis/gift/doSaveGfToFile.do")
	public void doSaveGfToFile(HttpServletRequest req, HttpServletResponse res,
		@RequestParam(value="fileInfo", required=true) String fileInfo)throws HncisException, IOException{

		String contentType = req.getContentType();
		if(contentType != null && contentType.startsWith("multipart/form")){
			BgabGascZ011Dto bgabGascZ011Dto = (BgabGascZ011Dto)getJsonToBean(fileInfo, BgabGascZ011Dto.class);
			giftManager.saveGfToFile(req, res, bgabGascZ011Dto);
		}
	}

	/**
	 * business Travel report save
	 * @param reportInfoI
	 * @param reportInfoU
	 * @return ModelAndView
	 * @throws HncisException
	 */
	@RequestMapping(value="/hncis/gift/doSearchGfToFile.do")
	public ModelAndView doSearchGfToFile(HttpServletRequest req, HttpServletResponse res,
		@RequestParam(value="paramJson", required=true) String paramJson) throws HncisException{
		ModelAndView modelAndView = null;
		BgabGascZ011Dto dto = (BgabGascZ011Dto) getJsonToBean(paramJson, BgabGascZ011Dto.class);

		CommonList list = new CommonList();
		list.setRows(giftManager.getSelectGfToFile(dto));

		modelAndView = new ModelAndView();
		modelAndView.setViewName(DATA_JSON_PAGE);
		modelAndView.addObject(JSON_DATA_KEY, JSONObject.fromObject(list).toString());

		return modelAndView;
	}

	/**
	 * business Travel report save
	 * @param reportInfoI
	 * @param reportInfoU
	 * @return ModelAndView
	 * @throws HncisException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/hncis/gift/doFileDown.do")
	public ModelAndView doFileDown(HttpServletRequest req, HttpServletResponse res,
		@RequestParam(value="fileInfo", required=true) String fileInfo) throws HncisException{

		BgabGascZ011Dto dto = (BgabGascZ011Dto) getJsonToBean(fileInfo, BgabGascZ011Dto.class);
		BgabGascZ011Dto bgabGascZ011Dto = giftManager.getSelectGfToFileInfo(dto);

		Map mpfileData = new HashMap();
		mpfileData.put("fileRealName",   bgabGascZ011Dto.getOgc_fil_nm());
		mpfileData.put("fileName",   bgabGascZ011Dto.getFil_nm());
		mpfileData.put("folderName",   "gift");

		return new ModelAndView("download", "fileData", mpfileData);
	}

	/**
	 * business Travel report save
	 * @param reportInfoI
	 * @param reportInfoU
	 * @return ModelAndView
	 * @throws HncisException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/hncis/gift/doDeleteGfToFile.do")
	public ModelAndView doDeleteGfToFile(HttpServletRequest req, HttpServletResponse res,
		@RequestParam(value="fileInfo", required=true) String fileInfo) throws HncisException{

		List<BgabGascZ011Dto> dto = (List<BgabGascZ011Dto>) getJsonToList(fileInfo, BgabGascZ011Dto.class);

		giftManager.deleteGfToFile(dto);

		CommonMessage message = new CommonMessage();
		message.setMessage(HncisMessageSource.getMessage("DELETE.0000"));

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(DATA_JSON_PAGE);
		modelAndView.addObject(JSON_DATA_KEY, JSONObject.fromObject(message).toString());
		modelAndView.addObject("uiType", "ajax");

		return modelAndView;
	}

	@RequestMapping(value="/hncis/gift/doSelectXgf08Info.do")
	public ModelAndView doSelectXgf08Info(HttpServletRequest req, HttpServletResponse res,
			@RequestParam(value="paramJson", required=true) String paramJson) throws HncisException{
		BgabGascgf05Dto dto = (BgabGascgf05Dto) getJsonToBean(paramJson, BgabGascgf05Dto.class);

		BgabGascgf05Dto rsReqDto = giftManager.selectXgf08Info(dto);

		if(rsReqDto == null){
			rsReqDto = new BgabGascgf05Dto();
		}

		rsReqDto.setMessage(HncisMessageSource.getMessage("SEARCH.0000"));

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(DATA_JSON_PAGE);
		modelAndView.addObject(JSON_DATA_KEY, JSONObject.fromObject(rsReqDto).toString());
		modelAndView.addObject("uiType", "ajax");

		return modelAndView;
	}

	@RequestMapping(value="/hncis/gift/doSaveXgf08Info.do")
	public ModelAndView doSaveXgf08Info(HttpServletRequest req, HttpServletResponse res,
			@RequestParam(value="paramJson" , required=true) String paramJson
			) throws Exception{
		BgabGascgf05Dto dto = (BgabGascgf05Dto) getJsonToBean(paramJson, BgabGascgf05Dto.class);
		dto.setIpe_eeno(SessionInfo.getSess_empno(req));
		dto.setUpdr_eeno(SessionInfo.getSess_empno(req));

		CommonMessage message = giftManager.saveXgf08Info(dto);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(DATA_JSON_PAGE);
		modelAndView.addObject(JSON_DATA_KEY, JSONObject.fromObject(message).toString());
		modelAndView.addObject("uiType", "ajax");

		return modelAndView;
	}
	
	/**
	 * business Card accept excel
	 * @param req
	 * @param res
	 * @param paramJson
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/hncis/gift/doExcelGiftList.excel")
	public ModelAndView doExcelPickUpList(HttpServletRequest req, HttpServletResponse res,
		@RequestParam(value="fileName", required=false) String fileName,
		@RequestParam(value="header", required=false) String header,
		@RequestParam(value="headerName", required=false) String headerName,
		@RequestParam(value="fomatter", required=false) String fomatter,
		@RequestParam(value="page", required = false) String pageNumber,
		@RequestParam(value="rows", required = false) String pageSize,
		@RequestParam(value="paramJson", required=true) String paramJson) throws HncisException{

		//조회조건 설정
		BgabGascgf02Dto bgabGascgf02Dto = (BgabGascgf02Dto) getJsonToBean(paramJson, BgabGascgf02Dto.class);

		if(StringUtil.isNullToString(pageNumber).equals("")){ pageNumber = "1"; }
		if(StringUtil.isNullToString(pageSize).equals("")){   pageSize = Constant.pageSize; }

 		int currentPage = Integer.parseInt(pageNumber);
 		int startRow = (currentPage - 1)* Integer.parseInt(pageSize) +1;
 		int endRow = currentPage * Integer.parseInt(pageSize);
 		//검색
 		int count = giftManager.selectCountGfToReqList(bgabGascgf02Dto);

 		CommonList list = new CommonList();
 		list.setPage(pageNumber);
 		list.setTotal(Math.ceil((float)count / (float)Integer.parseInt(pageSize))+"");
 		list.setRecords(Integer.toString(count));

 		bgabGascgf02Dto.setStartRow(1);
 		bgabGascgf02Dto.setEndRow(count);
 		//검색
 		list.setRows(giftManager.selectGfToReqList(bgabGascgf02Dto));

		JSONArray gridData = JSONArray.fromObject(list.getRows());
		String[] headerTitleArray = header.replace("[","").replace("]","").split(",");
		String[] headerNameArray  = headerName.replace("[","").replace("]","").split(",");
		String[] fomatterArray    = fomatter.replace("[","").replace("]","").split(",");

		Map mpExcelData = new HashMap();
		mpExcelData.put("fileName",   fileName);
		mpExcelData.put("jobName",   "BT");
		mpExcelData.put("header",     headerTitleArray);
		mpExcelData.put("headerName", headerNameArray);
		mpExcelData.put("fomatter",   fomatterArray);
		mpExcelData.put("gridData",   gridData);

		return new ModelAndView("GridExcelView", "excelData", mpExcelData);
	}
}