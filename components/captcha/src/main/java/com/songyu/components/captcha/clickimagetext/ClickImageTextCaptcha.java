package com.songyu.components.captcha.clickimagetext;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.songyu.commonutils.CommonFileUtils;
import com.songyu.components.captcha.Captcha;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Base64;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;

/**
 * <p>
 * 图片文字点选验证码
 * </p>
 *
 * @author songYu
 * @since 2023/9/22 9:08
 */
public class ClickImageTextCaptcha extends Captcha {

    public ClickImageTextCaptcha(String seeds) {
        super(seeds);
    }

    @Override
    protected void initCaptcha() {
        // 创建图像缓存操作
        BufferedImage bufferedImage = new BufferedImage(300, 200, BufferedImage.TYPE_INT_RGB);
        // 获取画布
        Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
        // 读取背景文件
        BufferedImage read;
        Random random = new Random();
        File vcodeFileDir;
        try {
            URL resource = ClickImageTextCaptcha.class.getClassLoader().getResource("verify-code");
            if (Objects.isNull(resource)) {
                throw new FileNotFoundException("未找到 验证码背景图片 文件夹");
            }
            vcodeFileDir = new File(resource.getPath());
            File[] vcodeBgs = vcodeFileDir.listFiles();
            if (vcodeBgs == null || vcodeBgs.length == 0) {
                throw new FileNotFoundException("验证码背景图片中未找到背景图");
            }
            String vcodeFileName = String.format("vcode-bg-%d.png", random.nextInt(vcodeBgs.length - 1) + 1);
            File bgFile = new File(vcodeFileDir, vcodeFileName);
            read = ImageIO.read(bgFile);
        } catch (IOException e) {
            throw new RuntimeException("读取背景填充图片失败",e);
        }
        // 填充背景
        graphics.drawImage(read, 0, 0, 300, 200, (img, i, x, y, wid, hei) -> false);
        // 放置文字
        ClickImageTextPointsVerify verifyCodeImageTextPoint = new ClickImageTextPointsVerify();
        LinkedList<ClickImageTextPointsVerify.TextPoint> textPoints = new LinkedList<>();
        String vcodeTextJson = CommonFileUtils.readFileContentFromPaths(vcodeFileDir.getAbsolutePath(), "vcode-text.json");
        JSONObject jsonObject = JSONUtil.parseObj(vcodeTextJson);
        char[] chars = jsonObject.get(String.valueOf(random.nextInt(jsonObject.keySet().size()) + 1)).toString().toCharArray();
        LinkedList<Float> xStage = new LinkedList<>();
        LinkedList<Float> yStage = new LinkedList<>();
        for (char aChar : chars) {
            ClickImageTextPointsVerify.TextPoint textPoint = new ClickImageTextPointsVerify.TextPoint();
            textPoint.setText(String.valueOf(aChar));
            float x = randomInScopeWithSlices(xStage, 300, 30, random);
            float y = randomInScopeWithSlices(yStage, 200, 30 * 0.75, random);
            textPoint.setX(x);
            textPoint.setY(y);
            textPoints.add(textPoint);
        }
        textPoints.forEach(textPoint -> {
            Font font = new Font("楷体", Font.BOLD, 30); // 创建字体对象
            graphics.setFont(font); // 设置字体
            GradientPaint paint = new GradientPaint(20, 20, Color.BLUE, 100, 120, Color.RED, true);
            graphics.setPaint(paint);// 设置渐变
            graphics.drawString(textPoint.getText(), textPoint.getX(), textPoint.getY());
        });
        // 输出图片
        graphics.dispose();
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, "png", outputStream);
            outputStream.flush();
            String imgBase64Str = Base64.getEncoder().encodeToString(outputStream.toByteArray());
            base64BGStr = String.format("data:image/gif;base64,%s", imgBase64Str);
        } catch (IOException e) {
            throw new RuntimeException("输出文件到输出流失败");
        }
        verifyCodeImageTextPoint.setTextPoints(textPoints);
        verifyInfo = verifyCodeImageTextPoint;
    }

    /**
     * 按slice层级插入新的文字位置
     *
     * @param slices   slice 层级
     * @param maxValue 最大范围值
     * @param stage    最小层值
     * @param random   随机数器
     * @return 新的文字位置
     */
    private float randomInScopeWithSlices(LinkedList<Float> slices, int maxValue, double stage, Random random) {
        float x = 0f;
        if (!slices.isEmpty()) {
            float start = 0f;
            for (int i = 0; i < slices.size(); i++) {
                float current = slices.get(i);
                if ((x = randomInScope(start, current, stage, random)) > 0) {
                    slices.add(i, x);
                    break;
                } else {
                    start = current;
                }
            }
            if (x < 0) {
                x = randomInScope(start, maxValue, stage, random);
                slices.addLast(x);
            }
        } else {
            x = randomInScope(0, maxValue, stage, random);
            slices.addLast(x);
        }
        return x;
    }

    /**
     * 随机一个区间内的数
     *
     * @param start  区间开始
     * @param stop   区间结束
     * @param stage  层
     * @param random 随机数器
     * @return 随机结果
     */
    private float randomInScope(float start, float stop, double stage, Random random) {
        if (stop - start <= stage * 2) return -1f;
        return (int) (random.nextInt((int) (stop - start - (stage * 2))) + (stage + start));
    }

}
