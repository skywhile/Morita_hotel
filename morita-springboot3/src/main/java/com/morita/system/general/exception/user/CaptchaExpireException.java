package com.morita.system.general.exception.user;

/**
 * 验证码失效异常类
 *
 * @author morita
 */
public class CaptchaExpireException extends UserException
{
    private static final long serialVersionUID = 1L;

    public CaptchaExpireException()
    {
        super("user.jcaptcha.expire", null);
    }
}
