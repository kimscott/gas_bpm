package com.hncis.businessTravel.manager.impl;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.hncis.businessTravel.dao.BusinessTravelDao;
import com.hncis.businessTravel.manager.BusinessTravelManager;
import com.hncis.businessTravel.vo.BgabGascbt01;
import com.hncis.businessTravel.vo.BgabGascbt02;
import com.hncis.businessTravel.vo.BgabGascbt03;
import com.hncis.businessTravel.vo.BgabGascbt04;
import com.hncis.businessTravel.vo.BgabGascbt06;
import com.hncis.businessTravel.vo.BgabGascbt07;
import com.hncis.businessTravel.vo.BgabGascbt08;
import com.hncis.businessTravel.vo.BgabGascbt09;
import com.hncis.businessTravel.vo.BgabGascbtDto;
import com.hncis.businessTravel.vo.BgabGascbtExcelDto;
import com.hncis.businessTravel.vo.BgabGascbtVoucherExcelDto;
import com.hncis.common.Constant;
import com.hncis.common.application.ApprovalGasc;
import com.hncis.common.application.RfcBudgetCheck;
import com.hncis.common.application.RfcFiCreate;
import com.hncis.common.application.RfcPoCreate;
import com.hncis.common.application.SessionInfo;
import com.hncis.common.dao.CommonJobDao;
import com.hncis.common.exception.impl.SessionException;
import com.hncis.common.message.HncisMessageSource;
import com.hncis.common.util.BpmApiUtil;
import com.hncis.common.util.CurrentDateTime;
import com.hncis.common.util.ExcelTemplat;
import com.hncis.common.util.FileUtil;
import com.hncis.common.util.StringUtil;
//import com.hncis.common.util.Submit;
import com.hncis.common.vo.BgabGascZ011Dto;
import com.hncis.common.vo.BgabGascz002Dto;
import com.hncis.common.vo.BgabGascz005Dto;
import com.hncis.common.vo.CommonApproval;
import com.hncis.common.vo.CommonCode;
import com.hncis.common.vo.CommonMessage;
import com.hncis.common.vo.CommonSap;
import com.hncis.common.vo.RfcBudgetCheckVo;
import com.hncis.common.vo.RfcPoCreateVo;
import com.hncis.system.dao.SystemDao;
import com.hncis.system.vo.BgabGascz014Dto;
import com.hncis.system.vo.BgabGascz016Dto;

@Service("businessTravelManagerImpl")
public class BusinessTravelManagerImpl implements BusinessTravelManager{

    private transient Log logger = LogFactory.getLog(getClass());
    private static final String strMessege = "messege";
    
	@Autowired
	public BusinessTravelDao businessTravelDao;

	@Autowired
	public CommonJobDao commonJobDao;

	@Autowired
	public SystemDao systemDao;

	private static final String gubunAb = "PT001";
	private static final String gubunDo = "PT002";
	private static final String pCode = "P-D-001";
	private static final String sCode = "GASBZ01410010";
	private static final String rCode = "GASROLE01410030";
	private static final String pCode2 = "P-D-002";
	private static final String sCode2 = "GASBZ01420010";
	private static final String rCode2 = "GASROLE01420030";
	private static final String adminId = "10000001";
	private static final String fileName = "businessTravel";
	private static final String fileMessage = "FILE.0001";
	private static final String orgCode = "H301";
	private static final String descCancel = "cancel";

	@Override
	public int insertBTToRequest(BgabGascbt01 bsicInfo, List<BgabGascbt02> travelerList, List<BgabGascbt03> scheduleList){

		Integer basicRs = businessTravelDao.insertBTToRequestByBasic(bsicInfo);
		Integer travelerRs = businessTravelDao.insertBTToRequestByTraveler(travelerList);
		Integer scheduleRs = businessTravelDao.insertBTToRequestBySchedule(scheduleList);

		return travelerRs;
	}

	@Override
	public int saveBTToRequest(BgabGascbt01 bgabGascbt01
			, List<BgabGascbt02> bgabGascbt02IList, List<BgabGascbt02> bgabGascbt02UList
//			, List<BgabGascbt03> bgabGascbt03IList, List<BgabGascbt03> bgabGascbt03UList
			, List<BgabGascbt07> bgabGascbt07IList, List<BgabGascbt07> bgabGascbt07UList
			, List<BgabGascbt08> bgabGascbt08IList, List<BgabGascbt08> bgabGascbt08UList
			, List<BgabGascbt09> bgabGascbt09IList, List<BgabGascbt09> bgabGascbt09UList
			, List<BgabGascbt02> bgabGascbt02VtIList, List<BgabGascbt02> bgabGascbt02VtUList){

		Integer basicRs = 0;
		if(bgabGascbt01.getBasic_mode().equals("I")){
			basicRs = businessTravelDao.insertBTToRequestByBasic(bgabGascbt01);
		}else{
			basicRs = businessTravelDao.updateBTToRequestByBasic(bgabGascbt01);
		}
		Integer travelerIRs = businessTravelDao.insertBTToRequestByTraveler(bgabGascbt02IList);
//		Integer scheduleIRs = (Integer)businessTravelDao.insertBTToRequestBySchedule(bgabGascbt03IList);
		Integer flightIRs   = businessTravelDao.insertFlightConfirmBtToRequest(bgabGascbt07IList);
		Integer hotelIRs    = businessTravelDao.insertHotelConfirmBtToRequest(bgabGascbt08IList);
		Integer rentIRs     = businessTravelDao.insertRentCarBtToRequest(bgabGascbt09IList);
		Integer travelerVtIRs = businessTravelDao.insertBTToRequestByTraveler(bgabGascbt02VtIList);

		Integer travelerURs = businessTravelDao.updateBTToRequestByTraveler(bgabGascbt02UList);
//		Integer scheduleURs = (Integer)businessTravelDao.updateBTToRequestBySchedule(bgabGascbt03UList);
		Integer flightURs   = businessTravelDao.updateFlightConfirmBtToRequest(bgabGascbt07UList);
		Integer hotelURs    = businessTravelDao.updateHotelConfirmBtToRequest(bgabGascbt08UList);
		Integer rentURs     = businessTravelDao.updateRentCarBtToRequest(bgabGascbt09UList);
		Integer travelerVtURs = businessTravelDao.updateBTToRequestByTraveler(bgabGascbt02VtUList);
		
		if(basicRs > 0){
			// BPM API호출
			String processCode = ""; 	//프로세스 코드 (출장 신청 프로세스) - 프로세스 정의서 참조
			String statusCode = "";	//액티비티 코드 (출장신청서작성) - 프로세스 정의서 참조
			String roleCode = "";   //담당자 역할코드
			if(bgabGascbt01.getDom_abrd_scn_cd().equals(gubunAb)){
				processCode = pCode2; // 해외
				statusCode = sCode2;	//액티비티 코드 (출장신청서작성) - 프로세스 정의서 참조
				roleCode = rCode2;   //담당자 역할코드
			}else{
				processCode = pCode; // 국내
				statusCode = sCode;	//액티비티 코드 (출장신청서작성) - 프로세스 정의서 참조
				roleCode = rCode;   //담당자 역할코드
			}
			String bizKey = bgabGascbt01.getDoc_no();	//신청서 번호
			String loginUserId = bgabGascbt01.getEeno();	//로그인 사용자 아이디
			String comment = null;
			//역할정보
			List<String> approveList = new ArrayList<String>();
			List<String> managerList = new ArrayList<String>();
			managerList.add(adminId);
			
			BpmApiUtil.sendSaveTask(processCode, bizKey, statusCode, loginUserId, roleCode, approveList, managerList );
		}
		
		return travelerURs;
	}

	@Override
	public int deleteBTToRequest(BgabGascbt01 bsicInfo){

		Integer basicRs = businessTravelDao.deleteBTToRequestByBasic(bsicInfo);

		BgabGascbt03 bgabGascbt03 = new BgabGascbt03();
		bgabGascbt03.setCorp_cd(bsicInfo.getCorp_cd());
		bgabGascbt03.setDoc_no(bsicInfo.getDoc_no());
		Integer scheduleRs = businessTravelDao.deleteBTToRequestBySchedule(bgabGascbt03);

		BgabGascbt02 bgabGascbt02 = new BgabGascbt02();
		bgabGascbt02.setCorp_cd(bsicInfo.getCorp_cd());
		bgabGascbt02.setDoc_no(bsicInfo.getDoc_no());
		Integer travelerRs = businessTravelDao.deleteBTToRequestByTraveler(bgabGascbt02);

		BgabGascbt07 bgabGascbt07 = new BgabGascbt07();
		bgabGascbt07.setCorp_cd(bsicInfo.getCorp_cd());
		bgabGascbt07.setDoc_no(bsicInfo.getDoc_no());
		Integer flightRs = businessTravelDao.deleteFlightConfirmBtToRequest(bgabGascbt07);

		BgabGascbt08 bgabGascbt08 = new BgabGascbt08();
		bgabGascbt08.setCorp_cd(bsicInfo.getCorp_cd());
		bgabGascbt08.setDoc_no(bsicInfo.getDoc_no());
		Integer hotelRs = businessTravelDao.deleteHotelConfirmBtToRequest(bgabGascbt08);

		BgabGascbt09 bgabGascbt09 = new BgabGascbt09();
		bgabGascbt09.setCorp_cd(bsicInfo.getCorp_cd());
		bgabGascbt09.setDoc_no(bsicInfo.getDoc_no());
		Integer rentRs = businessTravelDao.deleteRentCarBtToRequest(bgabGascbt09);

		BgabGascbt04 bgabGascbt04 = new BgabGascbt04();
		bgabGascbt04.setCorp_cd(bsicInfo.getCorp_cd());
		bgabGascbt04.setDoc_no(bsicInfo.getDoc_no());
		List<BgabGascbt04> bgabGascbt04List = businessTravelDao.getSelectBTToReport(bgabGascbt04);
		deleteBTToReport(bgabGascbt04List);
		
		if(basicRs > 0){
			// BPM API호출
			String processCode = ""; 	//프로세스 코드 (출장 신청 프로세스) - 프로세스 정의서 참조
			String statusCode = "";	//액티비티 코드 (출장신청서작성) - 프로세스 정의서 참조
			String roleCode = "";   //담당자 역할코드
			if(bsicInfo.getDom_abrd_scn_cd().equals(gubunAb)){
				processCode = pCode2; // 해외
				statusCode = sCode2;	//액티비티 코드 (출장신청서작성) - 프로세스 정의서 참조
				roleCode = rCode2;   //담당자 역할코드
			}else{
				processCode = pCode; // 국내
				statusCode = sCode;	//액티비티 코드 (출장신청서작성) - 프로세스 정의서 참조
				roleCode = rCode;   //담당자 역할코드
			}
			String bizKey = bsicInfo.getDoc_no();	//신청서 번호
			String loginUserId = bsicInfo.getEeno();	//로그인 사용자 아이디
			
			BpmApiUtil.sendDeleteAndRejectTask(processCode, bizKey, statusCode, loginUserId);
		}

		return travelerRs;
	}

	@Override
	public int deleteTravelerToRequest(BgabGascbt02 bgabGascbt02){
		return businessTravelDao.deleteBTToRequestByTraveler(bgabGascbt02);
	}

	@Override
	public int deleteScheduleToRequest(BgabGascbt03 bgabGascbt03){
		return businessTravelDao.deleteBTToRequestBySchedule(bgabGascbt03);
	}

	@Override
	public BgabGascbt01 getSelectBTToRequestByBasicInfo(BgabGascbt01 bsicInfo){
		return businessTravelDao.getSelectBTToRequestByBasicInfo(bsicInfo);
	}

	@Override
	public List<BgabGascbt02> getSelectBTToRequestByTraveler(BgabGascbt02 bgabGascbt02){
		return businessTravelDao.getSelectBTToRequestByTraveler(bgabGascbt02);
	}

	@Override
	public List<BgabGascbt02> getSelectBTToRequestByVirtualTraveler(BgabGascbt02 bgabGascbt02){
		return businessTravelDao.getSelectBTToRequestByVirtualTraveler(bgabGascbt02);
	}

	@Override
	public List<BgabGascbt03> getSelectBTToRequestBySchedule(BgabGascbt03 bgabGascbt03){
		return businessTravelDao.getSelectBTToRequestBySchedule(bgabGascbt03);
	}

	@Override
	public int getSelectCountBTToList(BgabGascbt01 bgabGascbt01){
		return Integer.parseInt(businessTravelDao.getSelectCountBTToList(bgabGascbt01));
	}

	@Override
	public List<BgabGascbt01> getSelectBTToList(BgabGascbt01 bgabGascbt01){
		return businessTravelDao.getSelectBTToList(bgabGascbt01);
	}

	@Override
	public List<CommonCode> getSelectBTToComboListByReport(CommonCode code){
		return businessTravelDao.getSelectBTToComboListByReport(code);
	}

	@Override
	public List<BgabGascbt04> getSelectBTToReport(BgabGascbt04 bgabGascbt04){
		return businessTravelDao.getSelectBTToReport(bgabGascbt04);
	}

	@Override
	public List<BgabGascbt04> getSelectBTToReportCard(BgabGascbt04 bgabGascbt04){
		return businessTravelDao.getSelectBTToReportCard(bgabGascbt04);
	}

	@Override
	public List<BgabGascbt04> getSelectBTToReportVendor(BgabGascbt04 bgabGascbt04){
		return businessTravelDao.getSelectBTToReportVendor(bgabGascbt04);
	}

	@Override
	public int saveBTToReport(BgabGascbt02 bgabGascbt02, List<BgabGascbt04> bgabGascbt04IList, List<BgabGascbt04> bgabGascbt04UList){

		int seqMax = Integer.parseInt(businessTravelDao.getSelectMaxSeqBTToReport(bgabGascbt02));

		for(int i=0; i<bgabGascbt04IList.size(); i++){
			bgabGascbt04IList.get(i).setSeq(seqMax+i);
		}

		Integer reportIRs = businessTravelDao.insertBTToReport(bgabGascbt04IList);
		Integer reportURs = businessTravelDao.updateBTToReport(bgabGascbt04UList);
		bgabGascbt02.setTot_arsl_refl_yn("N");

		businessTravelDao.updateBTToTravelerBySapYn(bgabGascbt02);

		return reportIRs+reportURs;
	}

	@Override
	public int deleteBTToReport(List<BgabGascbt04> bgabGascbt04DList){
		Integer reportDRs = businessTravelDao.deleteBTToReport(bgabGascbt04DList);

		for(int i=0; i<bgabGascbt04DList.size(); i++){
			BgabGascZ011Dto bgabGascZ011Dto = new BgabGascZ011Dto();
			bgabGascZ011Dto.setDoc_no(bgabGascbt04DList.get(i).getDoc_no());
			bgabGascZ011Dto.setEeno(bgabGascbt04DList.get(i).getEeno());
			bgabGascZ011Dto.setSeq(bgabGascbt04DList.get(i).getSeq());
			bgabGascZ011Dto.setAffr_scn_cd("BT");

			List<BgabGascZ011Dto> bgabGascZ011List = businessTravelDao.getSelectBTToFile(bgabGascZ011Dto);
			deleteBTToFile(bgabGascZ011List);
		}
		if(bgabGascbt04DList.size() > 0){
			int NCnt = Integer.parseInt(businessTravelDao.getSelectCountBTToReportBySapYn(bgabGascbt04DList.get(0)));

			BgabGascbt02 bgabGascbt02 = new BgabGascbt02();
			if(NCnt == 0){
				bgabGascbt02.setDoc_no(bgabGascbt04DList.get(0).getDoc_no());
				bgabGascbt02.setEeno(bgabGascbt04DList.get(0).getEeno());
				bgabGascbt02.setTot_arsl_refl_yn("Y");

				businessTravelDao.updateBTToTravelerBySapYn(bgabGascbt02);
			}
		}
		return reportDRs;
	}

