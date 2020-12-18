package service;

import modles.BaseResponse;
import modles.NewsModel;
import modles.VideoCategoryModel;
import modles.VideoModel;
import org.springframework.web.bind.annotation.*;
import utils.ApiConfig;
import utils.DbUtil;
import utils.TokenUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 光
 */
@RestController
@RequestMapping("/list")
public class NewsService {

    /**
     * 获取资讯列表
     * @param limit 每次返回的资讯个数
     * @param page 第几页
     * @param token 验证的token
     * @return 访问结果
     */
    @ResponseBody
    @RequestMapping(value = "/news", params = {"limit", "page"}, method = RequestMethod.GET)
    public BaseResponse<List<NewsModel>> getNewsList(int limit, long page, @RequestHeader("token")String token) {
        BaseResponse<List<NewsModel>> result = new BaseResponse<>();
        if (TokenUtil.verificationToken(token)) {
            result.setCode(ApiConfig.FAIL_CODE_402);
            return result;
        }
        String sql = "select * from news limit " + (page == 0? page : limit * page - 1) + ", " + limit + "";
        ResultSet rs = DbUtil.Instance.queryDb(sql);
        try {
            if (rs != null) {
                List<NewsModel> videoNewsModels = new ArrayList<>();
                while (rs.next()) {
                    NewsModel videoNewsModel = new NewsModel();
                    videoNewsModel.setNewsId(rs.getInt(1));
                    videoNewsModel.setNewsTitle(rs.getString(2));
                    videoNewsModel.setAuthorName(rs.getString(3));
                    videoNewsModel.setHeaderUrl(rs.getString(4));
                    videoNewsModel.setCommentCount(rs.getInt(5));
                    videoNewsModel.setReleaseDate(rs.getString(6));
                    videoNewsModel.setType(rs.getInt(7));
                    videoNewsModels.add(videoNewsModel);
                }
                result.setMsg(ApiConfig.SUCCESS);
                result.setCode(ApiConfig.SUCCESS_CODE_200);
                result.setData(videoNewsModels);
            }else {
                result.setCode(ApiConfig.FAIL_CODE_403);
            }
        } catch (SQLException troubles) {
            troubles.printStackTrace();
        } finally {
            DbUtil.Instance.close();
        }
        return result;
    }

    /**
     * 获取所有视频类别的接口
     * @param token 验证的token
     * @return 访问结果
     */
    @ResponseBody
    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public BaseResponse<List<VideoCategoryModel>> getCategory(@RequestHeader("token")String token) {
        BaseResponse<List<VideoCategoryModel>> result = new BaseResponse<>();
        if (TokenUtil.verificationToken(token)) {
            result.setMsg(ApiConfig.SUCCESS);
            result.setCode(ApiConfig.FAIL_CODE_402);
            return result;
        }
        String sql = "select * from video_category";
        ResultSet rs = DbUtil.Instance.queryDb(sql);
        try {
            if (rs != null) {
                List<VideoCategoryModel> categoryList = new ArrayList<>();
                while (rs.next()) {
                    VideoCategoryModel categoryModel = new VideoCategoryModel();
                    categoryModel.setCategoryId(rs.getInt(1));
                    categoryModel.setCategoryName(rs.getString(2));
                    categoryList.add(categoryModel);
                }
                result.setMsg(ApiConfig.SUCCESS);
                result.setCode(ApiConfig.SUCCESS_CODE_200);
                result.setData(categoryList);
            }else {
                result.setCode(ApiConfig.FAIL_CODE_403);
            }
        } catch (SQLException troubles) {
            troubles.printStackTrace();
        } finally {
            DbUtil.Instance.close();
        }
        return result;
    }

    /**
     * 获取视频的接口
     * @param token 验证的token
     * @param category 本次访问的视频类别
     * @return 访问结果
     */
    @ResponseBody
    @RequestMapping(value = "/video",params = "category", method = RequestMethod.GET)
    public BaseResponse<List<VideoModel>> getVideoList(@RequestHeader("token")String token, int category) {
        BaseResponse<List<VideoModel>> result = new BaseResponse<>();
        if (TokenUtil.verificationToken(token)) {
            result.setMsg(ApiConfig.SUCCESS);
            result.setCode(ApiConfig.FAIL_CODE_402);
            return result;
        }
        String sql = "select * from video_list where category_id = " + category + "";
        ResultSet rs = DbUtil.Instance.queryDb(sql);
        try {
            if (rs != null) {
                List<VideoModel> categoryList = new ArrayList<>();
                while (rs.next()) {
                    VideoModel videoModel = new VideoModel();
                    videoModel.setVtitle(rs.getString(2));
                    videoModel.setAuthor(rs.getString(3));
                    videoModel.setCoverUrl(rs.getString(4));
                    videoModel.setHeadurl(rs.getString(5));
                    videoModel.setCommentNum(rs.getInt(6));
                    videoModel.setLikeNum(rs.getInt(7));
                    videoModel.setCollectNum(rs.getInt(8));
                    videoModel.setPlayUrl(rs.getString(9));
                    categoryList.add(videoModel);
                }
                result.setMsg(ApiConfig.SUCCESS);
                result.setCode(ApiConfig.SUCCESS_CODE_200);
                result.setData(categoryList);
            }else {
                result.setCode(ApiConfig.FAIL_CODE_403);
            }
        } catch (SQLException troubles) {
            troubles.printStackTrace();
        } finally {
            DbUtil.Instance.close();
        }
        return result;
    }
}
