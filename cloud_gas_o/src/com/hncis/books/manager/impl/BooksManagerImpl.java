package com.hncis.books.manager.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.hncis.books.dao.BooksDao;
import com.hncis.books.manager.BooksManager;
import com.hncis.books.vo.BgabGascbk01Dto;
import com.hncis.books.vo.BgabGascbk02Dto;
import com.hncis.books.vo.BgabGascbk03Dto;
import com.hncis.books.vo.BgabGascbk04Dto;
import com.hncis.common.message.HncisMessageSource;
import com.hncis.common.util.BpmApiUtil;
import com.hncis.common.util.FileUtil;
import com.hncis.common.util.StringUtil;
import com.hncis.common.vo.BgabGascZ011Dto;
import com.hncis.common.vo.CommonMessage;

@Service("booksManagerImpl")
public class BooksManagerImpl implements BooksManager{
    private static final String strMessege = "messege";

    private transient Log logger = LogFactory.getLog(getClass());
    
	@Autowired
	public BooksDao booksDao;

	@Override
	public List<BgabGascbk03Dto> selectBkToLrgCombo(BgabGascbk03Dto dto) {
		return booksDao.selectBkToLrgCombo(dto);
	}

	@Override
	public List<BgabGascbk04Dto> selectBkToMrgCombo(BgabGascbk04Dto dto) {
		return booksDao.selectBkToMrgCombo(dto);
	}
	
	@Override
	public int selectCountBkToBookList(BgabGascbk01Dto dto) {
		return booksDao.selectCountBkToBookList(dto);
	}

	@Override
	public List<BgabGascbk01Dto> selectBkToBookList(BgabGascbk01Dto dto) {
		
		List<BgabGascbk01Dto> list = booksDao.selectBkToBookList(dto);
		
		for(BgabGascbk01Dto vo : list){
			vo.setBk_rent(vo.getBk_rent_yn().equals("Y") ? "<font color='blue'>"+HncisMessageSource.getMessage("psb")+"</font>" : "<font color='red'>"+HncisMessageSource.getMessage("ipsb")+"</font>");
			
			if(!StringUtil.isNullToString(vo.getOrg_file_nm()).equals("")){
				vo.setImg_pop(HncisMessageSource.getMessage("preview"));
			}
		}
		
		return list;
	}
	