	@Override
	public void saveBtToFile(HttpServletRequest req, HttpServletResponse res, BgabGascZ011Dto bgabGascZ011Dto){
		String msg        = "";
		String resultUrl  = "xbt01_file.gas";
		if("confirm".equals(bgabGascZ011Dto.getSrcId())){
			resultUrl = "xbt_confirm_file.gas";
		}
		String[] result   = new String[4];
		String[] paramVal = new String[4];

		try{
			paramVal[0] = "file_name";
			paramVal[1] = "old_file_name";
			paramVal[2] = fileName;

			result = FileUtil.uploadFile(req, res, paramVal);

			if(result != null){
				if(result[0] != null){
					bgabGascZ011Dto.setOgc_fil_nm(result[0]);
					bgabGascZ011Dto.setFil_nm(result[5]);
					bgabGascZ011Dto.setFil_mgn_qty(Integer.parseInt(result[3]));
					Integer fileRs = businessTravelDao.insertBTToFile(bgabGascZ011Dto);
				}
				msg = result[4];

			}else{
				//resultUrl = "xbt01_file.gas";
				msg = HncisMessageSource.getMessage(fileMessage);
			}
		}catch(Exception e){
			//resultUrl = "xbt01_file.gas";
			msg = HncisMessageSource.getMessage(fileMessage);
			logger.error(strMessege, e);
		}
		try{
			String dispatcherYN = "Y";
			req.setAttribute("hid_doc_no",  bgabGascZ011Dto.getDoc_no());
			req.setAttribute("hid_eeno",  bgabGascZ011Dto.getEeno());
			req.setAttribute("hid_pgs_st_cd",  bgabGascZ011Dto.getPgs_st_cd());
			req.setAttribute("hid_seq",  bgabGascZ011Dto.getSeq());
			req.setAttribute("dispatcherYN", dispatcherYN);
			req.setAttribute("csrfToken", bgabGascZ011Dto.getCsrfToken());
			req.setAttribute("message",  msg);
			req.getRequestDispatcher(resultUrl).forward(req, res);

			return;
		}catch(Exception e){
			logger.error(strMessege, e);
		}
	
	}

	@Override
	public void saveBtToOutCompFile(HttpServletRequest req, HttpServletResponse res, BgabGascZ011Dto bgabGascZ011Dto){
		String msg        = "";
		String resultUrl  = "businessTravel_confirm_file.gas";
//		if("confirm".equals(bgabGascZ011Dto.getSrcId())){
//			resultUrl = "xbt_confirm_file.gas";
//		}
		String[] result   = new String[4];
		String[] paramVal = new String[4];

		try{
			paramVal[0] = "file_name";
			paramVal[1] = "old_file_name";
			paramVal[2] = fileName;

			result = FileUtil.uploadFile(req, res, paramVal);

			if(result != null){
				if(result[0] != null){
					bgabGascZ011Dto.setOgc_fil_nm(result[0]);
					bgabGascZ011Dto.setFil_nm(result[5]);
					bgabGascZ011Dto.setFil_mgn_qty(Integer.parseInt(result[3]));
					Integer fileRs = businessTravelDao.insertBTToFile(bgabGascZ011Dto);
				}
				msg = result[4];

			}else{
				//resultUrl = "xbt01_file.gas";
				msg = HncisMessageSource.getMessage(fileMessage);
			}
		}catch(Exception e){
			//resultUrl = "xbt01_file.gas";
			msg = HncisMessageSource.getMessage(fileMessage);
			logger.error(strMessege, e);
		}
		try{

			String dispatcherYN = "Y";
			req.setAttribute("hid_doc_no",  bgabGascZ011Dto.getDoc_no());
			req.setAttribute("hid_eeno",  bgabGascZ011Dto.getEeno());
			req.setAttribute("hid_pgs_st_cd",  bgabGascZ011Dto.getPgs_st_cd());
			req.setAttribute("hid_seq",  bgabGascZ011Dto.getSeq());
			req.setAttribute("dispatcherYN", dispatcherYN);
			req.setAttribute("csrfToken", bgabGascZ011Dto.getCsrfToken());
			req.setAttribute("message",  msg);
			req.getRequestDispatcher(resultUrl).forward(req, res);

			return;
		}catch(Exception e){
			logger.error(strMessege, e);
		}
		
	}

	@Override
	public void saveBtToOutCompFileHotel(HttpServletRequest req, HttpServletResponse res, BgabGascZ011Dto bgabGascZ011Dto){
		String msg        = "";
		String resultUrl  = "businessTravel_file_cmpx.gas";
//		if("confirm".equals(bgabGascZ011Dto.getSrcId())){
//			resultUrl = "xbt_confirm_file.gas";
//		}
		String[] result   = new String[4];
		String[] paramVal = new String[4];

		try{
			paramVal[0] = "file_name";
			paramVal[1] = "old_file_name";
			paramVal[2] = fileName;

			result = FileUtil.uploadFile(req, res, paramVal);

			if(result != null){
				if(result[0] != null){
					bgabGascZ011Dto.setOgc_fil_nm(result[0]);
					bgabGascZ011Dto.setFil_nm(result[5]);
					bgabGascZ011Dto.setFil_mgn_qty(Integer.parseInt(result[3]));
					Integer fileRs = businessTravelDao.insertBTToFile(bgabGascZ011Dto);
				}
				msg = result[4];

			}else{
				//resultUrl = "xbt01_file.gas";
				msg = HncisMessageSource.getMessage(fileMessage);
			}
		}catch(Exception e){
			//resultUrl = "xbt01_file.gas";
			msg = HncisMessageSource.getMessage(fileMessage);
			logger.error(strMessege, e);
		}
		try{

			String dispatcherYN = "Y";
			req.setAttribute("hid_doc_no",  bgabGascZ011Dto.getDoc_no());
			req.setAttribute("hid_eeno",  bgabGascZ011Dto.getEeno());
			req.setAttribute("hid_pgs_st_cd",  bgabGascZ011Dto.getPgs_st_cd());
			req.setAttribute("hid_seq",  bgabGascZ011Dto.getSeq());
			req.setAttribute("dispatcherYN", dispatcherYN);
			req.setAttribute("csrfToken", bgabGascZ011Dto.getCsrfToken());
			req.setAttribute("message",  msg);
			req.getRequestDispatcher(resultUrl).forward(req, res);

			return;
		}catch(Exception e){
			logger.error(strMessege, e);
		}
		
	}

	@Override
	public List<BgabGascZ011Dto> getSelectBTToFile(BgabGascZ011Dto bgabGascZ011Dto){
		return businessTravelDao.getSelectBTToFile(bgabGascZ011Dto);
	}

	@Override
	public BgabGascZ011Dto getSelectBTToFileInfo(BgabGascZ011Dto bgabGascZ011Dto){
		return businessTravelDao.getSelectBTToFileInfo(bgabGascZ011Dto);
	}

	@Override
	public int deleteBTToFile(List<BgabGascZ011Dto> bgabGascZ011IList){
		String fileResult = "";
		for(int i=0; i<bgabGascZ011IList.size(); i++){
			BgabGascZ011Dto fileInfo = bgabGascZ011IList.get(i);
			try {
				fileResult = FileUtil.deleteFile(fileInfo.getCorp_cd(), fileName, fileInfo.getOgc_fil_nm());
			} catch (IOException e) {
				logger.error(strMessege, e);
			}
		}
		Integer fileDRs = businessTravelDao.deleteBTToFile(bgabGascZ011IList);
		return fileDRs;
	}

	@Override
	public CommonMessage setApproval(BgabGascbt01 btReqDto, HttpServletRequest req) throws SessionException{
		BgabGascz002Dto userParam = new BgabGascz002Dto();
		userParam.setCorp_cd(btReqDto.getCorp_cd());
		userParam.setXusr_empno(btReqDto.getEeno());
		BgabGascz002Dto userInfo = commonJobDao.getXusrInfo(userParam);

		CommonMessage message = new CommonMessage();
		CommonApproval appInfo = new CommonApproval();
		appInfo.setDoc_no(btReqDto.getDoc_no());						// 문서번호
		appInfo.setSystem_code(btReqDto.getApp_type());					// 시스템코드
		appInfo.setTable_name("bgab_gascbt01");							// 업무 테이블이름
		appInfo.setRpts_eeno(userInfo.getXusr_empno());		// 상신자 사번
		appInfo.setRpts_dept(userInfo.getXusr_dept_code());	// 상신자 부서코드
		appInfo.setStep_code(userInfo.getXusr_step_code());	// 상신자 직위코드
		appInfo.setRpts_eeno_nm(userInfo.getXusr_name());		// 상신자 성명
		appInfo.setStep_nm(userInfo.getXusr_step_name());		// 직위 이름
		appInfo.setTitle_nm(HncisMessageSource.getMessage("bns_tr"));							// 업무 이름
		appInfo.setAppType(btReqDto.getApp_type());						// 전결권자 업무
		appInfo.setMax_level(5);	// 해외 결재 LEVEL
		appInfo.setCorp_cd(userInfo.getCorp_cd());


		// 결재요청
		CommonApproval commonApproval = ApprovalGasc.setApprovalRequestUseYn(appInfo);

		if(commonApproval.getResult().equals("Z")){
			// BPM API호출
			String processCode = ""; 	//프로세스 코드 (출장 신청 프로세스) - 프로세스 정의서 참조
			String statusCode = "";	//액티비티 코드 (출장 신청서작성) - 프로세스 정의서 참조
			String roleCode = "";   //담당자 역할코드
			if(btReqDto.getDom_abrd_scn_cd().equals(gubunAb)){
				processCode = pCode2; // 해외
				statusCode = sCode2;	//액티비티 코드 (출장 신청서작성) - 프로세스 정의서 참조
				roleCode = rCode2;   //담당자 역할코드
			}else{
				processCode = pCode; // 국내
				statusCode = sCode;	//액티비티 코드 (출장 신청서작성) - 프로세스 정의서 참조
				roleCode = rCode;   //담당자 역할코드
			}
			String bizKey = btReqDto.getDoc_no();	//신청서 번호
			String loginUserId = btReqDto.getEeno();	//로그인 사용자 아이디
			String comment = null;
			
			//역할정보
			List<String> approveList = commonApproval.getApproveList();
			List<String> managerList = new ArrayList<String>();
			managerList.add(adminId);

			BpmApiUtil.sendCompleteTask(processCode, bizKey, statusCode, loginUserId, roleCode, approveList, managerList);
			
			message.setMessage(HncisMessageSource.getMessage("APPROVE.0000"));
			message.setCode(btReqDto.getPgs_st_cd());
			message.setCode1(btReqDto.getIf_id());

		}else{
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			message.setErrorCode("E");
			message.setMessage(commonApproval.getMessage());
			message.setCode("");
			message.setCode1("");
		}

		return message;
	}

	@Override
	public CommonMessage setApprovalCancel(BgabGascbt01 btReqDto, HttpServletRequest req){
		CommonMessage message = new CommonMessage();

		if("".equals(StringUtil.isNullToString(btReqDto.getIf_id()))){
			btReqDto.setPgs_st_cd("0");
			int cnt = businessTravelDao.approveCancelBtToRequest(btReqDto);

			if(cnt > 0){
				// BPM API호출
				String processCode = ""; 	//프로세스 코드 (출장 신청 프로세스) - 프로세스 정의서 참조
				String statusCode = "";	//액티비티 코드 (출장 신청서작성) - 프로세스 정의서 참조
				String roleCode = "";  	//담당자 역할코드
				if(btReqDto.getDom_abrd_scn_cd().equals(gubunAb)){
					processCode = pCode2; // 해외
					statusCode = sCode2;	//액티비티 코드 (출장 신청서작성) - 프로세스 정의서 참조
					roleCode = rCode2;  	//담당자 역할코드
				}else{
					processCode = pCode; // 국내
					statusCode = sCode;	//액티비티 코드 (출장 신청서작성) - 프로세스 정의서 참조
					roleCode = rCode;  	//담당자 역할코드
				}
				String bizKey = btReqDto.getDoc_no();		//신청서 번호
				String loginUserId = btReqDto.getUpdr_eeno();		//로그인 사용자 아이디
				String comment = null;
				
				//역할정보
				List<String> approveList = new ArrayList<String>();
				List<String> managerList = new ArrayList<String>();
				managerList.add(adminId);
				
				BpmApiUtil.sendCollectTask(processCode, bizKey, statusCode, loginUserId, roleCode, approveList, managerList );
				
				message.setMessage(HncisMessageSource.getMessage("APPROVE.0002"));
			}else{
				message.setMessage(HncisMessageSource.getMessage("APPROVE.0003"));
			}
		}else{
			CommonApproval appInfo = new CommonApproval();
			appInfo.setIf_id(btReqDto.getIf_id());
			appInfo.setTable_name("bgab_gascbt01");
			appInfo.setCorp_cd(btReqDto.getCorp_cd());
			CommonApproval commonApproval = ApprovalGasc.setApprovalCancelProcess(appInfo);

			if(commonApproval.getResult().equals("Z")){
				// BPM API호출
				String processCode = ""; 	//프로세스 코드 (출장 신청 프로세스) - 프로세스 정의서 참조
				String statusCode = "";	//액티비티 코드 (출장 신청서작성) - 프로세스 정의서 참조
				String roleCode = "";  	//담당자 역할코드
				if(btReqDto.getDom_abrd_scn_cd().equals(gubunAb)){
					processCode = pCode2; // 해외
					statusCode = sCode2;	//액티비티 코드 (출장 신청서작성) - 프로세스 정의서 참조
					roleCode = rCode2;  	//담당자 역할코드
				}else{
					processCode = pCode; // 국내
					statusCode = sCode;	//액티비티 코드 (출장 신청서작성) - 프로세스 정의서 참조
					roleCode = rCode;  	//담당자 역할코드
				}
				String bizKey = btReqDto.getDoc_no();		//신청서 번호
				String loginUserId = btReqDto.getUpdr_eeno();		//로그인 사용자 아이디
				String comment = null;
				
				//역할정보
				List<String> approveList = new ArrayList<String>();
				List<String> managerList = new ArrayList<String>();
				managerList.add(adminId);
				
				BpmApiUtil.sendCollectTask(processCode, bizKey, statusCode, loginUserId, roleCode, approveList, managerList );
				
				message.setMessage(HncisMessageSource.getMessage("APPROVE.0002"));
			}else{
				message.setMessage(commonApproval.getMessage());
			}
		}

		return message;
	}

	@Override
	public String getSelectApprovalInfoToRequest(BgabGascbt01 bgabGascbt01){
		return businessTravelDao.getSelectApprovalInfoToRequest(bgabGascbt01);
	}

	@Override
	public int confirmBTToRequest(HttpServletRequest req, BgabGascbt01 bgabGascbt01) throws SessionException{
		int confirmCnt = businessTravelDao.confirmBTToRequest(bgabGascbt01);

		if(confirmCnt > 0){
			String fromEeno   = SessionInfo.getSess_name(req);
			String fromStepNm = SessionInfo.getSess_step_name(req);
			String toEeno     = bgabGascbt01.getEeno();

			CommonApproval commonApproval = new CommonApproval();
			commonApproval.setRdcs_eeno(toEeno);
			commonApproval.setCorp_cd(SessionInfo.getSess_corp_cd(req));

			String mailAdr = commonJobDao.getSelectInfoToEenoEmailAdr(commonApproval);

			if(StringUtil.isNullToString(bgabGascbt01.getMode()).equals("confirmCancel")){
//				Submit.confirmCancelEmail(fromEeno, fromStepNm, mailAdr, "Business Travel", bgabGascbt01.getSnb_rson_sbc());
			}else{

				// BPM API호출
				String processCode = ""; 	//프로세스 코드 (출장 신청 프로세스) - 프로세스 정의서 참조
				String roleCode = "";   //담당자 역할코드
				if(bgabGascbt01.getDom_abrd_scn_cd().equals(gubunAb)){
					processCode = pCode2; // 해외
					roleCode = rCode2;   //담당자 역할코드
				}else{
					processCode = pCode; // 국내
					roleCode = rCode;   //담당자 역할코드
				}
				String bizKey = bgabGascbt01.getDoc_no();	//신청서 번호
				String statusCode = "";
				String comment = null;
				//역할정보
				List<String> approveList = new ArrayList<String>();
				List<String> managerList = new ArrayList<String>();
				if(commonApproval.getApproveList() == null){
					approveList.add(adminId);
				}else{
					approveList = commonApproval.getApproveList();
				}
				managerList.add(adminId);
				
				if(bgabGascbt01.getMode().equals("confirm3")){ // 1차 확정
					// BPM API호출
					
					if(bgabGascbt01.getDom_abrd_scn_cd().equals(gubunAb)){
						statusCode = "GASBZ01420030";	// 해외
					}else{
						statusCode = "GASBZ01410030";	// 국내
					}
					String loginUserId = bgabGascbt01.getUpdr_eeno();	//로그인 사용자 아이디
					
					BpmApiUtil.sendCompleteTask(processCode, bizKey, statusCode, loginUserId, roleCode, approveList, managerList);
				}else if(bgabGascbt01.getMode().equals("afterCal")){ // 후정산
					// BPM API호출
					if(bgabGascbt01.getDom_abrd_scn_cd().equals(gubunAb)){
						statusCode = "GASBZ01420040";	// 해외
					}else{
						statusCode = "GASBZ01410040";	// 국내
					}
					String loginUserId = bgabGascbt01.getEeno();	//로그인 사용자 아이디
					
					BpmApiUtil.sendCompleteTask(processCode, bizKey, statusCode, loginUserId, roleCode, approveList, managerList);
				}else if(bgabGascbt01.getMode().equals("confirm")){ // 2차 확정
					// BPM API호출
					if(bgabGascbt01.getDom_abrd_scn_cd().equals(gubunAb)){
						statusCode = "GASBZ01420050";	// 해외
					}else{
						statusCode = "GASBZ01410050";	// 국내
					}
					String loginUserId = bgabGascbt01.getUpdr_eeno();	//로그인 사용자 아이디

					BpmApiUtil.sendFinalCompleteTask(processCode, bizKey, statusCode, loginUserId);
				}
				
				if(bgabGascbt01.getAcpc_pgs_st_cd().equals("3")){
//					Submit.confirmEmail(fromEeno, fromStepNm, mailAdr, "Business Travel");
				}
			}
		}

		return confirmCnt;
	}

