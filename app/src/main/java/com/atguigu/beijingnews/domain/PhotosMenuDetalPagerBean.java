package com.atguigu.beijingnews.domain;

import java.util.List;

/**
 * 作者：杨光福 on 2016/6/7 14:10
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：图组页面的数据
 */
public class PhotosMenuDetalPagerBean {

    /**
     * countcommenturl : http://zhbj.qianlong.com/client/content/countComment/
     * more : http://zhbj.qianlong.com/static/api/news/10003/list_2.json
     * news : [{"comment":true,"commentlist":"http://zhbj.qianlong.com/static/api/news/10003/72/82772/comment_1.json","commenturl":"http://zhbj.qianlong.com/client/user/newComment/82772","id":82772,"largeimage":"http://zhbj.qianlong.com/static/images/2014/11/07/70/476518773M7R.jpg","listimage":"http://10.0.2.2:8080/zhbj/photos/images/46728356JDGO.jpg","pubdate":"2014-11-07 11:40","smallimage":"http://zhbj.qianlong.com/static/images/2014/11/07/79/485753989TVL.jpg","title":"北京·APEC绚丽之夜","type":"news","url":"http://zhbj.qianlong.com/static/html/2014/11/07/7743665E4E6B10766F26.html"},{"comment":true,"commentlist":"http://zhbj.qianlong.com/static/api/news/10003/99/77099/comment_1.json","commenturl":"http://zhbj.qianlong.com/client/user/newComment/77099","id":77099,"largeimage":"http://zhbj.qianlong.com/static/images/2014/10/16/62/47651877483X.jpg","listimage":"http://10.0.2.2:8080/zhbj/photos/images/46728356SFJE.jpg","pubdate":"2014-10-16 11:05","smallimage":"http://zhbj.qianlong.com/static/images/2014/10/16/51/485753981NU5.jpg","title":"最美公路 难以想象","type":"news","url":"http://zhbj.qianlong.com/static/html/2014/10/16/764D6855406219716C2E.html"},{"comment":true,"commentlist":"http://zhbj.qianlong.com/static/api/news/10003/66/69866/comment_1.json","commenturl":"http://zhbj.qianlong.com/client/user/newComment/69866","id":69866,"largeimage":"http://zhbj.qianlong.com/static/images/2014/09/15/64/47651877KF8W.jpg","listimage":"http://10.0.2.2:8080/zhbj/photos/images/46728356YLZ2.jpg","pubdate":"2014-09-15 11:26","smallimage":"http://zhbj.qianlong.com/static/images/2014/09/15/25/485753981N83.jpg","title":"南宁特警主题海报 炫似大片","type":"news","url":"http://zhbj.qianlong.com/static/html/2014/09/15/764C6F5C4862187F6825.html"}]
     * title : 组图
     * topic : []
     */

    private DataEntity data;
    /**
     * data : {"countcommenturl":"http://zhbj.qianlong.com/client/content/countComment/","more":"http://zhbj.qianlong.com/static/api/news/10003/list_2.json","news":[{"comment":true,"commentlist":"http://zhbj.qianlong.com/static/api/news/10003/72/82772/comment_1.json","commenturl":"http://zhbj.qianlong.com/client/user/newComment/82772","id":82772,"largeimage":"http://zhbj.qianlong.com/static/images/2014/11/07/70/476518773M7R.jpg","listimage":"http://10.0.2.2:8080/zhbj/photos/images/46728356JDGO.jpg","pubdate":"2014-11-07 11:40","smallimage":"http://zhbj.qianlong.com/static/images/2014/11/07/79/485753989TVL.jpg","title":"北京·APEC绚丽之夜","type":"news","url":"http://zhbj.qianlong.com/static/html/2014/11/07/7743665E4E6B10766F26.html"},{"comment":true,"commentlist":"http://zhbj.qianlong.com/static/api/news/10003/99/77099/comment_1.json","commenturl":"http://zhbj.qianlong.com/client/user/newComment/77099","id":77099,"largeimage":"http://zhbj.qianlong.com/static/images/2014/10/16/62/47651877483X.jpg","listimage":"http://10.0.2.2:8080/zhbj/photos/images/46728356SFJE.jpg","pubdate":"2014-10-16 11:05","smallimage":"http://zhbj.qianlong.com/static/images/2014/10/16/51/485753981NU5.jpg","title":"最美公路 难以想象","type":"news","url":"http://zhbj.qianlong.com/static/html/2014/10/16/764D6855406219716C2E.html"},{"comment":true,"commentlist":"http://zhbj.qianlong.com/static/api/news/10003/66/69866/comment_1.json","commenturl":"http://zhbj.qianlong.com/client/user/newComment/69866","id":69866,"largeimage":"http://zhbj.qianlong.com/static/images/2014/09/15/64/47651877KF8W.jpg","listimage":"http://10.0.2.2:8080/zhbj/photos/images/46728356YLZ2.jpg","pubdate":"2014-09-15 11:26","smallimage":"http://zhbj.qianlong.com/static/images/2014/09/15/25/485753981N83.jpg","title":"南宁特警主题海报 炫似大片","type":"news","url":"http://zhbj.qianlong.com/static/html/2014/09/15/764C6F5C4862187F6825.html"}],"title":"组图","topic":[]}
     * retcode : 200
     */

