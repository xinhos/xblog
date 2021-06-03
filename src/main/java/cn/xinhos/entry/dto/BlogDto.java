package cn.xinhos.entry.dto;

import cn.xinhos.entry.Blog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor
@Data public class BlogDto extends Blog{
    private List<String> marks;

    public BlogDto(Blog aBlog) {
        init(aBlog);
    }

    public BlogDto(Blog aBlog, List<String> marks) {
        this(aBlog);
        this.marks = marks;
    }

    public BlogDto reset(Blog aBlog) {
        if (aBlog != null) {
            init(aBlog);
        }
        return this;
    }

    private void init(Blog aBlog) {
        this.setId(aBlog.getId());
        this.setCategoryId(aBlog.getCategoryId());
        this.setCiteId(aBlog.getCiteId());
        this.setTitle(aBlog.getTitle());
        this.setAbstracts(aBlog.getAbstracts());
        this.setCover(aBlog.getCover());
        this.setPublishDate(aBlog.getPublishDate());
        this.setModifiedDate(aBlog.getModifiedDate());
        this.setFileName(aBlog.getFileName());
        this.setVisitNum(aBlog.getVisitNum());
        this.setCommentNum(aBlog.getCommentNum());
        this.setState(aBlog.getState());
    }
}
