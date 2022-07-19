package com.example.demo.restapi.controller;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.restapi.config.JwtTokenConfig;
import com.example.demo.restapi.config.Seed;
import com.example.demo.restapi.entity.Income;
import com.example.demo.restapi.entity.Member;
import com.example.demo.restapi.entity.Spend;
import com.example.demo.restapi.service.CookieService;
import com.example.demo.restapi.service.IncomeService;
import com.example.demo.restapi.service.MemberService;
import com.example.demo.restapi.service.SpendService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = {"Member"}) 
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/szs")
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private JwtTokenConfig jwtTokenConfig;
	
	@Autowired
	private CookieService cookieService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	private SpendService spendService;
	
	@Autowired
	private IncomeService incomeService;
	
	@ApiOperation(value = "/szs/signup", notes = "신규 회원 가입")
	@PostMapping(value = "/signup")
	public Map<String, Object> signUp(@RequestBody Member member) {
		Map<String, Object> result = new LinkedHashMap<>();
		Member newMember = Member.builder()
				.userid(member.getUserid())
				.password(passwordEncoder.encode(member.getPassword()))
				.name(member.getName())
				.regno(Seed.encrypt(member.getRegno()))
				.build();
		if((member.getName().equals("홍길동") && member.getRegno().equals("860824-1655068"))
				||(member.getName().equals("김둘리") && member.getRegno().equals("921108-1582816"))
				||(member.getName().equals("마징가") && member.getRegno().equals("880601-2455116"))
				||(member.getName().equals("베지터") && member.getRegno().equals("910411-1656116"))
				||(member.getName().equals("손오공") && member.getRegno().equals("820326-2715702"))) {
			Map<String, Object> rtnVal = memberService.signup(newMember);
			result = rtnVal;
		}else {
			result.put("status", "fail");
			result.put("error", "가입 불가능한 사용자 입니다.");
		}
		return result;
	}
	
	@ApiOperation(value = "/szs/login", notes = "로그인")
	@PostMapping(value = "/login")
	public Map<String, Object> login(@RequestBody Member member, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> result = new LinkedHashMap<>();
		try {
			Member user = memberService.login(member.getUserid(), member.getPassword());
			String token = jwtTokenConfig.createToken(user.getUserid());
			String rToken = jwtTokenConfig.refreshToken(user.getUserid());
			
			Cookie loginToken = cookieService.createCookie(JwtTokenConfig.TOKEN_NAME, token);
			Cookie refreshToken = cookieService.createCookie(JwtTokenConfig.REFRESH_TOKEN_NAME, rToken);
			response.addCookie(loginToken);
			response.addCookie(refreshToken);
			result.put("result", "success");
			result.put("token", loginToken);
		} catch(Exception e) {
			result.put("result", "fail : " + e.getMessage());
		}
		return result;
	}
	
	@SuppressWarnings("unused")
	@ApiOperation(value = "/szs/me", notes = "내 정보 가져오기")
	@GetMapping(value = "/me")
	public Map<String, Object> getMyInfo(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new LinkedHashMap<>();
		
		Cookie[] cookie = request.getCookies();
		String token = null;
		String userId = null;
		for(Cookie c : cookie) {
			if(c.getName().equals(JwtTokenConfig.TOKEN_NAME)) {
				token = c.getValue();
			}
		}
		if(token == null) {
			result.put("result", "토큰 없음");
		}else{
			userId = jwtTokenConfig.getUserId(token);
			Member member = memberService.getMyInfo(userId);
			Member info = Member.builder()
					.userid(member.getUserid())
					.password(member.getPassword())
					.name(member.getName())
					.regno(Seed.decrypt(member.getRegno().getBytes()))
					.build();
			if(member != null) {
				result.put("MyInfo", info);
            }else {
            	result.put("result", "fail");
            	result.put("error", "존재하지 않는 사용자 입니다.");
            }
		}
        return result;
    }
	
	@ApiOperation(value = "/szs/scrap", notes = "내 데이터 스크랩하기")
	@PostMapping(value = "/scrap")
	public Map<String, Object>  scrapData(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		Map<String, Object> result = new LinkedHashMap<>();
		
		Cookie[] cookie = httpRequest.getCookies();
		String token = null;
		String userId = null;
		for(Cookie c : cookie) {
			if(c.getName().equals(JwtTokenConfig.TOKEN_NAME)) {
				token = c.getValue();
			}
		}
		if(token == null) {
			result.put("Status", "토큰없음");
		}else{
			userId = jwtTokenConfig.getUserId(token);
			Member member = memberService.getMyInfo(userId);
			if(member != null) {
			try {
				HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
				factory.setConnectTimeout(30000);
				factory.setReadTimeout(30000);
				RestTemplate template = new RestTemplate(factory);
				
				JSONObject request = new JSONObject();
				request.put("name", member.getName());
				request.put("regNo", Seed.decrypt(member.getRegno().getBytes()));
				HttpHeaders header = new HttpHeaders();
				header.setContentType(MediaType.APPLICATION_JSON);
		        HttpEntity<String> entity = new HttpEntity<String>(request.toString(), header);
		        
		        String url = "https://codetest.3o3.co.kr/v1/scrap";
		        String answer = template.postForObject(url, entity, String.class);
		        JSONObject json = new JSONObject(answer);
		        String status = json.getString("status");
		        
		        if(status.equals("success")) {
		        	JSONObject data = json.getJSONObject("data");
		        	JSONObject jsonList = data.getJSONObject("jsonList");
		        	JSONArray scrap2 = jsonList.getJSONArray("scrap002");
		        	JSONArray scrap1 = jsonList.getJSONArray("scrap001");
		        	for(Object s2 : scrap2) {
		        		JSONObject s2Json 	= (JSONObject) s2;
		        		Spend spend = Spend.builder()
		        				.userid(member.getUserid())
		        				.total(Long.parseLong(s2Json.getString("총사용금액").replaceAll(",", "")))
		        				.type(s2Json.getString("소득구분"))
		        				.build();
		        		spendService.deleteSpend(member.getUserid());
		        		spendService.saveSpend(spend);
		        	}
		        	
		        	for(Object s1 : scrap1) {
		        		JSONObject s1Json 	= (JSONObject) s1;
		        		Income income = Income.builder()
		        				.company(s1Json.getString("기업명"))
		        				.companycode(s1Json.getString("사업자등록번호"))
		        				.userid(member.getUserid())
		        				.type(s1Json.getString("소득내역"))
		        				.total(Long.parseLong(s1Json.getString("총지급액").replaceAll(",", "")))
		        				.startdate(s1Json.getString("업무시작일"))
		        				.enddate(s1Json.getString("업무종료일"))
		        				.payday(s1Json.getString("지급일"))
		        				.paygbn(s1Json.getString("소득구분"))
		        				.build();
		        		
		        		incomeService.deleteIncome(member.getUserid());
		        		incomeService.saveIncome(income);
		        	}
		        	result.put("Status", "success");
		        	result.put("saveUserName", member.getName());
		        	result.put("saveUserRegno", Seed.decrypt(member.getRegno().getBytes()));
		        }else{
		        	result.put("error",  json.get("errors"));
		        }
		        } catch (HttpClientErrorException | HttpServerErrorException e) {
		            result.put("Status", e.getRawStatusCode());
		            result.put("body"  , e.getStatusText());
		        }
			}
				
		}
		return result;
	}
	
	@ApiOperation(value = "/szs/refund", notes = "내 환급액 조회하기")
	@GetMapping(value = "/refund")
	public Map<String, Object> getRefundData(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		Map<String, Object> result = new LinkedHashMap<>();
		
		Cookie[] cookie = httpRequest.getCookies();
		String token = null;
		String userId = null;
		for(Cookie c : cookie) {
			if(c.getName().equals(JwtTokenConfig.TOKEN_NAME)) {
				token = c.getValue();
			}
		}
		if(token == null) {
			result.put("Status", "토큰없음");
		}else{
			userId = jwtTokenConfig.getUserId(token);
			Member member = memberService.getMyInfo(userId);
			if(member != null) {
				try {
					String name = member.getName();
					long limit = incomeService.getLimit(userId);
					long tax = spendService.getTax(userId);
					result.put("이름", name);
					result.put("한도", memberService.changeValueString(limit));
					result.put("공제액", memberService.changeValueString(tax));
					if(limit < tax) {
						result.put("환급액", memberService.changeValueString(limit));
					}else{
						result.put("환급액", memberService.changeValueString(tax));
					}
				} catch (Exception e) {
					result.put("error", e.getMessage());
				}
			}
		}
		return result;
	}
}