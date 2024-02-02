package com.songyu.components.springboot.data;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.mybatisflex.core.keygen.IKeyGenerator;

/**
 * <p>
 * nano id 主键生成器
 * </p>
 *
 * @author songYu
 * @since 2023/9/18 9:53
 */
public class NanoIDKeyGenerator implements IKeyGenerator {

    @Override
    public Object generate(Object o, String s) {
        return NanoIdUtils.randomNanoId();
    }

}
