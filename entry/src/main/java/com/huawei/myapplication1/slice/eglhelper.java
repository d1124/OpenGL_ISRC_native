package com.huawei.myapplication1.slice;


import ohos.agp.graphics.Surface;
import ohos.agp.render.opengl.*;

public class eglhelper {
/**
 * @Author: DY
 * @Date: 2020/12/31
 */
    public void start(Surface surface) {

        //EGL
        mEgl=new EGL();
        //Display
        mEglDisplay = EGL.eglGetDisplay(EGL.EGL_DEFAULT_DISPLAY);
        //初始化
        int[] version = new int[2];
        int[] version2 = new int[2];
        if(!mEgl.eglInitialize(mEglDisplay, version,version2)) {
            throw new RuntimeException("eglInitialize failed");
        }
        //config
        int attribList[] = {
                EGL.EGL_RED_SIZE, 8,
                EGL.EGL_GREEN_SIZE, 8,
                EGL.EGL_BLUE_SIZE, 8,
                EGL.EGL_ALPHA_SIZE, 8,
                EGL.EGL_DEPTH_SIZE, 16,
                EGL.EGL_STENCIL_SIZE, 8,
                EGL.EGL_RENDERABLE_TYPE, 4,
                EGL.EGL_NONE};
        int[] num_config = new int[1];
        if (!mEgl.eglChooseConfig(mEglDisplay,attribList,null, 1, num_config)) {
            throw new IllegalArgumentException("eglChooseConfig failed");
        }

        int numConfigs = num_config[0];
        EGLConfig[] configs = new EGLConfig[numConfigs];
        if (!mEgl.eglChooseConfig(mEglDisplay,attribList,configs, numConfigs, num_config)) {
            throw new IllegalArgumentException("eglChooseConfig#2 failed");
        }
        mEglConfig=configs[0];

        //context
        int []contextAttribs = {
                //0x3098,2,
                EGL.EGL_NONE
        };
        mEglContext=EGL.eglCreateContext(mEglDisplay,mEglConfig,EGL.EGL_NO_CONTEXT,contextAttribs);
        int []sAttribs = {
                EGL.EGL_NONE};
        //surface
        mEglSurface = EGL.eglCreateWindowSurface(mEglDisplay,mEglConfig,surface,sAttribs);
        EGL.eglMakeCurrent(mEglDisplay,mEglSurface,mEglSurface,mEglContext);
    }


    public boolean swapBuffers() {
        if (mEgl != null) {
            return mEgl.eglSwapBuffers(mEglDisplay, mEglSurface);
        } else {
            throw new RuntimeException("egl is null ");
        }
    }

    public void destoryEgl(){
        if (mEgl != null) {
            mEgl.eglMakeCurrent(mEglDisplay, EGL.EGL_NO_SURFACE,
                    EGL.EGL_NO_SURFACE,
                    EGL.EGL_NO_CONTEXT);
            mEgl.eglDestroySurface(mEglDisplay, mEglSurface);
            mEglSurface = null;

            mEgl.eglDestroyContext(mEglDisplay, mEglContext);
            mEglContext = null;

            mEgl.eglTerminate(mEglDisplay);
            mEglDisplay = null;
            mEgl = null;
        }
    }
     EGL mEgl;
     EGLDisplay mEglDisplay;
     EGLSurface mEglSurface;
     EGLConfig mEglConfig;
     EGLContext mEglContext;

}