	@Override
	public int checkBTToConfirmList(List<BgabGascbt01> bgabGascbt01){
		return businessTravelDao.checkBTToConfirmList(bgabGascbt01);
	}

	@Override
	public List<CommonSap> getExpenseExcelList(List<BgabGascbt04> reportCardList, List<BgabGascbt04> reportCashList){

		List<CommonSap> excelMergeList = new ArrayList<CommonSap>();
		List<BgabGascbt04> tempCardList = new ArrayList<BgabGascbt04>();
		List<BgabGascbt04> tempCashList = new ArrayList<BgabGascbt04>();

		double totalCard = 0.00;
		double totalCash = 0.00;

		NumberFormat df = new DecimalFormat("#.00");

		for(int i=0; i<reportCardList.size(); i++){
			BgabGascbt04 cardInfo = businessTravelDao.getSelectBTToReportInfo(reportCardList.get(i));
			tempCardList.add(cardInfo);
			totalCard += Double.parseDouble(cardInfo.getPrvs_amt());
		}

		for(int i=0; i<reportCashList.size(); i++){
			BgabGascbt04 cashInfo = businessTravelDao.getSelectBTToReportInfo(reportCashList.get(i));
			tempCashList.add(cashInfo);
			totalCash += Double.parseDouble(cashInfo.getPrvs_amt());
		}

		for(int i=0; i<tempCardList.size(); i++){
			CommonSap excelMerge = new CommonSap();
			excelMerge.setHeader01(tempCardList.get(i).getDoc_no()+"Z"+tempCardList.get(i).getEeno());	// GA number
			excelMerge.setHeader02(CurrentDateTime.getDate());											// Report date
			excelMerge.setHeader03(df.format(totalCard));												// Total Amt
			excelMerge.setHeader04("Z");																// Pay method
			excelMerge.setHeader05(tempCardList.get(i).getEeno());										// Employee no
			excelMerge.setHeader06(tempCardList.get(i).getEenm());										// Employee name
			excelMerge.setHeader07(tempCardList.get(i).getCost_cd());									// Cost center
			excelMerge.setHeader08("Production Team");													// CC Name
			excelMerge.setHeader09(CurrentDateTime.getDate(CurrentDateTime.getDate(), 5));				// Due date
			excelMerge.setHeader10(Integer.toString(i+1));												// Item no
			excelMerge.setHeader11(tempCardList.get(i).getBudg_no());									// GL account
			excelMerge.setHeader12(df.format(tempCardList.get(i).getPrvs_amt()));						// Amount
			excelMerge.setHeader13(tempCardList.get(i).getPrvs_dtl_nm());								// Category
			excelMergeList.add(excelMerge);
		}

		businessTravelDao.updateBTToReportBySapYn(reportCardList);

		for(int i=0; i<tempCashList.size(); i++){
			CommonSap excelMerge = new CommonSap();
			excelMerge.setHeader01(tempCashList.get(i).getDoc_no()+"F"+tempCashList.get(i).getEeno());	// GA number
			excelMerge.setHeader02(CurrentDateTime.getDate());											// Report date
			excelMerge.setHeader03(df.format(totalCash));												// Total Amt
			excelMerge.setHeader04("F");																// Pay method
			excelMerge.setHeader05(tempCashList.get(i).getEeno());										// Employee no
			excelMerge.setHeader06(tempCashList.get(i).getEenm());										// Employee name
			excelMerge.setHeader07(tempCashList.get(i).getCost_cd());									// Cost center
			excelMerge.setHeader08("Production Team");													// CC Name
			excelMerge.setHeader09(CurrentDateTime.getDate(CurrentDateTime.getDate(), 5));				// Due date
			excelMerge.setHeader10(Integer.toString(i+1));												// Item no
			excelMerge.setHeader11(tempCashList.get(i).getBudg_no());									// GL account
			excelMerge.setHeader12(df.format(tempCashList.get(i).getPrvs_amt()));						// Amount
			excelMerge.setHeader13(tempCashList.get(i).getPrvs_dtl_nm());								// Category
			excelMergeList.add(excelMerge);
		}
		businessTravelDao.updateBTToReportBySapYn(reportCashList);

		BgabGascbt04 travelerInfo = new BgabGascbt04();
		if(reportCardList.size() > 0){
			travelerInfo.setDoc_no(reportCardList.get(0).getDoc_no());
			travelerInfo.setEeno(reportCardList.get(0).getEeno());
		}else{
			travelerInfo.setDoc_no(reportCashList.get(0).getDoc_no());
			travelerInfo.setEeno(reportCashList.get(0).getEeno());
		}

		int NCnt = Integer.parseInt(businessTravelDao.getSelectCountBTToReportBySapYn(travelerInfo));

		BgabGascbt02 bgabGascbt02 = new BgabGascbt02();
		if(NCnt == 0){
			bgabGascbt02.setDoc_no(travelerInfo.getDoc_no());
			bgabGascbt02.setEeno(travelerInfo.getEeno());
			bgabGascbt02.setTot_arsl_refl_yn("Y");
			businessTravelDao.updateBTToTravelerBySapYn(bgabGascbt02);
		}
		return excelMergeList;
	}

	@Override
	public List<CommonSap> getExpenseExcelToSap(List<BgabGascbt04> travelerList){

		List<CommonSap> excelMergeList 		= new ArrayList<CommonSap>();

		double totalCard = 0.00;
		double totalCash = 0.00;

		NumberFormat df = new DecimalFormat("#.00");

		for(int i=0; i<travelerList.size(); i++){

			List<BgabGascbt04> reportCardList 	= new ArrayList<BgabGascbt04>();
			List<BgabGascbt04> reportCashList	= new ArrayList<BgabGascbt04>();
			List<BgabGascbt04> tempCardList 	= new ArrayList<BgabGascbt04>();
			List<BgabGascbt04> tempCashList 	= new ArrayList<BgabGascbt04>();
			List<BgabGascbt04> tempReportList = businessTravelDao.getSelectBTToReport(travelerList.get(i));


			for(int j=0; j<tempReportList.size(); j++){
				if(StringUtil.isNullToString(tempReportList.get(j).getStl_way_cd()).equals("BT001")){			// CARD
					reportCardList.add(tempReportList.get(j));
				}else if(StringUtil.isNullToString(tempReportList.get(j).getStl_way_cd()).equals("BT002")){		// CASH
					reportCashList.add(tempReportList.get(j));
				}
			}

			for(int j=0; j<reportCardList.size(); j++){
				BgabGascbt04 cardInfo = businessTravelDao.getSelectBTToReportInfo(reportCardList.get(j));
				reportCardList.get(j).setArsl_refl_yn("Y");
				tempCardList.add(cardInfo);
				if(cardInfo != null){
					totalCard += Double.parseDouble(cardInfo.getNatv_curr_amt());
				}
			}

			try{
				for(int j=0; j<reportCashList.size(); j++){
					BgabGascbt04 cashInfo = businessTravelDao.getSelectBTToReportInfo(reportCashList.get(j));
					reportCashList.get(j).setArsl_refl_yn("Y");
					tempCashList.add(cashInfo);
					if(cashInfo != null){
						totalCash += Double.parseDouble(cashInfo.getNatv_curr_amt());
					}

				}

				for(int j=0; j<tempCardList.size(); j++){
					CommonSap excelMerge = new CommonSap();
					excelMerge.setHeader01(tempCardList.get(j).getDoc_no()+tempCardList.get(j).getEeno()+"Z");	// GA number
					excelMerge.setHeader02(CurrentDateTime.getDate());											// Document date
					excelMerge.setHeader03(CurrentDateTime.getDate(CurrentDateTime.getDate(), 5));				// Due date
					excelMerge.setHeader04(tempCardList.get(j).getEeno());										// Employee no(Vendor)
					excelMerge.setHeader05("Z");																// Pay method
					excelMerge.setHeader06(tempCardList.get(j).getEenm());										// Employee name(Invoice No)
					excelMerge.setHeader07(Integer.toString(j+1));												// Item no
					excelMerge.setHeader08(tempCardList.get(j).getBudg_no());									// GL account
					excelMerge.setHeader09(tempCardList.get(j).getCost_cd());									// Cost center
					excelMerge.setHeader10(tempCardList.get(j).getPrvs_dtl_nm());								// Category
					excelMerge.setHeader11(df.format(Double.parseDouble(tempCardList.get(j).getNatv_curr_amt())));						// Amount
					excelMergeList.add(excelMerge);
				}
				businessTravelDao.updateBTToReportBySapYn(reportCardList);


				for(int j=0; j<tempCashList.size(); j++){
					CommonSap excelMerge = new CommonSap();
					excelMerge.setHeader01(tempCashList.get(j).getDoc_no()+tempCashList.get(j).getEeno()+"F");	// GA number
					excelMerge.setHeader02(CurrentDateTime.getDate());									// Document date
					excelMerge.setHeader03(CurrentDateTime.getDate(CurrentDateTime.getDate(), 5));									// Due date
					excelMerge.setHeader04(tempCashList.get(j).getEeno());										// Employee no(Vendor)
					excelMerge.setHeader05("F");																// Pay method
					excelMerge.setHeader06(tempCashList.get(j).getEenm());										// Employee name(Invoice No)
					excelMerge.setHeader07(Integer.toString(j+1));												// Item no
					excelMerge.setHeader08(tempCashList.get(j).getBudg_no());									// GL account
					excelMerge.setHeader09(tempCashList.get(j).getCost_cd());									// Cost center
					excelMerge.setHeader10(tempCashList.get(j).getPrvs_dtl_nm());								// Category
					excelMerge.setHeader11(df.format(Double.parseDouble(tempCashList.get(j).getNatv_curr_amt())));						// Amount
					excelMergeList.add(excelMerge);
				}
				businessTravelDao.updateBTToReportBySapYn(reportCashList);

				BgabGascbt04 travelerInfo = new BgabGascbt04();
				boolean flagChk = false;

				if(reportCardList.size() > 0){
					travelerInfo.setDoc_no(reportCardList.get(0).getDoc_no());
					travelerInfo.setEeno(reportCardList.get(0).getEeno());
					flagChk = true;
				}else if (reportCashList.size() > 0){
					travelerInfo.setDoc_no(reportCashList.get(0).getDoc_no());
					travelerInfo.setEeno(reportCashList.get(0).getEeno());
					flagChk = true;
				}
				else
				{
					travelerInfo.setDoc_no("");
					travelerInfo.setEeno("");
					flagChk = false;
				}

				if(flagChk){
					int NCnt = Integer.parseInt(businessTravelDao.getSelectCountBTToReportBySapYn(travelerInfo));
					BgabGascbt02 bgabGascbt02 = new BgabGascbt02();
					if(NCnt == 0){
						bgabGascbt02.setDoc_no(travelerInfo.getDoc_no());
						bgabGascbt02.setEeno(travelerInfo.getEeno());
						bgabGascbt02.setTot_arsl_refl_yn("Y");
						businessTravelDao.updateBTToTravelerBySapYn(bgabGascbt02);
					}
				}
			}catch(Exception e){
				logger.error("messege", e);
			}
		}
		return excelMergeList;
	}

	@Override
	public int getExpenseVendorList(List<BgabGascbt04> reportVendorList){

		businessTravelDao.updateBTToReportBySapYn(reportVendorList);

		BgabGascbt04 travelerInfo = new BgabGascbt04();
		if(reportVendorList.size() > 0){
			travelerInfo.setDoc_no(reportVendorList.get(0).getDoc_no());
			travelerInfo.setEeno(reportVendorList.get(0).getEeno());
		}

		int NCnt = Integer.parseInt(businessTravelDao.getSelectCountBTToReportBySapYn(travelerInfo));
		BgabGascbt02 bgabGascbt02 = new BgabGascbt02();
		if(NCnt == 0){
			bgabGascbt02.setDoc_no(travelerInfo.getDoc_no());
			bgabGascbt02.setEeno(travelerInfo.getEeno());
			bgabGascbt02.setTot_arsl_refl_yn("Y");
			businessTravelDao.updateBTToTravelerBySapYn(bgabGascbt02);
		}
		return NCnt;
	}

	@Override
	public int getExpenseCancelList(List<BgabGascbt04> reportCancelList){

		int uCnt = businessTravelDao.updateBTToReportBySapYn(reportCancelList);

		BgabGascbt04 travelerInfo = new BgabGascbt04();
		if(reportCancelList.size() > 0){
			travelerInfo.setDoc_no(reportCancelList.get(0).getDoc_no());
			travelerInfo.setEeno(reportCancelList.get(0).getEeno());
		}

		BgabGascbt02 bgabGascbt02 = new BgabGascbt02();
		bgabGascbt02.setDoc_no(travelerInfo.getDoc_no());
		bgabGascbt02.setEeno(travelerInfo.getEeno());
		bgabGascbt02.setTot_arsl_refl_yn("N");

		businessTravelDao.updateBTToTravelerBySapYn(bgabGascbt02);
		return uCnt;
	}

