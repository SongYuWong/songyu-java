package com.songyu.domains.staticpage.entity;

import com.songyu.components.springboot.data.AbstractTable;
import com.songyu.components.springboot.exception.IllegalInfoException;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 静态页面对象
 * </p>
 *
 * @author songYu
 * @since 2024/1/23 18:25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StaticPage extends AbstractTable {

    /**
     * 静态页编码
     */
    private String pageCode;

    /**
     * 页面标题
     */
    private String title;

    /**
     * 原始生成的 html （用于编辑）
     */
    private String html;

    /**
     * 格式化页面展示用的 html （用于展示）
     */
    private String formattedHtml;

    @Override
    public void checkIfPrimaryInfoComplete() throws IllegalInfoException {

    }

}
