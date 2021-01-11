package service;

import modles.BaseResponse;
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
 * @author Miracle
 */
@RestController
@RequestMapping("/video")
public class VideoService {

    /**
     * 获取视频的接口
     *
     * @param token    验证的token
     * @param category 本次访问的视频类别
     * @return 访问结果
     */
    @ResponseBody
    @RequestMapping(value = "/list", params = "category", method = RequestMethod.GET)
    public BaseResponse<List<VideoModel>> getVideoList(int category, @RequestHeader("token") String token) {
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
     * 获取所有视频类别的接口
     *
     * @param token 验证的token
     * @return 访问结果
     */
    @ResponseBody
    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public BaseResponse<List<VideoCategoryModel>> getCategory(@RequestHeader("token") String token) {
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
