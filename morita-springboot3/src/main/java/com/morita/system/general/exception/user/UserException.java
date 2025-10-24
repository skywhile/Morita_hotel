package com.morita.system.general.exception.user;

import com.morita.system.general.exception.base.BaseException;

/**
 * 用户信息异常类
 *
 * @author morita
 */
public class UserException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }
}