	@Override
	public CommonMessage getBudgetInfoCheck(BgabGascbt01 btReqDto) throws Exception{

		CommonMessage message = new CommonMessage();
		RfcBudgetCheckVo o_BudgetInfo = new RfcBudgetCheckVo();

		boolean chkBudget = true;

		try{
			// TRAVELER 개인별 총 비용계산
			// 1. TRAVELER LIST 가져오기
//			List<BgabGascbtDto> travelerList = businessTravelDao.getSelectTravelerToBudget(btReqDto);
			BgabGascbt02 param = new BgabGascbt02();
			param.setDoc_no(btReqDto.getDoc_no());
			List<BgabGascbt02> travelerList = businessTravelDao.getSelectBTToRequestByTraveler(param);
			// SWITCH 정보
			BgabGascz005Dto bgabGascz005Dto = new BgabGascz005Dto();
			bgabGascz005Dto.setXcod_knd("X0020");
			bgabGascz005Dto.setXcod_code("BT");

			BgabGascz005Dto switchInfo = systemDao.getSelectSwitchToBudgetCheck(bgabGascz005Dto);

//			switchInfo = new BgabGascz005Dto();

			for(int i=0; i<travelerList.size(); i++){
				// 예산체크
				RfcBudgetCheck rfc = new RfcBudgetCheck(btReqDto.getBudg_type());
				RfcBudgetCheckVo i_BudgetInfo = new RfcBudgetCheckVo();

				i_BudgetInfo.setI_gubn(btReqDto.getBudg_type());
				i_BudgetInfo.setI_date(CurrentDateTime.getDate());
				i_BudgetInfo.setI_gjahr(CurrentDateTime.getYear());
				i_BudgetInfo.setI_kostl(travelerList.get(i).getCost_cd());
				if("D".equals(btReqDto.getBudg_type())){
					i_BudgetInfo.setI_hkont(btReqDto.getBudg_no());
				}else{
					i_BudgetInfo.setI_hkont(btReqDto.getBudg_text());
				}

				double balanceAmt = 0;
				if("Y".equals(switchInfo.getXcod_hname())){
					o_BudgetInfo = rfc.getResult(i_BudgetInfo);
					balanceAmt = Double.parseDouble(StringUtil.isNullToString(o_BudgetInfo.getO_balance().replaceAll(",", ""),"0"));

					if(StringUtil.isNullToString(balanceAmt).equals("")){
						balanceAmt = 0;
					}
				}else{
					o_BudgetInfo.setO_actual("Z");
					chkBudget = true;
				}

				if("E".equals(o_BudgetInfo.getO_actual())){
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					message.setErrorCode("E");
					message.setMessage(o_BudgetInfo.getGs_msg());
					chkBudget = false;
				}else{
					double totAmt = Double.parseDouble(travelerList.get(i).getFlht_utz_amt())
								  + Double.parseDouble(travelerList.get(i).getBztp_lodg_exp())
								  + Double.parseDouble(travelerList.get(i).getRent_amt())
								  + Double.parseDouble(travelerList.get(i).getVs_ptt_exp())
								  + Double.parseDouble(travelerList.get(i).getEtc_amt()) ;

					if("Y".equals(switchInfo.getXcod_hname()) && totAmt > balanceAmt){
						// 예산부족
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						message.setErrorCode("E");
						message.setMessage(HncisMessageSource.getMessage("BUDGET.0001"));
						chkBudget = false;
					}
				}
			}

//			// TRAVELER 조직별 총 비용계산
//			// parameter Information : Department D, WBS W, Internal order I
//			RfcBudgetCheck rfc = new RfcBudgetCheck(btReqDto.getBudg_type());
//			double poaAmt_t			= 0.00;		// 직급별 기준값(total)
//			double poaAmt 			= 0.00;		// 직급별 기준값(항공, 픽업)
//			double poaAmt_d			= 0.00;		// 직급별 기준값(일별)
//			double holdingAmt 		= 0.00;		// 코스트센터별 홀딩예산
//			double holdingMinusAmt 	= 0.00;		// 코스트센터별 회계처리 완료 데이터
//			double holdingAmtTemp	= 0.00;
//			double balanceAmt 		= 0.00;		// 코스트센터별 잔여예산
//			double calAmt 			= 0.00;		// 계산비용
//
//
//			for(int i=0; i<travelerList.size(); i++){
//				/*
//				// 직급별 금액 분류
//				poaAmt = 0.00;
//				if(StringUtil.isNullToString(travelerList.get(i).getStep_code()).equals("50") || StringUtil.isNullToString(travelerList.get(i).getStep_code()).equals("51")){
//					poaAmt 		= travelerList.get(i).getPoa_g1_amt();			// DIRECTOR, EXECUTIVE DIRECTOR
//					poaAmt_d 	= travelerList.get(i).getPoa_g1_amt_d();		// DIRECTOR, EXECUTIVE DIRECTOR
//				}else if(StringUtil.isNullToString(travelerList.get(i).getStep_code()).equals("41")){
//					poaAmt 		= travelerList.get(i).getPoa_g2_amt();			// GENERAL MANAGER
//					poaAmt_d 	= travelerList.get(i).getPoa_g2_amt_d();		// GENERAL MANAGER
//				}else if(StringUtil.isNullToString(travelerList.get(i).getStep_code()).equals("30") || StringUtil.isNullToString(travelerList.get(i).getStep_code()).equals("40")){
//					poaAmt 		= travelerList.get(i).getPoa_g3_amt();			// MANAGER, SR. MANAGER
//					poaAmt_d 	= travelerList.get(i).getPoa_g3_amt_d();		// MANAGER, SR. MANAGER
//				}else{
//					poaAmt 		= travelerList.get(i).getPoa_g4_amt();			// OTHERS
//					poaAmt_d 	= travelerList.get(i).getPoa_g4_amt_d();		// OTHERS
//				}
//
//				poaAmt_t = poaAmt + (poaAmt_d * diffDay);
//				*/
//
//				// 출장일자 계산
//				long diffDay = CurrentDateTime.diffOfDate(travelerList.get(i).getStrt_ymd(), travelerList.get(i).getFnh_ymd())+1;
//
//				poaAmt_t = travelerList.get(i).getFlht_utz_amt() + travelerList.get(i).getBztp_lodg_exp() + travelerList.get(i).getRent_amt() + travelerList.get(i).getVs_ptt_exp() + travelerList.get(i).getEtc_amt();
//
//				System.out.println("getFlht_utz_amt="+travelerList.get(i).getFlht_utz_amt());
//				System.out.println("getBztp_lodg_exp="+travelerList.get(i).getBztp_lodg_exp());
//				System.out.println("getRent_amt="+travelerList.get(i).getRent_amt());
//				System.out.println("getVs_ptt_exp="+travelerList.get(i).getVs_ptt_exp());
//				System.out.println("getEtc_amt="+travelerList.get(i).getEtc_amt());
//				System.out.println("poaAmt_t="+poaAmt_t);
//
//				RfcBudgetCheckVo i_BudgetInfo = new RfcBudgetCheckVo();
//
//				// Department D, WBS W, Internal order I
//				i_BudgetInfo.setI_gubn(btReqDto.getBudg_type());
//				i_BudgetInfo.setI_gjahr(CurrentDateTime.getYear());
//				i_BudgetInfo.setI_date(CurrentDateTime.getDate());
//				i_BudgetInfo.setI_kostl(travelerList.get(i).getCost_cd());
//				i_BudgetInfo.setI_hkont(travelerList.get(i).getBudg_no());
//
//				holdingAmt 		= travelerList.get(i).getH_budget();
//				holdingMinusAmt = travelerList.get(i).getH_minus_budget();
//				holdingAmtTemp 	= holdingAmt - holdingMinusAmt;
//
//				if(holdingAmtTemp < 0){
//					holdingAmtTemp = 0;
//				}
//
//				System.out.println("holdingAmtTemp="+holdingAmtTemp);
//				calAmt			= poaAmt_t+holdingAmtTemp;
//
//				/*
//				System.out.println("발생비용="+poaAmt_t);
//				System.out.println("홀딩예산="+holdingAmt);
//				System.out.println("홀딩마이너스예산="+holdingMinusAmt);
//				System.out.println("발생총비용="+calAmt);
//				System.out.println("잔여예산="+balanceAmt);
//				*/
//
//				travelerList.get(i).setTemp_term((int) diffDay);		// 기간
//				travelerList.get(i).setTemp_amt01(poaAmt);				// 항공,픽업 직급별 비용
//				travelerList.get(i).setTemp_amt02(poaAmt_d);			// 일별 계산 직급별 비용
//				travelerList.get(i).setTemp_amt03(holdingAmt);			// 홀딩예산
//				travelerList.get(i).setTemp_amt04(holdingMinusAmt);		// 홀딩마이너스예산
//				travelerList.get(i).setTemp_amt05(calAmt);				// 총계산비용 = 총발생비용 + 홀딩예산 - 홀딩마이너스예산
//				travelerList.get(i).setT_budget(poaAmt_t);				// 총 발생비용 = 항공/픽업 비용  + (일별 비용 * 기간)
//
//				if(StringUtil.isNullToString(switchInfo.getXcod_hname()).equals("Y")){
//					RfcBudgetCheckVo o_BudgetInfo = rfc.getResult(i_BudgetInfo);
//					balanceAmt = Double.parseDouble(StringUtil.isNullToString(o_BudgetInfo.getO_balance(),"0"));
//
//					if(StringUtil.isNullToString(balanceAmt).equals("")){
//						balanceAmt = 0;
//					}
//					System.out.println("balanceAmt="+balanceAmt);
//					System.out.println("calAmt="+calAmt);
//					if("E".equals(o_BudgetInfo.getO_actual())){
//						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//						message.setMessage(o_BudgetInfo.getGs_msg());
//						message.setCode(travelerList.get(i).getDept_code());
//						message.setCode1(travelerList.get(i).getEeno());
//
//						chkBudget = false;
//						break;
//					}else{
//						if(calAmt > balanceAmt){
//							// 예산부족
//							TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//							message.setMessage(HmbMessageSource.getMessage("BUDGET.0001"));
//							message.setCode(travelerList.get(i).getDept_code());
//							message.setCode1(travelerList.get(i).getEeno());
//
//							chkBudget = false;
//							break;
//						}
//					}
//				}
//
//				travelerList.get(i).setTemp_amt06(balanceAmt);					// SAP 잔여예산
//				businessTravelDao.updateTravelerTotBudget(travelerList.get(i));
//			}
		}catch (Exception e) {
			logger.error(strMessege, e);
		}
		if(chkBudget){
			message.setResult("Z");
		}else{
			message.setResult("E");
		}
		return message;
	}

	@Override
	public int getSelectCountCountryManagement(BgabGascz005Dto bgabGascz005Dto){
		return Integer.parseInt(businessTravelDao.getSelectCountCountryManagement(bgabGascz005Dto));
	}

	@Override
	public List<BgabGascz005Dto> getSelectCountryManagement(BgabGascz005Dto bgabGascz005Dto){
		return businessTravelDao.getSelectCountryManagement(bgabGascz005Dto);
	}

	@Override
	public int saveListToCountryManagement(List<BgabGascz005Dto> dtoI, List<BgabGascz005Dto> dtoU){
		int iCnt = businessTravelDao.insertListToCountryManagement(dtoI);
		int uCnt = businessTravelDao.updateListToCountryManagement(dtoU);
		return iCnt+uCnt;
	}

	@Override
	public int deleteListToCountryManagement(List<BgabGascz005Dto> dtoD){
		return businessTravelDao.deleteListToCountryManagement(dtoD);
	}

	@Override
	public int getSelectCountBTToSapMngrByTaveler(BgabGascbtDto bgabGascbtDto){
		return Integer.parseInt(businessTravelDao.getSelectCountBTToSapMngrByTaveler(bgabGascbtDto));
	}

	@Override
	public List<BgabGascbtDto> getSelectBTToSapMngrByTaveler(BgabGascbtDto bgabGascbtDto){
		return businessTravelDao.getSelectBTToSapMngrByTaveler(bgabGascbtDto);
	}

	@Override
	public int getSelectCountExpenseManagement(BgabGascbt06 bgabGascbt06){
		return Integer.parseInt(businessTravelDao.getSelectCountExpenseManagement(bgabGascbt06));
	}

	@Override
	public List<BgabGascbt06> getSelectExpenseManagement(BgabGascbt06 bgabGascbt06){
		return businessTravelDao.getSelectExpenseManagement(bgabGascbt06);
	}

	@Override
	public int getSelectCountBudgetView(BgabGascbt02 bgabGascbt02){
		return Integer.parseInt(businessTravelDao.getSelectCountBudgetView(bgabGascbt02));
	}

	@Override
	public List<BgabGascbt02> getSelectBudgetView(BgabGascbt02 bgabGascbt02){

		RfcBudgetCheck rfc = new RfcBudgetCheck(bgabGascbt02.getBudg_type());
		RfcBudgetCheckVo i_BudgetInfo = new RfcBudgetCheckVo();
		i_BudgetInfo.setI_gubn(bgabGascbt02.getBudg_type());
		i_BudgetInfo.setI_date(CurrentDateTime.getDate());
		i_BudgetInfo.setI_gjahr(CurrentDateTime.getYear());
		i_BudgetInfo.setI_kostl(bgabGascbt02.getCost_cd());
		i_BudgetInfo.setI_hkont(bgabGascbt02.getBudg_no());

		RfcBudgetCheckVo o_BudgetInfo = null;
		try {
			o_BudgetInfo = rfc.getResult(i_BudgetInfo);
		} catch (Exception e) {
			logger.error(strMessege, e);
		}

		if(StringUtil.isNullToString(bgabGascbt02.getBudg_type()).equals("D")){
			o_BudgetInfo.setGs_msg("");
		}

		if(StringUtil.isNullToString(o_BudgetInfo.getO_balance()).equals("")){
			bgabGascbt02.setBalance_amt("0");
		}else{
			bgabGascbt02.setBalance_amt(o_BudgetInfo.getO_balance());
		}
		bgabGascbt02.setBudg_result(o_BudgetInfo.getGs_msg());

		List<BgabGascbt02> budgetList = businessTravelDao.getSelectBudgetView(bgabGascbt02);

		for(int i=0; i<budgetList.size(); i++){
			budgetList.get(i).setBalance_amt(bgabGascbt02.getBalance_amt());
			budgetList.get(i).setBudg_result(o_BudgetInfo.getGs_msg());
		}

		if(budgetList.size() == 0){
			BgabGascbt02 setBudgetInfo = new BgabGascbt02();

			setBudgetInfo.setCost_cd(bgabGascbt02.getCost_cd());
			setBudgetInfo.setBudg_no(bgabGascbt02.getBudg_no());
			setBudgetInfo.setPrvs_amt("0");
			setBudgetInfo.setNatv_curr_amt("0");
			setBudgetInfo.setBalance_amt(bgabGascbt02.getBalance_amt());
			setBudgetInfo.setCost_nm(commonJobDao.getSelectCostCenterName(bgabGascbt02.getCost_cd()));
			setBudgetInfo.setBudg_result(o_BudgetInfo.getGs_msg());

			budgetList.add(setBudgetInfo);
		}

		return budgetList;
	}

	@Override
	public int saveListToExpenseManagement(List<BgabGascbt06> dtoI, List<BgabGascbt06> dtoU){
		int iCnt = businessTravelDao.insertListToExpenseManagement(dtoI);
		int uCnt = businessTravelDao.updateListToExpenseManagement(dtoU);
		return iCnt+uCnt;
	}

	@Override
	public int deleteListToExpenseManagement(List<BgabGascbt06> dtoD){
		return businessTravelDao.deleteListToExpenseManagement(dtoD);
	}

	@Override
	public int submitBTToReport(BgabGascbt02 bgabGascbt02){
		return businessTravelDao.updateBTToTravelerByStatus(bgabGascbt02);
	}

	@Override
	public CommonMessage confirmToExpenseManagement(List<BgabGascbt02> travelerList, HttpServletRequest req){
		CommonMessage message = new CommonMessage();
		if(StringUtil.isNullToString(travelerList.get(0).getMode()).equals("confirmCancel")){
			businessTravelDao.confirmToExpenseManagement(travelerList);
		}else{
			for(int i=0; i<travelerList.size(); i++){
				BgabGascbt01 dto = new BgabGascbt01();
				dto.setDoc_no(travelerList.get(i).getDoc_no());
				dto.setPgs_st_cd(travelerList.get(i).getPgs_st_cd());
				dto.setUpdr_eeno(travelerList.get(i).getUpdr_eeno());
				dto.setAcpc_pgs_st_cd(travelerList.get(i).getAcpc_pgs_st_cd());
				dto.setCorp_cd(travelerList.get(i).getCorp_cd());

				int confirmCnt = businessTravelDao.confirmBTToRequest(dto);

				if(confirmCnt > 0){
					try {
						String fromEeno;

						fromEeno = SessionInfo.getSess_name(req);
						String fromStepNm = SessionInfo.getSess_step_name(req);
						String toEeno     = travelerList.get(i).getEeno();

						CommonApproval commonApproval = new CommonApproval();
						commonApproval.setRdcs_eeno(toEeno);
						commonApproval.setCorp_cd(SessionInfo.getSess_corp_cd(req));

						String mailAdr = commonJobDao.getSelectInfoToEenoEmailAdr(commonApproval);

						if(dto.getAcpc_pgs_st_cd().equals("3")){
//							Submit.confirmEmail(fromEeno, fromStepNm, mailAdr, "Business Travel");
						}
					} catch (SessionException e) {
						logger.error(strMessege, e);
					}
				}
			}
		}
		return message;
	}