    private int retcode;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public DataEntity getData() {
        return data;
    }

    public int getRetcode() {
        return retcode;
    }

    public static class DataEntity {
        private String countcommenturl;
        private String more;
        private String title;
        /**
         * comment : true
         * commentlist : http://zhbj.qianlong.com/static/api/news/10003/72/82772/comment_1.json
         * commenturl : http://zhbj.qianlong.com/client/user/newComment/82772
         * id : 82772
         * largeimage : http://zhbj.qianlong.com/static/images/2014/11/07/70/476518773M7R.jpg
         * listimage : http://10.0.2.2:8080/zhbj/photos/images/46728356JDGO.jpg
         * pubdate : 2014-11-07 11:40
         * smallimage : http://zhbj.qianlong.com/static/images/2014/11/07/79/485753989TVL.jpg
         * title : 北京·APEC绚丽之夜
         * type : news
         * url : http://zhbj.qianlong.com/static/html/2014/11/07/7743665E4E6B10766F26.html
         */

        private List<NewsData> news;
        private List<?> topic;

        public void setCountcommenturl(String countcommenturl) {
            this.countcommenturl = countcommenturl;
        }

        public void setMore(String more) {
            this.more = more;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setNews(List<NewsData> news) {
            this.news = news;
        }

        public void setTopic(List<?> topic) {
            this.topic = topic;
        }

        public String getCountcommenturl() {
            return countcommenturl;
        }

        public String getMore() {
            return more;
        }

        public String getTitle() {
            return title;
        }

        public List<NewsData> getNews() {
            return news;
        }

        public List<?> getTopic() {
            return topic;
        }

        public static class NewsData {
            private boolean comment;
            private String commentlist;
            private String commenturl;
            private int id;
            private String largeimage;
            private String listimage;
            private String pubdate;
            private String smallimage;
            private String title;
            private String type;
            private String url;

            public void setComment(boolean comment) {
                this.comment = comment;
            }

            public void setCommentlist(String commentlist) {
                this.commentlist = commentlist;
            }

            public void setCommenturl(String commenturl) {
                this.commenturl = commenturl;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setLargeimage(String largeimage) {
                this.largeimage = largeimage;
            }

            public void setListimage(String listimage) {
                this.listimage = listimage;
            }

            public void setPubdate(String pubdate) {
                this.pubdate = pubdate;
            }

            public void setSmallimage(String smallimage) {
                this.smallimage = smallimage;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public void setType(String type) {
                this.type = type;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public boolean isComment() {
                return comment;
            }

            public String getCommentlist() {
                return commentlist;
            }

            public String getCommenturl() {
                return commenturl;
            }

            public int getId() {
                return id;
            }

            public String getLargeimage() {
                return largeimage;
            }

            public String getListimage() {
                return listimage;
            }

            public String getPubdate() {
                return pubdate;
            }

            public String getSmallimage() {
                return smallimage;
            }

            public String getTitle() {
                return title;
            }

            public String getType() {
                return type;
            }

            public String getUrl() {
                return url;
            }
        }
    }
}
