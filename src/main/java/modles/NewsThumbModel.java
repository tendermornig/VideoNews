package modles;

import java.io.Serializable;

/**
 * @author Miracle
 */
public class NewsThumbModel implements Serializable {

    private int thumbId;
    private String thumbUrl;
    private int newsId;

    public int getThumbId() {
        return thumbId;
    }

    public void setThumbId(int thumbId) {
        this.thumbId = thumbId;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }
}