	@Override
	public boolean setExpenseTemplatMake(BgabGascbt02 bgabGascbt02){

		String appArrayVal = "";
		String realFilePath = "";
		String destFilePath = "";

		Map<String,Object> map = new HashMap();
		BgabGascbtExcelDto excelInfo = new BgabGascbtExcelDto();
		NumberFormat df = new DecimalFormat("#,###.00");

		// traveler 정보
		BgabGascbtDto travelerInfo = businessTravelDao.getSelectBTToTravelerExcelInfo(bgabGascbt02);
		// Co_travelers List
		List<BgabGascbtDto> travelersList = businessTravelDao.getSelectBTToTravelersExcelList(bgabGascbt02);
		// Expense List
		List<BgabGascbt04> expenseList = businessTravelDao.getSelectBTToExpenseExcelList(bgabGascbt02);

		try {
			if(travelerInfo != null){
				CommonApproval commonApproval = new CommonApproval();
				commonApproval.setIf_id(travelerInfo.getIf_id());
				commonApproval.setSystem_code("BT");
				appArrayVal = commonJobDao.getSelectApprovalInfo(commonApproval);
			}

			if(!StringUtil.isNullToString(appArrayVal).equals("")){

				appArrayVal = appArrayVal.replace("|", "!");
				String[] appArray01 = appArrayVal.split("#");
				String[] appArray02 = appArray01[2].split("!");
				String[] appArray02_0 = appArray02[0].split("@");
				String[] appArray02_1 = appArray02[1].split("@");
				String[] appArray02_2 = appArray02[2].split("@");
				String[] appArray02_3 = null;
				if(appArray02.length > 3){
					appArray02_3 = appArray02[3].split("@");
				}

				excelInfo.setApp01_name(appArray02_0[1]);						// 상신자 이름
				excelInfo.setApp01_date(appArray02_0[3]);						// 상신일자

				if(StringUtil.isNullToString(appArray02_1[0]).equals("COORDINATOR")){
					excelInfo.setApp02_name("");								// 결재자 이름
					excelInfo.setApp02_date("");								// 결재일자
					excelInfo.setApp03_name(appArray02_1[1]);
					excelInfo.setApp03_date(appArray02_1[3]);
					excelInfo.setApp04_name(appArray02_2[1]);
					excelInfo.setApp04_date(appArray02_2[3]);
				}else if(StringUtil.isNullToString(appArray02_1[0]).equals("H.O.D")){
					if(StringUtil.isNullToString(appArray02_1[2]).equals("EXECUTIVE DIRECTOR")){
						excelInfo.setApp03_name(appArray02_1[1]);
						excelInfo.setApp03_date(appArray02_1[3]);
						if(StringUtil.isNullToString(appArray02_2[0]).equals("H.O.S.D")){
							excelInfo.setApp04_name(appArray02_2[1]);
							excelInfo.setApp04_date(appArray02_2[3]);
						}
					}else{
						excelInfo.setApp02_name(appArray02_1[1]);
						excelInfo.setApp02_date(appArray02_1[3]);
						if(appArray02.length > 3 && appArray02_2 != null && StringUtil.isNullToString(appArray02_2[0]).equals("COORDINATOR")){
							excelInfo.setApp03_name(appArray02_2[1]);
							excelInfo.setApp03_date(appArray02_2[3]);
						}

						if(appArray02.length > 3 && appArray02_3 != null && appArray02_3.length > 3){
							if(StringUtil.isNullToString(appArray02_3[0]).equals("H.O.S.D")){
								excelInfo.setApp04_name(appArray02_3[1]);
								excelInfo.setApp04_date(appArray02_3[3]);
							}
						}
					}
				}else if(StringUtil.isNullToString(appArray02_1[0]).equals("H.O.S.D")){
					excelInfo.setApp04_name(appArray02_1[1]);
					excelInfo.setApp04_date(appArray02_1[3]);
				}
			}

			excelInfo.setCoordi_nm(travelerInfo.getCoordi_nm());		// G.A COORDI NAME
			excelInfo.setManager_nm(travelerInfo.getManager_nm());		// G.A MANAGER NAME
			excelInfo.setBudg_no(travelerInfo.getBudg_no());			// GL ACCOUNT
			excelInfo.setEeno(travelerInfo.getEeno());					// 사번
			excelInfo.setEe_nm(travelerInfo.getEe_nm());				// NAME
			excelInfo.setCost_cd(travelerInfo.getCost_cd());			// COST CENTER
			excelInfo.setOps_nm(travelerInfo.getOps_nm());				// DEPT NAME
			excelInfo.setDest_nat_nm(travelerInfo.getDest_nat_nm());	// Place to Visit
			excelInfo.setStrt_ymd(travelerInfo.getStrt_ymd());			// 출발일자
			excelInfo.setFnh_ymd(travelerInfo.getFnh_ymd());			// 도착일자

			long diffDay = CurrentDateTime.diffOfDate(StringUtil.trimChar(CurrentDateTime.getDateFormatKr(travelerInfo.getStrt_ymd()), "/"), StringUtil.trimChar(CurrentDateTime.getDateFormatKr(travelerInfo.getFnh_ymd()), "/"));
			excelInfo.setNights((int)diffDay);
			excelInfo.setDays((int)(diffDay+1));

			String temp_travlers = "";
			for(int i=0; i<travelersList.size(); i++){
				temp_travlers += (i+1)+") "+ travelersList.get(i).getEe_nm() + " ";
			}
			excelInfo.setCo_travelers(temp_travlers);						// CO Travelers
			excelInfo.setVsit_purp_sbc(travelerInfo.getVsit_purp_sbc());	// 방문목적

			List<BgabGascbt04> accommodationCardList = new ArrayList<BgabGascbt04>();
			List<BgabGascbt04> accommodationCashList = new ArrayList<BgabGascbt04>();
			List<BgabGascbt04> transportCardList = new ArrayList<BgabGascbt04>();
			List<BgabGascbt04> transportCashList = new ArrayList<BgabGascbt04>();
			List<BgabGascbt04> mealCardList = new ArrayList<BgabGascbt04>();
			List<BgabGascbt04> mealCashList = new ArrayList<BgabGascbt04>();
			List<BgabGascbt04> othersCardList = new ArrayList<BgabGascbt04>();
			List<BgabGascbt04> othersCashList = new ArrayList<BgabGascbt04>();

			double accommodationCardTamt = 0;
			double accommodationCardPamt = 0;
			double accommodationCardApl = 0;
			double accommodationCashTamt = 0;
			double accommodationCashPamt = 0;
			double accommodationCashApl = 0;
			double transportCardTamt = 0;
			double transportCardPamt = 0;
			double transportCardApl = 0;
			double transportCashTamt = 0;
			double transportCashPamt = 0;
			double transportCashApl = 0;
			double mealCardTamt = 0;
			double mealCardPamt = 0;
			double mealCardApl = 0;
			double mealCashTamt = 0;
			double mealCashPamt = 0;
			double mealCashApl = 0;
			double othersCardTamt = 0;
			double othersCardPamt = 0;
			double othersCardApl = 0;
			double othersCashTamt = 0;
			double othersCashPamt = 0;
			double othersCashApl = 0;

			double totalGeneralExpenditureCard = 0;
			double totalPaymentCard = 0;
			double totalGeneralExpenditureCash = 0;
			double totalPaymentCash = 0;

			for(int i=0; i<expenseList.size(); i++){
				if(StringUtil.isNullToString(expenseList.get(i).getStl_way_cd()).equals("BT001")){					// CARD

					if(StringUtil.isNullToString(expenseList.get(i).getPrvs_scn_cd()).equals("CA001")){				// ACCOMMODATION
						accommodationCardList.add(expenseList.get(i));
						accommodationCardPamt += Double.parseDouble(expenseList.get(i).getPrvs_amt());
						accommodationCardTamt += Double.parseDouble(expenseList.get(i).getNatv_curr_amt());
						accommodationCardApl = expenseList.get(i).getApl_xr();
						totalGeneralExpenditureCard += Double.parseDouble(expenseList.get(i).getNatv_curr_amt());
						totalPaymentCard += Double.parseDouble(expenseList.get(i).getNatv_curr_amt());
					}else if(StringUtil.isNullToString(expenseList.get(i).getPrvs_scn_cd()).equals("CA002")){		// TRANSPORT
						transportCardList.add(expenseList.get(i));
						transportCardPamt += Double.parseDouble(expenseList.get(i).getPrvs_amt());
						transportCardTamt += Double.parseDouble(expenseList.get(i).getNatv_curr_amt());
						transportCardApl = expenseList.get(i).getApl_xr();
						totalGeneralExpenditureCard += Double.parseDouble(expenseList.get(i).getNatv_curr_amt());
						totalPaymentCard += Double.parseDouble(expenseList.get(i).getNatv_curr_amt());

						if(StringUtil.isNullToString(expenseList.get(i).getPrvs_dtl_cd()).equals("B01")){
							excelInfo.setAirplane("X");
						}else if(StringUtil.isNullToString(expenseList.get(i).getPrvs_dtl_cd()).equals("B04")){
							excelInfo.setTrain("X");
						}else if(StringUtil.isNullToString(expenseList.get(i).getPrvs_dtl_cd()).equals("B05")){
							excelInfo.setOwn("X");
						}
					}else if(StringUtil.isNullToString(expenseList.get(i).getPrvs_scn_cd()).equals("CA003")){		// MEAL
						mealCardList.add(expenseList.get(i));
						mealCardPamt += Double.parseDouble(expenseList.get(i).getPrvs_amt());
						mealCardTamt += Double.parseDouble(expenseList.get(i).getNatv_curr_amt());
						mealCardApl = expenseList.get(i).getApl_xr();
						totalGeneralExpenditureCard += Double.parseDouble(expenseList.get(i).getNatv_curr_amt());
						totalPaymentCard += Double.parseDouble(expenseList.get(i).getNatv_curr_amt());
					}else if(StringUtil.isNullToString(expenseList.get(i).getPrvs_scn_cd()).equals("CA004")){		// OTHERS
						othersCardList.add(expenseList.get(i));
						othersCardPamt += Double.parseDouble(expenseList.get(i).getPrvs_amt());
						othersCardTamt += Double.parseDouble(expenseList.get(i).getNatv_curr_amt());
						othersCardApl = expenseList.get(i).getApl_xr();
						totalGeneralExpenditureCard += Double.parseDouble(expenseList.get(i).getNatv_curr_amt());
						totalPaymentCard += Double.parseDouble(expenseList.get(i).getNatv_curr_amt());
					}
				}else if(StringUtil.isNullToString(expenseList.get(i).getStl_way_cd()).equals("BT002")){			// CASH

					if(StringUtil.isNullToString(expenseList.get(i).getPrvs_scn_cd()).equals("CA001")){				// ACCOMMODATION
						accommodationCashList.add(expenseList.get(i));
						accommodationCashPamt += Double.parseDouble(expenseList.get(i).getPrvs_amt());
						accommodationCashTamt += Double.parseDouble(expenseList.get(i).getNatv_curr_amt());
						accommodationCashApl = expenseList.get(i).getApl_xr();
						totalGeneralExpenditureCash += Double.parseDouble(expenseList.get(i).getNatv_curr_amt());
						totalPaymentCash += Double.parseDouble(expenseList.get(i).getNatv_curr_amt());
					}else if(StringUtil.isNullToString(expenseList.get(i).getPrvs_scn_cd()).equals("CA002")){		// TRANSPORT
						transportCashList.add(expenseList.get(i));
						transportCashPamt += Double.parseDouble(expenseList.get(i).getPrvs_amt());
						transportCashTamt += Double.parseDouble(expenseList.get(i).getNatv_curr_amt());
						transportCashApl = expenseList.get(i).getApl_xr();
						totalGeneralExpenditureCash += Double.parseDouble(expenseList.get(i).getNatv_curr_amt());
						totalPaymentCash += Double.parseDouble(expenseList.get(i).getNatv_curr_amt());

						if(StringUtil.isNullToString(expenseList.get(i).getPrvs_dtl_cd()).equals("B01")){
							excelInfo.setAirplane("X");
						}else if(StringUtil.isNullToString(expenseList.get(i).getPrvs_dtl_cd()).equals("B04")){
							excelInfo.setTrain("X");
						}else if(StringUtil.isNullToString(expenseList.get(i).getPrvs_dtl_cd()).equals("B05")){
							excelInfo.setOwn("X");
						}
					}else if(StringUtil.isNullToString(expenseList.get(i).getPrvs_scn_cd()).equals("CA003")){		// MEAL
						mealCashList.add(expenseList.get(i));
						mealCashPamt += Double.parseDouble(expenseList.get(i).getPrvs_amt());
						mealCashTamt += Double.parseDouble(expenseList.get(i).getNatv_curr_amt());
						mealCashApl = expenseList.get(i).getApl_xr();
						totalGeneralExpenditureCash += Double.parseDouble(expenseList.get(i).getNatv_curr_amt());
						totalPaymentCash += Double.parseDouble(expenseList.get(i).getNatv_curr_amt());
					}else if(StringUtil.isNullToString(expenseList.get(i).getPrvs_scn_cd()).equals("CA004")){		// OTHERS
						othersCashList.add(expenseList.get(i));
						othersCashPamt += Double.parseDouble(expenseList.get(i).getPrvs_amt());
						othersCashTamt += Double.parseDouble(expenseList.get(i).getNatv_curr_amt());
						othersCashApl = expenseList.get(i).getApl_xr();
						totalGeneralExpenditureCash += Double.parseDouble(expenseList.get(i).getNatv_curr_amt());
						totalPaymentCash += Double.parseDouble(expenseList.get(i).getNatv_curr_amt());
					}
				}
				expenseList.get(i).setPrvs_amt(StringUtil.replaceComma(df.format(Double.parseDouble(expenseList.get(i).getPrvs_amt())))+expenseList.get(i).getCurr_nm());
				expenseList.get(i).setNatv_curr_amt(StringUtil.replaceComma(df.format(Double.parseDouble(expenseList.get(i).getNatv_curr_amt())))+"R$");
			}
			excelInfo.setA_prvs_amt_card(StringUtil.replaceComma(df.format(accommodationCardPamt)));				// ACCOMMODATION SUBTOTAL PRVS_AMT
			if(accommodationCardTamt == 0){
				excelInfo.setA_natv_curr_amt_card("");																// ACCOMMODATION SUBTOTAL NATV_CURR_AMT
			}else{
				excelInfo.setA_natv_curr_amt_card(StringUtil.replaceComma(df.format(accommodationCardTamt))+"R$");	// ACCOMMODATION SUBTOTAL NATV_CURR_AMT
			}
			excelInfo.setA_apl_xr_card(accommodationCardApl);														// ACCOMMODATION SUBTOTAL APL_XR
			excelInfo.setA_prvs_amt_cash(StringUtil.replaceComma(df.format(accommodationCashPamt)));				// ACCOMMODATION SUBTOTAL PRVS_AMT
			if(accommodationCashTamt == 0){
				excelInfo.setA_natv_curr_amt_cash("");																// ACCOMMODATION SUBTOTAL NATV_CURR_AMT
			}else{
				excelInfo.setA_natv_curr_amt_cash(StringUtil.replaceComma(df.format(accommodationCashTamt))+"R$");	// ACCOMMODATION SUBTOTAL NATV_CURR_AMT
			}
			excelInfo.setA_apl_xr_cash(accommodationCashApl);														// ACCOMMODATION SUBTOTAL APL_XR
			excelInfo.setT_prvs_amt_card(StringUtil.replaceComma(df.format(transportCardPamt)));					// TRANSPORT SUBTOTAL PRVS_AMT
			if(transportCardTamt == 0){
				excelInfo.setT_natv_curr_amt_card("");																// TRANSPORT SUBTOTAL NATV_CURR_AMT
			}else{
				excelInfo.setT_natv_curr_amt_card(StringUtil.replaceComma(df.format(transportCardTamt))+"R$");		// TRANSPORT SUBTOTAL NATV_CURR_AMT
			}
			excelInfo.setT_apl_xr_card(transportCardApl);															// TRANSPORT SUBTOTAL APL_XR
			excelInfo.setT_prvs_amt_cash(StringUtil.replaceComma(df.format(transportCashPamt)));					// TRANSPORT SUBTOTAL PRVS_AMT
			if(transportCashTamt == 0){
				excelInfo.setT_natv_curr_amt_cash("");																// TRANSPORT SUBTOTAL NATV_CURR_AMT
			}else{
				excelInfo.setT_natv_curr_amt_cash(StringUtil.replaceComma(df.format(transportCashTamt))+"R$");		// TRANSPORT SUBTOTAL NATV_CURR_AMT
			}
			excelInfo.setT_apl_xr_cash(transportCashApl);															// TRANSPORT SUBTOTAL APL_XR
			excelInfo.setR_prvs_amt_card(StringUtil.replaceComma(df.format(mealCardPamt)));							// MEAL SUBTOTAL PRVS_AMT
			if(mealCardTamt == 0){
				excelInfo.setR_natv_curr_amt_card("");																// MEAL SUBTOTAL NATV_CURR_AMT
			}else{
				excelInfo.setR_natv_curr_amt_card(StringUtil.replaceComma(df.format(mealCardTamt))+"R$");			// MEAL SUBTOTAL NATV_CURR_AMT
			}
			excelInfo.setR_apl_xr_card(mealCardApl);																// MEAL SUBTOTAL APL_XR
			excelInfo.setR_prvs_amt_cash(StringUtil.replaceComma(df.format(mealCashPamt)));							// MEAL SUBTOTAL PRVS_AMT
			if(mealCashTamt == 0){
				excelInfo.setR_natv_curr_amt_cash("");																// MEAL SUBTOTAL NATV_CURR_AMT
			}else{
				excelInfo.setR_natv_curr_amt_cash(StringUtil.replaceComma(df.format(mealCashTamt))+"R$");			// MEAL SUBTOTAL NATV_CURR_AMT
			}
			excelInfo.setR_apl_xr_cash(mealCashApl);																// MEAL SUBTOTAL APL_XR
			excelInfo.setO_prvs_amt_card(StringUtil.replaceComma(df.format(othersCardPamt)));						// OTHERS SUBTOTAL PRVS_AMT
			if(othersCardTamt == 0){
				excelInfo.setO_natv_curr_amt_card("");																// OTHERS SUBTOTAL NATV_CURR_AMT
			}else{
				excelInfo.setO_natv_curr_amt_card(StringUtil.replaceComma(df.format(othersCardTamt))+"R$");			// OTHERS SUBTOTAL NATV_CURR_AMT
			}
			excelInfo.setO_apl_xr_card(othersCardApl);																// OTHERS SUBTOTAL APL_XR
			excelInfo.setO_prvs_amt_cash(StringUtil.replaceComma(df.format(othersCashPamt)));						// OTHERS SUBTOTAL PRVS_AMT
			if(othersCashTamt == 0){
				excelInfo.setO_natv_curr_amt_cash("");																// OTHERS SUBTOTAL NATV_CURR_AMT
			}else{
				excelInfo.setO_natv_curr_amt_cash(StringUtil.replaceComma(df.format(othersCashTamt))+"R$");			// OTHERS SUBTOTAL NATV_CURR_AMT
			}
			excelInfo.setO_apl_xr_cash(othersCashApl);																// OTHERS SUBTOTAL APL_XR

			if(totalGeneralExpenditureCard == 0){
				excelInfo.setTtl_gnrl_exp_card("");																	// TOTAL GENERAL EXPENDITURE CARD
			}else{
				excelInfo.setTtl_gnrl_exp_card(StringUtil.replaceComma(df.format(totalGeneralExpenditureCard))+"R$");// TOTAL GENERAL EXPENDITURE CARD
			}
			if(totalPaymentCard == 0){
				excelInfo.setTtl_pymnt_card("");																	// TOTAL PAYMENT CARD
			}else{
				excelInfo.setTtl_pymnt_card(StringUtil.replaceComma(df.format(totalPaymentCard))+"R$");				// TOTAL PAYMENT CARD
			}
			if(totalGeneralExpenditureCash == 0){
				excelInfo.setTtl_gnrl_exp_cash("");																	// TOTAL GENERAL EXPENDITURE CASH
			}else{
				excelInfo.setTtl_gnrl_exp_cash(StringUtil.replaceComma(df.format(totalGeneralExpenditureCash))+"R$");// TOTAL GENERAL EXPENDITURE CASH
			}
			if(totalPaymentCash == 0){
				excelInfo.setTtl_pymnt_cash("");																	// TOTAL PAYMENT CASH
			}else{
				excelInfo.setTtl_pymnt_cash(StringUtil.replaceComma(df.format(totalPaymentCash))+"R$");				// TOTAL PAYMENT CASH
			}

			map.put("excelInfo", excelInfo);
			map.put("accommodationCardList", accommodationCardList);
			map.put("accommodationCashList", accommodationCashList);
			map.put("transportCardList", transportCardList);
			map.put("transportCashList", transportCashList);
			map.put("mealCardList", mealCardList);
			map.put("mealCashList", mealCashList);
			map.put("othersCardList", othersCardList);
			map.put("othersCashList", othersCashList);

			String temp_path = "";
			if(StringUtil.getSystemArea().equals("LOCAL")){
				temp_path = Constant.UPLOAD_LOCAL_PATH;
			}else if(StringUtil.getSystemArea().equals("TEST")){
				temp_path = Constant.UPLOAD_TEST_PATH;
			}else{
				temp_path = Constant.UPLOAD_REAL_PATH;
			}

			realFilePath = temp_path+Constant.EXCEL_TEMPLAT;
			destFilePath = temp_path+"/temp/"+bgabGascbt02.getDoc_no()+bgabGascbt02.getEeno()+".xls";
        	ExcelTemplat.createXlsFile(realFilePath, destFilePath, map);
	    } catch (Exception e) {
			logger.error(strMessege, e);
	    }
		return true;
	}

