package com.tianfang.controller;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.tianfang.business.dto.AddressesDto;
import com.tianfang.business.service.IAddressesService;
import com.tianfang.common.constants.DataStatus;
import com.tianfang.common.constants.SessionConstants;
import com.tianfang.common.digest.MD5Coder;
import com.tianfang.common.exception.SystemException;
import com.tianfang.common.model.Response;
import com.tianfang.common.tools.RandomPicTools;
import com.tianfang.common.util.DateUtils;
import com.tianfang.common.util.PropertiesUtils;
import com.tianfang.common.util.StringUtils;
import com.tianfang.common.util.UUIDGenerator;
import com.tianfang.dto.LoginUserDto;
import com.tianfang.user.dto.UserDto;
import com.tianfang.user.service.IEmailSendService;
import com.tianfang.user.service.ISmsSendService;
import com.tianfang.user.service.IUserService;

/**
 * @author YIn
 * @time:2016年3月11日 上午10:50:38
 * @ClassName: UserController
 * @Description: 用户controller
 * @
 */
@Controller
@RequestMapping(value = "user")
public class UserController extends BaseController{
	
	protected static final Log logger = LogFactory.getLog(UserController.class);
    
    @Autowired
	private ISmsSendService iSmsSendService;
    
    @Autowired
	private IEmailSendService iEmailSendService;
    
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IAddressesService addressesService;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
	@RequestMapping(value = "/SMS/send")
	@ResponseBody
	public Response<String> sendSMS(String mobilePhone,String picCaptcha,HttpSession session,HttpServletRequest request) {
		Response<String> result = new Response<String>();
		UserDto dto = new UserDto();
		dto.setMobile(mobilePhone);
		List<UserDto> list = userService.findUserByParam(dto);
		if (list == null || list.size() <= 0) {
			result.setStatus(-1);
			result.setMessage("此号码未注册过请先注册！");
			return result;
		}
		if(StringUtils.isEmpty(picCaptcha)){
			result.setStatus(DataStatus.HTTP_FAILE);
			result.setMessage("验证码为空！");
			return result;
		}
		String randomPicSession = session.getAttribute("RandomCode").toString().toLowerCase();
		if (!picCaptcha.toLowerCase().equals(randomPicSession)) {
			result.setStatus(DataStatus.HTTP_FAILE);
			result.setMessage("验证码输入错误！");
			return result;
		}		
//		LoginUserDto loginUserDto = SessionUtil.getLoginSession(session);
//		if (loginUserDto == null) {
//			result.setStatus(DataStatus.HTTP_FAILE);
//			result.setMessage("用户未登录");
//			return result;
//		}
//		
//		if (editPhone!=null) {
//			String userAccountId = SessionUtil.getLoginSession(session).getId();
//			UsersDto oldusersDto = iUserService
//					.selectUsersByUserAccountId(userAccountId);
//			mobilePhone=oldusersDto.getMobileNo();
//		}
		
		/*if(!CheckSendMsg(redisTemplate, mobilePhone, request)){
			result.setStatus(DataStatus.HTTP_FAILE);
			result.setMessage("发送短信过于频繁,请您稍后再试");
			return result;			
		}*/

		/*if(!CheckSendMsg(redisTemplate, mobilePhone)){
			result.setStatus(DataStatus.HTTP_FAILE);
			result.setMessage("超过当天最多的发送次数");
			return result;
		}*/
		
		int randomNumber = (int) (Math.random() * 9000 + 1000);
		String content = "温馨提示，为了保护您的隐私，请您在90秒内输入" + randomNumber + "验证码。";// 短信内容
		//String returnString = iSmsSendService.sendSms(randomNumber, mobilePhone, content);
//		String keyCode = SessionConstants.PHONE_NUMBER+loginUserDto.getId();
		String keyCode = mobilePhone + "forget";
		redisTemplate.opsForValue().set(keyCode, randomNumber, 90, TimeUnit.SECONDS);
		System.out.println("mobilePhone:"+mobilePhone);
		System.out.println("keyCode:"+keyCode);
		System.out.println("randomNumber:"+randomNumber);
		
//		session.setAttribute(SessionConstants.SMS_VALIDAT_NUMBER, "");
//		session.setAttribute(SessionConstants.SMS_VALIDAT_NUMBER, randomNumber);
//		session.setAttribute(SessionConstants.PHONE_NUMBER, mobilePhone);
//		session.setMaxInactiveInterval(90); // 设置session有效期为90秒
		//return JsonUtil.getJsonStr(new RequestResult(true, returnString + " " + randomNumber));
		result.setStatus(DataStatus.HTTP_SUCCESS);
//		result.setMessage(returnString + " " + randomNumber);
		return result;
	}
    
