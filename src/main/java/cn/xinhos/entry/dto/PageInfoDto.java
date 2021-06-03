package cn.xinhos.entry.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @ClassName: PageInfoDto
 * @Description: 前端分页信息
 * @author: xinhos
 * @data: 2021-05-22-20:35
 */
@NoArgsConstructor @AllArgsConstructor
@Data public class PageInfoDto {
    private Integer dataSize;  // 全部记录数量
    private Integer pageSize;  // 一页的记录数量
    private Integer curPage;   // 当前处于哪一页
    private Integer pageNum;   // 一共多少页（得到前两个算出来）
}