	@Override
	public List<BgabGascbtVoucherExcelDto> getSelectListBtToVoucherList(List<BgabGascbtVoucherExcelDto> travelerList) {
		List<BgabGascbtVoucherExcelDto> list = new ArrayList<BgabGascbtVoucherExcelDto>();

		for(int i = 0 ; i < travelerList.size() ; i++){
			List<BgabGascbtVoucherExcelDto> voucherList = businessTravelDao.getSelectListBtToVoucherList(travelerList.get(i));
			list.addAll(voucherList);
		}
		return list;
	}

	@Override
	public int updateListBtToVoucherList(List<BgabGascbtVoucherExcelDto> travelerList) {
		int cntReport = businessTravelDao.updateBTToReportBySapYnForVoucher(travelerList);
		int cntTraveler = businessTravelDao.updateBTToTravelerBySapYnForVoucher(travelerList);

		if(travelerList.size() > 0){
			for(int i = 0 ; i < travelerList.size() ; i++){
				BgabGascbt04 bgabGascbt04 = new BgabGascbt04();
				bgabGascbt04.setDoc_no(travelerList.get(0).getDoc_no());
				bgabGascbt04.setEeno(travelerList.get(0).getEeno());

				int NCnt = Integer.parseInt(businessTravelDao.getSelectCountBTToReportBySapYn(bgabGascbt04));
				BgabGascbt02 bgabGascbt02 = new BgabGascbt02();
				if(NCnt == 0){
					bgabGascbt02.setDoc_no(bgabGascbt04.getDoc_no());
					bgabGascbt02.setEeno(bgabGascbt04.getEeno());
					bgabGascbt02.setTot_arsl_refl_yn("Y");

					businessTravelDao.updateBTToTravelerBySapYn(bgabGascbt02);
				}
			}
		}

		return cntReport;
	}

	@Override
	public int selectCountBtCustomerToList(BgabGascbt01 bgabGascbt01){
		return Integer.parseInt(businessTravelDao.selectCountBtCustomerToList(bgabGascbt01));
	}

	@Override
	public List<BgabGascbt01> selectListBtCustomerToList(BgabGascbt01 bgabGascbt01){
		return businessTravelDao.selectListBtCustomerToList(bgabGascbt01);
	}

	@Override
	public List<BgabGascbt07> selectFlightConfirmListBtToRequest(BgabGascbt07 bgabGascbt07){
		return businessTravelDao.selectFlightConfirmListBtToRequest(bgabGascbt07);
	}
	public int insertFlightConfirmBtToRequest(List<BgabGascbt07> iList){
		return businessTravelDao.insertFlightConfirmBtToRequest(iList);
	}
	@Override
	public int deleteFlightConfirmBtToRequest(BgabGascbt07 dList){
		return businessTravelDao.deleteFlightConfirmBtToRequest(dList);
	}

	@Override
	public List<BgabGascbt08> selectHotelConfirmListBtToRequest(BgabGascbt08 bgabGascbt08){
		return businessTravelDao.selectHotelConfirmListBtToRequest(bgabGascbt08);
	}
	public int insertHotelConfirmBtToRequest(List<BgabGascbt08> iList){
		return businessTravelDao.insertHotelConfirmBtToRequest(iList);
	}
	@Override
	public int deleteHotelConfirmBtToRequest(BgabGascbt08 dList){
		return businessTravelDao.deleteHotelConfirmBtToRequest(dList);
	}

	@Override
	public List<BgabGascbt09> selectRentCarListBtToRequest(BgabGascbt09 bgabGascbt09){
		return businessTravelDao.selectRentCarListBtToRequest(bgabGascbt09);
	}
	public int insertRentCarBtToRequest(List<BgabGascbt09> iList){
		return businessTravelDao.insertRentCarBtToRequest(iList);
	}
	@Override
	public int deleteRentCarBtToRequest(BgabGascbt09 dList){
		return businessTravelDao.deleteRentCarBtToRequest(dList);
	}

	@Override
	public CommonMessage updateCompletePOCreate(List<BgabGascbt04> param){
		// switch 조회
		BgabGascz005Dto switch_param = new BgabGascz005Dto();
		switch_param.setXcod_code("BT");
		BgabGascz005Dto switchInfo = systemDao.getSelectCheckBudgetSwitch(switch_param);

		CommonMessage message = new CommonMessage();
		if("Y".equals(switchInfo.getXcod_hname())){
			RfcPoCreateVo o_PoInfo = new RfcPoCreateVo();
			for(int z=0; z<param.size(); z++){
				try {
					Boolean flag = true;

					// temp PO 조회
					BgabGascbt02 info = new BgabGascbt02();
					info.setDoc_no(param.get(z).getDoc_no());
					info.setSeq(param.get(z).getHid_seq());
					info.setEeno(param.get(z).getEeno());
					List<BgabGascbt02> totInfo = businessTravelDao.getSelectBTToRequestByTraveler(info);

					// material 정보 조회
					BgabGascz016Dto m_param = new BgabGascz016Dto();
					m_param.setKey_job("XBT");
					m_param.setStartRow(0);
					m_param.setEndRow(10);
					List<BgabGascz016Dto> m_info = systemDao.getSelectMaterialManagement(m_param);

					if(m_info.size() == 0){
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						message.setErrorCode("E");
						message.setMessage(HncisMessageSource.getMessage("MATERIAL.0001"));
						flag = false;
					}

					String vendorNm = "";
					if("".equals(param.get(z).getVendor_nm())){
						BgabGascz014Dto v_param = new BgabGascz014Dto();
						v_param.setKey_code(param.get(z).getVendor_cd());
						v_param.setKey_job("XBT");
						v_param.setStartRow(0);
						v_param.setEndRow(10);
						List<BgabGascz014Dto> vendorInfo = systemDao.getSelectVendorManagement(v_param);
						if(vendorInfo.size() == 0){
							TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
							message.setErrorCode("E");
							message.setMessage(HncisMessageSource.getMessage("VENDOR.0001"));
							flag = false;
						}else{
							vendorNm = vendorInfo.get(0).getVendor_name();
						}
					}else{
						vendorNm = param.get(z).getVendor_nm();
					}

					if(flag){
						////////////////////////////////////// FINAL PO CREATE /////////////////////////////////////////////
						String budg = param.get(z).getBudg_type();
						CurrentDateTime d = new CurrentDateTime();
						// 지역 조회
						BgabGascz002Dto w_info = new BgabGascz002Dto();
						if("O".equals(param.get(z).getH_gubn())){
							w_info.setXusr_empno(param.get(z).getReq_eeno());
						}else{
							w_info.setXusr_empno(param.get(z).getEeno());
						}
						BgabGascz002Dto	workPlace = systemDao.getSelectUserWorkPlace(w_info);

						RfcPoCreate crfc = new RfcPoCreate();
						RfcPoCreateVo i_PoInfo = new RfcPoCreateVo();
						i_PoInfo.setI_date(CurrentDateTime.getDate());
						i_PoInfo.setI_vendor_code(param.get(z).getVendor_cd());		// mapping
						i_PoInfo.setI_vendor_name(vendorNm);		// mapping
						i_PoInfo.setI_pur_org_code(orgCode);
						i_PoInfo.setI_pur_group("B11");
						i_PoInfo.setI_wrkplc_cd(workPlace.getXusr_plac_work());			// Piracicaba, São Paulo
						if("O".equals(param.get(z).getH_gubn())){
							i_PoInfo.setI_usn(param.get(z).getReq_eeno());
						}else{
							i_PoInfo.setI_usn(param.get(z).getEeno());
						}
						i_PoInfo.setI_material_code(m_info.get(0).getMaterial_code());
						i_PoInfo.setI_material_desc(m_info.get(0).getMaterial_desc());
						i_PoInfo.setI_mat_group(m_info.get(0).getMaterial_group());
						i_PoInfo.setI_qty("1");
						i_PoInfo.setI_price(Double.toString(param.get(z).getApl_xr()));
						i_PoInfo.setI_delivery_date(d.getMonth(d.getDate(), 1));
						i_PoInfo.setI_cost_cd(param.get(z).getCost_cd());
						i_PoInfo.setI_company_code(orgCode);

						if("D".equals(budg)){
							i_PoInfo.setI_account_category("K");
							i_PoInfo.setI_account_code(param.get(z).getBudg_no());
						}else if("I".equals(budg)){
							i_PoInfo.setI_account_category("F");
							i_PoInfo.setI_account_code(param.get(z).getBudg_no());
							i_PoInfo.setI_io_cd(param.get(z).getBudg_text());
						}else if("W".equals(budg)){
							i_PoInfo.setI_account_category("P");
							i_PoInfo.setI_account_code(param.get(z).getBudg_no());
							i_PoInfo.setI_wbs_cd(param.get(z).getBudg_text());
						}

						o_PoInfo = crfc.getResult(i_PoInfo);

						if("Z".equals(o_PoInfo.getO_if_result())){
							param.get(z).setPo_no(o_PoInfo.getO_po_no());
							businessTravelDao.updateCompleteFinalPO(param.get(z));

							////////////////////////////////////// FINAL PO END /////////////////////////////////////////////


							////////////////////////////////////// TEMP PO DELETE /////////////////////////////////////////////

							RfcPoCreate drfc = new RfcPoCreate();
							RfcPoCreateVo d_PoInfo = new RfcPoCreateVo();
							RfcPoCreateVo do_PoInfo = new RfcPoCreateVo();
							if(!"".equals(StringUtil.isNullToString(totInfo.get(0).getPo_no(),""))){
								d_PoInfo.setI_date(CurrentDateTime.getDate());
								d_PoInfo.setI_po_no(totInfo.get(0).getPo_no());
								d_PoInfo.setI_po_desc(descCancel);

								do_PoInfo = drfc.doPoDelete(d_PoInfo);
							}else{
								do_PoInfo.setO_if_result("Z");
							}

							if("Z".equals(do_PoInfo.getO_if_result())){
								//////////////////////////////////////TEMP PO END /////////////////////////////////////////////

								////////////////////////////////////// TEMP PO CREATE /////////////////////////////////////////////
								RfcPoCreate tmp_rfc = new RfcPoCreate();
								RfcPoCreateVo tmp_i_PoInfo = new RfcPoCreateVo();
								// Dummy Vendor Info
								BgabGascz005Dto bgabGascz005Dto = new BgabGascz005Dto();
								bgabGascz005Dto.setXcod_knd("X3015");
								BgabGascz005Dto dummyInfo = businessTravelDao.getSelectDummyVendorInfo(bgabGascz005Dto);
								double totAmt = Double.parseDouble(totInfo.get(0).getTot_amt()) - param.get(z).getApl_xr();

								tmp_i_PoInfo.setI_date(CurrentDateTime.getDate());
								tmp_i_PoInfo.setI_vendor_code(dummyInfo.getXcod_code());		// mapping
								tmp_i_PoInfo.setI_vendor_name(dummyInfo.getXcod_hname());		// mapping
								tmp_i_PoInfo.setI_pur_org_code(orgCode);
								tmp_i_PoInfo.setI_pur_group("B11");
								tmp_i_PoInfo.setI_wrkplc_cd(workPlace.getXusr_plac_work());			// Piracicaba, São Paulo
								tmp_i_PoInfo.setI_usn(totInfo.get(0).getEeno());
								tmp_i_PoInfo.setI_material_code(m_info.get(0).getMaterial_code());
								tmp_i_PoInfo.setI_material_desc(m_info.get(0).getMaterial_desc());
								tmp_i_PoInfo.setI_mat_group(m_info.get(0).getMaterial_group());
								tmp_i_PoInfo.setI_qty("1");
								tmp_i_PoInfo.setI_price(Double.toString(totAmt));
								tmp_i_PoInfo.setI_delivery_date(d.getMonth(d.getDate(), 1));
								tmp_i_PoInfo.setI_cost_cd(param.get(z).getCost_cd());
								tmp_i_PoInfo.setI_company_code(orgCode);
								tmp_i_PoInfo.setI_po_no(totInfo.get(0).getPo_no());

								if("D".equals(budg)){
									tmp_i_PoInfo.setI_account_category("K");
									tmp_i_PoInfo.setI_account_code(param.get(z).getBudg_no());
								}else if("I".equals(budg)){
									tmp_i_PoInfo.setI_account_category("F");
									tmp_i_PoInfo.setI_account_code(param.get(z).getBudg_no());
									tmp_i_PoInfo.setI_io_cd(param.get(z).getBudg_text());
								}else if("W".equals(budg)){
									tmp_i_PoInfo.setI_account_category("P");
									tmp_i_PoInfo.setI_account_code(param.get(z).getBudg_no());
									tmp_i_PoInfo.setI_wbs_cd(param.get(z).getBudg_text());
								}

								RfcPoCreateVo tmp_PoInfo = new RfcPoCreateVo();
								if(!"".equals(StringUtil.isNullToString(totInfo.get(0).getPo_no(),"")) && totAmt != 0){
									tmp_PoInfo = tmp_rfc.getResult(tmp_i_PoInfo);
								}else{
									tmp_PoInfo.setO_if_result("Z");
								}

								if("Z".equals(tmp_PoInfo.getO_if_result())){
									if(!"".equals(StringUtil.isNullToString(totInfo.get(0).getPo_no(),""))){
										BgabGascbt02 tmpinfo = new BgabGascbt02();
										tmpinfo.setDoc_no(param.get(z).getDoc_no());
										tmpinfo.setSeq(param.get(z).getHid_seq());
										tmpinfo.setTot_amt(String.valueOf(totAmt));
										tmpinfo.setPo_no(tmp_PoInfo.getO_po_no());

										businessTravelDao.updateTravelerTempPo(tmpinfo);
									}
									////////////////////////////////////// TEMP PO END /////////////////////////////////////////////

									// 기존에 sap yn 업데이트 하는 부분
									businessTravelDao.updateBTToReportBySapYnRow(param.get(z));

									BgabGascbt04 travelerInfo = new BgabGascbt04();
									if(param.size() > 0){
										travelerInfo.setDoc_no(param.get(0).getDoc_no());
										travelerInfo.setEeno(param.get(0).getEeno());
									}

									int NCnt = Integer.parseInt(businessTravelDao.getSelectCountBTToReportBySapYn(travelerInfo));
									BgabGascbt02 bgabGascbt02 = new BgabGascbt02();
									if(NCnt == 0){
										bgabGascbt02.setDoc_no(travelerInfo.getDoc_no());
										bgabGascbt02.setEeno(travelerInfo.getEeno());
										bgabGascbt02.setTot_arsl_refl_yn("Y");
										businessTravelDao.updateBTToTravelerBySapYn(bgabGascbt02);
									}
								}else{
									TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
									message.setErrorCode("E");
									message.setMessage(tmp_PoInfo.getO_if_fail_msg());
								}
							}else{
								TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
								message.setErrorCode("E");
								message.setMessage(do_PoInfo.getO_if_fail_msg());
							}
						}else{
							TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
							message.setErrorCode("E");
							message.setMessage(o_PoInfo.getO_if_fail_msg());
						}
					}
				} catch (Exception e) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					message.setErrorCode("E");
					message.setMessage(o_PoInfo.getO_if_fail_msg());
					logger.error(strMessege, e);
				}
			}
		}else{
			for(int z=0; z<param.size(); z++){
				// 기존에 sap yn 업데이트 하는 부분
				businessTravelDao.updateBTToReportBySapYnRow(param.get(z));

				BgabGascbt04 travelerInfo = new BgabGascbt04();
				if(param.size() > 0){
					travelerInfo.setDoc_no(param.get(0).getDoc_no());
					travelerInfo.setEeno(param.get(0).getEeno());
				}

				int NCnt = Integer.parseInt(businessTravelDao.getSelectCountBTToReportBySapYn(travelerInfo));
				BgabGascbt02 bgabGascbt02 = new BgabGascbt02();
				if(NCnt == 0){
					bgabGascbt02.setDoc_no(travelerInfo.getDoc_no());
					bgabGascbt02.setEeno(travelerInfo.getEeno());
					bgabGascbt02.setTot_arsl_refl_yn("Y");
					businessTravelDao.updateBTToTravelerBySapYn(bgabGascbt02);
				}
			}
//			message.setMessage(HmbMessageSource.getMessage("SWITCH.0001"));
		}

