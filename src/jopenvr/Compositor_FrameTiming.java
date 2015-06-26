package jopenvr;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : /home/phr00t/OpenVR/headers/openvr_capi.h</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class Compositor_FrameTiming extends Structure {
	public int size;
	public double frameStart;
	public float frameVSync;
	public int droppedFrames;
	public int frameIndex;
	/** C type : TrackedDevicePose_t */
	public TrackedDevicePose_t pose;
	public Compositor_FrameTiming() {
		super();
	}
	protected List<? > getFieldOrder() {
		return Arrays.asList("size", "frameStart", "frameVSync", "droppedFrames", "frameIndex", "pose");
	}
	/** @param pose C type : TrackedDevicePose_t */
	public Compositor_FrameTiming(int size, double frameStart, float frameVSync, int droppedFrames, int frameIndex, TrackedDevicePose_t pose) {
		super();
		this.size = size;
		this.frameStart = frameStart;
		this.frameVSync = frameVSync;
		this.droppedFrames = droppedFrames;
		this.frameIndex = frameIndex;
		this.pose = pose;
	}
	public Compositor_FrameTiming(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends Compositor_FrameTiming implements Structure.ByReference {
		
	};
	public static class ByValue extends Compositor_FrameTiming implements Structure.ByValue {
		
	};
}