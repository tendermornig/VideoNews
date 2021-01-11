package service;

import modles.*;
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
@RequestMapping("/news")
public class NewsService {

    /**
     * 获取资讯列表
     *
     * @param limit 每次返回的资讯个数
     * @param page  第几页
     * @param token 验证的token
     * @return 访问结果
     */
    @ResponseBody
    @RequestMapping(value = "/list", params = {"page", "limit"}, method = RequestMethod.GET)
    public BaseResponse<List<NewsModel>> getNewsList(long page, int limit, @RequestHeader("token") String token) {
        BaseResponse<List<NewsModel>> result = new BaseResponse<>();
        if (TokenUtil.verificationToken(token)) {
            result.setCode(ApiConfig.FAIL_CODE_402);
            return result;
        }
        String sql = "select * from news limit " + (page == 0 ? page : limit * page - 1) + ", " + limit + "";
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
            } else {
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
     * 获取资讯略缩图接口
     * @param newsId 资讯id
     * @param token 验证的token
     * @return 资讯略缩图集合
     */
    @ResponseBody
    @RequestMapping(value = "/thumb", params = {"newsId"}, method = RequestMethod.GET)
    public BaseResponse<List<NewsThumbModel>> getNewsThumb(int newsId, @RequestHeader("token") String token) {
        BaseResponse<List<NewsThumbModel>> result = new BaseResponse<>();
        if (TokenUtil.verificationToken(token)) {
            result.setCode(ApiConfig.FAIL_CODE_402);
            return result;
        }
        String sql = "select * from news_thumb where news_id = " + newsId + "";
        ResultSet rs = DbUtil.Instance.queryDb(sql);
        try {
            if (rs != null) {
                List<NewsThumbModel> newsThumbModels = new ArrayList<>();
                while (rs.next()) {
                    NewsThumbModel newsThumbModel = new NewsThumbModel();
                    newsThumbModel.setThumbId(rs.getInt(1));
                    newsThumbModel.setThumbUrl(rs.getString(2));
                    newsThumbModel.setNewsId(rs.getInt(3));
                    newsThumbModels.add(newsThumbModel);
                }
                result.setMsg(ApiConfig.SUCCESS);
                result.setCode(ApiConfig.SUCCESS_CODE_200);
                result.setData(newsThumbModels);
            } else {
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
