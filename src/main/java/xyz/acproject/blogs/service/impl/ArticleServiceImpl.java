package xyz.acproject.blogs.service.impl;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import xyz.acproject.blogs.dao.ArticleContentMapper;
import xyz.acproject.blogs.dao.ArticlePraiseMapper;
import xyz.acproject.blogs.dao.ArticlePropertyMapper;
import xyz.acproject.blogs.dao.ArticleScaleMapper;
import xyz.acproject.blogs.dao.CommentMapper;
import xyz.acproject.blogs.dao.CommentPraiseMapper;
import xyz.acproject.blogs.dao.SmallcategoryMapper;
import xyz.acproject.blogs.entity.Admin;
import xyz.acproject.blogs.entity.ArticleContent;
import xyz.acproject.blogs.entity.ArticlePraise;
import xyz.acproject.blogs.entity.ArticleProperty;
import xyz.acproject.blogs.entity.ArticlePropertyExample;
import xyz.acproject.blogs.entity.ArticleScale;
import xyz.acproject.blogs.entity.Comment;
import xyz.acproject.blogs.entity.CommentPraise;
import xyz.acproject.blogs.entity.Smallcategory;
import xyz.acproject.blogs.service.ArticleService;
import xyz.acproject.blogs.tools.cleanXss;
import xyz.acproject.blogs.tools.page.page3.Page;
import xyz.acproject.blogs.tools.returnJson.FastjsonConfig.FastJsonUtil;
import xyz.acproject.blogs.tools.returnJson.FastjsonConfig.Response;
import xyz.acproject.blogs.tools.returnJson.PubilcClass.ArticlePraiseJson;
import xyz.acproject.blogs.tools.returnJson.PubilcClass.CommentPraiseNum;
import xyz.acproject.blogs.tools.returnJson.PubilcClass.CommentsFlag;
import xyz.acproject.blogs.tools.returnJson.PubilcClass.CommentsJson;
import xyz.acproject.blogs.tools.returnJson.PubilcClass.SearchJson;

@Service
public class ArticleServiceImpl implements ArticleService {
//	@Autowired
//	private ArticleContentMapper articleContentMapper;//??????
	@Autowired
	private ArticlePraiseMapper articlePraiseMapper;
	@Autowired
	private ArticlePropertyMapper articlePropertyMapper;
	@Autowired
	private ArticleScaleMapper articleScaleMapper;
	@Autowired
	private CommentMapper commentMapper;
	@Autowired
	private CommentPraiseMapper commentPraiseMapper;
	@Autowired
	private ArticleContentMapper articleContentMapper;
	@Autowired
	private SmallcategoryMapper smallcategoryMapper;
	
	
	@Override
	@Cacheable(cacheNames="newArticles")
	public List<ArticleProperty> selectListByPageNum(int num) {
		// TODO ???????????????????????????
		// ????????????5???????????????????????????
		List<ArticleProperty> list = articlePropertyMapper.selectListByPageNum(num * 5);
		return list;
	}

	@Override
	public long articlePropertyCount() {
		// TODO ???????????????????????????
		return articlePropertyMapper.count();
	}
	@Cacheable(cacheNames="indexCommentNum")
	@Override
	public List<ArticleProperty> selectListOrderBycommentNumDesc() {
		// TODO ???????????????????????????
		return articlePropertyMapper.selectListOrderBycommentNumDesc();
	}
	@Cacheable(cacheNames="indexArticlePv")
	@Override
	public List<ArticleProperty> selectListOrderByarticlePvDesc() {
		// TODO ???????????????????????????
		return articlePropertyMapper.selectListOrderByarticlePvDesc();
	}
	@Cacheable(cacheNames="indexArticlePraise")
	@Override
	public List<ArticleProperty> selectListOrderByarticlePraiseDesc() {
		// TODO ???????????????????????????
		return articlePropertyMapper.selectListOrderByarticlePraiseDesc();
	}

	@Override
	public ArticleProperty selectListByid(Integer id) {
		// TODO ???????????????????????????
		return articlePropertyMapper.selectByid(id);
	}

