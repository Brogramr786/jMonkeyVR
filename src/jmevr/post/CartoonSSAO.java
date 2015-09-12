/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package jmevr.post;

import com.jme3.asset.AssetManager;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture;
import java.io.IOException;
import java.util.ArrayList;

public class CartoonSSAO extends Filter{
    private Pass normalPass;
    private Vector3f frustumCorner;
    private Vector2f frustumNearFar;
    // Wide area occlusion settings
    private float sampleRadius = 3.0f;
    private float intensity = 10.2f;
    private float scale = 3.15f;
    private float bias = 0.025f;
    private float downsample = 1f;

    //private float downSampleFactor = 1f;
    RenderManager renderManager;
    ViewPort viewPort;

    /**
    * Create a Screen Space Ambient Occlusion Filter
    */
    public CartoonSSAO() {
        super("CartoonSSAO");
    }

    /**
    * Create a Screen Space Ambient Occlusion Filter
    * @param sampleRadius The radius of the area where random samples will be picked. default 3.0f
    * @param intensity intensity of the resulting AO. default 10.2f
    * @param scale distance between occluders and occludee. default 3.15f
    * @param bias the width of the occlusion cone considered by the occludee. default 0.025f
    * @param downsample factor to divide resolution by for filter, >1 increases speed but degrades quality
    */
    public CartoonSSAO(float sampleRadius, float intensity, float scale, float bias, float downsample) {
        this();
        this.sampleRadius = sampleRadius;
        this.intensity = intensity;
        this.scale = scale;
        this.bias = bias;
        this.downsample = downsample;
    }
    
    public CartoonSSAO(CartoonSSAO cloneFrom) {
        this();
        this.sampleRadius = cloneFrom.sampleRadius;
        this.intensity = cloneFrom.intensity;
        this.scale = cloneFrom.scale;
        this.bias = cloneFrom.bias;
    }

    @Override
    protected boolean isRequiresDepthTexture() {
        return true;
    }

    @Override
    protected void postQueue(RenderQueue renderQueue) {
        PreNormalCaching.getPreNormals(renderManager, normalPass, viewPort);
    }

    public void setDownsampling(float downsample) {
        this.downsample = downsample;
    }
    
    public float getDownsampling() {
        return this.downsample;
    }

    @Override
    protected Material getMaterial() {
        return material;
    }

    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
        this.renderManager = renderManager;
        this.viewPort = vp;

        int screenWidth = Math.round(w / downsample);
        int screenHeight = Math.round(h / downsample);

        normalPass = new Pass();
        normalPass.init(renderManager.getRenderer(), screenWidth, screenHeight, Format.RGBA8, Format.Depth);

        frustumNearFar = new Vector2f();

        float farY = (vp.getCamera().getFrustumTop() / vp.getCamera().getFrustumNear()) * vp.getCamera().getFrustumFar();
        float farX = farY * ((float) screenWidth / (float) screenHeight);
        frustumCorner = new Vector3f(farX, farY, vp.getCamera().getFrustumFar());
        frustumNearFar.x = vp.getCamera().getFrustumNear();
        frustumNearFar.y = vp.getCamera().getFrustumFar();

        //ssao Pass
        material = new Material(manager, "jmevr/shaders/CartoonSSAO.j3md");
        material.setTexture("Normals", normalPass.getRenderedTexture());

        material.setFloat("SampleRadius", sampleRadius);
        material.setFloat("Intensity", intensity);
        material.setFloat("Scale", scale);
        material.setFloat("Bias", bias);

        material.setVector3("FrustumCorner", frustumCorner);
        material.setVector2("FrustumNearFar", frustumNearFar);
    }

    /**
    * Return the wide area bias<br>
    * see {@link  #setBias(float bias)}
    * @return  bias
    */
    public float getBias() {
        return bias;
    }

    /**
    * Sets the the width of the wide area occlusion cone considered by the occludee default is 0.025f
    * @param bias
    */
    public void setBias(float bias) {
        this.bias = bias;
        if (material != null) {
            material.setFloat("Bias", bias);
        }
    }

    /**
    /**
    * returns the ambient occlusion intensity
    * @return intensity
    */
    public float getIntensity() {
        return intensity;
    }

    /**
    * Sets the Ambient occlusion intensity default is 10.2f
    * @param intensity
    */
    public void setIntensity(float intensity) {
        this.intensity = intensity;
        if (material != null) {
            material.setFloat("Intensity", intensity);
        }
    }


    /**
    * returns the sample radius<br>
    * see {link setSampleRadius(float sampleRadius)}
    * @return the sample radius
    */
    public float getSampleRadius() {
        return sampleRadius;
    }

    /**
    * Sets the radius of the area where random samples will be picked dafault 3.0f
    * @param sampleRadius
    */
    public void setSampleRadius(float sampleRadius) {
        this.sampleRadius = sampleRadius;
        if (material != null) {
            material.setFloat("SampleRadius", sampleRadius);
        }
    }

    /**
    * returns the scale<br>
    * see {@link #setScale(float scale)}
    * @return scale
    */
    public float getScale() {
        return scale;
    }

    /**
    *
    * Returns the distance between occluders and occludee. default 3.15f
    * @param scale
    */
    public void setScale(float scale) {
        this.scale = scale;
        if (material != null) {
            material.setFloat("Scale", scale);
        }
    }


    public void scaleSettings(float aoScale) {
        setBias(getBias()*aoScale);
        setIntensity(getIntensity()*aoScale);
        setScale(getScale()*aoScale);
        setScale(getScale()*aoScale);
        setSampleRadius(getSampleRadius()*aoScale);
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);

        OutputCapsule oc = ex.getCapsule(this);
            oc.write(sampleRadius, "sampleRadius", 3.0f);
            oc.write(intensity, "intensity", 10.2f);
            oc.write(scale, "scale", 3.15f);
            oc.write(bias, "bias", 0.025f);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        sampleRadius = ic.readFloat("sampleRadius", 3.0f);
        intensity = ic.readFloat("intensity", 10.2f);
        scale = ic.readFloat("scale", 3.15f);
        bias = ic.readFloat("bias", 0.025f);
    }
}