		return message;
	}

	@Override
	public CommonMessage updateCompletePODelete(List<BgabGascbt04> param){
		// switch 조회
		BgabGascz005Dto switch_param = new BgabGascz005Dto();
		switch_param.setXcod_code("BT");
		BgabGascz005Dto switchInfo = systemDao.getSelectCheckBudgetSwitch(switch_param);

		CommonMessage message = new CommonMessage();
//		if("Y".equals(switchInfo.getXcod_hname())){
//			RfcPoCreateVo o_PoInfo = new RfcPoCreateVo();
//			for(int z=0; z<param.size(); z++){
//				try {
//					boolean flag = true;
//					// temp PO 조회
//					BgabGascbt02 info = new BgabGascbt02();
//					info.setDoc_no(param.get(z).getDoc_no());
//					info.setSeq(param.get(z).getHid_seq());
//					info.setEeno(param.get(z).getEeno());
//					List<BgabGascbt02> totInfo = businessTravelDao.getSelectBTToRequestByTraveler(info);
//
//					// material 정보 조회
//					BgabGascz016Dto m_param = new BgabGascz016Dto();
//					m_param.setKey_job("XBT");
//					m_param.setStartRow(0);
//					m_param.setEndRow(10);
//					List<BgabGascz016Dto> m_info = systemDao.getSelectMaterialManagement(m_param);
//
//					if(m_info.size() == 0){
//						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//						message.setErrorCode("E");
//						message.setMessage(HncisMessageSource.getMessage("MATERIAL.0001"));
//						flag = false;
//					}
//
//					if(flag){
//						////////////////////////////////////// FINAL PO DELETE /////////////////////////////////////////////
//						String budg = param.get(z).getBudg_type();
//						CurrentDateTime d = new CurrentDateTime();
//
//						RfcPoCreate crfc = new RfcPoCreate();
//						RfcPoCreateVo i_PoInfo = new RfcPoCreateVo();
//
//						if(!"".equals(StringUtil.isNullToString(param.get(z).getPo_no(),""))){
//							i_PoInfo.setI_date(CurrentDateTime.getDate());
//							i_PoInfo.setI_po_no(param.get(z).getPo_no());
//							i_PoInfo.setI_po_desc("cancel");
//
//							o_PoInfo = crfc.doPoDelete(i_PoInfo);
//						}else{
//							o_PoInfo.setO_if_result("Z");
//						}
//
//						if("Z".equals(o_PoInfo.getO_if_result())){
//							param.get(z).setPo_no("");
//							businessTravelDao.updateCompleteFinalPO(param.get(z));
//
//							////////////////////////////////////// FINAL PO END /////////////////////////////////////////////
//
//
//							//////////////////////////////////////TEMP PO DELETE /////////////////////////////////////////////
//							RfcPoCreate drfc = new RfcPoCreate();
//							RfcPoCreateVo d_PoInfo = new RfcPoCreateVo();
//
//							RfcPoCreateVo do_PoInfo = new RfcPoCreateVo();
//							if(!"".equals(StringUtil.isNullToString(totInfo.get(0).getPo_no(),""))){
//								d_PoInfo.setI_date(CurrentDateTime.getDate());
//								d_PoInfo.setI_po_no(totInfo.get(0).getPo_no());
//								d_PoInfo.setI_po_desc("cancel");
//
//								do_PoInfo = drfc.doPoDelete(d_PoInfo);
//							}else{
//								do_PoInfo.setO_if_result("Z");
//							}
//
//							if("Z".equals(do_PoInfo.getO_if_result())){
//								//////////////////////////////////////TEMP PO END /////////////////////////////////////////////
//
//								////////////////////////////////////// TEMP PO CREATE /////////////////////////////////////////////
//								RfcPoCreate tmp_rfc = new RfcPoCreate();
//								RfcPoCreateVo tmp_i_PoInfo = new RfcPoCreateVo();
//								// 지역 조회
//								BgabGascz002Dto w_info = new BgabGascz002Dto();
//								if("O".equals(param.get(z).getH_gubn())){
//									w_info.setXusr_empno(param.get(z).getReq_eeno());
//								}else{
//									w_info.setXusr_empno(param.get(z).getEeno());
//								}
//								BgabGascz002Dto	workPlace = systemDao.getSelectUserWorkPlace(w_info);
//								// Dummy Vendor Info
//								BgabGascz005Dto bgabGascz005Dto = new BgabGascz005Dto();
//								bgabGascz005Dto.setXcod_knd("X3015");
//								BgabGascz005Dto dummyInfo = businessTravelDao.getSelectDummyVendorInfo(bgabGascz005Dto);
//								double totAmt = Double.parseDouble(totInfo.get(0).getTot_amt()) + param.get(z).getApl_xr();
//
//								tmp_i_PoInfo.setI_date(CurrentDateTime.getDate());
//								tmp_i_PoInfo.setI_vendor_code(dummyInfo.getXcod_code());		// mapping
//								tmp_i_PoInfo.setI_vendor_name(dummyInfo.getXcod_hname());		// mapping
//								tmp_i_PoInfo.setI_pur_org_code("H301");
//								tmp_i_PoInfo.setI_pur_group("B11");
//								tmp_i_PoInfo.setI_wrkplc_cd(workPlace.getXusr_plac_work());			// Piracicaba, São Paulo
//								tmp_i_PoInfo.setI_usn(totInfo.get(0).getEeno());
//								tmp_i_PoInfo.setI_material_code(m_info.get(0).getMaterial_code());
//								tmp_i_PoInfo.setI_material_desc(m_info.get(0).getMaterial_desc());
//								tmp_i_PoInfo.setI_mat_group(m_info.get(0).getMaterial_group());
//								tmp_i_PoInfo.setI_qty("1");
//								tmp_i_PoInfo.setI_price(Double.toString(totAmt));
//								tmp_i_PoInfo.setI_delivery_date(d.getMonth(d.getDate(), 1));
//								tmp_i_PoInfo.setI_cost_cd(param.get(z).getCost_cd());
//								tmp_i_PoInfo.setI_company_code("H301");
//								tmp_i_PoInfo.setI_po_no(totInfo.get(0).getPo_no());
//
//								if("D".equals(budg)){
//									tmp_i_PoInfo.setI_account_category("K");
//									tmp_i_PoInfo.setI_account_code(param.get(z).getBudg_no());
//								}else if("I".equals(budg)){
//									tmp_i_PoInfo.setI_account_category("F");
//									tmp_i_PoInfo.setI_io_cd(param.get(z).getBudg_text());
//									tmp_i_PoInfo.setI_account_code(param.get(z).getBudg_no());
//								}else if("W".equals(budg)){
//									tmp_i_PoInfo.setI_account_category("P");
//									tmp_i_PoInfo.setI_wbs_cd(param.get(z).getBudg_text());
//									tmp_i_PoInfo.setI_account_code(param.get(z).getBudg_no());
//								}
//
//								RfcPoCreateVo tmp_PoInfo = new RfcPoCreateVo();
//								if(!"".equals(StringUtil.isNullToString(totInfo.get(0).getPo_no(),"")) && totAmt != 0){
//									tmp_PoInfo = tmp_rfc.getResult(tmp_i_PoInfo);
//								}else{
//									tmp_PoInfo.setO_if_result("Z");
//								}
//
//								if("Z".equals(tmp_PoInfo.getO_if_result())){
//									if(!"".equals(StringUtil.isNullToString(totInfo.get(0).getPo_no(),""))){
//										BgabGascbt02 tmpinfo = new BgabGascbt02();
//										tmpinfo.setDoc_no(param.get(z).getDoc_no());
//										tmpinfo.setSeq(param.get(z).getHid_seq());
//										tmpinfo.setTot_amt(String.valueOf(totAmt));
//										tmpinfo.setPo_no(tmp_PoInfo.getO_po_no());

//										businessTravelDao.updateTravelerTempPo(tmpinfo);
//									}
									//////////////////////////////////////TEMP PO END /////////////////////////////////////////////

									// 기존 sap yn 업데이트

		for(int z=0; z<param.size(); z++){
			businessTravelDao.updateBTToReportBySapYnRow(param.get(z));

			BgabGascbt04 travelerInfo = new BgabGascbt04();
			if(param.size() > 0){
				travelerInfo.setDoc_no(param.get(0).getDoc_no());
				travelerInfo.setEeno(param.get(0).getEeno());
			}

			BgabGascbt02 bgabGascbt02 = new BgabGascbt02();
			bgabGascbt02.setDoc_no(travelerInfo.getDoc_no());
			bgabGascbt02.setEeno(travelerInfo.getEeno());
			bgabGascbt02.setTot_arsl_refl_yn("N");

			businessTravelDao.updateBTToTravelerBySapYn(bgabGascbt02);
		}

									//////////////////////////////////////TEMP PO END /////////////////////////////////////////////
//								}else{
//									TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//									message.setErrorCode("E");
//									message.setMessage(tmp_PoInfo.getO_if_fail_msg());
//								}
//							}else{
//								TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//								message.setErrorCode("E");
//								message.setMessage(do_PoInfo.getO_if_fail_msg());
//							}
//						}else{
//							TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//							message.setErrorCode("E");
//							message.setMessage(o_PoInfo.getO_if_fail_msg());
//						}
//					}
//				} catch (Exception e) {
//					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//					message.setErrorCode("E");
//					message.setMessage(o_PoInfo.getO_if_fail_msg());
//					logger.error("messege", e);
//				}
//			}
//		}else{
//			message.setMessage(HncisMessageSource.getMessage("SWITCH.0001"));
//		}

		return message;
	}

	@Override
	public CommonMessage updateInfoBtToReject(BgabGascbt01 param, HttpServletRequest req) throws SessionException{
		CommonMessage message = new CommonMessage();

		// switch 조회
		BgabGascz005Dto switch_param = new BgabGascz005Dto();
		switch_param.setXcod_code("BT");
		switch_param.setLocale(param.getLocale());
		BgabGascz005Dto switchInfo = systemDao.getSelectCheckBudgetSwitch(switch_param);

		if("Y".equals(switchInfo.getXcod_hname())){
			BgabGascbt02 p_param = new BgabGascbt02();
			p_param.setDoc_no(param.getDoc_no());
			List<BgabGascbt02> travelList = businessTravelDao.getSelectTravelRejectSubmitPoSearch(p_param);

			for(int i=0; i<travelList.size(); i++){
				if(!"".equals(StringUtil.isNullToString(travelList.get(i).getPo_no()))){
					// P.O 삭제
					RfcPoCreate crfc = new RfcPoCreate();
					RfcPoCreateVo i_PoInfo = new RfcPoCreateVo();

					RfcPoCreateVo o_PoInfo = new RfcPoCreateVo();
					if(!"".equals(StringUtil.isNullToString(i_PoInfo.getI_po_no(),""))){
						try {
							i_PoInfo.setI_date(CurrentDateTime.getDate());
							i_PoInfo.setI_po_no(travelList.get(i).getPo_no());
							i_PoInfo.setI_po_desc(descCancel);

							o_PoInfo = crfc.doPoDelete(i_PoInfo);

							if("Z".equals(o_PoInfo.getO_if_result())){
								travelList.get(i).setPo_no("");
								businessTravelDao.updateTravelerTempPo(travelList.get(i));
							}else{
								TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
								message.setMessage(o_PoInfo.getO_if_fail_msg());
								message.setErrorCode("E");
							}
						} catch (Exception e) {
							TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
							message.setMessage(o_PoInfo.getO_if_fail_msg());
							message.setErrorCode("E");
							logger.error(strMessege, e);
						}
					}
				}
			}

			BgabGascbt04 v_param = new BgabGascbt04();
			v_param.setDoc_no(param.getDoc_no());
			v_param.setStl_way_cd("BT002");
			List<BgabGascbt04> vendorList = businessTravelDao.getSelectBTToReportVendor(v_param);

			for(int i=0; i<vendorList.size(); i++){
				if(!"".equals(StringUtil.isNullToString(vendorList.get(i).getVou_po_no()))){
					// P.O 삭제
					RfcPoCreate crfc = new RfcPoCreate();
					RfcPoCreateVo i_PoInfo = new RfcPoCreateVo();
					RfcPoCreateVo o_PoInfo = new RfcPoCreateVo();

					try {
						i_PoInfo.setI_date(CurrentDateTime.getDate());
						i_PoInfo.setI_po_no(vendorList.get(i).getVou_po_no());
						i_PoInfo.setI_po_desc(descCancel);

						o_PoInfo = crfc.doPoDelete(i_PoInfo);

						if("Z".equals(o_PoInfo.getO_if_result())){
							vendorList.get(i).setPo_no("");
							vendorList.get(i).setDoc_no(vendorList.get(i).getVou_doc_no());
							vendorList.get(i).setSeq(vendorList.get(i).getVou_seq());
							businessTravelDao.updateCompleteFinalPO(vendorList.get(i));
						}else{
							TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
							message.setMessage(o_PoInfo.getO_if_fail_msg());
							message.setErrorCode("E");
						}
					} catch (Exception e) {
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						message.setMessage(o_PoInfo.getO_if_fail_msg());
						message.setErrorCode("E");
						logger.error(strMessege, e);
					}
				}
			}
		}

		if(!"E".equals(message.getErrorCode())){
			// reject
			businessTravelDao.updateInfoBtToReject(param);
			
			// BPM API호출
			String processCode = ""; 	//프로세스 코드 (출장 신청 프로세스) - 프로세스 정의서 참조
			if(param.getDom_abrd_scn_cd().equals(gubunAb)){
				processCode = pCode2; // 해외
			}else{
				processCode = pCode; // 국내
			}
			String bizKey = param.getDoc_no();	//신청서 번호
			String statusCode = "GASBZ01410030";	//액티비티 코드 (휴양소 당당자 확인) - 프로세스 정의서 참조
			String loginUserId = param.getUpdr_eeno();	//로그인 사용자 아이디

			BpmApiUtil.sendDeleteAndRejectTask(processCode, bizKey, statusCode, loginUserId);

			//컨펌취소 메일 발송
			String fromEeno   = SessionInfo.getSess_name(req);
			String fromStepNm = SessionInfo.getSess_step_name(req);
			String toEeno     = param.getEeno();
			String rtnText    = param.getSnb_rson_sbc();
			String title	  = HncisMessageSource.getMessage("bns_tr");
			String corp_cd	  = param.getCorp_cd();
			
			CommonApproval commonApproval = new CommonApproval();
			commonApproval.setRdcs_eeno(toEeno);
			commonApproval.setCorp_cd(SessionInfo.getSess_corp_cd(req));

			String mailAdr = commonJobDao.getSelectInfoToEenoEmailAdr(commonApproval);

//			Submit.returnEmail(fromEeno, fromStepNm, mailAdr,"", title, rtnText, corp_cd);
		}

		return message;
	}

	@Override
	public CommonMessage updateCompleteReInvoiceCreate(List<BgabGascbt04> param){
		// switch 조회
		BgabGascz005Dto switch_param = new BgabGascz005Dto();
		switch_param.setXcod_code("BT");
		BgabGascz005Dto switchInfo = systemDao.getSelectCheckBudgetSwitch(switch_param);

		CommonMessage message = new CommonMessage();
		if("Y".equals(switchInfo.getXcod_hname())){
			RfcPoCreateVo o_PoInfo = new RfcPoCreateVo();
			for(int z=0; z<param.size(); z++){
				try {
					Boolean flag = true;

					// temp PO 조회
					BgabGascbt02 info = new BgabGascbt02();
					info.setDoc_no(param.get(z).getDoc_no());
					info.setSeq(param.get(z).getHid_seq());
					info.setEeno(param.get(z).getEeno());
					List<BgabGascbt02> totInfo = businessTravelDao.getSelectBTToRequestByTraveler(info);

					////////////////////////////////////// FINAL REINVOICE CREATE /////////////////////////////////////////////

					RfcFiCreate crfc = new RfcFiCreate();
					RfcPoCreateVo i_PoInfo = new RfcPoCreateVo();
					i_PoInfo.setI_po_no(param.get(z).getDoc_no()+""+param.get(z).getEeno()+"F");
					i_PoInfo.setI_usn(param.get(z).getEeno());
					i_PoInfo.setI_qty(Integer.toString(param.get(z).getSeq()));
					i_PoInfo.setI_cost_cd(param.get(z).getCost_cd());
					if("D".equals(param.get(z).getBudg_type())){
						i_PoInfo.setI_account_code(param.get(z).getBudg_no());
					}else if("I".equals(param.get(z).getBudg_type())){
						i_PoInfo.setI_account_code(param.get(z).getBudg_no());
						i_PoInfo.setI_io_cd(param.get(z).getBudg_text());
					}else if("W".equals(param.get(z).getBudg_type())){
						i_PoInfo.setI_account_code(param.get(z).getBudg_no());
						i_PoInfo.setI_wbs_cd(param.get(z).getBudg_text());
					}
					i_PoInfo.setI_vendor_name(param.get(z).getOwn_dtl_nm());
					i_PoInfo.setI_price(Double.toString(param.get(z).getApl_xr()));

					o_PoInfo = crfc.getResult(i_PoInfo);

					if("Z".equals(o_PoInfo.getO_if_result())){
						param.get(z).setPo_no(o_PoInfo.getO_po_no());
						businessTravelDao.updateCompleteFinalPO(param.get(z));

						////////////////////////////////////// FINAL PO END /////////////////////////////////////////////


						////////////////////////////////////// TEMP PO DELETE /////////////////////////////////////////////

						RfcPoCreate drfc = new RfcPoCreate();
						RfcPoCreateVo d_PoInfo = new RfcPoCreateVo();
						RfcPoCreateVo do_PoInfo = new RfcPoCreateVo();
						if(!"".equals(StringUtil.isNullToString(totInfo.get(0).getPo_no(),""))){
							d_PoInfo.setI_date(CurrentDateTime.getDate());
							d_PoInfo.setI_po_no(totInfo.get(0).getPo_no());
							d_PoInfo.setI_po_desc(descCancel);

							do_PoInfo = drfc.doPoDelete(d_PoInfo);
						}else{
							do_PoInfo.setO_if_result("Z");
						}

						if("Z".equals(do_PoInfo.getO_if_result())){
							//////////////////////////////////////TEMP PO END /////////////////////////////////////////////

							////////////////////////////////////// TEMP PO CREATE /////////////////////////////////////////////
							RfcPoCreate tmp_rfc = new RfcPoCreate();
							RfcPoCreateVo tmp_i_PoInfo = new RfcPoCreateVo();
							CurrentDateTime d = new CurrentDateTime();
							// Dummy Vendor Info
							BgabGascz005Dto bgabGascz005Dto = new BgabGascz005Dto();
							bgabGascz005Dto.setXcod_knd("X3015");
							BgabGascz005Dto dummyInfo = businessTravelDao.getSelectDummyVendorInfo(bgabGascz005Dto);
							double totAmt = Double.parseDouble(totInfo.get(0).getTot_amt()) - param.get(z).getApl_xr();
							// 지역 조회
							BgabGascz002Dto w_info = new BgabGascz002Dto();
							w_info.setXusr_empno(param.get(z).getEeno());
							BgabGascz002Dto	workPlace = systemDao.getSelectUserWorkPlace(w_info);
							// material 정보 조회
							BgabGascz016Dto m_param = new BgabGascz016Dto();
							m_param.setKey_job("XBT");
							m_param.setStartRow(0);
							m_param.setEndRow(10);
							List<BgabGascz016Dto> m_info = systemDao.getSelectMaterialManagement(m_param);

							if(m_info.size() == 0){
								TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
								message.setErrorCode("E");
								message.setMessage(HncisMessageSource.getMessage("MATERIAL.0001"));
								flag = false;
							}

							if(flag){
								tmp_i_PoInfo.setI_date(CurrentDateTime.getDate());
								tmp_i_PoInfo.setI_vendor_code(dummyInfo.getXcod_code());		// mapping
								tmp_i_PoInfo.setI_vendor_name(dummyInfo.getXcod_hname());		// mapping
								tmp_i_PoInfo.setI_pur_org_code(orgCode);
								tmp_i_PoInfo.setI_pur_group("B11");
								tmp_i_PoInfo.setI_wrkplc_cd(workPlace.getXusr_plac_work());			// Piracicaba, São Paulo
								tmp_i_PoInfo.setI_usn(totInfo.get(0).getEeno());
								tmp_i_PoInfo.setI_material_code(m_info.get(0).getMaterial_code());
								tmp_i_PoInfo.setI_material_desc(m_info.get(0).getMaterial_desc());
								tmp_i_PoInfo.setI_mat_group(m_info.get(0).getMaterial_group());
								tmp_i_PoInfo.setI_qty("1");
								tmp_i_PoInfo.setI_price(Double.toString(totAmt));
								tmp_i_PoInfo.setI_delivery_date(d.getMonth(d.getDate(), 1));
								tmp_i_PoInfo.setI_cost_cd(param.get(z).getCost_cd());
								tmp_i_PoInfo.setI_company_code(orgCode);
								tmp_i_PoInfo.setI_po_no(totInfo.get(0).getPo_no());

								if("D".equals(param.get(z).getBudg_type())){
									tmp_i_PoInfo.setI_account_category("K");
									tmp_i_PoInfo.setI_account_code(param.get(z).getBudg_no());
								}else if("I".equals(param.get(z).getBudg_type())){
									tmp_i_PoInfo.setI_account_category("F");
									tmp_i_PoInfo.setI_account_code(param.get(z).getBudg_no());
									tmp_i_PoInfo.setI_io_cd(param.get(z).getBudg_text());
								}else if("W".equals(param.get(z).getBudg_type())){
									tmp_i_PoInfo.setI_account_category("P");
									tmp_i_PoInfo.setI_account_code(param.get(z).getBudg_no());
									tmp_i_PoInfo.setI_wbs_cd(param.get(z).getBudg_text());
								}

								RfcPoCreateVo tmp_PoInfo = new RfcPoCreateVo();
								if(!"".equals(StringUtil.isNullToString(totInfo.get(0).getPo_no(),""))){
									tmp_PoInfo = tmp_rfc.getResult(tmp_i_PoInfo);
								}else{
									tmp_PoInfo.setO_if_result("Z");
								}

								if("Z".equals(tmp_PoInfo.getO_if_result())){
									if(!"".equals(StringUtil.isNullToString(totInfo.get(0).getPo_no(),"")) && totAmt != 0){
										BgabGascbt02 tmpinfo = new BgabGascbt02();
										tmpinfo.setDoc_no(param.get(z).getDoc_no());
										tmpinfo.setSeq(param.get(z).getHid_seq());
										tmpinfo.setTot_amt(String.valueOf(totAmt));
										tmpinfo.setPo_no(tmp_PoInfo.getO_po_no());

										businessTravelDao.updateTravelerTempPo(tmpinfo);
									}
									////////////////////////////////////// TEMP PO END /////////////////////////////////////////////

									// 기존에 sap yn 업데이트 하는 부분
									businessTravelDao.updateBTToReportBySapYnRow(param.get(z));

									BgabGascbt04 travelerInfo = new BgabGascbt04();
									if(param.size() > 0){
										travelerInfo.setDoc_no(param.get(0).getDoc_no());
										travelerInfo.setEeno(param.get(0).getEeno());
									}

									int NCnt = Integer.parseInt(businessTravelDao.getSelectCountBTToReportBySapYn(travelerInfo));
									BgabGascbt02 bgabGascbt02 = new BgabGascbt02();
									if(NCnt == 0){
										bgabGascbt02.setDoc_no(travelerInfo.getDoc_no());
										bgabGascbt02.setEeno(travelerInfo.getEeno());
										bgabGascbt02.setTot_arsl_refl_yn("Y");
										businessTravelDao.updateBTToTravelerBySapYn(bgabGascbt02);
									}
								}else{
									TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
									message.setErrorCode("E");
									message.setMessage(tmp_PoInfo.getO_if_fail_msg());
								}
							}
						}else{
							TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
							message.setErrorCode("E");
							message.setMessage(do_PoInfo.getO_if_fail_msg());
						}
					}else{
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						message.setErrorCode("E");
						message.setMessage(o_PoInfo.getO_if_fail_msg());
					}
				} catch (Exception e) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					message.setErrorCode("E");
					message.setMessage(o_PoInfo.getO_if_fail_msg());
					logger.error(strMessege, e);
				}
			}
		}else{
			for(int z=0; z<param.size(); z++){
				// 기존에 sap yn 업데이트 하는 부분
				businessTravelDao.updateBTToReportBySapYnRow(param.get(z));

				BgabGascbt04 travelerInfo = new BgabGascbt04();
				if(param.size() > 0){
					travelerInfo.setDoc_no(param.get(0).getDoc_no());
					travelerInfo.setEeno(param.get(0).getEeno());
				}

				int NCnt = Integer.parseInt(businessTravelDao.getSelectCountBTToReportBySapYn(travelerInfo));
				BgabGascbt02 bgabGascbt02 = new BgabGascbt02();
				if(NCnt == 0){
					bgabGascbt02.setDoc_no(travelerInfo.getDoc_no());
					bgabGascbt02.setEeno(travelerInfo.getEeno());
					bgabGascbt02.setTot_arsl_refl_yn("Y");
					businessTravelDao.updateBTToTravelerBySapYn(bgabGascbt02);
				}
			}
//			message.setMessage(HmbMessageSource.getMessage("SWITCH.0001"));
		}

		return message;
	}

	@Override
	public List<BgabGascZ011Dto> doSearchBTToSendMail(BgabGascZ011Dto bgabGascZ011Dto){
		return businessTravelDao.doSearchBTToSendMail(bgabGascZ011Dto);
	}

	@Override
	public int vendorCheckBTToConfirmList(List<BgabGascbt01> bgabGascbt01){
		return businessTravelDao.vendorCheckBTToConfirmList(bgabGascbt01);
	}
	@Override
	public int saveVendorCheckBTToSaveDetail(BgabGascbt01 dto){
		return businessTravelDao.saveVendorCheckBTToSaveDetail(dto);
	}
	@Override
	public List<BgabGascbt01> selectBtToExcelList(BgabGascbt01 bgabGascbt04){
		return businessTravelDao.selectBtToExcelList(bgabGascbt04);
	}

	@Override
	public List<BgabGascbt02> getSelectBudgetViewNew(BgabGascbt02 bgabGascbt02){

		RfcBudgetCheck rfc = new RfcBudgetCheck(bgabGascbt02.getBudg_type());
		RfcBudgetCheckVo i_BudgetInfo = new RfcBudgetCheckVo();
		i_BudgetInfo.setI_gubn(bgabGascbt02.getBudg_type());
		i_BudgetInfo.setI_date(CurrentDateTime.getDate());
		i_BudgetInfo.setI_gjahr(CurrentDateTime.getYear());
		i_BudgetInfo.setI_kostl(bgabGascbt02.getCost_cd());
		i_BudgetInfo.setI_hkont(bgabGascbt02.getBudg_no());

		RfcBudgetCheckVo o_BudgetInfo = null;
		try {
			o_BudgetInfo = rfc.getResult(i_BudgetInfo);
		} catch (Exception e) {
			logger.error(strMessege, e);
		}

		String blance = "0";
		String commitment = "0";
		String actual = "0";
		if(!"".equals(StringUtil.isNullToString(o_BudgetInfo.getO_balance().replaceAll(",","")))){
			blance = StringUtil.isNullToString(o_BudgetInfo.getO_balance().replaceAll(",",""));
		}
		if(!"".equals(StringUtil.isNullToString(o_BudgetInfo.getO_commitment().replaceAll(",","")))){
			commitment = StringUtil.isNullToString(o_BudgetInfo.getO_commitment().replaceAll(",",""));
		}
		if(!"".equals(StringUtil.isNullToString(o_BudgetInfo.getO_actual().replaceAll(",","")))){
			actual = StringUtil.isNullToString(o_BudgetInfo.getO_actual().replaceAll(",",""));
		}

		List<BgabGascbt02> budgetList = new ArrayList<BgabGascbt02>();
		if("E".equals(o_BudgetInfo.getO_actual())){
			BgabGascbt02 setBudgetInfo = new BgabGascbt02();
//
			setBudgetInfo.setCost_cd(bgabGascbt02.getCost_cd());
			setBudgetInfo.setBudg_no(bgabGascbt02.getBudg_no());
			setBudgetInfo.setPrvs_amt("0");
			setBudgetInfo.setNatv_curr_amt("0");
			setBudgetInfo.setBalance_amt(bgabGascbt02.getBalance_amt());
			setBudgetInfo.setCost_nm(commonJobDao.getSelectCostCenterName(bgabGascbt02.getCost_cd()));
			setBudgetInfo.setBudg_result(o_BudgetInfo.getGs_msg());

			budgetList.add(setBudgetInfo);
		}else{
			BgabGascbt02 setBudgetInfo = new BgabGascbt02();

			setBudgetInfo.setCost_cd(bgabGascbt02.getCost_cd());
			setBudgetInfo.setBudg_no(bgabGascbt02.getBudg_no());
			setBudgetInfo.setBalance_amt(blance);
			if("D".equals(bgabGascbt02.getBudg_type())){
				setBudgetInfo.setNatv_curr_amt(actual);
				setBudgetInfo.setPrvs_amt(commitment);
			}else{
				setBudgetInfo.setPrvs_amt("0");
				setBudgetInfo.setNatv_curr_amt("0");
			}
			setBudgetInfo.setCost_nm(commonJobDao.getSelectCostCenterName(bgabGascbt02.getCost_cd()));
			setBudgetInfo.setBudg_result("");


			budgetList.add(setBudgetInfo);
		}

//		if(StringUtil.isNullToString(bgabGascbt02.getBudg_type()).equals("D")){
//			o_BudgetInfo.setGs_msg("");
//		}
//
//		if(StringUtil.isNullToString(o_BudgetInfo.getO_balance()).equals("")){
//			bgabGascbt02.setBalance_amt("0");
//		}else{
//			bgabGascbt02.setBalance_amt(o_BudgetInfo.getO_balance());
//		}
//		bgabGascbt02.setBudg_result(o_BudgetInfo.getGs_msg());
//
//		List<BgabGascbt02> budgetList = businessTravelDao.getSelectBudgetView(bgabGascbt02);
//
//		for(int i=0; i<budgetList.size(); i++){
//			budgetList.get(i).setBalance_amt(bgabGascbt02.getBalance_amt());
//			budgetList.get(i).setBudg_result(o_BudgetInfo.getGs_msg());
//		}
//
//		if(budgetList.size() == 0){
//			BgabGascbt02 setBudgetInfo = new BgabGascbt02();
//
//			setBudgetInfo.setCost_cd(bgabGascbt02.getCost_cd());
//			setBudgetInfo.setBudg_no(bgabGascbt02.getBudg_no());
//			setBudgetInfo.setPrvs_amt("0");
//			setBudgetInfo.setNatv_curr_amt("0");
//			setBudgetInfo.setBalance_amt(bgabGascbt02.getBalance_amt());
//			setBudgetInfo.setCost_nm(commonJobDao.getSelectCostCenterName(bgabGascbt02.getCost_cd()));
//			setBudgetInfo.setBudg_result(o_BudgetInfo.getGs_msg());
//
//			budgetList.add(setBudgetInfo);
//		}

		return budgetList;
	}
}