	@Override
	public List<ArticleProperty> selectListBybcidOrscid(Integer bcid, Integer scid, int num) {
		// TODO ???????????????????????????
		// ????????????5???????????????????????????
		return articlePropertyMapper.selectListBybcidOrscid(bcid, scid, num * 5);
	}

	@Override
	public List<ArticleProperty> selectListByscid(Integer scid) {
		// TODO ???????????????????????????
		return articlePropertyMapper.selectListByscid(scid);
	}

	/**
	 * @effect {?????????????????????????????????json???}
	 */
	@Override
	public void selectListByTitle(String title, HttpServletRequest req, HttpServletResponse resp, PrintWriter writer) {
		// TODO ???????????????????????????
		try {
			req.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO ??????????????? catch ???
			e.printStackTrace();
		}
		// ????????????????????????????????????
		List<ArticleProperty> articleProperties = articlePropertyMapper.selectListByTitle(title);
		// ????????????????????????????????????????????????????????????
		List<String> titleList = articleProperties.stream().map(ArticleProperty::getTitle).collect(Collectors.toList());
		// ??????????????????????????????????????????id????????????
		List<Integer> idList = articleProperties.stream().map(ArticleProperty::getId).collect(Collectors.toList());
		// ???"???"?????????????????????
		String afterString = String.join("???", titleList);
		// ???????????????
		String beforeString = afterString.replace(title,
				"<span style='color:red;font-weight:bold;'>" + title + "</span>");
		// ???????????????????????????
		String[] reArray = beforeString.split("???");
		// ????????????
		List<String> retitleList = new ArrayList<String>();
		// ???????????????
		for (String string : reArray) {
			retitleList.add(string);
		}
		// ????????????
		SearchJson<List<?>> searchJson = new SearchJson<List<?>>(titleList, retitleList, idList);
		// ??????fastjson???????????????json???
		String jsonString = FastJsonUtil.toJson(Response.success(searchJson, req));
		// ?????????????????????????????????json???
		resp.setContentType("application/json;charset=UTF-8");
		writer.append(jsonString);
		writer.flush();
		writer.close();
	}

	@Override
	public List<ArticleProperty> selectListByTitles(String title, HttpServletRequest req, HttpServletResponse resp,
			int num) {
		// TODO ???????????????????????????
		// ????????????5???????????????????????????
		return articlePropertyMapper.selectListByTitles(title, num * 5);
	}

	@Override
	public int updateByApidOnPv(int apid) {
		// TODO ???????????????????????????
		return articleScaleMapper.updateByApidOnPv(apid);
	}

	@Override
	public long selectListByTitlesCount(String title) {
		// TODO ???????????????????????????
		return articlePropertyMapper.selectListByTitlesCount(title);
	}

	@Override
	public long selectListBybcidOrscidCount(Integer bcid, Integer scid) {
		// TODO ???????????????????????????
		return articlePropertyMapper.selectListBybcidOrscidCount(bcid, scid);
	}

