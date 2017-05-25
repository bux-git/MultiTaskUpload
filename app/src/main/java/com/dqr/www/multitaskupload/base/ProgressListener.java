/*
 * Copyright (c) 2015.
 * 湖南球谱科技有限公司版权所有
 * Hunan Qiupu Technology Co., Ltd. all rights reserved.
 */

package com.dqr.www.multitaskupload.base;

public interface ProgressListener {
        void progress(long bytesRead, long contentLength, boolean done);

}