	@Override
	public CommonMessage updateBkToBookRequest(BgabGascbk02Dto dto) {
		
		CommonMessage message = new CommonMessage();
		
		String bpmSaveMsg="";
		String bpmReqMsg="";
		try{
			
			int chkCnt = booksDao.selectBkToBookExtrQty(dto);
			
			if(chkCnt < 1){
				message.setCode1("N");
				message.setMessage(HncisMessageSource.getMessage("MSG.VAL.0047"));
			}else{
				
				String docNo = StringUtil.getDocNo();
				dto.setDoc_no(docNo);
				int cnt = booksDao.insertBkToBookRequest(dto);
				
				message.setCode1("Y");
				message.setMessage(HncisMessageSource.getMessage("APPLY.0000"));
				message.setCode(dto.getBk_seq());
				
				// BPM API호출
				String processCode = "P-B-004"; 	//프로세스 코드 (도서 프로세스) - 프로세스 정의서 참조
				String bizKey = dto.getDoc_no();	//신청서 번호
				String statusCode = "GASBZ01240010";	//액티비티 코드 (도서신청서작성) - 프로세스 정의서 참조
				String loginUserId = dto.getEeno();	//로그인 사용자 아이디
				String comment = null;
				String roleCode = "GASROLE01240030";   //도서 담당자 역할코드
				//역할정보
				List<String> approveList = new ArrayList<String>();
				List<String> managerList = new ArrayList<String>();
				managerList.add("10000001");

				bpmSaveMsg = BpmApiUtil.sendSaveTask(processCode, bizKey, statusCode, loginUserId, roleCode, approveList, managerList );
				
				bpmReqMsg = BpmApiUtil.sendCompleteTask(processCode, bizKey, statusCode, loginUserId, roleCode, approveList, managerList );
			}
			
		}catch (Exception e) {
			logger.error(strMessege, e);
			message.setCode1("N");
			message.setMessage(HncisMessageSource.getMessage("APPLY.0001"));
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		return message;
	}
	
	@Override
	public CommonMessage updateBkToBookReturn(BgabGascbk02Dto dto) {
		CommonMessage message = new CommonMessage();
		
		try{
			
			int cnt = booksDao.updateBkToBookReturn(dto);
			
			message.setCode1("Y");
			message.setMessage(HncisMessageSource.getMessage("RETURN.0002"));
			
		}catch (Exception e) {
			message.setCode1("N");
			message.setMessage(HncisMessageSource.getMessage("RETURN.0003"));
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return message;
	}
	
	@Override
	public CommonMessage updateBkToBookReturnList(List<BgabGascbk02Dto> list) {
		CommonMessage message = new CommonMessage();
		
		try{
			
			int cnt = booksDao.updateBkToBookReturnList(list);
			
			message.setCode1("Y");
			message.setMessage(HncisMessageSource.getMessage("RETURN.0002"));
			
		}catch (Exception e) {
			message.setCode1("N");
			message.setMessage(HncisMessageSource.getMessage("RETURN.0003"));
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return message;
	}
	
	public int updateBkToBookRent(List<BgabGascbk02Dto> list){
		return booksDao.updateBkToBookRent(list);
	}
	
	
	@Override
	public int selectCountBkToBookRentList(BgabGascbk02Dto dto) {
		return booksDao.selectCountBkToBookRentList(dto);
	}

	@Override
	public List<BgabGascbk02Dto> selectBkToBookRentList(BgabGascbk02Dto dto) {
		List<BgabGascbk02Dto> list = booksDao.selectBkToBookRentList(dto);
		
		for(BgabGascbk02Dto vo : list){
			if(vo.getBk_dly_yn().equals("Y")){
				vo.setPgs_st_nm(HncisMessageSource.getMessage("delay"));
			}
		}
		
		return list;
	}
	
	@Override
	public int deleteRentListToRequestCancel(List<BgabGascbk02Dto> dto){
		return booksDao.deleteRentListToRequestCancel(dto);
	}
	
	@Override
	public void saveBkToLrgList(List<BgabGascbk03Dto> iList, List<BgabGascbk03Dto> uList) {
		
		int iCnt = booksDao.insertBkToLrgList(iList);
		int uCnt = booksDao.updateBkToLrgList(uList);
	}

	@Override
	public List<BgabGascbk03Dto> selectBkListToLrgInfo(BgabGascbk03Dto vo) {
		return booksDao.selectBkListToLrgInfo(vo);
	}
	
	@Override
	public void saveBkToMrgList(List<BgabGascbk04Dto> iList, List<BgabGascbk04Dto> uList) {
		
		int iCnt = booksDao.insertBkToMrgList(iList);
		int uCnt = booksDao.updateBkToMrgList(uList);
	}

	@Override
	public List<BgabGascbk04Dto> selectBkListToMrgInfo(BgabGascbk04Dto vo) {
		return booksDao.selectBkListToMrgInfo(vo);
	}

	@Override
	public void deleteBkToLrgList(List<BgabGascbk03Dto> dList) {
		
		int cnt1 = booksDao.deleteBkToLrgList(dList);
		int cnt2 = booksDao.deleteBkToMrgDtlList(dList);
		
	}

	@Override
	public void deleteBkToMrgList(List<BgabGascbk04Dto> dList) {
		int cnt1 = booksDao.deleteBkToMrgList(dList);
	}

	@Override
	public int selectCountBkToBookMgmtList(BgabGascbk01Dto dto) {
		return booksDao.selectCountBkToBookMgmtList(dto);
	}

	@Override
	public List<BgabGascbk01Dto> selectBkToBookMgmtList(BgabGascbk01Dto dto) {
		
		List<BgabGascbk01Dto> list = booksDao.selectBkToBookMgmtList(dto);
		
		for(BgabGascbk01Dto vo : list){
			if(!StringUtil.isNullToString(vo.getOrg_file_nm()).equals("")){
				vo.setImg_pop("미리보기");
			}
		}
		return list;
	}

	@Override
	public CommonMessage isnertBkToBookInfo(BgabGascbk01Dto dto) {
		
		CommonMessage message = new CommonMessage();
		
		try{
//			if(dto.getBk_seq().equals("")){
//				String docNo = StringUtil.getDocNo();
//				dto.setBk_seq(docNo);
//			}
			
			int cnt = booksDao.isnertBkToBookInfo(dto);
			
			message.setCode1("Y");
			message.setMessage(HncisMessageSource.getMessage("SAVE.0000"));
			message.setCode(dto.getBk_seq());
		}catch (Exception e) {
			logger.error(strMessege, e);
			message.setCode1("N");
			message.setMessage(HncisMessageSource.getMessage("SAVE.0001"));
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return message;
	}
	
	@Override
	public CommonMessage deleteBkToBookInfo(BgabGascbk01Dto dto) {
		
		CommonMessage message = new CommonMessage();
		
		try{
			
			int cnt = booksDao.deleteBkToBookInfo(dto);
			
			message.setCode1("Y");
			message.setMessage(HncisMessageSource.getMessage("DELETE.0000"));
			message.setCode(dto.getBk_seq());
		}catch (Exception e) {
			logger.error(strMessege, e);
			message.setCode1("N");
			message.setMessage(HncisMessageSource.getMessage("DELETE.0001"));
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return message;
	}

	@Override
	public BgabGascbk01Dto selectInfoBkToBookInfo(BgabGascbk01Dto dto) {
		return booksDao.selectInfoBkToBookInfo(dto);
	}

	
public void saveBkToFile(HttpServletRequest req, HttpServletResponse res, BgabGascZ011Dto bgabGascZ011Dto){
		
		String msg        = "";
		String resultUrl  = "xbk06_file.gas";
		String[] result   = new String[4];
		String[] paramVal = new String[4];
		
		try{
			
			paramVal[0] = "file_name";
			paramVal[1] = "old_file_name";
			paramVal[2] = "books";
			
			result = FileUtil.uploadFileView(req, res, paramVal);
			
			if(result != null){
				if(result[0] != null){
					bgabGascZ011Dto.setOgc_fil_nm(result[0]);
					bgabGascZ011Dto.setFil_nm(result[5]);
					bgabGascZ011Dto.setFil_mgn_qty(Integer.parseInt(result[3]));
					Integer fileRs = (Integer)booksDao.insertBkToFile(bgabGascZ011Dto);
				}
				msg = result[4];
				
			}else{
				resultUrl = "xbk06_file.gas";
				msg = HncisMessageSource.getMessage("FILE.0001");
			}
		}catch(Exception e){
			resultUrl = "xbk06_file.gas";
			msg = HncisMessageSource.getMessage("FILE.0001");
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
			req.setAttribute("saveYn",  "Y");
			req.getRequestDispatcher(resultUrl).forward(req, res);
		
			return;
		}catch(Exception e){
			logger.error(strMessege, e);
		}
	}
	
	public List<BgabGascZ011Dto> getSelectBkToFile(BgabGascZ011Dto bgabGascZ011Dto){
		return booksDao.getSelectBkToFile(bgabGascZ011Dto);
	}
	
	public BgabGascZ011Dto getSelectBkToFileInfo(BgabGascZ011Dto bgabGascZ011Dto){
		return booksDao.getSelectBkToFileInfo(bgabGascZ011Dto);
	}
	
	public int deleteBkToFile(List<BgabGascZ011Dto> bgabGascZ011IList){
		String fileResult = "";
		for(int i=0; i<bgabGascZ011IList.size(); i++){
			BgabGascZ011Dto fileInfo = bgabGascZ011IList.get(i);
			try {
				fileResult = FileUtil.deleteFile(fileInfo.getCorp_cd(), "books", fileInfo.getOgc_fil_nm());
			} catch (IOException e) {
				logger.error(strMessege, e);
			}
		}
		Integer fileDRs = (Integer)booksDao.deleteBkToFile(bgabGascZ011IList);
		return fileDRs;
	}
	
}
