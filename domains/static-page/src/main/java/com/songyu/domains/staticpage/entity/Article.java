package com.songyu.domains.staticpage.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 文章对象
 * </p>
 *
 * @author songYu
 * @since 2024/1/23 18:26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Article extends StaticPage {

    /**
     * 文章摘要
     */
    private String summary;

}
