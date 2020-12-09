package service;

import modles.BaseResponse;
import modles.VideoNewsModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
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

    @ResponseBody
    @RequestMapping(value = "/list", params = {"limit", "page", "token"}, method = RequestMethod.GET)
    public BaseResponse<List<VideoNewsModel>> getVideoNewsList(int limit, long page, String token) {
        BaseResponse<List<VideoNewsModel>> result = new BaseResponse<>();
        if (TokenUtil.verificationToken(token)) {
            result.setCode(ApiConfig.FAIL_CODE_402);
            return result;
        }
        String sql = "select * from news limit " + (page == 0? page : limit * page - 1) + ", " + limit + "";
        ResultSet rs = DbUtil.Instance.queryDb(sql);
        try {
            if (rs != null) {
                List<VideoNewsModel> videoNewsModels = new ArrayList<>();
                while (rs.next()) {
                    VideoNewsModel videoNewsModel = new VideoNewsModel();
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
}
