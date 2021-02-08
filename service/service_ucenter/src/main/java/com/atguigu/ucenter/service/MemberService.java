package com.atguigu.ucenter.service;

import com.atguigu.ucenter.entity.Member;
import com.atguigu.ucenter.entity.vo.LoginVo;
import com.atguigu.ucenter.entity.vo.RegisterVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author csp
 * @since 2021-01-26
 */
public interface MemberService extends IService<Member> {

    String login(LoginVo loginVo);

    void register(RegisterVo registerVo);

    Member getLoginInfo(String memberId);

    Member getByOpenid(String openid);

}