	/**
	 * @effect {??????????????????}
	 */
	@Override
	public void insertArticlePraise(int apid, HttpServletRequest req, HttpServletResponse resp, PrintWriter writer) {
		// TODO ???????????????????????????
		// ??????ip
		String ip = req.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getRemoteAddr();
		}
		// ????????????
		Calendar calendar = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timeString = dateFormat.format(calendar.getTime());
		Timestamp time = Timestamp.valueOf(timeString.toString());
		// ??????????????????????????????
		ArticlePraise record = new ArticlePraise();
		record.setApid(apid);
		record.setName(ip);
		record.setTime(time);
		// TODO ???????????????????????????
		try {
			req.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO ??????????????? catch ???
			e.printStackTrace();
		}
		// ??????????????????????????????????????????????????????false????????????????????????
		if (articlePraiseMapper.selectByNameAndApid(ip, apid) != null) {
			// ???????????????
			long count = articlePraiseMapper.countByApid(apid);
			// ????????????
			ArticlePraiseJson<String> articlePraiseJson = new ArticlePraiseJson<String>("false", count + "");
			// fastjson??????json??????
			String jsonString = FastJsonUtil.toJson(Response.success(articlePraiseJson, req));
			// ??????json?????????
			resp.setContentType("application/json;charset=UTF-8");
			writer.append(jsonString);
			writer.flush();
			writer.close();
		} else {
			// ?????????????????????
			articlePraiseMapper.insertIsExists(record);
			// ???????????????
			long count = articlePraiseMapper.countByApid(apid);
			// ???????????????
			articleScaleMapper.updateByApidOnPraise((int) count, apid);
			// ????????????
			ArticlePraiseJson<String> articlePraiseJson = new ArticlePraiseJson<String>("true", count + "");
			// fastjson??????json??????
			String jsonString = FastJsonUtil.toJson(Response.success(articlePraiseJson, req));
			// ??????json?????????
			resp.setContentType("application/json;charset=UTF-8");
			writer.append(jsonString);
			writer.flush();
			writer.close();
		}
	}

	/**
	 * @effect {??????ip(??????)???????????????id?????????????????????}
	 */
	@Override
	public void selectArticlePraiseByNameAndApid(String ip, Integer apid, HttpServletRequest req,
			HttpServletResponse resp, PrintWriter writer) {
		// TODO ???????????????????????????
		try {
			req.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO ??????????????? catch ???
			e.printStackTrace();
		}
		if (articlePraiseMapper.selectByNameAndApid(ip, apid) != null) {
			// ???????????????
			long count = articlePraiseMapper.countByApid(apid);
			// ????????????
			ArticlePraiseJson<String> articlePraiseJson = new ArticlePraiseJson<String>("true", count + "");
			// fastjson??????json??????
			String jsonString = FastJsonUtil.toJson(Response.success(articlePraiseJson, req));
			// ??????json?????????
			resp.setContentType("application/json;charset=UTF-8");
			writer.append(jsonString);
			writer.flush();
			writer.close();
		} else {
			// ???????????????
			long count = articlePraiseMapper.countByApid(apid);
			// ????????????
			ArticlePraiseJson<String> articlePraiseJson = new ArticlePraiseJson<String>("false", count + "");
			// fastjson??????json??????
			String jsonString = FastJsonUtil.toJson(Response.success(articlePraiseJson, req));
			// ??????json?????????
			resp.setContentType("application/json;charset=UTF-8");
			writer.append(jsonString);
			writer.flush();
			writer.close();
		}
	}

	/**
	 * @effect {??????????????????json}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void selectCommentByApid(Integer apid, HttpServletRequest req, HttpServletResponse resp, PrintWriter writer,
			Model model) {
		// TODO ???????????????????????????
		long counts = commentMapper.countByApid(apid);
		articleScaleMapper.updateByApidOnCommentNum((int) counts, apid);
		// ??????ip
		String ip = req.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getRemoteAddr();
		}
		// ????????????
		long total = commentMapper.countByApid(apid);
		model.addAttribute("total", total);
		String pageNow = req.getParameter("pageNow");
		if (pageNow == null)
			pageNow = "" + 1;
		long count = commentMapper.countByReApid(apid);
		Page page = new Page((int) count, Integer.parseInt(pageNow));
		page.setPageSize(10);
		// ?????????
		List<Comment> commentList = commentMapper.selectByApid(apid, page.getStartPos(), page.getPageSize());
		List<CommentsFlag> commentsFlagList = new ArrayList<CommentsFlag>();
		for (Comment comment : commentList) {
			if (commentPraiseMapper.selectByIpAndCommentId(ip, comment.getId(), apid) != null) {
				commentsFlagList.add(new CommentsFlag(comment, "true"));
			} else {
				commentsFlagList.add(new CommentsFlag(comment, "false"));
			}
		}
		/*
		 * for (CommentsFlag commentsFlag : commentsFlagList) {
		 * System.out.println(commentsFlag); }
		 */
		// ????????????
		List<Comment> recommentList = commentMapper.selectByReApid(apid);
		List<CommentsFlag> recommentsFlagList = new ArrayList<CommentsFlag>();
		for (Comment comment : recommentList) {
			if (commentPraiseMapper.selectByIpAndCommentId(ip, comment.getId(), apid) != null) {
				recommentsFlagList.add(new CommentsFlag(comment, "true"));
			} else {
				recommentsFlagList.add(new CommentsFlag(comment, "false"));
			}
		}
		// ????????????json???
		CommentsJson commentsJson = new CommentsJson(commentsFlagList, recommentsFlagList, page);
		String jsonString = FastJsonUtil.toJson(Response.success(commentsJson, req));
		resp.setContentType("application/json;charset=UTF-8");
		writer.write(jsonString);
		writer.flush();
		writer.close();
	}

	/**
	 * @effect {????????????????????????}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void insertPraiseByApid(Integer apid, Integer cid, HttpServletRequest req, HttpServletResponse resp,
			PrintWriter writer) {
		// TODO ???????????????????????????
		// ??????ip
		String ip = req.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getRemoteAddr();
		}
		// ????????????
		Calendar calendar = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timeString = dateFormat.format(calendar.getTime());
		Timestamp time = Timestamp.valueOf(timeString.toString());
		// ????????????????????????
		CommentPraise record = new CommentPraise();
		record.setName(ip);
		record.setTime(time);
		record.setCommentid(cid);
		record.setApid(apid);
		long praiseNum = 0;
		if (commentPraiseMapper.selectByIpAndCommentId(ip, cid, apid) != null) {
			praiseNum = commentMapper.selectByIdOnPraiseNum(cid);
			CommentPraiseNum commentPraiseNum = new CommentPraiseNum(praiseNum, "true");
			String jsonString = FastJsonUtil.toJson(Response.success(commentPraiseNum, req));
			resp.setContentType("application/json;charset=UTF-8");
			writer.write(jsonString);
			writer.flush();
			writer.close();
		} else {
			int a = commentPraiseMapper.insertIsExists(record);
			if (a != 0) {
				commentMapper.updateByIdOnPraiseNum(cid);
				praiseNum = commentMapper.selectByIdOnPraiseNum(cid);
				CommentPraiseNum commentPraiseNum = new CommentPraiseNum(praiseNum, "true");
				String jsonString = FastJsonUtil.toJson(Response.success(commentPraiseNum, req));
				resp.setContentType("application/json;charset=UTF-8");
				writer.write(jsonString);
				writer.flush();
				writer.close();
			} else {
			//	commentMapper.updateByIdOnPraiseNum(cid);
				praiseNum = commentMapper.selectByIdOnPraiseNum(cid);
				CommentPraiseNum commentPraiseNum = new CommentPraiseNum(praiseNum, "false");
				String jsonString = FastJsonUtil.toJson(Response.success(commentPraiseNum, req));
				resp.setContentType("application/json;charset=UTF-8");
				writer.write(jsonString);
				writer.flush();
				writer.close();
			}
		}
	}

	/**
	 * @effect {?????????????????????}
	 */
	@Override
	public void insertCommentSelective(Integer apid, String value, Comment record, HttpServletRequest req,
			HttpServletResponse resp, PrintWriter writer) {
		// TODO ???????????????????????????
		// ??????ip
		String ip = req.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getRemoteAddr();
		}
		// ????????????
		Calendar calendar = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timeString = dateFormat.format(calendar.getTime());
		Timestamp time = Timestamp.valueOf(timeString.toString());
		// ????????????
		Admin admin = (Admin) (req.getSession().getAttribute("admin"));
		if (admin != null) {
			record.setName(admin.getName());
		} else {
			record.setName("??????");
		}
		record.setValue(cleanXss.cleanXSS(value));
		record.setIp(ip);
		record.setTime(time);
		record.setApid(apid);
		// ?????????????????????????????????????????????????????????????????????????????????????????????error
		int a = commentMapper.insertSelective(record);
		if (a != 0) {
			long count = commentMapper.countByApid(apid);
			articleScaleMapper.updateByApidOnCommentNum((int) count, apid);
			Comment comment = commentMapper.selectByTime(time);
			String jsonString = FastJsonUtil.toJson(Response.success(comment, req));
			resp.setContentType("application/json;charset=UTF-8");
			writer.write(jsonString);
			writer.flush();
			writer.close();
		} else {
			String jsonString = FastJsonUtil.toJson(Response.error(req));
			resp.setContentType("application/json;charset=UTF-8");
			writer.write(jsonString);
			writer.flush();
			writer.close();
		}
	}

	/**
	 * @effect {????????????????????????}
	 */
	@Override
	public void insertReCommentSelective(Integer apid, String value, Integer cid, Comment record,
			HttpServletRequest req, HttpServletResponse resp, PrintWriter writer) {
		// TODO ???????????????????????????
		// ??????ip
		String ip = req.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getRemoteAddr();
		}
		// ????????????
		Calendar calendar = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timeString = dateFormat.format(calendar.getTime());
		Timestamp time = Timestamp.valueOf(timeString.toString());
		// ????????????
		Admin admin = (Admin) (req.getSession().getAttribute("admin"));
		if (admin != null) {
			record.setName(admin.getName());
		} else {
			record.setName("??????");
		}
		record.setValue(cleanXss.cleanXSS(value));
		record.setIp(ip);
		record.setTime(time);
		record.setCid(cid);
		record.setApid(apid);
		// ?????????????????????????????????????????????????????????????????????????????????????????????error
		int a = commentMapper.insertSelective(record);
		if (a != 0) {
			long count = commentMapper.countByApid(apid);
			articleScaleMapper.updateByApidOnCommentNum((int) count, apid);
			Comment recomment = commentMapper.selectByTime(time);
			String jsonString = FastJsonUtil.toJson(Response.success(recomment, req));
			resp.setContentType("application/json;charset=UTF-8");
			writer.write(jsonString);
			writer.flush();
			writer.close();
		} else {
			String jsonString = FastJsonUtil.toJson(Response.error(req));
			resp.setContentType("application/json;charset=UTF-8");
			writer.write(jsonString);
			writer.flush();
			writer.close();
		}
	}

	@Override
	public List<ArticleProperty> selectListByScidDescOnTime(Integer scid) {
		// TODO ???????????????????????????
		return articlePropertyMapper.selectListByScidDescOnTime(scid);
	}

	@Override
	public List<ArticleProperty> selectListByBcidDescOnTime(Integer bcid) {
		// TODO ???????????????????????????
		return articlePropertyMapper.selectListByBcidDescOnTime(bcid);
	}

	@Override
	public long CountByTotalArticlePv() {
		// TODO ???????????????????????????
		return articleScaleMapper.CountByTotalArticlePv();
	}

	@Override
	public List<ArticleProperty> selectListByPage2(Integer startPos, Integer pageSize) {
		// TODO ???????????????????????????
		return articlePropertyMapper.selectListByPage2(startPos, pageSize);
	}

	/**
	 * @effect {????????????}
	 */
	@Transactional
	@Override
	public int deleteArticle(Integer apid) {
		// TODO ???????????????????????????
		int as = articleScaleMapper.deleteByApid(apid);
		int ac = articleContentMapper.deleteByApid(apid);
		int ap = articlePropertyMapper.deleteByPrimaryKey(apid);
		articlePraiseMapper.deleteByApid(apid);
		commentMapper.deleteByApid(apid);
		commentPraiseMapper.deleteByApid(apid);
		if (as != 0 && ac != 0 && ap != 0) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * @effect {???????????????}
	 */
	@Transactional
	@Override
	public int insertArticle(Integer apid, String title, String content, String describe, String imgUrl, Integer bcid,
			String smallCategory, Model model, HttpServletRequest req, HttpServletResponse resp) {
		// TODO ???????????????????????????
		// ??????????????????---------
		ArticleProperty articleProperty = new ArticleProperty();
		apid = articlePropertyMapper.selectByMaxApid() + 1;// ??????apid
		articleProperty.setId(apid);
		articleProperty.setTitle(title);
		articleProperty.setDescribe(describe);
		articleProperty.setBcid(bcid);
		articleProperty.setImgurl("../" + imgUrl);
		Admin admin = (Admin) req.getSession().getAttribute("admin");
		articleProperty.setCreateman(admin.getName());
		Calendar calendar = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timeString = dateFormat.format(calendar.getTime());
		Timestamp time = Timestamp.valueOf(timeString.toString());
		articleProperty.setCreatetime(time);
		articleProperty.setContentid(apid);
		// ???????????????
		Smallcategory smallcategorys = smallcategoryMapper.selectByValue(smallCategory);
		if (smallcategorys != null) {
			articleProperty.setScid(smallcategorys.getId());
		} else {
			int scid = smallcategoryMapper.selectByMaxScid() + 1;
			Smallcategory newSmallcategory = new Smallcategory();
			newSmallcategory.setBcid(bcid);
			newSmallcategory.setValue(smallCategory);
			newSmallcategory.setId(scid);
			articleProperty.setScid(scid);
			smallcategoryMapper.insertSelective(newSmallcategory);
		}
		int ap = articlePropertyMapper.insertSelective(articleProperty);
		// ??????????????????---------
		ArticleContent articleContent = new ArticleContent();
		articleContent.setApid(apid);
		articleContent.setId(apid);
		articleContent.setContent(content);
		int ac = articleContentMapper.insertSelective(articleContent);
		// ?????????????????????---------
		ArticleScale articleScale = new ArticleScale();
		articleScale.setApid(apid);
		articleScale.setId(apid);
		int as = articleScaleMapper.insertSelective(articleScale);
		if (ap != 0 && ac != 0 && as != 0) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public long countByApid(Integer apid) {
		// TODO ???????????????????????????
		return commentMapper.countByApid(apid);
	}

	@Override
	public int updateArticle(Integer apid, String title, String content, String describe, String imgUrl, Integer bcid,
			String smallCategory, Model model, HttpServletRequest req, HttpServletResponse resp) {
		// TODO ???????????????????????????
		// ??????????????????---------
		ArticleProperty articleProperty = new ArticleProperty();
		articleProperty.setId(apid);
		articleProperty.setTitle(title);
		articleProperty.setDescribe(describe);
		articleProperty.setBcid(bcid);
		articleProperty.setImgurl("../" + imgUrl);
		Admin admin = (Admin) req.getSession().getAttribute("admin");
		articleProperty.setCreateman(admin.getName());
		Calendar calendar = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timeString = dateFormat.format(calendar.getTime());
		Timestamp time = Timestamp.valueOf(timeString.toString());
		articleProperty.setCreatetime(time);
		articleProperty.setContentid(apid);
		// ???????????????
		Smallcategory smallcategorys = smallcategoryMapper.selectByValue(smallCategory);
		if (smallcategorys != null) {
			articleProperty.setScid(smallcategorys.getId());
		} else {
			int scid = smallcategoryMapper.selectByMaxScid() + 1;
			Smallcategory newSmallcategory = new Smallcategory();
			newSmallcategory.setBcid(bcid);
			newSmallcategory.setValue(smallCategory);
			newSmallcategory.setId(scid);
			articleProperty.setScid(scid);
			smallcategoryMapper.insertSelective(newSmallcategory);
		}
		int ap = articlePropertyMapper.updateByPrimaryKeySelective(articleProperty);
		// ??????????????????---------
		ArticleContent articleContent = new ArticleContent();
		articleContent.setApid(apid);
		articleContent.setId(apid);
		articleContent.setContent(content);
		int ac = articleContentMapper.updateByPrimaryKeySelective(articleContent);
		// ?????????????????????---------
		ArticleScale articleScale = new ArticleScale();
		articleScale.setApid(apid);
		articleScale.setId(apid);
		int as = articleScaleMapper.updateByPrimaryKeySelective(articleScale);
		if (ap != 0 && ac != 0 && as != 0) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public int countBycreateMan(String createman) {
		// TODO ???????????????????????????
		return articlePropertyMapper.countBycreateMan(createman);
	}

	@Override
	public int updateByPrimaryKeySelectiveP(ArticleProperty record) {
		// TODO ???????????????????????????
		return articlePropertyMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<ArticleProperty> selectByExampleP(ArticlePropertyExample example) {
		// TODO ???????????????????????????
		return articlePropertyMapper.selectByExample(example);
	}

}
