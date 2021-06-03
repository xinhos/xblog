package cn.xinhos.entry;

import lombok.*;

import java.io.Serializable;
import java.sql.Date;

@Data
public class Blog {
    private Integer id;
    private Integer categoryId;
    private Integer citeId;
    private String title;
    private String abstracts;
    private String cover;
    private Date publishDate;
    private Date modifiedDate;
    private String fileName;
    private Integer visitNum;
    private Integer commentNum;
    private Integer state;
}
