#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES uTextureSampler;
varying vec2 vTextureCoord;
const highp vec3 W = vec3(0.2125, 0.7154, 0.0721);

void main() {
    vec4 vCameraColor = texture2D(uTextureSampler, vTextureCoord);
    float temp = dot(vCameraColor.rgb, W);
    gl_FragColor = vec4(vec3(temp), 1.0);
}