    @RequestMapping(value = "/email/send")
	@ResponseBody
	public Response<String> sendEmail(String email, HttpSession session,
			HttpServletRequest request) {
		Response<String> result = new Response<String>();
		UserDto dto = new UserDto();
		dto.setEmail(email);
		List<UserDto> list = userService.findUserByParam(dto);
		if (list == null || list.size() <= 0) {
			result.setStatus(-1);
			result.setMessage("此号码未注册过请先注册！");
			return result;
		}
//		LoginUserDto loginUserDto = SessionUtil.getLoginSession(session);
//		if (loginUserDto == null) {
//			result.setStatus(DataStatus.HTTP_FAILE);
//			result.setMessage("用户未登录");
//			return result;
//		}

		// ctx = SpringContextUtil.getApplicationContext();
		// JavaMailSender sender = (JavaMailSender) ctx.getBean("mailSender");
		// SimpleMailMessage mail = new SimpleMailMessage(); //<span
		// style="color: #ff0000;">注意SimpleMailMessage只能用来发送text格式的邮件</span>
		int randomNumber = (int) (Math.random() * 9000 + 1000); // 验证码
		String content = "温馨提示，为了保护您的隐私，请您在90秒内输入" + randomNumber + "验证码。";// 短信内容
		String from_ = "jujusports@ssic.cn"; // 发送的邮箱
		String subject = "聚运动邮箱验证"; // 主题
		// session.setAttribute(SessionConstants.EMAIL_VALIDAT_NUMBER, "");
		// session.setAttribute(SessionConstants.EMAIL_VALIDAT_NUMBER,
		// randomNumber);
		// session.setAttribute(SessionConstants.EMAIL_NUMBER, email);
		// session.setMaxInactiveInterval(90); //设置session有效期为90秒

//		 redisTemplate.opsForValue().set(SessionConstants.EMAIL_NUMBER,
//		 randomNumber);
//		String keyCode = SessionConstants.EMAIL_NUMBER + loginUserDto.getId();
		String keyCode = SessionConstants.EMAIL_NUMBER;
		redisTemplate.opsForValue().set(keyCode, randomNumber, 90,
				TimeUnit.SECONDS);
		try {
			// mail.setTo(email);//接受者
			// mail.setFrom("jamhihi@126.com");//发送者,这里还可以另起Email别名，不用和xml里的username一致
			// mail.setSubject("验证邮箱");//主题
			// mail.setText(content);//邮件内容
			// sender.send(mail);
			iEmailSendService.sendEmail(randomNumber, email, content, from_,
					subject);
		} catch (Exception e) {
			// return JsonUtil.getJsonStr(new RequestResult(true,"发送邮件失败！"));
			result.setStatus(DataStatus.HTTP_FAILE);
			result.setMessage("发送邮件失败！");
			return result;
		}
		result.setStatus(DataStatus.HTTP_SUCCESS);
		result.setMessage("发送邮件成功！");
		return result;
	}
    
