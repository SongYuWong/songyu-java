package com.songyu.components.captcha.clickimagetext;

import com.songyu.commonutils.CommonArrayUtils;
import com.songyu.commonutils.CommonCollectionUtils;
import com.songyu.components.captcha.VerifyInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.LinkedList;

/**
 * <p>
 * 文字点选验证码验证类
 * </p>
 *
 * @author songYu
 * @since 2023/9/22 16:02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ClickImageTextPointsVerify extends VerifyInfo<ClickImageTextPointsVerify.TextPoint[]> {

    private LinkedList<TextPoint> textPoints;

    @Override
    public boolean verifyCaptcha(TextPoint[] textPoints) {
        if (CommonArrayUtils.isEmpty(textPoints)) {
            throw new RuntimeException("校验验证码顺序文字点集不能为空");
        }
        for (int i = 0; i < this.textPoints.size(); i++) {
            TextPoint textPoint = this.textPoints.get(i);
            TextPoint textPointCustom = textPoints[i];
            if (Math.abs(textPoint.getX() - textPointCustom.getX()) > 30 * 1.5
                    || Math.abs(textPoint.getY() - textPointCustom.getY()) > 30 * 1.5) {
                return false;
            }
        }
        return true;
    }

    @Data
    public static class TextPoint {

        private Float x;

        private Float y;

        private String text;

    }

}
