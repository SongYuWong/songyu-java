package com.songyu.domains.auth.aggregate;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.core.paginate.Page;
import com.songyu.domains.auth.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户分页
 * </p>
 *
 * @author songYu
 * @since 2024/1/11 16:06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserPage extends User {

    /**
     * 用户分页信息
     */
    @Column(ignore = true)
    private Page<UserPage> page;

}
