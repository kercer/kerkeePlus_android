package com.kercer.kerkeesdk.deploy;

import com.kercer.kernet.http.error.KCNetError;

/**
 * Created by zihong on 16/5/4.
 */
public class KCDeployError extends KCNetError
{
    public KCDeployError(String exceptionMessage)
    {
        super(exceptionMessage);
    }

    public KCDeployError(String exceptionMessage, Throwable reason)
    {
        super(exceptionMessage, reason);
    }

    public KCDeployError(Throwable cause)
    {
        super(cause);
    }
}