    @RequestMapping("/forget")
    public ModelAndView index(HttpServletRequest request){
    	ModelAndView mv = getModelAndView();
    	mv.setViewName("/forget");
        return mv;
    }
    /**
     * 获取图片验证码
     * @param reponse
     * @param request
     * @param session
     */
    @RequestMapping(value = "/drawRandom")
    @ResponseBody
    public void drawRandom(HttpServletResponse reponse,
            HttpServletRequest request, HttpSession session) {
        RandomPicTools randomPicTools = new RandomPicTools();
        int width = 80;// 图片宽
        int height = 26;// 图片高
        int lineSize = 40;// 干扰线数量
        int stringNum = 4;// 随机产生字符数量
        session = request.getSession();
        // BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();// 产生Image对象的Graphics对象,改对象可以在图像上进行各种绘制操作
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 18));
        g.setColor(randomPicTools.getRandColor(110, 133));
        // 绘制干扰线
        for (int i = 0; i <= lineSize; i++) {
            randomPicTools.drowLine(g, width, height);
        }
        // 绘制随机字符
        String randomString = "";
        for (int i = 1; i <= stringNum; i++) {
            randomString = randomPicTools.drowString(g, randomString, i);
        }
        session.removeAttribute("RandomCode");
        session.setAttribute("RandomCode", randomString);
        g.dispose();
        try {
            // 将内存中的图片通过流动形式输出到客户端
            ImageIO.write(image, "JPEG", reponse.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 比较图片验证码
     * @param imgCode
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value ="/authImageCode")
    public Response<String> authImageCode(String imgCode,HttpSession session){
        Response<String> result =new Response<String>();
        String randomCode = session.getAttribute("RandomCode").toString().toLowerCase();
        if(randomCode.equals(imgCode.toLowerCase())){
            result.setStatus(DataStatus.HTTP_SUCCESS);;
        }else{
            result.setStatus(DataStatus.HTTP_FAILE);
            result.setMessage("图片验证码错误,请重新输入图片验证码~~");
        }
        return result;
    }
    
    /**
     * 
     * mobliephone：手机用户找回密码
     * @param validateCode
     * @param mobilePhone
     * @param password
     * @return
     * @throws Exception 
     * @exception	
     * @author Administrator
     * @date 2015年11月19日 下午6:06:16
     */
    @RequestMapping("/mobliephone")
    @ResponseBody
    public Response<String> mobliephone( String validateCode,String mobilePhone,String password) throws Exception {
        logger.debug("validateCode =" + validateCode);
        Response<String> result = new Response<String>();
        String md5oldPwd = MD5Coder.encodeMD5Hex(password);
        String keyCode = mobilePhone + "forget";
        if(validateCode ==null){
            result.setStatus(DataStatus.HTTP_FAILE);
            result.setMessage("短信验证码失效！");
            return result;
        }
        if(redisTemplate.opsForValue().get(keyCode)==null || redisTemplate.opsForValue().get(keyCode).equals("")){
            result.setStatus(DataStatus.HTTP_FAILE);
            result.setMessage("短信验证码失效！");
            return result;
        }
        String checkCode = redisTemplate.opsForValue().get(keyCode).toString();
        if (validateCode.equals(checkCode)) {
        	UserDto dto = new UserDto();
        	dto.setMobile(mobilePhone);
    		List<UserDto> list = userService.findUserByParam(dto);
    		if(list == null || list.size() == 0){
    			 result.setStatus(DataStatus.HTTP_SUCCESS);
                 result.setMessage("此手机号码没注册过请先注册！");
                 return result;
    		}
    		list.get(0).setPassword(md5oldPwd);
            Integer flag = userService.update(list.get(0));
            if (flag == 1) {
                result.setStatus(DataStatus.HTTP_SUCCESS);
                result.setMessage("手机找回密码成功！");
            }
            if(flag == 0){
              result.setStatus(DataStatus.HTTP_FAILE);
              result.setMessage("手机验证失败！");   
            }
            return result;
        }else {
            result.setStatus(DataStatus.HTTP_FAILE);
            result.setMessage("验证码错误！");
            return result;
        }
    }   
    
    @RequestMapping("/email")
    @ResponseBody
    public Response<String> email(String validateCode,String email,String password) throws Exception {
        logger.debug("validateCode =" + validateCode);
        Response<String> result = new Response<String>();
        String md5oldPwd = MD5Coder.encodeMD5Hex(password);
        String keyCode = SessionConstants.EMAIL_NUMBER;
        if (redisTemplate.opsForValue().get(keyCode) == null) {
            result.setStatus(DataStatus.HTTP_FAILE);
            result.setMessage("验证码失效");
            return result;
        }

        if (StringUtils.isEmpty(email)) {
            result.setStatus(DataStatus.HTTP_FAILE);
            result.setMessage("邮箱为空");
            return result;
        }
        String checkCode = redisTemplate.opsForValue().get(keyCode).toString();
        if (validateCode.equals(checkCode)) {
        	
        	UserDto dto = new UserDto();
        	dto.setEmail(email);
    		List<UserDto> list = userService.findUserByParam(dto);
    		if(list == null || list.size() == 0){
    			 result.setStatus(DataStatus.HTTP_SUCCESS);
                 result.setMessage("此邮箱没注册过请先注册！");
                 return result;
    		}
    		list.get(0).setPassword(md5oldPwd);
            Integer flag = userService.update(list.get(0));
            if (flag ==1) {
                result.setStatus(DataStatus.HTTP_SUCCESS);
                result.setMessage("邮箱找回密码成功！");
            }
            if (flag == 0) {
              result.setStatus(DataStatus.HTTP_FAILE);
              result.setMessage("邮箱验证失败！");   
            }
            return result;
        }else {
            result.setStatus(DataStatus.HTTP_FAILE);
            result.setMessage("验证码错误！");
            return result;
        }
    }
    
    /**
	 * 去用户详情页面
	 * @return
	 */
	@RequestMapping("/toPerson")
	public ModelAndView toPerson(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		ModelAndView mv = getModelAndView();
		UserDto userDto= (UserDto) session.getAttribute(SessionConstants.LOGIN_USER_INFO);
		if(userDto == null){
			return new ModelAndView("redirect:/index.htm");
//			return mv;
		}
		List<UserDto> list = userService.findUserByParam(userDto);
		if(list == null || list.size() == 0){
			return new ModelAndView("redirect:/index.htm");
//			return mv;
		}
		UserDto dto = list.get(0);
		if(dto.getCreateTime() != null){
			dto.setCreateTimeStr(DateUtils.format(dto.getCreateTime(), DateUtils.YMD_DASH));
		}
		if(dto.getLastLoginTime() != null){
			dto.setLastLoginTimeStr(DateUtils.format(dto.getLastLoginTime(), DateUtils.YMD_DASH_WITH_TIME));
		}
		
		//根据省Id查询省名称
		if(StringUtils.isNotEmpty(dto.getProvince())){
		AddressesDto province = new AddressesDto();
		province.setId(Integer.valueOf(dto.getProvince().replace(",", "")));
		List<AddressesDto> provinceList = addressesService.findAddressList(province);
		dto.setProvinceStr(provinceList.get(0).getName());
		}
		
		//根据市Id查询市名称
		if(StringUtils.isNotEmpty(dto.getArea())){
		AddressesDto area = new AddressesDto();
		area.setId(Integer.valueOf(dto.getArea().replace(",", "")));
		List<AddressesDto> areaList = addressesService.findAddressList(area);
		dto.setAreaStr(areaList.get(0).getName());
		}
		
		//根据地区Id查询地区名称
		if(StringUtils.isNotEmpty(dto.getLocation())){
		AddressesDto location = new AddressesDto();
		location.setId(Integer.valueOf(dto.getLocation().replace(",", "")));
		List<AddressesDto> locationList = addressesService.findAddressList(location);
		dto.setLocationStr(locationList.get(0).getName());
		}
		
		//获取所有的省份
		AddressesDto addr = new AddressesDto();
		addr.setLevel(1); //省份
		List<AddressesDto> allProvince = addressesService.findAddressList(addr);
		
		mv.addObject("allProvince", allProvince);
		mv.addObject("userInfo", dto);
		mv.setViewName("/person");
		return mv;
	}
	
	/**
	 * @author YIn
	 * @time:2016年2月3日 上午9:46:24
	 * @param userDto
	 * @return
	 */
    @ResponseBody
    @RequestMapping("/edit") 
    public Response<String> edit(UserDto userDto,@RequestParam(value = "file",required = false)  MultipartFile file,
			HttpServletRequest request,HttpServletResponse response){
     Response<String> result = new Response<String>();
     if(userDto == null ){
    	 result.setStatus(DataStatus.HTTP_FAILE);
		 result.setMessage("用户信息为空");
		 return result;
     }
   	int stat = 0;
	try {
        if (file != null) {
//          Response<UploadDto> res = uploadPic(myfile, request, response);
        	Map<String, String> map = uploadImages(file , request);
        	userDto.setPic(map.get("fileUrl"));
        }
		stat = userService.update(userDto);
	} catch (Exception e) {
		e.printStackTrace();
	}
   	 if(stat == 0){
   		 result.setStatus(DataStatus.HTTP_FAILE);
		 result.setMessage("修改用户信息失败");
   	 }else{
   		 result.setStatus(DataStatus.HTTP_SUCCESS);
		 result.setMessage("修改用户信息成功");
   	 }
   	return result;
    }
    
    @ResponseBody
    @RequestMapping("/uploadImages.do"   )  
    public Map<String, String> uploadImages(@RequestParam("file") MultipartFile file,HttpServletRequest request){      	
    	//String context = "/upload";
		String realPath = PropertiesUtils.getProperty("upload.url");
		String fileDe = DateUtils.format(new Date(), DateUtils.YMD);
		String path = "";
		String filePath = "";
		String fileName = ""; //重新新命名
		String realName = "";
		Map<String, String> m = new HashMap<String, String>();
    	if(file.isEmpty()){
    		System.out.println("请选择需要上传的文件!");  
    		m.put("message", "请选择需要上传的文件!");
	       	return m;
    	}else{
    			realName = file.getOriginalFilename();
 	            System.out.println("fileName4---------->" + realName); 
 	            if(file.getSize()> DataStatus._FILESIZE_){
 	       		System.out.println("上传图片大小不允许超过1M");
 	       		m.put("message", "上传图片大小不允许超过1M");
 	       		return m;
 	            }
 	                int pre = (int) System.currentTimeMillis();  
 	                path = realPath + "/" + fileDe;
 	                fileName = this.getUploadFileName(file.getOriginalFilename());
 	                filePath = path  + "/" + fileName;
 	                File f = new File(path);
 	                //如果文件夹不存在则创建    
 	                if(!f.exists() && !f.isDirectory()) {
 	                  f.mkdir();    
 	                }
 	                try {  
 	                	file.transferTo(new File(path + "/" + fileName));
 	                    int finaltime = (int) System.currentTimeMillis();  
 	                    System.out.println("上传3共耗时：" + (finaltime - pre) + "毫秒");  
 	                }catch (FileNotFoundException e) {
 	                    e.printStackTrace();
 	                }catch (IOException e) {  
 	                    e.printStackTrace();  
 	                }  
    	}
        System.out.println("上传成功4"); 
        m.put("fileUrl", filePath);
        m.put("realName", realName);
        return m;  
    }
    
    public  String getUploadFileName(String fileName) {
  		String tempFile = fileName.substring(fileName.lastIndexOf(".")+1);
  		return UUIDGenerator.getUUID32Bit() + "." + tempFile;
  	}
    
    public static Boolean CheckSendMsg(final RedisTemplate<String, Object> redisTemplate,final String mobilePhone,HttpServletRequest request){
    	String keySessionId = mobilePhone + DateUtils.format(new Date(), DateUtils.YMD_DASH)+"diff";
    	if(redisTemplate.opsForValue().get(keySessionId)!=null){
    		String nowDate = (String)redisTemplate.opsForValue().get(keySessionId);
    		long  diffmin = DateUtils.diffNowMin(DateUtils.parse(nowDate, DateUtils.YMD_DASH_WITH_TIME));
    		if(diffmin < 10){
    			return false;
    		}
    	}else{
    		String nowDate = DateUtils.format(new Date(), DateUtils.YMD_DASH_WITH_TIME);
    		redisTemplate.opsForValue().set(keySessionId, nowDate, 10, TimeUnit.MINUTES);
    	}
    	return true;
    }
    
	public static Boolean CheckSendMsg(final RedisTemplate<String, Object> redisTemplate,final String mobilePhone){
		String phoneKey = mobilePhone + DateUtils.format(new Date(), DateUtils.YMD_DASH);
		if(redisTemplate.opsForValue().get(phoneKey)!=null){
			Integer tempRounds = (Integer)redisTemplate.opsForValue().get(phoneKey);
			if(tempRounds > Integer.valueOf(PropertiesUtils.getProperty("maxSendNumber"))){
				return false;
			}
			redisTemplate.delete(phoneKey);
			redisTemplate.opsForValue().set(phoneKey, (tempRounds+1), 24, TimeUnit.HOURS);	
		}else{
			redisTemplate.opsForValue().set(phoneKey, 1, 24, TimeUnit.HOURS);
		}
		return true;
	}
	

	/**
	 * 用户注册页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/toRegiest")
    public ModelAndView regiest(){
    	ModelAndView mv = getModelAndView();
    	mv.setViewName("/regiest");
        return mv;
    }
	
	/**
	 * 用户注册
	 * @param session
	 * @param sportUserReqDto
	 * @param randomPic
	 * @return
	 */
	@RequestMapping(value = "/register")
	@ResponseBody
	public Response<UserDto> register(
			HttpSession session,
			UserDto sportUserReqDto,
			@RequestParam(value = "randomPic", required = false) String randomPic,String picCaptcha) {
		logger.debug("SportUserReqDto：" + sportUserReqDto);
		Response<UserDto> result = new Response<UserDto>();
		//sportUserReqDto.setUtype(UserTypeEnum.NORMALUSER.getIndex());
		String md5oldPwd;// 获取页面上输入的密码并加密校验
		try {
			md5oldPwd = MD5Coder.encodeMD5Hex(sportUserReqDto.getPassword());
			sportUserReqDto.setPassword(md5oldPwd);
		} catch (Exception e) {
			throw new SystemException(e.getMessage(), e);
		}
		String key = "";
		if(StringUtils.isBlank(sportUserReqDto.getMobile())){
			 key = "reg" + sportUserReqDto.getEmail();
		}else{
			 key = "reg" + sportUserReqDto.getMobile();
		}
		if (StringUtils.isNotBlank(picCaptcha)) {
			String randomPicSession = session.getAttribute("RandomCode").toString().toLowerCase();
			if (!picCaptcha.toLowerCase().equals(randomPicSession)) {
				result.setStatus(DataStatus.HTTP_FAILE);
				result.setMessage("验证码输入错误！");
				return result;
			}
		}
		if(!StringUtils.isBlank(randomPic)){
			int num;
			try {
				num = Integer.parseInt(randomPic);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				result.setStatus(DataStatus.HTTP_FAILE);
				result.setMessage("输入的短信验证码不是4位数字！");
				return result;
			}
			if (redisTemplate.opsForValue().get(key) == null) {
				result.setStatus(DataStatus.HTTP_FAILE);
				result.setMessage("没有点击获取验证码！");
				return result;
			}if(!redisTemplate.opsForValue().get(key).equals(num)){
				result.setStatus(DataStatus.HTTP_FAILE);
				result.setMessage("手机验证码输入错误！");
				return result;
			}
		}
		if (null != userService.checkUser(sportUserReqDto)) {
			result.setStatus(DataStatus.HTTP_FAILE);
			result.setMessage("用户名已存在！");
			return result;
		}
		UserDto dto = userService.regiest(sportUserReqDto);
		if (dto == null) {
			result.setStatus(DataStatus.HTTP_FAILE);
			result.setMessage("注册失败！");
			return result;
		} else {
			/*LoginUserDto loginUserDto = new LoginUserDto();
			loginUserDto.setId(loginUserDto.getId());
			loginUserDto.setType(loginUserDto.getType());
			session.setAttribute(SessionConstants.LOGIN_USER_INFO, loginUserDto);*/
			if(!StringUtils.isEmpty(dto.getEmail())){
				dto.setUserAccount(dto.getEmail());
			}
			if(!StringUtils.isEmpty(dto.getMobile())){
				dto.setUserAccount(dto.getMobile());
			}
			UserDto userInfo = userService.checkUser(dto);
			UserDto loginUserDto = new UserDto();
			loginUserDto.setId(userInfo.getId());
			loginUserDto.setUtype(userInfo.getUtype());
			session.setAttribute(SessionConstants.LOGIN_USER_INFO, loginUserDto);
			if(userInfo != null){
				//result.setData(sportUserReqDto.getUserAccount());
				loginUserDto.setUserAccount(sportUserReqDto.getUserAccount());
				redisTemplate.opsForValue().set(userInfo.getId(), loginUserDto);
			}
			result.setStatus(DataStatus.HTTP_SUCCESS);
			result.setMessage("恭喜您注册成功！");
			return result;
		}
	}
}
