package com.huawei.myapplication1.slice;

import android.opengl.GLES10;
import com.huawei.myapplication1.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Text;
import ohos.agp.components.surfaceprovider.SurfaceProvider;
import ohos.agp.graphics.SurfaceOps;
import ohos.agp.render.opengl.EGLConfig;
import ohos.agp.render.opengl.GLES1X;
import ohos.agp.render.opengl.GLES30;
import ohos.app.Context;
import ohos.opengl.ETC1;
import ohos.opengl.GLSurfaceProvider;
import ohos.opengl.GLU;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class MainAbilitySlice extends AbilitySlice {
    /**
     * @Author: DY
     * @Date: 2020/12/31
     */

    //显示GLSurfaceProvider的Layout
    DirectionalLayout myLayout = new DirectionalLayout(this);

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        //1、GLSurfaceProvider的示例（红色三角形）
//        DirectionalLayout.LayoutConfig ps = new DirectionalLayout.LayoutConfig(800, 800);
//        myLayout.setLayoutConfig(ps);
//        GlSurfaceProvider glSurfaceProvider = new GlSurfaceProvider(this);
//        glSurfaceProvider.pinToZTop(true);
//        myLayout.addComponent(glSurfaceProvider);
//        super.setUIContent(myLayout);
        //2、EGL的示例(如果无法显示可以尝试测试ETC1和EGL，取消注释即可)
        super.setUIContent(ResourceTable.Layout_ability_main);//切换界面

        Text a=(Text) findComponentById(ResourceTable.Id_text_helloworld);//本行和下一行为ETC1测试
        a.setText("测试ETC1中的方法："+ ETC1.getEncodedDataSize(10,5));//输出48则成功

        initView();//(EGL测试)中间将显示一个紫色正方形。
    }
    //创建GLSurfaceProvider
    public class GlSurfaceProvider extends GLSurfaceProvider {
        private final GlRenderer mRenderer;
        public GlSurfaceProvider(Context context) {
            super(context);
            mRenderer = new GlRenderer();
            setRenderer(mRenderer);
        }
    }
    //创建Renderer
    public class GlRenderer implements GLSurfaceProvider.Renderer {
        //参考https://blog.csdn.net/gigi_/article/details/53558906
        //创建GLES10
        GLES10 gl2 = new GLES10();
        @Override
        public void onSurfaceCreated(GLES1X gl, EGLConfig config) {

            //设置背景色,透明度1为完全不透明
            gl2.glClearColor(0f, 0, 0f, 1f);
            //启用客户端状态,启用顶点缓冲区
            gl2.glEnableClientState(GLES10.GL_VERTEX_ARRAY);
        }

        //表层size改变时，即画面的大小改变时调用
        @Override
        public void onSurfaceChanged(GLES1X gl, int width, int height) {
            //设置视口,输出画面的区域,在控件的什么区域来输出,x,y是左下角坐标
            gl2.glViewport(0, 0, width, height);
            float ratio =(float) width /(float) height;

            //矩阵模式,投影矩阵,openGL基于状态机
            gl2.glMatrixMode(GLES10.GL_PROJECTION);
            //加载单位矩阵
            gl2.glLoadIdentity();
            //平截头体
            gl2.glFrustumf(-1f, 1f, -ratio, ratio, 3, 7);
        }

        //绘图
        @Override
        public void onDrawFrame(GLES1X gl) {
            //清除颜色缓冲区
            gl2.glClear(gl2.GL_COLOR_BUFFER_BIT);
            //模型视图矩阵
            gl2.glMatrixMode(GLES10.GL_MODELVIEW);
            //操作新矩阵要先清0,加载单位矩阵
            gl2.glLoadIdentity();
            //眼睛放的位置,eyes,eyey,eyez眼球(相机)的坐标
            //centerx,y,z镜头朝向，眼球的观察点
            //upx,upy,upz:指定眼球向上的向量,眼睛正着看
            GLU.gluLookAt(gl2, 0, 0, 5, 0, 0, 0,
                    0,1,0);
            //画三角形
            //三角形的坐标
            float[] coords = {
                    0f,0.5f,0f,
                    -0.5f,-0.5f,1f,
                    0.5f,-0.5f,0f,
            };
            //分配字节缓冲区空间,存放顶点坐标数据,将坐标放在缓冲区中
            ByteBuffer ibb = ByteBuffer.allocateDirect(coords.length * 4);  //直接分配字节的大小
            //设置顺序(本地顺序)
            ibb.order(ByteOrder.nativeOrder());
            //放置顶点坐标数组
            FloatBuffer fbb =  ibb.asFloatBuffer();
            fbb.put(coords);
            //定位指针的位置,从该位置开始读取顶点数据
            ibb.position(0);
            //设置绘图的颜色，红色
            gl2.glColor4f(1f, 0f, 0f, 1f);
            //3:3维点,使用三个坐标值表示一个点
            //type:每个点的数据类型
            //stride:0,上,点的跨度
            //ibb:指定顶点缓冲区
            gl2.glVertexPointer(3, gl2.GL_FLOAT, 0, ibb);
            //绘制三角形数组
            gl2.glDrawArrays(gl2.GL_TRIANGLES, 0, 3);   //count以点的数量来算,为3
        }


    }

    //EGL示例代码
    private SurfaceProvider surfaceProvider;//创建SurfaceProvider
    //初始化
    private void initView() {
        surfaceProvider =(SurfaceProvider)findComponentById(ResourceTable.Id_sv);
        surfaceProvider.pinToZTop(true);
        surfaceProvider.getSurfaceOps().get().addCallback(new SurfaceOps.Callback() {
            eglhelper eglHelper = new eglhelper();
            @Override
            public void surfaceCreated(SurfaceOps surfaceOps) {
            }

            @Override
            public void surfaceChanged(SurfaceOps surfaceOps, int i, int i1, int i2) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        eglHelper.start(surfaceOps.getSurface());
                        while (true) {
                            GLES30.glViewport(0, 0, i1, i2);
                            GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
                            GLES30.glClearColor(1.0f, 0.0f, 1.0f, 0.0f);
                            eglHelper.swapBuffers();
                            try {
                                Thread.sleep(16);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }.start();
            }
            @Override
            public void surfaceDestroyed(SurfaceOps surfaceOps) {
                eglHelper.destoryEgl();
            }
        });
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

}